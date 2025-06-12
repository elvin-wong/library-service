/*
 * Copyright 2024 the original author or authors.
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

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import org.springframework.util.StringUtils;

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
	@Convert(converter = CCodeContentDigitConverter.class)
	private Set<String> cCodeDigits = new HashSet<>();

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

	public Set<String> getCCodeDigits() {
		return this.cCodeDigits;
	}

	public void setCCodeDigits(Set<String> cCodeDigits) {
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

	/**
	 * Convert comma delimited String to a Set.
	 */
	static class CCodeContentDigitConverter implements AttributeConverter<Set<String>, String> {

		@Override
		public String convertToDatabaseColumn(Set<String> attribute) {
			// NOTE: this is readonly. Need to consider ordering when we support write.
			return attribute.stream().collect(Collectors.joining(","));
		}

		@Override
		public Set<String> convertToEntityAttribute(String dbData) {
			// NOTE: the column is non-null and default empty string
			if (!StringUtils.hasText(dbData)) {
				return new HashSet<>();
			}
			return Arrays.stream(dbData.split(",")).collect(Collectors.toSet());
		}

	}

}
