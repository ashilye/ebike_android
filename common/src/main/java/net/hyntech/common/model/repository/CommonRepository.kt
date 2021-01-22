package net.hyntech.common.model.repository

import net.hyntech.baselib.base.BaseRepository
import net.hyntech.common.model.RetrofitClient

class CommonRepository : BaseRepository() {

    private val retrofitClient = RetrofitClient.service


}