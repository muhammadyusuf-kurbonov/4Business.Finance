package uz.forbusiness.finance.di

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import uz.forbusiness.finance.Database
import java.io.File
import java.lang.IllegalStateException

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
            ).getLong(0) ?: throw IllegalStateException("Could not get database version")

            Database.Schema.migrate(driver, version.toInt(), Database.Schema.version)

            driver.execute(identifier = null, "PRAGMA user_version(${Database.Schema.version});", parameters = 0)
        }
        return driver
    }
}