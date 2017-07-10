package logia.quanlyso.web.rest;

import java.util.List;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.QuanlysoApp;
import logia.quanlyso.domain.Channel;
import logia.quanlyso.repository.ChannelRepository;
import logia.quanlyso.service.ChannelService;
import logia.quanlyso.service.dto.ChannelDTO;
import logia.quanlyso.service.mapper.ChannelMapper;
import logia.quanlyso.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the ChannelResource REST controller.
 *
 * @see ChannelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
public class ChannelResourceIntTest {

	/** The Constant DEFAULT_NAME. */
	private static final String						DEFAULT_NAME		= "AAAAAAAAAA";

	/** The Constant UPDATED_NAME. */
	private static final String						UPDATED_NAME		= "BBBBBBBBBB";

	/** The Constant DEFAULT_CODE. */
	private static final String						DEFAULT_CODE		= "12345";

	/** The Constant UPDATED_CODE. */
	private static final String						UPDATED_CODE		= "54321";

	/** The Constant DEFAULT_SUNDAY. */
	private static final Boolean					DEFAULT_SUNDAY		= false;

	/** The Constant UPDATED_SUNDAY. */
	private static final Boolean					UPDATED_SUNDAY		= true;

	/** The Constant DEFAULT_MONDAY. */
	private static final Boolean					DEFAULT_MONDAY		= false;

	/** The Constant UPDATED_MONDAY. */
	private static final Boolean					UPDATED_MONDAY		= true;

	/** The Constant DEFAULT_TUESDAY. */
	private static final Boolean					DEFAULT_TUESDAY		= false;

	/** The Constant UPDATED_TUESDAY. */
	private static final Boolean					UPDATED_TUESDAY		= true;

	/** The Constant DEFAULT_WEDNESDAY. */
	private static final Boolean					DEFAULT_WEDNESDAY	= false;

	/** The Constant UPDATED_WEDNESDAY. */
	private static final Boolean					UPDATED_WEDNESDAY	= true;

	/** The Constant DEFAULT_THURSDAY. */
	private static final Boolean					DEFAULT_THURSDAY	= false;

	/** The Constant UPDATED_THURSDAY. */
	private static final Boolean					UPDATED_THURSDAY	= true;

	/** The Constant DEFAULT_FRIDAY. */
	private static final Boolean					DEFAULT_FRIDAY		= false;

	/** The Constant UPDATED_FRIDAY. */
	private static final Boolean					UPDATED_FRIDAY		= true;

	/** The Constant DEFAULT_SATURDAY. */
	private static final Boolean					DEFAULT_SATURDAY	= false;

	/** The Constant UPDATED_SATURDAY. */
	private static final Boolean					UPDATED_SATURDAY	= true;

	/** The channel repository. */
	@Autowired
	private ChannelRepository						channelRepository;

	/** The channel mapper. */
	@Autowired
	private ChannelMapper							channelMapper;

	/** The channel service. */
	@Autowired
	private ChannelService							channelService;

	/** The jackson message converter. */
	@Autowired
	private MappingJackson2HttpMessageConverter		jacksonMessageConverter;

	/** The pageable argument resolver. */
	@Autowired
	private PageableHandlerMethodArgumentResolver	pageableArgumentResolver;

	/** The exception translator. */
	@Autowired
	private ExceptionTranslator						exceptionTranslator;

	/** The em. */
	@Autowired
	private EntityManager							em;

	/** The rest channel mock mvc. */
	private MockMvc									restChannelMockMvc;

	/** The channel. */
	private Channel									channel;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ChannelResource channelResource = new ChannelResource(this.channelService);
		this.restChannelMockMvc = MockMvcBuilders.standaloneSetup(channelResource)
				.setCustomArgumentResolvers(this.pageableArgumentResolver)
				.setControllerAdvice(this.exceptionTranslator)
				.setMessageConverters(this.jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 * 
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 *
	 * @param em the em
	 * @return the channel
	 */
	public static Channel createEntity(EntityManager em) {
		Channel channel = new Channel().name(ChannelResourceIntTest.DEFAULT_NAME).code(ChannelResourceIntTest.DEFAULT_CODE).sunday(ChannelResourceIntTest.DEFAULT_SUNDAY)
				.monday(ChannelResourceIntTest.DEFAULT_MONDAY).tuesday(ChannelResourceIntTest.DEFAULT_TUESDAY).wednesday(ChannelResourceIntTest.DEFAULT_WEDNESDAY)
				.thursday(ChannelResourceIntTest.DEFAULT_THURSDAY).friday(ChannelResourceIntTest.DEFAULT_FRIDAY).saturday(ChannelResourceIntTest.DEFAULT_SATURDAY);
		return channel;
	}

	/**
	 * Inits the test.
	 */
	@Before
	public void initTest() {
		this.channel = ChannelResourceIntTest.createEntity(this.em);
	}

	/**
	 * Creates the channel.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void createChannel() throws Exception {
		int databaseSizeBeforeCreate = this.channelRepository.findAll().size();

		// Create the Channel
		ChannelDTO channelDTO = this.channelMapper.toDto(this.channel);
		this.restChannelMockMvc
		.perform(MockMvcRequestBuilders.post("/api/channels").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(channelDTO)))
		.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the Channel in the database
		List<Channel> channelList = this.channelRepository.findAll();
		Assertions.assertThat(channelList).hasSize(databaseSizeBeforeCreate + 1);
		Channel testChannel = channelList.get(channelList.size() - 1);
		Assertions.assertThat(testChannel.getName()).isEqualTo(ChannelResourceIntTest.DEFAULT_NAME);
		Assertions.assertThat(testChannel.getCode()).isEqualTo(ChannelResourceIntTest.DEFAULT_CODE);
		Assertions.assertThat(testChannel.isSunday()).isEqualTo(ChannelResourceIntTest.DEFAULT_SUNDAY);
		Assertions.assertThat(testChannel.isMonday()).isEqualTo(ChannelResourceIntTest.DEFAULT_MONDAY);
		Assertions.assertThat(testChannel.isTuesday()).isEqualTo(ChannelResourceIntTest.DEFAULT_TUESDAY);
		Assertions.assertThat(testChannel.isWednesday()).isEqualTo(ChannelResourceIntTest.DEFAULT_WEDNESDAY);
		Assertions.assertThat(testChannel.isThursday()).isEqualTo(ChannelResourceIntTest.DEFAULT_THURSDAY);
		Assertions.assertThat(testChannel.isFriday()).isEqualTo(ChannelResourceIntTest.DEFAULT_FRIDAY);
		Assertions.assertThat(testChannel.isSaturday()).isEqualTo(ChannelResourceIntTest.DEFAULT_SATURDAY);
	}

	/**
	 * Creates the channel with existing id.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void createChannelWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = this.channelRepository.findAll().size();

		// Create the Channel with an existing ID
		this.channel.setId(1L);
		ChannelDTO channelDTO = this.channelMapper.toDto(this.channel);

		// An entity with an existing ID cannot be created, so this API call must fail
		this.restChannelMockMvc
		.perform(MockMvcRequestBuilders.post("/api/channels").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(channelDTO)))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());

		// Validate the Alice in the database
		List<Channel> channelList = this.channelRepository.findAll();
		Assertions.assertThat(channelList).hasSize(databaseSizeBeforeCreate);
	}

	/**
	 * Gets the all channels.
	 *
	 * @return the all channels
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void getAllChannels() throws Exception {
		// Initialize the database
		this.channelRepository.saveAndFlush(this.channel);

		// Get all the channelList
		this.restChannelMockMvc.perform(MockMvcRequestBuilders.get("/api/channels?sort=id,desc")).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").value(Matchers.hasItem(this.channel.getId().intValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].name").value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_NAME.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].code").value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_CODE.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].sunday").value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_SUNDAY.booleanValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].monday").value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_MONDAY.booleanValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].tuesday").value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_TUESDAY.booleanValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].wednesday")
				.value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_WEDNESDAY.booleanValue())))
		.andExpect(
				MockMvcResultMatchers.jsonPath("$.[*].thursday").value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_THURSDAY.booleanValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].friday").value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_FRIDAY.booleanValue())))
		.andExpect(
				MockMvcResultMatchers.jsonPath("$.[*].saturday").value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_SATURDAY.booleanValue())));
	}

	/**
	 * Gets the channel.
	 *
	 * @return the channel
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void getChannel() throws Exception {
		// Initialize the database
		this.channelRepository.saveAndFlush(this.channel);

		// Get the channel
		this.restChannelMockMvc.perform(MockMvcRequestBuilders.get("/api/channels/{id}", this.channel.getId()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(this.channel.getId().intValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(ChannelResourceIntTest.DEFAULT_NAME.toString()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(ChannelResourceIntTest.DEFAULT_CODE.toString()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.sunday").value(ChannelResourceIntTest.DEFAULT_SUNDAY.booleanValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.monday").value(ChannelResourceIntTest.DEFAULT_MONDAY.booleanValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.tuesday").value(ChannelResourceIntTest.DEFAULT_TUESDAY.booleanValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.wednesday").value(ChannelResourceIntTest.DEFAULT_WEDNESDAY.booleanValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.thursday").value(ChannelResourceIntTest.DEFAULT_THURSDAY.booleanValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.friday").value(ChannelResourceIntTest.DEFAULT_FRIDAY.booleanValue()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.saturday").value(ChannelResourceIntTest.DEFAULT_SATURDAY.booleanValue()));
	}

	/**
	 * Gets the channel by open day.
	 *
	 * @return the channel
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void getChannelByOpenDayAssertHaveRecord() throws Exception {
		// Initialize the database
		this.channel.setSunday(ChannelResourceIntTest.UPDATED_SUNDAY);
		this.channelRepository.saveAndFlush(this.channel);

		// Get the channel
		this.restChannelMockMvc.perform(MockMvcRequestBuilders.get("/api/channels/day/SUNDAY"))
		        .andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").value(Matchers.hasItem(this.channel.getId().intValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].name").value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_NAME.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].code").value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_CODE.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].sunday").value(Matchers.hasItem(ChannelResourceIntTest.UPDATED_SUNDAY.booleanValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].monday").value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_MONDAY.booleanValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].tuesday").value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_TUESDAY.booleanValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].wednesday")
				.value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_WEDNESDAY.booleanValue())))
		.andExpect(
				MockMvcResultMatchers.jsonPath("$.[*].thursday").value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_THURSDAY.booleanValue())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].friday").value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_FRIDAY.booleanValue())))
		.andExpect(
				MockMvcResultMatchers.jsonPath("$.[*].saturday").value(Matchers.hasItem(ChannelResourceIntTest.DEFAULT_SATURDAY.booleanValue())));
	}

	@Test
	@Ignore
	@Transactional
	public void getChannelByOpenDayAssertNotHaveRecord() throws Exception {
		// Initialize the database
		this.channelRepository.saveAndFlush(this.channel);

		// Get the channel
		this.restChannelMockMvc.perform(MockMvcRequestBuilders.get("/api/channels/day/otherday")).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
	}

	/**
	 * Gets the non existing channel.
	 *
	 * @return the non existing channel
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void getNonExistingChannel() throws Exception {
		// Get the channel
		this.restChannelMockMvc.perform(MockMvcRequestBuilders.get("/api/channels/{id}", Long.MAX_VALUE))
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * Update channel.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void updateChannel() throws Exception {
		// Initialize the database
		this.channelRepository.saveAndFlush(this.channel);
		int databaseSizeBeforeUpdate = this.channelRepository.findAll().size();

		// Update the channel
		Channel updatedChannel = this.channelRepository.findOne(this.channel.getId());
		updatedChannel.name(ChannelResourceIntTest.UPDATED_NAME).code(ChannelResourceIntTest.UPDATED_CODE).sunday(ChannelResourceIntTest.UPDATED_SUNDAY)
		.monday(ChannelResourceIntTest.UPDATED_MONDAY).tuesday(ChannelResourceIntTest.UPDATED_TUESDAY).wednesday(ChannelResourceIntTest.UPDATED_WEDNESDAY)
		.thursday(ChannelResourceIntTest.UPDATED_THURSDAY).friday(ChannelResourceIntTest.UPDATED_FRIDAY).saturday(ChannelResourceIntTest.UPDATED_SATURDAY);
		ChannelDTO channelDTO = this.channelMapper.toDto(updatedChannel);

		this.restChannelMockMvc
		.perform(MockMvcRequestBuilders.put("/api/channels").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(channelDTO)))
		.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the Channel in the database
		List<Channel> channelList = this.channelRepository.findAll();
		Assertions.assertThat(channelList).hasSize(databaseSizeBeforeUpdate);
		Channel testChannel = channelList.get(channelList.size() - 1);
		Assertions.assertThat(testChannel.getName()).isEqualTo(ChannelResourceIntTest.UPDATED_NAME);
		Assertions.assertThat(testChannel.getCode()).isEqualTo(ChannelResourceIntTest.UPDATED_CODE);
		Assertions.assertThat(testChannel.isSunday()).isEqualTo(ChannelResourceIntTest.UPDATED_SUNDAY);
		Assertions.assertThat(testChannel.isMonday()).isEqualTo(ChannelResourceIntTest.UPDATED_MONDAY);
		Assertions.assertThat(testChannel.isTuesday()).isEqualTo(ChannelResourceIntTest.UPDATED_TUESDAY);
		Assertions.assertThat(testChannel.isWednesday()).isEqualTo(ChannelResourceIntTest.UPDATED_WEDNESDAY);
		Assertions.assertThat(testChannel.isThursday()).isEqualTo(ChannelResourceIntTest.UPDATED_THURSDAY);
		Assertions.assertThat(testChannel.isFriday()).isEqualTo(ChannelResourceIntTest.UPDATED_FRIDAY);
		Assertions.assertThat(testChannel.isSaturday()).isEqualTo(ChannelResourceIntTest.UPDATED_SATURDAY);
	}

	/**
	 * Update non existing channel.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void updateNonExistingChannel() throws Exception {
		int databaseSizeBeforeUpdate = this.channelRepository.findAll().size();

		// Create the Channel
		ChannelDTO channelDTO = this.channelMapper.toDto(this.channel);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		this.restChannelMockMvc
		.perform(MockMvcRequestBuilders.put("/api/channels").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(channelDTO)))
		.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the Channel in the database
		List<Channel> channelList = this.channelRepository.findAll();
		Assertions.assertThat(channelList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	/**
	 * Delete channel.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void deleteChannel() throws Exception {
		// Initialize the database
		this.channelRepository.saveAndFlush(this.channel);
		int databaseSizeBeforeDelete = this.channelRepository.findAll().size();

		// Get the channel
		this.restChannelMockMvc.perform(MockMvcRequestBuilders.delete("/api/channels/{id}", this.channel.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the database is empty
		List<Channel> channelList = this.channelRepository.findAll();
		Assertions.assertThat(channelList).hasSize(databaseSizeBeforeDelete - 1);
	}

	/**
	 * Equals verifier.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Channel.class);
		Channel channel1 = new Channel();
		channel1.setId(1L);
		Channel channel2 = new Channel();
		channel2.setId(channel1.getId());
		Assertions.assertThat(channel1).isEqualTo(channel2);
		channel2.setId(2L);
		Assertions.assertThat(channel1).isNotEqualTo(channel2);
		channel1.setId(null);
		Assertions.assertThat(channel1).isNotEqualTo(channel2);
	}

	/**
	 * Dto equals verifier.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier(ChannelDTO.class);
		ChannelDTO channelDTO1 = new ChannelDTO();
		channelDTO1.setId(1L);
		ChannelDTO channelDTO2 = new ChannelDTO();
		Assertions.assertThat(channelDTO1).isNotEqualTo(channelDTO2);
		channelDTO2.setId(channelDTO1.getId());
		Assertions.assertThat(channelDTO1).isEqualTo(channelDTO2);
		channelDTO2.setId(2L);
		Assertions.assertThat(channelDTO1).isNotEqualTo(channelDTO2);
		channelDTO1.setId(null);
		Assertions.assertThat(channelDTO1).isNotEqualTo(channelDTO2);
	}

	/**
	 * Test entity from id.
	 */
	@Test
	@Transactional
	public void testEntityFromId() {
		Assertions.assertThat(this.channelMapper.fromId(42L).getId()).isEqualTo(42);
		Assertions.assertThat(this.channelMapper.fromId(null)).isNull();
	}
}
