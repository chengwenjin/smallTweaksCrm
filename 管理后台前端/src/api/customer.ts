import request from '@/utils/request'

export interface CustomerQueryParams {
  pageNum?: number
  pageSize?: number
  customerName?: string
  shortName?: string
  creditCode?: string
  levelCode?: string
  status?: number
  ownerUserId?: number
  industry?: string
  province?: string
  city?: string
  tags?: string
  source?: string
}

export interface CustomerCreateParams {
  customerName: string
  shortName?: string
  customerType?: string
  creditCode?: string
  legalPerson?: string
  registeredCapital?: number
  establishDate?: string
  businessStatus?: string
  businessScope?: string
  registeredAddress?: string
  industry?: string
  province?: string
  city?: string
  district?: string
  address?: string
  website?: string
  email?: string
  phone?: string
  fax?: string
  employeeCount?: number
  annualRevenue?: number
  companyScale?: string
  levelCode?: string
  ownerUserId?: number
  tags?: string
  source?: string
  status?: number
  description?: string
  remark?: string
  parentCustomerId?: number
}

export interface CustomerUpdateParams {
  id: number
  customerName?: string
  shortName?: string
  customerType?: string
  creditCode?: string
  legalPerson?: string
  registeredCapital?: number
  establishDate?: string
  businessStatus?: string
  businessScope?: string
  registeredAddress?: string
  industry?: string
  province?: string
  city?: string
  district?: string
  address?: string
  website?: string
  email?: string
  phone?: string
  fax?: string
  employeeCount?: number
  annualRevenue?: number
  companyScale?: string
  levelCode?: string
  ownerUserId?: number
  tags?: string
  source?: string
  status?: number
  description?: string
  remark?: string
  parentCustomerId?: number
}

export interface CustomerLevelVO {
  id: number
  levelCode: string
  levelName: string
  sortOrder: number
  description: string
  isEnabled: number
}

export interface CustomerTagVO {
  id: number
  tagName: string
  tagColor: string
  tagCategory: string
  sortOrder: number
  description: string
  isEnabled: number
}

export interface CustomerContactVO {
  id: number
  customerId: number
  contactName: string
  contactPosition?: string
  contactDepartment?: string
  mobile?: string
  phone?: string
  email?: string
  wechat?: string
  qq?: string
  isKeyContact?: number
  isKeyContactName?: string
  isPrimary?: number
  isPrimaryName?: string
  sortOrder?: number
  gender?: number
  genderName?: string
  birthday?: string
  description?: string
}

export interface CustomerFollowVO {
  id: number
  customerId: number
  followUserId?: number
  followUserName?: string
  followType?: number
  followTypeName?: string
  followContent: string
  nextFollowTime?: string
  nextFollowRemark?: string
  isLast?: number
  createTime?: string
}

export interface CustomerQuoteVO {
  id: number
  customerId: number
  quoteNo: string
  quoteName: string
  quoteAmount: number
  quoteDate: string
  validDate?: string
  status?: number
  statusName?: string
  description?: string
  createTime?: string
}

export interface CustomerContractVO {
  id: number
  customerId: number
  contractNo: string
  contractName: string
  contractType?: string
  contractAmount: number
  signedDate?: string
  startDate?: string
  endDate?: string
  status?: number
  statusName?: string
  description?: string
  createTime?: string
}

export interface CustomerPaymentVO {
  id: number
  customerId: number
  contractId?: number
  paymentNo: string
  paymentAmount: number
  paymentDate: string
  paymentMethod?: string
  paymentMethodName?: string
  paymentStatus?: number
  paymentStatusName?: string
  description?: string
  createTime?: string
}

export interface CustomerTicketVO {
  id: number
  customerId: number
  ticketNo: string
  ticketTitle: string
  ticketType?: string
  ticketTypeName?: string
  priority?: number
  priorityName?: string
  status?: number
  statusName?: string
  description: string
  solution?: string
  assigneeUserId?: number
  assigneeUserName?: string
  createUserId?: number
  createUserName?: string
  resolvedTime?: string
  closedTime?: string
  createTime?: string
}

export interface CustomerVO {
  id: number
  customerNo: string
  customerName: string
  shortName?: string
  customerType?: string
  customerTypeName?: string
  creditCode?: string
  legalPerson?: string
  registeredCapital?: number
  establishDate?: string
  businessStatus?: string
  businessScope?: string
  registeredAddress?: string
  industry?: string
  province?: string
  city?: string
  district?: string
  address?: string
  fullAddress?: string
  website?: string
  email?: string
  phone?: string
  fax?: string
  employeeCount?: number
  annualRevenue?: number
  companyScale?: string
  levelCode?: string
  levelName?: string
  ownerUserId?: number
  ownerUserName?: string
  tags?: string
  tagList?: string[]
  source?: string
  status?: number
  statusName?: string
  firstContactTime?: string
  lastContactTime?: string
  description?: string
  remark?: string
  parentCustomerId?: number
  parentCustomerName?: string
  contacts?: CustomerContactVO[]
  follows?: CustomerFollowVO[]
  quotes?: CustomerQuoteVO[]
  contracts?: CustomerContractVO[]
  payments?: CustomerPaymentVO[]
  tickets?: CustomerTicketVO[]
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
}

export interface CustomerContactCreateParams {
  customerId: number
  contactName: string
  contactPosition?: string
  contactDepartment?: string
  mobile?: string
  phone?: string
  email?: string
  wechat?: string
  qq?: string
  isKeyContact?: number
  isPrimary?: number
  sortOrder?: number
  gender?: number
  birthday?: string
  description?: string
}

export interface CustomerContactUpdateParams {
  id: number
  customerId?: number
  contactName?: string
  contactPosition?: string
  contactDepartment?: string
  mobile?: string
  phone?: string
  email?: string
  wechat?: string
  qq?: string
  isKeyContact?: number
  isPrimary?: number
  sortOrder?: number
  gender?: number
  birthday?: string
  description?: string
}

export interface CustomerFollowCreateParams {
  customerId: number
  followType?: number
  followContent: string
  nextFollowTime?: string
  nextFollowRemark?: string
}

export function getCustomerList(params: CustomerQueryParams) {
  return request.get('/crm/customers', { params })
}

export function getCustomerDetail(id: number) {
  return request.get(`/crm/customers/${id}`)
}

export function createCustomer(data: CustomerCreateParams) {
  return request.post('/crm/customers', data)
}

export function updateCustomer(id: number, data: CustomerUpdateParams) {
  return request.put(`/crm/customers/${id}`, data)
}

export function deleteCustomer(id: number) {
  return request.delete(`/crm/customers/${id}`)
}

export function getCustomerLevels() {
  return request.get('/crm/customers/levels')
}

export function getCustomerTags() {
  return request.get('/crm/customers/tags')
}

export function getCustomerContacts(customerId: number) {
  return request.get(`/crm/customers/${customerId}/contacts`)
}

export function createCustomerContact(customerId: number, data: CustomerContactCreateParams) {
  return request.post(`/crm/customers/${customerId}/contacts`, data)
}

export function updateCustomerContact(customerId: number, id: number, data: CustomerContactUpdateParams) {
  return request.put(`/crm/customers/${customerId}/contacts/${id}`, data)
}

export function deleteCustomerContact(customerId: number, id: number) {
  return request.delete(`/crm/customers/${customerId}/contacts/${id}`)
}

export function getCustomerFollows(customerId: number) {
  return request.get(`/crm/customers/${customerId}/follows`)
}

export function createCustomerFollow(customerId: number, data: CustomerFollowCreateParams) {
  return request.post(`/crm/customers/${customerId}/follows`, data)
}

export function deleteCustomerFollow(customerId: number, id: number) {
  return request.delete(`/crm/customers/${customerId}/follows/${id}`)
}

export function getAllCustomerTags() {
  return request.get('/crm/customer-tags')
}

export function createCustomerTag(data: { tagName: string; tagColor?: string; tagCategory?: string; sortOrder?: number; description?: string }) {
  return request.post('/crm/customer-tags', data)
}

export function deleteCustomerTag(id: number) {
  return request.delete(`/crm/customer-tags/${id}`)
}
