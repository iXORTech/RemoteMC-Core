import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.time.ZoneId
import java.util.Properties
import java.time.format.DateTimeFormatter
import java.time.ZonedDateTime

val versionPropertiesFile = "${projectDir}/project.properties"
val dependenciesPropertiesFile = "${projectDir}/versions.properties"

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

fun getDependenciesInfo(): String {
    val fileInputStream = FileInputStream(dependenciesPropertiesFile)
    val props = Properties()
    props.load(fileInputStream)
    var dependencies = ""
    for (key in props.keys) {
        val dependency: String = key.toString()
            .replace("version.", "")
            .replace("plugin.", "")
            .replace("..", ".")
        val version = props.getProperty(key.toString())
        dependencies = "$dependency:$version;"
    }
    return if (dependencies == "") {
        dependencies
    } else {
        dependencies.substring(0, dependencies.length - 1)
    }
}

plugins {
    application
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    id("de.comahe.i18n4k")
}

group = "tech.ixor"
version = getVersion() + "-" + getStage() + "+" + getRevision()

i18n4k {
    sourceCodeLocales = listOf("en", "zh_CN")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }

    val projectProps by registering(WriteProperties::class) {
        outputFile = file("${projectDir}/src/main/resources/version.properties")
        encoding = "UTF-8"
        property("version", getVersion())
        property("stage", getStage())
        property("revision", getRevision())
        property("buildDate",
            ZonedDateTime
                .now(ZoneId.of("UTC"))
                .format(DateTimeFormatter.ofPattern("E, MMM dd yyyy"))
        )
        property("dependencies", getDependenciesInfo())
    }

    var shadowJarVersion = getVersion()
    shadowJar {
        if (getStage() == "dev" || getStage() == "alpha" || getStage() == "beta" || getStage() == "rc") {
            shadowJarVersion = shadowJarVersion + "-" + getStage()
        }
        shadowJarVersion = shadowJarVersion + "+" + getRevision()
        destinationDirectory.set(file("${projectDir}/build/distributions"))
        archiveVersion.set(shadowJarVersion)
        archiveClassifier.set("")
    }

    processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        dependsOn(generateI18n4kFiles)
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
    implementation("de.comahe.i18n4k:i18n4k-core-jvm:_")
    // JSON Parser
    implementation("com.beust:klaxon:_")
    // Scheduled Jobs
    implementation("dev.inmo:krontab:_")
    // Config Loader
    implementation("com.sksamuel.hoplite:hoplite-core:_")
    implementation("com.sksamuel.hoplite:hoplite-yaml:_")
    // Ktor
    implementation("io.ktor:ktor-server-core-jvm:_")
    implementation("io.ktor:ktor-server-netty-jvm:_")
    implementation("io.ktor:ktor-server-html-builder-jvm:_")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:_")
    implementation(Ktor.server.contentNegotiation)
    implementation(Ktor.plugins.serialization.gson)
    implementation("io.ktor:ktor-client-core-jvm:_")
    implementation("io.ktor:ktor-client-cio-jvm:_")
    // Logback
    implementation("ch.qos.logback:logback-classic:_")

    // Test Dependencies
    // Ktor
    // Kotlin
    testImplementation(Kotlin.test.junit)
    testImplementation("io.ktor:ktor-server-tests-jvm:_")
}
