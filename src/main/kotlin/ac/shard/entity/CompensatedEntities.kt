/*
 * This file is part of GrimAC - https://github.com/GrimAnticheat/Grim
 * Copyright (C) 2021-2026 GrimAC, DefineOutside and contributors
 *
 * GrimAC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GrimAC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ac.shard.entity

import ac.shard.entity.types.PacketEntityArmorStand
import ac.shard.entity.types.PacketEntityHorse
import ac.shard.entity.types.PacketEntityPlayer
import ac.shard.entity.types.PacketEntitySelf
import ac.shard.entity.types.PacketEntityTrackXRot
import ac.shard.entity.types.PacketEntityUnHittable
import ac.shard.player.ShardPlayer
import com.github.retrooper.packetevents.protocol.entity.type.EntityType
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.util.UUID

class CompensatedEntities(private val player: ShardPlayer) {
  val entityMap: Int2ObjectOpenHashMap<PacketEntity> = Int2ObjectOpenHashMap()
  val self: PacketEntitySelf = PacketEntitySelf(player)

  fun addEntity(entityId: Int, uuid: UUID, type: EntityType) {
    val packetEntity =
      when {
        type == EntityTypes.PLAYER -> PacketEntityPlayer(player, uuid)
        EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_HORSE) ->
          PacketEntityHorse(player, uuid, type)
        EntityTypes.isTypeInstanceOf(type, EntityTypes.BOAT) || type == EntityTypes.CHICKEN ->
          PacketEntityTrackXRot(player, uuid, type)
        EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_ARROW) ||
          type == EntityTypes.FIREWORK_ROCKET ||
          type == EntityTypes.ITEM -> PacketEntityUnHittable(player, uuid, type)
        type == EntityTypes.ARMOR_STAND -> PacketEntityArmorStand(player, uuid, type)
        else -> PacketEntity(player, uuid, type)
      }

    entityMap.put(entityId, packetEntity)
  }

  fun getEntity(entityId: Int): PacketEntity? {
    if (entityId == player.entityId) {
      return self
    }
    return entityMap.get(entityId)
  }

  fun removeEntity(entityId: Int) {
    entityMap.remove(entityId)
  }

  fun clear() {
    self.eject()
    entityMap.clear()
  }
}
