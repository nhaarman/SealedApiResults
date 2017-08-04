package com.nhaarman.sealedapiresults

import retrofit2.Call
import retrofit2.CallAdapter
import java.io.IOException
import java.lang.reflect.Type


/**
 * Can adapt [Call]s to [ApiResult]s.
 */
internal class ApiResultCallAdapter<R>(private val responseType: Type) : CallAdapter<R, ApiResult<*>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): ApiResult<R> {
        return try {
            call.execute().toApiResult()
        } catch (e: IOException) {
            ApiResult.NetworkError(e)
        }
    }
}
