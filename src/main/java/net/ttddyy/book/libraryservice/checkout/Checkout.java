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

package net.ttddyy.book.libraryservice.checkout;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import net.ttddyy.book.libraryservice.book.Book;
import net.ttddyy.book.libraryservice.member.Member;

/**
 * @author Tadaya Tsuyukubo
 */
@Entity(name = "checkout")
@Table(name = "checkouts")
public class Checkout {

	@EmbeddedId
	private CheckoutId id;

	@MapsId("bookId")
	@JoinColumn(name = "book_id", insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Book book;

	@MapsId("memberId")
	@JoinColumn(name = "member_id", insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@Column(name = "checkout_date")
	private LocalDate checkoutDate;

	@Column(name = "due_date")
	private LocalDate dueDate;

	@Column(insertable = false, updatable = false)
	private Instant createdAt;

	@Column(insertable = false, updatable = false)
	private Instant updatedAt;

	public CheckoutId getId() {
		return this.id;
	}

	public void setId(CheckoutId id) {
		this.id = id;
	}

	public Book getBook() {
		return this.book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public LocalDate getCheckoutDate() {
		return this.checkoutDate;
	}

	public void setCheckoutDate(LocalDate checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public LocalDate getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
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
