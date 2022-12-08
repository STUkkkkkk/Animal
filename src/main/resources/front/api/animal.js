function getDogByIdApi(id){
    return $axios({
        'url': `/animal/getDogById/${id}`,
        'method': 'get',
    })
}

function getCatByIdApi(id){
    return $axios({
        'url': `/animal/getCatById/${id}`,
        'method': 'get',
    })
}

function getAllDog(){
    return $axios({
        'url': '/animal/getAllDog',
        'method': 'get',
    })
}

function getAllCat(){
    return $axios({
        'url': '/animal/getAllCat',
        'method': 'get',
    })
}