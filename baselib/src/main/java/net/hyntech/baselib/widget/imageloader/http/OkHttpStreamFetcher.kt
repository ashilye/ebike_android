package net.hyntech.baselib.widget.imageloader.http

import android.os.Build
import android.util.Log
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.HttpException
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.util.ContentLengthInputStream
import com.bumptech.glide.util.Preconditions
import okhttp3.Call
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.io.InputStream

class OkHttpStreamFetcher(var client: Call.Factory, var url: GlideUrl) : DataFetcher<InputStream>,
    okhttp3.Callback {

    private val TAG = "OkHttpStreamFetcher"


    private var stream: InputStream? = null

    private lateinit var callback: DataFetcher.DataCallback<in InputStream>

    private var responseBody: ResponseBody? = null

    private lateinit var call: Call

    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun cleanup() {
        try {
            stream?.close()
            responseBody?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getDataSource(): DataSource {
        return DataSource.REMOTE
    }

    override fun cancel() {
        val local: Call = call
        local.cancel()
    }

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        this.callback = callback
        val requestBuilder: Request.Builder = Request.Builder().url(url.toStringUrl())
        url.headers.entries.forEach {
            requestBuilder.addHeader(it.key, it.value)
        }
        val request: Request = requestBuilder.build()
        call = client.newCall(request)
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            call.enqueue(this)
        } else {
            try {
                onResponse(call, call.execute())
            } catch (e: IOException) {
                onFailure(call, e)
                e.fillInStackTrace()
            } catch (e: ClassCastException) {
                onFailure(call, IOException("Workaround for framework bug on O", e))
                e.fillInStackTrace()
            }
        }
    }

    override fun onFailure(call: Call, e: IOException) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "OkHttp failed to obtain result", e);
        }
        callback.onLoadFailed(e)
    }

    override fun onResponse(call: Call, response: Response) {
        responseBody = response.body
        if (response.isSuccessful) {
            val contentLength: Long = Preconditions.checkNotNull(responseBody).contentLength()
            stream = responseBody?.byteStream()
                ?.let { ContentLengthInputStream.obtain(it, contentLength) }
            callback.onDataReady(stream)
        } else {
            callback.onLoadFailed(HttpException(response.message, response.code))
        }
    }
}