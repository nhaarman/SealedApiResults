package com.nhaarman.sealedapiresults

import com.nhaarman.sealedapiresults.SealedApiResult.Some
import com.nhaarman.sealedapiresults.SealedApiResult.Some.ClientError4XX.*
import com.nhaarman.sealedapiresults.SealedApiResult.Some.Informational1XX.*
import com.nhaarman.sealedapiresults.SealedApiResult.Some.Redirection3XX.*
import com.nhaarman.sealedapiresults.SealedApiResult.Some.ServerError5xx.*
import com.nhaarman.sealedapiresults.SealedApiResult.Some.Success2XX.*
import retrofit2.Response

fun <R : Any?> Response<R>.toSealedApiResult(): Some<R> {
    return when (code) {
        100 -> Continue100<R>(headers)
        101 -> SwitchingProtocols101<R>(headers)
        102 -> Processing102<R>(headers)

        200 -> Ok200(body, headers)
        201 -> Created201(body, headers)
        202 -> Accepted202(body, headers)
        203 -> NonAuthoritativeInformation203(body, headers)
        204 -> NoContent204<R>(headers)
        205 -> ResetContent205<R>(headers)
        206 -> PartialContent206(body, headers)
        207 -> MultiStatus207(body, headers)
        208 -> AlreadyReported208(body, headers)
        226 -> IMUsed226(body, headers)

        300 -> MultipleChoices300<R>(headers)
        301 -> MovedPermanently301<R>(headers)
        302 -> Found302<R>(headers)
        303 -> SeeOther303<R>(headers)
        304 -> NotModified304<R>(headers)
        305 -> UseProxy305<R>(headers)
        307 -> TemporaryRedirect307<R>(headers)
        308 -> PermanentRedirect308<R>(headers)

        400 -> BadRequest400<R>(headers)
        401 -> Unauthorized401<R>(headers)
        402 -> PaymentRequired402<R>(headers)
        403 -> Forbidden403<R>(headers)
        404 -> NotFound404<R>(headers)
        405 -> MethodNotAllowed405<R>(headers)
        406 -> NotAcceptable406<R>(headers)
        407 -> ProxyAuthenticationRequired407<R>(headers)
        408 -> RequestTimeout408<R>(headers)
        409 -> Conflict409<R>(headers)
        410 -> Gone410<R>(headers)
        411 -> LengthRequired411<R>(headers)
        412 -> PreconditionFailed412<R>(headers)
        413 -> PayloadTooLarge413<R>(headers)
        414 -> URITooLong414<R>(headers)
        415 -> UnsupportedMediaType415<R>(headers)
        416 -> RangeNotSatisfiable416<R>(headers)
        417 -> ExpectationFailed417<R>(headers)
        421 -> MisdirectedRequest421<R>(headers)
        422 -> UnprocessableEntry422<R>(headers)
        423 -> Locked423<R>(headers)
        424 -> FailedDependency424<R>(headers)
        426 -> UpgradeRequired426<R>(headers)
        428 -> PreconditionRequired428<R>(headers)
        429 -> TooManyRequests429<R>(headers)
        431 -> RequestHeaderFieldsTooLarge431<R>(headers)
        451 -> UnavailableForLegalReasons451<R>(headers)

        500 -> InternalServerError500<R>(headers)
        501 -> NotImplementedError501<R>(headers)
        502 -> BadGateway502<R>(headers)
        503 -> ServiceUnavailable503<R>(headers)
        504 -> GatewayTimeout504<R>(headers)
        505 -> HTTPVersionNotSupported505<R>(headers)
        506 -> VariantAlsoNegotiates506<R>(headers)
        507 -> InsufficientStorage507<R>(headers)
        508 -> LoopDetected508<R>(headers)
        510 -> NotExtended510<R>(headers)
        511 -> NetworkAuthenticationRequired511<R>(headers)

        else -> error("Illegal status code: $code")
    }
}

private val Response<*>.code: Int
    get() = code()

private val <R> Response<R>.body: R
    get() = body()

private val Response<*>.headers: Map<String, List<String>>
    get() = headers().toMultimap()
