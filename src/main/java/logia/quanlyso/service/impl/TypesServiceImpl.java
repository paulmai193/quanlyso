package logia.quanlyso.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.domain.Types;
import logia.quanlyso.repository.TypesRepository;
import logia.quanlyso.service.TypesService;
import logia.quanlyso.service.dto.TypesDTO;
import logia.quanlyso.service.mapper.TypesMapper;

/**
 * Service Implementation for managing Types.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class TypesServiceImpl implements TypesService {

	/** The log. */
	private final Logger			log	= LoggerFactory.getLogger(TypesServiceImpl.class);

	/** The types repository. */
	private final TypesRepository	typesRepository;

	/** The types mapper. */
	private final TypesMapper		typesMapper;

	/**
	 * Instantiates a new types service impl.
	 *
	 * @param typesRepository the types repository
	 * @param typesMapper the types mapper
	 */
	public TypesServiceImpl(TypesRepository typesRepository, TypesMapper typesMapper) {
		this.typesRepository = typesRepository;
		this.typesMapper = typesMapper;
	}

	/**
	 * Save a types.
	 *
	 * @param typesDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public TypesDTO save(TypesDTO typesDTO) {
		this.log.debug("Request to save Types : {}", typesDTO);
		Types types = this.typesMapper.toEntity(typesDTO);
		types = this.typesRepository.save(types);
		TypesDTO result = this.typesMapper.toDto(types);
		return result;
	}

	/**
	 * Get all the types.
	 * 
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public List<TypesDTO> findAll() {
		this.log.debug("Request to get all Types");
		List<TypesDTO> result = this.typesRepository.findAll().stream().map(this.typesMapper::toDto)
				.collect(Collectors.toCollection(LinkedList::new));

		return result;
	}

	/**
	 * Get one types by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public TypesDTO findOne(Long id) {
		this.log.debug("Request to get Types : {}", id);
		Types types = this.typesRepository.findOne(id);
		TypesDTO typesDTO = this.typesMapper.toDto(types);
		return typesDTO;
	}

	/**
	 * Delete the types by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		this.log.debug("Request to delete Types : {}", id);
		this.typesRepository.delete(id);
	}
}
