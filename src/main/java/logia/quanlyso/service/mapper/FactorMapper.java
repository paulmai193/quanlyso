package logia.quanlyso.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import logia.quanlyso.domain.Factor;
import logia.quanlyso.service.dto.FactorDTO;

/**
 * Mapper for the entity Factor and its DTO FactorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FactorMapper extends EntityMapper <FactorDTO, Factor> {
    
    @Mapping(target = "profitFactors", ignore = true)
    @Mapping(target = "costFactors", ignore = true)
    @Mapping(target = "transactionDetails", ignore = true)
    Factor toEntity(FactorDTO factorDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Factor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Factor factor = new Factor();
        factor.setId(id);
        return factor;
    }
}
