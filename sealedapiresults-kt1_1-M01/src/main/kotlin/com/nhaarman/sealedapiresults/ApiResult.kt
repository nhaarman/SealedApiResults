package com.nhaarman.sealedapiresults

import java.io.IOException

// https://tools.ietf.org/html/rfc7231
// http://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml

sealed class SealedApiResult<out T>

sealed class Some<out T>(val code: Int, open val headers: Map<String, List<String>> = emptyMap()) : SealedApiResult<T>()

/* 1XX Informational results */
sealed class Informational1XX<out T>(code: Int, headers: Map<String, List<String>> = emptyMap()) : Some<T>(code, headers)

data class Continue100<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Informational1XX<T>(100, headers)

data class SwitchingProtocols101<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Informational1XX<T>(101, headers)

data class Processing102<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Informational1XX<T>(102, headers)

/* 2XX Success results */
sealed class Success2XX<out T>(code: Int, headers: Map<String, List<String>> = emptyMap()) : Some<T>(code, headers)

data class Ok200<out T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(200, headers)

data class Created201<out T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(201, headers)

data class Accepted202<out T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(202, headers)

data class NonAuthoritativeInformation203<out T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(203, headers)

data class NoContent204<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(204, headers)

data class ResetContent205<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(205, headers)

data class PartialContent206<out T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(206, headers)

data class MultiStatus207<out T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(207, headers)

data class AlreadyReported208<out T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(208, headers)

data class IMUsed226<out T>(val body: T, override val headers: Map<String, List<String>> = emptyMap()) : Success2XX<T>(226, headers)

/* 3XX Redirection results */
sealed class Redirection3XX<out T>(code: Int, headers: Map<String, List<String>> = emptyMap()) : Some<T>(code, headers)

data class MultipleChoices300<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(300, headers)

data class MovedPermanently301<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(301, headers)

data class Found302<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(302, headers)

data class SeeOther303<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(303, headers)

data class NotModified304<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(304, headers)

data class UseProxy305<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(305, headers)

data class TemporaryRedirect307<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(307, headers)

data class PermanentRedirect308<out T>(override val headers: Map<String, List<String>> = emptyMap()) : Redirection3XX<T>(308, headers)

/* 4XX Client error results */
sealed class ClientError4XX<out T>(code: Int, headers: Map<String, List<String>> = emptyMap()) : Some<T>(code, headers)

data class BadRequest400<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(400, headers)

data class Unauthorized401<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(401, headers)

data class PaymentRequired402<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(402, headers)

data class Forbidden403<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(403, headers)

data class NotFound404<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(404, headers)

data class MethodNotAllowed405<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(405, headers)

data class NotAcceptable406<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(406, headers)

data class ProxyAuthenticationRequired407<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(407, headers)

data class RequestTimeout408<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(408, headers)

data class Conflict409<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(409, headers)

data class Gone410<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(410, headers)

data class LengthRequired411<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(411, headers)

data class PreconditionFailed412<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(412, headers)

data class PayloadTooLarge413<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(413, headers)

data class URITooLong414<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(414, headers)

data class UnsupportedMediaType415<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(415, headers)

data class RangeNotSatisfiable416<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(416, headers)

data class ExpectationFailed417<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(417, headers)

data class MisdirectedRequest421<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(421, headers)

data class UnprocessableEntry422<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(422, headers)

data class Locked423<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(423, headers)

data class FailedDependency424<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(424, headers)

data class UpgradeRequired426<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(426, headers)

data class PreconditionRequired428<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(428, headers)

data class TooManyRequests429<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(429, headers)

data class RequestHeaderFieldsTooLarge431<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(431, headers)

data class UnavailableForLegalReasons451<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ClientError4XX<T>(451, headers)

/* 5XX Server error results */
sealed class ServerError5xx<out T>(code: Int, headers: Map<String, List<String>> = emptyMap()) : Some<T>(code, headers)

data class InternalServerError500<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(500, headers)

data class NotImplementedError501<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(501, headers)

data class BadGateway502<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(502, headers)

data class ServiceUnavailable503<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(503, headers)

data class GatewayTimeout504<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(504, headers)

data class HTTPVersionNotSupported505<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(505, headers)

data class VariantAlsoNegotiates506<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(506, headers)

data class InsufficientStorage507<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(507, headers)

data class LoopDetected508<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(508, headers)

data class NotExtended510<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(510, headers)

data class NetworkAuthenticationRequired511<out T>(override val headers: Map<String, List<String>> = emptyMap()) : ServerError5xx<T>(511, headers)

/* Network error */
class NetworkError<out T>(val e: IOException) : SealedApiResult<T>()
