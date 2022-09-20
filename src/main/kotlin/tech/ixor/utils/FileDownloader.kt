package tech.ixor.utils

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import tech.ixor.I18N
import java.io.File

class FileDownloader {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun downloadFile(urlString: String, pathname: String, downloadDescription: String) {
        val client = HttpClient()
        val file = File(pathname)

        runBlocking {
            val httpResponse: HttpResponse = client.get(urlString) {
                onDownload { bytesSentTotal, contentLength ->
                    logger.info(I18N.configFileDownloading(downloadDescription))
                    logger.info(I18N.configFileDownloadingTo(pathname, bytesSentTotal, contentLength))
                }
            }
            val responseBody: ByteArray = httpResponse.body()
            if (!file.parentFile.exists()) {
                logger.info(I18N.configFileDirNotExist())
                file.parentFile.mkdirs()
                logger.info(I18N.configFileDirDone())
            }
            file.writeBytes(responseBody)
            logger.info(I18N.configFileSaved(downloadDescription, pathname))
        }
    }
}
