package com.nhaarman.sealedapiresults

import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory
import retrofit2.Retrofit
import java.lang.reflect.*


class ApiResultCallAdapterFactory : Factory() {

    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val rawType = returnType.rawType
        if (rawType != ApiResult::class.java) {
            return null
        }

        if (returnType !is ParameterizedType) {
            error("ApiResult return type must be parameterized as ApiResult<Foo> or ApiResult<out Foo>")
        }

        return ApiResultCallAdapter<Any>(returnType.getParameterUpperBound(0))
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

private val Type.rawType: Class<*>
    get() {
        return when (this) {
            is Class<*> -> {
                // Type is a normal class.
                this
            }
            is ParameterizedType -> {
                // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
                // suspects some pathological case related to nested classes exists.
                this.getRawType() as? Class<*> ?: throw IllegalArgumentException()
            }
            is GenericArrayType -> {
                val componentType = this.genericComponentType
                java.lang.reflect.Array.newInstance(componentType.rawType, 0).javaClass
            }
            is TypeVariable<*> -> {
                // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
                // this that's more general than necessary is okay.
                Any::class.java
            }
            is WildcardType -> {
                this.upperBounds[0].rawType
            }
            else -> error("Expected a Class, ParameterizedType, or GenericArrayType, but <$this> is of type ${this.javaClass.name}")
        }
    }
