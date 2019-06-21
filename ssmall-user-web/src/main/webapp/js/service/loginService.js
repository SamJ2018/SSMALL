app.service('loginService', function ($http) {
    //读取列表数据绑定到表中
    this.showName = function () {
        return $http.get('../login/name.do');
    }
});