package com.nhaarman.sealedapiresults

import com.google.common.reflect.TypeToken
import com.nhaarman.expect.expect
import com.nhaarman.expect.expectErrorWithMessage
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit


class SealedCallAdapterFactoryTest {

    lateinit var retrofit: Retrofit
    lateinit var factory: SealedCallAdapterFactory

    @Before
    fun setup() {
        retrofit = Retrofit.Builder().baseUrl("http://localhost/").build()
        factory = SealedCallAdapterFactory()
    }

    @Test
    fun nonSealedApiResultType_returnsNull() {
        /* When */
        val result = factory.get(String::class.java, emptyArray(), retrofit)

        /* Then */
        expect(result).toBeNull()
    }

    @Test
    fun nonParameterizedType_throws() {
        /* Expect */
        expectErrorWithMessage("return type must be parameterized as SealedApiResult<Foo> or SealedApiResult<out Foo>") on {
            /* When */
            factory.get(SealedApiResult::class.java, emptyArray(), retrofit)
        }
    }

    @Test
    fun properType_returnsProperCallAdapter() {
        /* Given */
        val type = object : TypeToken<SealedApiResult<String>>() {}.type

        /* When */
        val result = factory.get(type, emptyArray(), retrofit)

        /* Then */
        expect(result?.responseType()).toBe(String::class.java)
    }

    @Test
    fun properOutType_returnsProperCallAdapter() {
        /* Given */
        val type = object : TypeToken<SealedApiResult<out String>>() {}.type

        /* When */
        val result = factory.get(type, emptyArray(), retrofit)

        /* Then */
        expect(result?.responseType()).toBe(String::class.java)
    }

    @Test
    fun properArrayType_returnsProperCallAdapter() {
        /* Given */
        val type = object : TypeToken<SealedApiResult<Array<String>>>() {}.type

        /* When */
        val result = factory.get(type, emptyArray(), retrofit)

        /* Then */
        expect(result?.responseType()).toBe(Array<String>::class.java)
    }

    @Test
    fun properWildcardType_returnsProperCallAdapter() {
        /* Given */
        val type = object : TypeToken<SealedApiResult<*>>() {}.type

        /* When */
        val result = factory.get(type, emptyArray(), retrofit)

        /* Then */
        expect(result?.responseType()).toBe(Any::class.java)
    }
}
