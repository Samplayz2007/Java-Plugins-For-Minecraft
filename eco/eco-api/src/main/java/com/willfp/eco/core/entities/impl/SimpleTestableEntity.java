package com.willfp.eco.core.entities.impl;

import com.willfp.eco.core.entities.TestableEntity;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Default vanilla entities.
 */
public class SimpleTestableEntity implements TestableEntity {
    /**
     * The entity type.
     */
    private final EntityType type;

    /**
     * Create a new simple testable entity.
     *
     * @param type The entity type.
     */
    public SimpleTestableEntity(@NotNull final EntityType type) {
        this.type = type;

        Validate.notNull(type.getEntityClass(), "Entity cannot be of unknown type!");
    }

    @Override
    public boolean matches(@Nullable final Entity entity) {
        return entity != null && entity.getType() == type;
    }

    @Override
    public Entity spawn(@NotNull final Location location) {
        Validate.notNull(location.getWorld());

        assert type.getEntityClass() != null;

        return location.getWorld().spawn(location, type.getEntityClass());
    }

    /**
     * Get the type.
     *
     * @return The type.
     */
    public EntityType getType() {
        return this.type;
    }
}
