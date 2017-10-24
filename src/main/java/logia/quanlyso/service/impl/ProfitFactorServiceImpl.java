/*
 * 
 */
package logia.quanlyso.service.impl;

import logia.quanlyso.domain.ProfitFactor;
import logia.quanlyso.domain.Style;
import logia.quanlyso.repository.ProfitFactorRepository;
import logia.quanlyso.repository.StyleRepository;
import logia.quanlyso.service.ProfitFactorService;
import logia.quanlyso.service.dto.ProfitFactorDTO;
import logia.quanlyso.service.mapper.ProfitFactorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ProfitFactor.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class ProfitFactorServiceImpl implements ProfitFactorService {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(ProfitFactorServiceImpl.class);

	/** The profit factor repository. */
	private final ProfitFactorRepository profitFactorRepository;

	private final StyleRepository styleRepository;

	/** The profit factor mapper. */
	private final ProfitFactorMapper profitFactorMapper;

	public ProfitFactorServiceImpl(ProfitFactorRepository profitFactorRepository, StyleRepository styleRepository,
			ProfitFactorMapper profitFactorMapper) {
		this.profitFactorRepository = profitFactorRepository;
		this.styleRepository = styleRepository;
		this.profitFactorMapper = profitFactorMapper;
	}

	/**
	 * Save a profitFactor.
	 *
	 * @param profitFactorDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	@Override
	public ProfitFactorDTO save(ProfitFactorDTO profitFactorDTO) {
		this.log.debug("Request to save ProfitFactor : {}", profitFactorDTO);
		ProfitFactor profitFactor = this.profitFactorMapper.toEntity(profitFactorDTO);
		profitFactor = this.profitFactorRepository.save(profitFactor);
		ProfitFactorDTO result = this.profitFactorMapper.toDto(profitFactor);
		return result;
	}

	/**
	 * Get all the profitFactors.
	 *
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ProfitFactorDTO> findAll() {
		this.log.debug("Request to get all ProfitFactors");
		List<ProfitFactorDTO> result = this.profitFactorRepository.findAll().stream()
				.map(this.profitFactorMapper::toDto).collect(Collectors.toCollection(LinkedList::new));

		return result;
	}

	/**
	 * Get one profitFactor by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public ProfitFactorDTO findOne(Long id) {
		this.log.debug("Request to get ProfitFactor : {}", id);
		ProfitFactor profitFactor = this.profitFactorRepository.findOne(id);
		ProfitFactorDTO profitFactorDTO = this.profitFactorMapper.toDto(profitFactor);
		return profitFactorDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public ProfitFactorDTO findOneByStyleId(Long id) {
		this.log.debug("Request to get ProfitFactor by Style : {}", id);
		Style style = this.styleRepository.getOne(id);
		ProfitFactor costFactor = this.profitFactorRepository.findOneByStyles(style);
		ProfitFactorDTO profitFactorDTO = this.profitFactorMapper.toDto(costFactor);
		return profitFactorDTO;
	}

	/**
	 * Delete the profitFactor by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	@Override
	public void delete(Long id) {
		this.log.debug("Request to delete ProfitFactor : {}", id);
		this.profitFactorRepository.delete(id);
	}
}
