package com.tripmint.service;

import com.tripmint.model.HealthResponse;
import java.time.Clock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

	private final Clock clock;
	private final String applicationName;

	public HealthService(
			Clock clock,
			@Value("${spring.application.name:tripmint-backend}") String applicationName) {
		this.clock = clock;
		this.applicationName = applicationName;
	}

	public HealthResponse getHealth() {
		return new HealthResponse("UP", applicationName, clock.instant());
	}
}
