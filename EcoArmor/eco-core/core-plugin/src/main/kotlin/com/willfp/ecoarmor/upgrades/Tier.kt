package com.willfp.ecoarmor.upgrades

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.items.CustomItem
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.recipe.recipes.ShapedCraftingRecipe
import com.willfp.eco.util.StringUtils
import com.willfp.ecoarmor.sets.ArmorSlot
import com.willfp.ecoarmor.sets.ArmorUtils.getCrystalTier
import com.willfp.ecoarmor.util.notNullMapOf
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

class Tier(
    val id: String,
    private val config: Config,
    plugin: EcoPlugin
) {
    /**
     * The display name of the crystal.
     */
    val displayName: String

    /**
     * The names of the tiers required for application.
     */
    private val requiredTiersForApplication: List<String>

    /**
     * If the crafting recipe is enabled.
     */
    val craftable: Boolean

    /**
     * The ItemStack of the crystal.
     */
    val crystal: ItemStack

    /**
     * The crafting recipe to make the crystal.
     */
    val crystalRecipe: ShapedCraftingRecipe?

    /**
     * Item properties.
     */
    val properties = notNullMapOf<ArmorSlot, TierProperties>()

    /**
     * Create a new Tier.
     */
    init {
        Tiers.addNewTier(this)

        craftable = this.config.getBool("crystal.craftable")
        displayName = this.config.getFormattedString("display")
        requiredTiersForApplication = this.config.getStrings("requiresTiers")
        val key = plugin.namespacedKeyFactory.create("upgrade_crystal")
        val out = Items.lookup(this.config.getString("crystal.item")).item
        val outMeta = out.itemMeta!!
        val container = outMeta.persistentDataContainer
        container.set(key, PersistentDataType.STRING, id)
        @Suppress("UsePropertyAccessSyntax")
        outMeta.setDisplayName(this.config.getFormattedString("crystal.name"))
        val lore: MutableList<String> = ArrayList()
        for (loreLine in this.config.getStrings("crystal.lore")) {
            lore.add(Display.PREFIX + StringUtils.format(loreLine!!))
        }
        outMeta.lore = lore
        out.itemMeta = outMeta
        out.amount = 1 // who knows
        crystal = out
        for (slot in ArmorSlot.values()) {
            properties[slot] = TierProperties(
                this.config.getInt("properties." + slot.name.lowercase(Locale.getDefault()) + ".armor"),
                this.config.getInt("properties." + slot.name.lowercase(Locale.getDefault()) + ".toughness"),
                this.config
                    .getInt("properties." + slot.name.lowercase(Locale.getDefault()) + ".knockbackResistance"),
                this.config.getInt("properties." + slot.name.lowercase(Locale.getDefault()) + ".speedPercentage"),
                this.config
                    .getInt("properties." + slot.name.lowercase(Locale.getDefault()) + ".attackSpeedPercentage"),
                this.config
                    .getInt("properties." + slot.name.lowercase(Locale.getDefault()) + ".attackDamagePercentage"),
                this.config
                    .getInt("properties." + slot.name.lowercase(Locale.getDefault()) + ".attackKnockbackPercentage")
            )
        }

        CustomItem(
            plugin.namespacedKeyFactory.create("crystal_" + id.lowercase(Locale.getDefault())),
            { test: ItemStack? -> this == getCrystalTier(test!!) },
            out
        ).register()

        if (this.craftable) {
            val recipeOut = out.clone()
            recipeOut.amount = this.config.getInt("crystal.giveAmount")
            val builder = ShapedCraftingRecipe.builder(plugin, "upgrade_crystal_$id")
                .setOutput(recipeOut)
            val recipeStrings: List<String> = this.config.getStrings("crystal.recipe")
            CustomItem(plugin.namespacedKeyFactory.create("upgrade_crystal_$id"), { test: ItemStack? ->
                if (test == null) {
                    return@CustomItem false
                }
                if (getCrystalTier(test) == null) {
                    return@CustomItem false
                }
                this == getCrystalTier(test)
            }, out).register()
            for (i in 0..8) {
                builder.setRecipePart(i, Items.lookup(recipeStrings[i]))
            }
            crystalRecipe = builder.build()
            crystalRecipe.register()
        } else {
            crystalRecipe = null
        }
    }

    /**
     * Get the required tiers for application.
     *
     * @return The tiers, or a blank list if always available.
     */
    fun getRequiredTiersForApplication(): List<Tier> {
        return requiredTiersForApplication.mapNotNull { Tiers.getByID(it) }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Tier) {
            return false
        }
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hash(this.id)
    }
}

data class TierProperties(
    val armor: Int,
    val toughness: Int,
    val knockback: Int,
    val speed: Int,
    val attackSpeed: Int,
    val attackDamage: Int,
    val attackKnockback: Int
)