package net.hyntech.common.utils

import android.text.TextUtils
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * 常用工具
 */
class CommonUtils {
    companion object {

        // 身份证隐藏
        fun coverIDCard(idCard: String?): String {
            if (TextUtils.isEmpty(idCard)) {
                return ""
            }
            if (idCard!!.length >= 16) {
                return idCard.substring(0, 6) + "********" + idCard.substring(
                    idCard.length - 4,
                    idCard.length
                )
            } else {
                return ""
            }
        }

        // 手机号隐藏
        fun coverPhone(phone: String?): String {
            if (TextUtils.isEmpty(phone)) {
                return ""
            }
            if (phone!!.length >= 11) {
                return phone.substring(0, 3) + "****" + phone.substring(
                    phone.length - 4,
                    phone.length
                )
            } else {
                return ""
            }
        }

        /**
         * list 拼接字符（最后一位不拼接）
         * @param list 集合
         * @param c 拼接字符
         */
        private fun listSpliceChar(list: List<String>, c: String): String {
            val sb = StringBuffer()
            if (list.isNotEmpty()) {
                list.forEachWithIndex { i, s ->
                    if (i < list.size - 1) {
                        sb.append(list.get(i) + c)
                    } else {
                        sb.append(list.get(i))
                    }
                }
            }
            return sb.toString()
        }

        //list 拼接逗号
        fun listSpliceComma(list: List<String>): String {
            return listSpliceChar(list, ",")
        }


        /**
         * 分割字符串中包含逗号的字符
         * @param url 要分割的字符串
         * @return 结果
         */
        fun splitPicList(url: String?): List<String> {
            val list: MutableList<String> = mutableListOf()
            if (!TextUtils.isEmpty(url)) {
                if (url?.contains(",") ?: false) {
                    list.addAll(url!!.split(","))
                } else {
                    list.add(url!!)
                }
            }
            return list
        }
    }
}