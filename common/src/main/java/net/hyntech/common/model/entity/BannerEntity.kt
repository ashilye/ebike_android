package net.hyntech.common.model.entity
//type 0 url, 1 resId
data class BannerEntity(val name:String,val url:String,val resId:Int,val type:Int = 0,val isBanner:Boolean = true)