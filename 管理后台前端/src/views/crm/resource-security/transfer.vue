<template>
  <div class="resource-transfer">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>离职一键回收</span>
          </template>

          <el-form :model="transferForm" :rules="transferRules" ref="transferFormRef" label-width="120px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="原负责人" prop="fromUserId">
                  <el-select
                    v-model="transferForm.fromUserId"
                    placeholder="请选择原负责人"
                    style="width: 100%"
                    filterable
                    @change="handleFromUserChange"
                  >
                    <el-option
                      v-for="user in userList"
                      :key="user.id"
                      :label="user.nickname || user.username"
                      :value="user.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="接手人" prop="toUserId">
                  <el-select
                    v-model="transferForm.toUserId"
                    placeholder="请选择接手人（不选则移入公海）"
                    style="width: 100%"
                    filterable
                    clearable
                  >
                    <el-option
                      v-for="user in userList"
                      :key="user.id"
                      :label="user.nickname || user.username"
                      :value="user.id"
                      :disabled="user.id === transferForm.fromUserId"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="转移方式" prop="transferMethod">
                  <el-radio-group v-model="transferForm.transferMethod">
                    <el-radio :value="1">全部转移</el-radio>
                    <el-radio :value="3">移入公海</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="原因">
                  <el-input v-model="transferForm.reason" placeholder="请输入原因" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item v-if="transferForm.fromUserId">
              <template #label>
                <span class="form-label">资源预览</span>
              </template>
              <div class="resource-preview">
                <el-card class="preview-card">
                  <template #header>
                    <div class="card-header">
                      <span>客户列表 ({{ customerList.length }} 条)</span>
                    </div>
                  </template>
                  <el-table :data="customerList" size="small" v-loading="loadingCustomer" style="width: 100%">
                    <el-table-column type="selection" width="40" v-if="transferForm.transferMethod === 2" />
                    <el-table-column prop="customerNo" label="客户编号" width="180" />
                    <el-table-column prop="customerName" label="客户名称" width="200">
                      <template #default="{ row }">
                        <div :title="row.customerName">{{ row.customerName }}</div>
                      </template>
                    </el-table-column>
                    <el-table-column prop="levelCode" label="等级" width="80">
                      <template #default="{ row }">
                        <el-tag :type="getLevelTagType(row.levelCode)" size="small">{{ row.levelCode || '-' }}</el-tag>
                      </template>
                    </el-table-column>
                    <el-table-column prop="status" label="状态" width="80">
                      <template #default="{ row }">
                        <el-tag :type="getStatusTagType(row.status)" size="small">
                          {{ getStatusName(row.status) }}
                        </el-tag>
                      </template>
                    </el-table-column>
                    <el-table-column prop="createTime" label="创建时间" width="180" />
                  </el-table>
                </el-card>

                <el-card class="preview-card" style="margin-top: 20px">
                  <template #header>
                    <div class="card-header">
                      <span>线索列表 ({{ leadList.length }} 条)</span>
                    </div>
                  </template>
                  <el-table :data="leadList" size="small" v-loading="loadingLead" style="width: 100%">
                    <el-table-column type="selection" width="40" v-if="transferForm.transferMethod === 2" />
                    <el-table-column prop="leadNo" label="线索编号" width="180" />
                    <el-table-column prop="leadName" label="线索名称" width="200">
                      <template #default="{ row }">
                        <div :title="row.leadName">{{ row.leadName }}</div>
                      </template>
                    </el-table-column>
                    <el-table-column prop="contactName" label="联系人" width="100" />
                    <el-table-column prop="contactMobile" label="手机号" width="130" />
                    <el-table-column prop="sourceName" label="来源" width="100" />
                    <el-table-column prop="status" label="状态" width="80">
                      <template #default="{ row }">
                        <el-tag :type="getLeadStatusTagType(row.status)" size="small">
                          {{ getLeadStatusName(row.status) }}
                        </el-tag>
                      </template>
                    </el-table-column>
                    <el-table-column prop="createTime" label="创建时间" width="180" />
                  </el-table>
                </el-card>
              </div>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleTransfer" :loading="transferring">
                <el-icon><Switch /></el-icon>
                执行转移
              </el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>转移记录</span>
          </template>

          <el-form :inline="true" :model="searchForm" class="search-form">
            <el-form-item label="转移类型">
              <el-select v-model="searchForm.transferType" placeholder="请选择类型" clearable style="width: 140px">
                <el-option label="离职回收" :value="1" />
                <el-option label="手动转移" :value="2" />
                <el-option label="自动回收" :value="3" />
                <el-option label="调岗转移" :value="4" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">
                <el-icon><Search /></el-icon>
                查询
              </el-button>
              <el-button @click="handleRecordReset">
                <el-icon><Refresh /></el-icon>
                重置
              </el-button>
            </el-form-item>
          </el-form>

          <el-table :data="recordList" border stripe v-loading="loadingRecord" style="width: 100%">
            <el-table-column prop="transferNo" label="转移单号" width="200" />
            <el-table-column prop="transferTypeName" label="转移类型" width="100">
              <template #default="{ row }">
                <el-tag size="small">{{ row.transferTypeName || '-' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="fromUserName" label="原负责人" width="120" />
            <el-table-column prop="toUserName" label="接手人" width="120">
              <template #default="{ row }">
                {{ row.toUserName || '公海' }}
              </template>
            </el-table-column>
            <el-table-column prop="customerCount" label="客户数量" width="80" />
            <el-table-column prop="leadCount" label="线索数量" width="80" />
            <el-table-column prop="reason" label="原因" width="200">
              <template #default="{ row }">
                <div :title="row.reason">{{ row.reason || '-' }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="operatorName" label="操作人" width="100" />
            <el-table-column prop="createTime" label="操作时间" width="180" />
            <el-table-column label="操作" width="100" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" size="small" @click="handleViewDetail(row)">详情</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            v-model:current-page="recordPagination.pageNum"
            v-model:page-size="recordPagination.pageSize"
            :total="recordPagination.total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="fetchRecords"
            @current-change="fetchRecords"
            style="margin-top: 20px; justify-content: flex-end"
          />
        </el-card>
      </el-col>
    </el-row>

    <el-dialog
      v-model="detailDialogVisible"
      title="转移详情"
      width="800px"
    >
      <el-table :data="detailList" border stripe style="width: 100%">
        <el-table-column prop="resourceTypeName" label="资源类型" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ row.resourceTypeName || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="resourceNo" label="资源编号" width="200" />
        <el-table-column prop="resourceName" label="资源名称" width="200">
          <template #default="{ row }">
            <div :title="row.resourceName">{{ row.resourceName }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="fromUserName" label="原负责人" width="100" />
        <el-table-column prop="toUserName" label="接手人" width="100">
          <template #default="{ row }">
            {{ row.toUserName || '公海' }}
          </template>
        </el-table-column>
        <el-table-column prop="transferTime" label="转移时间" width="180" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  getTransferRecordList,
  getTransferRecord,
  resignTransfer,
  getUserCustomers,
  getUserLeads,
  type TransferRecordVO,
  type TransferDetailVO
} from '@/api/resourceSecurity'
import { getUserList } from '@/api/user'
import type { CrmCustomer } from '@/api/customer'
import type { CrmLead } from '@/api/lead'

const loading = ref(false)
const loadingCustomer = ref(false)
const loadingLead = ref(false)
const loadingRecord = ref(false)
const transferring = ref(false)
const detailDialogVisible = ref(false)
const transferFormRef = ref<FormInstance>()

const userList = ref<any[]>([])
const customerList = ref<CrmCustomer[]>([])
const leadList = ref<CrmLead[]>([])
const recordList = ref<TransferRecordVO[]>([])
const detailList = ref<TransferDetailVO[]>([])

const transferForm = reactive({
  fromUserId: undefined as number | undefined,
  toUserId: undefined as number | undefined,
  transferMethod: 1,
  reason: '员工离职，资源转移'
})

const transferRules: FormRules = {
  fromUserId: [{ required: true, message: '请选择原负责人', trigger: 'change' }]
}

const searchForm = reactive({
  transferType: undefined as number | undefined
})

const recordPagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const getLevelTagType = (code: string | undefined) => {
  if (code === 'A') return 'danger'
  if (code === 'B') return 'warning'
  if (code === 'C') return 'primary'
  if (code === 'D') return 'info'
  return ''
}

const getStatusTagType = (status: number | undefined) => {
  if (status === 0) return 'info'
  if (status === 1) return 'success'
  if (status === 2) return 'danger'
  if (status === 3) return 'warning'
  return ''
}

const getStatusName = (status: number | undefined) => {
  const names = ['潜在', '合作中', '已流失', '休眠']
  if (status !== undefined && status >= 0 && status <= 3) {
    return names[status]
  }
  return '-'
}

const getLeadStatusTagType = (status: number | undefined) => {
  if (status === 0) return 'info'
  if (status === 1) return 'primary'
  if (status === 2) return 'success'
  if (status === 3) return 'danger'
  if (status === 4) return 'warning'
  return ''
}

const getLeadStatusName = (status: number | undefined) => {
  const names = ['新线索', '跟进中', '已转化', '已无效', '已回收']
  if (status !== undefined && status >= 0 && status <= 4) {
    return names[status]
  }
  return '-'
}

const fetchUsers = async () => {
  try {
    const res = await getUserList({ pageNum: 1, pageSize: 1000 })
    if (res.code === 0 && res.data?.records) {
      userList.value = res.data.records
    }
  } catch (error) {
    console.error('获取用户列表失败', error)
  }
}

const handleFromUserChange = async (userId: number) => {
  customerList.value = []
  leadList.value = []
  if (!userId) return

  loadingCustomer.value = true
  loadingLead.value = true
  try {
    const [customerRes, leadRes] = await Promise.all([
      getUserCustomers(userId),
      getUserLeads(userId)
    ])
    if (customerRes.code === 0 && customerRes.data) {
      customerList.value = customerRes.data
    }
    if (leadRes.code === 0 && leadRes.data) {
      leadList.value = leadRes.data
    }
  } catch (error) {
    console.error('获取用户资源失败', error)
    ElMessage.error('获取用户资源失败')
  } finally {
    loadingCustomer.value = false
    loadingLead.value = false
  }
}

const fetchRecords = async () => {
  loadingRecord.value = true
  try {
    const res = await getTransferRecordList({
      pageNum: recordPagination.pageNum,
      pageSize: recordPagination.pageSize,
      transferType: searchForm.transferType
    })
    if (res.code === 0) {
      recordList.value = res.data.records || []
      recordPagination.total = res.data.total || 0
    }
  } catch (error) {
    console.error('获取转移记录失败', error)
  } finally {
    loadingRecord.value = false
  }
}

const handleSearch = () => {
  recordPagination.pageNum = 1
  fetchRecords()
}

const handleRecordReset = () => {
  searchForm.transferType = undefined
  recordPagination.pageNum = 1
  fetchRecords()
}

const handleReset = () => {
  transferForm.fromUserId = undefined
  transferForm.toUserId = undefined
  transferForm.transferMethod = 1
  transferForm.reason = '员工离职，资源转移'
  customerList.value = []
  leadList.value = []
  transferFormRef.value?.resetFields()
}

const handleTransfer = async () => {
  if (!transferFormRef.value) return
  await transferFormRef.value.validate(async (valid) => {
    if (valid) {
      const total = customerList.value.length + leadList.value.length
      if (total === 0) {
        ElMessage.warning('该用户没有可转移的资源')
        return
      }

      ElMessageBox.confirm(
        `确定要转移该用户的资源吗？将转移 ${customerList.value.length} 个客户和 ${leadList.value.length} 条线索。`,
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(async () => {
        transferring.value = true
        try {
          const res = await resignTransfer(
            transferForm.fromUserId!,
            transferForm.toUserId,
            transferForm.reason
          )
          if (res.code === 0) {
            ElMessage.success('转移成功')
            handleReset()
            fetchRecords()
          } else {
            ElMessage.error(res.message || '转移失败')
          }
        } catch (error) {
          console.error('转移失败', error)
          ElMessage.error('转移失败')
        } finally {
          transferring.value = false
        }
      })
    }
  })
}

const handleViewDetail = async (row: TransferRecordVO) => {
  try {
    const res = await getTransferRecord(row.id)
    if (res.code === 0 && res.data?.details) {
      detailList.value = res.data.details
      detailDialogVisible.value = true
    }
  } catch (error) {
    console.error('获取详情失败', error)
    ElMessage.error('获取详情失败')
  }
}

onMounted(() => {
  fetchUsers()
  fetchRecords()
})
</script>

<style scoped>
.resource-transfer {
  min-height: 100%;
}

.search-form {
  margin-bottom: 20px;
}

.form-label {
  color: #303133;
  font-weight: 500;
}

.resource-preview {
  width: 100%;
}

.preview-card {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
