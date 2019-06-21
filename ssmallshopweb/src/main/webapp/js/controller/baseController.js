app.controller('baseController', function ($scope) {
    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1, //当前页
        totalItems: 10, //总记录数
        itemsPerPage: 10, //每页的记录数
        perPageOptions: [10, 20, 30, 40, 50], //分页选项
        onChange: function () {//当页码变更后自动触发的方法
            $scope.reloadList();
        }
    };

    //不需要每次都写findPage方法，只需要调用reloadList方法即可
    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }

    $scope.selectIds = []; //用户勾选的ID集合

    //用户勾选批量删除复选框
    $scope.updateSelection = function ($event, id) { //将id存入selectIds集合中
        /*event判断是点击还是取消事件*/
        if ($event.target.checked) {
            $scope.selectIds.push(id); //push向集合添加元素
        } else {
            var index = $scope.selectIds.indexOf(id); //查找值得位置
            $scope.selectIds.splice(index, 1); //参数1：移除的位置  参数2：移除的个数
        }
    }

    $scope.jsonToString = function (jsonString, key) {
        var json = JSON.parse(jsonString);
        var value = "";

        for (var i = 0; i < json.length; i++) {
            if (i > 0) {
                value += ",";
            }
            value += json[i][key];
        }
        return value;
    }

    //在List集合中根据某key的值查询对象
    $scope.searchObjectByKey = function (list, key, keyValue) {
        for (var i = 0; i < list.length; i++) {
            if (list[i][key] == keyValue) {
                return list[i]; //选择的选项，规格名称已经存在
            }
        }
        return null;
    }
});