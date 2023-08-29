import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/article/list',
    method: 'get',
    params: params
  })
}

export function getInfo(id) {
  return request({
    url: '/article/' + id,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: `/article/update/${data.id}`,
    method: 'post',
    data
  })
}

export function deleteBatch(params) {
  return request({
    url: `/article/delete`,
    method: 'post',
    params
  })
}

export function create(data) {
  return request({
    url: `/article/create`,
    method: 'post',
    data
  })
}
