package tech.ixor.utils

import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.*

class VersionUtil {
    companion object {
        fun getVersion(): String {
            val pwd = System.getProperty("user.dir")
            var fileInputStream: FileInputStream
            try {
                fileInputStream = FileInputStream("$pwd/version.properties")
            } catch (e: FileNotFoundException) {
                println("Fallback to version properties in development environment")
                fileInputStream = FileInputStream("$pwd/src/main/resources/version.properties")
            }
            val properties = Properties()
            properties.load(fileInputStream)
            return "Version: ${properties.getProperty("version")}-${properties.getProperty("stage")}" +
                    " (${properties.getProperty("revision").uppercase()})"
        }
    }
}