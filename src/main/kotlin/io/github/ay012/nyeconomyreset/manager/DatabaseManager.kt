package io.github.ay012.nyeconomyreset.manager

import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submitAsync

abstract class DatabaseManager {

	abstract fun addPlayer(user: String)

	abstract fun getPlayer(): List<String>


	companion object {
		var players = HashSet<String>()
		// 懒加载变量 在启动方法指定加载模式
		lateinit var instance: DatabaseManager

		fun loadPlayer(){
			instance.getPlayer().forEach {
				players.add(it)
			}
		}

		@SubscribeEvent
		private fun onPlayerJoin(event: PlayerJoinEvent) {
			val player = event.player.name

			submitAsync {
				if (player !in players) {
					instance.addPlayer(player)
					players.add(player)
				}
			}
		}
	}
}