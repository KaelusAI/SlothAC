/*
 * This file is part of Shard - https://github.com/KaelusAI/Shard
 * Copyright (C) 2026 KaelusAI
 *
 * Shard is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Shard is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ac.shard.ai

import ac.shard.server.AIResponse
import com.fasterxml.jackson.databind.ObjectMapper

class JacksonAiResponseParser : AiResponseParser {
  override fun parse(response: String): AIResponse {
    val node = OBJECT_MAPPER.readTree(response).get("probability")
    val probability =
      when {
        node == null || node.isNull -> null
        node.isNumber -> node.doubleValue()
        node.isTextual -> node.textValue().toDoubleOrNull()
        else -> null
      } ?: throw IllegalArgumentException("AI response does not contain a valid probability")
    return AIResponse(probability)
  }

  companion object {
    private val OBJECT_MAPPER = ObjectMapper()
  }
}
