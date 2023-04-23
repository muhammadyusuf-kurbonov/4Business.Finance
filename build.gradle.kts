import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")

    id("app.cash.sqldelight").version("2.0.0-alpha05")
    id("io.github.skeptick.libres").version("1.1.6")
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
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val commonMain by getting {
            val koinVersion = extra["koin.version"] as String

            dependencies {
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.0-alpha05")

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

                implementation("app.cash.sqldelight:sqlite-driver:2.0.0-alpha05")
                // https://mvnrepository.com/artifact/com.googlecode.libphonenumber/libphonenumber
                implementation("com.googlecode.libphonenumber:libphonenumber:8.13.2")
            }
        }
        val jvmTest by getting
    }
    jvmToolchain(11)
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe, TargetFormat.AppImage)
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
            packageName.set("uz.forbusiness.finance")
            schemaOutputDirectory.set(file("uz.forbusiness.finance"))
            migrationOutputDirectory.set(file("uz.forbusiness.finance"))
//            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)
        }
    }
}

libres {
    generatedClassName = "MainRes" // "Res" by default
    generateNamedArguments = true // false by default
    camelCaseNamesForAppleFramework = true // false by default
}