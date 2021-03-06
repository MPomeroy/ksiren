package com.brightspace.ksiren

/**
 * Copyright 2017 D2L Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
abstract class KSirenRequestBuilder<T>(val action: Action) {
	protected val fieldValues: MutableMap<String, String> = mutableMapOf()

	init {
		action.fields.forEach {
			field ->

			field.value?.let {
				fieldValues[field.name] = it
			}
		}
	}

	fun addFieldValue(name: String, value: String): KSirenRequestBuilder<T> {
		fieldValues.put(name, value)
		return this
	}

	abstract fun build(): T
}
