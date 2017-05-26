package logia.quanlyso.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.domain.Style;
import logia.quanlyso.repository.StyleRepository;
import logia.quanlyso.service.StyleService;
import logia.quanlyso.service.dto.StyleDTO;
import logia.quanlyso.service.mapper.StyleMapper;

/**
 * Service Implementation for managing Style.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class StyleServiceImpl implements StyleService{

    /** The log. */
    private final Logger log = LoggerFactory.getLogger(StyleServiceImpl.class);
    
    /** The style repository. */
    private final StyleRepository styleRepository;

    /** The style mapper. */
    private final StyleMapper styleMapper;

    /**
     * Instantiates a new style service impl.
     *
     * @param styleRepository the style repository
     * @param styleMapper the style mapper
     */
    public StyleServiceImpl(StyleRepository styleRepository, StyleMapper styleMapper) {
        this.styleRepository = styleRepository;
        this.styleMapper = styleMapper;
    }

    /**
     * Save a style.
     *
     * @param styleDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StyleDTO save(StyleDTO styleDTO) {
        log.debug("Request to save Style : {}", styleDTO);
        Style style = styleMapper.toEntity(styleDTO);
        style = styleRepository.save(style);
        StyleDTO result = styleMapper.toDto(style);
        return result;
    }

    /**
     *  Get all the styles.
     *  
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<StyleDTO> findAll() {
        log.debug("Request to get all Styles");
        List<StyleDTO> result = styleRepository.findAll().stream()
            .map(styleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one style by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StyleDTO findOne(Long id) {
        log.debug("Request to get Style : {}", id);
        Style style = styleRepository.findOne(id);
        StyleDTO styleDTO = styleMapper.toDto(style);
        return styleDTO;
    }

    /**
     *  Delete the  style by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Style : {}", id);
        styleRepository.delete(id);
    }
}
