package com.willfp.eco.internal.spigot.integrations.shop

import com.willfp.eco.core.integrations.shop.ShopIntegration
import com.willfp.eco.core.integrations.shop.ShopSellEvent
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.price.Price
import com.willfp.eco.core.price.impl.PriceEconomy
import net.brcdev.shopgui.ShopGuiPlusApi
import net.brcdev.shopgui.event.ShopPreTransactionEvent
import net.brcdev.shopgui.provider.item.ItemProvider
import net.brcdev.shopgui.shop.ShopManager
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

class ShopShopGuiPlus : ShopIntegration {
    override fun registerEcoProvider() {
        ShopGuiPlusApi.registerItemProvider(EcoShopGuiPlusProvider())
    }

    override fun getSellEventAdapter(): Listener {
        return ShopGuiPlusSellEventListeners
    }

    override fun getUnitValue(itemStack: ItemStack, player: Player): Price {
        return PriceEconomy(
            ShopGuiPlusApi.getItemStackPriceSell(player, itemStack.clone().apply {
                amount = 1
            })
        )
    }

    override fun isSellable(itemStack: ItemStack, player: Player): Boolean {
        return ShopGuiPlusApi.getItemStackPriceSell(player, itemStack) > 0
    }

    class EcoShopGuiPlusProvider : ItemProvider("eco") {
        override fun isValidItem(itemStack: ItemStack?): Boolean {
            itemStack ?: return false

            return Items.isCustomItem(itemStack)
        }

        override fun loadItem(configurationSection: ConfigurationSection): ItemStack? {
            val id = configurationSection.getString("eco")
            return if (id == null) null else Items.lookup(id).item
        }

        override fun compare(itemStack1: ItemStack, itemStack2: ItemStack): Boolean {
            return Items.getCustomItem(itemStack1)?.key == Items.getCustomItem(itemStack2)?.key
        }
    }

    object ShopGuiPlusSellEventListeners : Listener {
        @EventHandler
        fun shopEventToEcoEvent(event: ShopPreTransactionEvent) {
            if (event.isCancelled) {
                return
            }

            if (event.shopAction == ShopManager.ShopAction.BUY) {
                return
            }

            val ecoEvent = ShopSellEvent(event.player, PriceEconomy(event.price), event.shopItem.item)
            Bukkit.getPluginManager().callEvent(ecoEvent)
            event.price = ecoEvent.value.getValue(event.player) * ecoEvent.multiplier
        }
    }

    override fun getPluginName(): String {
        return "ShopGUIPlus"
    }
}