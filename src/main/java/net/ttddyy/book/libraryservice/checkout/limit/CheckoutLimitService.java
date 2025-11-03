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

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.ttddyy.book.libraryservice.checkout.limit.defaults.CheckoutLimitDefault;
import net.ttddyy.book.libraryservice.checkout.limit.defaults.CheckoutLimitDefaultRepository;
import net.ttddyy.book.libraryservice.checkout.limit.schedule.CheckoutLimitSchedule;
import net.ttddyy.book.libraryservice.checkout.limit.schedule.CheckoutLimitScheduleRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Tadaya Tsuyukubo
 */
@Service
@Transactional(readOnly = true)
public class CheckoutLimitService {

	private final Clock clock;

	private final CheckoutLimitDefaultRepository limitDefaultRepository;

	private final CheckoutLimitScheduleRepository limitScheduleRepository;

	private final int maxBooks;

	private final int maxDays;

	public CheckoutLimitService(Clock clock, CheckoutLimitDefaultRepository limitDefaultRepository,
			CheckoutLimitScheduleRepository limitScheduleRepository,
			@Value("${library-service.checkout.max-books}") int maxBooks,
			@Value("${library-service.checkout.max-days}") int maxDays) {
		this.clock = clock;
		this.limitDefaultRepository = limitDefaultRepository;
		this.limitScheduleRepository = limitScheduleRepository;
		this.maxBooks = maxBooks;
		this.maxDays = maxDays;
	}

	public List<CheckoutLimit> getEffectiveCheckoutLimits(String schoolId, @Nullable LocalDate date) {
		if (date == null) {
			date = LocalDate.now(this.clock);
		}
		List<CheckoutLimitSchedule> schedules = this.limitScheduleRepository.findAllByScheduleDateAndSchoolId(date,
				schoolId);
		List<CheckoutLimitDefault> defaults = this.limitDefaultRepository.findAllBySchoolId(schoolId);

		Map<Integer, CheckoutLimitSchedule> scheduleByGrade = schedules.stream()
			.collect(Collectors.toMap(CheckoutLimitSchedule::getGrade, limit -> limit));

		List<CheckoutLimit> result = new ArrayList<>();
		for (CheckoutLimitDefault schoolDefault : defaults) {
			int grade = schoolDefault.getGrade();
			CheckoutLimitSchedule scheduled = scheduleByGrade.get(grade);
			CheckoutLimit limit = determineEffectiveLimit(grade, scheduled, () -> schoolDefault);
			result.add(limit);
		}
		return result;
	}

	public CheckoutLimit getEffectiveCheckoutLimit(String schoolId, int grade) {
		LocalDate today = LocalDate.now(this.clock);
		CheckoutLimitSchedule schedule = this.limitScheduleRepository.findByScheduleDateAndSchoolIdAndGrade(today,
				schoolId, grade);
		return determineEffectiveLimit(grade, schedule,
				() -> this.limitDefaultRepository.findBySchoolIdAndGrade(schoolId, grade));
	}

	private CheckoutLimit determineEffectiveLimit(int grade, @Nullable CheckoutLimitSchedule scheduled,
			// with jspecify: Supplier<@Nullable CheckoutLimitDefault>
			Supplier<CheckoutLimitDefault> defaultLimitSupplier) {
		// pick the scheduled if exists
		if (scheduled != null) {
			return new CheckoutLimit(grade, scheduled.getMaxBooks(), scheduled.getMaxDays());
		}
		CheckoutLimitDefault defaultLimit = defaultLimitSupplier.get();
		if (defaultLimit != null) {
			return new CheckoutLimit(grade, defaultLimit.getMaxBooks(), defaultLimit.getMaxDays());
		}
		// fallback to the system default
		return new CheckoutLimit(grade, this.maxBooks, this.maxDays);
	}

}
