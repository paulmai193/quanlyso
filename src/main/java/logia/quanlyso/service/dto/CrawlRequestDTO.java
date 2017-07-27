package logia.quanlyso.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

/**
 * The Class CrawlRequestDTO.
 * 
 * @author Paul Mai
 */
public class CrawlRequestDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 1L;

	/** The channel codes. */
	private Set<String>			channelCodes;

	/** The open day. */
	private ZonedDateTime		openDay;

	/**
	 * Instantiates a new crawl request DTO.
	 */
	public CrawlRequestDTO() {
	}

	/**
	 * Gets the channel codes.
	 *
	 * @return the channel codes
	 */
	public Set<String> getChannelCodes() {
		return channelCodes;
	}


	/**
	 * Sets the channel codes.
	 *
	 * @param channelCodes the new channel codes
	 */
	public void setChannelCodes(Set<String> channelCodes) {
		this.channelCodes = channelCodes;
	}


	/**
	 * Gets the open day.
	 *
	 * @return the open day
	 */
	public ZonedDateTime getOpenDay() {
		return openDay;
	}


	/**
	 * Sets the open day.
	 *
	 * @param openDay the new open day
	 */
	public void setOpenDay(ZonedDateTime openDay) {
		this.openDay = openDay;
	}


}
