package logia.quanlyso.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import logia.quanlyso.service.StyleService;
import logia.quanlyso.service.dto.StyleDTO;
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
 * REST controller for managing Style.
 *
 * @author Dai Mai
 */
@RestController
@RequestMapping("/api")
public class StyleResource {

	/** The log. */
	private final Logger		log			= LoggerFactory.getLogger(StyleResource.class);

	/** The Constant ENTITY_NAME. */
	private static final String	ENTITY_NAME	= "style";

	/** The style service. */
	private final StyleService	styleService;

	/**
	 * Instantiates a new style resource.
	 *
	 * @param styleService the style service
	 */
	public StyleResource(StyleService styleService) {
		this.styleService = styleService;
	}

	/**
	 * POST /styles : Create a new style.
	 *
	 * @param styleDTO the styleDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new styleDTO, or with
	 *         status 400 (Bad Request) if the style has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/styles")
	@Timed
	public ResponseEntity<StyleDTO> createStyle(@RequestBody StyleDTO styleDTO)
			throws URISyntaxException {
		this.log.debug("REST request to save Style : {}", styleDTO);
		if (styleDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StyleResource.ENTITY_NAME,
					"idexists", "A new style cannot already have an ID")).body(null);
		}
		StyleDTO result = this.styleService.save(styleDTO);
		return ResponseEntity
				.created(new URI("/api/styles/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(StyleResource.ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /styles : Updates an existing style.
	 *
	 * @param styleDTO the styleDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated styleDTO,
	 *         or with status 400 (Bad Request) if the styleDTO is not valid,
	 *         or with status 500 (Internal Server Error) if the styleDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/styles")
	@Timed
	public ResponseEntity<StyleDTO> updateStyle(@RequestBody StyleDTO styleDTO)
			throws URISyntaxException {
		this.log.debug("REST request to update Style : {}", styleDTO);
		if (styleDTO.getId() == null) {
			return this.createStyle(styleDTO);
		}
		StyleDTO result = this.styleService.save(styleDTO);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(StyleResource.ENTITY_NAME, styleDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET /styles : get all the styles.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of styles in body
	 */
	@GetMapping("/styles")
	@Timed
	public List<StyleDTO> getAllStyles() {
		this.log.debug("REST request to get all Styles");
		return this.styleService.findAll();
	}

	/**
	 * GET /styles/:id : get the "id" style.
	 *
	 * @param id the id of the styleDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the styleDTO, or with status
	 *         404 (Not Found)
	 */
	@GetMapping("/styles/{id}")
	@Timed
	public ResponseEntity<StyleDTO> getStyle(@PathVariable Long id) {
		this.log.debug("REST request to get Style : {}", id);
		StyleDTO styleDTO = this.styleService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(styleDTO));
	}

	/**
	 * DELETE /styles/:id : delete the "id" style.
	 *
	 * @param id the id of the styleDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/styles/{id}")
	@Timed
	public ResponseEntity<Void> deleteStyle(@PathVariable Long id) {
		this.log.debug("REST request to delete Style : {}", id);
		this.styleService.delete(id);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityDeletionAlert(StyleResource.ENTITY_NAME, id.toString())).build();
	}

}
