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

import ac.shard.api.model.MonitorSnapshot
import ac.shard.api.service.MonitorApi
import ac.shard.checks.impl.ai.AiCheck
import ac.shard.player.PlayerDataManager
import java.util.Optional
import java.util.UUID

class MonitorApiImpl(private val playerDataManager: PlayerDataManager) : MonitorApi {
  override fun getSnapshot(playerId: UUID): Optional<MonitorSnapshot> {
    val shardPlayer = playerDataManager.getPlayer(playerId) ?: return Optional.empty()
    val aiCheck = shardPlayer.checkManager.getCheck(AiCheck::class.java) ?: return Optional.empty()
    val ping = shardPlayer.player.ping
    return Optional.of(
      MonitorSnapshot(
        aiCheck.lastProbability,
        aiCheck.buffer,
        ping,
        shardPlayer.combat.damageMultiplier,
        aiCheck.prob90,
      )
    )
  }
}
