/*
 * 
 */
package logia.quanlyso.service.impl;

import logia.quanlyso.domain.CostFactor;
import logia.quanlyso.domain.Style;
import logia.quanlyso.repository.CostFactorRepository;
import logia.quanlyso.repository.StyleRepository;
import logia.quanlyso.service.CostFactorService;
import logia.quanlyso.service.dto.CostFactorDTO;
import logia.quanlyso.service.mapper.CostFactorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CostFactor.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class CostFactorServiceImpl implements CostFactorService {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(CostFactorServiceImpl.class);

	/** The cost factor repository. */
	private final CostFactorRepository costFactorRepository;

	private final StyleRepository styleRepository;

	/** The cost factor mapper. */
	private final CostFactorMapper costFactorMapper;

	public CostFactorServiceImpl(CostFactorRepository costFactorRepository, StyleRepository styleRepository,
			CostFactorMapper costFactorMapper) {
		this.costFactorRepository = costFactorRepository;
		this.styleRepository = styleRepository;
		this.costFactorMapper = costFactorMapper;
	}

	/**
	 * Save a costFactor.
	 *
	 * @param costFactorDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	@Override
	public CostFactorDTO save(CostFactorDTO costFactorDTO) {
		this.log.debug("Request to save CostFactor : {}", costFactorDTO);
		CostFactor costFactor = this.costFactorMapper.toEntity(costFactorDTO);
		costFactor = this.costFactorRepository.save(costFactor);
		CostFactorDTO result = this.costFactorMapper.toDto(costFactor);
		return result;
	}

	/**
	 * Get all the costFactors.
	 *
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public List<CostFactorDTO> findAll() {
		this.log.debug("Request to get all CostFactors");
		List<CostFactorDTO> result = this.costFactorRepository.findAll().stream().map(this.costFactorMapper::toDto)
				.collect(Collectors.toCollection(LinkedList::new));

		return result;
	}

	/**
	 * Get one costFactor by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public CostFactorDTO findOne(Long id) {
		this.log.debug("Request to get CostFactor : {}", id);
		CostFactor costFactor = this.costFactorRepository.findOne(id);
		CostFactorDTO costFactorDTO = this.costFactorMapper.toDto(costFactor);
		return costFactorDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public CostFactorDTO findOneByStyleId(Long id) {
		this.log.debug("Request to get CostFactor by Style : {}", id);
		Style style = this.styleRepository.getOne(id);
		CostFactor costFactor = this.costFactorRepository.findOneByStyles(style);
		CostFactorDTO costFactorDTO = this.costFactorMapper.toDto(costFactor);
		return costFactorDTO;
	}

	/**
	 * Delete the costFactor by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	@Override
	public void delete(Long id) {
		this.log.debug("Request to delete CostFactor : {}", id);
		this.costFactorRepository.delete(id);
	}
}
