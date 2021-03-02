package net.hyntech.common.widget.map.ebike


class BiaduUtils {
    companion object {
        fun getDirectionByAngle(d:Int): String{
            return if (d in 337..360 || d in 0..23) {
                "北向"
            } else if (d in 23..68) {
                "东北向"
            } else if (d in 68..113) {
                "东向"
            } else if (d in 113..158) {
                "东南向"
            } else if (d in 158..203) {
                "南向"
            } else if (d in 203..248) {
                "西南向"
            } else if (d in 248..293) {
                "西向"
            } else  //if (d >= 293 && d <= 338) {
                "西北向"
        }
    }
}