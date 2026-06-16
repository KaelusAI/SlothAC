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
package ac.shard.api.internal

import ac.shard.api.ShardApi
import ac.shard.api.event.ShardEventBus
import ac.shard.api.service.AiApi
import ac.shard.api.service.CheckApi
import ac.shard.api.service.MonitorApi
import ac.shard.api.service.PunishmentApi

class ShardApiImpl(
  private val aiApi: AiApi,
  private val checkApi: CheckApi,
  private val punishmentApi: PunishmentApi,
  private val monitorApi: MonitorApi,
  private val eventBus: ShardEventBus,
) : ShardApi {
  override fun ai(): AiApi = aiApi

  override fun checks(): CheckApi = checkApi

  override fun punishments(): PunishmentApi = punishmentApi

  override fun monitor(): MonitorApi = monitorApi

  override fun events(): ShardEventBus = eventBus
}
