package logia.quanlyso.service.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import logia.quanlyso.domain.Authority;
import logia.quanlyso.domain.User;
import logia.quanlyso.service.dto.UserDTO;

/**
 * Mapper for the entity User and its DTO UserDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserMapper {

	/**
	 * User to user DTO.
	 *
	 * @param user the user
	 * @return the user DTO
	 */
	UserDTO userToUserDTO(User user);

	/**
	 * Users to user DT os.
	 *
	 * @param users the users
	 * @return the list
	 */
	List<UserDTO> usersToUserDTOs(List<User> users);

	/**
	 * User DTO to user.
	 *
	 * @param userDTO the user DTO
	 * @return the user
	 */
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "activationKey", ignore = true)
	@Mapping(target = "resetKey", ignore = true)
	@Mapping(target = "resetDate", ignore = true)
	@Mapping(target = "password", ignore = true)
	User userDTOToUser(UserDTO userDTO);

	/**
	 * User DT os to users.
	 *
	 * @param userDTOs the user DT os
	 * @return the list
	 */
	List<User> userDTOsToUsers(List<UserDTO> userDTOs);

	/**
	 * User from id.
	 *
	 * @param id the id
	 * @return the user
	 */
	default User userFromId(Long id) {
		if (id == null) {
			return null;
		}
		User user = new User();
		user.setId(id);
		return user;
	}

	/**
	 * Strings from authorities.
	 *
	 * @param authorities the authorities
	 * @return the sets the
	 */
	default Set<String> stringsFromAuthorities(Set<Authority> authorities) {
		return authorities.stream().map(Authority::getName).collect(Collectors.toSet());
	}

	/**
	 * Authorities from strings.
	 *
	 * @param strings the strings
	 * @return the sets the
	 */
	default Set<Authority> authoritiesFromStrings(Set<String> strings) {
		return strings.stream().map(string -> {
			Authority auth = new Authority();
			auth.setName(string);
			return auth;
		}).collect(Collectors.toSet());
	}
}
