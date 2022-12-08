function getAllBlogApi() {
    return new $axios({
        'url': '/blog/getAll',
        'method':'get'
    })
}


function getBlogByIdApi(id) {
    return new $axios({
        'url': '/blog/getById/'+id,
        'method':'get'
    })
}