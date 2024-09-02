package io.github.ay012.nyeconomyreset.database

import io.github.ay012.nyeconomyreset.NyEconomyReset.config
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submitAsync
import taboolib.module.database.ColumnOptionSQL
import taboolib.module.database.ColumnTypeSQL
import taboolib.module.database.Table
import taboolib.module.database.getHost

class MySQL {

	private val host = config.getHost("database")
	private val dataSource by lazy { host.createDataSource() }
	private val table = Table("players", host) {
		add { id() }
		add("user") {
			type(ColumnTypeSQL.VARCHAR, 36) {
				options(ColumnOptionSQL.KEY)
			}
		}
	}

	init {
		table.createTable(dataSource)
	}

	fun addPlayer(user: String) {
		table.insert(dataSource, "user") {
			value(user)
		}
	}

	fun getPlayer(): List<String> {
		return table.select(dataSource) {
		}.map {
			getString("user")
		}
	}

	companion object{
		var players = HashSet<String>()

		@Awake(LifeCycle.ENABLE)
		fun loadPlayer(){
			MySQL().getPlayer().forEach {
				players.add(it)
			}
		}

		@SubscribeEvent
		fun onPlayerJoin(event: PlayerJoinEvent) {
			val player = event.player.name

			submitAsync {
				if (player !in players) {
					MySQL().addPlayer(player)
					players.add(player)
				}
			}
		}
	}
}
