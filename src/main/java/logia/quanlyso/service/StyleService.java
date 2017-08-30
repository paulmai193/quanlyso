package logia.quanlyso.service;

import logia.quanlyso.service.dto.StyleDTO;

import java.util.List;

/**
 * Service Interface for managing Style.
 *
 * @author Dai Mai
 */
public interface StyleService {

	/**
	 * Save a style.
	 *
	 * @param styleDTO the entity to save
	 * @return the persisted entity
	 */
	StyleDTO save(StyleDTO styleDTO);

	/**
	 * Get all the styles.
	 *
	 * @return the list of entities
	 */
	List<StyleDTO> findAll();

	/**
	 * Get the "id" style.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	StyleDTO findOne(Long id);

	/**
	 * Delete the "id" style.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
