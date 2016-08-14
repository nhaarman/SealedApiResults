package com.nhaarman.sealedapiresults

import java.io.IOException

// https://tools.ietf.org/html/rfc7231
// http://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml

sealed class SealedApiResult<out T> {

    sealed class Some<out T>(val code: Int, val headers: Map<String, List<String>> = emptyMap()) : SealedApiResult<T>() {
        override fun toString() = "Some(code=$code, headers=$headers)"

        /* 1XX Informational results */
        sealed class Informational1XX<out T>(code: Int, headers: Map<String, List<String>> = emptyMap()) : Some<T>(code, headers) {

            class Continue100<out T>(headers: Map<String, List<String>> = emptyMap()) : Informational1XX<T>(100, headers) {
                override fun toString() = "Continue100(headers=$headers)"
            }

            class SwitchingProtocols101<out T>(headers: Map<String, List<String>> = emptyMap()) : Informational1XX<T>(101, headers) {
                override fun toString() = "SwitchingProtocols101(headers=$headers)"
            }

            class Processing102<out T>(headers: Map<String, List<String>> = emptyMap()) : Informational1XX<T>(102, headers) {
                override fun toString() = "Processing102(headers=$headers)"
            }
        }

        /* 2XX Success results */
        sealed class Success2XX<out T>(code: Int, headers: Map<String, List<String>> = emptyMap()) : Some<T>(code, headers) {

            class Ok200<out T>(val body: T, headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(200, headers) {
                override fun toString() = "Ok200(body=$body, headers=$headers)"
            }

            class Created201<out T>(val body: T, headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(201, headers) {
                override fun toString() = "Created201(body = $body, headers=$headers)"
            }

            class Accepted202<out T>(val body: T, headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(202, headers) {
                override fun toString() = "Accepted202(body=$body, headers=$headers)"
            }

            class NonAuthoritativeInformation203<out T>(val body: T, headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(203, headers) {
                override fun toString() = "NonAuthoritativeInformation203(body=$body, headers=$headers)"
            }

            class NoContent204<out T>(headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(204, headers) {
                override fun toString() = "NoContent204(headers=$headers)"
            }

            class ResetContent205<out T>(headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(205, headers) {
                override fun toString() = "ResetContent205(headers=$headers)"
            }

            class PartialContent206<out T>(val body: T, headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(206, headers) {
                override fun toString() = "PartialContent206(body=$body, headers=$headers)"
            }

            class MultiStatus207<out T>(val body: T, headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(207, headers) {
                override fun toString() = "MultiStatus207(body=$body, headers=$headers)"
            }

            class AlreadyReported208<out T>(val body: T, headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(208, headers) {
                override fun toString() = "AlreadyReported208(body=$body, headers=$headers)"
            }

            class IMUsed226<out T>(val body: T, headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(226, headers) {
                override fun toString() = "IMUsed226(body=$body, headers=$headers)"
            }
        }

        /* 3XX Redirection results */
        sealed class Redirection3XX<out T>(code: Int, headers: Map<String, List<String>> = emptyMap()) : Some<T>(code, headers) {
            override fun toString() = "Redirection3XX(headers=$headers)"

            class MultipleChoices300<out T>(headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(300, headers) {
                override fun toString() = "MultipleChoices300(headers=$headers)"
            }

            class MovedPermanently301<out T>(headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(301, headers) {
                override fun toString() = "MovedPermanently301(headers=$headers)"
            }

            class Found302<out T>(headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(302, headers) {
                override fun toString() = "Found302(headers=$headers)"
            }

            class SeeOther303<out T>(headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(303, headers) {
                override fun toString() = "SeeOther303(headers=$headers)"
            }

            class NotModified304<out T>(headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(304, headers) {
                override fun toString() = "NotModified304(headers=$headers)"
            }

            class UseProxy305<out T>(headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(305, headers) {
                override fun toString() = "UseProxy305(headers=$headers)"
            }

            class TemporaryRedirect307<out T>(headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(307, headers) {
                override fun toString() = "TemporaryRedirect307(headers=$headers)"
            }

            class PermanentRedirect308<out T>(headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(308, headers) {
                override fun toString() = "PermanentRedirect308(headers=$headers)"
            }
        }

        /* 4XX Client error results */
        sealed class ClientError4XX<out T>(code: Int, headers: Map<String, List<String>> = emptyMap()) : Some<T>(code, headers) {
            override fun toString() = "ClientError4XX(headers=$headers)"

            class BadRequest400<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(400, headers) {
                override fun toString() = "BadRequest400(headers=$headers)"
            }

            class Unauthorized401<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(401, headers) {
                override fun toString() = "Unauthorized401(headers=$headers)"
            }

            class PaymentRequired402<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(402, headers) {
                override fun toString() = "PaymentRequired403(headers=$headers)"
            }

            class Forbidden403<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(403, headers) {
                override fun toString() = "Forbidden403(headers=$headers)"
            }

            class NotFound404<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(404, headers) {
                override fun toString() = "NotFound404(headers=$headers)"
            }

            class MethodNotAllowed405<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(405, headers) {
                override fun toString() = "MethodNotAllowed405(headers=$headers)"
            }

            class NotAcceptable406<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(406, headers) {
                override fun toString() = "NotAcceptable406(headers=$headers)"
            }

            class ProxyAuthenticationRequired407<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(407, headers) {
                override fun toString() = "ProxyAuthenticationRequired407(headers=$headers)"
            }

            class RequestTimeout408<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(408, headers) {
                override fun toString() = "RequestTimeout408(headers=$headers)"
            }

            class Conflict409<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(409, headers) {
                override fun toString() = "Conflict409(headers=$headers)"
            }

            class Gone410<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(410, headers) {
                override fun toString() = "Gone410(headers=$headers)"
            }

            class LengthRequired411<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(411, headers) {
                override fun toString() = "LengthRequired411(headers=$headers)"
            }

            class PreconditionFailed412<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(412, headers) {
                override fun toString() = "PreconditionFailed412(headers=$headers)"
            }

            class PayloadTooLarge413<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(413, headers) {
                override fun toString() = "PayloadTooLarge413(headers=$headers)"
            }

            class URITooLong414<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(414, headers) {
                override fun toString() = "URITooLong414(headers=$headers)"
            }

            class UnsupportedMediaType415<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(415, headers) {
                override fun toString() = "UnsupportedMediaType415(headers=$headers)"
            }

            class RangeNotSatisfiable416<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(416, headers) {
                override fun toString() = "RangeNotSatisfiable416(headers=$headers)"
            }

            class ExpectationFailed417<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(417, headers) {
                override fun toString() = "ExpectationFailed417(headers=$headers)"
            }

            class MisdirectedRequest421<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(421, headers) {
                override fun toString() = "MisdirectedRequest421(headers=$headers)"
            }

            class UnprocessableEntry422<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(422, headers) {
                override fun toString() = "UnprocessableEntry422(headers=$headers)"
            }

            class Locked423<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(423, headers) {
                override fun toString() = "Locked423(headers=$headers)"
            }

            class FailedDependency424<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(424, headers) {
                override fun toString() = "FailedDependency424(headers=$headers)"
            }

            class UpgradeRequired426<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(426, headers) {
                override fun toString() = "UpgradeRequired426(headers=$headers)"
            }

            class PreconditionRequired428<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(428, headers) {
                override fun toString() = "PreconditionRequired428(headers=$headers)"
            }

            class TooManyRequests429<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(429, headers) {
                override fun toString() = "TooManyRequests429(headers=$headers)"
            }

            class RequestHeaderFieldsTooLarge431<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(431, headers) {
                override fun toString() = "RequestHeaderFieldsTooLarge431(headers=$headers)"
            }

            class UnavailableForLegalReasons451<out T>(headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(451, headers) {
                override fun toString() = "UnavailableForLegalReasons451(headers=$headers)"
            }
        }

        /* 5XX Server error results */
        sealed class ServerError5xx<out T>(code: Int, headers: Map<String, List<String>> = emptyMap()) : Some<T>(code, headers) {
            override fun toString() = "ServerError5xx(headers=$headers)"

            class InternalServerError500<out T>(headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(500, headers) {
                override fun toString() = "InternalServerError500(headers=$headers)"
            }

            class NotImplementedError501<out T>(headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(501, headers) {
                override fun toString() = "NotImplementedError501(headers=$headers)"
            }

            class BadGateway502<out T>(headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(502, headers) {
                override fun toString() = "BadGateway501(headers=$headers)"
            }

            class ServiceUnavailable503<out T>(headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(503, headers) {
                override fun toString() = "ServiceUnavailable503(headers=$headers)"
            }

            class GatewayTimeout504<out T>(headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(504, headers) {
                override fun toString() = "GatewayTimeout504(headers=$headers)"
            }

            class HTTPVersionNotSupported505<out T>(headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(505, headers) {
                override fun toString() = "HTTPVersionNotSupported505(headers=$headers)"
            }

            class VariantAlsoNegotiates506<out T>(headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(506, headers) {
                override fun toString() = "VariantAlsoNegotiates506(headers=$headers)"
            }

            class InsufficientStorage507<out T>(headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(507, headers) {
                override fun toString() = "InsufficientStorage507(headers=$headers)"
            }

            class LoopDetected508<out T>(headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(508, headers) {
                override fun toString() = "LoopDetected508(headers=$headers)"
            }

            class NotExtended510<out T>(headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(510, headers) {
                override fun toString() = "NotExtended510(headers=$headers)"
            }

            class NetworkAuthenticationRequired511<out T>(headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(511, headers) {
                override fun toString() = "NetworkAuthenticationRequired511(headers=$headers)"
            }
        }
    }

    /* Network error */
    class NetworkError<out T>(val e: IOException) : SealedApiResult<T>() {
        override fun toString() = "NetworkError(e=${e.message})"
    }
}