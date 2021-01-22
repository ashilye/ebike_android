package net.hyntech.usual.app

import android.app.Application
import androidx.lifecycle.ViewModelStore
import net.hyntech.common.app.CommonApp

class UsualApplication : CommonApp() {
    override fun getAppPackage(): String = this.packageName

    override fun getViewModelStore(): ViewModelStore = ViewModelStore()

    override fun initSDK(app: Application) {

    }
}