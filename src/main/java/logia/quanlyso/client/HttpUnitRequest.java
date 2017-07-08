package logia.quanlyso.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * The Class HttpUnitRequest.
 *
 * @author Paul Mai
 */
public final class HttpUnitRequest implements AutoCloseable {

	/** The Constant LOGGER. */
	private final Logger	LOGGER	= LoggerFactory.getLogger(this.getClass());

	/** The url. */
	private final String	URL;

	/** The web. */
	private WebClient		web;

	/**
	 * Instantiates a new http unit request.
	 *
	 * @param __url the url
	 */
	public HttpUnitRequest(String __url) {
		super();
		this.URL = __url;
		this.web = new WebClient(BrowserVersion.CHROME);
		this.web.getOptions().setThrowExceptionOnScriptError(false);
		this.web.getOptions().setThrowExceptionOnFailingStatusCode(false);
		this.web.getOptions().setJavaScriptEnabled(true);
		this.web.getOptions().setCssEnabled(true);
		this.web.getOptions().setRedirectEnabled(true);
		this.web.getOptions().setDoNotTrackEnabled(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		this.web.close();
	}

	/**
	 * Crawl.
	 *
	 * @return the html page
	 * @throws Exception the exception
	 */
	public HtmlPage crawl() throws Exception {
		try {
			return this.web.getPage(this.URL);
		}
		catch (Exception __ex) {
			this.LOGGER.error("Error when crawl data from " + this.URL, __ex);
			throw __ex;
		}
	}

	/**
	 * Raw crawl.
	 *
	 * @return the string
	 * @throws Exception the exception
	 */
	public String rawCrawl() throws Exception {
		try {
			return this.web.getPage(this.URL).getWebResponse().getContentAsString();
		}
		catch (Exception __ex) {
			this.LOGGER.error("Error when crawl data from " + this.URL, __ex);
			throw __ex;
		}
	}
}
