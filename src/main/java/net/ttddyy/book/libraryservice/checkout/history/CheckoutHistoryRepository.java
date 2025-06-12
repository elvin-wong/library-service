
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

/**
 * @author Tadaya Tsuyukubo
 */
@org.springframework.stereotype.Repository
public interface CheckoutHistoryRepository extends Repository<CheckoutHistory, CheckoutHistoryId> {

	@Query(value = "SELECT co FROM CheckoutHistory co LEFT JOIN FETCH co.member LEFT JOIN FETCH co.book WHERE co.book.id = :bookId",
			countQuery = "SELECT count(co) FROM CheckoutHistory co WHERE co.book.id = :bookId")
	Page<CheckoutHistory> findAllByBookId(@Param("bookId") long bookId, Pageable pageable);

	@Query(value = "SELECT co FROM CheckoutHistory co LEFT JOIN FETCH co.member LEFT JOIN FETCH co.book WHERE co.member.id = :memberId",
			countQuery = "SELECT count(co) FROM CheckoutHistory co WHERE co.member.id = :memeberId")
	Page<CheckoutHistory> findAllByMemberId(@Param("memberId") long memberId, Pageable pageable);

	// TODO: book entity has eager association to book-category. If not specifying the
	// join, query doesn't fetch it and causes extra query to get it.
	// Once eager association is fixed, the join fetch should be removed as the history
	// dto doesn't use it.
	@Query("FROM CheckoutHistory history LEFT JOIN FETCH history.book LEFT JOIN FETCH history.book.category LEFT JOIN FETCH history.member WHERE history.id = :id")
	@Nullable
	CheckoutHistory findById(CheckoutHistoryId id);

}
