package logia.quanlyso.config;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import io.github.jhipster.config.JHipsterConstants;
import io.github.jhipster.config.JHipsterProperties;

/**
 * The Class CacheConfiguration.
 *
 * @author Dai Mai
 */
@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

	/** The log. */
	private final Logger		log	= LoggerFactory.getLogger(CacheConfiguration.class);

	/** The env. */
	private final Environment	env;

	/**
	 * Instantiates a new cache configuration.
	 *
	 * @param env the env
	 */
	public CacheConfiguration(Environment env) {
		this.env = env;
	}

	/**
	 * Destroy.
	 */
	@PreDestroy
	public void destroy() {
		this.log.info("Closing Cache Manager");
		Hazelcast.shutdownAll();
	}

	/**
	 * Cache manager.
	 *
	 * @param hazelcastInstance the hazelcast instance
	 * @return the cache manager
	 */
	@Bean
	public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
		this.log.debug("Starting HazelcastCacheManager");
		CacheManager cacheManager = new com.hazelcast.spring.cache.HazelcastCacheManager(
				hazelcastInstance);
		return cacheManager;
	}

	/**
	 * Hazelcast instance.
	 *
	 * @param jHipsterProperties the j hipster properties
	 * @return the hazelcast instance
	 */
	@Bean
	public HazelcastInstance hazelcastInstance(JHipsterProperties jHipsterProperties) {
		this.log.debug("Configuring Hazelcast");
		HazelcastInstance hazelCastInstance = Hazelcast.getHazelcastInstanceByName("quanlyso");
		if (hazelCastInstance != null) {
			this.log.debug("Hazelcast already initialized");
			return hazelCastInstance;
		}
		Config config = new Config();
		config.setInstanceName("quanlyso");
		config.getNetworkConfig().setPort(5701);
		config.getNetworkConfig().setPortAutoIncrement(true);

		// In development, remove multicast auto-configuration
		if (this.env.acceptsProfiles(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)) {
			System.setProperty("hazelcast.local.localAddress", "127.0.0.1");

			config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(false);
			config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
			config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
		}
		config.getMapConfigs().put("default", this.initializeDefaultMapConfig());
		config.getMapConfigs().put("logia.quanlyso.domain.*",
				this.initializeDomainMapConfig(jHipsterProperties));
		return Hazelcast.newHazelcastInstance(config);
	}

	/**
	 * Initialize default map config.
	 *
	 * @return the map config
	 */
	private MapConfig initializeDefaultMapConfig() {
		MapConfig mapConfig = new MapConfig();

		/*
		 * Number of backups. If 1 is set as the backup-count for example,
		 * then all entries of the map will be copied to another JVM for
		 * fail-safety. Valid numbers are 0 (no backup), 1, 2, 3.
		 */
		mapConfig.setBackupCount(0);

		/*
		 * Valid values are:
		 * NONE (no eviction),
		 * LRU (Least Recently Used),
		 * LFU (Least Frequently Used).
		 * NONE is the default.
		 */
		mapConfig.setEvictionPolicy(EvictionPolicy.LRU);

		/*
		 * Maximum size of the map. When max size is reached,
		 * map is evicted based on the policy defined.
		 * Any integer between 0 and Integer.MAX_VALUE. 0 means
		 * Integer.MAX_VALUE. Default is 0.
		 */
		mapConfig
		.setMaxSizeConfig(new MaxSizeConfig(0, MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE));

		return mapConfig;
	}

	/**
	 * Initialize domain map config.
	 *
	 * @param jHipsterProperties the j hipster properties
	 * @return the map config
	 */
	private MapConfig initializeDomainMapConfig(JHipsterProperties jHipsterProperties) {
		MapConfig mapConfig = new MapConfig();
		mapConfig.setTimeToLiveSeconds(
				jHipsterProperties.getCache().getHazelcast().getTimeToLiveSeconds());
		return mapConfig;
	}
}
