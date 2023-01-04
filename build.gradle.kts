import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")

    id("com.squareup.sqldelight")
}

group = "uz.qmgroup"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val commonMain by getting {
            val koinVersion = extra["koin.version"] as String

            dependencies {
                implementation("com.squareup.sqldelight:coroutines-extensions:1.5.4")

                // Koin Core features
                api("io.insert-koin:koin-core:$koinVersion")

                api(compose.foundation)
                api(compose.runtime)
                api(compose.material)
                api(compose.materialIconsExtended)

                implementation(compose.preview)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test")) // This brings all the platform dependencies automatically
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation("com.squareup.sqldelight:sqlite-driver:1.5.4")
                // https://mvnrepository.com/artifact/com.googlecode.libphonenumber/libphonenumber
                implementation("com.googlecode.libphonenumber:libphonenumber:8.13.2")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "LogiAdmin"
            packageVersion = "1.0.0"
        }
    }
}

sqldelight {
    database("Database") { // This will be the name of the generated database class.
        packageName = "uz.qmgroup"
        schemaOutputDirectory = file("uz.qmgroup.cache")
        migrationOutputDirectory = file("uz.qmgroup.cache")
        deriveSchemaFromMigrations = true
        verifyMigrations = true
    }
}