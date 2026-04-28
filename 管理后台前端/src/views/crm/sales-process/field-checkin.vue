<template>
  <div class="field-checkin-management">
    <el-card>
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="签到类型">
          <el-select v-model="searchForm.checkinType" placeholder="请选择类型" clearable style="width: 100px">
            <el-option
              v-for="item in CHECKIN_TYPE_OPTIONS"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="签到日期">
          <el-date-picker
            v-model="searchForm.checkinDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            style="width: 150px"
          />
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
          新增签到
        </el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="checkinNo" label="签到编号" width="180" />
        <el-table-column prop="checkinTypeName" label="签到类型" width="80">
          <template #default="{ row }">
            <el-tag :type="row.checkinType === 1 ? 'success' : 'info'" size="small">
              {{ row.checkinTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="checkinUserName" label="签到人" width="100" />
        <el-table-column prop="checkinTime" label="签到时间" width="160" />
        <el-table-column prop="locationAddress" label="签到位置" min-width="200">
          <template #default="{ row }">
            <div class="location-cell">
              <el-icon><Location /></el-icon>
              <span :title="row.locationAddress">{{ row.locationAddress || '-' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="businessName" label="关联业务" width="120">
          <template #default="{ row }">
            <span v-if="row.businessName">{{ row.businessName }}</span>
            <span v-else class="text-gray-400">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="purpose" label="拜访目的" width="120">
          <template #default="{ row }">
            <span v-if="row.purpose">{{ row.purpose }}</span>
            <span v-else class="text-gray-400">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="photoUrls" label="现场照片" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.photoUrls && row.photoUrls.length > 0" type="info" size="small">
              {{ row.photoUrls.length }}张
            </el-tag>
            <span v-else class="text-gray-400">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="networkType" label="网络" width="60" />
        <el-table-column prop="batteryLevel" label="电量" width="70">
          <template #default="{ row }">
            <span v-if="row.batteryLevel !== null && row.batteryLevel !== undefined">
              {{ row.batteryLevel }}%
            </span>
            <span v-else class="text-gray-400">-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">详情</el-button>
            <el-button link type="primary" size="small" @click="handleViewTrack(row)">轨迹</el-button>
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
      title="新增签到"
      width="700px"
    >
      <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="签到类型">
              <el-select v-model="form.checkinType" style="width: 100%">
                <el-option
                  v-for="item in CHECKIN_TYPE_OPTIONS"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="网络类型">
              <el-select v-model="form.networkType" placeholder="请选择" clearable style="width: 100%">
                <el-option
                  v-for="item in NETWORK_TYPE_OPTIONS"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider>位置信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="纬度" prop="latitude">
              <el-input-number v-model="form.latitude" :precision="7" :min="-90" :max="90" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经度" prop="longitude">
              <el-input-number v-model="form.longitude" :precision="7" :min="-180" :max="180" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="省份">
              <el-input v-model="form.locationProvince" placeholder="请输入省份" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="城市">
              <el-input v-model="form.locationCity" placeholder="请输入城市" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="区县">
              <el-input v-model="form.locationDistrict" placeholder="请输入区县" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="详细地址">
          <el-input v-model="form.locationAddress" placeholder="请输入详细地址" />
        </el-form-item>

        <el-divider>现场照片</el-divider>
        <el-form-item label="照片URL">
          <el-tag
            v-for="(url, index) in form.photoUrls"
            :key="index"
            class="mr-1 mb-1"
            closable
            @close="form.photoUrls.splice(index, 1)"
          >
            {{ url.length > 40 ? url.substring(0, 40) + '...' : url }}
          </el-tag>
          <el-input
            v-model="photoUrlInput"
            placeholder="输入照片URL后按回车添加"
            @keyup.enter="addPhotoUrl"
            style="margin-top: 8px"
          />
        </el-form-item>

        <el-divider>关联信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="业务类型">
              <el-select v-model="form.businessType" placeholder="请选择" clearable style="width: 100%">
                <el-option label="客户" value="customer" />
                <el-option label="线索" value="lead" />
                <el-option label="商机" value="opportunity" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务ID">
              <el-input-number v-model="form.businessId" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="业务名称">
              <el-input v-model="form.businessName" placeholder="请输入业务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="拜访目的">
              <el-input v-model="form.purpose" placeholder="请输入拜访目的" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider>其他信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="电量(%)">
              <el-input-number v-model="form.batteryLevel" :min="0" :max="100" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="IP地址">
              <el-input v-model="form.ipAddress" placeholder="请输入IP地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input
            v-model="form.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="detailDialogVisible"
      title="签到详情"
      width="700px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="签到编号">{{ currentCheckin?.checkinNo }}</el-descriptions-item>
        <el-descriptions-item label="签到类型">
          <el-tag :type="currentCheckin?.checkinType === 1 ? 'success' : 'info'" size="small">
            {{ currentCheckin?.checkinTypeName }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="签到人">{{ currentCheckin?.checkinUserName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="签到时间">{{ currentCheckin?.checkinTime }}</el-descriptions-item>
        <el-descriptions-item label="位置地址" :span="2">{{ currentCheckin?.locationAddress || '-' }}</el-descriptions-item>
        <el-descriptions-item label="省份">{{ currentCheckin?.locationProvince || '-' }}</el-descriptions-item>
        <el-descriptions-item label="城市">{{ currentCheckin?.locationCity || '-' }}</el-descriptions-item>
        <el-descriptions-item label="区县">{{ currentCheckin?.locationDistrict || '-' }}</el-descriptions-item>
        <el-descriptions-item label="经纬度">
          经度: {{ currentCheckin?.longitude }}, 纬度: {{ currentCheckin?.latitude }}
        </el-descriptions-item>
        <el-descriptions-item label="关联业务">{{ currentCheckin?.businessName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="拜访目的">{{ currentCheckin?.purpose || '-' }}</el-descriptions-item>
        <el-descriptions-item label="网络类型">{{ currentCheckin?.networkType || '-' }}</el-descriptions-item>
        <el-descriptions-item label="电量">{{ currentCheckin?.batteryLevel !== null && currentCheckin?.batteryLevel !== undefined ? currentCheckin.batteryLevel + '%' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ currentCheckin?.ipAddress || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentCheckin?.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="现场照片" :span="2" v-if="currentCheckin?.photoUrls && currentCheckin.photoUrls.length > 0">
          <div v-for="(url, index) in currentCheckin.photoUrls" :key="index" class="photo-item">
            <el-tag type="info">{{ url }}</el-tag>
          </div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog
      v-model="trackDialogVisible"
      title="查看轨迹"
      width="800px"
    >
      <el-form :inline="true" class="search-form">
        <el-form-item label="查询日期">
          <el-date-picker
            v-model="trackQueryDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchTrackList">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="trackList" border stripe v-loading="trackLoading">
        <el-table-column prop="recordTime" label="记录时间" width="160" />
        <el-table-column prop="locationAddress" label="位置" min-width="200">
          <template #default="{ row }">
            <div class="location-cell">
              <el-icon><Location /></el-icon>
              <span>{{ row.locationAddress || `${row.latitude}, ${row.longitude}` }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="accuracy" label="精度(米)" width="100">
          <template #default="{ row }">
            <span v-if="row.accuracy !== null">{{ row.accuracy }}</span>
            <span v-else class="text-gray-400">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="speed" label="速度(km/h)" width="100">
          <template #default="{ row }">
            <span v-if="row.speed !== null">{{ row.speed }}</span>
            <span v-else class="text-gray-400">-</span>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="trackList.length === 0 && !trackLoading" description="暂无轨迹数据" />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import {
  getCheckinList,
  getCheckinDetail,
  getTracksByUserAndDate,
  createCheckin,
  CHECKIN_TYPE_OPTIONS,
  NETWORK_TYPE_OPTIONS,
  type FieldCheckinVO,
  type FieldTrackVO
} from '@/api/fieldCheckin'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const trackDialogVisible = ref(false)
const formRef = ref<FormInstance>()
const photoUrlInput = ref('')
const trackLoading = ref(false)

const searchForm = reactive({
  checkinType: null as number | null,
  checkinDate: '',
  startTime: '',
  endTime: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref<FieldCheckinVO[]>([])
const currentCheckin = ref<FieldCheckinVO | null>(null)
const trackList = ref<FieldTrackVO[]>([])
const trackQueryDate = ref('')

const form = reactive({
  checkinType: 1,
  latitude: null as number | null,
  longitude: null as number | null,
  locationAddress: '',
  locationProvince: '',
  locationCity: '',
  locationDistrict: '',
  photoUrls: [] as string[],
  businessType: '',
  businessId: null as number | null,
  businessName: '',
  purpose: '',
  remark: '',
  deviceInfo: '',
  ipAddress: '',
  networkType: '',
  batteryLevel: null as number | null
})

const rules: FormRules = {
  latitude: [{ required: true, message: '请输入纬度', trigger: 'blur' }],
  longitude: [{ required: true, message: '请输入经度', trigger: 'blur' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const params: any = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    if (searchForm.checkinType !== null) params.checkinType = searchForm.checkinType
    if (searchForm.checkinDate) params.checkinDate = searchForm.checkinDate
    if (searchForm.startTime) params.startTime = searchForm.startTime
    if (searchForm.endTime) params.endTime = searchForm.endTime

    const res = await getCheckinList(params)
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
  searchForm.checkinType = null
  searchForm.checkinDate = ''
  searchForm.startTime = ''
  searchForm.endTime = ''
  handleSearch()
}

const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

const handleView = async (row: FieldCheckinVO) => {
  try {
    const res = await getCheckinDetail(row.id!)
    currentCheckin.value = res.data
    detailDialogVisible.value = true
  } catch (error) {
    console.error(error)
  }
}

const handleViewTrack = (row: FieldCheckinVO) => {
  if (row.checkinUserId) {
    trackQueryDate.value = row.checkinTime ? row.checkinTime.substring(0, 10) : ''
    trackList.value = []
    trackDialogVisible.value = true
    if (trackQueryDate.value) {
      fetchTrackList()
    }
  }
}

const fetchTrackList = async () => {
  if (!trackQueryDate.value) return
  
  trackLoading.value = true
  try {
    const res = await getTracksByUserAndDate(currentCheckin?.checkinUserId || 1, trackQueryDate.value)
    trackList.value = res.data
  } catch (error) {
    console.error(error)
  } finally {
    trackLoading.value = false
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const data: any = {
          checkinType: form.checkinType,
          latitude: form.latitude,
          longitude: form.longitude
        }
        if (form.locationAddress) data.locationAddress = form.locationAddress
        if (form.locationProvince) data.locationProvince = form.locationProvince
        if (form.locationCity) data.locationCity = form.locationCity
        if (form.locationDistrict) data.locationDistrict = form.locationDistrict
        if (form.photoUrls.length > 0) data.photoUrls = form.photoUrls
        if (form.businessType) data.businessType = form.businessType
        if (form.businessId !== null) data.businessId = form.businessId
        if (form.businessName) data.businessName = form.businessName
        if (form.purpose) data.purpose = form.purpose
        if (form.remark) data.remark = form.remark
        if (form.ipAddress) data.ipAddress = form.ipAddress
        if (form.networkType) data.networkType = form.networkType
        if (form.batteryLevel !== null) data.batteryLevel = form.batteryLevel

        await createCheckin(data)
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

const addPhotoUrl = () => {
  const url = photoUrlInput.value.trim()
  if (url && !form.photoUrls.includes(url)) {
    form.photoUrls.push(url)
    photoUrlInput.value = ''
  }
}

const resetForm = () => {
  form.checkinType = 1
  form.latitude = null
  form.longitude = null
  form.locationAddress = ''
  form.locationProvince = ''
  form.locationCity = ''
  form.locationDistrict = ''
  form.photoUrls = []
  form.businessType = ''
  form.businessId = null
  form.businessName = ''
  form.purpose = ''
  form.remark = ''
  form.deviceInfo = ''
  form.ipAddress = ''
  form.networkType = ''
  form.batteryLevel = null
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.field-checkin-management {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;
}

.location-cell {
  display: flex;
  align-items: center;
  gap: 4px;
}

.mr-1 {
  margin-right: 4px;
}

.mb-1 {
  margin-bottom: 4px;
}

.text-gray-400 {
  color: #909399;
}

.photo-item {
  margin-bottom: 8px;
}
</style>
