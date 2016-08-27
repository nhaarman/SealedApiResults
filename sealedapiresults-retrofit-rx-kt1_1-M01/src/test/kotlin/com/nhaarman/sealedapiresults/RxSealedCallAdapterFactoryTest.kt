package com.nhaarman.sealedapiresults

import com.google.common.reflect.TypeToken
import com.nhaarman.expect.expect
import com.nhaarman.expect.expectErrorWithMessage
import okhttp3.Request
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import rx.Observable
import rx.Single


class RxSealedCallAdapterFactoryTest {

    lateinit var retrofit: Retrofit
    lateinit var factory: RxSealedCallAdapterFactory

    @Before
    fun setup() {
        retrofit = Retrofit.Builder().baseUrl("http://localhost/").build()
        factory = RxSealedCallAdapterFactory()
    }

    @Test
    fun nonSingleOrObservableType_returnsNull() {
        /* When */
        val result = factory.get(String::class.java, emptyArray(), retrofit)

        /* Then */
        expect(result).toBeNull()
    }

    @Test
    fun nonParameterizedSingleType_throws() {
        /* Expect */
        expectErrorWithMessage("return type must be parameterized as Single<SealedApiResult<Foo>> or Single<SealedApiResult<out Foo>>") on {
            /* When */
            factory.get(Single::class.java, emptyArray(), retrofit)
        }
    }

    @Test
    fun nonParameterizedObservableType_throws() {
        /* Expect */
        expectErrorWithMessage("return type must be parameterized as Observable<SealedApiResult<Foo>> or Observable<SealedApiResult<out Foo>>") on {
            /* When */
            factory.get(Observable::class.java, emptyArray(), retrofit)
        }
    }

    @Test
    fun properSingleType_returnsProperCallAdapter() {
        /* Given */
        val type = object : TypeToken<Single<SealedApiResult<String>>>() {}.type

        /* When */
        val result = factory.get(type, emptyArray(), retrofit)

        /* Then */
        expect(result?.responseType()).toBe(String::class.java)
        expect(result?.adapt(SimpleCall("test"))).toBeInstanceOf<Single<SealedApiResult<String>>>()
    }

    @Test
    fun properObservableType_returnsProperCallAdapter() {
        /* Given */
        val type = object : TypeToken<Observable<SealedApiResult<String>>>() {}.type

        /* When */
        val result = factory.get(type, emptyArray(), retrofit)

        /* Then */
        expect(result?.responseType()).toBe(String::class.java)
        expect(result?.adapt(SimpleCall("test"))).toBeInstanceOf<Observable<SealedApiResult<String>>>()
    }

    @Test
    fun properOutType_returnsProperCallAdapter() {
        /* Given */
        val type = object : TypeToken<Observable<SealedApiResult<out String>>>() {}.type

        /* When */
        val result = factory.get(type, emptyArray(), retrofit)

        /* Then */
        expect(result?.responseType()).toBe(String::class.java)
    }

    @Test
    fun properArrayType_returnsProperCallAdapter() {
        /* Given */
        val type = object : TypeToken<Observable<SealedApiResult<Array<String>>>>() {}.type

        /* When */
        val result = factory.get(type, emptyArray(), retrofit)

        /* Then */
        expect(result?.responseType()).toBe(Array<String>::class.java)
    }

    @Test
    fun properWildcardType_returnsProperCallAdapter() {
        /* Given */
        val type = object : TypeToken<Observable<SealedApiResult<*>>>() {}.type

        /* When */
        val result = factory.get(type, emptyArray(), retrofit)

        /* Then */
        expect(result?.responseType()).toBe(Any::class.java)
    }

    private class SimpleCall<T>(private val result: T) : Call<T> {

        override fun clone(): Call<T> {
            return SimpleCall(result)
        }

        override fun execute(): Response<T> {
            return Response.success(result)
        }

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
