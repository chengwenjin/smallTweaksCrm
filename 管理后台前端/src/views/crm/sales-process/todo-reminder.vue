<template>
  <div class="todo-reminder-management">
    <el-card>
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="全部待办" name="all">
          <div class="search-section">
            <el-form :inline="true" :model="searchForm" class="search-form">
              <el-form-item label="状态">
                <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
                  <el-option
                    v-for="item in STATUS_OPTIONS"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="优先级">
                <el-select v-model="searchForm.priority" placeholder="请选择优先级" clearable style="width: 100px">
                  <el-option
                    v-for="item in PRIORITY_OPTIONS"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="提醒时间">
                <el-date-picker
                  v-model="searchForm.remindTimeRange"
                  type="datetimerange"
                  range-separator="至"
                  start-placeholder="开始时间"
                  end-placeholder="结束时间"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  style="width: 320px"
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
          </div>
        </el-tab-pane>
        <el-tab-pane label="待处理" name="pending">
          <div class="pending-summary">
            <el-statistic title="待处理任务数" :value="pendingCount">
              <template #suffix>
                <span style="font-size: 16px; color: #909399">个</span>
              </template>
            </el-statistic>
          </div>
        </el-tab-pane>
      </el-tabs>

      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增待办
        </el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%" row-key="id">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="title" label="标题" min-width="180">
          <template #default="{ row }">
            <div class="title-cell">
              <el-tag :type="getPriorityTagType(row.priority)" size="small" class="mr-1">
                {{ getPriorityName(row.priority) }}
              </el-tag>
              <span>{{ row.title }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="内容" min-width="150">
          <template #default="{ row }">
            <span v-if="row.content" :title="row.content">
              {{ row.content.length > 30 ? row.content.substring(0, 30) + '...' : row.content }}
            </span>
            <span v-else class="text-gray-400">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)" size="small">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remindTime" label="提醒时间" width="160" />
        <el-table-column prop="endTime" label="截止时间" width="160" />
        <el-table-column prop="businessName" label="关联业务" width="120">
          <template #default="{ row }">
            <span v-if="row.businessName">{{ row.businessName }}</span>
            <span v-else class="text-gray-400">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="isRecurring" label="重复" width="60">
          <template #default="{ row }">
            <el-tag v-if="row.isRecurring === 1" type="info" size="small">是</el-tag>
            <span v-else class="text-gray-400">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">详情</el-button>
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <template v-if="row.status === 0">
              <el-button link type="success" size="small" @click="handleComplete(row)">完成</el-button>
              <el-button link type="info" size="small" @click="handleCancel(row)">取消</el-button>
            </template>
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
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="100px"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入待办标题" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="3"
            placeholder="请输入待办内容"
          />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="优先级">
              <el-select v-model="form.priority" style="width: 100%">
                <el-option
                  v-for="item in PRIORITY_OPTIONS"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提醒方式">
              <el-select v-model="form.remindType" style="width: 100%">
                <el-option
                  v-for="item in REMIND_TYPE_OPTIONS"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="提醒时间">
              <el-date-picker
                v-model="form.remindTime"
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
                v-model="form.endTime"
                type="datetime"
                placeholder="选择截止时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider>关联信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="业务类型">
              <el-select v-model="form.businessType" placeholder="请选择" clearable style="width: 100%">
                <el-option label="客户" value="customer" />
                <el-option label="线索" value="lead" />
                <el-option label="商机" value="opportunity" />
                <el-option label="跟进" value="follow" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务ID">
              <el-input-number v-model="form.businessId" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="业务名称">
          <el-input v-model="form.businessName" placeholder="请输入业务名称" />
        </el-form-item>

        <el-divider>重复设置</el-divider>
        <el-form-item label="是否重复">
          <el-switch v-model="form.isRecurring" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <template v-if="form.isRecurring === 1">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="重复类型">
                <el-select v-model="form.recurringType" placeholder="请选择" style="width: 100%">
                  <el-option
                    v-for="item in RECURRING_TYPE_OPTIONS"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </template>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="detailDialogVisible"
      title="待办详情"
      width="500px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="待办编号">{{ currentTodo?.todoNo }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTagType(currentTodo?.status)" size="small">
            {{ getStatusName(currentTodo?.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="优先级">
          <el-tag :type="getPriorityTagType(currentTodo?.priority)" size="small">
            {{ getPriorityName(currentTodo?.priority) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="提醒方式">{{ currentTodo?.remindTypeName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="标题" :span="2">{{ currentTodo?.title }}</el-descriptions-item>
        <el-descriptions-item label="内容" :span="2">
          <div v-if="currentTodo?.content" style="white-space: pre-wrap">{{ currentTodo.content }}</div>
          <span v-else class="text-gray-400">-</span>
        </el-descriptions-item>
        <el-descriptions-item label="提醒时间">{{ currentTodo?.remindTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="截止时间">{{ currentTodo?.endTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="关联业务">{{ currentTodo?.businessName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="是否重复">
          <el-tag v-if="currentTodo?.isRecurring === 1" type="info" size="small">是</el-tag>
          <span v-else class="text-gray-400">-</span>
        </el-descriptions-item>
        <el-descriptions-item label="完成时间" v-if="currentTodo?.completeTime">
          {{ currentTodo.completeTime }}
        </el-descriptions-item>
        <el-descriptions-item label="完成备注" v-if="currentTodo?.completeRemark">
          {{ currentTodo.completeRemark }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ currentTodo?.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog
      v-model="completeDialogVisible"
      title="完成待办"
      width="400px"
    >
      <el-form :model="completeForm" label-width="80px">
        <el-form-item label="完成备注">
          <el-input
            v-model="completeForm.completeRemark"
            type="textarea"
            :rows="3"
            placeholder="请输入完成备注（可选）"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="completeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitComplete" :loading="completeLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  getTodoList,
  getPendingTodos,
  getTodoDetail,
  createTodo,
  updateTodo,
  completeTodo,
  cancelTodo,
  deleteTodo,
  PRIORITY_OPTIONS,
  STATUS_OPTIONS,
  REMIND_TYPE_OPTIONS,
  RECURRING_TYPE_OPTIONS,
  type TodoReminderVO
} from '@/api/todoReminder'

const loading = ref(false)
const submitLoading = ref(false)
const completeLoading = ref(false)
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const completeDialogVisible = ref(false)
const dialogTitle = ref('新增待办')
const formRef = ref<FormInstance>()
const activeTab = ref('all')
const pendingCount = ref(0)

const searchForm = reactive({
  status: null as number | null,
  priority: null as number | null,
  remindTimeRange: [] as string[]
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref<TodoReminderVO[]>([])
const currentTodo = ref<TodoReminderVO | null>(null)
const currentTodoId = ref<number | null>(null)

const form = reactive({
  id: null as number | null,
  title: '',
  content: '',
  priority: 2,
  remindTime: '',
  endTime: '',
  businessType: '',
  businessId: null as number | null,
  businessName: '',
  remindType: 1,
  isRecurring: 0,
  recurringType: ''
})

const completeForm = reactive({
  completeRemark: ''
})

const rules: FormRules = {
  title: [{ required: true, message: '请输入待办标题', trigger: 'blur' }]
}

const getPriorityName = (priority?: number) => {
  const item = PRIORITY_OPTIONS.find(o => o.value === priority)
  return item?.label || '-'
}

const getPriorityTagType = (priority?: number) => {
  const item = PRIORITY_OPTIONS.find(o => o.value === priority)
  return item?.type || 'info'
}

const getStatusName = (status?: number) => {
  const item = STATUS_OPTIONS.find(o => o.value === status)
  return item?.label || '-'
}

const getStatusTagType = (status?: number) => {
  const item = STATUS_OPTIONS.find(o => o.value === status)
  return item?.type || 'info'
}

const fetchData = async () => {
  loading.value = true
  try {
    const params: any = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    if (activeTab.value === 'pending') {
      params.status = 0
    } else {
      if (searchForm.status !== null) params.status = searchForm.status
    }
    if (searchForm.priority !== null) params.priority = searchForm.priority
    if (searchForm.remindTimeRange && searchForm.remindTimeRange.length === 2) {
      params.remindTimeStart = searchForm.remindTimeRange[0]
      params.remindTimeEnd = searchForm.remindTimeRange[1]
    }

    const res = await getTodoList(params)
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const fetchPendingCount = async () => {
  try {
    const res = await getPendingTodos()
    pendingCount.value = res.data.length
  } catch (error) {
    console.error(error)
  }
}

const handleTabChange = () => {
  pagination.pageNum = 1
  fetchData()
}

const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

const handleReset = () => {
  searchForm.status = null
  searchForm.priority = null
  searchForm.remindTimeRange = []
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增待办'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row: TodoReminderVO) => {
  dialogTitle.value = '编辑待办'
  resetForm()
  
  form.id = row.id
  form.title = row.title || ''
  form.content = row.content || ''
  form.priority = row.priority || 2
  form.remindTime = row.remindTime || ''
  form.endTime = row.endTime || ''
  form.businessType = row.businessType || ''
  form.businessId = row.businessId || null
  form.businessName = row.businessName || ''
  form.remindType = row.remindType || 1
  form.isRecurring = row.isRecurring || 0
  form.recurringType = row.recurringType || ''
  
  dialogVisible.value = true
}

const handleView = async (row: TodoReminderVO) => {
  try {
    const res = await getTodoDetail(row.id!)
    currentTodo.value = res.data
    detailDialogVisible.value = true
  } catch (error) {
    console.error(error)
  }
}

const handleComplete = (row: TodoReminderVO) => {
  currentTodoId.value = row.id
  completeForm.completeRemark = ''
  completeDialogVisible.value = true
}

const submitComplete = async () => {
  if (!currentTodoId.value) return
  
  completeLoading.value = true
  try {
    await completeTodo(currentTodoId.value, completeForm)
    ElMessage.success('完成成功')
    completeDialogVisible.value = false
    fetchData()
    fetchPendingCount()
  } catch (error) {
    console.error(error)
  } finally {
    completeLoading.value = false
  }
}

const handleCancel = (row: TodoReminderVO) => {
  ElMessageBox.confirm('确定要取消该待办吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await cancelTodo(row.id!)
      ElMessage.success('取消成功')
      fetchData()
      fetchPendingCount()
    } catch (error) {
      console.error(error)
    }
  })
}

const handleDelete = (row: TodoReminderVO) => {
  ElMessageBox.confirm('确定要删除该待办吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteTodo(row.id!)
      ElMessage.success('删除成功')
      fetchData()
      fetchPendingCount()
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
          title: form.title
        }
        if (form.content) data.content = form.content
        data.priority = form.priority
        if (form.remindTime) data.remindTime = form.remindTime
        if (form.endTime) data.endTime = form.endTime
        if (form.businessType) data.businessType = form.businessType
        if (form.businessId !== null) data.businessId = form.businessId
        if (form.businessName) data.businessName = form.businessName
        data.remindType = form.remindType
        data.isRecurring = form.isRecurring
        if (form.recurringType) data.recurringType = form.recurringType

        if (form.id) {
          await updateTodo(form.id, data)
          ElMessage.success('更新成功')
        } else {
          await createTodo(data)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
        fetchData()
        fetchPendingCount()
      } catch (error) {
        console.error(error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const resetForm = () => {
  form.id = null
  form.title = ''
  form.content = ''
  form.priority = 2
  form.remindTime = ''
  form.endTime = ''
  form.businessType = ''
  form.businessId = null
  form.businessName = ''
  form.remindType = 1
  form.isRecurring = 0
  form.recurringType = ''
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  resetForm()
}

onMounted(() => {
  fetchData()
  fetchPendingCount()
})
</script>

<style scoped>
.todo-reminder-management {
  padding: 20px;
}

.search-section {
  margin-bottom: 16px;
}

.search-form {
  margin-bottom: 0;
}

.toolbar {
  margin-bottom: 20px;
}

.title-cell {
  display: flex;
  align-items: center;
}

.mr-1 {
  margin-right: 4px;
}

.text-gray-400 {
  color: #909399;
}

.pending-summary {
  padding: 20px;
  text-align: center;
}
</style>
