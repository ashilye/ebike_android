package net.hyntech.common.utils

import android.text.TextUtils

class CommonUtils {
    companion object{

        // 身份证隐藏
        fun coverIDCard(idCard:String?):String{
            if(TextUtils.isEmpty(idCard)){
                return ""
            }
            if(idCard!!.length >= 16){
                return idCard.substring(0,6) + "********" + idCard.substring(idCard.length - 4,idCard.length)
            }else{
                return ""
            }
        }

        // 手机号隐藏
        fun coverPhone(phone:String?):String{
            if(TextUtils.isEmpty(phone)){
                return ""
            }
            if(phone!!.length >= 11){
                return phone.substring(0,3) + "****" + phone.substring(phone.length - 4,phone.length)
            }else{
                return ""
            }
        }


    }
}