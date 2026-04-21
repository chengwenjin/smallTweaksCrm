import request from '@/utils/request'

export interface LeadQueryParams {
  pageNum?: number
  pageSize?: number
  leadName?: string
  contactName?: string
  contactMobile?: string
  sourceId?: number
  sourceCode?: string
  level?: number
  status?: number
  assignUserId?: number
  tags?: string
}

export interface LeadCreateParams {
  leadName: string
  contactName?: string
  contactPhone?: string
  contactMobile?: string
  contactEmail?: string
  contactQq?: string
  contactWechat?: string
  province?: string
  city?: string
  district?: string
  address?: string
  industry?: string
  sourceId: number
  sourceRemark?: string
  level?: number
  probability?: number
  expectedAmount?: number
  expectedDealDate?: string
  nextFollowTime?: string
  assignUserId?: number
  description?: string
  remark?: string
  tags?: string
}

export interface LeadUpdateParams {
  id: number
  leadName?: string
  contactName?: string
  contactPhone?: string
  contactMobile?: string
  contactEmail?: string
  contactQq?: string
  contactWechat?: string
  province?: string
  city?: string
  district?: string
  address?: string
  industry?: string
  sourceId?: number
  sourceRemark?: string
  level?: number
  status?: number
  probability?: number
  expectedAmount?: number
  expectedDealDate?: string
  nextFollowTime?: string
  assignUserId?: number
  description?: string
  remark?: string
  tags?: string
}

export interface LeadStatusUpdateParams {
  status: number
  remark?: string
}

export interface LeadAssignParams {
  assignUserId: number
  remark?: string
}

export interface LeadSourceVO {
  id: number
  sourceCode: string
  sourceName: string
  sourceType: number
  sourceTypeName: string
  isSystem: number
  isEnabled: number
  sortOrder: number
  description: string
}

export interface LeadVO {
  id: number
  leadNo: string
  leadName: string
  contactName?: string
  contactPhone?: string
  contactMobile?: string
  contactEmail?: string
  contactQq?: string
  contactWechat?: string
  province?: string
  city?: string
  district?: string
  address?: string
  industry?: string
  sourceId?: number
  sourceCode?: string
  sourceName?: string
  sourceRemark?: string
  level?: number
  levelName?: string
  status?: number
  statusName?: string
  probability?: number
  expectedAmount?: number
  expectedDealDate?: string
  nextFollowTime?: string
  assignUserId?: number
  assignUserName?: string
  assignTime?: string
  description?: string
  remark?: string
  tags?: string
  tagList?: string[]
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
}

export interface LeadImportResult {
  totalCount: number
  successCount: number
  failCount: number
  successIds: number[]
  failDetails: Array<{
    rowNum: number
    data: string
    reason: string
  }>
}

export function getLeadList(params: LeadQueryParams) {
  return request.get('/crm/leads', { params })
}

export function getLeadDetail(id: number) {
  return request.get(`/crm/leads/${id}`)
}

export function createLead(data: LeadCreateParams) {
  return request.post('/crm/leads', data)
}

export function updateLead(id: number, data: LeadUpdateParams) {
  return request.put(`/crm/leads/${id}`, data)
}

export function deleteLead(id: number) {
  return request.delete(`/crm/leads/${id}`)
}

export function updateLeadStatus(id: number, data: LeadStatusUpdateParams) {
  return request.put(`/crm/leads/${id}/status`, data)
}

export function assignLead(id: number, data: LeadAssignParams) {
  return request.put(`/crm/leads/${id}/assign`, data)
}

export function getLeadSources() {
  return request.get('/crm/leads/sources')
}

export function importLeads(file: File) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/crm/leads/import', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
