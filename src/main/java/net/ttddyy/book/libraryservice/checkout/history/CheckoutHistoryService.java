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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Tadaya Tsuyukubo
 */
@Service
@Transactional(readOnly = true)
public class CheckoutHistoryService {

	private final CheckoutHistoryRepository checkoutHistoryRepository;

	public CheckoutHistoryService(CheckoutHistoryRepository checkoutHistoryRepository) {
		this.checkoutHistoryRepository = checkoutHistoryRepository;
	}

	@Nullable
	public CheckoutHistory get(long bookId, long memberId) {
		return this.checkoutHistoryRepository.findById(new CheckoutHistoryId(bookId, memberId));
	}

	public Page<CheckoutHistory> listByBook(long bookId, Pageable pageable) {
		return this.checkoutHistoryRepository.findAllByBookId(bookId, pageable);
	}

	public Page<CheckoutHistory> listByMember(long memberId, Pageable pageable) {
		return this.checkoutHistoryRepository.findAllByMemberId(memberId, pageable);
	}

}
