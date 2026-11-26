package com.tripmint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tripmint.model.SearchLog;
import com.tripmint.repository.SearchLogRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TripmintBackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SearchLogRepository searchLogRepository;

	@Test
	void contextLoadsAndFlywaySchemaMatchesJpaEntity() {
		SearchLog saved = searchLogRepository.save(new SearchLog("PAR", "LIS", new BigDecimal("180.00")));
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getCreatedAt()).isNotNull();
		assertThat(searchLogRepository.findById(saved.getId())).isPresent();
	}

	@Test
	void healthEndpointReturnsUp() throws Exception {
		mockMvc.perform(get("/api/v1/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("UP"))
				.andExpect(jsonPath("$.service").value("tripmint-backend"))
				.andExpect(jsonPath("$.timestamp").isNotEmpty());
	}

	@Test
	void unknownEndpointReturnsStructuredNotFoundError() throws Exception {
		mockMvc.perform(get("/api/v1/does-not-exist"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.path").value("/api/v1/does-not-exist"));
	}
}
