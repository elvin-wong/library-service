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

import java.util.Set;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
public class BookCategoryService {

	private final BookCategoryRepository categoryRepository;

	public BookCategoryService(BookCategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public Page<BookCategory> list(@Nullable String schoolId, Pageable pageable) {
		BookCategory category = new BookCategory();
		category.setSchoolId(schoolId);
		// For c-code, ignore the field when the set is empty
		ExampleMatcher matcher = ExampleMatcher.matching()
			.withTransformer("cCodeDigits", (optional) -> optional.map(val -> ((Set<?>) val).isEmpty() ? null : val));
		return this.categoryRepository.findAll(Example.of(category, matcher), pageable);
	}

}
