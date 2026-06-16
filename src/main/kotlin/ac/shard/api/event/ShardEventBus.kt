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
package ac.shard.api.event

/** Functional listener for [ShardEventBus] subscriptions. */
fun interface ShardEventListener<T : ShardEvent> {
  fun handle(event: T)
}

/**
 * Lightweight event bus for Shard events.
 *
 * Events are dispatched on the thread that calls [post].
 */
interface ShardEventBus {
  /** Post an event to all registered listeners. */
  fun post(event: ShardEvent)

  /** Subscribe with default priority (0) and ignoreCancelled=false. */
  fun <T : ShardEvent> subscribe(
    pluginContext: Any,
    eventType: Class<T>,
    listener: ShardEventListener<T>,
  )

  /** Subscribe with explicit priority and ignoreCancelled. */
  fun <T : ShardEvent> subscribe(
    pluginContext: Any,
    eventType: Class<T>,
    listener: ShardEventListener<T>,
    priority: Int,
    ignoreCancelled: Boolean,
  )

  /** Unregister a specific listener for a context. */
  fun unregisterListener(pluginContext: Any, listener: ShardEventListener<*>)

  /** Unregister all listeners for a context. */
  fun unregisterAll(pluginContext: Any)
}
