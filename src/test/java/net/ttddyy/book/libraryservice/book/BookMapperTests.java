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

import net.ttddyy.book.libraryservice.MockClocks.MockClock;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * @author Tadaya Tsuyukubo
 */
class BookMapperTests {

	@Test
	void updateBookFromDto() {
		Book book = new Book();

		BookDtoUpdate dto = new BookDtoUpdate("bar-title", "bar-title-kana", "bar-author", "bar-author-kana",
				"bar-isbn", "bar-comments", "bar-publisher", null, null);
		Clock clock = mock(Clock.class);

		BookMapper.INSTANCE.updateBookFromDto(book, dto, clock);
		assertThat(book.getTitle()).isEqualTo("bar-title");
		assertThat(book.getTitleKana()).isEqualTo("bar-title-kana");
		assertThat(book.getAuthor()).isEqualTo("bar-author");
		assertThat(book.getAuthorKana()).isEqualTo("bar-author-kana");
		assertThat(book.getIsbn()).isEqualTo("bar-isbn");
		assertThat(book.getComments()).isEqualTo("bar-comments");
		assertThat(book.getPublisher()).isEqualTo("bar-publisher");

		verifyNoInteractions(clock);
	}

	@Test
	void updateBookFromDtoWithNull() {
		Book book = new Book();
		book.setTitle("foo-title");
		book.setTitleKana("foo-title-kana");
		book.setAuthor("foo-author");
		book.setAuthorKana("foo-author-kana");
		book.setIsbn("foo-isbn");
		book.setComments("foo-comments");
		book.setPublisher("foo-publisher");

		BookDtoUpdate dto = new BookDtoUpdate(null, null, null, null, null, null, null, null, null);
		Clock clock = mock(Clock.class);

		BookMapper.INSTANCE.updateBookFromDto(book, dto, clock);
		assertThat(book.getTitle()).isEqualTo("foo-title");
		assertThat(book.getTitleKana()).isEqualTo("foo-title-kana");
		assertThat(book.getAuthor()).isEqualTo("foo-author");
		assertThat(book.getAuthorKana()).isEqualTo("foo-author-kana");
		assertThat(book.getIsbn()).isEqualTo("foo-isbn");
		assertThat(book.getComments()).isEqualTo("foo-comments");
		assertThat(book.getPublisher()).isEqualTo("foo-publisher");

		verifyNoInteractions(clock);
	}

	@Test
	void updateBookFromDtoWithDelete() {
		Book book = new Book();

		BookDtoUpdate dto = new BookDtoUpdate("bar-title", null, null, null, null, null, null, true, null);

		MockClock clock = new MockClock("2022-12-01T00:00:00.00Z");

		BookMapper.INSTANCE.updateBookFromDto(book, dto, clock);
		assertThat(book.getTitle()).isEqualTo("bar-title");
		assertThat(book.getDeletedDate()).isEqualTo("2022-12-01");

		assertThat(clock.getCallCount()).isEqualTo(1);

		// now cancel the deletion (delete=false)
		dto = new BookDtoUpdate("bar-title", null, null, null, null, null, null, false, null);
		BookMapper.INSTANCE.updateBookFromDto(book, dto, clock);
		assertThat(book.getDeletedDate()).isNull();
	}

	@Test
	void updateBookFromDtoWithMissing() {
		Book book = new Book();
		assertThat(book.getMissing()).isNull();
		assertThat(book.getLostDate()).isNull();

		// missing=true
		BookDtoUpdate dto = new BookDtoUpdate(null, null, null, null, null, null, null, null, true);
		MockClock clock = new MockClock("2022-12-01T00:00:00.00Z");

		BookMapper.INSTANCE.updateBookFromDto(book, dto, clock);
		assertThat(book.getMissing()).isTrue();
		assertThat(book.getLostDate()).isEqualTo("2022-12-01");

		assertThat(clock.getCallCount()).isEqualTo(1);

		// now cancel missing (missing=false)
		dto = new BookDtoUpdate(null, null, null, null, null, null, null, null, false);
		BookMapper.INSTANCE.updateBookFromDto(book, dto, clock);
		assertThat(book.getMissing()).isFalse();
		assertThat(book.getLostDate()).isNull();
	}

}
