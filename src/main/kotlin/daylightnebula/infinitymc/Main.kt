package daylightnebula.infinitymc

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Main: JavaPlugin() {
    override fun onEnable() {
        ItemManager.loadMaterialList(File(dataFolder, "items.txt"))
        Bukkit.getPluginManager().registerEvents(PlayerListener, this)
        println("Infinity MC Started")
    }

    override fun onDisable() {
        ItemManager.saveMaterialList(File(dataFolder, "items.txt"))
        println("Infinity MC Stopped")
    }
}