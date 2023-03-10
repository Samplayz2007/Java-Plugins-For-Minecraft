package com.willfp.ecoarmor

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.items.Items
import com.willfp.ecoarmor.commands.CommandEcoarmor
import com.willfp.ecoarmor.display.ArmorDisplay
import com.willfp.ecoarmor.sets.ArmorSets
import com.willfp.ecoarmor.sets.ArmorUtils
import com.willfp.ecoarmor.sets.EffectiveDurabilityListener
import com.willfp.ecoarmor.sets.PreventSkullPlaceListener
import com.willfp.ecoarmor.upgrades.AdvancementShardListener
import com.willfp.ecoarmor.upgrades.CrystalListener
import com.willfp.ecoarmor.upgrades.TierArgParser
import com.willfp.ecoarmor.upgrades.Tiers
import com.willfp.ecoarmor.util.DiscoverRecipeListener
import com.willfp.ecoarmor.util.EffectListener
import com.willfp.libreforge.LibReforgePlugin
import org.bukkit.event.Listener

class EcoArmorPlugin : LibReforgePlugin() {
    init {
        instance = this
        Items.registerArgParser(TierArgParser())
        registerHolderProvider { ArmorUtils.getActiveHolders(it) }
    }

    override fun handleEnableAdditional() {
        this.copyConfigs("sets")
        this.copyConfigs("tiers")
    }

    override fun handleReloadAdditional() {
        logger.info(Tiers.values().size.toString() + " Tiers Loaded")
        logger.info(ArmorSets.values().size.toString() + " Sets Loaded")
    }

    override fun loadPluginCommands(): List<PluginCommand> {
        return listOf(
            CommandEcoarmor(this)
        )
    }

    override fun loadListeners(): List<Listener> {
        return listOf(
            CrystalListener(),
            AdvancementShardListener(this),
            EffectiveDurabilityListener(this),
            DiscoverRecipeListener(this),
            PreventSkullPlaceListener(),
            EffectListener(this)
        )
    }

    override fun createDisplayModule(): DisplayModule {
        return ArmorDisplay(this)
    }

    companion object {
        @JvmStatic
        lateinit var instance: EcoArmorPlugin
    }
}
