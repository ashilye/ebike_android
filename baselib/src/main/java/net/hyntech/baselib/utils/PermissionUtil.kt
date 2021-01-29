package net.hyntech.baselib.utils

import android.Manifest
import android.annotation.SuppressLint
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Consumer

class PermissionUtil {

    companion object {

        @SuppressLint("CheckResult")
        private fun requestPermission(
            requestPermission: RequestPermission,
            rxPermissions: RxPermissions,
            vararg permissions: String
        ) {
            if (permissions.isNullOrEmpty()) {
                return
            }
            val needRequest: MutableList<String> = mutableListOf()
            //过滤调已经申请过的权限
            permissions.forEach {
                if (!rxPermissions.isGranted(it)) {
                    needRequest.add(it)
                }
            }
            if (needRequest.isEmpty()) {
                //全部权限都已经申请过，直接执行操作
                requestPermission.onRequestPermissionSuccess()
            } else {
                //没有申请过,则开始申请
                rxPermissions.requestEach(*needRequest.toTypedArray())
                    .buffer(needRequest.size)
                    .subscribe(object : Consumer<List<Permission>> {
                        override fun accept(t: List<Permission>?) {
                            val failurePermissions: MutableList<String> = mutableListOf()
                            val askNeverAgainPermissions: MutableList<String> = mutableListOf()
                            t?.forEach {
                                if (!it.granted) {
                                    if (it.shouldShowRequestPermissionRationale) {
                                        failurePermissions.add(it.name)
                                    } else {
                                        askNeverAgainPermissions.add(it.name)
                                    }
                                }
                            }

                            if (failurePermissions.size > 0) {
                                requestPermission.onRequestPermissionFailure(failurePermissions)
                            }

                            if (askNeverAgainPermissions.size > 0) {
                                requestPermission.onRequestPermissionFailureWithAskNeverAgain(
                                    askNeverAgainPermissions
                                )
                            }

                            if (failurePermissions.size == 0 && askNeverAgainPermissions.size == 0) {
                                requestPermission.onRequestPermissionSuccess()
                            }
                        }
                    })
            }
        }


        /**
         * 请求位置
         */
        fun applyLocation(requestPermission: RequestPermission, rxPermissions:RxPermissions ){
            requestPermission(requestPermission,
                rxPermissions,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE)
        }

        /**
         * 请求摄像头权限
         */
        fun applyCamera(requestPermission: RequestPermission, rxPermissions:RxPermissions ){
            requestPermission(requestPermission,
                rxPermissions,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)
        }

        /**
         * 请求外部存储的权限
         */
        fun applyExternalStorage(requestPermission: RequestPermission, rxPermissions:RxPermissions ){
            requestPermission(requestPermission,
                rxPermissions,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        /**
         * 请求发送短信权限
         */
        fun applySendSms(requestPermission: RequestPermission, rxPermissions:RxPermissions ){
            requestPermission(requestPermission,
                rxPermissions,
                Manifest.permission.SEND_SMS)
        }
        /**
         * 请求打电话权限
         */
        fun applyCallPhone(requestPermission: RequestPermission, rxPermissions:RxPermissions ){
            requestPermission(requestPermission,
                rxPermissions,
                Manifest.permission.CALL_PHONE)
        }

        /**
         * 请求获取手机状态的权限
         */
        fun applyPhonestate(requestPermission: RequestPermission, rxPermissions:RxPermissions ){
            requestPermission(requestPermission,
                rxPermissions,
                Manifest.permission.READ_PHONE_STATE)
        }

        fun applyPermissions(requestPermission: RequestPermission, rxPermissions:RxPermissions ){
            requestPermission(requestPermission,
                rxPermissions,
                Manifest.permission.ACCESS_FINE_LOCATION, // 访问位置
                Manifest.permission.CAMERA,     //相机
                Manifest.permission.CALL_PHONE,   //拨打电话
                Manifest.permission.READ_PHONE_STATE,   //手机状态
                Manifest.permission.ACCESS_COARSE_LOCATION ,//网络定位
                Manifest.permission.READ_EXTERNAL_STORAGE, //读取存储
                Manifest.permission.WRITE_EXTERNAL_STORAGE) //写入存储
        }
    }
}