package logia.quanlyso.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import logia.quanlyso.service.TransactionsService;
import logia.quanlyso.service.dto.TransactionsDTO;
import logia.quanlyso.web.rest.util.HeaderUtil;
import logia.quanlyso.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Transactions.
 *
 * @author Dai Mai
 */
@RestController
@RequestMapping("/api")
public class TransactionsResource {

	/** The log. */
	private final Logger				log			= LoggerFactory
			.getLogger(TransactionsResource.class);

	/** The Constant ENTITY_NAME. */
	private static final String			ENTITY_NAME	= "transactions";

	/** The transactions service. */
	private final TransactionsService	transactionsService;

	/**
	 * Instantiates a new transactions resource.
	 *
	 * @param transactionsService the transactions service
	 */
	public TransactionsResource(TransactionsService transactionsService) {
		this.transactionsService = transactionsService;
	}

	/**
	 * POST /transactions : Create a new transactions.
	 *
	 * @param transactionsDTO the transactionsDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new transactionsDTO,
	 *         or with status 400 (Bad Request) if the transactions has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/transactions")
	@Timed
	public ResponseEntity<TransactionsDTO> createTransactions(
			@RequestBody TransactionsDTO transactionsDTO) throws URISyntaxException {
		this.log.debug("REST request to save Transactions : {}", transactionsDTO);
		if (transactionsDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(TransactionsResource.ENTITY_NAME,
					"idexists", "A new transactions cannot already have an ID")).body(null);
		}
		TransactionsDTO result = this.transactionsService.save(transactionsDTO);
		return ResponseEntity
				.created(new URI("/api/transactions/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(TransactionsResource.ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /transactions : Updates an existing transactions.
	 *
	 * @param transactionsDTO the transactionsDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated transactionsDTO,
	 *         or with status 400 (Bad Request) if the transactionsDTO is not valid,
	 *         or with status 500 (Internal Server Error) if the transactionsDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/transactions")
	@Timed
	public ResponseEntity<TransactionsDTO> updateTransactions(
			@RequestBody TransactionsDTO transactionsDTO) throws URISyntaxException {
		this.log.debug("REST request to update Transactions : {}", transactionsDTO);
		if (transactionsDTO.getId() == null) {
			return this.createTransactions(transactionsDTO);
		}
		TransactionsDTO result = this.transactionsService.save(transactionsDTO);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(TransactionsResource.ENTITY_NAME, transactionsDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET /transactions : get all the transactions.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of transactions in body
	 */
	@GetMapping("/transactions")
	@Timed
	public ResponseEntity<List<TransactionsDTO>> getAllTransactions(@ApiParam Pageable pageable) {
		this.log.debug("REST request to get a page of Transactions");
		Page<TransactionsDTO> page = this.transactionsService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/transactions");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /transactions/:id : get the "id" transactions.
	 *
	 * @param id the id of the transactionsDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the transactionsDTO, or with
	 *         status 404 (Not Found)
	 */
	@GetMapping("/transactions/{id}")
	@Timed
	public ResponseEntity<TransactionsDTO> getTransactions(@PathVariable Long id) {
		this.log.debug("REST request to get Transactions : {}", id);
		TransactionsDTO transactionsDTO = this.transactionsService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transactionsDTO));
	}

	/**
	 * DELETE /transactions/:id : delete the "id" transactions.
	 *
	 * @param id the id of the transactionsDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/transactions/{id}")
	@Timed
	public ResponseEntity<Void> deleteTransactions(@PathVariable Long id) {
		this.log.debug("REST request to delete Transactions : {}", id);
		this.transactionsService.delete(id);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityDeletionAlert(TransactionsResource.ENTITY_NAME, id.toString())).build();
	}

	/**
	 * POST /transactions/calculate : Calculate value of transactions.
	 *
	 * @param transactionsDTO the transactionsDTO to calculate
	 * @return the ResponseEntity with status 200 (Success) and with body the transactionsDTO has
	 *         been calculating value
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/transactions/calculate")
	@Timed
	public ResponseEntity<TransactionsDTO> calculateTransactions(
			@RequestBody TransactionsDTO transactionsDTO) throws URISyntaxException {
		this.log.debug("REST request to calculate value of Transactions : {}", transactionsDTO);
		TransactionsDTO result = this.transactionsService.calculate(transactionsDTO);
		return ResponseEntity.ok(result);
	}

}
