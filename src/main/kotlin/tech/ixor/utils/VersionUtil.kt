package tech.ixor.utils
import java.io.InputStream
import java.util.*


class VersionUtil {
    companion object {
        fun getVersion(): String {
            val inputStream: InputStream? = this::class.java.getResourceAsStream("/version.properties")
            val properties = Properties()
            properties.load(inputStream)
            return "${properties.getProperty("version")}-${properties.getProperty("stage")}" +
                    " (${properties.getProperty("revision").uppercase()})"
        }
    }
}
