import java.io.FileInputStream
import java.util.Properties

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

val versionPropertiesFile = "${projectDir}/version.properties"

fun getProperties(file: String, key: String): String {
    val fileInputStream = FileInputStream(file)
    val props = Properties()
    props.load(fileInputStream)
    return props.getProperty(key)
}

fun getVersion(): String {
    return getProperties(versionPropertiesFile, "version") + "-" +
            getProperties(versionPropertiesFile, "stage")
}

plugins {
    application
    kotlin("jvm") version "1.6.10"
}

group = "tech.ixor"
version = getVersion()
application {
    mainClass.set("tech.ixor.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}