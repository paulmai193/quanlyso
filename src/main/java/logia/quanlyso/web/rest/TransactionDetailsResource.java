package logia.quanlyso.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import logia.quanlyso.service.TransactionDetailsService;
import logia.quanlyso.service.dto.TransactionDetailsDTO;
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
 * REST controller for managing TransactionDetails.
 *
 * @author Dai Mai
 */
@RestController
@RequestMapping("/api")
public class TransactionDetailsResource {

	/** The log. */
	private final Logger					log			= LoggerFactory
			.getLogger(TransactionDetailsResource.class);

	/** The Constant ENTITY_NAME. */
	private static final String				ENTITY_NAME	= "transactionDetails";

	/** The transaction details service. */
	private final TransactionDetailsService	transactionDetailsService;

	/**
	 * Instantiates a new transaction details resource.
	 *
	 * @param transactionDetailsService the transaction details service
	 */
	public TransactionDetailsResource(TransactionDetailsService transactionDetailsService) {
		this.transactionDetailsService = transactionDetailsService;
	}

	/**
	 * POST /transaction-details : Create a new transactionDetails.
	 *
	 * @param transactionDetailsDTO the transactionDetailsDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         transactionDetailsDTO, or with status 400 (Bad Request) if the transactionDetails has
	 *         already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/transaction-details")
	@Timed
	public ResponseEntity<TransactionDetailsDTO> createTransactionDetails(
			@RequestBody TransactionDetailsDTO transactionDetailsDTO) throws URISyntaxException {
		this.log.debug("REST request to save TransactionDetails : {}", transactionDetailsDTO);
		if (transactionDetailsDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(TransactionDetailsResource.ENTITY_NAME,
					"idexists", "A new transactionDetails cannot already have an ID")).body(null);
		}
		TransactionDetailsDTO result = this.transactionDetailsService.save(transactionDetailsDTO);
		return ResponseEntity
				.created(new URI("/api/transaction-details/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(TransactionDetailsResource.ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /transaction-details : Updates an existing transactionDetails.
	 *
	 * @param transactionDetailsDTO the transactionDetailsDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         transactionDetailsDTO,
	 *         or with status 400 (Bad Request) if the transactionDetailsDTO is not valid,
	 *         or with status 500 (Internal Server Error) if the transactionDetailsDTO couldnt be
	 *         updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/transaction-details")
	@Timed
	public ResponseEntity<TransactionDetailsDTO> updateTransactionDetails(
			@RequestBody TransactionDetailsDTO transactionDetailsDTO) throws URISyntaxException {
		this.log.debug("REST request to update TransactionDetails : {}", transactionDetailsDTO);
		if (transactionDetailsDTO.getId() == null) {
			return this.createTransactionDetails(transactionDetailsDTO);
		}
		TransactionDetailsDTO result = this.transactionDetailsService.save(transactionDetailsDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(TransactionDetailsResource.ENTITY_NAME,
				transactionDetailsDTO.getId().toString())).body(result);
	}

	/**
	 * GET /transaction-details : get all the transactionDetails.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of transactionDetails in body
	 */
	@GetMapping("/transaction-details")
	@Timed
	public ResponseEntity<List<TransactionDetailsDTO>> getAllTransactionDetails(
			@ApiParam Pageable pageable) {
		this.log.debug("REST request to get a page of TransactionDetails");
		Page<TransactionDetailsDTO> page = this.transactionDetailsService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/transaction-details");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /transaction-details/:id : get the "id" transactionDetails.
	 *
	 * @param id the id of the transactionDetailsDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the transactionDetailsDTO, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/transaction-details/{id}")
	@Timed
	public ResponseEntity<TransactionDetailsDTO> getTransactionDetails(@PathVariable Long id) {
		this.log.debug("REST request to get TransactionDetails : {}", id);
		TransactionDetailsDTO transactionDetailsDTO = this.transactionDetailsService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transactionDetailsDTO));
	}

	/**
	 * DELETE /transaction-details/:id : delete the "id" transactionDetails.
	 *
	 * @param id the id of the transactionDetailsDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/transaction-details/{id}")
	@Timed
	public ResponseEntity<Void> deleteTransactionDetails(@PathVariable Long id) {
		this.log.debug("REST request to delete TransactionDetails : {}", id);
		this.transactionDetailsService.delete(id);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityDeletionAlert(TransactionDetailsResource.ENTITY_NAME, id.toString())).build();
	}

}
