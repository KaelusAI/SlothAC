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
package ac.shard.api

import ac.shard.api.event.ShardEventBus
import ac.shard.api.service.AiApi
import ac.shard.api.service.CheckApi
import ac.shard.api.service.MonitorApi
import ac.shard.api.service.PunishmentApi

/**
 * Public API surface for Shard.
 *
 * Obtain an instance via [ac.shard.api.ShardApiProvider.get].
 */
interface ShardApi {
  /** AI-related data access and status. */
  fun ai(): AiApi

  /** Check metadata and per-player check listing. */
  fun checks(): CheckApi

  /** Violation and punishment accessors. */
  fun punishments(): PunishmentApi

  /** Current monitor snapshot data (probability/buffer/ping/dmg). */
  fun monitor(): MonitorApi

  /** Shard event bus for subscribing to internal events. */
  fun events(): ShardEventBus
}
