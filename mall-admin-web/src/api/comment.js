import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/comment/list',
    method: 'get',
    params: params
  })
}

export function getInfo(id) {
  return request({
    url: '/comment/' + id,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: `/comment/update/${data.id}`,
    method: 'post',
    data
  })
}

export function deleteBatch(params) {
  return request({
    url: `/comment/delete`,
    method: 'post',
    params
  })
}

export function create(data) {
  return request({
    url: `/comment/create`,
    method: 'post',
    data
  })
}

export function show(id) {
  return request({
    url: `/comment/show/` + id,
    method: 'post'
  })
}

export function hide(id) {
  return request({
    url: `/comment/hide/` + id,
    method: 'post'
  })
}
