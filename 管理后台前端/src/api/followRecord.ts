import request from '@/utils/request'

export interface FollowRecordVO {
  id: number
  businessType: string
  businessId: number
  businessName?: string
  followUserId?: number
  followUserName?: string
  followType?: number
  followTypeName?: string
  contentType?: number
  contentTypeName?: string
  textContent?: string
  voiceUrl?: string
  voiceDuration?: number
  imageUrls?: string[]
  videoUrl?: string
  videoDuration?: number
  fileUrl?: string
  fileName?: string
  fileSize?: number
  locationLatitude?: number
  locationLongitude?: number
  locationAddress?: string
  nextFollowTime?: string
  nextFollowRemark?: string
  todoId?: number
  isLast?: number
  createTime?: string
  updateTime?: string
}

export interface FollowRecordCreateParams {
  businessType: string
  businessId: number
  followType?: number
  contentType: number
  textContent?: string
  voiceUrl?: string
  voiceDuration?: number
  imageUrls?: string[]
  videoUrl?: string
  videoDuration?: number
  fileUrl?: string
  fileName?: string
  fileSize?: number
  locationLatitude?: number
  locationLongitude?: number
  locationAddress?: string
  nextFollowTime?: string
  nextFollowRemark?: string
  createTodo?: boolean
  todoRemindTime?: string
  todoEndTime?: string
}

export interface FollowRecordQueryParams {
  businessType?: string
  businessId?: number
  followUserId?: number
  followType?: number
  contentType?: number
  startTime?: string
  endTime?: string
  pageNum?: number
  pageSize?: number
}

export function getFollowRecordList(params: FollowRecordQueryParams) {
  return request.get('/crm/follow-records', { params })
}

export function getFollowRecordsByBusiness(businessType: string, businessId: number) {
  return request.get(`/crm/follow-records/business/${businessType}/${businessId}`)
}

export function getFollowRecordDetail(id: number) {
  return request.get(`/crm/follow-records/${id}`)
}

export function createFollowRecord(data: FollowRecordCreateParams) {
  return request.post('/crm/follow-records', data)
}

export function deleteFollowRecord(id: number) {
  return request.delete(`/crm/follow-records/${id}`)
}

export const FOLLOW_TYPE_OPTIONS = [
  { value: 1, label: '电话' },
  { value: 2, label: '微信' },
  { value: 3, label: '邮件' },
  { value: 4, label: '拜访' },
  { value: 5, label: '其他' }
]

export const CONTENT_TYPE_OPTIONS = [
  { value: 1, label: '文本' },
  { value: 2, label: '语音' },
  { value: 3, label: '图片' },
  { value: 4, label: '视频' },
  { value: 5, label: '文件' }
]

export const BUSINESS_TYPE_OPTIONS = [
  { value: 'customer', label: '客户' },
  { value: 'lead', label: '线索' },
  { value: 'opportunity', label: '商机' }
]
