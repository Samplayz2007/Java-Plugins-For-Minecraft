package com.willfp.ecoitems.display

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.display.DisplayPriority
import com.willfp.eco.core.fast.FastItemStack
import com.willfp.eco.util.StringUtils
import com.willfp.eco.util.formatEco
import com.willfp.ecoitems.items.ItemUtils
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ItemsDisplay(plugin: EcoPlugin) : DisplayModule(plugin, DisplayPriority.LOWEST) {
    override fun display(
        itemStack: ItemStack,
        player: Player?,
        vararg args: Any
    ) {
        val fis = FastItemStack.wrap(itemStack)
        val ecoItem = ItemUtils.getEcoItem(itemStack)

        if (ecoItem != null) {
            val itemFast = FastItemStack.wrap(ecoItem.itemStack)

            val lore = ecoItem.lore.map { "${Display.PREFIX}${StringUtils.format(it, player)}" }.toMutableList()

            if (player != null) {
                val lines = ecoItem.getNotMetLines(player).map { Display.PREFIX + it }

                if (lines.isNotEmpty()) {
                    lore.add(Display.PREFIX)
                    lore.addAll(lines)
                }
            }

            lore.addAll(fis.lore)

            fis.displayName = ecoItem.displayName.formatEco(player, true)
            fis.addItemFlags(*itemFast.itemFlags.toTypedArray())
            fis.lore = lore
        }
    }
}
