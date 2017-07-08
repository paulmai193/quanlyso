package logia.quanlyso.service.impl;

import java.text.MessageFormat;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import logia.quanlyso.repository.ChannelRepository;
import logia.quanlyso.repository.CodeRepository;
import logia.quanlyso.service.CrawlDataService;

/**
 * The Class ImplCrawlDataService.
 *
 * @author Paul Mai
 */
public class ImplCrawlDataService implements CrawlDataService {

	/** The logger. */
	private final Logger			LOGGER	= LoggerFactory.getLogger(this.getClass());

	/** The channel repository. */
	private final ChannelRepository	channelRepository;

	/** The code repository. */
	private final CodeRepository	codeRepository;

	/** The base url. */
	private String					baseUrl	= "http://www.minhngoc.net.vn/getkqxs";

	/**
	 * Instantiates a new impl crawl data service.
	 *
	 * @param channelRepository the channel repository
	 * @param codeRepository the code repository
	 */
	public ImplCrawlDataService(ChannelRepository channelRepository,
			CodeRepository codeRepository) {
		this.channelRepository = channelRepository;
		this.codeRepository = codeRepository;
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
				_openDay = ZonedDateTime.parse(_lastSubmidDate);
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
		ZonedDateTime _openDay = ZonedDateTime.parse(__date);
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
		this.LOGGER.debug("Trying crawl data from URL " + _url);
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
