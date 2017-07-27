package logia.quanlyso.config;

import java.io.File;
import java.nio.file.Paths;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilter;
import com.codahale.metrics.servlets.MetricsServlet;
import com.hazelcast.core.HazelcastInstance;

import io.github.jhipster.config.JHipsterConstants;
import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.web.filter.CachingHttpHeadersFilter;
import io.undertow.UndertowOptions;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 *
 * @author Dai Mai
 */
@Configuration
public class WebConfigurer
implements ServletContextInitializer, EmbeddedServletContainerCustomizer {

	/** The log. */
	private final Logger				log	= LoggerFactory.getLogger(WebConfigurer.class);

	/** The env. */
	private final Environment			env;

	/** The j hipster properties. */
	private final JHipsterProperties	jHipsterProperties;

	/** The hazelcast instance. */
	private final HazelcastInstance		hazelcastInstance;

	/** The metric registry. */
	private MetricRegistry				metricRegistry;

	/**
	 * Instantiates a new web configurer.
	 *
	 * @param env the env
	 * @param jHipsterProperties the j hipster properties
	 * @param hazelcastInstance the hazelcast instance
	 */
	public WebConfigurer(Environment env, JHipsterProperties jHipsterProperties,
			HazelcastInstance hazelcastInstance) {

		this.env = env;
		this.jHipsterProperties = jHipsterProperties;
		this.hazelcastInstance = hazelcastInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.boot.web.servlet.ServletContextInitializer#onStartup(javax.servlet.
	 * ServletContext)
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		if (this.env.getActiveProfiles().length != 0) {
			this.log.info("Web application configuration, using profiles: {}",
					(Object[]) this.env.getActiveProfiles());
		}
		EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD,
				DispatcherType.ASYNC);
		this.initMetrics(servletContext, disps);
		if (this.env.acceptsProfiles(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
			this.initCachingHttpHeadersFilter(servletContext, disps);
		}
		this.log.info("Web application fully configured");
	}

	/**
	 * Customize the Servlet engine: Mime types, the document root, the cache.
	 *
	 * @param container the container
	 */
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
		// IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
		mappings.add("html", "text/html;charset=utf-8");
		// CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
		mappings.add("json", "text/html;charset=utf-8");
		container.setMimeMappings(mappings);
		// When running in an IDE or with ./mvnw spring-boot:run, set location of the static web
		// assets.
		this.setLocationForStaticAssets(container);

		/*
		 * Enable HTTP/2 for Undertow - https://twitter.com/ankinson/status/829256167700492288
		 * HTTP/2 requires HTTPS, so HTTP requests will fallback to HTTP/1.1.
		 * See the JHipsterProperties class and your application-*.yml configuration files
		 * for more information.
		 */
		if (this.jHipsterProperties.getHttp().getVersion().equals(JHipsterProperties.Http.Version.V_2_0)
				&& container instanceof UndertowEmbeddedServletContainerFactory) {

			((UndertowEmbeddedServletContainerFactory) container).addBuilderCustomizers(
					builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));
		}
	}

	/**
	 * Sets the location for static assets.
	 *
	 * @param container the new location for static assets
	 */
	private void setLocationForStaticAssets(ConfigurableEmbeddedServletContainer container) {
		File root;
		String prefixPath = this.resolvePathPrefix();
		root = new File(prefixPath + "target/www/");
		if (root.exists() && root.isDirectory()) {
			container.setDocumentRoot(root);
		}
	}

	/**
	 * Resolve path prefix to static resources.
	 *
	 * @return the string
	 */
	private String resolvePathPrefix() {
		String fullExecutablePath = this.getClass().getResource("").getPath();
		String rootPath = Paths.get(".").toUri().normalize().getPath();
		String extractedPath = fullExecutablePath.replace(rootPath, "");
		int extractionEndIndex = extractedPath.indexOf("target/");
		if (extractionEndIndex <= 0) {
			return "";
		}
		return extractedPath.substring(0, extractionEndIndex);
	}

	/**
	 * Initializes the caching HTTP Headers Filter.
	 *
	 * @param servletContext the servlet context
	 * @param disps the disps
	 */
	private void initCachingHttpHeadersFilter(ServletContext servletContext,
			EnumSet<DispatcherType> disps) {
		this.log.debug("Registering Caching HTTP Headers Filter");
		FilterRegistration.Dynamic cachingHttpHeadersFilter = servletContext.addFilter(
				"cachingHttpHeadersFilter", new CachingHttpHeadersFilter(this.jHipsterProperties));

		cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/content/*");
		cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/app/*");
		cachingHttpHeadersFilter.setAsyncSupported(true);
	}

	/**
	 * Initializes Metrics.
	 *
	 * @param servletContext the servlet context
	 * @param disps the disps
	 */
	private void initMetrics(ServletContext servletContext, EnumSet<DispatcherType> disps) {
		this.log.debug("Initializing Metrics registries");
		servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE, this.metricRegistry);
		servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY, this.metricRegistry);

		this.log.debug("Registering Metrics Filter");
		FilterRegistration.Dynamic metricsFilter = servletContext.addFilter("webappMetricsFilter",
				new InstrumentedFilter());

		metricsFilter.addMappingForUrlPatterns(disps, true, "/*");
		metricsFilter.setAsyncSupported(true);

		this.log.debug("Registering Metrics Servlet");
		ServletRegistration.Dynamic metricsAdminServlet = servletContext
				.addServlet("metricsServlet", new MetricsServlet());

		metricsAdminServlet.addMapping("/management/metrics/*");
		metricsAdminServlet.setAsyncSupported(true);
		metricsAdminServlet.setLoadOnStartup(2);
	}

	/**
	 * Cors filter.
	 *
	 * @return the cors filter
	 */
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = this.jHipsterProperties.getCors();
		if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
			this.log.debug("Registering CORS filter");
			source.registerCorsConfiguration("/api/**", config);
			source.registerCorsConfiguration("/v2/api-docs", config);
		}
		return new CorsFilter(source);
	}

	/**
	 * Sets the metric registry.
	 *
	 * @param metricRegistry the new metric registry
	 */
	@Autowired(required = false)
	public void setMetricRegistry(MetricRegistry metricRegistry) {
		this.metricRegistry = metricRegistry;
	}
}
