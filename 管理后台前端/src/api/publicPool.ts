import request from '@/utils/request'

export interface PublicPoolQueryParams {
  pageNum?: number
  pageSize?: number
  leadName?: string
  contactMobile?: string
  industry?: string
  sourceId?: number
  level?: number
  status?: number
  recycleType?: number
  isClaimed?: number
}

export interface PublicPoolClaimParams {
  leadId: number
  remark?: string
}

export interface PublicPoolVO {
  id: number
  leadId: number
  leadNo: string
  leadName: string
  contactName: string
  contactMobile: string
  province: string
  city: string
  industry: string
  sourceId: number
  sourceName: string
  level: number
  levelName: string
  status: number
  statusName: string
  expectedAmount: number
  fromUserId: number
  fromUserName: string
  recycleType: number
  recycleTypeName: string
  recycleReason: string
  isClaimed: number
  claimUserId: number
  claimUserName: string
  claimTime: string
  createTime: string
  publicTime: string
}

export function getPublicPoolList(params: PublicPoolQueryParams) {
  return request.get('/crm/public-pool', { params })
}

export function claimPublicPoolLead(data: PublicPoolClaimParams) {
  return request.post('/crm/public-pool/claim', data)
}

export function recycleLeadToPool(leadId: number, reason?: string) {
  const params = reason ? { reason } : {}
  return request.post(`/crm/public-pool/recycle/${leadId}`, null, { params })
}
