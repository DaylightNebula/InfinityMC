package daylightnebula.infinitymc

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.io.File
import java.lang.StringBuilder

object ItemManager {
    private val unlockedMaterials = mutableListOf<Material>()

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
        unlockedMaterials.forEach { builder.append(it) }
        file.writeText(builder.toString())

        println("Saved material list to ${file.absolutePath}")
    }

    fun playerGetMaterial(player: Player, material: Material) {
        // if not a new material, cancel
        if (unlockedMaterials.contains(material)) return

        // add to list
        unlockedMaterials.add(material)

        // broadcast new material
        broadcastNewMaterial(player, material)
    }

    // function to broadcast a message about a new material
    private fun broadcastNewMaterial(player: Player, material: Material) {
        Bukkit.broadcastMessage("§6Player §c${player.name} §6unlocked infinite §c${material.name.lowercase()}(s)§6!")
        Bukkit.getOnlinePlayers().forEach { it.playSound(it.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f) }
    }
}