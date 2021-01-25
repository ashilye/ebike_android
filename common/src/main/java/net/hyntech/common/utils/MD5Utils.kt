package net.hyntech.common.utils

import android.text.TextUtils
import net.hyntech.common.app.global.Global
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils


class MD5Utils {
    companion object {
        fun sigMD5Hex(
            accessToken: String,
            nonce: String,
            timestamp: String,
            needToken: Boolean
        ): String {
            val content: String
            var contentMD5: String = ""
            if (needToken) {
                content = accessToken + nonce + timestamp + Global.HMAC
            } else {
                content = nonce + timestamp + Global.HMAC
            }

            if (!TextUtils.isEmpty(content)) {
                contentMD5 = String(Hex.encodeHex(DigestUtils.md5(content)))
            }
            return contentMD5
        }

        fun md5Hex(content:String):String{
            var contentMD5:String = ""
            if(!TextUtils.isEmpty(content)){
                contentMD5 = String(Hex.encodeHex(DigestUtils.md5(content)))
            }
            return contentMD5
        }
    }
}