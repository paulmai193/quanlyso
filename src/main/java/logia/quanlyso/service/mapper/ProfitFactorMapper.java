/*
 * 
 */
package logia.quanlyso.service.mapper;

import logia.quanlyso.domain.ProfitFactor;
import logia.quanlyso.service.dto.ProfitFactorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity ProfitFactor and its DTO ProfitFactorDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = { StyleMapper.class, })
public interface ProfitFactorMapper extends EntityMapper<ProfitFactorDTO, ProfitFactor> {

	/*
	 * (non-Javadoc)
	 *
	 * @see logia.quanlyso.service.mapper.EntityMapper#toDto(java.lang.Object)
	 */
	@Override
	@Mapping(source = "styles.id", target = "stylesId")
	ProfitFactorDTO toDto(ProfitFactor profitFactor);

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * logia.quanlyso.service.mapper.EntityMapper#toEntity(java.lang.Object)
	 */
	@Override
	@Mapping(source = "stylesId", target = "styles")
	ProfitFactor toEntity(ProfitFactorDTO profitFactorDTO);

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

	default ProfitFactor fromId(Long id) {
		if (id == null) {
			return null;
		}
		ProfitFactor profitFactor = new ProfitFactor();
		profitFactor.setId(id);
		return profitFactor;
	}
}
