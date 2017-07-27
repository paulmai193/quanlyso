package logia.quanlyso.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.QuanlysoApp;
import logia.quanlyso.config.Constants;
import logia.quanlyso.domain.User;
import logia.quanlyso.repository.UserRepository;
import logia.quanlyso.security.AuthoritiesConstants;
import logia.quanlyso.service.dto.UserDTO;
import logia.quanlyso.service.util.RandomUtil;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
@Transactional
public class UserServiceIntTest {

	/** The user repository. */
	@Autowired
	private UserRepository	userRepository;

	/** The user service. */
	@Autowired
	private UserService		userService;

	/**
	 * Assert that user must exist to reset password.
	 */
	@Test
	public void assertThatUserMustExistToResetPassword() {
		Optional<User> maybeUser = this.userService.requestPasswordReset("john.doe@localhost");
		Assertions.assertThat(maybeUser.isPresent()).isFalse();

		maybeUser = this.userService.requestPasswordReset("admin@localhost");
		Assertions.assertThat(maybeUser.isPresent()).isTrue();

		Assertions.assertThat(maybeUser.get().getEmail()).isEqualTo("admin@localhost");
		Assertions.assertThat(maybeUser.get().getResetDate()).isNotNull();
		Assertions.assertThat(maybeUser.get().getResetKey()).isNotNull();
	}

	/**
	 * Assert that only activated user can request password reset.
	 */
	@Test
	public void assertThatOnlyActivatedUserCanRequestPasswordReset() {
		User user = this.userService.createUser("johndoe", "johndoe", "John", "Doe",
				"john.doe@localhost", "http://placehold.it/50x50", "en-US");
		Optional<User> maybeUser = this.userService.requestPasswordReset("john.doe@localhost");
		Assertions.assertThat(maybeUser.isPresent()).isFalse();
		this.userRepository.delete(user);
	}

	/**
	 * Assert that reset key must not be older than 24 hours.
	 */
	@Test
	public void assertThatResetKeyMustNotBeOlderThan24Hours() {
		User user = this.userService.createUser("johndoe", "johndoe", "John", "Doe",
				"john.doe@localhost", "http://placehold.it/50x50", "en-US");

		Instant daysAgo = Instant.now().minus(25, ChronoUnit.HOURS);
		String resetKey = RandomUtil.generateResetKey();
		user.setActivated(true);
		user.setResetDate(daysAgo);
		user.setResetKey(resetKey);

		this.userRepository.save(user);

		Optional<User> maybeUser = this.userService.completePasswordReset("johndoe2",
				user.getResetKey());

		Assertions.assertThat(maybeUser.isPresent()).isFalse();

		this.userRepository.delete(user);
	}

	/**
	 * Assert that reset key must be valid.
	 */
	@Test
	public void assertThatResetKeyMustBeValid() {
		User user = this.userService.createUser("johndoe", "johndoe", "John", "Doe",
				"john.doe@localhost", "http://placehold.it/50x50", "en-US");

		Instant daysAgo = Instant.now().minus(25, ChronoUnit.HOURS);
		user.setActivated(true);
		user.setResetDate(daysAgo);
		user.setResetKey("1234");
		this.userRepository.save(user);
		Optional<User> maybeUser = this.userService.completePasswordReset("johndoe2",
				user.getResetKey());
		Assertions.assertThat(maybeUser.isPresent()).isFalse();
		this.userRepository.delete(user);
	}

	/**
	 * Assert that user can reset password.
	 */
	@Test
	public void assertThatUserCanResetPassword() {
		User user = this.userService.createUser("johndoe", "johndoe", "John", "Doe",
				"john.doe@localhost", "http://placehold.it/50x50", "en-US");
		String oldPassword = user.getPassword();
		Instant daysAgo = Instant.now().minus(2, ChronoUnit.HOURS);
		String resetKey = RandomUtil.generateResetKey();
		user.setActivated(true);
		user.setResetDate(daysAgo);
		user.setResetKey(resetKey);
		this.userRepository.save(user);
		Optional<User> maybeUser = this.userService.completePasswordReset("johndoe2",
				user.getResetKey());
		Assertions.assertThat(maybeUser.isPresent()).isTrue();
		Assertions.assertThat(maybeUser.get().getResetDate()).isNull();
		Assertions.assertThat(maybeUser.get().getResetKey()).isNull();
		Assertions.assertThat(maybeUser.get().getPassword()).isNotEqualTo(oldPassword);

		this.userRepository.delete(user);
	}

	/**
	 * Test find not activated users by creation date before.
	 */
	@Test
	public void testFindNotActivatedUsersByCreationDateBefore() {
		this.userService.removeNotActivatedUsers();
		Instant now = Instant.now();
		List<User> users = this.userRepository
				.findAllByActivatedIsFalseAndCreatedDateBefore(now.minus(3, ChronoUnit.DAYS));
		Assertions.assertThat(users).isEmpty();
	}

	/**
	 * Assert that anonymous user is not get.
	 */
	@Test
	public void assertThatAnonymousUserIsNotGet() {
		final PageRequest pageable = new PageRequest(0, (int) this.userRepository.count());
		final Page<UserDTO> allManagedUsers = this.userService.getAllManagedUsers(pageable);
		Assertions.assertThat(allManagedUsers.getContent().stream()
				.noneMatch(user -> Constants.ANONYMOUS_USER.equals(user.getLogin()))).isTrue();
	}

	@Test
	public void assertThatCanGetUserByRole() {
		User user = this.userService.createUser("johndoe", "johndoe", "John", "Doe",
				"john.doe@localhost", "http://placehold.it/50x50", "en-US", true);
		this.userRepository.save(user);
		Page<UserDTO> page = this.userService.getUserWithAuthoritiesByRole(new PageRequest(0, 10),
				AuthoritiesConstants.USER);
		Assertions.assertThat(page.getNumberOfElements()).isEqualTo(4);
		UserDTO check = page.getContent().get(page.getNumberOfElements() - 1);
		Assertions.assertThat(check.getEmail().equals(user.getEmail()));

		this.userRepository.delete(user);
	}
}
