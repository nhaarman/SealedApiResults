package com.nhaarman.sealedapiresults

import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory
import retrofit2.Retrofit
import rx.Observable
import rx.Scheduler
import rx.Single
import java.lang.reflect.*


class RxSealedCallAdapterFactory(private val defaultScheduler: Scheduler? = null) : Factory() {

    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*>? {
        val rawType = returnType.rawType
        if (rawType != Single::class.java && rawType != Observable::class.java) {
            return null
        }

        val type = if (rawType == Single::class.java) "Single" else "Observable"
        if (returnType !is ParameterizedType) {
            error("$type return type must be parameterized as $type<SealedApiResult<Foo>> or $type<SealedApiResult<out Foo>>")
        }

        val observableType = returnType.getParameterUpperBound(0)
        val rawObservableType = observableType.rawType
        if (rawObservableType != SealedApiResult::class.java || observableType !is ParameterizedType) {
            error("$type return type must be parameterized as $type<SealedApiResult<Foo>> or $type<SealedApiResult<out Foo>>")
        }

        val adapter = ObservableSealedCallAdapter(observableType.getParameterUpperBound(0), defaultScheduler)
        if (rawType == Single::class.java) {
            return SingleSealedCallAdapter(adapter)
        }
        return adapter
    }
}


private fun ParameterizedType.getParameterUpperBound(index: Int): Type {
    if (index < 0 || index >= actualTypeArguments.size) {
        throw IllegalArgumentException(
              "Index $index not in range [0,${actualTypeArguments.size}) for $this")
    }
    val paramType = actualTypeArguments[index]
    if (paramType is WildcardType) {
        return paramType.upperBounds[0]
    }
    return paramType
}

private val Type.rawType: Class<*> get() {
    if (this is Class<*>) {
        // Type is a normal class.
        return this
    }
    if (this is ParameterizedType) {

        // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
        // suspects some pathological case related to nested classes exists.
        val rawType = this.getRawType()
        if (rawType !is Class<*>) throw IllegalArgumentException()
        return rawType
    }
    if (this is GenericArrayType) {
        val componentType = this.genericComponentType
        return java.lang.reflect.Array.newInstance(componentType.rawType, 0).javaClass
    }
    if (this is TypeVariable<*>) {
        // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
        // this that's more general than necessary is okay.
        return Any::class.java
    }
    if (this is WildcardType) {
        return this.upperBounds[0].rawType
    }

    throw IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <$this> is of type ${this.javaClass.name}")
}
