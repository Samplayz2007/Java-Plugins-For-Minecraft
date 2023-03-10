package com.willfp.eco.core.placeholder;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.integrations.placeholder.PlaceholderManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * A placeholder that requires a player.
 */
public final class PlayerPlaceholder implements Placeholder {
    /**
     * The name of the placeholder.
     */
    private final String identifier;

    /**
     * The placeholder pattern.
     */
    private final Pattern pattern;

    /**
     * The function to retrieve the output of the placeholder given a player.
     */
    private final Function<Player, String> function;

    /**
     * The plugin for the placeholder.
     */
    private final EcoPlugin plugin;

    /**
     * Create a new player placeholder.
     *
     * @param plugin     The plugin.
     * @param identifier The identifier.
     * @param function   The function to retrieve the value.
     */
    public PlayerPlaceholder(@NotNull final EcoPlugin plugin,
                             @NotNull final String identifier,
                             @NotNull final Function<Player, String> function) {
        this.plugin = plugin;
        this.identifier = identifier;
        this.pattern = Pattern.compile(identifier);
        this.function = function;
    }

    /**
     * Get the value of the placeholder for a given player.
     *
     * @param player The player.
     * @return The value.
     */
    @NotNull
    public String getValue(@NotNull final Player player) {
        return function.apply(player);
    }

    /**
     * Register the placeholder.
     *
     * @return The placeholder.
     */
    public PlayerPlaceholder register() {
        PlaceholderManager.registerPlaceholder(this);
        return this;
    }

    @Override
    public @NotNull EcoPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return this.identifier;
    }

    @NotNull
    @Override
    public Pattern getPattern() {
        return this.pattern;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerPlaceholder that)) {
            return false;
        }
        return Objects.equals(this.getIdentifier(), that.getIdentifier())
                && Objects.equals(this.getPlugin(), that.getPlugin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getIdentifier(), this.getPlugin());
    }
}
