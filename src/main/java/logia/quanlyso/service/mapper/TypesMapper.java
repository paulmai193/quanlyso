package logia.quanlyso.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import logia.quanlyso.domain.Types;
import logia.quanlyso.service.dto.TypesDTO;

/**
 * Mapper for the entity Types and its DTO TypesDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypesMapper extends EntityMapper <TypesDTO, Types> {
    
    /* (non-Javadoc)
     * @see logia.quanlyso.service.mapper.EntityMapper#toEntity(java.lang.Object)
     */
    @Mapping(target = "profitFactors", ignore = true)
    @Mapping(target = "costFactors", ignore = true)
    @Mapping(target = "transactionDetails", ignore = true)
    Types toEntity(TypesDTO typesDTO); 
    
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity.
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Types fromId(Long id) {
        if (id == null) {
            return null;
        }
        Types types = new Types();
        types.setId(id);
        return types;
    }
}
