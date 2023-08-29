import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/member/list',
    method: 'get',
    params: params
  })
}

export function updateStatus(params) {
  return request({
    url: '/member/update/status',
    method: 'post',
    params: params
  })
}

export function create(data) {
  return request({
    url: '/member/create',
    method: 'post',
    data
  })

}

export function update(data) {
  return request({
    url: `/member/update/${data.id}`,
    method: 'post',
    data
  })
}

export function getInfo(id) {
  return request({
    url: `/member/${id}`,
    method: 'get'
  })
}

export function fetchMemberLevelList(status) {
  return request({
    url: '/memberLevel/list',
    method: 'get',
    params: {defaultStatus: status}
  })
}

export function fetchAddressList(params) {
  return request({
    url: '/memberReceiveAddress/list',
    method: 'get',
    params
  })
}

export function fetchMemberSearchHistory(params) {
  return request({
    url: '/memberKeywords/list',
    method: 'get',
    params
  })
}

export function fetchLoginLogList(params) {
  return request({
    url: '/memberLoginLog/list',
    method: 'get',
    params
  })
}

export function getMemberStatisticsInfo(id) {
  return request({
    url: '/memberStatisticsInfo/getStatInfo/' + id,
    method: 'get'
  })
}

export function getMemberGenderStatistics() {
  return request({
    url: '/memberStatisticsInfo/getStatInfoOfSex',
    method: 'get'
  })
}

export function getMemberStatisticsOfTime(params) {
  return request({
    url: '/memberStatisticsInfo/getStatInfoOfTime',
    method: 'get',
    params
  })
}

export function getMemberCountStatistics() {
  return request({
    url: '/memberStatisticsInfo/getStatInfoOfNumber',
    method: 'get'
  })
}
