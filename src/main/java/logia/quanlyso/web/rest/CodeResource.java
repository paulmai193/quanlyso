package logia.quanlyso.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import logia.quanlyso.service.ChannelService;
import logia.quanlyso.service.CodeService;
import logia.quanlyso.service.dto.ChannelDTO;
import logia.quanlyso.service.dto.CodeDTO;
import logia.quanlyso.service.dto.CrawlRequestDTO;
import logia.quanlyso.service.util.DateFormatterUtil;
import logia.quanlyso.web.rest.util.HeaderUtil;
import logia.quanlyso.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Code.
 */
@RestController
@RequestMapping("/api")
public class CodeResource {

	/** The log. */
	private final Logger		log			= LoggerFactory.getLogger(CodeResource.class);

	/** The Constant ENTITY_NAME. */
	private static final String	ENTITY_NAME	= "code";

	/** The code service. */
	private final CodeService	codeService;

	/** The channel service. */
	private final ChannelService	channelService;

	/**
	 * Instantiates a new code resource.
	 *
	 * @param codeService the code service
	 * @param channelService the channel service
	 */
	public CodeResource(CodeService codeService, ChannelService channelService) {
		this.codeService = codeService;
		this.channelService = channelService;
	}

	/**
	 * POST /codes : Create a new code.
	 *
	 * @param codeDTO the codeDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new codeDTO, or with
	 *         status 400 (Bad Request) if the code has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/codes")
	@Timed
	public ResponseEntity<CodeDTO> createCode(@RequestBody CodeDTO codeDTO)
			throws URISyntaxException {
		this.log.debug("REST request to save Code : {}", codeDTO);
		if (codeDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(CodeResource.ENTITY_NAME,
					"idexists", "A new code cannot already have an ID")).body(null);
		}
		CodeDTO result = this.codeService.save(codeDTO);
		return ResponseEntity
				.created(new URI("/api/codes/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(CodeResource.ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /codes : Updates an existing code.
	 *
	 * @param codeDTO the codeDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated codeDTO,
	 *         or with status 400 (Bad Request) if the codeDTO is not valid,
	 *         or with status 500 (Internal Server Error) if the codeDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/codes")
	@Timed
	public ResponseEntity<CodeDTO> updateCode(@RequestBody CodeDTO codeDTO)
			throws URISyntaxException {
		this.log.debug("REST request to update Code : {}", codeDTO);
		if (codeDTO.getId() == null) {
			return this.createCode(codeDTO);
		}
		CodeDTO result = this.codeService.save(codeDTO);
		return ResponseEntity.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert(CodeResource.ENTITY_NAME, codeDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET /codes : get all the codes.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of codes in body
	 */
	@GetMapping("/codes")
	@Timed
	public ResponseEntity<List<CodeDTO>> getAllCodes(@ApiParam Pageable pageable) {
		this.log.debug("REST request to get a page of Codes");
		Page<CodeDTO> page = this.codeService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/codes");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /codes/:id : get the "id" code.
	 *
	 * @param id the id of the codeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the codeDTO, or with status 404
	 *         (Not Found)
	 */
	@GetMapping("/codes/{id}")
	@Timed
	public ResponseEntity<CodeDTO> getCode(@PathVariable Long id) {
		this.log.debug("REST request to get Code : {}", id);
		CodeDTO codeDTO = this.codeService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(codeDTO));
	}

	/**
	 * DELETE /codes/:id : delete the "id" code.
	 *
	 * @param id the id of the codeDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/codes/{id}")
	@Timed
	public ResponseEntity<Void> deleteCode(@PathVariable Long id) {
		this.log.debug("REST request to delete Code : {}", id);
		this.codeService.delete(id);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityDeletionAlert(CodeResource.ENTITY_NAME, id.toString())).build();
	}

	/**
	 * POST /codes/crawl : Crawl codes data from other website.
	 *
	 * @param __crawlRequestDTO the crawl request DTO
	 * @return the response entity with status 201 (Created)
	 * @throws Exception the exception
	 */
	@PostMapping(value = "/codes/crawl", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux crawlData(@RequestBody CrawlRequestDTO __crawlRequestDTO) throws Exception {
		this.log.debug("REST request to crawl code data from other website");
		if (__crawlRequestDTO == null) {
			LocalDate _localDate = LocalDate.now(DateFormatterUtil.systemZoneId());
			DayOfWeek _openDay = _localDate.getDayOfWeek();
			List<ChannelDTO> channelDTOs = this.channelService.findAllByOpenDay(_openDay);
			Set<String> _codes = new HashSet<>();
			_codes = channelDTOs.parallelStream().map(_dto -> _dto.getCode()).collect(Collectors.toSet());
			
			__crawlRequestDTO = new CrawlRequestDTO(_codes, _openDay);
		}
		this.codeService.crawlLotteriesFromMinhNgocSite("tp-hcm", "28-02-2017", true);
		return ResponseEntity.created(new URI("/api/codes")).build();
	}

}
