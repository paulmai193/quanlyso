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
import logia.quanlyso.service.ProfitFactorService;
import logia.quanlyso.service.dto.ProfitFactorDTO;
import logia.quanlyso.web.rest.util.HeaderUtil;

/**
 * REST controller for managing ProfitFactor.
 *
 * @author Dai Mai
 */
@RestController
@RequestMapping("/api")
public class ProfitFactorResource {

    /** The log. */
    private final Logger log = LoggerFactory.getLogger(ProfitFactorResource.class);

    /** The Constant ENTITY_NAME. */
    private static final String ENTITY_NAME = "profitFactor";
        
    /** The profit factor service. */
    private final ProfitFactorService profitFactorService;

    /**
     * Instantiates a new profit factor resource.
     *
     * @param profitFactorService the profit factor service
     */
    public ProfitFactorResource(ProfitFactorService profitFactorService) {
        this.profitFactorService = profitFactorService;
    }

    /**
     * POST  /profit-factors : Create a new profitFactor.
     *
     * @param profitFactorDTO the profitFactorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new profitFactorDTO, or with status 400 (Bad Request) if the profitFactor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/profit-factors")
    @Timed
    public ResponseEntity<ProfitFactorDTO> createProfitFactor(@RequestBody ProfitFactorDTO profitFactorDTO) throws URISyntaxException {
        log.debug("REST request to save ProfitFactor : {}", profitFactorDTO);
        if (profitFactorDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new profitFactor cannot already have an ID")).body(null);
        }
        ProfitFactorDTO result = profitFactorService.save(profitFactorDTO);
        return ResponseEntity.created(new URI("/api/profit-factors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /profit-factors : Updates an existing profitFactor.
     *
     * @param profitFactorDTO the profitFactorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated profitFactorDTO,
     * or with status 400 (Bad Request) if the profitFactorDTO is not valid,
     * or with status 500 (Internal Server Error) if the profitFactorDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/profit-factors")
    @Timed
    public ResponseEntity<ProfitFactorDTO> updateProfitFactor(@RequestBody ProfitFactorDTO profitFactorDTO) throws URISyntaxException {
        log.debug("REST request to update ProfitFactor : {}", profitFactorDTO);
        if (profitFactorDTO.getId() == null) {
            return createProfitFactor(profitFactorDTO);
        }
        ProfitFactorDTO result = profitFactorService.save(profitFactorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, profitFactorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /profit-factors : get all the profitFactors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of profitFactors in body
     */
    @GetMapping("/profit-factors")
    @Timed
    public List<ProfitFactorDTO> getAllProfitFactors() {
        log.debug("REST request to get all ProfitFactors");
        return profitFactorService.findAll();
    }

    /**
     * GET  /profit-factors/:id : get the "id" profitFactor.
     *
     * @param id the id of the profitFactorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the profitFactorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/profit-factors/{id}")
    @Timed
    public ResponseEntity<ProfitFactorDTO> getProfitFactor(@PathVariable Long id) {
        log.debug("REST request to get ProfitFactor : {}", id);
        ProfitFactorDTO profitFactorDTO = profitFactorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(profitFactorDTO));
    }

    /**
     * DELETE  /profit-factors/:id : delete the "id" profitFactor.
     *
     * @param id the id of the profitFactorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/profit-factors/{id}")
    @Timed
    public ResponseEntity<Void> deleteProfitFactor(@PathVariable Long id) {
        log.debug("REST request to delete ProfitFactor : {}", id);
        profitFactorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
