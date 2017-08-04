package com.nhaarman.sealedapiresults

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.nhaarman.sealedapiresults.ApiResult.NetworkError
import io.reactivex.Scheduler
import io.reactivex.Single
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class RxApiResultCallAdapterFactory(private val defaultScheduler: Scheduler? = null) : Factory() {

    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val newType = WrappedType(returnType)
        return RxApiResultCallAdapter(annotations, newType, retrofit, defaultScheduler)
    }
}

private class WrappedType(private val returnType: Type) : ParameterizedType {

    override fun getRawType(): Type {
        return (returnType as ParameterizedType).rawType
    }

    override fun getOwnerType(): Type {
        return (returnType as ParameterizedType).ownerType
    }

    override fun getActualTypeArguments(): Array<Type> {
        return arrayOf(object : ParameterizedType {
            override fun getRawType(): Type {
                return Response::class.java
            }

            override fun getOwnerType(): Type? {
                return null
            }

            override fun getActualTypeArguments(): Array<Type> {
                return ((returnType as ParameterizedType).actualTypeArguments[0] as ParameterizedType).actualTypeArguments
            }
        })

    }
}

private class RxApiResultCallAdapter(
      annotations: Array<out Annotation>,
      newType: WrappedType,
      retrofit: Retrofit,
      defaultScheduler: Scheduler?
) : CallAdapter<Any, Any> {

    @Suppress("UNCHECKED_CAST")
    private val delegate =
          RxJava2CallAdapterFactory.createWithScheduler(defaultScheduler).get(
                newType,
                annotations,
                retrofit
          ) as CallAdapter<Any, Single<Response<*>>>

    override fun adapt(call: Call<Any>): Any {
        val adapt = delegate.adapt(call)
        return adapt
              .map { it.toApiResult() as ApiResult<Any> }
              .onErrorResumeNext { t ->
                  if (t is IOException) Single.just(NetworkError(t))
                  else Single.error(t)
              }
    }

    override fun responseType(): Type {
        return delegate.responseType()
    }
}