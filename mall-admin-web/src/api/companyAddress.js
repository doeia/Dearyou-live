import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/companyAddress/list',
    method: 'get',
    params: params
  })
}

export function getInfo(id) {
  return request({
    url: '/companyAddress/' + id,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: `/companyAddress/update/${data.id}`,
    method: 'post',
    data
  })
}

export function deleteBatch(params) {
  return request({
    url: `/companyAddress/delete`,
    method: 'post',
    params
  })
}

export function create(data) {
  return request({
    url: `/companyAddress/create`,
    method: 'post',
    data
  })
}
