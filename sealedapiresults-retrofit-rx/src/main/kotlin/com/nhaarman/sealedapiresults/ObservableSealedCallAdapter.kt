package com.nhaarman.sealedapiresults

import retrofit2.Call
import retrofit2.CallAdapter
import rx.*
import rx.exceptions.Exceptions
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean


/**
 * Can adapt [Call]s to [Observable<SealedApiResult<*>>]s.
 */
internal class ObservableSealedCallAdapter(
        private val responseType: Type,
        private val scheduler: Scheduler? = null
) : CallAdapter<Observable<out SealedApiResult<*>>> {

    override fun responseType() = responseType

    override fun <R : Any?> adapt(call: Call<R>): Observable<SealedApiResult<R>> {
        val observable: Observable<SealedApiResult<R>> = Observable.create(CallOnSubscribe(call))
        if (scheduler != null) {
            return observable.subscribeOn(scheduler)
        }
        return observable
    }
}

private class CallOnSubscribe<T>(private val originalCall: Call<T>) : Observable.OnSubscribe<SealedApiResult<T>> {

    override fun call(subscriber: Subscriber<in SealedApiResult<T>>) {
        // Since Call is a one-shot type, clone it for each new subscriber.
        val call = originalCall.clone()

        // Wrap the call in a helper which handles both unsubscription and backpressure.
        val requestArbiter = RequestArbiter(call, subscriber)
        subscriber.add(requestArbiter)
        subscriber.setProducer(requestArbiter)
    }
}

private class RequestArbiter<T>(
        private val call: Call<T>,
        private val subscriber: Subscriber<in SealedApiResult<T>>
) : AtomicBoolean(), Subscription, Producer {

    override fun request(n: Long) {
        if (n < 0) throw IllegalArgumentException("n < 0: " + n)
        if (n == 0L) return  // Nothing to do when requesting 0.
        if (!compareAndSet(false, true)) return  // Request was already triggered.

        try {
            val response = call.execute().toSealedApiResult()
            if (!subscriber.isUnsubscribed) {
                subscriber.onNext(response)
            }
        } catch(e: IOException) {
            if (!subscriber.isUnsubscribed) {
                subscriber.onNext(SealedApiResult.NetworkError(e))
            }
        } catch (t: Throwable) {
            Exceptions.throwIfFatal(t)
            if (!subscriber.isUnsubscribed) {
                subscriber.onError(t)
            }
            return
        }

        if (!subscriber.isUnsubscribed) {
            subscriber.onCompleted()
        }
    }

    override fun unsubscribe() {
        call.cancel()
    }

    override fun isUnsubscribed(): Boolean = call.isCanceled
}
