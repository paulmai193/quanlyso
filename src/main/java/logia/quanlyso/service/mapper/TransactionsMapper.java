package logia.quanlyso.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import logia.quanlyso.domain.Transactions;
import logia.quanlyso.service.dto.TransactionsDTO;

/**
 * Mapper for the entity Transactions and its DTO TransactionsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TransactionsMapper extends EntityMapper <TransactionsDTO, Transactions> {
    
    @Mapping(target = "transactionDetails", ignore = true)
    Transactions toEntity(TransactionsDTO transactionsDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Transactions fromId(Long id) {
        if (id == null) {
            return null;
        }
        Transactions transactions = new Transactions();
        transactions.setId(id);
        return transactions;
    }
}
