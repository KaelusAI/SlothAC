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
package ac.shard.api.service

import java.util.UUID
import java.util.concurrent.CompletableFuture
import org.bukkit.entity.Player

/** Access to punishment/violation levels. */
interface PunishmentApi {
  /**
   * Asynchronously fetches current violation level for a group.
   *
   * @param playerId player UUID
   * @param groupName punish group name
   * @return future with current VL (0 if missing)
   */
  fun getViolationLevel(playerId: UUID, groupName: String): CompletableFuture<Int>

  /**
   * Asynchronously resets violation level for a group.
   *
   * @param playerId player UUID
   * @param groupName punish group name
   * @return completion future
   */
  fun resetViolationLevel(playerId: UUID, groupName: String): CompletableFuture<Void>

  /** Convenience overload for Bukkit [Player]. */
  fun getViolationLevel(player: Player?, groupName: String): CompletableFuture<Int> {
    if (player == null) {
      return CompletableFuture.completedFuture(0)
    }
    return getViolationLevel(player.uniqueId, groupName)
  }

  fun resetViolationLevel(player: Player?, groupName: String): CompletableFuture<Void> {
    if (player == null) {
      return CompletableFuture.completedFuture(null)
    }
    return resetViolationLevel(player.uniqueId, groupName)
  }
}
