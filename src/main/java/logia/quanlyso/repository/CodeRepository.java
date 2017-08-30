package logia.quanlyso.repository;

import logia.quanlyso.domain.Channel;
import logia.quanlyso.domain.Code;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

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
