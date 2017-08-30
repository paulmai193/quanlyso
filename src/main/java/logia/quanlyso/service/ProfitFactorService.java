package logia.quanlyso.service;

import logia.quanlyso.service.dto.ProfitFactorDTO;

import java.util.List;

/**
 * Service Interface for managing ProfitFactor.
 *
 * @author Dai Mai
 */
public interface ProfitFactorService {

	/**
	 * Save a profitFactor.
	 *
	 * @param profitFactorDTO the entity to save
	 * @return the persisted entity
	 */
	ProfitFactorDTO save(ProfitFactorDTO profitFactorDTO);

	/**
	 * Get all the profitFactors.
	 *
	 * @return the list of entities
	 */
	List<ProfitFactorDTO> findAll();

	/**
	 * Get the "id" profitFactor.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	ProfitFactorDTO findOne(Long id);

	/**
	 * Delete the "id" profitFactor.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
