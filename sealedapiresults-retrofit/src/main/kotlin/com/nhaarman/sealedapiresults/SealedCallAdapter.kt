package com.nhaarman.sealedapiresults

import retrofit2.Call
import retrofit2.CallAdapter
import java.io.IOException
import java.lang.reflect.Type


/**
 * Can adapt [Call]s to [Single<SealedApiResult<*>>]s.
 */
internal class SealedCallAdapter<R>(private val responseType: Type) : CallAdapter<R, SealedApiResult<*>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): SealedApiResult<R> {
        return try {
            call.execute().toSealedApiResult()
        } catch(e: IOException) {
            SealedApiResult.NetworkError(e)
        }
    }
}
