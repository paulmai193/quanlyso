package logia.quanlyso.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import logia.quanlyso.domain.Code;
import logia.quanlyso.service.dto.CodeDTO;

/**
 * Mapper for the entity Code and its DTO CodeDTO.
 */
@Mapper(componentModel = "spring", uses = { ChannelMapper.class, })
public interface CodeMapper extends EntityMapper<CodeDTO, Code> {

	@Mapping(source = "channels.id", target = "channelsId")
	CodeDTO toDto(Code code);

	@Mapping(source = "channelsId", target = "channels")
	Code toEntity(CodeDTO codeDTO);

	/**
	 * generating the fromId for all mappers if the databaseType is sql, as the class has
	 * relationship to it might need it, instead of
	 * creating a new attribute to know if the entity has any relationship from some other entity
	 *
	 * @param id id of the entity
	 * @return the entity instance
	 */

	default Code fromId(Long id) {
		if (id == null) {
			return null;
		}
		Code code = new Code();
		code.setId(id);
		return code;
	}
}
