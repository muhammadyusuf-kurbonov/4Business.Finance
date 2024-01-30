import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.9.21"
    id("org.jetbrains.compose")

    id("app.cash.sqldelight").version("2.0.1")
    id("io.github.skeptick.libres").version("1.2.2")
}

group = "uz.forbusiness.finance"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
    }
    sourceSets {
        val commonMain by getting {
            val koinVersion = extra["koin.version"] as String

            dependencies {
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.1")
                implementation("app.cash.sqldelight:primitive-adapters:2.0.0")

                // Koin Core features
                api("io.insert-koin:koin-core:$koinVersion")

                api("io.github.skeptick.libres:libres-compose:1.2.2")

                api(compose.foundation)
                api(compose.runtime)
                api(compose.material)
                api(compose.materialIconsExtended)

                implementation(compose.preview)

                implementation("com.arkivanov.decompose:decompose:2.2.2")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:2.2.2")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:2.2.2-compose-experimental")
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

                implementation("app.cash.sqldelight:sqlite-driver:2.0.1")
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
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "ForBusiness.Finance"
            packageVersion = "1.0.0"

            modules("java.sql")
        }

        buildTypes.release {
            proguard {
                configurationFiles.from("compose-desktop.pro")
            }
        }
    }
}

sqldelight {
    databases {
        create("Database") { // This will be the name of the generated database class.
            packageName = "uz.forbusiness.finance"
            schemaOutputDirectory = file("uz.forbusiness.finance")
            migrationOutputDirectory = file("uz.forbusiness.finance")
            deriveSchemaFromMigrations = true
            verifyMigrations = true
        }
    }
}

libres {
    generatedClassName = "MainRes" // "Res" by default
    generateNamedArguments = true // false by default
    camelCaseNamesForAppleFramework = true // false by default
}