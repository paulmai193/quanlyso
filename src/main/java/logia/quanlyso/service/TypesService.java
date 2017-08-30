package logia.quanlyso.service;

import logia.quanlyso.service.dto.TypesDTO;

import java.util.List;

/**
 * Service Interface for managing Types.
 *
 * @author Dai Mai
 */
public interface TypesService {

	/**
	 * Save a types.
	 *
	 * @param typesDTO the entity to save
	 * @return the persisted entity
	 */
	TypesDTO save(TypesDTO typesDTO);

	/**
	 * Get all the types.
	 *
	 * @return the list of entities
	 */
	List<TypesDTO> findAll();

	/**
	 * Get the "id" types.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	TypesDTO findOne(Long id);

	/**
	 * Delete the "id" types.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
