package my.messenger.network

import my.messenger.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithHeader = chain.request().newBuilder()
            .addHeader("oauth", BuildConfig.OAUTH_CODE)
            .build()
        return chain.proceed(requestWithHeader)
    }
}
