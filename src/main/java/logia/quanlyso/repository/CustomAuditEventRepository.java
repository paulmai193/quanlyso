package logia.quanlyso.repository;

import logia.quanlyso.config.Constants;
import logia.quanlyso.config.audit.AuditEventConverter;
import logia.quanlyso.domain.PersistentAuditEvent;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * An implementation of Spring Boot's AuditEventRepository.
 *
 * @author Dai Mai
 */
@Repository
public class CustomAuditEventRepository implements AuditEventRepository {

	/** The Constant AUTHORIZATION_FAILURE. */
	private static final String						AUTHORIZATION_FAILURE	= "AUTHORIZATION_FAILURE";

	/** The persistence audit event repository. */
	private final PersistenceAuditEventRepository	persistenceAuditEventRepository;

	/** The audit event converter. */
	private final AuditEventConverter				auditEventConverter;

	/**
	 * Instantiates a new custom audit event repository.
	 *
	 * @param persistenceAuditEventRepository the persistence audit event repository
	 * @param auditEventConverter the audit event converter
	 */
	public CustomAuditEventRepository(
			PersistenceAuditEventRepository persistenceAuditEventRepository,
			AuditEventConverter auditEventConverter) {

		this.persistenceAuditEventRepository = persistenceAuditEventRepository;
		this.auditEventConverter = auditEventConverter;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.boot.actuate.audit.AuditEventRepository#find(java.util.Date)
	 */
	@Override
	public List<AuditEvent> find(Date after) {
		Iterable<PersistentAuditEvent> persistentAuditEvents = this.persistenceAuditEventRepository
				.findByAuditEventDateAfter(LocalDateTime.from(after.toInstant()));
		return this.auditEventConverter.convertToAuditEvent(persistentAuditEvents);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.boot.actuate.audit.AuditEventRepository#find(java.lang.String,
	 * java.util.Date)
	 */
	@Override
	public List<AuditEvent> find(String principal, Date after) {
		Iterable<PersistentAuditEvent> persistentAuditEvents;
		if (principal == null && after == null) {
			persistentAuditEvents = this.persistenceAuditEventRepository.findAll();
		}
		else if (after == null) {
			persistentAuditEvents = this.persistenceAuditEventRepository.findByPrincipal(principal);
		}
		else {
			persistentAuditEvents = this.persistenceAuditEventRepository
					.findByPrincipalAndAuditEventDateAfter(principal,
							LocalDateTime.from(after.toInstant()));
		}
		return this.auditEventConverter.convertToAuditEvent(persistentAuditEvents);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.boot.actuate.audit.AuditEventRepository#find(java.lang.String,
	 * java.util.Date, java.lang.String)
	 */
	@Override
	public List<AuditEvent> find(String principal, Date after, String type) {
		Iterable<PersistentAuditEvent> persistentAuditEvents = this.persistenceAuditEventRepository
				.findByPrincipalAndAuditEventDateAfterAndAuditEventType(principal,
						LocalDateTime.from(after.toInstant()), type);
		return this.auditEventConverter.convertToAuditEvent(persistentAuditEvents);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.boot.actuate.audit.AuditEventRepository#add(org.springframework.boot.
	 * actuate.audit.AuditEvent)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void add(AuditEvent event) {
		if (!CustomAuditEventRepository.AUTHORIZATION_FAILURE.equals(event.getType())
				&& !Constants.ANONYMOUS_USER.equals(event.getPrincipal())) {

			PersistentAuditEvent persistentAuditEvent = new PersistentAuditEvent();
			persistentAuditEvent.setPrincipal(event.getPrincipal());
			persistentAuditEvent.setAuditEventType(event.getType());
			Instant instant = Instant.ofEpochMilli(event.getTimestamp().getTime());
			persistentAuditEvent
			.setAuditEventDate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
			persistentAuditEvent.setData(this.auditEventConverter.convertDataToStrings(event.getData()));
			this.persistenceAuditEventRepository.save(persistentAuditEvent);
		}
	}
}
