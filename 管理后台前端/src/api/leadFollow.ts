import request from '@/utils/request'

export interface LeadFollowQueryParams {
  leadId: number
  pageNum?: number
  pageSize?: number
}

export interface AssignRecordQueryParams {
  pageNum?: number
  pageSize?: number
  leadNo?: string
  leadName?: string
  assignType?: number
  fromUserId?: number
  toUserId?: number
}

export interface LeadFollowCreateParams {
  leadId: number
  followType?: number
  followContent: string
  nextFollowTime?: string
  nextFollowRemark?: string
}

export interface LeadFollowVO {
  id: number
  leadId: number
  leadNo: string
  followUserId: number
  followUserName: string
  followType: number
  followTypeName: string
  followContent: string
  nextFollowTime: string
  nextFollowRemark: string
  isLast: number
  createTime: string
}

export interface AssignRecordVO {
  id: number
  leadId: number
  leadNo: string
  leadName: string
  fromUserId: number
  fromUserName: string
  toUserId: number
  toUserName: string
  assignType: number
  assignTypeName: string
  ruleId: number
  ruleName: string
  reason: string
  createTime: string
}

export function getLeadFollowList(params: LeadFollowQueryParams) {
  return request.get('/crm/lead-follows', { params })
}

export function createLeadFollow(data: LeadFollowCreateParams) {
  return request.post('/crm/lead-follows', data)
}

export function getAssignRecordList(params: AssignRecordQueryParams) {
  return request.get('/crm/lead-follows/assign-records', { params })
}

export function checkDuplicate(data: { leadId?: number; leadName?: string; contactMobile?: string; checkType?: number }) {
  return request.post('/crm/leads/duplicate-check', data)
}
