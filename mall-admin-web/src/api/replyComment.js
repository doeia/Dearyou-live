import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/commentReplay/list',
    method: 'get',
    params: params
  })
}

export function getInfo(id) {
  return request({
    url: '/commentReplay/' + id,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: `/commentReplay/update/${data.id}`,
    method: 'post',
    data
  })
}

export function deleteBatch(params) {
  return request({
    url: `/commentReplay/delete`,
    method: 'post',
    params
  })
}

export function create(data) {
  return request({
    url: `/commentReplay/create`,
    method: 'post',
    data
  })
}
