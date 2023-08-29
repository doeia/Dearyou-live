import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: `/sysLog/list`,
    method: 'get',
    params
  })
}

export function getInfo(id) {
  return request({
    url: `/sysLog/${id}`,
    method: 'get'
  })
}

export function dropBatch(params) {
  return request({
    url: `/sysLog/drop`,
    method: 'post',
    params
  })
}

export function recoverBatch(params) {
  return request({
    url: `/sysLog/recover`,
    method: 'post',
    params
  })
}
