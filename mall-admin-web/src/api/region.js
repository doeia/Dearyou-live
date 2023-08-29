import request from '@/utils/request'

export function fetchTreeList() {
  return request({
    url: '/regions/treeList',
    method: 'get'
  })
}

export function getInfo(id) {
  return request({
    url: '/regions/' + id,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: `/regions/update/${data.id}`,
    method: 'post',
    data
  })
}

export function drop(id) {
  return request({
    url: `/regions/drop/` + id,
    method: 'post'
  })
}

export function create(data) {
  return request({
    url: `/regions/create`,
    method: 'post',
    data
  })
}
