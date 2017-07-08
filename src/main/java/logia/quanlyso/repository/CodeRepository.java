package logia.quanlyso.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import logia.quanlyso.domain.Channel;
import logia.quanlyso.domain.Code;

/**
 * Spring Data JPA repository for the Code entity.
 */
@SuppressWarnings("unused")
public interface CodeRepository extends JpaRepository<Code, Long> {

	/**
	 * Find all by open day.
	 *
	 * @param channels the channels
	 * @param openDate the open date
	 * @return the list
	 */
	List<Code> findAllByChannelsAndOpenDate(Channel channels, ZonedDateTime openDate);
}
