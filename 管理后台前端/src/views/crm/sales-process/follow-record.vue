<template>
  <div class="follow-record-management">
    <el-card>
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="业务类型">
          <el-select v-model="searchForm.businessType" placeholder="请选择类型" clearable style="width: 120px">
            <el-option
              v-for="item in BUSINESS_TYPE_OPTIONS"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="跟进方式">
          <el-select v-model="searchForm.followType" placeholder="请选择方式" clearable style="width: 100px">
            <el-option
              v-for="item in FOLLOW_TYPE_OPTIONS"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="内容类型">
          <el-select v-model="searchForm.contentType" placeholder="请选择类型" clearable style="width: 100px">
            <el-option
              v-for="item in CONTENT_TYPE_OPTIONS"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="searchForm.startTime"
            type="datetime"
            placeholder="选择开始时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            v-model="searchForm.endTime"
            type="datetime"
            placeholder="选择结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            查询
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增跟进
        </el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="businessName" label="业务名称" width="150" />
        <el-table-column prop="businessType" label="业务类型" width="80">
          <template #default="{ row }">
            <el-tag size="small">{{ getBusinessTypeName(row.businessType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="followTypeName" label="跟进方式" width="80" />
        <el-table-column prop="contentTypeName" label="内容类型" width="80">
          <template #default="{ row }">
            <el-tag :type="getContentTypeTagType(row.contentType)" size="small">
              {{ row.contentTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="textContent" label="内容" min-width="200">
          <template #default="{ row }">
            <div class="content-cell">
              <span v-if="row.textContent">{{ row.textContent.length > 50 ? row.textContent.substring(0, 50) + '...' : row.textContent }}</span>
              <span v-else-if="row.imageUrls && row.imageUrls.length > 0">
                <el-tag type="info" size="small">图片 x{{ row.imageUrls.length }}</el-tag>
              </span>
              <span v-else-if="row.voiceUrl">
                <el-tag type="warning" size="small">语音 ({{ row.voiceDuration }}秒)</el-tag>
              </span>
              <span v-else-if="row.videoUrl">
                <el-tag type="primary" size="small">视频</el-tag>
              </span>
              <span v-else-if="row.fileUrl">
                <el-tag type="success" size="small">{{ row.fileName }}</el-tag>
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="followUserName" label="跟进人" width="100" />
        <el-table-column prop="locationAddress" label="位置" width="120">
          <template #default="{ row }">
            <span v-if="row.locationAddress" :title="row.locationAddress">{{ row.locationAddress }}</span>
            <span v-else class="text-gray-400">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="nextFollowTime" label="下次跟进" width="160">
          <template #default="{ row }">
            <span v-if="row.nextFollowTime">
              <el-icon><Timer /></el-icon>
              {{ row.nextFollowTime }}
              <span v-if="row.nextFollowRemark" class="text-gray-500 ml-1">({{ row.nextFollowRemark }})</span>
            </span>
            <span v-else class="text-gray-400">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">查看</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchData"
        @current-change="fetchData"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      @close="handleDialogClose"
    >
      <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="业务类型" prop="businessType">
              <el-select v-model="form.businessType" placeholder="请选择类型" style="width: 100%">
                <el-option
                  v-for="item in BUSINESS_TYPE_OPTIONS"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务ID" prop="businessId">
              <el-input-number v-model="form.businessId" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="跟进方式">
              <el-select v-model="form.followType" style="width: 100%">
                <el-option
                  v-for="item in FOLLOW_TYPE_OPTIONS"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内容类型" prop="contentType">
              <el-select v-model="form.contentType" style="width: 100%">
                <el-option
                  v-for="item in CONTENT_TYPE_OPTIONS"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider>内容信息</el-divider>
        <el-form-item label="文本内容" v-if="form.contentType === 1">
          <el-input
            v-model="form.textContent"
            type="textarea"
            :rows="4"
            placeholder="请输入跟进内容"
          />
        </el-form-item>
        <el-row :gutter="20" v-if="form.contentType === 2">
          <el-col :span="12">
            <el-form-item label="语音URL">
              <el-input v-model="form.voiceUrl" placeholder="请输入语音文件URL" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="语音时长(秒)">
              <el-input-number v-model="form.voiceDuration" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="图片URL" v-if="form.contentType === 3">
          <el-tag
            v-for="(url, index) in form.imageUrls"
            :key="index"
            class="mr-1 mb-1"
            closable
            @close="form.imageUrls.splice(index, 1)"
          >
            {{ url.length > 30 ? url.substring(0, 30) + '...' : url }}
          </el-tag>
          <el-input
            v-model="imageUrlInput"
            placeholder="输入图片URL后按回车添加"
            @keyup.enter="addImageUrl"
            style="margin-top: 8px"
          />
        </el-form-item>
        <el-row :gutter="20" v-if="form.contentType === 4">
          <el-col :span="12">
            <el-form-item label="视频URL">
              <el-input v-model="form.videoUrl" placeholder="请输入视频文件URL" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="视频时长(秒)">
              <el-input-number v-model="form.videoDuration" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="form.contentType === 5">
          <el-col :span="12">
            <el-form-item label="文件URL">
              <el-input v-model="form.fileUrl" placeholder="请输入文件URL" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="文件名">
              <el-input v-model="form.fileName" placeholder="请输入文件名" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider>位置信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="纬度">
              <el-input-number v-model="form.locationLatitude" :precision="7" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经度">
              <el-input-number v-model="form.locationLongitude" :precision="7" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="位置地址">
          <el-input v-model="form.locationAddress" placeholder="请输入位置地址" />
        </el-form-item>

        <el-divider>下次跟进</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="下次跟进时间">
              <el-date-picker
                v-model="form.nextFollowTime"
                type="datetime"
                placeholder="选择时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="跟进备注">
              <el-input v-model="form.nextFollowRemark" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider>待办提醒</el-divider>
        <el-form-item label="创建待办">
          <el-switch v-model="form.createTodo" />
        </el-form-item>
        <el-row :gutter="20" v-if="form.createTodo">
          <el-col :span="12">
            <el-form-item label="提醒时间">
              <el-date-picker
                v-model="form.todoRemindTime"
                type="datetime"
                placeholder="选择提醒时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="截止时间">
              <el-date-picker
                v-model="form.todoEndTime"
                type="datetime"
                placeholder="选择截止时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="detailDialogVisible"
      title="跟进记录详情"
      width="600px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="ID">{{ currentRecord?.id }}</el-descriptions-item>
        <el-descriptions-item label="业务类型">{{ getBusinessTypeName(currentRecord?.businessType) }}</el-descriptions-item>
        <el-descriptions-item label="业务名称">{{ currentRecord?.businessName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="跟进方式">{{ currentRecord?.followTypeName }}</el-descriptions-item>
        <el-descriptions-item label="内容类型">
          <el-tag :type="getContentTypeTagType(currentRecord?.contentType)" size="small">
            {{ currentRecord?.contentTypeName }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="跟进人">{{ currentRecord?.followUserName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ currentRecord?.createTime }}</el-descriptions-item>
        <el-descriptions-item label="内容" :span="2">
          <div v-if="currentRecord?.textContent" style="white-space: pre-wrap">{{ currentRecord.textContent }}</div>
          <div v-else-if="currentRecord?.imageUrls && currentRecord.imageUrls.length > 0">
            <div class="text-gray-500 mb-2">图片列表：</div>
            <el-tag v-for="(url, index) in currentRecord.imageUrls" :key="index" class="mr-1 mb-1">
              {{ url }}
            </el-tag>
          </div>
          <div v-else-if="currentRecord?.voiceUrl">
            语音文件：{{ currentRecord.voiceUrl }} ({{ currentRecord.voiceDuration }}秒)
          </div>
          <div v-else-if="currentRecord?.videoUrl">
            视频文件：{{ currentRecord.videoUrl }}
          </div>
          <div v-else-if="currentRecord?.fileUrl">
            附件文件：{{ currentRecord.fileName || currentRecord.fileUrl }}
          </div>
          <span v-else class="text-gray-400">-</span>
        </el-descriptions-item>
        <el-descriptions-item label="位置" :span="2">
          <span v-if="currentRecord?.locationAddress">{{ currentRecord.locationAddress }}</span>
          <span v-else-if="currentRecord?.locationLatitude && currentRecord?.locationLongitude">
            经度: {{ currentRecord.locationLongitude }}, 纬度: {{ currentRecord.locationLatitude }}
          </span>
          <span v-else class="text-gray-400">-</span>
        </el-descriptions-item>
        <el-descriptions-item label="下次跟进时间" v-if="currentRecord?.nextFollowTime">
          {{ currentRecord.nextFollowTime }}
        </el-descriptions-item>
        <el-descriptions-item label="下次跟进备注" v-if="currentRecord?.nextFollowRemark">
          {{ currentRecord.nextFollowRemark }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  getFollowRecordList,
  createFollowRecord,
  deleteFollowRecord,
  FOLLOW_TYPE_OPTIONS,
  CONTENT_TYPE_OPTIONS,
  BUSINESS_TYPE_OPTIONS,
  type FollowRecordVO
} from '@/api/followRecord'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const dialogTitle = ref('新增跟进')
const formRef = ref<FormInstance>()
const imageUrlInput = ref('')

const searchForm = reactive({
  businessType: '',
  followType: null as number | null,
  contentType: null as number | null,
  startTime: '',
  endTime: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref<FollowRecordVO[]>([])
const currentRecord = ref<FollowRecordVO | null>(null)

const form = reactive({
  businessType: 'customer',
  businessId: null as number | null,
  followType: 5,
  contentType: 1,
  textContent: '',
  voiceUrl: '',
  voiceDuration: null as number | null,
  imageUrls: [] as string[],
  videoUrl: '',
  videoDuration: null as number | null,
  fileUrl: '',
  fileName: '',
  fileSize: null as number | null,
  locationLatitude: null as number | null,
  locationLongitude: null as number | null,
  locationAddress: '',
  nextFollowTime: '',
  nextFollowRemark: '',
  createTodo: false,
  todoRemindTime: '',
  todoEndTime: ''
})

const rules: FormRules = {
  businessType: [{ required: true, message: '请选择业务类型', trigger: 'change' }],
  businessId: [{ required: true, message: '请输入业务ID', trigger: 'blur' }],
  contentType: [{ required: true, message: '请选择内容类型', trigger: 'change' }]
}

const getBusinessTypeName = (type?: string) => {
  const item = BUSINESS_TYPE_OPTIONS.find(o => o.value === type)
  return item?.label || type || '-'
}

const getContentTypeTagType = (type?: number) => {
  if (type === 1) return 'info'
  if (type === 2) return 'warning'
  if (type === 3) return 'success'
  if (type === 4) return 'primary'
  if (type === 5) return 'danger'
  return 'info'
}

const fetchData = async () => {
  loading.value = true
  try {
    const params: any = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    if (searchForm.businessType) params.businessType = searchForm.businessType
    if (searchForm.followType !== null) params.followType = searchForm.followType
    if (searchForm.contentType !== null) params.contentType = searchForm.contentType
    if (searchForm.startTime) params.startTime = searchForm.startTime
    if (searchForm.endTime) params.endTime = searchForm.endTime

    const res = await getFollowRecordList(params)
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

const handleReset = () => {
  searchForm.businessType = ''
  searchForm.followType = null
  searchForm.contentType = null
  searchForm.startTime = ''
  searchForm.endTime = ''
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增跟进'
  resetForm()
  dialogVisible.value = true
}

const handleView = (row: FollowRecordVO) => {
  currentRecord.value = row
  detailDialogVisible.value = true
}

const handleDelete = (row: FollowRecordVO) => {
  ElMessageBox.confirm('确定要删除该跟进记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteFollowRecord(row.id!)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error(error)
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const data: any = {
          businessType: form.businessType,
          businessId: form.businessId,
          followType: form.followType,
          contentType: form.contentType
        }
        if (form.textContent) data.textContent = form.textContent
        if (form.voiceUrl) data.voiceUrl = form.voiceUrl
        if (form.voiceDuration !== null) data.voiceDuration = form.voiceDuration
        if (form.imageUrls.length > 0) data.imageUrls = form.imageUrls
        if (form.videoUrl) data.videoUrl = form.videoUrl
        if (form.videoDuration !== null) data.videoDuration = form.videoDuration
        if (form.fileUrl) data.fileUrl = form.fileUrl
        if (form.fileName) data.fileName = form.fileName
        if (form.locationLatitude !== null) data.locationLatitude = form.locationLatitude
        if (form.locationLongitude !== null) data.locationLongitude = form.locationLongitude
        if (form.locationAddress) data.locationAddress = form.locationAddress
        if (form.nextFollowTime) data.nextFollowTime = form.nextFollowTime
        if (form.nextFollowRemark) data.nextFollowRemark = form.nextFollowRemark
        if (form.createTodo) {
          data.createTodo = true
          if (form.todoRemindTime) data.todoRemindTime = form.todoRemindTime
          if (form.todoEndTime) data.todoEndTime = form.todoEndTime
        }

        await createFollowRecord(data)
        ElMessage.success('创建成功')
        dialogVisible.value = false
        fetchData()
      } catch (error) {
        console.error(error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const addImageUrl = () => {
  const url = imageUrlInput.value.trim()
  if (url && !form.imageUrls.includes(url)) {
    form.imageUrls.push(url)
    imageUrlInput.value = ''
  }
}

const resetForm = () => {
  form.businessType = 'customer'
  form.businessId = null
  form.followType = 5
  form.contentType = 1
  form.textContent = ''
  form.voiceUrl = ''
  form.voiceDuration = null
  form.imageUrls = []
  form.videoUrl = ''
  form.videoDuration = null
  form.fileUrl = ''
  form.fileName = ''
  form.fileSize = null
  form.locationLatitude = null
  form.locationLongitude = null
  form.locationAddress = ''
  form.nextFollowTime = ''
  form.nextFollowRemark = ''
  form.createTodo = false
  form.todoRemindTime = ''
  form.todoEndTime = ''
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  resetForm()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.follow-record-management {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;
}

.content-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.ml-1 {
  margin-left: 4px;
}

.mr-1 {
  margin-right: 4px;
}

.mb-1 {
  margin-bottom: 4px;
}

.mb-2 {
  margin-bottom: 8px;
}

.text-gray-400 {
  color: #909399;
}

.text-gray-500 {
  color: #606266;
}
</style>
