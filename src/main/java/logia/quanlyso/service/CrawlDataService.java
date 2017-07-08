package logia.quanlyso.service;

/**
 * The Interface CrawlDataService.
 *
 * @author Paul Mai
 */
public interface CrawlDataService {

	/**
	 * Crawl lotteries from minh ngoc site.
	 *
	 * @param __channelCode the channel code
	 * @throws Exception the exception
	 */
	void crawlLotteriesFromMinhNgocSite(String __channelCode) throws Exception;

	/**
	 * Crawl lotteries from minh ngoc site.
	 *
	 * @param __channelCode the channel code
	 * @param __date the open day
	 * @param __forceUpdate the force update
	 * @throws Exception the exception
	 */
	void crawlLotteriesFromMinhNgocSite(String __channelCode, String __date, boolean __forceUpdate)
			throws Exception;
}
