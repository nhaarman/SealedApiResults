package com.nhaarman.sealedapiresults

import java.io.IOException

// https://tools.ietf.org/html/rfc7231
// http://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml

sealed class ApiResult<out T> {

    abstract fun <R> map(map: ((T) -> R)): ApiResult<R>

    sealed class Some<out T>(open val code: Int, open val headers: Map<String, List<String>> = emptyMap()) : ApiResult<T>() {

        /* 1XX Informational results */
        sealed class Informational1XX<out T>(code: Int, headers: Map<String, List<String>> = emptyMap()) : Some<T>(code, headers) {

            data class Continue100<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Informational1XX<T>(100, headers) {
                override fun <R> map(map: ((T) -> R)) = Continue100<R>(headers)
            }

            data class SwitchingProtocols101<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Informational1XX<T>(101, headers) {
                override fun <R> map(map: ((T) -> R)) = SwitchingProtocols101<R>(headers)
            }

            data class Processing102<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Informational1XX<T>(102, headers) {
                override fun <R> map(map: ((T) -> R)) = Processing102<R>(headers)
            }
        }

        /* 2XX Success results */
        sealed class Success2XX<out T>(code: Int, headers: Map<String, List<String>> = emptyMap()) : Some<T>(code, headers) {

            data class Ok200<T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(200, headers) {
                override fun <R> map(map: ((T) -> R)) = Ok200(map(body), headers)
            }

            data class Created201<T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(201, headers) {
                override fun <R> map(map: ((T) -> R)) = Created201(map(body), headers)
            }

            data class Accepted202<T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(202, headers) {
                override fun <R> map(map: ((T) -> R)) = Accepted202(map(body), headers)
            }

            data class NonAuthoritativeInformation203<T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(203, headers) {
                override fun <R> map(map: ((T) -> R)) = NonAuthoritativeInformation203(map(body), headers)
            }

            data class NoContent204<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(204, headers) {
                override fun <R> map(map: ((T) -> R)) = NoContent204<R>(headers)
            }

            data class ResetContent205<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(205, headers) {
                override fun <R> map(map: ((T) -> R)) = ResetContent205<R>(headers)
            }

            data class PartialContent206<T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(206, headers) {
                override fun <R> map(map: ((T) -> R)) = PartialContent206(map(body), headers)
            }

            data class MultiStatus207<T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(207, headers) {
                override fun <R> map(map: ((T) -> R)) = MultiStatus207(map(body), headers)
            }

            data class AlreadyReported208<T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(208, headers) {
                override fun <R> map(map: ((T) -> R)) = AlreadyReported208(map(body), headers)
            }

            data class IMUsed226<T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(226, headers) {
                override fun <R> map(map: ((T) -> R)) = IMUsed226(map(body), headers)
            }
        }

        /* 3XX Redirection results */
        sealed class Redirection3XX<out T>(code: Int, headers: Map<String, List<String>> = emptyMap()) : Some<T>(code, headers) {

            data class MultipleChoices300<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(300, headers) {
                override fun <R> map(map: ((T) -> R)) = MultipleChoices300<R>(headers)
            }

            data class MovedPermanently301<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(301, headers) {
                override fun <R> map(map: ((T) -> R)) = MovedPermanently301<R>(headers)
            }

            data class Found302<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(302, headers) {
                override fun <R> map(map: ((T) -> R)) = Found302<R>(headers)
            }

            data class SeeOther303<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(303, headers) {
                override fun <R> map(map: ((T) -> R)) = SeeOther303<R>(headers)
            }

            data class NotModified304<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(304, headers) {
                override fun <R> map(map: ((T) -> R)) = NotModified304<R>(headers)
            }

            data class UseProxy305<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(305, headers) {
                override fun <R> map(map: ((T) -> R)) = UseProxy305<R>(headers)
            }

            data class TemporaryRedirect307<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(307, headers) {
                override fun <R> map(map: ((T) -> R)) = TemporaryRedirect307<R>(headers)
            }

            data class PermanentRedirect308<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(308, headers) {
                override fun <R> map(map: ((T) -> R)) = PermanentRedirect308<R>(headers)
            }
        }

        /* 4XX Client error results */
        sealed class ClientError4XX<out T>(code: Int, headers: Map<String, List<String>> = emptyMap()) : Some<T>(code, headers) {

            data class BadRequest400<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(400, headers) {
                override fun <R> map(map: ((T) -> R)) = BadRequest400<R>(headers)
            }

            data class Unauthorized401<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(401, headers) {
                override fun <R> map(map: ((T) -> R)) = Unauthorized401<R>(headers)
            }

            data class PaymentRequired402<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(402, headers) {
                override fun <R> map(map: ((T) -> R)) = PaymentRequired402<R>(headers)
            }

            data class Forbidden403<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(403, headers) {
                override fun <R> map(map: ((T) -> R)) = Forbidden403<R>(headers)
            }

            data class NotFound404<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(404, headers) {
                override fun <R> map(map: ((T) -> R)) = NotFound404<R>(headers)
            }

            data class MethodNotAllowed405<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(405, headers) {
                override fun <R> map(map: ((T) -> R)) = MethodNotAllowed405<R>(headers)
            }

            data class NotAcceptable406<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(406, headers) {
                override fun <R> map(map: ((T) -> R)) = NotAcceptable406<R>(headers)
            }

            data class ProxyAuthenticationRequired407<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(407, headers) {
                override fun <R> map(map: ((T) -> R)) = ProxyAuthenticationRequired407<R>(headers)
            }

            data class RequestTimeout408<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(408, headers) {
                override fun <R> map(map: ((T) -> R)) = RequestTimeout408<R>(headers)
            }

            data class Conflict409<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(409, headers) {
                override fun <R> map(map: ((T) -> R)) = Conflict409<R>(headers)
            }

            data class Gone410<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(410, headers) {
                override fun <R> map(map: ((T) -> R)) = Gone410<R>(headers)
            }

            data class LengthRequired411<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(411, headers) {
                override fun <R> map(map: ((T) -> R)) = LengthRequired411<R>(headers)
            }

            data class PreconditionFailed412<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(412, headers) {
                override fun <R> map(map: ((T) -> R)) = PreconditionFailed412<R>(headers)
            }

            data class PayloadTooLarge413<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(413, headers) {
                override fun <R> map(map: ((T) -> R)) = PayloadTooLarge413<R>(headers)
            }

            data class URITooLong414<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(414, headers) {
                override fun <R> map(map: ((T) -> R)) = URITooLong414<R>(headers)
            }

            data class UnsupportedMediaType415<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(415, headers) {
                override fun <R> map(map: ((T) -> R)) = UnsupportedMediaType415<R>(headers)
            }

            data class RangeNotSatisfiable416<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(416, headers) {
                override fun <R> map(map: ((T) -> R)) = RangeNotSatisfiable416<R>(headers)
            }

            data class ExpectationFailed417<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(417, headers) {
                override fun <R> map(map: ((T) -> R)) = ExpectationFailed417<R>(headers)
            }

            data class MisdirectedRequest421<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(421, headers) {
                override fun <R> map(map: ((T) -> R)) = MisdirectedRequest421<R>(headers)
            }

            data class UnprocessableEntry422<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(422, headers) {
                override fun <R> map(map: ((T) -> R)) = UnprocessableEntry422<R>(headers)
            }

            data class Locked423<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(423, headers) {
                override fun <R> map(map: ((T) -> R)) = Locked423<R>(headers)
            }

            data class FailedDependency424<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(424, headers) {
                override fun <R> map(map: ((T) -> R)) = FailedDependency424<R>(headers)
            }

            data class UpgradeRequired426<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(426, headers) {
                override fun <R> map(map: ((T) -> R)) = UpgradeRequired426<R>(headers)
            }

            data class PreconditionRequired428<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(428, headers) {
                override fun <R> map(map: ((T) -> R)) = PreconditionRequired428<R>(headers)
            }

            data class TooManyRequests429<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(429, headers) {
                override fun <R> map(map: ((T) -> R)) = TooManyRequests429<R>(headers)
            }

            data class RequestHeaderFieldsTooLarge431<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(431, headers) {
                override fun <R> map(map: ((T) -> R)) = RequestHeaderFieldsTooLarge431<R>(headers)
            }

            data class UnavailableForLegalReasons451<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(451, headers) {
                override fun <R> map(map: ((T) -> R)) = UnavailableForLegalReasons451<R>(headers)
            }
        }

        /* 5XX Server error results */
        sealed class ServerError5xx<out T>(code: Int, headers: Map<String, List<String>> = emptyMap()) : Some<T>(code, headers) {

            data class InternalServerError500<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(500, headers) {
                override fun <R> map(map: ((T) -> R)) = InternalServerError500<R>(headers)
            }

            data class NotImplementedError501<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(501, headers) {
                override fun <R> map(map: ((T) -> R)) = NotImplementedError501<R>(headers)
            }

            data class BadGateway502<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(502, headers) {
                override fun <R> map(map: ((T) -> R)) = BadGateway502<R>(headers)
            }

            data class ServiceUnavailable503<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(503, headers) {
                override fun <R> map(map: ((T) -> R)) = ServiceUnavailable503<R>(headers)
            }

            data class GatewayTimeout504<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(504, headers) {
                override fun <R> map(map: ((T) -> R)) = GatewayTimeout504<R>(headers)
            }

            data class HTTPVersionNotSupported505<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(505, headers) {
                override fun <R> map(map: ((T) -> R)) = HTTPVersionNotSupported505<R>(headers)
            }

            data class VariantAlsoNegotiates506<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(506, headers) {
                override fun <R> map(map: ((T) -> R)) = VariantAlsoNegotiates506<R>(headers)
            }

            data class InsufficientStorage507<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(507, headers) {
                override fun <R> map(map: ((T) -> R)) = InsufficientStorage507<R>(headers)
            }

            data class LoopDetected508<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(508, headers) {
                override fun <R> map(map: ((T) -> R)) = LoopDetected508<R>(headers)
            }

            data class NotExtended510<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(510, headers) {
                override fun <R> map(map: ((T) -> R)) = NotExtended510<R>(headers)
            }

            data class NetworkAuthenticationRequired511<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(511, headers) {
                override fun <R> map(map: ((T) -> R)) = NetworkAuthenticationRequired511<R>(headers)
            }
        }
    }

    /* Network error */
    class NetworkError<T>(val e: IOException) : ApiResult<T>() {
        override fun <R> map(map: ((T) -> R)) = NetworkError<R>(e)
    }
}