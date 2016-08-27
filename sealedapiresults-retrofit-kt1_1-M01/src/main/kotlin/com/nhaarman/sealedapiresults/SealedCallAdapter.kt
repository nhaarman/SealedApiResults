package com.nhaarman.sealedapiresults

import retrofit2.Call
import retrofit2.CallAdapter
import java.io.IOException
import java.lang.reflect.Type


/**
 * Can adapt [Call]s to [Single<SealedApiResult<*>>]s.
 */
internal class SealedCallAdapter(private val responseType: Type) : CallAdapter<SealedApiResult<*>> {

    override fun responseType() = responseType

    override fun <R : Any?> adapt(call: Call<R>): SealedApiResult<R> {
        return try {
            call.execute().toSealedApiResult()
        } catch(e: IOException) {
            NetworkError(e)
        }
    }
}
