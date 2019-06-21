var app = angular.module('ssmall', []);//商城后台模块
//定义过滤器
app.filter('trustHtml',['$sce',function ($sce) {
    return function (data) {//data：传入的参数是被过滤的内容
        return $sce.trustAsHtml(data);//返回过滤后的内容（信任html的转换）
    }
}]);