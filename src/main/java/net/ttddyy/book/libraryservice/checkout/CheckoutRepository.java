
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

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Tadaya Tsuyukubo
 */
@Repository
public interface CheckoutRepository extends CrudRepository<Checkout, CheckoutId> {

	long countByIdMemberId(long memberId);

	@Query(value = "SELECT co FROM checkout co LEFT JOIN FETCH co.member LEFT JOIN FETCH co.book",
			countQuery = "SELECT count(co) FROM checkout co")
	Page<Checkout> findAll(Pageable pageable);

	@Query(value = "SELECT co FROM checkout co LEFT JOIN FETCH co.member LEFT JOIN FETCH co.book where co.member.id = :memberId",
			countQuery = "SELECT count(co) FROM checkout co WHERE co.member.id = :memeberId")
	Page<Checkout> findAllByMemberId(@Param("memberId") long memberId, Pageable pageable);

	@Query(value = "SELECT co FROM checkout co LEFT JOIN FETCH co.member LEFT JOIN FETCH co.book where co.book.schoolId = :schoolId",
			countQuery = "SELECT count(co) FROM checkout co LEFT JOIN co.book where co.book.schoolId = :schoolId")
	Page<Checkout> findAllBySchoolId(@Param("schoolId") String schoolId, Pageable pageable);

	@Modifying
	@Query("DELETE FROM checkout co WHERE co.id.bookId in :bookIds")
	void deleteAllByIdBookIdIn(Iterable<Long> bookIds);

	@Query("FROM checkout co JOIN FETCH co.book WHERE co.member.id = :memberId")
	List<Checkout> bookDetailsByMemberId(long memberId);

	@Query(value = "SELECT co FROM checkout co LEFT JOIN FETCH co.member LEFT JOIN FETCH co.book WHERE co.dueDate < :dueDate",
			countQuery = "SELECT count(co) FROM checkout co WHERE co.dueDate < :dueDate")
	Page<Checkout> findOverdue(@Param("dueDate") LocalDate dueDate, Pageable pageable);

}
