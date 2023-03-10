package com.willfp.ecoenchants.proxy.v1_19_R2

import com.willfp.ecoenchants.proxy.proxies.OpenInventoryProxy
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer
import org.bukkit.entity.Player

class OpenInventory : OpenInventoryProxy {
    override fun getOpenInventory(player: Player): Any {
        return (player as CraftPlayer).handle.bU
    }
}
