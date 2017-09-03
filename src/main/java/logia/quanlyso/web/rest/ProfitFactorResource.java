package logia.quanlyso.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import logia.quanlyso.domain.ProfitFactor;
import logia.quanlyso.service.ProfitFactorService;
import logia.quanlyso.service.dto.CostFactorDTO;
import logia.quanlyso.service.dto.ProfitFactorDTO;
import logia.quanlyso.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ProfitFactor.
 *
 * @author Dai Mai
 */
@RestController
@RequestMapping("/api")
public class ProfitFactorResource {

	/** The log. */
	private final Logger				log			= LoggerFactory
			.getLogger(ProfitFactorResource.class);

	/** The Constant ENTITY_NAME. */
	private static final String			ENTITY_NAME	= "profitFactor";

	/** The profit factor service. */
	private final ProfitFactorService	profitFactorService;

	/**
	 * Instantiates a new profit factor resource.
	 *
	 * @param profitFactorService the profit factor service
	 */
	public ProfitFactorResource(ProfitFactorService profitFactorService) {
		this.profitFactorService = profitFactorService;
	}

	/**
	 * POST /profit-factors : Create a new profitFactor.
	 *
	 * @param profitFactorDTO the profitFactorDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new profitFactorDTO,
	 *         or with status 400 (Bad Request) if the profitFactor has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/profit-factors")
	@Timed
	public ResponseEntity<ProfitFactorDTO> createProfitFactor(
			@RequestBody ProfitFactorDTO profitFactorDTO) throws URISyntaxException {
		this.log.debug("REST request to save ProfitFactor : {}", profitFactorDTO);
		if (profitFactorDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ProfitFactorResource.ENTITY_NAME,
					"idexists", "A new profitFactor cannot already have an ID")).body(null);
		}
		ProfitFactorDTO result = this.profitFactorService.save(profitFactorDTO);
		return ResponseEntity
				.created(new URI("/api/profit-factors/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(ProfitFactorResource.ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /profit-factors : Updates an existing profitFactor.
	 *
	 * @param profitFactorDTO the profitFactorDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated profitFactorDTO,
	 *         or with status 400 (Bad Request) if the profitFactorDTO is not valid,
	 *         or with status 500 (Internal Server Error) if the profitFactorDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/profit-factors")
	@Timed
	public ResponseEntity<ProfitFactorDTO> updateProfitFactor(
			@RequestBody ProfitFactorDTO profitFactorDTO) throws URISyntaxException {
		this.log.debug("REST request to update ProfitFactor : {}", profitFactorDTO);
		if (profitFactorDTO.getId() == null) {
			return this.createProfitFactor(profitFactorDTO);
		}
		ProfitFactorDTO result = this.profitFactorService.save(profitFactorDTO);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ProfitFactorResource.ENTITY_NAME, profitFactorDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET /profit-factors : get all the profitFactors.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of profitFactors in body
	 */
	@GetMapping("/profit-factors")
	@Timed
	public List<ProfitFactorDTO> getAllProfitFactors() {
		this.log.debug("REST request to get all ProfitFactors");
		return this.profitFactorService.findAll();
	}

	/**
	 * GET /profit-factors/:id : get the "id" profitFactor.
	 *
	 * @param id the id of the profitFactorDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the profitFactorDTO, or with
	 *         status 404 (Not Found)
	 */
	@GetMapping("/profit-factors/{id}")
	@Timed
	public ResponseEntity<ProfitFactorDTO> getProfitFactor(@PathVariable Long id) {
		this.log.debug("REST request to get ProfitFactor : {}", id);
		ProfitFactorDTO profitFactorDTO = this.profitFactorService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(profitFactorDTO));
	}

    /**
     * GET /profit-factors/style/:id : get the profitFactor by style ID.
     *
     * @param id the Style ID of the profitFactorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the profitFactorDTO, or with
     *         status 404 (Not Found)
     */
    @GetMapping("/profit-factors/style/{id}")
    @Timed
    public ResponseEntity<ProfitFactorDTO> getProfitFactorByStyle(@PathVariable Long id) {
        this.log.debug("REST request to get ProfitFactor : {}", id);
        ProfitFactorDTO profitFactorDTO = this.profitFactorService.findOneByStyleId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(profitFactorDTO));
    }

	/**
	 * DELETE /profit-factors/:id : delete the "id" profitFactor.
	 *
	 * @param id the id of the profitFactorDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/profit-factors/{id}")
	@Timed
	public ResponseEntity<Void> deleteProfitFactor(@PathVariable Long id) {
		this.log.debug("REST request to delete ProfitFactor : {}", id);
		this.profitFactorService.delete(id);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityDeletionAlert(ProfitFactorResource.ENTITY_NAME, id.toString())).build();
	}

}
