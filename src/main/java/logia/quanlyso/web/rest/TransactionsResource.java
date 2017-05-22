package logia.quanlyso.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import io.swagger.annotations.ApiParam;
import logia.quanlyso.service.TransactionsService;
import logia.quanlyso.service.dto.TransactionsDTO;
import logia.quanlyso.web.rest.util.HeaderUtil;
import logia.quanlyso.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Transactions.
 */
@RestController
@RequestMapping("/api")
public class TransactionsResource {

    private final Logger log = LoggerFactory.getLogger(TransactionsResource.class);

    private static final String ENTITY_NAME = "transactions";
        
    private final TransactionsService transactionsService;

    public TransactionsResource(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    /**
     * POST  /transactions : Create a new transactions.
     *
     * @param transactionsDTO the transactionsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transactionsDTO, or with status 400 (Bad Request) if the transactions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transactions")
    @Timed
    public ResponseEntity<TransactionsDTO> createTransactions(@RequestBody TransactionsDTO transactionsDTO) throws URISyntaxException {
        log.debug("REST request to save Transactions : {}", transactionsDTO);
        if (transactionsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new transactions cannot already have an ID")).body(null);
        }
        TransactionsDTO result = transactionsService.save(transactionsDTO);
        return ResponseEntity.created(new URI("/api/transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transactions : Updates an existing transactions.
     *
     * @param transactionsDTO the transactionsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transactionsDTO,
     * or with status 400 (Bad Request) if the transactionsDTO is not valid,
     * or with status 500 (Internal Server Error) if the transactionsDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transactions")
    @Timed
    public ResponseEntity<TransactionsDTO> updateTransactions(@RequestBody TransactionsDTO transactionsDTO) throws URISyntaxException {
        log.debug("REST request to update Transactions : {}", transactionsDTO);
        if (transactionsDTO.getId() == null) {
            return createTransactions(transactionsDTO);
        }
        TransactionsDTO result = transactionsService.save(transactionsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transactionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transactions : get all the transactions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transactions in body
     */
    @GetMapping("/transactions")
    @Timed
    public ResponseEntity<List<TransactionsDTO>> getAllTransactions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Transactions");
        Page<TransactionsDTO> page = transactionsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transactions/:id : get the "id" transactions.
     *
     * @param id the id of the transactionsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transactionsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/transactions/{id}")
    @Timed
    public ResponseEntity<TransactionsDTO> getTransactions(@PathVariable Long id) {
        log.debug("REST request to get Transactions : {}", id);
        TransactionsDTO transactionsDTO = transactionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transactionsDTO));
    }

    /**
     * DELETE  /transactions/:id : delete the "id" transactions.
     *
     * @param id the id of the transactionsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransactions(@PathVariable Long id) {
        log.debug("REST request to delete Transactions : {}", id);
        transactionsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
