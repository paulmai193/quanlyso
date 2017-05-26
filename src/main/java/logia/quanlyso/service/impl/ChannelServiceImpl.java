package logia.quanlyso.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.domain.Channel;
import logia.quanlyso.repository.ChannelRepository;
import logia.quanlyso.service.ChannelService;
import logia.quanlyso.service.dto.ChannelDTO;
import logia.quanlyso.service.mapper.ChannelMapper;

/**
 * Service Implementation for managing Channel.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class ChannelServiceImpl implements ChannelService{

    /** The log. */
    private final Logger log = LoggerFactory.getLogger(ChannelServiceImpl.class);
    
    /** The channel repository. */
    private final ChannelRepository channelRepository;

    /** The channel mapper. */
    private final ChannelMapper channelMapper;

    /**
     * Instantiates a new channel service impl.
     *
     * @param channelRepository the channel repository
     * @param channelMapper the channel mapper
     */
    public ChannelServiceImpl(ChannelRepository channelRepository, ChannelMapper channelMapper) {
        this.channelRepository = channelRepository;
        this.channelMapper = channelMapper;
    }

    /**
     * Save a channel.
     *
     * @param channelDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ChannelDTO save(ChannelDTO channelDTO) {
        log.debug("Request to save Channel : {}", channelDTO);
        Channel channel = channelMapper.toEntity(channelDTO);
        channel = channelRepository.save(channel);
        ChannelDTO result = channelMapper.toDto(channel);
        return result;
    }

    /**
     *  Get all the channels.
     *  
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ChannelDTO> findAll() {
        log.debug("Request to get all Channels");
        List<ChannelDTO> result = channelRepository.findAll().stream()
            .map(channelMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one channel by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ChannelDTO findOne(Long id) {
        log.debug("Request to get Channel : {}", id);
        Channel channel = channelRepository.findOne(id);
        ChannelDTO channelDTO = channelMapper.toDto(channel);
        return channelDTO;
    }

    /**
     *  Delete the  channel by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Channel : {}", id);
        channelRepository.delete(id);
    }
}
