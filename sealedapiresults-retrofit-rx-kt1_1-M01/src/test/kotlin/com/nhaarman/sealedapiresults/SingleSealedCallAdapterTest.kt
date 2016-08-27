package com.nhaarman.sealedapiresults

import com.nhaarman.expect.expect
import com.nhaarman.mockito_kotlin.*
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rx.Subscriber
import rx.functions.Action1
import rx.schedulers.TestScheduler
import java.io.IOException

internal class SingleSealedCallAdapterTest {

    val successResult = "test"

    lateinit var mAdapter: SingleSealedCallAdapter

    @Before
    fun setup() {
        mAdapter = SingleSealedCallAdapter(ObservableSealedCallAdapter(String::class.java))
    }

    @Test
    fun networkError() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(networkErrorCall()).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<NetworkError<String>>()
        })
    }

    @Test
    fun fatalError() {
        /* Given */
        val mock = spy<Subscriber<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(fatalErrorCall()).subscribe(mock)

        /* Then */
        verify(mock).onError(any())
    }

    @Test
    fun cancelled() {
        /* Given */
        val mock = spy<Subscriber<SealedApiResult<String>>>()

        val scheduler = TestScheduler()
        val adapter = SingleSealedCallAdapter(ObservableSealedCallAdapter(String::class.java, scheduler))
        val subscription = adapter.adapt(successCall(200)).subscribe(mock)

        /* When */
        subscription.unsubscribe()
        scheduler.triggerActions()

        /* Then */
        verify(mock, never()).onNext(any())
    }

    @Test
    fun notCancelled() {
        /* Given */
        val mock = spy<Subscriber<SealedApiResult<String>>>()

        val scheduler = TestScheduler()
        val adapter = SingleSealedCallAdapter(ObservableSealedCallAdapter(String::class.java, scheduler))
        adapter.adapt(successCall(200)).subscribe(mock)

        /* When */
        scheduler.triggerActions()

        /* Then */
        verify(mock).onNext(any())
    }

    @Test
    fun ok200() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(successCall(200)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<Ok200<String>>() {
                expect(it.body).toBe("test")
            }
        })
    }

    @Test
    fun created201() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(successCall(201)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<Created201<String>>() {
                expect(it.body).toBe("test")
            }
        })
    }

    @Test
    fun accepted202() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(successCall(202)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<Accepted202<String>>() {
                expect(it.body).toBe("test")
            }
        })
    }

    @Test
    fun nonAuthoritativeInformation203() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(successCall(203)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<NonAuthoritativeInformation203<String>>() {
                expect(it.body).toBe("test")
            }
        })
    }

    @Test
    fun noContent204() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(successCall(204)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<NoContent204<String>>()
        })
    }

    @Test
    fun resetContent205() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(successCall(205)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<ResetContent205<String>>()
        })
    }

    @Test
    fun partialContent206() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(successCall(206)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<PartialContent206<String>>() {
                expect(it.body).toBe("test")
            }
        })
    }

    @Test
    fun multiStatus207() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(successCall(207)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<MultiStatus207<String>>() {
                expect(it.body).toBe("test")
            }
        })
    }

    @Test
    fun alreadyReported208() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(successCall(208)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<AlreadyReported208<String>>() {
                expect(it.body).toBe("test")
            }
        })
    }

    @Test
    fun iMUsed226() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(successCall(226)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<IMUsed226<String>>() {
                expect(it.body).toBe("test")
            }
        })
    }

    @Test
    fun badRequest400() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(400)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<BadRequest400<String>>()
        })
    }

    @Test
    fun unauthorized401() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(401)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<Unauthorized401<String>>()
        })
    }

    @Test
    fun paymentRequired402() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(402)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<PaymentRequired402<String>>()
        })
    }

    @Test
    fun forbidden403() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(403)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<Forbidden403<String>>()
        })
    }

    @Test
    fun notFound404() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(404)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<NotFound404<String>>()
        })
    }

    @Test
    fun methodNotAllowed405() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(405)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<MethodNotAllowed405<String>>()
        })
    }

    @Test
    fun notAcceptable406() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(406)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<NotAcceptable406<String>>()
        })
    }

    @Test
    fun proxyAuthenticationRequired407() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(407)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<ProxyAuthenticationRequired407<String>>()
        })
    }

    @Test
    fun requestTimeout408() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(408)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<RequestTimeout408<String>>()
        })
    }

    @Test
    fun conflict409() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(409)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<Conflict409<String>>()
        })
    }

    @Test
    fun gone410() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(410)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<Gone410<String>>()
        })
    }

    @Test
    fun lengthRequired411() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(411)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<LengthRequired411<String>>()
        })
    }

    @Test
    fun preconditionFailed412() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(412)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<PreconditionFailed412<String>>()
        })
    }

    @Test
    fun payloadTooLarge413() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(413)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<PayloadTooLarge413<String>>()
        })
    }

    @Test
    fun uRITooLong414() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(414)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<URITooLong414<String>>()
        })
    }

    @Test
    fun unsupportedMediaType415() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(415)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<UnsupportedMediaType415<String>>()
        })
    }

    @Test
    fun rangeNotSatisfiable416() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(416)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<RangeNotSatisfiable416<String>>()
        })
    }

    @Test
    fun expectationFailed417() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(417)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<ExpectationFailed417<String>>()
        })
    }

    @Test
    fun misdirectedRequest421() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(421)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<MisdirectedRequest421<String>>()
        })
    }

    @Test
    fun unprocessableEntry422() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(422)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<UnprocessableEntry422<String>>()
        })
    }

    @Test
    fun locked423() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(423)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<Locked423<String>>()
        })
    }

    @Test
    fun failedDependency424() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(424)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<FailedDependency424<String>>()
        })
    }

    @Test
    fun upgradeRequired426() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(426)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<UpgradeRequired426<String>>()
        })
    }

    @Test
    fun preconditionRequired428() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(428)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<PreconditionRequired428<String>>()
        })
    }

    @Test
    fun tooManyRequests429() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(429)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<TooManyRequests429<String>>()
        })
    }

    @Test
    fun requestHeaderFieldsTooLarge431() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(431)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<RequestHeaderFieldsTooLarge431<String>>()
        })
    }

    @Test
    fun unavailableForLegalReasons451() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(451)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<UnavailableForLegalReasons451<String>>()
        })
    }

    @Test
    fun internalServerError500() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(500)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<InternalServerError500<String>>()
        })
    }

    @Test
    fun notImplementedError501() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(501)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<NotImplementedError501<String>>()
        })
    }

    @Test
    fun badGateway502() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(502)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<BadGateway502<String>>()
        })
    }

    @Test
    fun serviceUnavailable503() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(503)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<ServiceUnavailable503<String>>()
        })
    }

    @Test
    fun gatewayTimeout504() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(504)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<GatewayTimeout504<String>>()
        })
    }

    @Test
    fun hTTPVersionNotSupported505() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(505)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<HTTPVersionNotSupported505<String>>()
        })
    }

    @Test
    fun variantAlsoNegotiates506() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(506)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<VariantAlsoNegotiates506<String>>()
        })
    }

    @Test
    fun insufficientStorage507() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(507)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<InsufficientStorage507<String>>()
        })
    }

    @Test
    fun loopDetected508() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(508)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<LoopDetected508<String>>()
        })
    }

    @Test
    fun notExtended510() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(510)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<NotExtended510<String>>()
        })
    }

    @Test
    fun networkAuthenticationRequired511() {
        /* Given */
        val mock = mock<Action1<SealedApiResult<String>>>()

        /* When */
        mAdapter.adapt(nonSuccessCall(511)).subscribe(mock)

        /* Then */
        verify(mock).call(capture {
            expect(it).toBeInstanceOf<NetworkAuthenticationRequired511<String>>()
        })
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

                override fun clone(): Call<String> {
                    return successCall(responseCode)
                }
            }

    private fun nonSuccessCall(responseCode: Int): Call<String> {
        return object : SimpleCall<String>() {
            override fun execute(): Response<String> {
                return Response.error(responseCode, ResponseBody.create(null, ""))
            }

            override fun clone(): Call<String> {
                return nonSuccessCall(responseCode)
            }
        }
    }

    private fun networkErrorCall(): Call<String> {
        return object : SimpleCall<String>() {
            override fun clone(): Call<String> {
                return networkErrorCall()
            }

            override fun execute(): Response<String> {
                throw IOException()
            }
        }
    }

    private fun fatalErrorCall(): Call<String> {
        return object : SimpleCall<String>() {
            override fun clone(): Call<String> {
                return fatalErrorCall()
            }

            override fun execute(): Response<String> {
                throw Error()
            }
        }
    }

    private abstract class SimpleCall<T> : Call<T> {

        private var isCanceled = false

        override fun cancel() {
            isCanceled = true
        }

        override fun isCanceled(): Boolean = isCanceled
        override fun request(): Request = error("")
        override fun enqueue(callback: Callback<T>?) = error("")
        override fun isExecuted(): Boolean = error("")
    }
}
