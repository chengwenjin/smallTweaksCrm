import request from '@/utils/request'

export interface PrivateSeaConfigVO {
  id: number
  configType: number
  configTypeName?: string
  roleId?: number
  roleName?: string
  userId?: number
  userName?: string
  maxCustomerCount: number
  maxLeadCount: number
  autoRecycleDays: number
  isEnabled: number
  isEnabledName?: string
  description?: string
  sortOrder?: number
  createTime?: string
  updateTime?: string
}

export interface PrivateSeaUsageVO {
  userId: number
  userName?: string
  customerCount: number
  maxCustomerCount: number
  customerRemaining: number
  leadCount: number
  maxLeadCount: number
  leadRemaining: number
  autoRecycleDays: number
  customerUsageRate: number
  leadUsageRate: number
}

export interface PublicSeaRuleVO {
  id: number
  ruleName: string
  ruleType: number
  ruleTypeName?: string
  rotateDays?: number
  claimLimitPerDay: number
  claimLimitPerWeek: number
  autoRecycleEnabled: number
  autoRecycleEnabledName?: string
  autoRecycleDays: number
  isEnabled: number
  isEnabledName?: string
  description?: string
  sortOrder?: number
  createTime?: string
  updateTime?: string
}

export interface TransferRecordVO {
  id: number
  transferNo: string
  transferType: number
  transferTypeName?: string
  fromUserId: number
  fromUserName: string
  fromDepartmentId?: number
  fromDepartmentName?: string
  toUserId?: number
  toUserName?: string
  toDepartmentId?: number
  toDepartmentName?: string
  transferMethod: number
  transferMethodName?: string
  customerCount: number
  leadCount: number
  contractCount: number
  followCount: number
  status: number
  statusName?: string
  reason?: string
  remark?: string
  operatorId: number
  operatorName: string
  details?: TransferDetailVO[]
  createTime?: string
  updateTime?: string
}

export interface TransferDetailVO {
  id: number
  recordId: number
  transferNo: string
  resourceType: number
  resourceTypeName?: string
  resourceId: number
  resourceNo?: string
  resourceName: string
  fromUserId: number
  fromUserName: string
  toUserId?: number
  toUserName?: string
  transferStatus: number
  transferStatusName?: string
  transferTime?: string
  remark?: string
}

export interface PrivateSeaConfigQueryParams {
  pageNum?: number
  pageSize?: number
  configType?: number
  roleId?: number
  userId?: number
  isEnabled?: number
}

export interface PrivateSeaConfigCreateParams {
  configType: number
  roleId?: number
  roleName?: string
  userId?: number
  userName?: string
  maxCustomerCount?: number
  maxLeadCount?: number
  autoRecycleDays?: number
  isEnabled?: number
  description?: string
  sortOrder?: number
}

export interface PrivateSeaConfigUpdateParams {
  id: number
  configType?: number
  roleId?: number
  roleName?: string
  userId?: number
  userName?: string
  maxCustomerCount?: number
  maxLeadCount?: number
  autoRecycleDays?: number
  isEnabled?: number
  description?: string
  sortOrder?: number
}

export interface PublicSeaRuleQueryParams {
  pageNum?: number
  pageSize?: number
  ruleType?: number
  isEnabled?: number
}

export interface PublicSeaRuleCreateParams {
  ruleName: string
  ruleType?: number
  rotateDays?: number
  claimLimitPerDay?: number
  claimLimitPerWeek?: number
  autoRecycleEnabled?: number
  autoRecycleDays?: number
  isEnabled?: number
  description?: string
  sortOrder?: number
}

export interface PublicSeaRuleUpdateParams {
  id: number
  ruleName?: string
  ruleType?: number
  rotateDays?: number
  claimLimitPerDay?: number
  claimLimitPerWeek?: number
  autoRecycleEnabled?: number
  autoRecycleDays?: number
  isEnabled?: number
  description?: string
  sortOrder?: number
}

export interface TransferRecordQueryParams {
  pageNum?: number
  pageSize?: number
  transferType?: number
  fromUserId?: number
  toUserId?: number
  status?: number
  transferNo?: string
  keyword?: string
}

export interface TransferCreateParams {
  transferType: number
  fromUserId: number
  fromUserName?: string
  toUserId?: number
  toUserName?: string
  transferMethod: number
  reason?: string
  remark?: string
  customerIds?: number[]
  leadIds?: number[]
}

export function getPrivateSeaConfigList(params: PrivateSeaConfigQueryParams) {
  return request.get('/crm/private-sea', { params })
}

export function getPrivateSeaConfig(id: number) {
  return request.get(`/crm/private-sea/${id}`)
}

export function getCurrentUserPrivateSeaUsage() {
  return request.get('/crm/private-sea/usage')
}

export function getUserPrivateSeaUsage(userId: number) {
  return request.get(`/crm/private-sea/usage/${userId}`)
}

export function createPrivateSeaConfig(data: PrivateSeaConfigCreateParams) {
  return request.post('/crm/private-sea', data)
}

export function updatePrivateSeaConfig(data: PrivateSeaConfigUpdateParams) {
  return request.put('/crm/private-sea', data)
}

export function deletePrivateSeaConfig(id: number) {
  return request.delete(`/crm/private-sea/${id}`)
}

export function getPublicSeaRuleList(params: PublicSeaRuleQueryParams) {
  return request.get('/crm/public-sea-rule', { params })
}

export function getPublicSeaRule(id: number) {
  return request.get(`/crm/public-sea-rule/${id}`)
}

export function getActivePublicSeaRule() {
  return request.get('/crm/public-sea-rule/active')
}

export function canClaimLead() {
  return request.get('/crm/public-sea-rule/can-claim')
}

export function getTodayClaimCount() {
  return request.get('/crm/public-sea-rule/claim-count/today')
}

export function getWeekClaimCount() {
  return request.get('/crm/public-sea-rule/claim-count/week')
}

export function createPublicSeaRule(data: PublicSeaRuleCreateParams) {
  return request.post('/crm/public-sea-rule', data)
}

export function updatePublicSeaRule(data: PublicSeaRuleUpdateParams) {
  return request.put('/crm/public-sea-rule', data)
}

export function deletePublicSeaRule(id: number) {
  return request.delete(`/crm/public-sea-rule/${id}`)
}

export function getTransferRecordList(params: TransferRecordQueryParams) {
  return request.get('/crm/transfer', { params })
}

export function getTransferRecord(id: number) {
  return request.get(`/crm/transfer/${id}`)
}

export function getUserCustomers(userId: number) {
  return request.get(`/crm/transfer/user/customers/${userId}`)
}

export function getUserLeads(userId: number) {
  return request.get(`/crm/transfer/user/leads/${userId}`)
}

export function createTransfer(data: TransferCreateParams) {
  return request.post('/crm/transfer', data)
}

export function resignTransfer(fromUserId: number, toUserId?: number, reason?: string) {
  const params: any = { fromUserId }
  if (toUserId) params.toUserId = toUserId
  if (reason) params.reason = reason
  return request.post('/crm/transfer/resign', null, { params })
}
