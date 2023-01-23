package me.spartacus04.jext.listener

import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareItemCraftEvent

internal class PrepareCraftingEvent : Listener {

    @EventHandler
    fun prepareCraftingEvent(e: PrepareItemCraftEvent) {
        val isDisc = e.inventory.result != null && e.inventory.result!!.type == Material.MUSIC_DISC_5
        if(!isDisc) return

        val isCustomDisc = e.inventory.matrix.any {
            try {
                DiscContainer(it).namespace
                true
            }
            catch (_: IllegalStateException) {
                false
            }
        }

        // check if every disc has same namespace, if they have the same namespace return the namespace else an empty string
        val namespace = e.inventory.matrix.map {
            try {
                DiscContainer(it).namespace
            }
            catch (_: IllegalStateException) {
                ""
            }
        }.distinct().singleOrNull()

        if (isCustomDisc && namespace != null) {
            e.inventory.result = DiscContainer(DISCS.first { it.DISC_NAMESPACE == namespace }).discItem
        }
        else if(isCustomDisc) {
            e.inventory.result = null
        }
    }
}