package logia.quanlyso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import logia.quanlyso.domain.Channel;

/**
 * Spring Data JPA repository for the Channel entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
public interface ChannelRepository extends JpaRepository<Channel,Long> {

}
