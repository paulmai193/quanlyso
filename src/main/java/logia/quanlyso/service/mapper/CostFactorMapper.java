package logia.quanlyso.service.mapper;

import logia.quanlyso.domain.CostFactor;
import logia.quanlyso.service.dto.CostFactorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity CostFactor and its DTO CostFactorDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = { StyleMapper.class, TypesMapper.class, })
public interface CostFactorMapper extends EntityMapper<CostFactorDTO, CostFactor> {

	/*
	 * (non-Javadoc)
	 *
	 * @see logia.quanlyso.service.mapper.EntityMapper#toDto(java.lang.Object)
	 */
	@Mapping(source = "styles.id", target = "stylesId")
	CostFactorDTO toDto(CostFactor costFactor);

	/*
	 * (non-Javadoc)
	 *
	 * @see logia.quanlyso.service.mapper.EntityMapper#toEntity(java.lang.Object)
	 */
	@Mapping(source = "stylesId", target = "styles")
	CostFactor toEntity(CostFactorDTO costFactorDTO);

	/**
	 * generating the fromId for all mappers if the databaseType is sql, as the class has
	 * relationship to it might need it, instead of
	 * creating a new attribute to know if the entity has any relationship from some other entity.
	 *
	 * @param id id of the entity
	 * @return the entity instance
	 */

	default CostFactor fromId(Long id) {
		if (id == null) {
			return null;
		}
		CostFactor costFactor = new CostFactor();
		costFactor.setId(id);
		return costFactor;
	}
}
