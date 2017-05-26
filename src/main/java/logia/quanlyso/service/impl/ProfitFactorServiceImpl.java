package logia.quanlyso.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.domain.ProfitFactor;
import logia.quanlyso.repository.ProfitFactorRepository;
import logia.quanlyso.service.ProfitFactorService;
import logia.quanlyso.service.dto.ProfitFactorDTO;
import logia.quanlyso.service.mapper.ProfitFactorMapper;

/**
 * Service Implementation for managing ProfitFactor.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class ProfitFactorServiceImpl implements ProfitFactorService{

    /** The log. */
    private final Logger log = LoggerFactory.getLogger(ProfitFactorServiceImpl.class);
    
    /** The profit factor repository. */
    private final ProfitFactorRepository profitFactorRepository;

    /** The profit factor mapper. */
    private final ProfitFactorMapper profitFactorMapper;

    /**
     * Instantiates a new profit factor service impl.
     *
     * @param profitFactorRepository the profit factor repository
     * @param profitFactorMapper the profit factor mapper
     */
    public ProfitFactorServiceImpl(ProfitFactorRepository profitFactorRepository, ProfitFactorMapper profitFactorMapper) {
        this.profitFactorRepository = profitFactorRepository;
        this.profitFactorMapper = profitFactorMapper;
    }

    /**
     * Save a profitFactor.
     *
     * @param profitFactorDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProfitFactorDTO save(ProfitFactorDTO profitFactorDTO) {
        log.debug("Request to save ProfitFactor : {}", profitFactorDTO);
        ProfitFactor profitFactor = profitFactorMapper.toEntity(profitFactorDTO);
        profitFactor = profitFactorRepository.save(profitFactor);
        ProfitFactorDTO result = profitFactorMapper.toDto(profitFactor);
        return result;
    }

    /**
     *  Get all the profitFactors.
     *  
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProfitFactorDTO> findAll() {
        log.debug("Request to get all ProfitFactors");
        List<ProfitFactorDTO> result = profitFactorRepository.findAll().stream()
            .map(profitFactorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one profitFactor by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProfitFactorDTO findOne(Long id) {
        log.debug("Request to get ProfitFactor : {}", id);
        ProfitFactor profitFactor = profitFactorRepository.findOne(id);
        ProfitFactorDTO profitFactorDTO = profitFactorMapper.toDto(profitFactor);
        return profitFactorDTO;
    }

    /**
     *  Delete the  profitFactor by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProfitFactor : {}", id);
        profitFactorRepository.delete(id);
    }
}
