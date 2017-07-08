package logia.quanlyso.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.domain.Factor;
import logia.quanlyso.repository.FactorRepository;
import logia.quanlyso.service.FactorService;
import logia.quanlyso.service.dto.FactorDTO;
import logia.quanlyso.service.mapper.FactorMapper;

/**
 * Service Implementation for managing Factor.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class FactorServiceImpl implements FactorService {

	/** The log. */
	private final Logger			log	= LoggerFactory.getLogger(FactorServiceImpl.class);

	/** The factor repository. */
	private final FactorRepository	factorRepository;

	/** The factor mapper. */
	private final FactorMapper		factorMapper;

	/**
	 * Instantiates a new factor service impl.
	 *
	 * @param factorRepository the factor repository
	 * @param factorMapper the factor mapper
	 */
	public FactorServiceImpl(FactorRepository factorRepository, FactorMapper factorMapper) {
		this.factorRepository = factorRepository;
		this.factorMapper = factorMapper;
	}

	/**
	 * Save a factor.
	 *
	 * @param factorDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public FactorDTO save(FactorDTO factorDTO) {
		this.log.debug("Request to save Factor : {}", factorDTO);
		Factor factor = this.factorMapper.toEntity(factorDTO);
		factor = this.factorRepository.save(factor);
		FactorDTO result = this.factorMapper.toDto(factor);
		return result;
	}

	/**
	 * Get all the factors.
	 * 
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public List<FactorDTO> findAll() {
		this.log.debug("Request to get all Factors");
		List<FactorDTO> result = this.factorRepository.findAll().stream().map(this.factorMapper::toDto)
				.collect(Collectors.toCollection(LinkedList::new));

		return result;
	}

	/**
	 * Get one factor by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public FactorDTO findOne(Long id) {
		this.log.debug("Request to get Factor : {}", id);
		Factor factor = this.factorRepository.findOne(id);
		FactorDTO factorDTO = this.factorMapper.toDto(factor);
		return factorDTO;
	}

	/**
	 * Delete the factor by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		this.log.debug("Request to delete Factor : {}", id);
		this.factorRepository.delete(id);
	}
}
