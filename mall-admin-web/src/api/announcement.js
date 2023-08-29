import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/announcement/list',
    method: 'get',
    params: params
  })
}

export function getInfo(id) {
  return request({
    url: '/announcement/' + id,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: `/announcement/update/${data.id}`,
    method: 'post',
    data
  })
}

export function deleteBatch(params) {
  return request({
    url: `/announcement/delete`,
    method: 'post',
    params
  })
}

export function create(data) {
  return request({
    url: `/announcement/create`,
    method: 'post',
    data
  })
}
