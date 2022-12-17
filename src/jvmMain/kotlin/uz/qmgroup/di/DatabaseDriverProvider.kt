package uz.qmgroup.di

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

actual class DatabaseDriverProvider actual constructor() {
    actual val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:main.db")
}