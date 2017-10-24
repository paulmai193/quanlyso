/*
 * 
 */
package logia.quanlyso.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import logia.quanlyso.QuanlysoApp;
import logia.quanlyso.web.rest.vm.LoggerVM;

/**
 * Test class for the LogsResource REST controller.
 *
 * @see LogsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
public class LogsResourceIntTest {

	/** The rest logs mock mvc. */
	private MockMvc restLogsMockMvc;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		LogsResource logsResource = new LogsResource();
		this.restLogsMockMvc = MockMvcBuilders.standaloneSetup(logsResource).build();
	}

	/**
	 * Gets the all logs.
	 *
	 * @return the all logs
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void getAllLogs() throws Exception {
		this.restLogsMockMvc.perform(MockMvcRequestBuilders.get("/management/logs"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}

	/**
	 * Change logs.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void changeLogs() throws Exception {
		LoggerVM logger = new LoggerVM();
		logger.setLevel("INFO");
		logger.setName("ROOT");

		this.restLogsMockMvc
				.perform(MockMvcRequestBuilders.put("/management/logs").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(logger)))
				.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
}
