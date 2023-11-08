package tech.ixor.utils

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import okhttp3.Protocol
import org.slf4j.LoggerFactory

/*
 * HttpUtil.kt
 * RemoteMC-Core
 *
 * Created by Qian Qian "Cubik" on Wednesday Nov. 08.
 */

object HttpUtil {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val httpClient = HttpClient(OkHttp) {
        engine {
            config {
                followRedirects(true)
                protocols(listOf(Protocol.HTTP_1_1, Protocol.HTTP_2))
            }
        }
    }

    object GetRequests {
        fun request(url: String, requestBuilder: HttpRequestBuilder.() -> Unit = {}): HttpResponse {
            logger.debug("GET $url")
            val httpResponse: HttpResponse
            runBlocking {
                httpResponse = httpClient.get(url, requestBuilder)
            }
            logger.debug("Response: {}", httpResponse)
            return httpResponse
        }

        fun body(url: String, requestBuilder: HttpRequestBuilder.() -> Unit = {}): String {
            val httpResponseBody: String
            runBlocking {
                httpResponseBody = request(url, requestBuilder).bodyAsText()
            }
            return httpResponseBody
        }

        private fun bodyJson(url: String, requestBuilder: HttpRequestBuilder.() -> Unit = {}): Any {
            val parser: Parser = Parser.default()
            return parser.parse(StringBuilder(body(url, requestBuilder)))
        }

        fun bodyJsonObject(url: String, requestBuilder: HttpRequestBuilder.() -> Unit = {}): JsonObject {
            return bodyJson(url, requestBuilder) as JsonObject
        }

        @Suppress("UNCHECKED_CAST")
        fun bodyJsonArray(url: String, requestBuilder: HttpRequestBuilder.() -> Unit = {}): JsonArray<JsonObject> {
            return bodyJson(url, requestBuilder) as JsonArray<JsonObject>
        }
    }

    object PostRequests {
        fun request(url: String, requestBuilder: HttpRequestBuilder.() -> Unit = {}): HttpResponse {
            logger.debug("POST $url")
            val httpResponse: HttpResponse
            runBlocking {
                httpResponse = httpClient.post(url, requestBuilder)
            }
            logger.debug("Response: {}", httpResponse)
            return httpResponse
        }

        fun body(url: String, requestBuilder: HttpRequestBuilder.() -> Unit = {}): String {
            val httpResponseBody: String
            runBlocking {
                httpResponseBody = request(url, requestBuilder).bodyAsText()
            }
            return httpResponseBody
        }

        private fun bodyJson(url: String, requestBuilder: HttpRequestBuilder.() -> Unit = {}): Any {
            val parser: Parser = Parser.default()
            return parser.parse(StringBuilder(GetRequests.body(url, requestBuilder)))
        }

        fun bodyJsonObject(url: String, requestBuilder: HttpRequestBuilder.() -> Unit = {}): JsonObject {
            return bodyJson(url, requestBuilder) as JsonObject
        }

        @Suppress("UNCHECKED_CAST")
        fun bodyJsonArray(url: String, requestBuilder: HttpRequestBuilder.() -> Unit = {}): JsonArray<JsonObject> {
            return bodyJson(url, requestBuilder) as JsonArray<JsonObject>
        }
    }
}
