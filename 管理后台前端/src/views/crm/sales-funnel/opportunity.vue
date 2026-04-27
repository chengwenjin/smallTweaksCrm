<template>
  <div class="opportunity-management">
    <el-card class="stats-card">
      <template #header>
        <div class="card-header">
          <span>销售漏斗统计</span>
          <el-button type="primary" link @click="refreshStatistics">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-value">{{ statistics.totalOpportunityCount || 0 }}</div>
            <div class="stat-label">总商机数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-value amount">{{ formatAmount(statistics.totalExpectedAmount) }}</div>
            <div class="stat-label">总预计金额</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-value amount success">{{ formatAmount(statistics.totalForecastedAmount) }}</div>
            <div class="stat-label">预期成交金额</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-value">
              <span class="success">赢:{{ statistics.wonCount || 0 }}</span>
              <span class="divider">/</span>
              <span class="danger">输:{{ statistics.lostCount || 0 }}</span>
            </div>
            <div class="stat-label">赢单/输单</div>
          </div>
        </el-col>
      </el-row>

      <div class="funnel-container" v-if="statistics.stageStatistics && statistics.stageStatistics.length > 0">
        <div class="funnel-title">销售漏斗阶段分布</div>
        <div class="funnel-chart">
          <div
            v-for="(stage, index) in stageStatisticsOrdered"
            :key="stage.stageId"
            class="funnel-stage"
            :style="{
              width: getFunnelWidth(index, stageStatisticsOrdered.length) + '%',
              backgroundColor: getFunnelColor(index)
            }"
          >
            <div class="stage-info">
              <div class="stage-name">{{ stage.stageName }}</div>
              <div class="stage-count">商机数: {{ stage.opportunityCount }}</div>
              <div class="stage-amount">金额: {{ formatAmount(stage.expectedAmount) }}</div>
              <div class="stage-winrate">赢率: {{ stage.winProbability }}%</div>
              <div class="stage-forecast">预期: {{ formatAmount(stage.forecastedAmount) }}</div>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <el-card>
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="商机名称">
          <el-input v-model="searchForm.opportunityName" placeholder="请输入商机名称" clearable />
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="searchForm.customerName" placeholder="请输入客户名称" clearable />
        </el-form-item>
        <el-form-item label="销售阶段">
          <el-select v-model="searchForm.stageId" placeholder="请选择阶段" clearable style="width: 140px">
            <el-option
              v-for="stage in stageList"
              :key="stage.id"
              :label="stage.stageName"
              :value="stage.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="进行中" :value="0" />
            <el-option label="已赢单" :value="1" />
            <el-option label="已输单" :value="2" />
          </el-select>
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
          新增商机
        </el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="opportunityNo" label="商机编号" width="200" />
        <el-table-column prop="opportunityName" label="商机名称" min-width="180">
          <template #default="{ row }">
            <div :title="row.opportunityName">{{ row.opportunityName }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="customerName" label="客户名称" width="150">
          <template #default="{ row }">
            <div :title="row.customerName">{{ row.customerName || '-' }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="stageName" label="当前阶段" width="120">
          <template #default="{ row }">
            <el-tag :type="getStageTagType(row.stageCode)" size="small">
              {{ row.stageName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="winProbability" label="赢率" width="80">
          <template #default="{ row }">
            <el-tag :type="getProbabilityTagType(row.winProbability)" size="small">
              {{ row.winProbability }}%
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="expectedAmount" label="预计金额" width="120">
          <template #default="{ row }">
            <span class="amount-text">¥{{ formatAmount(row.expectedAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="forecastedAmount" label="预期成交" width="120">
          <template #default="{ row }">
            <span class="forecast-amount">¥{{ formatAmount(row.forecastedAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="statusName" label="商机状态" width="90">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)" size="small">
              {{ row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assignUserName" label="负责人" width="100">
          <template #default="{ row }">
            {{ row.assignUserName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="success" size="small" @click="handleMoveStage(row)">转移阶段</el-button>
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
            <el-form-item label="商机名称" prop="opportunityName">
              <el-input v-model="form.opportunityName" placeholder="请输入商机名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户名称">
              <el-input v-model="form.customerName" placeholder="请输入客户名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="销售阶段" prop="stageId">
              <el-select v-model="form.stageId" placeholder="请选择阶段" style="width: 100%">
                <el-option
                  v-for="stage in stageList"
                  :key="stage.id"
                  :label="stage.stageName + ' (' + stage.winProbability + '%)'"
                  :value="stage.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商机状态" v-if="isEdit">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="进行中" :value="0" />
                <el-option label="已赢单" :value="1" />
                <el-option label="已输单" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预计金额">
              <el-input-number
                v-model="form.expectedAmount"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计成交日期">
              <el-date-picker
                v-model="form.expectedDealDate"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人">
              <el-select
                v-model="form.assignUserId"
                placeholder="请选择负责人"
                filterable
                style="width: 100%"
              >
                <el-option
                  v-for="user in userList"
                  :key="user.id"
                  :label="user.realName || user.username"
                  :value="user.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属行业">
              <el-input v-model="form.industry" placeholder="请输入所属行业" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="来源">
              <el-input v-model="form.sourceName" placeholder="请输入来源" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下次跟进时间">
              <el-date-picker
                v-model="form.nextFollowTime"
                type="datetime"
                placeholder="选择日期时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="商机描述">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="3"
                placeholder="请输入商机描述"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="备注">
              <el-input
                v-model="form.remark"
                type="textarea"
                :rows="2"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标签">
              <el-input
                v-model="form.tags"
                placeholder="多个标签用逗号分隔"
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
      v-model="moveStageDialogVisible"
      title="转移阶段"
      width="450px"
    >
      <el-form :model="moveStageForm" label-width="100px">
        <el-form-item label="目标阶段">
          <el-select v-model="moveStageForm.targetStageId" placeholder="请选择目标阶段" style="width: 100%">
            <el-option
              v-for="stage in stageList"
              :key="stage.id"
              :label="stage.stageName + ' (' + stage.winProbability + '%)'"
              :value="stage.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="moveStageForm.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="moveStageDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitMoveStage" :loading="moveStageSubmitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import {
  getBusinessOpportunityList,
  createBusinessOpportunity,
  updateBusinessOpportunity,
  deleteBusinessOpportunity,
  moveOpportunityStage,
  getSalesFunnelStatistics,
  type BusinessOpportunityVO,
  type SalesFunnelStatisticsVO,
  type StageStatisticsVO
} from '@/api/businessOpportunity'
import { getEnabledSalesStages, type SalesStageVO } from '@/api/salesStage'
import { getUserList } from '@/api/user'

const loading = ref(false)
const submitLoading = ref(false)
const moveStageSubmitLoading = ref(false)
const dialogVisible = ref(false)
const moveStageDialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref<FormInstance>()
const stageList = ref<SalesStageVO[]>([])
const userList = ref<any[]>([])
const currentRow = ref<BusinessOpportunityVO | null>(null)
const statistics = ref<SalesFunnelStatisticsVO>({} as SalesFunnelStatisticsVO)

const searchForm = reactive({
  opportunityName: '',
  customerName: '',
  stageId: null as number | null,
  status: null as number | null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref<BusinessOpportunityVO[]>([])

const form = reactive({
  id: null as number | null,
  opportunityName: '',
  customerName: '',
  stageId: null as number | null,
  expectedAmount: 0,
  expectedDealDate: '',
  assignUserId: null as number | null,
  status: 0,
  industry: '',
  description: '',
  remark: '',
  tags: '',
  sourceName: '',
  nextFollowTime: ''
})

const moveStageForm = reactive({
  targetStageId: null as number | null,
  remark: ''
})

const rules: FormRules = {
  opportunityName: [{ required: true, message: '请输入商机名称', trigger: 'blur' }],
  stageId: [{ required: true, message: '请选择销售阶段', trigger: 'change' }]
}

const stageStatisticsOrdered = computed(() => {
  if (!statistics.value.stageStatistics) return []
  return statistics.value.stageStatistics.sort((a, b) => a.sortOrder - b.sortOrder)
})

const formatAmount = (amount: number | null | undefined) => {
  if (!amount) return '0.00'
  return Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const getStageTagType = (stageCode: string) => {
  if (stageCode === 'won') return 'success'
  if (stageCode === 'lost') return 'danger'
  if (stageCode === 'business_negotiation') return 'warning'
  return 'primary'
}

const getProbabilityTagType = (probability: number) => {
  if (probability >= 70) return 'success'
  if (probability >= 40) return 'warning'
  return 'info'
}

const getStatusTagType = (status: number) => {
  if (status === 1) return 'success'
  if (status === 2) return 'danger'
  return 'primary'
}

const getFunnelWidth = (index: number, total: number) => {
  return 100 - index * (80 / Math.max(total - 1, 1))
}

const getFunnelColor = (index: number) => {
  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#00D4FF']
  return colors[index % colors.length]
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const res: any = await getBusinessOpportunityList(params)
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const fetchStatistics = async () => {
  try {
    const params: any = {}
    const res: any = await getSalesFunnelStatistics(params)
    statistics.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const fetchStages = async () => {
  try {
    const res: any = await getEnabledSalesStages()
    stageList.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const fetchUsers = async () => {
  try {
    const params = { pageNum: 1, pageSize: 1000 }
    const res: any = await getUserList(params)
    userList.value = res.data.records
  } catch (error) {
    console.error(error)
  }
}

const refreshStatistics = () => {
  fetchStatistics()
}

const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

const handleReset = () => {
  searchForm.opportunityName = ''
  searchForm.customerName = ''
  searchForm.stageId = null
  searchForm.status = null
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增商机'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row: BusinessOpportunityVO) => {
  dialogTitle.value = '编辑商机'
  isEdit.value = true
  currentRow.value = row

  Object.assign(form, {
    id: row.id,
    opportunityName: row.opportunityName,
    customerName: row.customerName || '',
    stageId: row.stageId,
    expectedAmount: row.expectedAmount || 0,
    expectedDealDate: row.expectedDealDate || '',
    assignUserId: row.assignUserId,
    status: row.status,
    industry: row.industry || '',
    description: row.description || '',
    remark: row.remark || '',
    tags: row.tags || '',
    sourceName: row.sourceName || '',
    nextFollowTime: row.nextFollowTime || ''
  })

  dialogVisible.value = true
}

const handleMoveStage = (row: BusinessOpportunityVO) => {
  currentRow.value = row
  moveStageForm.targetStageId = row.stageId
  moveStageForm.remark = ''
  moveStageDialogVisible.value = true
}

const handleDelete = (row: BusinessOpportunityVO) => {
  ElMessageBox.confirm('确定要删除该商机吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteBusinessOpportunity(row.id)
      ElMessage.success('删除成功')
      fetchData()
      fetchStatistics()
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
        if (isEdit.value) {
          await updateBusinessOpportunity(form.id!, form as any)
          ElMessage.success('更新成功')
        } else {
          await createBusinessOpportunity(form as any)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
        fetchData()
        fetchStatistics()
      } catch (error) {
        console.error(error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleSubmitMoveStage = async () => {
  if (!moveStageForm.targetStageId) {
    ElMessage.warning('请选择目标阶段')
    return
  }

  moveStageSubmitLoading.value = true
  try {
    await moveOpportunityStage(currentRow.value!.id, {
      targetStageId: moveStageForm.targetStageId!,
      remark: moveStageForm.remark
    })
    ElMessage.success('阶段转移成功')
    moveStageDialogVisible.value = false
    fetchData()
    fetchStatistics()
  } catch (error) {
    console.error(error)
  } finally {
    moveStageSubmitLoading.value = false
  }
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    opportunityName: '',
    customerName: '',
    stageId: null,
    expectedAmount: 0,
    expectedDealDate: '',
    assignUserId: null,
    status: 0,
    industry: '',
    description: '',
    remark: '',
    tags: '',
    sourceName: '',
    nextFollowTime: ''
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  resetForm()
}

onMounted(() => {
  fetchData()
  fetchStatistics()
  fetchStages()
  fetchUsers()
})
</script>

<style scoped>
.opportunity-management {
  padding: 20px;
}

.stats-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-item {
  text-align: center;
  padding: 15px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 8px;
}

.stat-value.amount {
  font-size: 20px;
}

.stat-value.success {
  color: #67C23A;
}

.stat-value .success {
  color: #67C23A;
}

.stat-value .danger {
  color: #F56C6C;
}

.stat-value .divider {
  color: #909399;
  margin: 0 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.funnel-container {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #EBEEF5;
}

.funnel-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #303133;
}

.funnel-chart {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
}

.funnel-stage {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 15px;
  border-radius: 8px;
  color: white;
  min-height: 80px;
  transition: all 0.3s;
}

.funnel-stage:hover {
  opacity: 0.9;
  transform: scale(1.02);
}

.stage-info {
  text-align: center;
}

.stage-name {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
}

.stage-count,
.stage-amount,
.stage-winrate,
.stage-forecast {
  font-size: 13px;
  margin: 4px 0;
  opacity: 0.95;
}

.search-form {
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;
}

.amount-text {
  font-weight: 500;
  color: #409EFF;
}

.forecast-amount {
  font-weight: 600;
  color: #67C23A;
}
</style>
