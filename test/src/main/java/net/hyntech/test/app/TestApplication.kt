package net.hyntech.test.app

import androidx.lifecycle.ViewModelStore
import net.hyntech.common.app.CommonApp

class TestApplication: CommonApp() {

    companion object {
        @JvmStatic
        lateinit var instance: TestApplication
            private set
    }

    override fun initApp() {
        super.initApp()
        instance = this
    }

    override fun getAppPackage(): String = this.packageName


    override fun getViewModelStore(): ViewModelStore = ViewModelStore()
}