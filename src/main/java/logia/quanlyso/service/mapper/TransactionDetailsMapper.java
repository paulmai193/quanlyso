package logia.quanlyso.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import logia.quanlyso.domain.TransactionDetails;
import logia.quanlyso.service.dto.TransactionDetailsDTO;

/**
 * Mapper for the entity TransactionDetails and its DTO TransactionDetailsDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = {TransactionsMapper.class, ChannelMapper.class, FactorMapper.class, StyleMapper.class, TypesMapper.class, })
public interface TransactionDetailsMapper extends EntityMapper <TransactionDetailsDTO, TransactionDetails> {
    
    /* (non-Javadoc)
     * @see logia.quanlyso.service.mapper.EntityMapper#toDto(java.lang.Object)
     */
    @Mapping(source = "transactions.id", target = "transactionsId")
    @Mapping(source = "channels.id", target = "channelsId")
    @Mapping(source = "factors.id", target = "factorsId")
    @Mapping(source = "styles.id", target = "stylesId")
    @Mapping(source = "types.id", target = "typesId")
    TransactionDetailsDTO toDto(TransactionDetails transactionDetails); 
    
    /* (non-Javadoc)
     * @see logia.quanlyso.service.mapper.EntityMapper#toEntity(java.lang.Object)
     */
    @Mapping(source = "transactionsId", target = "transactions")
    @Mapping(source = "channelsId", target = "channels")
    @Mapping(source = "factorsId", target = "factors")
    @Mapping(source = "stylesId", target = "styles")
    @Mapping(source = "typesId", target = "types")
    TransactionDetails toEntity(TransactionDetailsDTO transactionDetailsDTO); 
    
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity.
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default TransactionDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setId(id);
        return transactionDetails;
    }
}
