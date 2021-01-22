package net.hyntech.baselib.widget.imageloader.http

import net.hyntech.baselib.widget.imageloader.listener.ProgressListener
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

class ProgressResponseBody(val url: String?, val responseBody: ResponseBody?) : ResponseBody() {


    private lateinit var bufferedSource: BufferedSource
    private var listener: ProgressListener? = null

    init {
        listener = ProgressInterceptor.LISTENER_MAP.get(url)
        listener?.onLoadStart()
    }

    override fun contentLength(): Long {
        return if (responseBody != null) responseBody.contentLength() else 0L
    }

    override fun contentType(): MediaType? {
        return responseBody?.contentType()
    }

    override fun source(): BufferedSource {
        bufferedSource = ProgressSource(responseBody!!.source()).buffer();
        return bufferedSource
    }

    inner class ProgressSource(delegate: Source) : ForwardingSource(delegate) {

        var totalBytesRead: Long = 0L
        var currentProgress: Int = 0


        override fun read(sink: Buffer, byteCount: Long): Long {
//            return super.read(sink, byteCount)
            val bytesRead: Long = super.read(sink, byteCount)
            val fullLength: Long = responseBody!!.contentLength()
            if (bytesRead == -1L) {
                totalBytesRead = fullLength
            } else {
                totalBytesRead += bytesRead
            }

            val progress: Int = (100 * totalBytesRead / fullLength).toInt()
            if (listener != null && progress != currentProgress) {
                listener!!.onLoadProgress(totalBytesRead == fullLength, progress)
            }
            if (listener != null && totalBytesRead == fullLength) {
                listener = null
            }
            currentProgress = progress
            return bytesRead
        }
    }
}