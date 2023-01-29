package daylightnebula.infinitymc

import br.com.devsrsouza.kotlinbukkitapi.extensions.item.displayName
import br.com.devsrsouza.kotlinbukkitapi.extensions.item.item
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.translateColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.io.File
import java.lang.StringBuilder
import java.util.*

object ItemManager {
    private var unlockedMaterials = mutableListOf<Material>()
    val inventoryName = "&6Infinite Inventory.  Page: ".translateColor()
    val openInventoryButtonItem = item(Material.ENDER_CHEST).displayName("&6Open Infinite Inventory".translateColor())
    val nextInventoryButtonItem = item(Material.ARROW).displayName("&6Next Page".translateColor())
    val backInventoryButtonItem = item(Material.ARROW).displayName("&6Back Page".translateColor())
    val deleteInventoryButtonItem = item(Material.BARRIER).displayName("&cDelete Item".translateColor())
    val currentPages = hashMapOf<UUID, Int>()

    fun loadMaterialList(file: File) {
        // make sure file exists
        if (!file.exists()) return

        // load each material from each line
        file.forEachLine { line ->
            unlockedMaterials.add(
                Material.valueOf(line)
            )
        }

        println("Loaded material list from ${file.absolutePath}")
    }

    fun saveMaterialList(file: File) {
        // make sure file exists
        if (!file.exists()) file.parentFile.mkdirs()

        // save file with each line being one material
        val builder = StringBuilder()
        unlockedMaterials.forEach { builder.append("$it\n") }
        file.writeText(builder.toString())

        println("Saved material list to ${file.absolutePath}")
    }

    fun playerGetMaterial(player: Player, material: Material) {
        // if not a new material, cancel
        if (unlockedMaterials.contains(material) || material == Material.AIR) return

        // add to list
        unlockedMaterials.add(material)

        // sort list alphabetically
        unlockedMaterials = unlockedMaterials.sortedBy { it.name }.toMutableList()

        // broadcast new material
        broadcastNewMaterial(player, material)
    }

    // function to broadcast a message about a new material
    private fun broadcastNewMaterial(player: Player, material: Material) {
        Bukkit.broadcastMessage("§6Player §c${player.name} §6unlocked infinite §c${material.name.lowercase()}(s)§6!")
        Bukkit.getOnlinePlayers().forEach { it.playSound(it.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f) }
    }

    fun setPlayerInventory(player: Player) {
        // set item in players inventory
        player.inventory.setItem(17, openInventoryButtonItem)
    }

    fun openMaterialInventory(player: Player, page: Int) {
        if (unlockedMaterials.isEmpty()) unlockedMaterials.add(Material.DIRT)

        // get the max page number that we can have
        val maxPage = unlockedMaterials.size / 45

        // make sure page is in range
        if (page < 0 || page > maxPage) return

        // save page
        currentPages[player.uniqueId] = page

        // create inventory
        val inventory = Bukkit.createInventory(player, 9 * 6, "${inventoryName}${page + 1}")

        // add back page, next page, and delete buttons
        if (page > 0)
            inventory.setItem(0, backInventoryButtonItem)
        else
            inventory.setItem(0, deleteInventoryButtonItem)
        inventory.setItem(1, deleteInventoryButtonItem)
        inventory.setItem(2, deleteInventoryButtonItem)
        inventory.setItem(3, deleteInventoryButtonItem)
        inventory.setItem(4, deleteInventoryButtonItem)
        inventory.setItem(5, deleteInventoryButtonItem)
        inventory.setItem(6, deleteInventoryButtonItem)
        inventory.setItem(7, deleteInventoryButtonItem)
        if (page >= maxPage)
            inventory.setItem(8, deleteInventoryButtonItem)
        else
            inventory.setItem(8, nextInventoryButtonItem)

        // add infinite items
        var i = page * 45
        while (i < (page + 1) * 45) {
            // if the index is out of range, stop
            if (i < 0 || i >= unlockedMaterials.size) break

            // create item
            val type = unlockedMaterials[i]
            val item = item(material = type, amount = type.maxStackSize)

            // make item name purple and italics
            val meta = item.itemMeta
            meta?.setDisplayName("&5&o".translateColor() + type.name.lowercase().replace("_", " "))
            item.itemMeta = meta

            // add item to list
            inventory.setItem(i - (page * 45) + 9, item)

            // update index
            i++
        }

        // open the inventory
        player.openInventory(inventory)
    }
}