/*
 * 
 */
package logia.quanlyso.config;

import io.github.jhipster.config.JHipsterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * The Class CloudDatabaseConfiguration.
 *
 * @author Dai Mai
 */
@Configuration
@Profile(JHipsterConstants.SPRING_PROFILE_CLOUD)
public class CloudDatabaseConfiguration extends AbstractCloudConfig {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(CloudDatabaseConfiguration.class);

	/**
	 * Data source.
	 *
	 * @param cacheManager
	 *            the cache manager
	 * @return the data source
	 */
	@Bean
	public DataSource dataSource(CacheManager cacheManager) {
		this.log.info("Configuring JDBC datasource from a cloud provider");
		return this.connectionFactory().dataSource();
	}
}
