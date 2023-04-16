package uz.forbusiness.finance.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlinx.coroutines.runBlocking
import uz.forbusiness.finance.Database
import java.io.File

actual class DatabaseDriverProvider actual constructor() {
    actual val driver: SqlDriver
    get() {
        val driver = JdbcSqliteDriver("jdbc:sqlite:main.db")
        if (!File("main.db").exists()) {
            Database.Schema.create(driver)

            driver.execute(identifier = null, "PRAGMA user_version(${Database.Schema.version});", parameters = 0)
        } else {
            val version = runBlocking {
                driver.executeQuery(
                    identifier = null,
                    sql = "PRAGMA user_version;",
                    parameters = 0,
                    mapper = { it.getLong(0) }
                ).await()
            } ?: throw IllegalStateException("Could not get database version")

            Database.Schema.migrate(driver, version.toInt(), Database.Schema.version)

            driver.execute(identifier = null, "PRAGMA user_version(${Database.Schema.version});", parameters = 0)
        }
        return driver
    }
}