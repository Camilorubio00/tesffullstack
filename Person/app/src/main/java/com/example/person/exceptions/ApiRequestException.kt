package com.example.person.exceptions

import com.example.person.R
import com.example.person.extensions.Text
import com.example.person.extensions.Text.StringResource
import com.example.person.extensions.Text.StringValue

sealed class ApiRequestException(val messageError: Text) : Exception() {
    class Authentication : ApiRequestException(StringResource(R.string.error_authentication))
    class AccessDenied : ApiRequestException(StringResource(R.string.error_access_denied))
    class UnavailableServer : ApiRequestException(StringResource(R.string.error_unavailable_server))
    class ConnectionNetwork : ApiRequestException(StringResource(R.string.error_connection_network))
    class ApiError(message: String) : ApiRequestException(StringValue(message))
    class NotFound : ApiRequestException(StringResource(R.string.error_not_found))
    class Unknown : ApiRequestException(StringResource(R.string.error_unknown))
}