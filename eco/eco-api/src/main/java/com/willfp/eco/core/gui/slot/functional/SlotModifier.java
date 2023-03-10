package com.willfp.eco.core.gui.slot.functional;

import com.willfp.eco.core.gui.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Interface to run on slot modify.
 *
 * @deprecated Use {@link SlotUpdater} instead.
 */
@FunctionalInterface
@Deprecated
public interface SlotModifier {
    /**
     * Performs this operation on the given arguments.
     *
     * @param player   The player.
     * @param menu     The menu.
     * @param previous The previous ItemStack.
     */
    void modify(@NotNull Player player,
                @NotNull Menu menu,
                @NotNull ItemStack previous);
}
