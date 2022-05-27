import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.util.Properties

val i18n4k_version: String by project
val klaxon_version: String by project
val krontab_version: String by project
val hoplite_version: String by project
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

val versionPropertiesFile = "${projectDir}/project.properties"

fun String.runCommand(currentWorkingDir: File = file("./")): String {
    val byteOut = ByteArrayOutputStream()
    project.exec {
        workingDir = currentWorkingDir
        commandLine = this@runCommand.split("\\s".toRegex())
        standardOutput = byteOut
    }
    return String(byteOut.toByteArray()).trim()
}

fun getRevision(): String {
    return "git rev-parse --short=7 HEAD".runCommand()
}

fun getProperties(file: String, key: String): String {
    val fileInputStream = FileInputStream(file)
    val props = Properties()
    props.load(fileInputStream)
    return props.getProperty(key)
}

fun getVersion(): String {
    return getProperties(versionPropertiesFile, "version")
}

fun getStage(): String {
    return getProperties(versionPropertiesFile, "stage")
}

plugins {
    application
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("de.comahe.i18n4k") version "0.4.0"
}

group = "tech.ixor"
version = getVersion() + "-" + getStage() + "+" + getRevision()

i18n4k {
    sourceCodeLocales = listOf("en", "zh_CN")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    val projectProps by registering(WriteProperties::class) {
        outputFile = file("${projectDir}/src/main/resources/version.properties")
        encoding = "UTF-8"
        property("version", getVersion())
        property("stage", getStage())
        property("revision", getRevision())
    }

    processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        exclude("conf/config.yaml")
        from(projectProps)
    }
}

application {
    mainClass.set("tech.ixor.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // Dependencies
    // i18n
    implementation("de.comahe.i18n4k:i18n4k-core-jvm:$i18n4k_version")
    // JSON Parser
    implementation("com.beust:klaxon:$klaxon_version")
    // Scheduled Jobs
    implementation("dev.inmo:krontab:$krontab_version")
    // Config Loader
    implementation("com.sksamuel.hoplite:hoplite-core:$hoplite_version")
    implementation("com.sksamuel.hoplite:hoplite-yaml:$hoplite_version")
    // Ktor
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-html-builder-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson:$ktor_version")
    implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-cio-jvm:$ktor_version")
    // Logback
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Test Dependencies
    // Ktor
    // Kotlin
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
}
