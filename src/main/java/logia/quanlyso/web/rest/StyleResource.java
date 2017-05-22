package logia.quanlyso.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import logia.quanlyso.service.StyleService;
import logia.quanlyso.service.dto.StyleDTO;
import logia.quanlyso.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Style.
 */
@RestController
@RequestMapping("/api")
public class StyleResource {

    private final Logger log = LoggerFactory.getLogger(StyleResource.class);

    private static final String ENTITY_NAME = "style";
        
    private final StyleService styleService;

    public StyleResource(StyleService styleService) {
        this.styleService = styleService;
    }

    /**
     * POST  /styles : Create a new style.
     *
     * @param styleDTO the styleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new styleDTO, or with status 400 (Bad Request) if the style has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/styles")
    @Timed
    public ResponseEntity<StyleDTO> createStyle(@RequestBody StyleDTO styleDTO) throws URISyntaxException {
        log.debug("REST request to save Style : {}", styleDTO);
        if (styleDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new style cannot already have an ID")).body(null);
        }
        StyleDTO result = styleService.save(styleDTO);
        return ResponseEntity.created(new URI("/api/styles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /styles : Updates an existing style.
     *
     * @param styleDTO the styleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated styleDTO,
     * or with status 400 (Bad Request) if the styleDTO is not valid,
     * or with status 500 (Internal Server Error) if the styleDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/styles")
    @Timed
    public ResponseEntity<StyleDTO> updateStyle(@RequestBody StyleDTO styleDTO) throws URISyntaxException {
        log.debug("REST request to update Style : {}", styleDTO);
        if (styleDTO.getId() == null) {
            return createStyle(styleDTO);
        }
        StyleDTO result = styleService.save(styleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, styleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /styles : get all the styles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of styles in body
     */
    @GetMapping("/styles")
    @Timed
    public List<StyleDTO> getAllStyles() {
        log.debug("REST request to get all Styles");
        return styleService.findAll();
    }

    /**
     * GET  /styles/:id : get the "id" style.
     *
     * @param id the id of the styleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the styleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/styles/{id}")
    @Timed
    public ResponseEntity<StyleDTO> getStyle(@PathVariable Long id) {
        log.debug("REST request to get Style : {}", id);
        StyleDTO styleDTO = styleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(styleDTO));
    }

    /**
     * DELETE  /styles/:id : delete the "id" style.
     *
     * @param id the id of the styleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/styles/{id}")
    @Timed
    public ResponseEntity<Void> deleteStyle(@PathVariable Long id) {
        log.debug("REST request to delete Style : {}", id);
        styleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
