/*
 * 
 */
package logia.quanlyso.config;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.ContextAwareBase;
import io.github.jhipster.config.JHipsterProperties;
import net.logstash.logback.appender.LogstashSocketAppender;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * The Class LoggingConfiguration.
 *
 * @author Dai Mai
 */
@Configuration
public class LoggingConfiguration {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(LoggingConfiguration.class);

	/** The context. */
	private LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

	/** The app name. */
	private final String appName;

	/** The server port. */
	private final String serverPort;

	/** The j hipster properties. */
	private final JHipsterProperties jHipsterProperties;

	/**
	 * Instantiates a new logging configuration.
	 *
	 * @param appName
	 *            the app name
	 * @param serverPort
	 *            the server port
	 * @param jHipsterProperties
	 *            the j hipster properties
	 */
	public LoggingConfiguration(@Value("${spring.application.name}") String appName,
			@Value("${server.port}") String serverPort, JHipsterProperties jHipsterProperties) {
		this.appName = appName;
		this.serverPort = serverPort;
		this.jHipsterProperties = jHipsterProperties;
		if (jHipsterProperties.getLogging().getLogstash().isEnabled()) {
			this.addLogstashAppender(this.context);

			// Add context listener
			LogbackLoggerContextListener loggerContextListener = new LogbackLoggerContextListener();
			loggerContextListener.setContext(this.context);
			this.context.addListener(loggerContextListener);
		}
	}

	/**
	 * Adds the logstash appender.
	 *
	 * @param context
	 *            the context
	 */
	public void addLogstashAppender(LoggerContext context) {
		this.log.info("Initializing Logstash logging");

		LogstashSocketAppender logstashAppender = new LogstashSocketAppender();
		logstashAppender.setName("LOGSTASH");
		logstashAppender.setContext(context);
		String customFields = "{\"app_name\":\"" + this.appName + "\",\"app_port\":\"" + this.serverPort + "\"}";

		// Set the Logstash appender config from JHipster properties
		logstashAppender.setSyslogHost(this.jHipsterProperties.getLogging().getLogstash().getHost());
		logstashAppender.setPort(this.jHipsterProperties.getLogging().getLogstash().getPort());
		logstashAppender.setCustomFields(customFields);

		// Limit the maximum length of the forwarded stacktrace so that it won't
		// exceed the 8KB UDP
		// limit of logstash
		ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
		throwableConverter.setMaxLength(7500);
		throwableConverter.setRootCauseFirst(true);
		logstashAppender.setThrowableConverter(throwableConverter);

		logstashAppender.start();

		// Wrap the appender in an Async appender for performance
		AsyncAppender asyncLogstashAppender = new AsyncAppender();
		asyncLogstashAppender.setContext(context);
		asyncLogstashAppender.setName("ASYNC_LOGSTASH");
		asyncLogstashAppender.setQueueSize(this.jHipsterProperties.getLogging().getLogstash().getQueueSize());
		asyncLogstashAppender.addAppender(logstashAppender);
		asyncLogstashAppender.start();

		context.getLogger("ROOT").addAppender(asyncLogstashAppender);
	}

	/**
	 * Logback configuration is achieved by configuration file and API. When
	 * configuration file change is detected, the configuration is reset. This
	 * listener ensures that the programmatic configuration is also re-applied
	 * after reset.
	 *
	 * @see LogbackLoggerContextEvent
	 */
	class LogbackLoggerContextListener extends ContextAwareBase implements LoggerContextListener {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * ch.qos.logback.classic.spi.LoggerContextListener#isResetResistant()
		 */
		@Override
		public boolean isResetResistant() {
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see ch.qos.logback.classic.spi.LoggerContextListener#onStart(ch.qos.
		 * logback.classic. LoggerContext)
		 */
		@Override
		public void onStart(LoggerContext context) {
			LoggingConfiguration.this.addLogstashAppender(context);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see ch.qos.logback.classic.spi.LoggerContextListener#onReset(ch.qos.
		 * logback.classic. LoggerContext)
		 */
		@Override
		public void onReset(LoggerContext context) {
			LoggingConfiguration.this.addLogstashAppender(context);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see ch.qos.logback.classic.spi.LoggerContextListener#onStop(ch.qos.
		 * logback.classic. LoggerContext)
		 */
		@Override
		public void onStop(LoggerContext context) {
			// Nothing to do.
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * ch.qos.logback.classic.spi.LoggerContextListener#onLevelChange(ch.qos
		 * .logback.classic. Logger, ch.qos.logback.classic.Level)
		 */
		@Override
		public void onLevelChange(ch.qos.logback.classic.Logger logger, Level level) {
			// Nothing to do.
		}
	}

}
