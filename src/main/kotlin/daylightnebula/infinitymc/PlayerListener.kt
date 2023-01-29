package daylightnebula.infinitymc

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

object PlayerListener: Listener {
    @EventHandler
    fun onEntityPickupItem(event: EntityPickupItemEvent) {
        // make sure event entity is a player
        if (event.entity !is Player) return

        // add material to item manager
        ItemManager.playerGetMaterial(event.entity as Player, event.item.itemStack.type)
    }
}