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

package net.ttddyy.book.libraryservice;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.cloud.openfeign.support.SortJacksonModule;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.util.MimeTypeUtils;

/**
 * Configuration to support deserializing {@link Page} and {@link Sort}.
 *
 * @author Tadaya Tsuyukubo
 */
@TestConfiguration(proxyBeanMethods = false)
public class PageDeserializeConfiguration {

	@Bean
	CodecCustomizer codecCustomizer(ObjectMapper objectMapper) {
		// Applied by WebTestClientContextCustomizer
		return configurer -> {
			// Set Spring Boot created ObjectMapper, which already applied Page/Sort
			// jackson
			// modules, as a default codec for WebTestClient
			Jackson2JsonDecoder decoder = new Jackson2JsonDecoder(objectMapper, MimeTypeUtils.APPLICATION_JSON);
			configurer.defaultCodecs().jackson2JsonDecoder(decoder);
		};
	}

	@Bean
	Module pageJacksonModule() {
		// From spring-cloud-openfeign to deserialize Page json
		return new PageJacksonModule();
	}

	@Bean
	Module sortJacksonModule() {
		// From spring-cloud-openfeign to deserialize Sort json
		return new SortJacksonModule();
	}

}
