import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/shipCompanies/list',
    method: 'get',
    params: params
  })
}

export function getInfo(id) {
  return request({
    url: '/shipCompanies/' + id,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: `/shipCompanies/update/${data.id}`,
    method: 'post',
    data
  })
}

export function deleteBatch(params) {
  return request({
    url: `/shipCompanies/delete`,
    method: 'post',
    params
  })
}

export function create(data) {
  return request({
    url: `/shipCompanies/create`,
    method: 'post',
    data
  })
}
