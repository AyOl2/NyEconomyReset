package io.github.ay012.nyeconomyreset.database

import io.github.ay012.nyeconomyreset.NyEconomyReset.config
import io.github.ay012.nyeconomyreset.manager.DatabaseManager
import taboolib.module.database.*

class MySQL: DatabaseManager() {

	private val host = config.getHost("database")
	private val dataSource by lazy { host.createDataSource() }
	private val table = Table("players", host) {
		add { id() }
		add("user") {
			type(ColumnTypeSQL.VARCHAR, 36) {
				options(ColumnOptionSQL.UNIQUE_KEY)
			}
		}
	}

	init {
		table.createTable(dataSource)
	}

	override fun addPlayer(user: String) {
		table.insert(dataSource, "user") {
			value(user)
		}
	}

	override fun getPlayer(): List<String> {
		return table.select(dataSource) {
		}.map {
			getString("user")
		}
	}
}
