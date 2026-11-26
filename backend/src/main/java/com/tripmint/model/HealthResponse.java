package com.tripmint.model;

import java.time.Instant;

public record HealthResponse(String status, String service, Instant timestamp) {
}
