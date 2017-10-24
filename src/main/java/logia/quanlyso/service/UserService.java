/*
 * 
 */
package logia.quanlyso.service;

import logia.quanlyso.config.Constants;
import logia.quanlyso.domain.Authority;
import logia.quanlyso.domain.User;
import logia.quanlyso.repository.AuthorityRepository;
import logia.quanlyso.repository.UserRepository;
import logia.quanlyso.security.AuthoritiesConstants;
import logia.quanlyso.security.SecurityUtils;
import logia.quanlyso.service.dto.UserDTO;
import logia.quanlyso.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class UserService {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(UserService.class);

	/** The user repository. */
	private final UserRepository userRepository;

	/** The password encoder. */
	private final PasswordEncoder passwordEncoder;

	/** The authority repository. */
	private final AuthorityRepository authorityRepository;

	/**
	 * Instantiates a new user service.
	 *
	 * @param userRepository
	 *            the user repository
	 * @param passwordEncoder
	 *            the password encoder
	 * @param authorityRepository
	 *            the authority repository
	 */
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthorityRepository authorityRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authorityRepository = authorityRepository;
	}

	/**
	 * Activate registration.
	 *
	 * @param key
	 *            the key
	 * @return the optional
	 */
	public Optional<User> activateRegistration(String key) {
		this.log.debug("Activating user for activation key {}", key);
		return this.userRepository.findOneByActivationKey(key).map(user -> {
			// activate given user for the registration key.
			user.setActivated(true);
			user.setActivationKey(null);
			this.log.debug("Activated user: {}", user);
			return user;
		});
	}

	/**
	 * Complete password reset.
	 *
	 * @param newPassword
	 *            the new password
	 * @param key
	 *            the key
	 * @return the optional
	 */
	public Optional<User> completePasswordReset(String newPassword, String key) {
		this.log.debug("Reset user password for reset key {}", key);

		return this.userRepository.findOneByResetKey(key)
				.filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400))).map(user -> {
					user.setPassword(this.passwordEncoder.encode(newPassword));
					user.setResetKey(null);
					user.setResetDate(null);
					return user;
				});
	}

	/**
	 * Request password reset.
	 *
	 * @param mail
	 *            the mail
	 * @return the optional
	 */
	public Optional<User> requestPasswordReset(String mail) {
		return this.userRepository.findOneByEmail(mail).filter(User::getActivated).map(user -> {
			user.setResetKey(RandomUtil.generateResetKey());
			user.setResetDate(Instant.now());
			return user;
		});
	}

	/**
	 * Creates the user.
	 *
	 * @param login
	 *            the login
	 * @param password
	 *            the password
	 * @param firstName
	 *            the first name
	 * @param lastName
	 *            the last name
	 * @param email
	 *            the email
	 * @param imageUrl
	 *            the image url
	 * @param langKey
	 *            the lang key
	 * @return the user
	 */
	public User createUser(String login, String password, String firstName, String lastName, String email,
			String imageUrl, String langKey) {

		return this.createUser(login, password, firstName, lastName, email, imageUrl, langKey, false);
	}

	/**
	 * Creates the user.
	 *
	 * @param login
	 *            the login
	 * @param password
	 *            the password
	 * @param firstName
	 *            the first name
	 * @param lastName
	 *            the last name
	 * @param email
	 *            the email
	 * @param imageUrl
	 *            the image url
	 * @param langKey
	 *            the lang key
	 * @param isActivate
	 *            the is activate
	 * @return the user
	 */
	public User createUser(String login, String password, String firstName, String lastName, String email,
			String imageUrl, String langKey, boolean isActivate) {

		User newUser = new User();
		Authority authority = this.authorityRepository.findOne(AuthoritiesConstants.USER);
		Set<Authority> authorities = new HashSet<>();
		String encryptedPassword = this.passwordEncoder.encode(password);
		newUser.setLogin(login);
		// new user gets initially a generated password
		newUser.setPassword(encryptedPassword);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(email);
		newUser.setImageUrl(imageUrl);
		newUser.setLangKey(langKey);
		if (isActivate) {
			// force activate
			newUser.setActivated(true);
		} else {
			// new user is not active
			newUser.setActivated(false);
			// new user gets registration key
			newUser.setActivationKey(RandomUtil.generateActivationKey());
		}
		authorities.add(authority);
		newUser.setAuthorities(authorities);
		this.userRepository.save(newUser);
		this.log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	/**
	 * Creates the user.
	 *
	 * @param userDTO
	 *            the user DTO
	 * @return the user
	 */
	public User createUser(UserDTO userDTO) {
		User user = new User();
		user.setLogin(userDTO.getLogin());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setEmail(userDTO.getEmail());
		user.setImageUrl(userDTO.getImageUrl());
		if (userDTO.getLangKey() == null) {
			user.setLangKey("vi"); // default language
		} else {
			user.setLangKey(userDTO.getLangKey());
		}
		if (userDTO.getAuthorities() != null) {
			Set<Authority> authorities = new HashSet<>();
			userDTO.getAuthorities().forEach(authority -> authorities.add(this.authorityRepository.findOne(authority)));
			user.setAuthorities(authorities);
		}
		String encryptedPassword = this.passwordEncoder.encode(RandomUtil.generatePassword());
		user.setPassword(encryptedPassword);
		user.setResetKey(RandomUtil.generateResetKey());
		user.setResetDate(Instant.now());
		user.setGrantAccessDate(userDTO.getGrantAccessDate());
		user.setRevokeAccessDate(userDTO.getRevokeAccessDate());
		user.setActivated(true);

		this.userRepository.save(user);
		this.log.debug("Created Information for User: {}", user);
		return user;
	}

	/**
	 * Update basic information (first name, last name, email, language) for the
	 * current user.
	 *
	 * @param firstName
	 *            first name of user
	 * @param lastName
	 *            last name of user
	 * @param email
	 *            email id of user
	 * @param langKey
	 *            language key
	 * @param imageUrl
	 *            image URL of user
	 */
	public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
		this.userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			user.setLangKey(langKey);
			user.setImageUrl(imageUrl);
			this.log.debug("Changed Information for User: {}", user);
		});
	}

	/**
	 * Update all information for a specific user, and return the modified user.
	 *
	 * @param userDTO
	 *            user to update
	 * @return updated user
	 */
	public Optional<UserDTO> updateUser(UserDTO userDTO) {
		return Optional.of(this.userRepository.findOne(userDTO.getId())).map(user -> {
			user.setLogin(userDTO.getLogin());
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			user.setEmail(userDTO.getEmail());
			user.setImageUrl(userDTO.getImageUrl());
			user.setActivated(userDTO.isActivated());
			user.setLangKey(userDTO.getLangKey());
			Set<Authority> managedAuthorities = user.getAuthorities();
			managedAuthorities.clear();
			userDTO.getAuthorities().stream().map(this.authorityRepository::findOne).forEach(managedAuthorities::add);
			this.log.debug("Changed Information for User: {}", user);
			return user;
		}).map(UserDTO::new);
	}

	/**
	 * Delete user.
	 *
	 * @param login
	 *            the login
	 */
	public void deleteUser(String login) {
		this.userRepository.findOneByLogin(login).ifPresent(user -> {
			this.userRepository.delete(user);
			this.log.debug("Deleted User: {}", user);
		});
	}

	/**
	 * Change password.
	 *
	 * @param password
	 *            the password
	 */
	public void changePassword(String password) {
		this.userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
			String encryptedPassword = this.passwordEncoder.encode(password);
			user.setPassword(encryptedPassword);
			this.log.debug("Changed password for User: {}", user);
		});
	}

	/**
	 * Gets the all managed users.
	 *
	 * @param pageable
	 *            the pageable
	 * @return the all managed users
	 */
	@Transactional(readOnly = true)
	public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
		return this.userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
	}

	/**
	 * Gets the user with authorities by login.
	 *
	 * @param login
	 *            the login
	 * @return the user with authorities by login
	 */
	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthoritiesByLogin(String login) {
		return this.userRepository.findOneWithAuthoritiesByLogin(login);
	}

	/**
	 * Gets the user with authorities by ID.
	 *
	 * @param id
	 *            the id
	 * @return the user with authorities
	 */
	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthoritiesById(Long id) {
		return this.userRepository.findOneWithAuthoritiesById(id);
	}

	/**
	 * Gets the user with authorities by role.
	 *
	 * @param pageable
	 *
	 * @param id
	 *            the id
	 * @return the user with authorities
	 */
	@Transactional(readOnly = true)
	public Page<UserDTO> getUserWithAuthoritiesByRole(Pageable pageable, String role) {
		Authority authority = this.authorityRepository.getOneByName(role);
		return this.userRepository.findAllByAuthorities(pageable, authority).map(UserDTO::new);
	}

	/**
	 * Gets the user with authorities.
	 *
	 * @return the user with authorities
	 */
	@Transactional(readOnly = true)
	public User getUserWithAuthorities() {
		return this.userRepository.findOneWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null);
	}

	/**
	 * Not activated users should be automatically deleted after 3 days.
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 * </p>
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeNotActivatedUsers() {
		List<User> users = this.userRepository
				.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
		for (User user : users) {
			this.log.debug("Deleting not activated user {}", user.getLogin());
			this.userRepository.delete(user);
		}
	}

	/**
	 * Gets the authorities.
	 *
	 * @return a list of all the authorities
	 */
	public List<String> getAuthorities() {
		return this.authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
	}
}
