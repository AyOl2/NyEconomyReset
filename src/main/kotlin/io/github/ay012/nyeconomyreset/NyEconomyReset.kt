package io.github.ay012.nyeconomyreset

import io.github.ay012.nyeconomyreset.manager.ConfigManager
import io.github.ay012.nyeconomyreset.manager.DatabaseManager
import io.github.ay012.nyeconomyreset.database.MySQL
import io.github.ay012.nyeconomyreset.database.SQLite
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Plugin
import taboolib.common.platform.command.simpleCommand
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile

object NyEconomyReset : Plugin() {

    @Config("config.yml")
    lateinit var config: ConfigFile

    override fun onEnable() {
        if (config.getBoolean("database.enable")) {
            DatabaseManager.instance = MySQL()
        }else {
            DatabaseManager.instance = SQLite()
        }
    }

    @Awake(LifeCycle.ENABLE)
    private fun reload() {
        simpleCommand("nyeconomyreset", permission = "nyeconomyreset.admin") { sender, _ ->
            ConfigManager.loadNyEconomy()
            sender.sendMessage("配置文件重载成功!")
        }
    }
}