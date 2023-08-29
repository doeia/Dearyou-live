import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/feightTemplate/list',
    method: 'get',
    params: params
  })
}

export function getInfo(id) {
  return request({
    url: '/feightTemplate/' + id,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: `/feightTemplate/update/${data.id}`,
    method: 'post',
    data
  })
}

export function deleteBatch(params) {
  return request({
    url: `/feightTemplate/delete`,
    method: 'post',
    params
  })
}

export function create(data) {
  return request({
    url: `/feightTemplate/create`,
    method: 'post',
    data
  })
}
