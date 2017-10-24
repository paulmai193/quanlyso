/*
 * 
 */
package logia.quanlyso.service.mapper;

import logia.quanlyso.domain.Style;
import logia.quanlyso.service.dto.StyleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Style and its DTO StyleDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = {})
public interface StyleMapper extends EntityMapper<StyleDTO, Style> {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * logia.quanlyso.service.mapper.EntityMapper#toEntity(java.lang.Object)
	 */
	@Override
	@Mapping(target = "profitFactors", ignore = true)
	@Mapping(target = "costFactors", ignore = true)
	@Mapping(target = "transactionDetails", ignore = true)
	Style toEntity(StyleDTO styleDTO);

	/**
	 * generating the fromId for all mappers if the databaseType is sql, as the
	 * class has relationship to it might need it, instead of creating a new
	 * attribute to know if the entity has any relationship from some other
	 * entity.
	 *
	 * @param id
	 *            id of the entity
	 * @return the entity instance
	 */

	default Style fromId(Long id) {
		if (id == null) {
			return null;
		}
		Style style = new Style();
		style.setId(id);
		return style;
	}
}
