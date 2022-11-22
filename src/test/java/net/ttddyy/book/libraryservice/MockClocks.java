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

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tadaya Tsuyukubo
 */
public interface MockClocks {

	class System extends Clock {

		private final Clock delegate = Clock.systemUTC();

		@Override
		public ZoneId getZone() {
			return this.delegate.getZone();
		}

		@Override
		public Clock withZone(ZoneId zone) {
			return this.delegate.withZone(zone);
		}

		@Override
		public Instant instant() {
			return this.delegate.instant();
		}

	}

	class MockClock extends Clock {

		private final List<Instant> list = new ArrayList<>();

		private final AtomicInteger counter = new AtomicInteger();

		public MockClock() {
			// used when directly included by @Import
		}

		public MockClock(String instantExpression) {
			this(Instant.parse(instantExpression));
		}

		public MockClock(Instant instant) {
			this.list.add(instant);
		}

		public void willReturn(Instant instant) {
			this.list.add(instant);
		}

		public int getCallCount() {
			return this.counter.get();
		}

		public void reset() {
			this.list.clear();
		}

		@Override
		public ZoneId getZone() {
			// fixed to UTC for now
			return ZoneId.from(ZoneOffset.UTC);
		}

		@Override
		public Clock withZone(ZoneId zone) {
			throw new UnsupportedOperationException("Not Supported");
		}

		@Override
		public Instant instant() {
			this.counter.incrementAndGet();
			if (this.list.isEmpty()) {
				throw new IllegalStateException("No instants remains");
			}
			if (this.list.size() == 1) {
				return this.list.get(0);
			}
			return this.list.remove(0);
		}

	}

}
