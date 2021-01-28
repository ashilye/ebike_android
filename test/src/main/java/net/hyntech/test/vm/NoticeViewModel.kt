package net.hyntech.test.vm

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.zy.multistatepage.MultiState
import com.zy.multistatepage.MultiStateConfig
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.MultiStatePage
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.common.model.entity.NoticeListEntity
import net.hyntech.common.model.repository.CommonRepository
import java.util.*

class NoticeViewModel:BaseViewModel() {

    private val repository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { CommonRepository() }


    private var pageNo: Int = 1
    private var pageSize: Int = 10
    var lastPage: Boolean = true

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
            params.put("PrmPageNo",pageNo)
            params.put("PrmItemsPerPage",pageSize)
            repository.getNoticeList(params)
        }, success = {
            it?.let { data ->
                lastPage = data.page?.isLastPage?:true
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
                        2 ->{
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
        pageNo = 1
        lastPage = true
        launchOnlyResult({
            val params: WeakHashMap<String, Any> = WeakHashMap()
            if(!TextUtils.isEmpty(keyword)){
                params.put("keyword",keyword)
            }
            params.put("PrmPageNo",pageNo)
            params.put("PrmItemsPerPage",pageSize)
            repository.getNoticeList(params)
        }, success = {
            it?.let { data ->
                lastPage = data.page?.isLastPage?:true
                noticeListRefresh.postValue(data.alarmInfoList)
            }
        },isShowDialog = false,isShowToast = false)
    }

    fun getNoticeListLoadMore(keyword: String) {
        pageNo +=1
        launchOnlyResult({
            val params: WeakHashMap<String, Any> = WeakHashMap()
            if(!TextUtils.isEmpty(keyword)){
                params.put("keyword",keyword)
            }
            params.put("PrmPageNo",pageNo)
            params.put("PrmItemsPerPage",pageSize)
            repository.getNoticeList(params)
        }, success = {
            it?.let { data ->
                lastPage = data.page?.isLastPage?:true
                noticeListLoadMore.postValue(data.alarmInfoList)
            }
        },isShowDialog = false,isShowToast = false)





    }
}