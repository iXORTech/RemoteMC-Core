package tech.ixor.utils

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import java.io.File

class FileDownloader {
    fun downloadFile(urlString: String, pathname: String) {
        val client = HttpClient()
        val file = File(pathname)

        runBlocking {
            val httpResponse: HttpResponse = client.get(urlString) {
                onDownload { bytesSentTotal, contentLength ->
                    println("Received $bytesSentTotal bytes from $contentLength")
                }
            }
            val responseBody: ByteArray = httpResponse.body()
            file.parentFile.mkdirs()
            file.writeBytes(responseBody)
        }
    }
}
