package logia.quanlyso.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import logia.quanlyso.domain.Channel;

/**
 * Spring Data JPA repository for the Channel entity.
 *
 * @author Dai Mai
 */
public interface ChannelRepository extends JpaRepository<Channel, Long> {

	/**
	 * Find all by sunday.
	 *
	 * @return the list
	 */
	@Query("from Channel channel where sunday = true")
	List<Channel> findAllBySunday();

	/**
	 * Find all by monday.
	 *
	 * @return the list
	 */
	@Query("from Channel channel where monday = true")
	List<Channel> findAllByMonday();

	/**
	 * Find all by tuesday.
	 *
	 * @return the list
	 */
	@Query("from Channel channel where tuesday = true")
	List<Channel> findAllByTuesday();

	/**
	 * Find all by wednesday.
	 *
	 * @return the list
	 */
	@Query("from Channel channel where wednesday = true")
	List<Channel> findAllByWednesday();

	/**
	 * Find all by thursday.
	 *
	 * @return the list
	 */
	@Query("from Channel channel where thursday = true")
	List<Channel> findAllByThursday();

	/**
	 * Find all by friday.
	 *
	 * @return the list
	 */
	@Query("from Channel channel where friday = true")
	List<Channel> findAllByFriday();

	/**
	 * Find all by saturday.
	 *
	 * @return the list
	 */
	@Query("from Channel channel where saturday = true")
	List<Channel> findAllBySaturday();

	/**
	 * Find one by code.
	 *
	 * @param __code the code
	 * @return the list
	 */
	Channel findOneByCode(String __code);
}
