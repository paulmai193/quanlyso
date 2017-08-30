package logia.quanlyso.service.impl;

import logia.quanlyso.domain.Channel;
import logia.quanlyso.repository.ChannelRepository;
import logia.quanlyso.service.ChannelService;
import logia.quanlyso.service.dto.ChannelDTO;
import logia.quanlyso.service.mapper.ChannelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Channel.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class ChannelServiceImpl implements ChannelService {

	/** The log. */
	private final Logger			log	= LoggerFactory.getLogger(ChannelServiceImpl.class);

	/** The channel repository. */
	private final ChannelRepository	channelRepository;

	/** The channel mapper. */
	private final ChannelMapper		channelMapper;

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
		this.log.debug("Request to save Channel : {}", channelDTO);
		Channel channel = this.channelMapper.toEntity(channelDTO);
		channel = this.channelRepository.save(channel);
		ChannelDTO result = this.channelMapper.toDto(channel);
		return result;
	}

	/**
	 * Get all the channels.
	 *
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ChannelDTO> findAll() {
		this.log.debug("Request to get all Channels");
		List<ChannelDTO> result = this.channelRepository.findAll().stream().map(this.channelMapper::toDto)
				.collect(Collectors.toCollection(LinkedList::new));

		return result;
	}

	/**
	 * Get one channel by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public ChannelDTO findOne(Long id) {
		this.log.debug("Request to get Channel : {}", id);
		Channel channel = this.channelRepository.findOne(id);
		ChannelDTO channelDTO = this.channelMapper.toDto(channel);
		return channelDTO;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see logia.quanlyso.service.ChannelService#findAllByOpenDay(logia.quanlyso.service.dto.
	 * ChannelOpenDay)
	 */
	@Override
	public List<ChannelDTO> findAllByOpenDay(DayOfWeek openDay) {
		this.log.debug("Request to get Channel by open day: {}", openDay);
		List<Channel> result;
		switch (openDay) {
			case SUNDAY:
				result = this.channelRepository.findAllBySunday();
				break;

			case MONDAY:
				result = this.channelRepository.findAllByMonday();
				break;

			case TUESDAY:
				result = this.channelRepository.findAllByTuesday();
				break;

			case WEDNESDAY:
				result = this.channelRepository.findAllByWednesday();
				break;

			case THURSDAY:
				result = this.channelRepository.findAllByThursday();
				break;

			case FRIDAY:
				result = this.channelRepository.findAllByFriday();
				break;

			case SATURDAY:
				result = this.channelRepository.findAllBySaturday();
				break;

			default:
				result = new ArrayList<>(0);
				break;
		}
		return result.stream().map(this.channelMapper::toDto)
				.collect(Collectors.toCollection(LinkedList::new));
	}

	/**
	 * Delete the channel by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		this.log.debug("Request to delete Channel : {}", id);
		this.channelRepository.delete(id);
	}
}
