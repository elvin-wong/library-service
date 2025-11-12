/*
 * Copyright 2024-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ttddyy.book.libraryservice.book.category;

import jakarta.persistence.*;

import java.time.Instant;

/**
 * Entity class for book categories.
 *
 * @author Tadaya Tsuyukubo
 */
@Entity(name = "Category")
@Table(name = "book_categories")
public class BookCategory {

	@Id
	@Column(name = "id")
	private Long id;

	@Version
	private Integer version;

	@Column(name = "starting_book_id")
	private Long startingId;

	@Column(name = "ending_book_id")
	private Long endingId;

	private String description;

	private String schoolId;

	@Column(name = "ccode_content_digits")
	private String cCodeDigits;

	@Column(insertable = false, updatable = false)
	private Instant createdAt;

	@Column(insertable = false, updatable = false)
	private Instant updatedAt;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getStartingId() {
		return this.startingId;
	}

	public void setStartingId(Long startingId) {
		this.startingId = startingId;
	}

	public Long getEndingId() {
		return this.endingId;
	}

	public void setEndingId(Long endingId) {
		this.endingId = endingId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSchoolId() {
		return this.schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getCCodeDigits() {
		return this.cCodeDigits;
	}

	public void setCCodeDigits(String cCodeDigits) {
		this.cCodeDigits = cCodeDigits;
	}

	public Instant getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

}
