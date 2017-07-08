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
	 * @param __regionCode the region code
	 * @throws Exception the exception
	 */
	void crawlLotteriesFromMinhNgocSite(String __regionCode) throws Exception;

	/**
	 * Crawl lotteries from minh ngoc site.
	 *
	 * @param __regionCode the region code
	 * @param __date the date
	 * @param __forceUpdate the force update
	 * @throws Exception the exception
	 */
	void crawlLotteriesFromMinhNgocSite(String __regionCode, String __date, boolean __forceUpdate)
			throws Exception;
}
