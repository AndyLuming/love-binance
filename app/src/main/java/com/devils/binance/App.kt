package com.devils.binance

import android.app.Application
import com.devils.binance.net.NetInvoker
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

/**
 * Created by andy on 2017/9/30.
 *
 */
class App : Application() {

    lateinit var gson: Gson
    lateinit var httpClient : OkHttpClient
    lateinit var retrofit: Retrofit

    override fun onCreate() {
        super.onCreate()
        initGson()
        initRetrofit()
    }

    private fun initGson() {
        gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
    }

    fun createHttpClient() : OkHttpClient {
        //创建一个不验证证书链的证书信任管理器。
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String) {
            }
        })

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts,
                java.security.SecureRandom())

        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory)
                .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logInterceptor)
                .build()
    }


    private fun initRetrofit(){
        httpClient = createHttpClient()

        retrofit = Retrofit.Builder()
                .baseUrl(NetInvoker.DOMAIN)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()

    }
}