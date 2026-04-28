import request from '@/utils/request'

export interface FieldCheckinVO {
  id: number
  checkinNo: string
  checkinUserId?: number
  checkinUserName?: string
  checkinType?: number
  checkinTypeName?: string
  checkinTime?: string
  latitude?: number
  longitude?: number
  locationAddress?: string
  locationProvince?: string
  locationCity?: string
  locationDistrict?: string
  photoUrls?: string[]
  businessType?: string
  businessId?: number
  businessName?: string
  purpose?: string
  remark?: string
  deviceInfo?: string
  ipAddress?: string
  networkType?: string
  batteryLevel?: number
  isAbnormal?: number
  abnormalReason?: string
  createTime?: string
  updateTime?: string
}

export interface FieldTrackVO {
  id: number
  trackNo: string
  trackUserId?: number
  trackUserName?: string
  trackDate?: string
  latitude?: number
  longitude?: number
  locationAddress?: string
  locationProvince?: string
  locationCity?: string
  locationDistrict?: string
  accuracy?: number
  speed?: number
  direction?: number
  recordTime?: string
  createTime?: string
}

export interface FieldCheckinCreateParams {
  checkinType?: number
  latitude: number
  longitude: number
  locationAddress?: string
  locationProvince?: string
  locationCity?: string
  locationDistrict?: string
  photoUrls?: string[]
  businessType?: string
  businessId?: number
  businessName?: string
  purpose?: string
  remark?: string
  deviceInfo?: string
  ipAddress?: string
  networkType?: string
  batteryLevel?: number
}

export interface FieldCheckinQueryParams {
  checkinUserId?: number
  checkinType?: number
  businessType?: string
  businessId?: number
  startTime?: string
  endTime?: string
  checkinDate?: string
  isAbnormal?: number
  pageNum?: number
  pageSize?: number
}

export interface FieldTrackQueryParams {
  trackUserId?: number
  trackDate?: string
  pageNum?: number
  pageSize?: number
}

export function getCheckinList(params: FieldCheckinQueryParams) {
  return request.get('/crm/field-checkins', { params })
}

export function getTodayCheckins() {
  return request.get('/crm/field-checkins/today')
}

export function getCheckinDetail(id: number) {
  return request.get(`/crm/field-checkins/${id}`)
}

export function createCheckin(data: FieldCheckinCreateParams) {
  return request.post('/crm/field-checkins', data)
}

export function getTrackList(params: FieldTrackQueryParams) {
  return request.get('/crm/field-checkins/tracks', { params })
}

export function getTracksByUserAndDate(userId: number, date: string) {
  return request.get(`/crm/field-checkins/tracks/${userId}/${date}`)
}

export const CHECKIN_TYPE_OPTIONS = [
  { value: 1, label: '签到' },
  { value: 2, label: '签退' }
]

export const NETWORK_TYPE_OPTIONS = [
  { value: 'wifi', label: 'WiFi' },
  { value: '4g', label: '4G' },
  { value: '5g', label: '5G' }
]
