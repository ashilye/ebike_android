package net.hyntech.baselib.utils

interface RequestPermission {
    /**
     * 权限请求成功
     */
    fun onRequestPermissionSuccess()
    /**
     * 用户拒绝了权限请求, 权限请求失败, 但还可以继续请求该权限
     *
     * @param permissions 请求失败的权限名
     */
    fun onRequestPermissionFailure(permissions:List<String>)
    /**
     * 用户拒绝了权限请求并且用户选择了以后不再询问, 权限请求失败, 这时将不能继续请求该权限, 需要提示用户进入设置页面打开该权限
     *
     * @param permissions 请求失败的权限名
     */
    fun onRequestPermissionFailureWithAskNeverAgain(permissions:List<String>)
}