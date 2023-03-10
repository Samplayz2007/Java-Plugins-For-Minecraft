package com.willfp.ecoenchants

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.integrations.IntegrationLoader
import com.willfp.ecoenchants.commands.CommandEcoEnchants
import com.willfp.ecoenchants.commands.CommandEnchantInfo
import com.willfp.ecoenchants.config.RarityYml
import com.willfp.ecoenchants.config.TargetsYml
import com.willfp.ecoenchants.config.TypesYml
import com.willfp.ecoenchants.config.VanillaEnchantsYml
import com.willfp.ecoenchants.display.EnchantDisplay
import com.willfp.ecoenchants.enchants.EcoEnchants
import com.willfp.ecoenchants.enchants.LoreConversion
import com.willfp.ecoenchants.enchants.registerVanillaEnchants
import com.willfp.ecoenchants.integrations.EnchantRegistrations
import com.willfp.ecoenchants.integrations.plugins.CMIIntegration
import com.willfp.ecoenchants.integrations.plugins.EssentialsIntegration
import com.willfp.ecoenchants.mechanics.AnvilSupport
import com.willfp.ecoenchants.mechanics.EnchantingTableSupport
import com.willfp.ecoenchants.mechanics.GrindstoneSupport
import com.willfp.ecoenchants.mechanics.LootSupport
import com.willfp.ecoenchants.mechanics.VillagerSupport
import com.willfp.ecoenchants.target.ActiveEnchantUpdateListeners
import com.willfp.ecoenchants.target.EnchantLookup.heldEnchantLevels
import com.willfp.libreforge.LibReforgePlugin
import org.bukkit.event.Listener

class EcoEnchantsPlugin : LibReforgePlugin() {
    val targetsYml = TargetsYml(this)
    val rarityYml = RarityYml(this)
    val typesYml = TypesYml(this)
    val vanillaEnchantsYml = VanillaEnchantsYml(this)
    var isLoaded = false
        private set

    init {
        instance = this
        copyConfigs("enchants")
        EcoEnchants.update(this)
    }

    override fun handleEnableAdditional() {
        registerHolderProvider { it.heldEnchantLevels }
    }

    override fun handleAfterLoad() {
        isLoaded = true
    }

    override fun handleReloadAdditional() {
        registerVanillaEnchants(this)

        logger.info(EcoEnchants.values().size.toString() + " Enchants Loaded")
    }

    override fun loadListeners(): List<Listener> {
        return listOf(
            ActiveEnchantUpdateListeners(this),
            VillagerSupport(this),
            EnchantingTableSupport(this),
            LootSupport(this),
            AnvilSupport(this),
            LoreConversion(this),
            GrindstoneSupport(this)
        )
    }

    override fun loadAdditionalIntegrations(): List<IntegrationLoader> {
        return listOf(
            IntegrationLoader("Essentials") { EnchantRegistrations.register(EssentialsIntegration()) },
            IntegrationLoader("CMI") { EnchantRegistrations.register(CMIIntegration()) }
        )
    }

    override fun loadPluginCommands(): List<PluginCommand> {
        return listOf(
            CommandEcoEnchants(this),
            CommandEnchantInfo(this)
        )
    }

    override fun createDisplayModule(): DisplayModule? {
        return if (configYml.getBool("display.enabled")) {
            EnchantDisplay(this)
        } else null
    }

    companion object {
        /** Instance of EcoEnchants. */
        @JvmStatic
        lateinit var instance: EcoEnchantsPlugin
            private set
    }
}
