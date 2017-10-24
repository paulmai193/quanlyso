/*
 * 
 */
package logia.quanlyso.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Persist AuditEvent managed by the Spring Boot actuator.
 *
 * @see org.springframework.boot.actuate.audit.AuditEvent
 */
@Entity
@Table(name = "jhi_persistent_audit_event")
public class PersistentAuditEvent implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_id")
	private Long id;

	/** The principal. */
	@NotNull
	@Column(nullable = false)
	private String principal;

	/** The audit event date. */
	@Column(name = "event_date")
	private LocalDateTime auditEventDate;

	/** The audit event type. */
	@Column(name = "event_type")
	private String auditEventType;

	/** The data. */
	@ElementCollection
	@MapKeyColumn(name = "name")
	@Column(name = "value")
	@CollectionTable(name = "jhi_persistent_audit_evt_data", joinColumns = @JoinColumn(name = "event_id"))
	private Map<String, String> data = new HashMap<>();

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the principal.
	 *
	 * @return the principal
	 */
	public String getPrincipal() {
		return this.principal;
	}

	/**
	 * Sets the principal.
	 *
	 * @param principal
	 *            the new principal
	 */
	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	/**
	 * Gets the audit event date.
	 *
	 * @return the audit event date
	 */
	public LocalDateTime getAuditEventDate() {
		return this.auditEventDate;
	}

	/**
	 * Sets the audit event date.
	 *
	 * @param auditEventDate
	 *            the new audit event date
	 */
	public void setAuditEventDate(LocalDateTime auditEventDate) {
		this.auditEventDate = auditEventDate;
	}

	/**
	 * Gets the audit event type.
	 *
	 * @return the audit event type
	 */
	public String getAuditEventType() {
		return this.auditEventType;
	}

	/**
	 * Sets the audit event type.
	 *
	 * @param auditEventType
	 *            the new audit event type
	 */
	public void setAuditEventType(String auditEventType) {
		this.auditEventType = auditEventType;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Map<String, String> getData() {
		return this.data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data
	 *            the data
	 */
	public void setData(Map<String, String> data) {
		this.data = data;
	}
}
