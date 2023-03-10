package com.willfp.eco.internal.spigot.data.storage

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.internal.spigot.data.EcoProfile
import com.willfp.eco.internal.spigot.data.ProfileHandler

class ProfileSaver(
    plugin: EcoPlugin,
    handler: ProfileHandler
) {
    init {
        val interval = plugin.configYml.getInt("save-interval").toLong()

        plugin.scheduler.runTimer(20, interval) {
            for ((uuid, set) in EcoProfile.CHANGE_MAP) {
                handler.saveKeysFor(uuid, set)
            }
            EcoProfile.CHANGE_MAP.clear()
        }
    }
}
