package logia.quanlyso.service.impl;

import java.text.MessageFormat;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import logia.quanlyso.client.HttpUnitRequest;
import logia.quanlyso.domain.Channel;
import logia.quanlyso.domain.Code;
import logia.quanlyso.domain.CostFactor;
import logia.quanlyso.domain.Factor;
import logia.quanlyso.domain.ProfitFactor;
import logia.quanlyso.domain.Style;
import logia.quanlyso.domain.StyleConstants;
import logia.quanlyso.domain.TransactionDetails;
import logia.quanlyso.domain.Transactions;
import logia.quanlyso.domain.Types;
import logia.quanlyso.domain.TypesConstants;
import logia.quanlyso.listener.ProcessingListener;
import logia.quanlyso.repository.ChannelRepository;
import logia.quanlyso.repository.CodeRepository;
import logia.quanlyso.repository.CostFactorRepository;
import logia.quanlyso.repository.ProfitFactorRepository;
import logia.quanlyso.service.CodeService;
import logia.quanlyso.service.dto.CodeDTO;
import logia.quanlyso.service.dto.ProcessingDTO;
import logia.quanlyso.service.mapper.CodeMapper;
import logia.quanlyso.service.util.DateFormatterUtil;

/**
 * Service Implementation for managing Code.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class CodeServiceImpl implements CodeService {

	/** The log. */
	private final Logger						log	= LoggerFactory
			.getLogger(CodeServiceImpl.class);

	/** The code repository. */
	private final CodeRepository				codeRepository;

	/** The code mapper. */
	private final CodeMapper					codeMapper;

	/** The cost factor repository. */
	private final CostFactorRepository			costFactorRepository;

	/** The profit factor repository. */
	private final ProfitFactorRepository		profitFactorRepository;

	/** The channel repository. */
	private final ChannelRepository			channelRepository;

	/** The base url. */
	private final String					baseUrl;
	
	/** The processing listener. */
	private final ProcessingListener processingListener;

	/**
	 * Instantiates a new code service impl.
	 *
	 * @param codeRepository the code repository
	 * @param codeMapper the code mapper
	 * @param costFactorRepository the cost factor repository
	 * @param profitFactorRepository the profit factor repository
	 * @param channelRepository the channel repository
	 * @param __processingListener the processing listener
	 */
	public CodeServiceImpl(CodeRepository codeRepository, CodeMapper codeMapper,
			CostFactorRepository costFactorRepository,
	        ProfitFactorRepository profitFactorRepository, ChannelRepository channelRepository, ProcessingListener __processingListener) {
		this.codeRepository = codeRepository;
		this.codeMapper = codeMapper;
		this.costFactorRepository = costFactorRepository;
		this.profitFactorRepository = profitFactorRepository;
		this.channelRepository = channelRepository;
		this.baseUrl = "http://www.minhngoc.net.vn/getkqxs";
		this.processingListener = __processingListener;
	}

	/**
	 * Save a code.
	 *
	 * @param codeDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public CodeDTO save(CodeDTO codeDTO) {
		this.log.debug("Request to save Code : {}", codeDTO);
		Code code = this.codeMapper.toEntity(codeDTO);
		code = this.codeRepository.save(code);
		CodeDTO result = this.codeMapper.toDto(code);
		return result;
	}

	/**
	 * Get all the codes.
	 * 
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<CodeDTO> findAll(Pageable pageable) {
		this.log.debug("Request to get all Codes");
		Page<Code> result = this.codeRepository.findAll(pageable);
		return result.map(code -> this.codeMapper.toDto(code));
	}

	/**
	 * Get one code by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public CodeDTO findOne(Long id) {
		this.log.debug("Request to get Code : {}", id);
		Code code = this.codeRepository.findOne(id);
		CodeDTO codeDTO = this.codeMapper.toDto(code);
		return codeDTO;
	}

	/**
	 * Delete the code by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		this.log.debug("Request to delete Code : {}", id);
		this.codeRepository.delete(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logia.quanlyso.service.CodeService#calculate(logia.quanlyso.domain.Transactions)
	 */
	@Override
	public Transactions calculate(Transactions __transactions) {
		String _chosenNumber = __transactions.getChosenNumber();
		float _netValue = 0f;
		for (TransactionDetails _details : __transactions.getTransactionDetails()) {
			// Get all condition
			Channel _channel = _details.getChannels();
			Factor _factor = _details.getFactors();
			Style _style = _details.getStyles();
			Types _types = _details.getTypes();

			// Get open date
			ZonedDateTime _openDate = __transactions.getOpenDate();
			if (_openDate == null) {
				// If not set, Assume transactions of current date
				_openDate = ZonedDateTime.now(DateFormatterUtil.systemZoneId());	
			}
			else {
				_openDate = _openDate.withZoneSameInstant(DateFormatterUtil.systemZoneId());
			}
			ZonedDateTime _formattedDate = _openDate.withHour(0).withMinute(0).withSecond(0).withNano(0);
			List<Code> _listCodes = this.codeRepository.findAllByChannelsAndOpenDate(_channel,
					_formattedDate);
			_listCodes = this.getMatchCondition(_chosenNumber, _listCodes, _style, _types);
			CostFactor _costFactor = this.costFactorRepository
					.findOneByFactorsAndStylesAndTypes(_factor, _style, _types);
			ProfitFactor _profitFactor = this.profitFactorRepository
					.findOneByFactorsAndStylesAndTypes(_factor, _style, _types);
			float _amount = _details.getAmount();
			_details.costs(_amount * _costFactor.getRate()).profit(_amount * _profitFactor.getRate() * _listCodes.size());

			_netValue = _netValue + _details.getProfit() - _details.getCosts();
		}
		__transactions.setNetValue(_netValue);

		return __transactions;
	}

	/**
	 * Gets the match condition.
	 *
	 * @param chosenNumber the chosen number
	 * @param listCodes the list codes
	 * @param style the style
	 * @param types the types
	 * @return the match condition
	 */
	private List<Code> getMatchCondition(String chosenNumber, List<Code> listCodes, Style style,
			Types types) {
		List<Code> filtered = new ArrayList<>();

		if (types.getId().equals(TypesConstants.TOP.getId())) {
			if (style.getId().equals(StyleConstants.TWO_NUM.getId())) {
				filtered = this.filterCodeOnTop(listCodes, 2);
			}
			else if (style.getId().equals(StyleConstants.THREE_NUM.getId())) {
				filtered = this.filterCodeOnTop(listCodes, 3);
			}
		}
		else if (types.getId().equals(TypesConstants.BOTH.getId())) {
			if (style.getId().equals(StyleConstants.TWO_NUM.getId())) {
				filtered = this.filterCodeOnTop(listCodes, 2);
			}
			else if (style.getId().equals(StyleConstants.THREE_NUM.getId())) {
				filtered = this.filterCodeOnTop(listCodes, 3);
			}
			filtered.addAll(this.filterCodeOnBottom(listCodes));
		}
		else if (types.getId().equals(TypesConstants.BOTTOM.getId())) {
			filtered = this.filterCodeOnBottom(listCodes);
		}
		else if (types.getId().equals(TypesConstants.ROLL.getId())) {
			filtered = listCodes;
		}

		return filtered.stream().filter(code -> code.getCode().contains(chosenNumber))
		        .collect(Collectors.toList());
	}

	/**
	 * Filter code on top.
	 *
	 * @param codes the codes
	 * @param length the length
	 * @return the list
	 */
	private List<Code> filterCodeOnTop(List<Code> codes, int length) {
		List<Code> filtered = new ArrayList<>();
		filtered = codes.stream().filter(code -> code.getCode().length() == length)
				.collect(Collectors.toList());
		return filtered;
	}

	/**
	 * Filter code on bottom.
	 *
	 * @param codes the codes
	 * @return the list
	 */
	private List<Code> filterCodeOnBottom(List<Code> codes) {
		List<Code> filtered = new ArrayList<>();
		filtered = codes.stream().filter(code -> code.getCode().length() == 6)
				.collect(Collectors.toList()); // 6 is max length of code
		return filtered;
	}

	/* (non-Javadoc)
	 * @see logia.quanlyso.service.CodeService#crawlLotteriesFromMinhNgocSite(java.util.Collection)
	 */
	@Override
	public void crawlLotteriesFromMinhNgocSite(Collection<String> __channelCodes) throws Exception {
		this.processingListener.setTotal(__channelCodes.size());
		for (String _channelCode : __channelCodes) {
			this.crawlLotteriesFromMinhNgocSite(_channelCode);
			this.processingListener.nextProcessing();
		}
		this.processingListener.resetProcessing();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logia.lottery.center.api.service.CrawlDataService#crawlLotteriesFromMinhNgocSite(java.lang.
	 * String)
	 */
	@Override
	@Transactional
	public void crawlLotteriesFromMinhNgocSite(String __channelCode) throws Exception {
		// Get channel by code
		Channel _channel = this.findChannelByCode(__channelCode);

		String _url = this.baseUrl + "/{0}.js";
		_url = MessageFormat.format(_url, __channelCode);
		try (HttpUnitRequest _httpUnitRequest = new HttpUnitRequest(_url);) {
			HtmlPage _page = _httpUnitRequest.crawl();

			ZonedDateTime _openDay;

			List<HtmlSelect> _selectTags = _page.getByXPath("//*[@id=\"box_kqxs_ngay\"]");
			if (_selectTags.size() > 0) {
				HtmlSelect _htmlSelect = _selectTags.get(0);
				HtmlOption _optionTag = _htmlSelect.getSelectedOptions().get(0);
				String _lastSubmidDate = _optionTag.getValueAttribute();
				_openDay = DateFormatterUtil.fromDDMMYYYYStringToZonedDateTime(_lastSubmidDate);
			}
			else {
				throw new NullPointerException("ID box_kqxs_ngay not found");
			}
			List<Code> _codes = this.codeRepository.findAllByChannelsAndOpenDate(_channel,
			        _openDay);
			if (_codes.size() > 0) {
				// Data was crawl before, skip and run next region
				return;
			}

			// Crawl code data and push into list entities
			this.crawlLotteryData(_page, _channel, _openDay, _codes);

			// Save all entities into db
			this.codeRepository.save(_codes);
		}
		catch (Exception __ex) {
			throw __ex;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logia.lottery.center.api.service.CrawlDataService#crawlLotteriesFromMinhNgocSite(java.lang.
	 * String, java.lang.String, boolean)
	 */
	@Override
	@Transactional
	public void crawlLotteriesFromMinhNgocSite(String __channelCode, String __date,
	        boolean __forceUpdate) throws Exception {
		// Get channel by code
		Channel _channel = this.findChannelByCode(__channelCode);

		// Check update or insert new code
		ZonedDateTime _openDay = DateFormatterUtil.fromDDMMYYYYStringToZonedDateTime(__date);
		List<Code> _codes = this.codeRepository.findAllByChannelsAndOpenDate(_channel, _openDay);
		if (_codes.size() > 0) {
			if (__forceUpdate) {
				this.codeRepository.delete(_codes);
				_codes.clear();
			}
			else {
				return;
			}
		}

		String _url;
		if (__date == null || __date.isEmpty()) {
			_url = this.baseUrl + "/{0}.js";
		}
		else {
			_url = this.baseUrl + "/{0}/{1}.js";
		}
		_url = MessageFormat.format(_url, __channelCode, __date);
		this.log.debug("Trying crawl data from URL " + _url);
		try (HttpUnitRequest _httpUnitRequest = new HttpUnitRequest(_url);) {
			HtmlPage _page = _httpUnitRequest.crawl();

			// Crawl code data and push into list entities
			this.crawlLotteryData(_page, _channel, _openDay, _codes);

			// Save all entities into db
			this.codeRepository.save(_codes);
		}
		catch (Exception __ex) {
			throw __ex;
		}
	}
	
	/* (non-Javadoc)
	 * @see logia.quanlyso.service.CodeService#getCrawlProcessing()
	 */
	@Override
	public ProcessingDTO getCrawlProcessing() {
		ProcessingDTO _dto = new ProcessingDTO(this.processingListener.getProcessing(),
		        this.processingListener.getTotal());
		return _dto;
	}

	/**
	 * Crawl lottery data.
	 *
	 * @param __page the page
	 * @param __channel the channel
	 * @param __date the date
	 * @param __codes the codes
	 * @throws ParseException the parse exception
	 */
	private void crawlLotteryData(HtmlPage __page, Channel __channel, ZonedDateTime __date,
	        List<Code> __codes) throws ParseException {
		// KQXS mien nam
		List<HtmlTable> _tableTags = __page.getByXPath("/html/body/div[2]/div[1]/div/div[2]/table");
		if (_tableTags.size() == 0) {
			// KQXS mien bac
			_tableTags = __page.getByXPath("/html/body/div[2]/div/div[2]/table");
			if (_tableTags.size() == 0) {
				throw new NullPointerException("KQXS not found");
			}
		}

		for (HtmlTable _tableTag : _tableTags) {
			List<HtmlTableRow> _rowTags = _tableTag.getRows();
			for (HtmlTableRow _rowTag : _rowTags) {
				List<HtmlTableCell> _cells = _rowTag.getCells();
				for (HtmlTableCell _cell : _cells) {
					if (_cell.getAttribute("class").toLowerCase().contains("giai")
					        && !_cell.hasAttribute("nowrap")) {
						String[] _lotteryCodes = _cell.asText().split("-");
						for (String _lotteryCode : _lotteryCodes) {
							Code _lottery = new Code().code(_lotteryCode.trim()).openDate(__date)
							        .channels(__channel);
							__codes.add(_lottery);
						}
					}
				}
			}
		}
	}

	/**
	 * Find channel by code.
	 *
	 * @param __code the code
	 * @return the channel
	 * @throws NullPointerException the null pointer exception
	 */
	private Channel findChannelByCode(String __code) throws NullPointerException {
		Channel _channel = this.channelRepository.findOneByCode(__code);
		if (_channel == null) {
			throw new NullPointerException("Region " + __code + " not found");
		}
		return _channel;
	}
}
