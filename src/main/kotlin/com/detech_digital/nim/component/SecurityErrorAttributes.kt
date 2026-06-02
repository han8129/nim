package com.detech_digital.nim.component

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.webmvc.error.DefaultErrorAttributes
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Component
import org.springframework.web.context.request.WebRequest

@Component
class SecurityErrorAttributes : DefaultErrorAttributes() {

    override fun getErrorAttributes(webRequest: WebRequest, options: ErrorAttributeOptions): Map<String, Any?> {
        val errorAttributes = super.getErrorAttributes(webRequest, options)
        val error = getError(webRequest)

        // Check if the cause is a malformed JSON payload
        if (error is HttpMessageNotReadableException) {
            errorAttributes["message"] = "Malformed JSON request body"
        }

        return errorAttributes
    }
}
