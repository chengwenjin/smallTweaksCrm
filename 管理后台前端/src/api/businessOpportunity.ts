import request from '@/utils/request'

export interface BusinessOpportunityVO {
  id: number
  opportunityNo: string
  opportunityName: string
  customerId?: number
  customerName?: string
  leadId?: number
  stageId: number
  stageCode: string
  stageName: string
  winProbability: number
  expectedAmount: number
  forecastedAmount: number
  expectedDealDate?: string
  assignUserId?: number
  assignUserName?: string
  assignTime?: string
  status: number
  statusName: string
  sourceName?: string
  industry?: string
  description?: string
  remark?: string
  tags?: string
  tagList?: string[]
  nextFollowTime?: string
  followCount?: number
  lastFollowTime?: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
}

export interface BusinessOpportunityCreateParams {
  opportunityName: string
  customerId?: number
  customerName?: string
  leadId?: number
  stageId: number
  expectedAmount?: number
  expectedDealDate?: string
  assignUserId?: number
  status?: number
  sourceName?: string
  industry?: string
  description?: string
  remark?: string
  tags?: string
  nextFollowTime?: string
}

export interface BusinessOpportunityUpdateParams {
  id: number
  opportunityName?: string
  customerId?: number
  customerName?: string
  stageId?: number
  expectedAmount?: number
  expectedDealDate?: string
  assignUserId?: number
  status?: number
  sourceName?: string
  industry?: string
  description?: string
  remark?: string
  tags?: string
  nextFollowTime?: string
}

export interface BusinessOpportunityQueryParams {
  pageNum?: number
  pageSize?: number
  opportunityName?: string
  customerName?: string
  stageId?: number
  stageCode?: string
  status?: number
  assignUserId?: number
  minExpectedAmount?: number
  maxExpectedAmount?: number
  createTimeStart?: string
  createTimeEnd?: string
  tags?: string
  industry?: string
}

export interface BusinessOpportunityStageMoveParams {
  targetStageId: number
  remark?: string
}

export interface StageStatisticsVO {
  stageId: number
  stageCode: string
  stageName: string
  sortOrder: number
  winProbability: number
  opportunityCount: number
  expectedAmount: number
  forecastedAmount: number
  countPercentage: number
  amountPercentage: number
}

export interface SalesFunnelStatisticsVO {
  totalOpportunityCount: number
  totalExpectedAmount: number
  totalForecastedAmount: number
  wonCount: number
  wonAmount: number
  lostCount: number
  inProgressCount: number
  stageStatistics: StageStatisticsVO[]
}

export function getBusinessOpportunityList(params: BusinessOpportunityQueryParams) {
  return request.get('/crm/opportunities', { params })
}

export function getBusinessOpportunityDetail(id: number) {
  return request.get(`/crm/opportunities/${id}`)
}

export function createBusinessOpportunity(data: BusinessOpportunityCreateParams) {
  return request.post('/crm/opportunities', data)
}

export function updateBusinessOpportunity(id: number, data: BusinessOpportunityUpdateParams) {
  return request.put(`/crm/opportunities/${id}`, data)
}

export function deleteBusinessOpportunity(id: number) {
  return request.delete(`/crm/opportunities/${id}`)
}

export function moveOpportunityStage(id: number, data: BusinessOpportunityStageMoveParams) {
  return request.put(`/crm/opportunities/${id}/move-stage`, data)
}

export function getSalesFunnelStatistics(params: BusinessOpportunityQueryParams) {
  return request.get('/crm/opportunities/funnel-statistics', { params })
}
