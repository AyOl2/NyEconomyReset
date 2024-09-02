package io.github.ay012.nyeconomyreset.manager

import com.mc9y.nyeconomy.api.NyEconomyAPI
import io.github.ay012.nyeconomyreset.NyEconomyReset.config
import io.github.ay012.nyeconomyreset.util.TimeUtil.getCurrentTime
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submitAsync

object ConfigManager {

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
			info("载入了 ${resets.size} 个需重置的货币")
		}
	}

	@Awake(LifeCycle.ENABLE)
	private fun resetNyEconomy(){
		submitAsync (period = 20) {

			resets.forEach { (currency, time) ->
				if (getCurrentTime() !in time) return@forEach

				DatabaseManager.players.forEach { player ->
					NyEconomyAPI.getInstance().reset(currency, player)
				}
			}
		}
	}
}