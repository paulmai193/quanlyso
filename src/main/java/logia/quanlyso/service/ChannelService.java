package logia.quanlyso.service;

import java.util.List;

import logia.quanlyso.service.dto.ChannelDTO;

/**
 * Service Interface for managing Channel.
 *
 * @author Dai Mai
 */
public interface ChannelService {

    /**
     * Save a channel.
     *
     * @param channelDTO the entity to save
     * @return the persisted entity
     */
    ChannelDTO save(ChannelDTO channelDTO);

    /**
     *  Get all the channels.
     *  
     *  @return the list of entities
     */
    List<ChannelDTO> findAll();

    /**
     *  Get the "id" channel.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ChannelDTO findOne(Long id);

    /**
     *  Delete the "id" channel.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
