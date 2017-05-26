package logia.quanlyso.service.mapper;

import java.util.List;

/**
 * Contract for a generic dto to entity mapper.
 *
 * @author Dai Mai
 * @param <DTO> - DTO type parameter.
 * @param <ENTITY> - Entity type parameter.
 */

public interface EntityMapper <DTO, ENTITY> {

    /**
     * To entity.
     *
     * @param dto the dto
     * @return the entity
     */
    public ENTITY toEntity(DTO dto);

    /**
     * To dto.
     *
     * @param entity the entity
     * @return the dto
     */
    public DTO toDto(ENTITY entity);

    /**
     * To entity.
     *
     * @param dtoList the dto list
     * @return the list
     */
    public List <ENTITY> toEntity(List<DTO> dtoList);

    /**
     * To dto.
     *
     * @param entityList the entity list
     * @return the list
     */
    public List <DTO> toDto(List<ENTITY> entityList);
}
