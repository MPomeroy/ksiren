package com.brightspace.ksiren

import com.squareup.moshi.JsonReader

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

class Action(
        val name: String,
        val classes: List<String> = listOf(),
        val method: String = "GET",
        val href: String,
        val title: String?,
        val type: ContentType = ContentType.FORM,
        val fields: List<Field> = listOf()) : JsonSerializable {

    companion object {
        fun fromJson(string: String): Action {
            return fromJson(string.toJsonReader())
        }

        fun fromJson(reader: JsonReader): Action {
            var name: String = ""
            val classes: MutableList<String> = mutableListOf()
            var method: String = "GET"
            var href: String = ""
            var title: String? = null
            var type = ContentType.FORM
            val fields: MutableList<Field> = mutableListOf()

            reader.beginObject()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "class" -> {
                        reader.beginArray()
                        while (reader.hasNext()) {
                            classes.add(reader.nextString())
                        }
                        reader.endArray()
                    }
                    "name" -> {
                        name = reader.nextString()
                    }
                    "method" -> {
                        method = reader.nextString()
                    }
                    "href" -> {
                        href = reader.nextString()
                    }
                    "title" -> {
                        title = reader.nextString()
                    }
                    "type" -> {
                        type = ContentType.parse(reader.nextString())
                    }
                    "fields" -> {
                        reader.beginArray()
                        while (reader.hasNext()) {
                            fields.add(Field.Companion.fromJson(reader))
                        }
                        reader.endArray()
                    }
                }
            }
            reader.endObject()
            val finishedAction: Action = Action(name, classes, method, href, title, type, fields)
            return validate(finishedAction)
        }

        fun validate(obj: Action): Action {
            if (obj.name == "" && obj.href == "") {
                throw KSirenException.ValidationException("Validation of action failed, href and name are empty.")
            } else if (obj.name == "") {
                throw KSirenException.ValidationException("Validation of action with href \"".plus(obj.href).plus("\" failed, action is missing name value."))
            } else if (obj.href == "") {
                throw KSirenException.ValidationException("Validation of action with name \"".plus(obj.name).plus("\" failed, action is missing an href."))
            }
            return obj
        }
    }

    fun hasField(name: String): Boolean {
        this.fields.forEach {
            if (it.name == name) {
                return true
            }
        }
        return false
    }

    override fun toJson(): CharSequence {
        return JsonUtils.toJson(this)
    }
}