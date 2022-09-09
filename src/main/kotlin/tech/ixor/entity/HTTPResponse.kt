package tech.ixor.entity

import com.beust.klaxon.Json

class HTTPResponse(
    @Json(name = "status_code")
    val statusCode: Int,
    val message: String
) {
    override fun toString(): String {
        return "HTTPResponse(statusCode=$statusCode, message='$message')"
    }
}
