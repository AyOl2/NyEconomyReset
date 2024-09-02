package io.github.ay012.nyeconomyreset

import io.github.ay012.nyeconomyreset.config.LoadConfig
import io.github.ay012.nyeconomyreset.database.MySQL
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Plugin
import taboolib.common.platform.command.simpleCommand
import taboolib.common.platform.function.info
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile

object NyEconomyReset : Plugin() {

    @Config("config.yml")
    lateinit var config: ConfigFile

    override fun onEnable() { MySQL() }

    @Awake(LifeCycle.ENABLE)
    private fun reload() {
        simpleCommand("nyeconomyreset", permission = "nyeconomyreset.admin") { _, _ ->
            LoadConfig.loadNyEconomy()
            info(MySQL.players.joinToString())
        }
    }
}