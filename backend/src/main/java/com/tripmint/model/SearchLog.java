package com.tripmint.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "search_logs")
public class SearchLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "origin_code", nullable = false, length = 10)
	private String originCode;

	@Column(name = "destination_code", length = 10)
	private String destinationCode;

	@Column(name = "max_budget", precision = 10, scale = 2)
	private BigDecimal maxBudget;

	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	protected SearchLog() {
	}

	public SearchLog(String originCode, String destinationCode, BigDecimal maxBudget) {
		this.originCode = originCode;
		this.destinationCode = destinationCode;
		this.maxBudget = maxBudget;
	}

	@PrePersist
	void prePersist() {
		if (this.createdAt == null) {
			this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
		}
	}

	public Long getId() {
		return id;
	}

	public String getOriginCode() {
		return originCode;
	}

	public String getDestinationCode() {
		return destinationCode;
	}

	public BigDecimal getMaxBudget() {
		return maxBudget;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}
}
