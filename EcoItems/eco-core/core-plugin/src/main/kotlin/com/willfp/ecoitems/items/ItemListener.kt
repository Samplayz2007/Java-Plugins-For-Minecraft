package com.willfp.ecoitems.items

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.util.NumberUtils
import com.willfp.libreforge.updateEffects
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import kotlin.math.roundToInt

class ItemListener(
    private val plugin: EcoPlugin
) : Listener {
    @EventHandler
    fun onHoldItem(event: PlayerItemHeldEvent) {
        event.player.updateEffects()
        plugin.scheduler.run { event.player.updateEffects() }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onPlaceItem(event: BlockPlaceEvent) {
        ItemUtils.getEcoItem(event.itemInHand) ?: return
        if (event.itemInHand.type.isBlock) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlaceItem2(event: PlayerInteractEvent) {
        ItemUtils.getEcoItem(event.item) ?: return

        if (event.item?.type?.isBlock != true) {
            return
        }

        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun effectiveDurabilityListener(event: PlayerItemDamageEvent) {
        val ecoItem = ItemUtils.getEcoItem(event.item) ?: return
        val maxDurability = event.item.type.maxDurability.toInt()
        val ratio = maxDurability.toDouble() / ecoItem.effectiveDurability
        if (ratio < 1) {
            if (NumberUtils.randFloat(0.0, 1.0) > ratio) {
                event.isCancelled = true
            }
        } else if (ratio > 1) {
            event.damage *= ratio.roundToInt()
        }
    }
}
