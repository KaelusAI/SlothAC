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
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package ac.shard.damage

import ac.shard.config.ConfigManager
import ac.shard.player.ShardPlayer
import kotlin.math.min

class AiDamageProcessor(private val configManager: ConfigManager) : DamageProcessor {
  override fun reset(shardPlayer: ShardPlayer) {
    shardPlayer.combat.damageMultiplier = 1.0
  }

  override fun applyProbability(shardPlayer: ShardPlayer, probability: Double) {
    if (!configManager.isAiDamageReductionEnabled()) {
      shardPlayer.combat.damageMultiplier = 1.0
      return
    }

    shardPlayer.combat.damageMultiplier =
      computeMultiplier(
        probability,
        configManager.aiDamageReductionProb,
        configManager.aiDamageReductionMultiplier,
      )
  }

  companion object {
    internal fun computeMultiplier(
      probability: Double,
      threshold: Double,
      multiplier: Double,
    ): Double {
      if (probability < threshold) return 1.0
      val ratio = (probability - threshold) / (1.0 - threshold)
      val reduction = min(1.0, ratio * multiplier)
      return 1.0 - reduction
    }
  }
}
