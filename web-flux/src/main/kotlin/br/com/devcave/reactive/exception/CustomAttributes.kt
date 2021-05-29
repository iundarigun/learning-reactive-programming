package br.com.devcave.reactive.exception

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ResponseStatusException

@Component
class CustomAttributes : DefaultErrorAttributes() {

    override fun getErrorAttributes(request: ServerRequest, options: ErrorAttributeOptions): MutableMap<String, Any> {
        val errorAttributes = super.getErrorAttributes(request, options)
        val throwable = getError(request)
        if (throwable is ResponseStatusException) {
            errorAttributes["message"] = throwable.message
            errorAttributes["developerMessage"] = "A responseStatusException Happened"
        }
        return errorAttributes
    }
}