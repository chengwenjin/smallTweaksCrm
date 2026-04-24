import request from '@/utils/request'

export interface AssignRuleQueryParams {
  pageNum?: number
  pageSize?: number
  ruleName?: string
  ruleType?: number
  isEnabled?: number
  assignUserId?: number
}

export interface AssignRuleCreateParams {
  ruleName: string
  ruleType?: number
  province?: string
  city?: string
  district?: string
  industry?: string
  minEmployeeCount?: number
  maxEmployeeCount?: number
  minAnnualRevenue?: number
  maxAnnualRevenue?: number
  assignUserId: number
  priority?: number
  isEnabled?: number
  description?: string
}

export interface AssignRuleUpdateParams {
  id: number
  ruleName?: string
  ruleType?: number
  province?: string
  city?: string
  district?: string
  industry?: string
  minEmployeeCount?: number
  maxEmployeeCount?: number
  minAnnualRevenue?: number
  maxAnnualRevenue?: number
  assignUserId?: number
  priority?: number
  isEnabled?: number
  description?: string
}

export interface AssignRuleVO {
  id: number
  ruleName: string
  ruleType: number
  ruleTypeName: string
  province: string
  city: string
  district: string
  industry: string
  minEmployeeCount: number
  maxEmployeeCount: number
  minAnnualRevenue: number
  maxAnnualRevenue: number
  assignUserId: number
  assignUserName: string
  priority: number
  isEnabled: number
  description: string
  createBy: string
  createTime: string
  updateBy: string
  updateTime: string
}

export function getAssignRuleList(params: AssignRuleQueryParams) {
  return request.get('/crm/assign-rules', { params })
}

export function getAssignRuleDetail(id: number) {
  return request.get(`/crm/assign-rules/${id}`)
}

export function createAssignRule(data: AssignRuleCreateParams) {
  return request.post('/crm/assign-rules', data)
}

export function updateAssignRule(id: number, data: AssignRuleUpdateParams) {
  return request.put(`/crm/assign-rules/${id}`, data)
}

export function deleteAssignRule(id: number) {
  return request.delete(`/crm/assign-rules/${id}`)
}
