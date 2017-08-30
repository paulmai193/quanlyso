package logia.quanlyso.config;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.*;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import com.zaxxer.hikari.HikariDataSource;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * The Class MetricsConfiguration.
 *
 * @author Dai Mai
 */
@Configuration
@EnableMetrics(proxyTargetClass = true)
public class MetricsConfiguration extends MetricsConfigurerAdapter {

	/** The Constant PROP_METRIC_REG_JVM_MEMORY. */
	private static final String			PROP_METRIC_REG_JVM_MEMORY	= "jvm.memory";

	/** The Constant PROP_METRIC_REG_JVM_GARBAGE. */
	private static final String			PROP_METRIC_REG_JVM_GARBAGE	= "jvm.garbage";

	/** The Constant PROP_METRIC_REG_JVM_THREADS. */
	private static final String			PROP_METRIC_REG_JVM_THREADS	= "jvm.threads";

	/** The Constant PROP_METRIC_REG_JVM_FILES. */
	private static final String			PROP_METRIC_REG_JVM_FILES	= "jvm.files";

	/** The Constant PROP_METRIC_REG_JVM_BUFFERS. */
	private static final String			PROP_METRIC_REG_JVM_BUFFERS	= "jvm.buffers";

	/** The log. */
	private final Logger				log							= LoggerFactory
			.getLogger(MetricsConfiguration.class);

	/** The metric registry. */
	private MetricRegistry				metricRegistry				= new MetricRegistry();

	/** The health check registry. */
	private HealthCheckRegistry			healthCheckRegistry			= new HealthCheckRegistry();

	/** The j hipster properties. */
	private final JHipsterProperties	jHipsterProperties;

	/** The hikari data source. */
	private HikariDataSource			hikariDataSource;

	/**
	 * Instantiates a new metrics configuration.
	 *
	 * @param jHipsterProperties the j hipster properties
	 */
	public MetricsConfiguration(JHipsterProperties jHipsterProperties) {
		this.jHipsterProperties = jHipsterProperties;
	}

	/**
	 * Sets the hikari data source.
	 *
	 * @param hikariDataSource the new hikari data source
	 */
	@Autowired(required = false)
	public void setHikariDataSource(HikariDataSource hikariDataSource) {
		this.hikariDataSource = hikariDataSource;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter#getMetricRegistry()
	 */
	@Override
	@Bean
	public MetricRegistry getMetricRegistry() {
		return this.metricRegistry;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter#
	 * getHealthCheckRegistry()
	 */
	@Override
	@Bean
	public HealthCheckRegistry getHealthCheckRegistry() {
		return this.healthCheckRegistry;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		this.log.debug("Registering JVM gauges");
		this.metricRegistry.register(MetricsConfiguration.PROP_METRIC_REG_JVM_MEMORY, new MemoryUsageGaugeSet());
		this.metricRegistry.register(MetricsConfiguration.PROP_METRIC_REG_JVM_GARBAGE, new GarbageCollectorMetricSet());
		this.metricRegistry.register(MetricsConfiguration.PROP_METRIC_REG_JVM_THREADS, new ThreadStatesGaugeSet());
		this.metricRegistry.register(MetricsConfiguration.PROP_METRIC_REG_JVM_FILES, new FileDescriptorRatioGauge());
		this.metricRegistry.register(MetricsConfiguration.PROP_METRIC_REG_JVM_BUFFERS,
				new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
		if (this.hikariDataSource != null) {
			this.log.debug("Monitoring the datasource");
			this.hikariDataSource.setMetricRegistry(this.metricRegistry);
		}
		if (this.jHipsterProperties.getMetrics().getJmx().isEnabled()) {
			this.log.debug("Initializing Metrics JMX reporting");
			JmxReporter jmxReporter = JmxReporter.forRegistry(this.metricRegistry).build();
			jmxReporter.start();
		}
		if (this.jHipsterProperties.getMetrics().getLogs().isEnabled()) {
			this.log.info("Initializing Metrics Log reporting");
			final Slf4jReporter reporter = Slf4jReporter.forRegistry(this.metricRegistry)
					.outputTo(LoggerFactory.getLogger("metrics")).convertRatesTo(TimeUnit.SECONDS)
					.convertDurationsTo(TimeUnit.MILLISECONDS).build();
			reporter.start(this.jHipsterProperties.getMetrics().getLogs().getReportFrequency(),
					TimeUnit.SECONDS);
		}
	}
}
