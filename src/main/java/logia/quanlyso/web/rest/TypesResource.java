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
import logia.quanlyso.service.TypesService;
import logia.quanlyso.service.dto.TypesDTO;
import logia.quanlyso.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Types.
 */
@RestController
@RequestMapping("/api")
public class TypesResource {

    private final Logger log = LoggerFactory.getLogger(TypesResource.class);

    private static final String ENTITY_NAME = "types";
        
    private final TypesService typesService;

    public TypesResource(TypesService typesService) {
        this.typesService = typesService;
    }

    /**
     * POST  /types : Create a new types.
     *
     * @param typesDTO the typesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typesDTO, or with status 400 (Bad Request) if the types has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/types")
    @Timed
    public ResponseEntity<TypesDTO> createTypes(@RequestBody TypesDTO typesDTO) throws URISyntaxException {
        log.debug("REST request to save Types : {}", typesDTO);
        if (typesDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new types cannot already have an ID")).body(null);
        }
        TypesDTO result = typesService.save(typesDTO);
        return ResponseEntity.created(new URI("/api/types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /types : Updates an existing types.
     *
     * @param typesDTO the typesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typesDTO,
     * or with status 400 (Bad Request) if the typesDTO is not valid,
     * or with status 500 (Internal Server Error) if the typesDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/types")
    @Timed
    public ResponseEntity<TypesDTO> updateTypes(@RequestBody TypesDTO typesDTO) throws URISyntaxException {
        log.debug("REST request to update Types : {}", typesDTO);
        if (typesDTO.getId() == null) {
            return createTypes(typesDTO);
        }
        TypesDTO result = typesService.save(typesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, typesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /types : get all the types.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of types in body
     */
    @GetMapping("/types")
    @Timed
    public List<TypesDTO> getAllTypes() {
        log.debug("REST request to get all Types");
        return typesService.findAll();
    }

    /**
     * GET  /types/:id : get the "id" types.
     *
     * @param id the id of the typesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/types/{id}")
    @Timed
    public ResponseEntity<TypesDTO> getTypes(@PathVariable Long id) {
        log.debug("REST request to get Types : {}", id);
        TypesDTO typesDTO = typesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(typesDTO));
    }

    /**
     * DELETE  /types/:id : delete the "id" types.
     *
     * @param id the id of the typesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/types/{id}")
    @Timed
    public ResponseEntity<Void> deleteTypes(@PathVariable Long id) {
        log.debug("REST request to delete Types : {}", id);
        typesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
