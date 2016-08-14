package com.nhaarman.sealedapiresults

import com.nhaarman.expect.expect
import com.nhaarman.sealedapiresults.SealedApiResult.NetworkError
import com.nhaarman.sealedapiresults.SealedApiResult.Some.ClientError4XX.*
import com.nhaarman.sealedapiresults.SealedApiResult.Some.ServerError5xx.*
import com.nhaarman.sealedapiresults.SealedApiResult.Some.Success2XX.*
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

internal class SealedCallAdapterTest {

    val successResult = "test"

    lateinit var adapter: SealedCallAdapter

    @Before
    fun setup() {
        adapter = SealedCallAdapter(String::class.java)
    }

    @Test
    fun networkError() {
        /* When */
        val result = adapter.adapt(object : SimpleCall<String>() {
            override fun execute(): Response<String> {
                throw IOException()
            }
        })

        /* Then */
        expect(result).toBeInstanceOf<NetworkError<String>>()
    }

    @Test
    fun ok200() {
        /* When */
        val result = adapter.adapt(successCall(200))

        /* Then */
        expect(result).toBeInstanceOf<Ok200<String>>() {
            expect(it.body).toBe("test")
        }
    }

    @Test
    fun created201() {
        /* When */
        val result = adapter.adapt(successCall(201))

        /* Then */
        expect(result).toBeInstanceOf<Created201<String>>() {
            expect(it.body).toBe("test")
        }
    }

    @Test
    fun accepted202() {
        /* When */
        val result = adapter.adapt(successCall(202))

        /* Then */
        expect(result).toBeInstanceOf<Accepted202<String>>() {
            expect(it.body).toBe("test")
        }
    }

    @Test
    fun nonAuthoritativeInformation203() {
        /* When */
        val result = adapter.adapt(successCall(203))

        /* Then */
        expect(result).toBeInstanceOf<NonAuthoritativeInformation203<String>>() {
            expect(it.body).toBe("test")
        }
    }

    @Test
    fun noContent204() {
        /* When */
        val result = adapter.adapt(successCall(204))

        /* Then */
        expect(result).toBeInstanceOf<NoContent204<String>>()
    }

    @Test
    fun resetContent205() {
        /* When */
        val result = adapter.adapt(successCall(205))

        /* Then */
        expect(result).toBeInstanceOf<ResetContent205<String>>()
    }

    @Test
    fun partialContent206() {
        /* When */
        val result = adapter.adapt(successCall(206))

        /* Then */
        expect(result).toBeInstanceOf<PartialContent206<String>>() {
            expect(it.body).toBe("test")
        }
    }

    @Test
    fun multiStatus207() {
        /* When */
        val result = adapter.adapt(successCall(207))

        /* Then */
        expect(result).toBeInstanceOf<MultiStatus207<String>>() {
            expect(it.body).toBe("test")
        }
    }

    @Test
    fun alreadyReported208() {
        /* When */
        val result = adapter.adapt(successCall(208))

        /* Then */
        expect(result).toBeInstanceOf<AlreadyReported208<String>>() {
            expect(it.body).toBe("test")
        }
    }

    @Test
    fun iMUsed226() {
        /* When */
        val result = adapter.adapt(successCall(226))

        /* Then */
        expect(result).toBeInstanceOf<IMUsed226<String>>() {
            expect(it.body).toBe("test")
        }
    }

    @Test
    fun badRequest400() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(400))

        /* Then */
        expect(result).toBeInstanceOf<BadRequest400<String>>()
    }

    @Test
    fun unauthorized401() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(401))

        /* Then */
        expect(result).toBeInstanceOf<Unauthorized401<String>>()
    }

    @Test
    fun paymentRequired402() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(402))

        /* Then */
        expect(result).toBeInstanceOf<PaymentRequired402<String>>()
    }

    @Test
    fun forbidden403() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(403))

        /* Then */
        expect(result).toBeInstanceOf<Forbidden403<String>>()
    }

    @Test
    fun notFound404() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(404))

        /* Then */
        expect(result).toBeInstanceOf<NotFound404<String>>()
    }

    @Test
    fun methodNotAllowed405() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(405))

        /* Then */
        expect(result).toBeInstanceOf<MethodNotAllowed405<String>>()
    }

    @Test
    fun notAcceptable406() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(406))

        /* Then */
        expect(result).toBeInstanceOf<NotAcceptable406<String>>()
    }

    @Test
    fun proxyAuthenticationRequired407() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(407))

        /* Then */
        expect(result).toBeInstanceOf<ProxyAuthenticationRequired407<String>>()
    }

    @Test
    fun requestTimeout408() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(408))

        /* Then */
        expect(result).toBeInstanceOf<RequestTimeout408<String>>()
    }

    @Test
    fun conflict409() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(409))

        /* Then */
        expect(result).toBeInstanceOf<Conflict409<String>>()
    }

    @Test
    fun gone410() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(410))

        /* Then */
        expect(result).toBeInstanceOf<Gone410<String>>()
    }

    @Test
    fun lengthRequired411() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(411))

        /* Then */
        expect(result).toBeInstanceOf<LengthRequired411<String>>()
    }

    @Test
    fun preconditionFailed412() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(412))

        /* Then */
        expect(result).toBeInstanceOf<PreconditionFailed412<String>>()
    }

    @Test
    fun payloadTooLarge413() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(413))

        /* Then */
        expect(result).toBeInstanceOf<PayloadTooLarge413<String>>()
    }

    @Test
    fun uRITooLong414() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(414))

        /* Then */
        expect(result).toBeInstanceOf<URITooLong414<String>>()
    }

    @Test
    fun unsupportedMediaType415() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(415))

        /* Then */
        expect(result).toBeInstanceOf<UnsupportedMediaType415<String>>()
    }

    @Test
    fun rangeNotSatisfiable416() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(416))

        /* Then */
        expect(result).toBeInstanceOf<RangeNotSatisfiable416<String>>()
    }

    @Test
    fun expectationFailed417() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(417))

        /* Then */
        expect(result).toBeInstanceOf<ExpectationFailed417<String>>()
    }

    @Test
    fun misdirectedRequest421() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(421))

        /* Then */
        expect(result).toBeInstanceOf<MisdirectedRequest421<String>>()
    }

    @Test
    fun unprocessableEntry422() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(422))

        /* Then */
        expect(result).toBeInstanceOf<UnprocessableEntry422<String>>()
    }

    @Test
    fun locked423() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(423))

        /* Then */
        expect(result).toBeInstanceOf<Locked423<String>>()
    }

    @Test
    fun failedDependency424() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(424))

        /* Then */
        expect(result).toBeInstanceOf<FailedDependency424<String>>()
    }

    @Test
    fun upgradeRequired426() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(426))

        /* Then */
        expect(result).toBeInstanceOf<UpgradeRequired426<String>>()
    }

    @Test
    fun preconditionRequired428() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(428))

        /* Then */
        expect(result).toBeInstanceOf<PreconditionRequired428<String>>()
    }

    @Test
    fun tooManyRequests429() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(429))

        /* Then */
        expect(result).toBeInstanceOf<TooManyRequests429<String>>()
    }

    @Test
    fun requestHeaderFieldsTooLarge431() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(431))

        /* Then */
        expect(result).toBeInstanceOf<RequestHeaderFieldsTooLarge431<String>>()
    }

    @Test
    fun unavailableForLegalReasons451() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(451))

        /* Then */
        expect(result).toBeInstanceOf<UnavailableForLegalReasons451<String>>()
    }

    @Test
    fun internalServerError500() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(500))

        /* Then */
        expect(result).toBeInstanceOf<InternalServerError500<String>>()
    }

    @Test
    fun notImplementedError501() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(501))

        /* Then */
        expect(result).toBeInstanceOf<NotImplementedError501<String>>()
    }

    @Test
    fun badGateway502() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(502))

        /* Then */
        expect(result).toBeInstanceOf<BadGateway502<String>>()
    }

    @Test
    fun serviceUnavailable503() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(503))

        /* Then */
        expect(result).toBeInstanceOf<ServiceUnavailable503<String>>()
    }

    @Test
    fun gatewayTimeout504() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(504))

        /* Then */
        expect(result).toBeInstanceOf<GatewayTimeout504<String>>()
    }

    @Test
    fun hTTPVersionNotSupported505() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(505))

        /* Then */
        expect(result).toBeInstanceOf<HTTPVersionNotSupported505<String>>()
    }

    @Test
    fun variantAlsoNegotiates506() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(506))

        /* Then */
        expect(result).toBeInstanceOf<VariantAlsoNegotiates506<String>>()
    }

    @Test
    fun insufficientStorage507() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(507))

        /* Then */
        expect(result).toBeInstanceOf<InsufficientStorage507<String>>()
    }

    @Test
    fun loopDetected508() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(508))

        /* Then */
        expect(result).toBeInstanceOf<LoopDetected508<String>>()
    }

    @Test
    fun notExtended510() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(510))

        /* Then */
        expect(result).toBeInstanceOf<NotExtended510<String>>()
    }

    @Test
    fun networkAuthenticationRequired511() {
        /* When */
        val result = adapter.adapt(nonSuccessCall(511))

        /* Then */
        expect(result).toBeInstanceOf<NetworkAuthenticationRequired511<String>>()
    }

    private fun successCall(responseCode: Int): Call<String> =
            object : SimpleCall<String>() {
                override fun execute(): Response<String> {
                    return Response.success(
                            successResult,
                            okhttp3.Response.Builder()
                                    .code(responseCode)
                                    .protocol(Protocol.HTTP_1_1)
                                    .request(Request.Builder().url("http://localhost/").build())
                                    .build()
                    )
                }
            }

    private fun nonSuccessCall(responseCode: Int): Call<String> {
        return object : SimpleCall<String>() {
            override fun execute(): Response<String> {
                return Response.error(responseCode, ResponseBody.create(null, ""))
            }
        }
    }

    private abstract class SimpleCall<T> : Call<T> {
        override fun cancel() = error("")
        override fun isCanceled(): Boolean = error("")
        override fun clone(): Call<T> = error("")
        override fun request(): Request = error("")
        override fun enqueue(callback: Callback<T>?) = error("")
        override fun isExecuted(): Boolean = error("")
    }
}

