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

package net.ttddyy.book.libraryservice.book;

import java.time.Clock;

import net.ttddyy.book.libraryservice.book.category.BookCategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Tadaya Tsuyukubo
 */
class BookServiceTests {

	@Test
	@SuppressWarnings("unchecked")
	void searchWithEmptyValues() {
		BookRepository bookRepository = mock(BookRepository.class);
		BookCategoryRepository categoryRepository = mock(BookCategoryRepository.class);
		Clock clock = mock(Clock.class);
		BookService bookService = new BookService(bookRepository, categoryRepository, clock);

		Pageable pageable = Pageable.unpaged();
		bookService.search(null, null, " ", " ", " ", " ", " ", null, pageable);

		ArgumentCaptor<Example<Book>> captor = ArgumentCaptor.forClass(Example.class);
		verify(bookRepository).findAll(captor.capture(), any(Pageable.class));

		Example<Book> example = captor.getValue();
		Book probe = example.getProbe();

		assertThat(probe.getTitle()).isNull();
		assertThat(probe.getTitleKana()).isNull();
		assertThat(probe.getAuthor()).isNull();
		assertThat(probe.getAuthorKana()).isNull();
		assertThat(probe.getPublisher()).isNull();
	}

}
