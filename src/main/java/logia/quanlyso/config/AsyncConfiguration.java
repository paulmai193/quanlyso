package logia.quanlyso.config;

import io.github.jhipster.async.ExceptionHandlingAsyncTaskExecutor;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * The Class AsyncConfiguration.
 *
 * @author Dai Mai
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer {

	/** The log. */
	private final Logger				log	= LoggerFactory.getLogger(AsyncConfiguration.class);

	/** The j hipster properties. */
	private final JHipsterProperties	jHipsterProperties;

	/**
	 * Instantiates a new async configuration.
	 *
	 * @param jHipsterProperties the j hipster properties
	 */
	public AsyncConfiguration(JHipsterProperties jHipsterProperties) {
		this.jHipsterProperties = jHipsterProperties;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.scheduling.annotation.AsyncConfigurer#getAsyncExecutor()
	 */
	@Override
	@Bean(name = "taskExecutor")
	public Executor getAsyncExecutor() {
		this.log.debug("Creating Async Task Executor");
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(this.jHipsterProperties.getAsync().getCorePoolSize());
		executor.setMaxPoolSize(this.jHipsterProperties.getAsync().getMaxPoolSize());
		executor.setQueueCapacity(this.jHipsterProperties.getAsync().getQueueCapacity());
		executor.setThreadNamePrefix("quanlyso-Executor-");
		return new ExceptionHandlingAsyncTaskExecutor(executor);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.scheduling.annotation.AsyncConfigurer#getAsyncUncaughtExceptionHandler()
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new SimpleAsyncUncaughtExceptionHandler();
	}
}
