# SealedApiResults
[ ![Download](https://api.bintray.com/packages/nhaarman/maven/SealedApiResults/images/download.svg) ](https://bintray.com/nhaarman/maven/SealedApiResults/_latestVersion)

SealedApiResults utilizes Kotlin's sealed classes to provide a safe way of handling http network calls.

## Example

```kotlin
fun onResult(result: SealedApiResult<String>) =
    when(result) {
      is Ok200 -> println(result.body)
      is Unauthorized401 -> println("Unauthorized")
      is Some -> println("Unhandled response: ${result.code}")
      is NetworkError -> result.e.printStackTrace()
    }
```

The compiler will complain when you forget to handle a case, such as the `NetworkError`.
This makes for a very safe way of dealing with network results.

## Install

SealedApiResults is available on JCenter.
For Gradle users, add the following to your `build.gradle`:

```groovy
repositories {
    jcenter()
}
dependencies {
    testCompile "com.nhaarman:sealedapiresults:x.x.x"
}
```

