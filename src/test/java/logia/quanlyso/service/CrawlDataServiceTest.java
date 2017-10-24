/*
 * 
 */
package logia.quanlyso.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.QuanlysoApp;
import logia.quanlyso.domain.Channel;
import logia.quanlyso.domain.Code;
import logia.quanlyso.repository.ChannelRepository;
import logia.quanlyso.repository.CodeRepository;
import logia.quanlyso.service.util.DateFormatterUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
@Transactional
public class CrawlDataServiceTest {

	@Autowired
	private CodeService codeService;

	@Autowired
	private ChannelRepository channelRepository;

	@Autowired
	private CodeRepository codeRepository;

	/**
	 * @throws Exception
	 */
	@Test
	public void assertCrawlDataHaveValue() throws Exception {
		// Init & save data to db
		Channel _channel = channelRepository.findOneByCode("mien-bac");
		String _channelCode = _channel.getCode();
		String _date = "01-07-2017";
		ZonedDateTime _openDate = DateFormatterUtil.fromDDMMYYYYStringToZonedDateTime(_date);
		boolean __forceUpdate = true;
		this.codeService.crawlLotteriesFromMinhNgocSite(_channelCode, _date, __forceUpdate);

		// Assert value
		List<Code> _codes = this.codeRepository.findAllByChannelsAndOpenDate(_channel, _openDate);
		assertThat(_codes).isNotEmpty();
	}
}
