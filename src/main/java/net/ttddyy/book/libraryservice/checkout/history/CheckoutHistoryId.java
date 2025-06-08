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

package net.ttddyy.book.libraryservice.checkout.history;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

/**
 * @author Tadaya Tsuyukubo
 */
@Embeddable
public class CheckoutHistoryId implements Serializable {

	private Long bookId;

	private Long memberId;

	public CheckoutHistoryId() {
	}

	public CheckoutHistoryId(Long bookId, Long memberId) {
		this.bookId = bookId;
		this.memberId = memberId;
	}

	public Long getBookId() {
		return this.bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Long getMemberId() {
		return this.memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CheckoutHistoryId that = (CheckoutHistoryId) o;
		return Objects.equals(bookId, that.bookId) && Objects.equals(memberId, that.memberId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(bookId, memberId);
	}

}
