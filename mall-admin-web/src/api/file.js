import request from '@/utils/request'
export function policy() {
  return request({
    url:'/file/policy',
    method:'get',
  })
}
