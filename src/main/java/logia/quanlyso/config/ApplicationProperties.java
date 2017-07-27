package logia.quanlyso.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to JHipster.
 * 
 * <p>
 * Properties are configured in the application.yml file.
 * </p>
 *
 * @author Dai Mai
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

}
