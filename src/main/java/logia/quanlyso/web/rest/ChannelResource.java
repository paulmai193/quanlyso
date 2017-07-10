package logia.quanlyso.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import logia.quanlyso.service.ChannelService;
import logia.quanlyso.service.dto.ChannelDTO;
import logia.quanlyso.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Channel.
 *
 * @author Dai Mai
 */
@RestController
@RequestMapping("/api")
public class ChannelResource {

	/** The log. */
	private final Logger			log			= LoggerFactory.getLogger(ChannelResource.class);

	/** The Constant ENTITY_NAME. */
	private static final String		ENTITY_NAME	= "channel";

	/** The channel service. */
	private final ChannelService	channelService;

	/**
	 * Instantiates a new channel resource.
	 *
	 * @param channelService the channel service
	 */
	public ChannelResource(ChannelService channelService) {
		this.channelService = channelService;
	}

	/**
	 * POST /channels : Create a new channel.
	 *
	 * @param channelDTO the channelDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new channelDTO, or
	 *         with status 400 (Bad Request) if the channel has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/channels")
	@Timed
	public ResponseEntity<ChannelDTO> createChannel(@RequestBody ChannelDTO channelDTO)
			throws URISyntaxException {
		this.log.debug("REST request to save Channel : {}", channelDTO);
		if (channelDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ChannelResource.ENTITY_NAME,
					"idexists", "A new channel cannot already have an ID")).body(null);
		}
		ChannelDTO result = this.channelService.save(channelDTO);
		return ResponseEntity
				.created(new URI("/api/channels/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(ChannelResource.ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /channels : Updates an existing channel.
	 *
	 * @param channelDTO the channelDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated channelDTO,
	 *         or with status 400 (Bad Request) if the channelDTO is not valid,
	 *         or with status 500 (Internal Server Error) if the channelDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/channels")
	@Timed
	public ResponseEntity<ChannelDTO> updateChannel(@RequestBody ChannelDTO channelDTO)
			throws URISyntaxException {
		this.log.debug("REST request to update Channel : {}", channelDTO);
		if (channelDTO.getId() == null) {
			return this.createChannel(channelDTO);
		}
		ChannelDTO result = this.channelService.save(channelDTO);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ChannelResource.ENTITY_NAME, channelDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET /channels : get all the channels.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of channels in body
	 */
	@GetMapping("/channels")
	@Timed
	public List<ChannelDTO> getAllChannels() {
		this.log.debug("REST request to get all Channels");
		return this.channelService.findAll();
	}

	/**
	 * GET /channels/:id : get the "id" channel.
	 *
	 * @param id the id of the channelDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the channelDTO, or with status
	 *         404 (Not Found)
	 */
	@GetMapping("/channels/{id}")
	@Timed
	public ResponseEntity<ChannelDTO> getChannel(@PathVariable Long id) {
		this.log.debug("REST request to get Channel : {}", id);
		ChannelDTO channelDTO = this.channelService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(channelDTO));
	}

	/**
	 * GET /channels/day/:openDay : get all channels by open day.
	 *
	 * @param openDay the open day of the channelDTOs to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the channelDTOs
	 */
	@GetMapping("/channels/day/{openDay}")
	@Timed
	public List<ChannelDTO> getChannelByChannel(@PathVariable DayOfWeek openDay) {
		this.log.debug("REST request to get Channels by open day : {}", openDay);
		return this.channelService.findAllByOpenDay(openDay);
	}

	/**
	 * DELETE /channels/:id : delete the "id" channel.
	 *
	 * @param id the id of the channelDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/channels/{id}")
	@Timed
	public ResponseEntity<Void> deleteChannel(@PathVariable Long id) {
		this.log.debug("REST request to delete Channel : {}", id);
		this.channelService.delete(id);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityDeletionAlert(ChannelResource.ENTITY_NAME, id.toString())).build();
	}

}
