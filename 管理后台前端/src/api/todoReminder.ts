import request from '@/utils/request'

export interface TodoReminderVO {
  id: number
  todoNo: string
  userId?: number
  userName?: string
  title: string
  content?: string
  priority?: number
  priorityName?: string
  status?: number
  statusName?: string
  remindTime?: string
  endTime?: string
  completeTime?: string
  completeRemark?: string
  businessType?: string
  businessId?: number
  businessName?: string
  followRecordId?: number
  remindType?: number
  remindTypeName?: string
  remindCount?: number
  lastRemindTime?: string
  isRecurring?: number
  recurringType?: string
  recurringConfig?: string
  createTime?: string
  updateTime?: string
}

export interface TodoReminderCreateParams {
  title: string
  content?: string
  priority?: number
  remindTime?: string
  endTime?: string
  businessType?: string
  businessId?: number
  businessName?: string
  followRecordId?: number
  remindType?: number
  isRecurring?: number
  recurringType?: string
  recurringConfig?: string
}

export interface TodoReminderUpdateParams {
  title?: string
  content?: string
  priority?: number
  status?: number
  remindTime?: string
  endTime?: string
  completeTime?: string
  completeRemark?: string
  remindType?: number
  isRecurring?: number
  recurringType?: string
  recurringConfig?: string
}

export interface TodoReminderQueryParams {
  userId?: number
  status?: number
  priority?: number
  businessType?: string
  businessId?: number
  remindTimeStart?: string
  remindTimeEnd?: string
  endTimeStart?: string
  endTimeEnd?: string
  pageNum?: number
  pageSize?: number
}

export interface TodoCompleteParams {
  completeRemark?: string
}

export function getTodoList(params: TodoReminderQueryParams) {
  return request.get('/crm/todo-reminders', { params })
}

export function getPendingTodos() {
  return request.get('/crm/todo-reminders/pending')
}

export function getTodoDetail(id: number) {
  return request.get(`/crm/todo-reminders/${id}`)
}

export function createTodo(data: TodoReminderCreateParams) {
  return request.post('/crm/todo-reminders', data)
}

export function updateTodo(id: number, data: TodoReminderUpdateParams) {
  return request.put(`/crm/todo-reminders/${id}`, data)
}

export function completeTodo(id: number, data?: TodoCompleteParams) {
  return request.post(`/crm/todo-reminders/${id}/complete`, data || {})
}

export function cancelTodo(id: number) {
  return request.post(`/crm/todo-reminders/${id}/cancel`)
}

export function deleteTodo(id: number) {
  return request.delete(`/crm/todo-reminders/${id}`)
}

export const PRIORITY_OPTIONS = [
  { value: 1, label: '高', type: 'danger' },
  { value: 2, label: '中', type: 'warning' },
  { value: 3, label: '低', type: 'info' }
]

export const STATUS_OPTIONS = [
  { value: 0, label: '待处理', type: 'warning' },
  { value: 1, label: '已完成', type: 'success' },
  { value: 2, label: '已取消', type: 'info' },
  { value: 3, label: '已过期', type: 'danger' }
]

export const REMIND_TYPE_OPTIONS = [
  { value: 1, label: '系统通知' },
  { value: 2, label: '短信' },
  { value: 3, label: '邮件' }
]

export const RECURRING_TYPE_OPTIONS = [
  { value: 'daily', label: '每日' },
  { value: 'weekly', label: '每周' },
  { value: 'monthly', label: '每月' }
]
