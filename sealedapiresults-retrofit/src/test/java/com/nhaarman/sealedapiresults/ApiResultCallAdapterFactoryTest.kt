package com.nhaarman.sealedapiresults

import com.google.common.reflect.TypeToken
import com.nhaarman.expect.expect
import com.nhaarman.expect.expectErrorWithMessage
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit


class ApiResultCallAdapterFactoryTest {

    lateinit var retrofit: Retrofit
    lateinit var factory: ApiResultCallAdapterFactory

    @Before
    fun setup() {
        retrofit = Retrofit.Builder().baseUrl("http://localhost/").build()
        factory = ApiResultCallAdapterFactory()
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
        expectErrorWithMessage("return type must be parameterized as ApiResult<Foo> or ApiResult<out Foo>") on {
            /* When */
            factory.get(ApiResult::class.java, emptyArray(), retrofit)
        }
    }

    @Test
    fun properType_returnsProperCallAdapter() {
        /* Given */
        val type = object : TypeToken<ApiResult<String>>() {}.type

        /* When */
        val result = factory.get(type, emptyArray(), retrofit)

        /* Then */
        expect(result?.responseType()).toBe(String::class.java)
    }

    @Suppress("REDUNDANT_PROJECTION")
    @Test
    fun properOutType_returnsProperCallAdapter() {
        /* Given */
        val type = object : TypeToken<ApiResult<out String>>() {}.type

        /* When */
        val result = factory.get(type, emptyArray(), retrofit)

        /* Then */
        expect(result?.responseType()).toBe(String::class.java)
    }

    @Test
    fun properArrayType_returnsProperCallAdapter() {
        /* Given */
        val type = object : TypeToken<ApiResult<Array<String>>>() {}.type

        /* When */
        val result = factory.get(type, emptyArray(), retrofit)

        /* Then */
        expect(result?.responseType()).toBe(Array<String>::class.java)
    }

    @Test
    fun properWildcardType_returnsProperCallAdapter() {
        /* Given */
        val type = object : TypeToken<ApiResult<*>>() {}.type

        /* When */
        val result = factory.get(type, emptyArray(), retrofit)

        /* Then */
        expect(result?.responseType()).toBe(Any::class.java)
    }
}
