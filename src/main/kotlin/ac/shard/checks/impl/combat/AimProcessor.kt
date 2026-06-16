/*
 * This file is part of Shard - https://github.com/KaelusAI/Shard
 * Copyright (C) 2026 KaelusAI
 *
 * This file contains code derived from GrimAC.
 * The original authors of GrimAC are credited below.
 *
 * Copyright (c) 2021-2026 GrimAC, DefineOutside and contributors.
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
package ac.shard.checks.impl.combat

import ac.shard.checks.AbstractCheck
import ac.shard.checks.CheckData
import ac.shard.checks.CheckFactory
import ac.shard.checks.type.RotationCheck
import ac.shard.player.ShardPlayer
import ac.shard.utils.lists.RunningMode
import ac.shard.utils.math.ShardMath
import ac.shard.utils.update.RotationUpdate

@CheckData(name = "AimProcessor_Internal")
class AimProcessor(shardPlayer: ShardPlayer) : AbstractCheck(shardPlayer), RotationCheck {
  var sensitivityX: Double = 0.0
  var sensitivityY: Double = 0.0
  var divisorX: Double = 0.0
  var divisorY: Double = 0.0
  var modeX: Double = 0.0
  var modeY: Double = 0.0
  var deltaDotsX: Double = 0.0
  var deltaDotsY: Double = 0.0
  private val xRotMode = RunningMode(TOTAL_SAMPLES_THRESHOLD)
  private val yRotMode = RunningMode(TOTAL_SAMPLES_THRESHOLD)
  private var lastXRot = 0.0
  private var lastYRot = 0.0

  private var lastDeltaYaw = 0.0f
  private var lastDeltaPitch = 0.0f

  var lastYawAccel = 0.0f
    private set

  var lastPitchAccel = 0.0f
    private set

  var currentYawAccel = 0.0f
    private set

  var currentPitchAccel = 0.0f
    private set

  interface Factory : CheckFactory {
    override fun create(player: ShardPlayer): AimProcessor
  }

  override fun process(rotationUpdate: RotationUpdate) {
    val deltaYaw = rotationUpdate.deltaYaw
    val deltaPitch = rotationUpdate.deltaPitch
    val deltaYawAbs = kotlin.math.abs(deltaYaw).toDouble()
    val deltaPitchAbs = kotlin.math.abs(deltaPitch).toDouble()
    lastYawAccel = currentYawAccel
    lastPitchAccel = currentPitchAccel
    currentYawAccel = (deltaYawAbs - kotlin.math.abs(lastDeltaYaw)).toFloat()
    currentPitchAccel = (deltaPitchAbs - kotlin.math.abs(lastDeltaPitch)).toFloat()
    lastDeltaYaw = deltaYaw
    lastDeltaPitch = deltaPitch

    divisorX = ShardMath.gcd(deltaYawAbs, lastXRot)
    if (deltaYawAbs > 0 && deltaYawAbs < 5 && divisorX > ShardMath.getMinimumDivisor()) {
      xRotMode.add(divisorX)
      lastXRot = deltaYawAbs
    }

    divisorY = ShardMath.gcd(deltaPitchAbs, lastYRot)
    if (deltaPitchAbs > 0 && deltaPitchAbs < 5 && divisorY > ShardMath.getMinimumDivisor()) {
      yRotMode.add(divisorY)
      lastYRot = deltaPitchAbs
    }

    if (xRotMode.size() > SIGNIFICANT_SAMPLES_THRESHOLD) {
      xRotMode.updateMode()
      if (xRotMode.modeCount > SIGNIFICANT_SAMPLES_THRESHOLD) {
        modeX = xRotMode.modeValue
        sensitivityX = convertToSensitivity(modeX)
      }
    }

    if (yRotMode.size() > SIGNIFICANT_SAMPLES_THRESHOLD) {
      yRotMode.updateMode()
      if (yRotMode.modeCount > SIGNIFICANT_SAMPLES_THRESHOLD) {
        modeY = yRotMode.modeValue
        sensitivityY = convertToSensitivity(modeY)
      }
    }

    if (modeX > 0) {
      deltaDotsX = deltaYawAbs / modeX
    }
    if (modeY > 0) {
      deltaDotsY = deltaPitchAbs / modeY
    }
  }

  companion object {
    private const val SIGNIFICANT_SAMPLES_THRESHOLD = 15
    private const val TOTAL_SAMPLES_THRESHOLD = 80

    fun convertToSensitivity(value: Double): Double {
      val normalized = value / 0.15F / 8.0
      val cubic = Math.cbrt(normalized)
      return (cubic - 0.2f) / 0.6f
    }
  }
}
