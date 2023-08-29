import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/brandCategory/list',
    method: 'get',
    params: params
  })
}

export function fetchListAll() {
  return request({
    url: '/brandCategory/listAll',
    method: 'get'
  })
}

export function getInfo(id) {
  return request({
    url: '/brandCategory/' + id,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: `/brandCategory/update/${data.id}`,
    method: 'post',
    data
  })
}

export function deleteBatch(params) {
  return request({
    url: `/brandCategory/delete`,
    method: 'post',
    params
  })
}

export function create(data) {
  return request({
    url: `/brandCategory/create`,
    method: 'post',
    data
  })
}
