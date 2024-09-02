package io.github.ay012.nyeconomyreset.database

import io.github.ay012.nyeconomyreset.manager.DatabaseManager
import taboolib.common.io.newFile
import taboolib.common.platform.function.getDataFolder
import taboolib.module.database.*

class SQLite: DatabaseManager() {

	private val host = HostSQLite(newFile(getDataFolder(), "data.db"))
	private val dataSource by lazy { host.createDataSource() }
	private val table = Table("players", host) {
		add { id() }
		add("user") {
			type(ColumnTypeSQLite.TEXT, 36) {
				options(ColumnOptionSQLite.UNIQUE)
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