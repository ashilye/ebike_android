package net.hyntech.common.utils

import android.text.TextUtils

class RegexUtils {
    //是否是手机号
    fun isPhone(phone:String?):Boolean{
        if(TextUtils.isEmpty(phone)){
            return false
        }
        return true
    }

    //是否是身份证号 (包括15位,18位,末尾带X,x)
    fun isIDCard(idCard:String?):Boolean{
        if(TextUtils.isEmpty(idCard)){
            return false
        }
        return true
    }

    //是否是有效的密码
    fun isValidPassword(pwd:String?):Boolean{
        if(TextUtils.isEmpty(pwd)){
            return false
        }
        return true
    }

    //是否是有效的账号
    fun isValidAccount(account:String?):Boolean{
        if(TextUtils.isEmpty(account)){
            return false
        }
        return true
    }



}