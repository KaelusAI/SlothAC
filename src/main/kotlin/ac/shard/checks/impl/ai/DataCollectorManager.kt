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
package ac.shard.checks.impl.ai

import ac.shard.Shard
import ac.shard.data.DataSession
import ac.shard.scheduler.SchedulerService
import java.io.IOException
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level

class DataCollectorManager(private val plugin: Shard, private val scheduler: SchedulerService) {
  val activeSessions: MutableMap<UUID, DataSession> = ConcurrentHashMap()

  fun startCollecting(uuid: UUID, playerName: String, status: String): Boolean {
    val existingSession = activeSessions[uuid]
    if (existingSession != null) {
      if (existingSession.status == status) {
        return false
      }
      stopCollecting(uuid)
    }
    activeSessions[uuid] = DataSession(uuid, playerName, status)
    return true
  }

  fun stopCollecting(uuid: UUID): Boolean {
    val session = activeSessions.remove(uuid)
    if (session != null) {
      scheduler.runAsync {
        try {
          session.saveAndClose(plugin)
        } catch (e: IOException) {
          plugin.logger.log(Level.SEVERE, "Failed to save data for $uuid", e)
        }
      }
      return true
    }
    return false
  }

  fun cancelCollecting(uuid: UUID): Boolean {
    return activeSessions.remove(uuid) != null
  }

  fun getSession(uuid: UUID): DataSession? {
    return activeSessions[uuid]
  }
}
