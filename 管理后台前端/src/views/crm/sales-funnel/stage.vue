<template>
  <div class="stage-management">
    <el-card>
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="阶段名称">
          <el-input v-model="searchForm.stageName" placeholder="请输入阶段名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.isEnabled" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
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
          新增阶段
        </el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="stageCode" label="阶段编码" width="180" />
        <el-table-column prop="stageName" label="阶段名称" width="120">
          <template #default="{ row }">
            <div :title="row.stageName">{{ row.stageName }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="winProbability" label="赢率" width="100">
          <template #default="{ row }">
            <el-tag :type="getProbabilityTagType(row.winProbability)" size="small">
              {{ row.winProbability }}%
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isClosed" label="是否结束阶段" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isClosed === 1 ? 'danger' : 'info'" size="small">
              {{ row.isClosed === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="closeTypeName" label="结束类型" width="100">
          <template #default="{ row }">
            <el-tag
              :type="row.closeType === 1 ? 'success' : row.closeType === 2 ? 'danger' : 'info'"
              size="small"
            >
              {{ row.closeTypeName || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'" size="small">
              {{ row.isEnabled === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
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
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="阶段编码" prop="stageCode">
              <el-input v-model="form.stageCode" placeholder="请输入阶段编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="请输入阶段名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="排序" prop="sortOrder">
              <el-input-number v-model="form.sortOrder" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="赢率(%)" prop="winProbability">
              <el-input-number v-model="form.winProbability" :min="0" :max="100" :step="10" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否启用">
              <el-switch v-model="form.isEnabled" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否结束阶段">
              <el-switch v-model="form.isClosed" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12" v-if="form.isClosed === 1">
            <el-form-item label="结束类型">
              <el-select v-model="form.closeType" placeholder="请选择结束类型" style="width: 100%">
                <el-option label="赢单" :value="1" />
                <el-option label="输单" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="3"
                placeholder="请输入阶段描述"
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import {
  getSalesStageList,
  createSalesStage,
  updateSalesStage,
  deleteSalesStage,
  type SalesStageVO
} from '@/api/salesStage'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref<FormInstance>()
const currentRow = ref<SalesStageVO | null>(null)

const searchForm = reactive({
  stageName: '',
  isEnabled: null as number | null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref<SalesStageVO[]>([])

const form = reactive({
  id: null as number | null,
  stageCode: '',
  stageName: '',
  sortOrder: 0,
  winProbability: 0,
  description: '',
  isEnabled: 1,
  isClosed: 0,
  closeType: 0
})

const rules: FormRules = {
  stageCode: [{ required: true, message: '请输入阶段编码', trigger: 'blur' }],
  stageName: [{ required: true, message: '请输入阶段名称', trigger: 'blur' }],
  sortOrder: [{ required: true, message: '请输入排序', trigger: 'blur' }],
  winProbability: [{ required: true, message: '请输入赢率', trigger: 'blur' }]
}

const getProbabilityTagType = (probability: number) => {
  if (probability >= 70) return 'success'
  if (probability >= 40) return 'warning'
  return 'info'
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const res: any = await getSalesStageList(params)
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
  searchForm.stageName = ''
  searchForm.isEnabled = null
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增阶段'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row: SalesStageVO) => {
  dialogTitle.value = '编辑阶段'
  isEdit.value = true
  currentRow.value = row

  Object.assign(form, {
    id: row.id,
    stageCode: row.stageCode,
    stageName: row.stageName,
    sortOrder: row.sortOrder,
    winProbability: row.winProbability,
    description: row.description || '',
    isEnabled: row.isEnabled,
    isClosed: row.isClosed,
    closeType: row.closeType || 0
  })

  dialogVisible.value = true
}

const handleDelete = (row: SalesStageVO) => {
  ElMessageBox.confirm('确定要删除该阶段吗？系统预设阶段不可删除。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteSalesStage(row.id)
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
        if (isEdit.value) {
          await updateSalesStage(form as any)
          ElMessage.success('更新成功')
        } else {
          await createSalesStage(form as any)
          ElMessage.success('创建成功')
        }
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

const resetForm = () => {
  Object.assign(form, {
    id: null,
    stageCode: '',
    stageName: '',
    sortOrder: 0,
    winProbability: 0,
    description: '',
    isEnabled: 1,
    isClosed: 0,
    closeType: 0
  })
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
.stage-management {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;
}
</style>
