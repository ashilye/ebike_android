package net.hyntech.test.vm

import android.graphics.drawable.Drawable
import android.text.TextUtils
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils
import net.hyntech.baselib.app.BaseApp
import net.hyntech.baselib.app.manager.SingleLiveEvent
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.common.app.global.Constants
import net.hyntech.common.model.entity.NoticeListEntity
import net.hyntech.common.model.repository.CommonRepository
import net.hyntech.test.R
import java.util.*

class AccountViewModel : BaseViewModel() {

    private val repository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { CommonRepository() }


    val bgDrawable: ObservableField<Drawable> = ObservableField()
    val verName: ObservableField<String> = ObservableField("")

    val account: ObservableField<String> = ObservableField("18537385619")
    val password: ObservableField<String> = ObservableField("000000")


    val loginEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val companyEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val forgetPwdEvent: SingleLiveEvent<Any> = SingleLiveEvent()

    init {
        bgDrawable.set(UIUtils.getDrawable(R.drawable.pic_test))
        verName.set("当前版本：${AppUtils.getAppVersionName()}")
    }

    fun onChooseCompany() {
        companyEvent.call()
    }


    fun onForgetPassword() {
        forgetPwdEvent.call()
    }

    fun onLogin() {
        onClickProxy {

            val phone = account.get()
            val pwd = password.get()
            if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd)) {
                defUI.toastEvent.postValue("请输入账号和密码")
                return@onClickProxy
            }
            login(phone!!,pwd!!)
        }
    }


    private fun login(phone:String,pwd:String){
        launchOnlyResult({
            val params: WeakHashMap<String, Any> = WeakHashMap()

            //民用
//            params.put("phone","15664284736")
//            params.put("pwd","136512")

            //警用
//            params.put("phone","18537385619")
//            params.put("pwd","000000")

            params.put("phone",phone)
            params.put("pwd",pwd)

            repository.loginPhone(params)
        }, success = {
            it?.let {data ->
                SPUtils.getInstance(BaseApp.instance.getAppPackage()).put(Constants.SaveInfoKey.ACCESS_TOKEN,data.accessToken)
                LogUtils.logGGQ("-->${data.userId}")
                loginEvent.call()
            }
        })
    }



    val noticeList: MutableLiveData<List<NoticeListEntity.AlarmInfoListBean>> = MutableLiveData()
    val noticeListRefresh: MutableLiveData<List<NoticeListEntity.AlarmInfoListBean>> = MutableLiveData()
    val noticeListLoadMore: MutableLiveData<List<NoticeListEntity.AlarmInfoListBean>> = MutableLiveData()


    fun getNoticeList(keyword: String) {
        defUI.showUILoading()
        launchOnlyResult({
            val params: WeakHashMap<String, Any> = WeakHashMap()
            if(!TextUtils.isEmpty(keyword)){
                params.put("keyword",keyword)
            }
            params.put("PrmPageNo",getPageNo())
            params.put("PrmItemsPerPage",getPageSize())
            repository.getNoticeList(params)
        }, success = {
            it?.let { data ->
                this.setIsLastPage(data.page?.isLastPage?:true)
                if(data.alarmInfoList.isNullOrEmpty()){
                    //数据空
                    defUI.showUIEmpty()
                }else{
                    // 数据正常
                    LogUtils.logGGQ("---获取数据-->>${data.alarmInfoList.size}")

                    val type = (0..2).random()
                    when(type){
                        0 ->{
                            defUI.showUIContent()
                            noticeList.postValue(data.alarmInfoList)
                        }
                        1 ->{
                            defUI.showUIEmpty()
                        }
                        else ->{
                            defUI.showUIError()
                        }
                    }

                }
            }?:let {
                //暂无空
                defUI.showUIEmpty()
            }
        },error = {
            //数据异常
            defUI.showUIError()
        },isShowDialog = false,isShowToast = false)
    }

    fun getNoticeListRefresh(keyword: String) {
       this.resetPage()
        launchOnlyResult({
            val params: WeakHashMap<String, Any> = WeakHashMap()
            if(!TextUtils.isEmpty(keyword)){
                params.put("keyword",keyword)
            }
            params.put("PrmPageNo",getPageNo())
            params.put("PrmItemsPerPage",getPageSize())
            repository.getNoticeList(params)
        }, success = {
            it?.let { data ->
                this.setIsLastPage(data.page?.isLastPage?:true)
                noticeListRefresh.postValue(data.alarmInfoList)
            }
        },isShowDialog = false,isShowToast = false)
    }

    fun getNoticeListLoadMore(keyword: String) {
        this.incrementPageNo()
        launchOnlyResult({
            val params: WeakHashMap<String, Any> = WeakHashMap()
            if(!TextUtils.isEmpty(keyword)){
                params.put("keyword",keyword)
            }
            params.put("PrmPageNo",getPageNo())
            params.put("PrmItemsPerPage",getPageSize())
            repository.getNoticeList(params)
        }, success = {
            it?.let { data ->
                this.setIsLastPage(data.page?.isLastPage?:true)
                noticeListLoadMore.postValue(data.alarmInfoList)
            }
        },isShowDialog = false,isShowToast = false)

    }

}