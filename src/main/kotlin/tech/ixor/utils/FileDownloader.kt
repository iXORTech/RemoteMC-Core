package tech.ixor.utils

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import tech.ixor.I18N
import java.io.File

class FileDownloader {
    fun downloadFile(urlString: String, pathname: String, downloadDescription: String) {
        val client = HttpClient()
        val file = File(pathname)

        runBlocking {
            val httpResponse: HttpResponse = client.get(urlString) {
                onDownload { bytesSentTotal, contentLength ->
                    println(I18N.configFileDownloading(downloadDescription))
                    println(I18N.configFileDownloadingTo(pathname, bytesSentTotal, contentLength))
                }
            }
            val responseBody: ByteArray = httpResponse.body()
            if (!file.parentFile.exists()) {
                print(I18N.configFileDirNotExist)
                file.parentFile.mkdirs()
                println(I18N.configFileDirDone)
            }
            file.writeBytes(responseBody)
            println(I18N.configFileSaved(downloadDescription, pathname))
            println()
        }
    }
}
