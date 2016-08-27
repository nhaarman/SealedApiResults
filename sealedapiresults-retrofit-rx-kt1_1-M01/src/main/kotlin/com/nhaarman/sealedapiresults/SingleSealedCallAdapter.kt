package com.nhaarman.sealedapiresults

import retrofit2.Call
import retrofit2.CallAdapter
import rx.Single

/**
 * Can adapt [Call]s to [Single<SealedApiResult<*>>]s.
 */
internal class SingleSealedCallAdapter(
        private val callAdapter: ObservableSealedCallAdapter
) : CallAdapter<Single<out SealedApiResult<*>>> {

    override fun responseType() = callAdapter.responseType()
    override fun <R : Any?> adapt(call: Call<R>): Single<SealedApiResult<R>> {
        return callAdapter.adapt(call).toSingle()
    }
}
