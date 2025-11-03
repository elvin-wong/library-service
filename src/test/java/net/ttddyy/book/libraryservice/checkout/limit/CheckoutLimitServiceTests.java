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

package net.ttddyy.book.libraryservice.checkout.limit;

import net.ttddyy.book.libraryservice.checkout.limit.defaults.CheckoutLimitDefault;
import net.ttddyy.book.libraryservice.checkout.limit.defaults.CheckoutLimitDefaultRepository;
import net.ttddyy.book.libraryservice.checkout.limit.schedule.CheckoutLimitSchedule;
import net.ttddyy.book.libraryservice.checkout.limit.schedule.CheckoutLimitScheduleRepository;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link CheckoutLimitService}.
 *
 * @author Tadaya Tsuyukubo
 */
class CheckoutLimitServiceTests {

	@Test
	void getEffectiveCheckoutLimit() {
		Clock clock = Clock.fixed(Instant.parse("2022-12-01T00:00:00.00Z"), ZoneOffset.UTC);
		CheckoutLimitDefaultRepository defaultRepository = mock(CheckoutLimitDefaultRepository.class);
		CheckoutLimitScheduleRepository scheduleRepository = mock(CheckoutLimitScheduleRepository.class);
		CheckoutLimitService checkoutLimitService = new CheckoutLimitService(clock, defaultRepository,
				scheduleRepository, 2, 15);

		CheckoutLimit result;

		// resolved by system default
		given(scheduleRepository.findByScheduleDateAndSchoolIdAndGrade(any(), anyString(), anyInt())).willReturn(null);
		given(defaultRepository.findBySchoolIdAndGrade(any(), anyInt())).willReturn(null);
		result = checkoutLimitService.getEffectiveCheckoutLimit("my-school", 0);
		assertThat(result).isNotNull();
		assertThat(result.maxBooks()).isEqualTo(2);
		assertThat(result.maxDays()).isEqualTo(15);
		verify(scheduleRepository).findByScheduleDateAndSchoolIdAndGrade(any(), anyString(), anyInt());
		verify(defaultRepository).findBySchoolIdAndGrade(any(), anyInt());

		reset(defaultRepository, scheduleRepository);

		// resolved by default
		CheckoutLimitDefault defaultEntity = new CheckoutLimitDefault();
		defaultEntity.setMaxBooks(10);
		defaultEntity.setMaxDays(20);
		given(scheduleRepository.findByScheduleDateAndSchoolIdAndGrade(any(), anyString(), anyInt())).willReturn(null);
		given(defaultRepository.findBySchoolIdAndGrade(any(), anyInt())).willReturn(defaultEntity);

		result = checkoutLimitService.getEffectiveCheckoutLimit("my-school", 0);

		assertThat(result).isNotNull();
		assertThat(result.maxBooks()).isEqualTo(10);
		assertThat(result.maxDays()).isEqualTo(20);
		verify(scheduleRepository).findByScheduleDateAndSchoolIdAndGrade(any(), anyString(), anyInt());
		verify(defaultRepository).findBySchoolIdAndGrade(any(), anyInt());

		reset(defaultRepository, scheduleRepository);

		// resolved by scheduled
		CheckoutLimitSchedule scheduledEntity = new CheckoutLimitSchedule();
		scheduledEntity.setMaxBooks(100);
		scheduledEntity.setMaxDays(200);
		given(scheduleRepository.findByScheduleDateAndSchoolIdAndGrade(any(), anyString(), anyInt()))
			.willReturn(scheduledEntity);

		result = checkoutLimitService.getEffectiveCheckoutLimit("my-school", 0);

		assertThat(result).isNotNull();
		assertThat(result.maxBooks()).isEqualTo(100);
		assertThat(result.maxDays()).isEqualTo(200);
		verify(scheduleRepository).findByScheduleDateAndSchoolIdAndGrade(any(), anyString(), anyInt());
		verifyNoInteractions(defaultRepository);
	}

	@Test
	void getEffectiveCheckoutLimitsDefault() {
		Clock clock = Clock.fixed(Instant.parse("2022-12-01T00:00:00.00Z"), ZoneOffset.UTC);
		CheckoutLimitDefaultRepository defaultRepository = mock(CheckoutLimitDefaultRepository.class);
		CheckoutLimitScheduleRepository scheduleRepository = mock(CheckoutLimitScheduleRepository.class);

		given(scheduleRepository.findAllByScheduleDateAndSchoolId(any(), anyString())).willReturn(List.of());

		CheckoutLimitDefault defaultEntity = new CheckoutLimitDefault();
		defaultEntity.setGrade(1);
		defaultEntity.setMaxBooks(20);
		defaultEntity.setMaxDays(200);
		given(defaultRepository.findAllBySchoolId(anyString())).willReturn(List.of(defaultEntity));

		CheckoutLimitService service = new CheckoutLimitService(clock, defaultRepository, scheduleRepository, 1, 1);
		List<CheckoutLimit> result = service.getEffectiveCheckoutLimits("my-school", null);

		verify(scheduleRepository).findAllByScheduleDateAndSchoolId(eq(LocalDate.parse("2022-12-01")), eq("my-school"));
		verify(defaultRepository).findAllBySchoolId(eq("my-school"));
		assertThat(result).hasSize(1).first().satisfies(limit -> {
			assertThat(limit.grade()).isEqualTo(1);
			assertThat(limit.maxBooks()).isEqualTo(20);
			assertThat(limit.maxDays()).isEqualTo(200);
		});
	}

	@Test
	void getEffectiveCheckoutLimitsScheduledOverride() {
		Clock clock = Clock.fixed(Instant.parse("2022-12-01T00:00:00.00Z"), ZoneOffset.UTC);
		CheckoutLimitDefaultRepository defaultRepository = mock(CheckoutLimitDefaultRepository.class);
		CheckoutLimitScheduleRepository scheduleRepository = mock(CheckoutLimitScheduleRepository.class);

		CheckoutLimitSchedule scheduledEntity = new CheckoutLimitSchedule();
		scheduledEntity.setGrade(1);
		scheduledEntity.setMaxBooks(10);
		scheduledEntity.setMaxDays(100);
		given(scheduleRepository.findAllByScheduleDateAndSchoolId(any(), anyString()))
			.willReturn(List.of(scheduledEntity));

		CheckoutLimitDefault defaultEntity = new CheckoutLimitDefault();
		defaultEntity.setGrade(1);
		defaultEntity.setMaxBooks(20);
		defaultEntity.setMaxDays(200);
		given(defaultRepository.findAllBySchoolId(anyString())).willReturn(List.of(defaultEntity));

		CheckoutLimitService service = new CheckoutLimitService(clock, defaultRepository, scheduleRepository, 1, 1);
		List<CheckoutLimit> result = service.getEffectiveCheckoutLimits("my-school", null);

		verify(scheduleRepository).findAllByScheduleDateAndSchoolId(eq(LocalDate.parse("2022-12-01")), eq("my-school"));
		verify(defaultRepository).findAllBySchoolId(eq("my-school"));
		assertThat(result).hasSize(1).first().satisfies(limit -> {
			assertThat(limit.grade()).isEqualTo(1);
			assertThat(limit.maxBooks()).isEqualTo(10);
			assertThat(limit.maxDays()).isEqualTo(100);
		});
	}

	@Test
	void getEffectiveCheckoutLimitsWithDate() {
		Clock clock = Clock.fixed(Instant.parse("2022-12-01T00:00:00.00Z"), ZoneOffset.UTC);
		CheckoutLimitDefaultRepository defaultRepository = mock(CheckoutLimitDefaultRepository.class);
		CheckoutLimitScheduleRepository scheduleRepository = mock(CheckoutLimitScheduleRepository.class);

		given(scheduleRepository.findAllByScheduleDateAndSchoolId(any(), anyString())).willReturn(List.of());

		CheckoutLimitDefault defaultEntity = new CheckoutLimitDefault();
		defaultEntity.setGrade(1);
		defaultEntity.setMaxBooks(20);
		defaultEntity.setMaxDays(200);
		given(defaultRepository.findAllBySchoolId(anyString())).willReturn(List.of(defaultEntity));

		CheckoutLimitService service = new CheckoutLimitService(clock, defaultRepository, scheduleRepository, 1, 1);

		LocalDate date = LocalDate.parse("2022-12-01");
		List<CheckoutLimit> result = service.getEffectiveCheckoutLimits("my-school", date);

		verify(scheduleRepository).findAllByScheduleDateAndSchoolId(eq(date), eq("my-school"));
		verify(defaultRepository).findAllBySchoolId(eq("my-school"));
		assertThat(result).hasSize(1).first().satisfies(limit -> {
			assertThat(limit.grade()).isEqualTo(1);
			assertThat(limit.maxBooks()).isEqualTo(20);
			assertThat(limit.maxDays()).isEqualTo(200);
		});
	}

}
