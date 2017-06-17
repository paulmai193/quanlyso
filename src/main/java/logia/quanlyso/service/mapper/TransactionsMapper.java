package logia.quanlyso.service.mapper;

import logia.quanlyso.domain.*;
import logia.quanlyso.service.dto.TransactionsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Transactions and its DTO TransactionsDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, TransactionDetailsMapper.class})
public interface TransactionsMapper extends EntityMapper <TransactionsDTO, Transactions> {
    
    /* (non-Javadoc)
     * @see logia.quanlyso.service.mapper.EntityMapper#toDto(java.lang.Object)
     */
    @Mapping(source = "users.id", target = "clientsId")
    @Mapping(source = "transactionDetails", target = "transactionDetailsDTOs")
    TransactionsDTO toDto(Transactions transactions); 
    
    /* (non-Javadoc)
     * @see logia.quanlyso.service.mapper.EntityMapper#toEntity(java.lang.Object)
     */
//    @Mapping(target = "transactionDetails", ignore = true)
    @Mapping(source = "transactionDetailsDTOs", target = "transactionDetails")
    @Mapping(source = "clientsId", target = "users")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)    
    Transactions toEntity(TransactionsDTO transactionsDTO); 
    
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity.
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
