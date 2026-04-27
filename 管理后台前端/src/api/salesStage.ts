import request from '@/utils/request'

export interface SalesStageVO {
  id: number
  stageCode: string
  stageName: string
  sortOrder: number
  winProbability: number
  description: string
  isSystem: number
  isEnabled: number
  isClosed: number
  closeType: number
  closeTypeName: string
  createTime: string
  updateTime: string
}

export interface SalesStageCreateParams {
  stageCode: string
  stageName: string
  sortOrder: number
  winProbability?: number
  description?: string
  isEnabled?: number
  isClosed?: number
  closeType?: number
}

export interface SalesStageUpdateParams {
  id: number
  stageCode?: string
  stageName?: string
  sortOrder?: number
  winProbability?: number
  description?: string
  isEnabled?: number
  isClosed?: number
  closeType?: number
}

export interface SalesStageQueryParams {
  pageNum?: number
  pageSize?: number
  stageCode?: string
  stageName?: string
  isEnabled?: number
  isSystem?: number
}

export function getSalesStageList(params: SalesStageQueryParams) {
  return request.get('/crm/sales-stages', { params })
}

export function getEnabledSalesStages() {
  return request.get('/crm/sales-stages/enabled')
}

export function getSalesStageDetail(id: number) {
  return request.get(`/crm/sales-stages/${id}`)
}

export function createSalesStage(data: SalesStageCreateParams) {
  return request.post('/crm/sales-stages', data)
}

export function updateSalesStage(data: SalesStageUpdateParams) {
  return request.put('/crm/sales-stages', data)
}

export function deleteSalesStage(id: number) {
  return request.delete(`/crm/sales-stages/${id}`)
}
