package logia.quanlyso.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import logia.quanlyso.domain.CostFactor;
import logia.quanlyso.service.dto.CostFactorDTO;

/**
 * Mapper for the entity CostFactor and its DTO CostFactorDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = { FactorMapper.class, StyleMapper.class,
		TypesMapper.class, })
public interface CostFactorMapper extends EntityMapper<CostFactorDTO, CostFactor> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see logia.quanlyso.service.mapper.EntityMapper#toDto(java.lang.Object)
	 */
	@Mapping(source = "factors.id", target = "factorsId")
	@Mapping(source = "styles.id", target = "stylesId")
	@Mapping(source = "types.id", target = "typesId")
	CostFactorDTO toDto(CostFactor costFactor);

	/*
	 * (non-Javadoc)
	 * 
	 * @see logia.quanlyso.service.mapper.EntityMapper#toEntity(java.lang.Object)
	 */
	@Mapping(source = "factorsId", target = "factors")
	@Mapping(source = "stylesId", target = "styles")
	@Mapping(source = "typesId", target = "types")
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
