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

package net.ttddyy.book.libraryservice.checkout.limit.defaults;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Tadaya Tsuyukubo
 */
@Service
@Transactional(readOnly = true)
public class CheckoutLimitDefaultService {

	private final CheckoutLimitDefaultRepository repository;

	public CheckoutLimitDefaultService(CheckoutLimitDefaultRepository repository) {
		this.repository = repository;
	}

	public Page<CheckoutLimitDefault> list(Pageable pageable) {
		return this.repository.findAll(pageable);
	}

	@Transactional
	public List<CheckoutLimitDefault> update(List<CheckoutLimitDefaultDtoUpdateBulk> dtoList) {
		List<CheckoutLimitDefault> entities = new ArrayList<>(dtoList.size());
		for (CheckoutLimitDefaultDtoUpdateBulk dto : dtoList) {
			CheckoutLimitDefault entity = this.repository.getReferenceById(dto.id());
			CheckoutLimitDefaultMapper.INSTANCE.updateEntityFromDto(entity, dto);
			entities.add(entity);
		}
		return this.repository.saveAll(entities);
	}

	@Transactional
	public void delete(Long id) {
		this.repository.deleteById(id);
	}

}
