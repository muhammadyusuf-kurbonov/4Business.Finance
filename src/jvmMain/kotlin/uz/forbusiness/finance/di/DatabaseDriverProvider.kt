package uz.forbusiness.finance.di


import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
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
            val version = driver.executeQuery(
                identifier = null,
                sql = "PRAGMA user_version;",
                parameters = 0,
                mapper = {
                    QueryResult.Value(it.getLong(0))
                }
            ).value ?: throw IllegalStateException("Could not get database version")

            if (version == Database.Schema.version) return driver

            Database.Schema.migrate(driver, version, Database.Schema.version)

            driver.execute(identifier = null, "PRAGMA user_version(${Database.Schema.version});", parameters = 0)
        }
        return driver
    }
}