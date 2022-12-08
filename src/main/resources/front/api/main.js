//获取所有的分类信息
function categoryListApi() {
    return $axios({
      'url': '/type/list',
      'method': 'get',
    })
  }

//获取宠物分类对应的数据
function dishListApi(data) {
    return $axios({
        'url': '/pet/list',
        'method': 'get',
        params:{...data}
    })
}

// 获取物品分类对应的数据
function goodsListApi(data) {
    return $axios({
        'url':'/good/list',
        'method': 'get',
        params:{...data}
    })
}

//获取套餐分类对应的套餐
function setmealListApi(data) {
    return $axios({
        'url': '/setmeal/list',
        'method': 'get',
        params:{...data}
    })
}

//获取购物车内商品的集合
function cartListApi(data) {
    return $axios({
        'url': '/shoppingCart/list',
        //'url': '/front/category.json',
        'method': 'get',
        params:{...data}
    })
}

//购物车中添加商品
function  addCartApi(data){
    return $axios({
        'url': '/shoppingCart/add',
        'method': 'post',
        data
      })
}

//购物车中修改商品
function  updateCartApi(data){
    return $axios({
        'url': '/shoppingCart/sub',
        'method': 'post',
        data
      })
}

//删除购物车的商品
function clearCartApi() {
    return $axios({
        'url': '/shoppingCart/clean',
        'method': 'delete',
    })
}

//获取套餐的全部信息
function setMealDishDetailsApi(id) {
    return $axios({
        'url': `/setmeal/goods/${id}`,
        'method': 'get',
    })
}


