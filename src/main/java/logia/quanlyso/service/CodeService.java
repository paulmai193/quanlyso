/*
 * 
 */
package logia.quanlyso.service;

import logia.quanlyso.domain.Transactions;
import logia.quanlyso.service.dto.CodeDTO;
import logia.quanlyso.service.dto.ProcessingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * Service Interface for managing Code.
 */
public interface CodeService {

	/**
	 * Save a code.
	 *
	 * @param codeDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	CodeDTO save(CodeDTO codeDTO);

	/**
	 * Get all the codes.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	Page<CodeDTO> findAll(Pageable pageable);

	/**
	 * Get the "id" code.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	CodeDTO findOne(Long id);

	/**
	 * Delete the "id" code.
	 *
	 * @param id
	 *            the id of the entity
	 */
	void delete(Long id);

	/**
	 * Calculate the value of transaction.
	 *
	 * @param transactions
	 *            the transactions
	 * @return the transactions
	 */
	Transactions calculate(Transactions transactions);

	/**
	 * Crawl lotteries from minh ngoc site.
	 *
	 * @param __channelCodes
	 *            the channel codes
	 * @throws Exception
	 *             the exception
	 */
	void crawlLotteriesFromMinhNgocSite(Collection<String> __channelCodes) throws Exception;

	/**
	 * Crawl lotteries from minh ngoc site.
	 *
	 * @param __channelCode
	 *            the channel code
	 * @throws Exception
	 *             the exception
	 */
	void crawlLotteriesFromMinhNgocSite(String __channelCode) throws Exception;

	/**
	 * Crawl lotteries from minh ngoc site.
	 *
	 * @param __channelCode
	 *            the channel code
	 * @param __date
	 *            the open day
	 * @param __forceUpdate
	 *            the force update
	 * @throws Exception
	 *             the exception
	 */
	void crawlLotteriesFromMinhNgocSite(String __channelCode, String __date, boolean __forceUpdate) throws Exception;

	/**
	 * Gets the crawl processing.
	 *
	 * @return the crawl processing in percent
	 */
	ProcessingDTO getCrawlProcessing();

}
