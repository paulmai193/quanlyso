/*
 * 
 */
package logia.quanlyso.web.rest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.codahale.metrics.annotation.Timed;
import logia.quanlyso.web.rest.vm.LoggerVM;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for view and managing Log Level at runtime.
 *
 * @author Dai Mai
 */
@RestController
@RequestMapping("/management")
public class LogsResource {

	/**
	 * Gets the list.
	 *
	 * @return the list
	 */
	@GetMapping("/logs")
	@Timed
	public List<LoggerVM> getList() {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		return context.getLoggerList().stream().map(LoggerVM::new).collect(Collectors.toList());
	}

	/**
	 * Change level.
	 *
	 * @param jsonLogger
	 *            the json logger
	 */
	@PutMapping("/logs")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Timed
	public void changeLevel(@RequestBody LoggerVM jsonLogger) {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		context.getLogger(jsonLogger.getName()).setLevel(Level.valueOf(jsonLogger.getLevel()));
	}
}
