package io.github.ay012.nyeconomyreset.config

import com.mc9y.nyeconomy.api.NyEconomyAPI
import io.github.ay012.nyeconomyreset.NyEconomyReset.config
import io.github.ay012.nyeconomyreset.database.MySQL
import io.github.ay012.nyeconomyreset.util.TimeUtil.getCurrentTime
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submitAsync

object LoadConfig {

	private val resets = HashMap<String, String>()

	@Awake(LifeCycle.ENABLE)
	fun loadNyEconomy(){
		submitAsync {
			config.reload()
			resets.clear()

			config.getStringList("reset").forEach {
				val parts = it.split("|")

				if (parts.size == 2) resets[parts[0]] = parts[1]
			}
		}
	}

	@Awake(LifeCycle.ENABLE)
	private fun resetNyEconomy(){
		submitAsync (period = 20) {

			resets.forEach { (currency, time) ->
				if (getCurrentTime() !in time) return@forEach

				MySQL.players.forEach { player ->
					NyEconomyAPI.getInstance().reset(currency, player)
				}
			}
		}
	}
}