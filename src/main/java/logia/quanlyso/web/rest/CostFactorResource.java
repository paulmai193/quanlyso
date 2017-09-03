package logia.quanlyso.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import logia.quanlyso.service.CostFactorService;
import logia.quanlyso.service.dto.CostFactorDTO;
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
 * REST controller for managing CostFactor.
 *
 * @author Dai Mai
 */
@RestController
@RequestMapping("/api")
public class CostFactorResource {

	/** The log. */
	private final Logger			log			= LoggerFactory.getLogger(CostFactorResource.class);

	/** The Constant ENTITY_NAME. */
	private static final String		ENTITY_NAME	= "costFactor";

	/** The cost factor service. */
	private final CostFactorService	costFactorService;

	/**
	 * Instantiates a new cost factor resource.
	 *
	 * @param costFactorService the cost factor service
	 */
	public CostFactorResource(CostFactorService costFactorService) {
		this.costFactorService = costFactorService;
	}

	/**
	 * POST /cost-factors : Create a new costFactor.
	 *
	 * @param costFactorDTO the costFactorDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new costFactorDTO, or
	 *         with status 400 (Bad Request) if the costFactor has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/cost-factors")
	@Timed
	public ResponseEntity<CostFactorDTO> createCostFactor(@RequestBody CostFactorDTO costFactorDTO)
			throws URISyntaxException {
		this.log.debug("REST request to save CostFactor : {}", costFactorDTO);
		if (costFactorDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(CostFactorResource.ENTITY_NAME,
					"idexists", "A new costFactor cannot already have an ID")).body(null);
		}
		CostFactorDTO result = this.costFactorService.save(costFactorDTO);
		return ResponseEntity
				.created(new URI("/api/cost-factors/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(CostFactorResource.ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /cost-factors : Updates an existing costFactor.
	 *
	 * @param costFactorDTO the costFactorDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated costFactorDTO,
	 *         or with status 400 (Bad Request) if the costFactorDTO is not valid,
	 *         or with status 500 (Internal Server Error) if the costFactorDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/cost-factors")
	@Timed
	public ResponseEntity<CostFactorDTO> updateCostFactor(@RequestBody CostFactorDTO costFactorDTO)
			throws URISyntaxException {
		this.log.debug("REST request to update CostFactor : {}", costFactorDTO);
		if (costFactorDTO.getId() == null) {
			return this.createCostFactor(costFactorDTO);
		}
		CostFactorDTO result = this.costFactorService.save(costFactorDTO);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(CostFactorResource.ENTITY_NAME, costFactorDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET /cost-factors : get all the costFactors.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of costFactors in body
	 */
	@GetMapping("/cost-factors")
	@Timed
	public List<CostFactorDTO> getAllCostFactors() {
		this.log.debug("REST request to get all CostFactors");
		return this.costFactorService.findAll();
	}

	/**
	 * GET /cost-factors/:id : get the "id" costFactor.
	 *
	 * @param id the id of the costFactorDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the costFactorDTO, or with
	 *         status 404 (Not Found)
	 */
	@GetMapping("/cost-factors/{id}")
	@Timed
	public ResponseEntity<CostFactorDTO> getCostFactor(@PathVariable Long id) {
		this.log.debug("REST request to get CostFactor : {}", id);
		CostFactorDTO costFactorDTO = this.costFactorService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(costFactorDTO));
	}

    /**
     * GET /cost-factors/style/:id : get the costFactor by style ID.
     *
     * @param id the Style ID of the costFactorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the costFactorDTO, or with
     *         status 404 (Not Found)
     */
    @GetMapping("/cost-factors/style/{id}")
    @Timed
    public ResponseEntity<CostFactorDTO> getCostFactorByStyle(@PathVariable Long id) {
        this.log.debug("REST request to get CostFactor : {}", id);
        CostFactorDTO costFactorDTO = this.costFactorService.findOneByStyleId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(costFactorDTO));
    }

	/**
	 * DELETE /cost-factors/:id : delete the "id" costFactor.
	 *
	 * @param id the id of the costFactorDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/cost-factors/{id}")
	@Timed
	public ResponseEntity<Void> deleteCostFactor(@PathVariable Long id) {
		this.log.debug("REST request to delete CostFactor : {}", id);
		this.costFactorService.delete(id);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityDeletionAlert(CostFactorResource.ENTITY_NAME, id.toString())).build();
	}

}
