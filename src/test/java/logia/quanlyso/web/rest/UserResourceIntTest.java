/*
 * 
 */
package logia.quanlyso.web.rest;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Before;
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
import logia.quanlyso.domain.Authority;
import logia.quanlyso.domain.User;
import logia.quanlyso.repository.UserRepository;
import logia.quanlyso.security.AuthoritiesConstants;
import logia.quanlyso.service.MailService;
import logia.quanlyso.service.UserService;
import logia.quanlyso.web.rest.errors.ExceptionTranslator;
import logia.quanlyso.web.rest.vm.ManagedUserVM;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
public class UserResourceIntTest {

	/** The Constant DEFAULT_LOGIN. */
	static final String DEFAULT_LOGIN = "johndoe";

	/** The Constant UPDATED_LOGIN. */
	static final String UPDATED_LOGIN = "jhipster";

	/** The Constant DEFAULT_PASSWORD. */
	static final String DEFAULT_PASSWORD = "passjohndoe";

	/** The Constant UPDATED_PASSWORD. */
	static final String UPDATED_PASSWORD = "passjhipster";

	/** The Constant DEFAULT_EMAIL. */
	static final String DEFAULT_EMAIL = "johndoe@localhost";

	/** The Constant UPDATED_EMAIL. */
	static final String UPDATED_EMAIL = "jhipster@localhost";

	/** The Constant DEFAULT_FIRSTNAME. */
	static final String DEFAULT_FIRSTNAME = "john";

	/** The Constant UPDATED_FIRSTNAME. */
	static final String UPDATED_FIRSTNAME = "jhipsterFirstName";

	/** The Constant DEFAULT_LASTNAME. */
	static final String DEFAULT_LASTNAME = "doe";

	/** The Constant UPDATED_LASTNAME. */
	static final String UPDATED_LASTNAME = "jhipsterLastName";

	/** The Constant DEFAULT_IMAGEURL. */
	static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";

	/** The Constant UPDATED_IMAGEURL. */
	static final String UPDATED_IMAGEURL = "http://placehold.it/40x40";

	/** The Constant DEFAULT_LANGKEY. */
	static final String DEFAULT_LANGKEY = "en";

	/** The Constant UPDATED_LANGKEY. */
	static final String UPDATED_LANGKEY = "fr";

	/** The user repository. */
	@Autowired
	private UserRepository userRepository;

	/** The mail service. */
	@Autowired
	private MailService mailService;

	/** The user service. */
	@Autowired
	private UserService userService;

	/** The jackson message converter. */
	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	/** The pageable argument resolver. */
	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	/** The exception translator. */
	@Autowired
	private ExceptionTranslator exceptionTranslator;

	/** The em. */
	@Autowired
	private EntityManager em;

	/** The rest user mock mvc. */
	private MockMvc restUserMockMvc;

	/** The user. */
	private User user;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		UserResource userResource = new UserResource(this.userRepository, this.mailService, this.userService);
		this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource)
				.setCustomArgumentResolvers(this.pageableArgumentResolver).setControllerAdvice(this.exceptionTranslator)
				.setMessageConverters(this.jacksonMessageConverter).build();
	}

	/**
	 * Create a User.
	 * 
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which has a required relationship to the User
	 * entity.
	 *
	 * @param em
	 *            the em
	 * @return the user
	 */
	public static User createEntity(EntityManager em) {
		User user = new User();
		user.setLogin(UserResourceIntTest.DEFAULT_LOGIN);
		user.setPassword(RandomStringUtils.random(60));
		user.setActivated(true);
		user.setEmail(UserResourceIntTest.DEFAULT_EMAIL);
		user.setFirstName(UserResourceIntTest.DEFAULT_FIRSTNAME);
		user.setLastName(UserResourceIntTest.DEFAULT_LASTNAME);
		user.setImageUrl(UserResourceIntTest.DEFAULT_IMAGEURL);
		user.setLangKey(UserResourceIntTest.DEFAULT_LANGKEY);
		return user;
	}

	// /**
	// * Creates the and save entity.
	// *
	// * @param em the em
	// * @return the user
	// */
	// public static User createAndSaveEntity(EntityManager em) {
	// User user = new User();
	// user.setLogin(DEFAULT_LOGIN);
	// user.setPassword(RandomStringUtils.random(60));
	// user.setActivated(true);
	// user.setEmail(DEFAULT_EMAIL);
	// user.setFirstName(DEFAULT_FIRSTNAME);
	// user.setLastName(DEFAULT_LASTNAME);
	// user.setImageUrl(DEFAULT_IMAGEURL);
	// user.setLangKey(DEFAULT_LANGKEY);
	// em.persist(user);
	// em.flush();
	// return user;
	// }

	/**
	 * Inits the test.
	 */
	@Before
	public void initTest() {
		this.user = UserResourceIntTest.createEntity(this.em);
	}

	/**
	 * Creates the user.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void createUser() throws Exception {
		int databaseSizeBeforeCreate = this.userRepository.findAll().size();

		// Create the User
		Set<String> authorities = new HashSet<>();
		authorities.add("ROLE_USER");
		ManagedUserVM managedUserVM = new ManagedUserVM(null, UserResourceIntTest.DEFAULT_LOGIN,
				UserResourceIntTest.DEFAULT_PASSWORD, UserResourceIntTest.DEFAULT_FIRSTNAME,
				UserResourceIntTest.DEFAULT_LASTNAME, UserResourceIntTest.DEFAULT_EMAIL, true,
				UserResourceIntTest.DEFAULT_IMAGEURL, UserResourceIntTest.DEFAULT_LANGKEY, null, null, null, null,
				authorities, ZonedDateTime.now(), ZonedDateTime.now());

		this.restUserMockMvc
				.perform(MockMvcRequestBuilders.post("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		// Validate the User in the database
		List<User> userList = this.userRepository.findAll();
		Assertions.assertThat(userList).hasSize(databaseSizeBeforeCreate + 1);
		User testUser = userList.get(userList.size() - 1);
		Assertions.assertThat(testUser.getLogin()).isEqualTo(UserResourceIntTest.DEFAULT_LOGIN);
		Assertions.assertThat(testUser.getFirstName()).isEqualTo(UserResourceIntTest.DEFAULT_FIRSTNAME);
		Assertions.assertThat(testUser.getLastName()).isEqualTo(UserResourceIntTest.DEFAULT_LASTNAME);
		Assertions.assertThat(testUser.getEmail()).isEqualTo(UserResourceIntTest.DEFAULT_EMAIL);
		Assertions.assertThat(testUser.getImageUrl()).isEqualTo(UserResourceIntTest.DEFAULT_IMAGEURL);
		Assertions.assertThat(testUser.getLangKey()).isEqualTo(UserResourceIntTest.DEFAULT_LANGKEY);
	}

	/**
	 * Creates the user with existing id.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void createUserWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = this.userRepository.findAll().size();

		Set<String> authorities = new HashSet<>();
		authorities.add("ROLE_USER");
		ManagedUserVM managedUserVM = new ManagedUserVM(1L, UserResourceIntTest.DEFAULT_LOGIN,
				UserResourceIntTest.DEFAULT_PASSWORD, UserResourceIntTest.DEFAULT_FIRSTNAME,
				UserResourceIntTest.DEFAULT_LASTNAME, UserResourceIntTest.DEFAULT_EMAIL, true,
				UserResourceIntTest.DEFAULT_IMAGEURL, UserResourceIntTest.DEFAULT_LANGKEY, null, null, null, null,
				authorities, ZonedDateTime.now(), ZonedDateTime.now());

		// An entity with an existing ID cannot be created, so this API call
		// must fail
		this.restUserMockMvc
				.perform(MockMvcRequestBuilders.post("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

		// Validate the User in the database
		List<User> userList = this.userRepository.findAll();
		Assertions.assertThat(userList).hasSize(databaseSizeBeforeCreate);
	}

	/**
	 * Creates the user with existing login.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void createUserWithExistingLogin() throws Exception {
		// Initialize the database
		this.userRepository.saveAndFlush(this.user);
		int databaseSizeBeforeCreate = this.userRepository.findAll().size();

		Set<String> authorities = new HashSet<>();
		authorities.add("ROLE_USER");
		ManagedUserVM managedUserVM = new ManagedUserVM(null, UserResourceIntTest.DEFAULT_LOGIN, // this
																									// login
																									// should
				UserResourceIntTest.// already be used
						DEFAULT_PASSWORD,
				UserResourceIntTest.DEFAULT_FIRSTNAME, UserResourceIntTest.DEFAULT_LASTNAME, "anothermail@localhost",
				true, UserResourceIntTest.DEFAULT_IMAGEURL, UserResourceIntTest.DEFAULT_LANGKEY, null, null, null, null,
				authorities, ZonedDateTime.now(), ZonedDateTime.now());

		// Create the User
		this.restUserMockMvc
				.perform(MockMvcRequestBuilders.post("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

		// Validate the User in the database
		List<User> userList = this.userRepository.findAll();
		Assertions.assertThat(userList).hasSize(databaseSizeBeforeCreate);
	}

	/**
	 * Creates the user with existing email.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void createUserWithExistingEmail() throws Exception {
		// Initialize the database
		this.userRepository.saveAndFlush(this.user);
		int databaseSizeBeforeCreate = this.userRepository.findAll().size();

		Set<String> authorities = new HashSet<>();
		authorities.add("ROLE_USER");
		ManagedUserVM managedUserVM = new ManagedUserVM(null, "anotherlogin", UserResourceIntTest.DEFAULT_PASSWORD,
				UserResourceIntTest.DEFAULT_FIRSTNAME, UserResourceIntTest.DEFAULT_LASTNAME,
				UserResourceIntTest.DEFAULT_EMAIL, // this email should already
													// be
				// used
				true, UserResourceIntTest.DEFAULT_IMAGEURL, UserResourceIntTest.DEFAULT_LANGKEY, null, null, null, null,
				authorities, ZonedDateTime.now(), ZonedDateTime.now());

		// Create the User
		this.restUserMockMvc
				.perform(MockMvcRequestBuilders.post("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

		// Validate the User in the database
		List<User> userList = this.userRepository.findAll();
		Assertions.assertThat(userList).hasSize(databaseSizeBeforeCreate);
	}

	/**
	 * Gets the all users.
	 *
	 * @return the all users
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getAllUsers() throws Exception {
		// Initialize the database
		this.userRepository.saveAndFlush(this.user);

		// Get all the users
		this.restUserMockMvc
				.perform(MockMvcRequestBuilders.get("/api/users?sort=id,desc").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].login")
						.value(Matchers.hasItem(UserResourceIntTest.DEFAULT_LOGIN)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].firstName")
						.value(Matchers.hasItem(UserResourceIntTest.DEFAULT_FIRSTNAME)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].lastName")
						.value(Matchers.hasItem(UserResourceIntTest.DEFAULT_LASTNAME)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].email")
						.value(Matchers.hasItem(UserResourceIntTest.DEFAULT_EMAIL)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].imageUrl")
						.value(Matchers.hasItem(UserResourceIntTest.DEFAULT_IMAGEURL)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].langKey")
						.value(Matchers.hasItem(UserResourceIntTest.DEFAULT_LANGKEY)));
	}

	/**
	 * Gets the all users by authority user.
	 *
	 * @return the all users by authority user
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getAllUsersByAuthorityUser() throws Exception {
		// Initialize the database
		Authority authorityUser = new Authority();
		authorityUser.setName(AuthoritiesConstants.USER);
		Set<Authority> authorities = new HashSet<>();
		authorities.add(authorityUser);
		this.user.setAuthorities(authorities);
		this.userRepository.saveAndFlush(this.user);

		// Get all the users
		this.restUserMockMvc
				.perform(MockMvcRequestBuilders.get("/api/users/role-user?sort=id,desc")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].login")
						.value(Matchers.hasItem(UserResourceIntTest.DEFAULT_LOGIN)));
	}

	/**
	 * Gets the all users by authority.
	 *
	 * @return the all users by authority
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getAllUsersByAuthority() throws Exception {
		// Initialize the database
		// User
		Authority authority = new Authority();
		authority.setName(AuthoritiesConstants.USER);
		Set<Authority> authorities = new HashSet<>();
		authorities.add(authority);
		User user = UserResourceIntTest.createEntity(this.em);
		String prefix = "test_user";
		user.setLogin(prefix);
		user.setEmail(prefix + "@localhost");
		user.setAuthorities(authorities);
		this.userRepository.saveAndFlush(user);

		// Admin
		authority.setName(AuthoritiesConstants.ADMIN);
		authorities.clear();
		authorities.add(authority);
		User admin = UserResourceIntTest.createEntity(this.em);
		prefix = "test_admin";
		admin.setLogin(prefix);
		admin.setEmail(prefix + "@localhost");
		admin.setAuthorities(authorities);
		this.userRepository.saveAndFlush(admin);

		// Get all the users
		this.restUserMockMvc
				.perform(MockMvcRequestBuilders.get("/api/users/role/" + AuthoritiesConstants.USER + "?sort=id,desc")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].login").value(Matchers.hasItem(user.getLogin())));

		// Get all the admin
		this.restUserMockMvc
				.perform(MockMvcRequestBuilders.get("/api/users/role/" + AuthoritiesConstants.ADMIN + "?sort=id,desc")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].login").value(Matchers.hasItem(admin.getLogin())));
	}

	/**
	 * Gets the user by login information.
	 *
	 * @return the user
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getUserByLogin() throws Exception {
		// Initialize the database
		this.userRepository.saveAndFlush(this.user);

		// Get the user
		this.restUserMockMvc.perform(MockMvcRequestBuilders.get("/api/users/{login}", this.user.getLogin()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.login").value(this.user.getLogin()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(UserResourceIntTest.DEFAULT_FIRSTNAME))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(UserResourceIntTest.DEFAULT_LASTNAME))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(UserResourceIntTest.DEFAULT_EMAIL))
				.andExpect(MockMvcResultMatchers.jsonPath("$.imageUrl").value(UserResourceIntTest.DEFAULT_IMAGEURL))
				.andExpect(MockMvcResultMatchers.jsonPath("$.langKey").value(UserResourceIntTest.DEFAULT_LANGKEY));
	}

	/**
	 * Gets the user by id.
	 *
	 * @return the user by id
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getUserById() throws Exception {
		// Initialize the database
		this.userRepository.saveAndFlush(this.user);

		// Get the user
		this.restUserMockMvc.perform(MockMvcRequestBuilders.get("/api/users/id/{id}", this.user.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.login").value(this.user.getLogin()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(UserResourceIntTest.DEFAULT_FIRSTNAME))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(UserResourceIntTest.DEFAULT_LASTNAME))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(UserResourceIntTest.DEFAULT_EMAIL))
				.andExpect(MockMvcResultMatchers.jsonPath("$.imageUrl").value(UserResourceIntTest.DEFAULT_IMAGEURL))
				.andExpect(MockMvcResultMatchers.jsonPath("$.langKey").value(UserResourceIntTest.DEFAULT_LANGKEY));
	}

	/**
	 * Gets the non existing user.
	 *
	 * @return the non existing user
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getNonExistingUser() throws Exception {
		this.restUserMockMvc.perform(MockMvcRequestBuilders.get("/api/users/unknown"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * Update user.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void updateUser() throws Exception {
		// Initialize the database
		this.userRepository.saveAndFlush(this.user);
		int databaseSizeBeforeUpdate = this.userRepository.findAll().size();

		// Update the user
		User updatedUser = this.userRepository.findOne(this.user.getId());

		Set<String> authorities = new HashSet<>();
		authorities.add("ROLE_USER");
		ManagedUserVM managedUserVM = new ManagedUserVM(updatedUser.getId(), updatedUser.getLogin(),
				UserResourceIntTest.UPDATED_PASSWORD, UserResourceIntTest.UPDATED_FIRSTNAME,
				UserResourceIntTest.UPDATED_LASTNAME, UserResourceIntTest.UPDATED_EMAIL, updatedUser.getActivated(),
				UserResourceIntTest.UPDATED_IMAGEURL, UserResourceIntTest.UPDATED_LANGKEY, updatedUser.getCreatedBy(),
				updatedUser.getCreatedDate(), updatedUser.getLastModifiedBy(), updatedUser.getLastModifiedDate(),
				authorities, ZonedDateTime.now(), ZonedDateTime.now());

		this.restUserMockMvc
				.perform(MockMvcRequestBuilders.put("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
				.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the User in the database
		List<User> userList = this.userRepository.findAll();
		Assertions.assertThat(userList).hasSize(databaseSizeBeforeUpdate);
		User testUser = userList.get(userList.size() - 1);
		Assertions.assertThat(testUser.getFirstName()).isEqualTo(UserResourceIntTest.UPDATED_FIRSTNAME);
		Assertions.assertThat(testUser.getLastName()).isEqualTo(UserResourceIntTest.UPDATED_LASTNAME);
		Assertions.assertThat(testUser.getEmail()).isEqualTo(UserResourceIntTest.UPDATED_EMAIL);
		Assertions.assertThat(testUser.getImageUrl()).isEqualTo(UserResourceIntTest.UPDATED_IMAGEURL);
		Assertions.assertThat(testUser.getLangKey()).isEqualTo(UserResourceIntTest.UPDATED_LANGKEY);
	}

	/**
	 * Update user login.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void updateUserLogin() throws Exception {
		// Initialize the database
		this.userRepository.saveAndFlush(this.user);
		int databaseSizeBeforeUpdate = this.userRepository.findAll().size();

		// Update the user
		User updatedUser = this.userRepository.findOne(this.user.getId());

		Set<String> authorities = new HashSet<>();
		authorities.add("ROLE_USER");
		ManagedUserVM managedUserVM = new ManagedUserVM(updatedUser.getId(), UserResourceIntTest.UPDATED_LOGIN,
				UserResourceIntTest.UPDATED_PASSWORD, UserResourceIntTest.UPDATED_FIRSTNAME,
				UserResourceIntTest.UPDATED_LASTNAME, UserResourceIntTest.UPDATED_EMAIL, updatedUser.getActivated(),
				UserResourceIntTest.UPDATED_IMAGEURL, UserResourceIntTest.UPDATED_LANGKEY, updatedUser.getCreatedBy(),
				updatedUser.getCreatedDate(), updatedUser.getLastModifiedBy(), updatedUser.getLastModifiedDate(),
				authorities, ZonedDateTime.now(), ZonedDateTime.now());

		this.restUserMockMvc
				.perform(MockMvcRequestBuilders.put("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
				.andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the User in the database
		List<User> userList = this.userRepository.findAll();
		Assertions.assertThat(userList).hasSize(databaseSizeBeforeUpdate);
		User testUser = userList.get(userList.size() - 1);
		Assertions.assertThat(testUser.getLogin()).isEqualTo(UserResourceIntTest.UPDATED_LOGIN);
		Assertions.assertThat(testUser.getFirstName()).isEqualTo(UserResourceIntTest.UPDATED_FIRSTNAME);
		Assertions.assertThat(testUser.getLastName()).isEqualTo(UserResourceIntTest.UPDATED_LASTNAME);
		Assertions.assertThat(testUser.getEmail()).isEqualTo(UserResourceIntTest.UPDATED_EMAIL);
		Assertions.assertThat(testUser.getImageUrl()).isEqualTo(UserResourceIntTest.UPDATED_IMAGEURL);
		Assertions.assertThat(testUser.getLangKey()).isEqualTo(UserResourceIntTest.UPDATED_LANGKEY);
	}

	/**
	 * Update user existing email.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void updateUserExistingEmail() throws Exception {
		// Initialize the database with 2 users
		this.userRepository.saveAndFlush(this.user);

		User anotherUser = new User();
		anotherUser.setLogin("jhipster");
		anotherUser.setPassword(RandomStringUtils.random(60));
		anotherUser.setActivated(true);
		anotherUser.setEmail("jhipster@localhost");
		anotherUser.setFirstName("java");
		anotherUser.setLastName("hipster");
		anotherUser.setImageUrl("");
		anotherUser.setLangKey("en");
		this.userRepository.saveAndFlush(anotherUser);

		// Update the user
		User updatedUser = this.userRepository.findOne(this.user.getId());

		Set<String> authorities = new HashSet<>();
		authorities.add("ROLE_USER");
		ManagedUserVM managedUserVM = new ManagedUserVM(updatedUser.getId(), updatedUser.getLogin(),
				updatedUser.getPassword(), updatedUser.getFirstName(), updatedUser.getLastName(), "jhipster@localhost", // this
																														// email
																														// should
																														// already
																														// be
																														// used
																														// by
																														// anotherUser
				updatedUser.getActivated(), updatedUser.getImageUrl(), updatedUser.getLangKey(),
				updatedUser.getCreatedBy(), updatedUser.getCreatedDate(), updatedUser.getLastModifiedBy(),
				updatedUser.getLastModifiedDate(), authorities, ZonedDateTime.now(), ZonedDateTime.now());

		this.restUserMockMvc
				.perform(MockMvcRequestBuilders.put("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	/**
	 * Update user existing login.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void updateUserExistingLogin() throws Exception {
		// Initialize the database
		this.userRepository.saveAndFlush(this.user);

		User anotherUser = new User();
		anotherUser.setLogin("jhipster");
		anotherUser.setPassword(RandomStringUtils.random(60));
		anotherUser.setActivated(true);
		anotherUser.setEmail("jhipster@localhost");
		anotherUser.setFirstName("java");
		anotherUser.setLastName("hipster");
		anotherUser.setImageUrl("");
		anotherUser.setLangKey("en");
		this.userRepository.saveAndFlush(anotherUser);

		// Update the user
		User updatedUser = this.userRepository.findOne(this.user.getId());

		Set<String> authorities = new HashSet<>();
		authorities.add("ROLE_USER");
		ManagedUserVM managedUserVM = new ManagedUserVM(updatedUser.getId(), "jhipster", // this
				// login
				// should
				// already
				// be used
				// by
				// anotherUser
				updatedUser.getPassword(), updatedUser.getFirstName(), updatedUser.getLastName(),
				updatedUser.getEmail(), updatedUser.getActivated(), updatedUser.getImageUrl(), updatedUser.getLangKey(),
				updatedUser.getCreatedBy(), updatedUser.getCreatedDate(), updatedUser.getLastModifiedBy(),
				updatedUser.getLastModifiedDate(), authorities, ZonedDateTime.now(), ZonedDateTime.now());

		this.restUserMockMvc
				.perform(MockMvcRequestBuilders.put("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	/**
	 * Delete user.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void deleteUser() throws Exception {
		// Initialize the database
		this.userRepository.saveAndFlush(this.user);
		int databaseSizeBeforeDelete = this.userRepository.findAll().size();

		// Delete the user
		this.restUserMockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{login}", this.user.getLogin())
				.accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isOk());

		// Validate the database is empty
		List<User> userList = this.userRepository.findAll();
		Assertions.assertThat(userList).hasSize(databaseSizeBeforeDelete - 1);
	}

	/**
	 * Gets the all authorities.
	 *
	 * @return the all authorities
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void getAllAuthorities() throws Exception {
		this.restUserMockMvc
				.perform(MockMvcRequestBuilders.get("/api/users/authorities").accept(TestUtil.APPLICATION_JSON_UTF8)
						.contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$").isArray()).andExpect(MockMvcResultMatchers.jsonPath("$")
						.value(Matchers.containsInAnyOrder("ROLE_USER", "ROLE_ADMIN")));
	}

	/**
	 * Equals verifier.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		User userA = new User();
		userA.setLogin("AAA");
		User userB = new User();
		userB.setLogin("BBB");
		Assertions.assertThat(userA).isNotEqualTo(userB);
	}
}
