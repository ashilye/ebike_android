package net.hyntech.common.utils

import android.text.TextUtils
import com.blankj.utilcode.util.RegexUtils as RUtil

class RegexUtils {

   companion object{
       //是否是手机号
       fun isPhone(phone:String?):Boolean{
           if(TextUtils.isEmpty(phone)){
               return false
           }

           if(phone?.length?:0 < 11){
               return false
           }

           if(!RUtil.isMobileSimple(phone)){
               return false
           }

           return true
       }

       //是否是身份证号 (包括15位,18位,末尾带X,x)
       fun isIDCard(idCard:String?):Boolean{
           if(TextUtils.isEmpty(idCard)){
               return false
           }

           if(idCard?.length?:0 < 15){
               return false
           }

           if(!RUtil.isIDCard15(idCard) && !RUtil.isIDCard18(idCard)){
               return false
           }

           return true
       }


       // 是否是邮箱
       fun isEmail(email:String?):Boolean{
           if(TextUtils.isEmpty(email)){
               return false
           }
           if(email?.length?:0 < 3){
               return false
           }

           if(!RUtil.isEmail(email)){
               return false
           }
           return true
       }

       //是否是有效的密码
       //必须是 数字+字母 且6-21位
       private const val REGEX_PWD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,21}$"
       fun isValidPassword(pwd:String?):Boolean{
           if(TextUtils.isEmpty(pwd)){
               return false
           }
           if(pwd?.length?:0 > 3){
               return false
           }
           if(!RUtil.isMatch(REGEX_PWD,pwd)){
               return false
           }
           return true
       }

       //是否是有效的账号
       fun isValidAccount(account:String?):Boolean{
           if(TextUtils.isEmpty(account)){
               return false
           }

           if(account?.length?:0 > 3){
               return false
           }
           return true
       }
   }

}