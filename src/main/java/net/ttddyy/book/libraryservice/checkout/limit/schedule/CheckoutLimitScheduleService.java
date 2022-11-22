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

package net.ttddyy.book.libraryservice.checkout.limit.schedule;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Example;
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
public class CheckoutLimitScheduleService {

	private final CheckoutLimitScheduleRepository repository;

	public CheckoutLimitScheduleService(CheckoutLimitScheduleRepository repository) {
		this.repository = repository;
	}

	public Page<CheckoutLimitSchedule> list(@Nullable String schoolId, Pageable pageable) {
		// example by query uses the fact that entity has null values to ignore condition.
		CheckoutLimitSchedule limit = new CheckoutLimitSchedule();
		limit.setSchoolId(schoolId);
		return this.repository.findAll(Example.of(limit), pageable);
	}

	@Transactional
	public List<CheckoutLimitSchedule> create(List<CheckoutLimitSchedule> entities) {
		return this.repository.saveAll(entities);
	}

	@Transactional
	public List<CheckoutLimitSchedule> update(List<CheckoutLimitScheduleDtoUpdate> dtoList) {
		List<CheckoutLimitSchedule> toUpdate = new ArrayList<>(dtoList.size());
		for (CheckoutLimitScheduleDtoUpdate dto : dtoList) {
			CheckoutLimitSchedule entity = this.repository.getReferenceById(dto.id());
			CheckoutLimitScheduleMapper.INSTANCE.updateEntityFromDto(entity, dto);
			toUpdate.add(entity);
		}
		return this.repository.saveAll(toUpdate);
	}

	@Transactional
	public void delete(Long id) {
		this.repository.deleteById(id);
	}

}
