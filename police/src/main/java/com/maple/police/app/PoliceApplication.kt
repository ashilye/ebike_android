package com.maple.police.app

import android.app.Application
import androidx.lifecycle.ViewModelStore
import net.hyntech.common.app.CommonApp

class PoliceApplication:CommonApp() {

    override fun getAppPackage(): String = this.packageName


    override fun getViewModelStore(): ViewModelStore = ViewModelStore()

    override fun initSDK(app: Application) {

    }
}