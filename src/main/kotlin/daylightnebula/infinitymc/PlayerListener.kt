package daylightnebula.infinitymc

import br.com.devsrsouza.kotlinbukkitapi.extensions.text.translateColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.event.player.PlayerJoinEvent

object PlayerListener: Listener {
    @EventHandler
    fun onEntityPickupItem(event: EntityPickupItemEvent) {
        // make sure event entity is a player
        if (event.entity !is Player) return

        // add material to item manager
        ItemManager.playerGetMaterial(event.entity as Player, event.item.itemStack.type)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        ItemManager.setPlayerInventory(event.player)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInventoryClick(event: InventoryClickEvent) {
        // make sure who clicked is a player
        if (event.whoClicked !is Player) return

        // make sure current item is the open inventory button
        if (event.currentItem?.equals(ItemManager.openInventoryButtonItem) == true)
            openInfiniteInventoryEvent(event)
        else if (event.currentItem?.equals(ItemManager.backInventoryButtonItem) == true)
            backPageInventoryEvent(event)
        else if (event.currentItem?.equals(ItemManager.nextInventoryButtonItem) == true)
            nextPageInventoryEvent(event)
        else if (event.currentItem?.equals(ItemManager.deleteInventoryButtonItem) == true)
            deleteInventoryEvent(event)
        else if (event.view.title.contains(ItemManager.inventoryName))
            resetInventoryEvent(event)
    }

    private fun openInfiniteInventoryEvent(event: InventoryClickEvent) {
        // cancel the event
        event.isCancelled = true
        event.whoClicked.setItemOnCursor(null)

        // call open inventory
        ItemManager.openMaterialInventory(event.whoClicked as Player, 0)
    }

    private fun backPageInventoryEvent(event: InventoryClickEvent) {
        // cancel the event
        event.isCancelled = true
        event.whoClicked.setItemOnCursor(null)

        // call open inventory
        ItemManager.openMaterialInventory(event.whoClicked as Player, (ItemManager.currentPages[event.whoClicked.uniqueId] ?: 1) - 1)
    }

    private fun nextPageInventoryEvent(event: InventoryClickEvent) {
        // cancel the event
        event.isCancelled = true
        event.whoClicked.setItemOnCursor(null)

        // call open inventory
        ItemManager.openMaterialInventory(event.whoClicked as Player, (ItemManager.currentPages[event.whoClicked.uniqueId] ?: -1) + 1)
    }

    private fun deleteInventoryEvent(event: InventoryClickEvent) {
        // cancel the event
        event.isCancelled = true
        event.whoClicked.setItemOnCursor(null)
    }

    private fun resetInventoryEvent(event: InventoryClickEvent) {
        if (event.slot < 9 || event.slot >= 54) return
        event.isCancelled = true
        event.whoClicked.setItemOnCursor(event.currentItem?.clone())
    }
}