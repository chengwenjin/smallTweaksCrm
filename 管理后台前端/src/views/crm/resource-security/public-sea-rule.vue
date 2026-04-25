<template>
  <div class="public-sea-rule">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>公海规则管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增规则
          </el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="规则类型">
          <el-select v-model="searchForm.ruleType" placeholder="请选择规则类型" clearable style="width: 140px">
            <el-option label="先抢先得" :value="1" />
            <el-option label="定期轮换" :value="2" />
          </el-select>
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

      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="ruleName" label="规则名称" width="200" />
        <el-table-column prop="ruleTypeName" label="规则类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.ruleType === 1 ? 'primary' : 'warning'" size="small">
              {{ row.ruleTypeName || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="claimLimitPerDay" label="每日认领上限" width="120" />
        <el-table-column prop="claimLimitPerWeek" label="每周认领上限" width="120" />
        <el-table-column prop="autoRecycleDays" label="自动回收天数" width="120" />
        <el-table-column prop="isEnabledName" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'" size="small">
              {{ row.isEnabledName || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="success" size="small" @click="handleEnable(row)" v-if="row.isEnabled !== 1">启用</el-button>
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
      width="650px"
      @close="handleDialogClose"
    >
      <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="120px"
      >
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="form.ruleName" placeholder="请输入规则名称" />
        </el-form-item>
        <el-form-item label="规则类型" prop="ruleType">
          <el-radio-group v-model="form.ruleType">
            <el-radio :value="1">先抢先得</el-radio>
            <el-radio :value="2">定期轮换</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="轮换天数" v-if="form.ruleType === 2">
          <el-input-number v-model="form.rotateDays" :min="1" :max="365" style="width: 200px" />
          <span class="form-tip">（每隔多少天轮换一次）</span>
        </el-form-item>
        <el-form-item label="每日认领上限" prop="claimLimitPerDay">
          <el-input-number v-model="form.claimLimitPerDay" :min="1" :max="1000" style="width: 200px" />
        </el-form-item>
        <el-form-item label="每周认领上限" prop="claimLimitPerWeek">
          <el-input-number v-model="form.claimLimitPerWeek" :min="1" :max="5000" style="width: 200px" />
        </el-form-item>
        <el-form-item label="启用自动回收" prop="autoRecycleEnabled">
          <el-radio-group v-model="form.autoRecycleEnabled">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="自动回收天数" v-if="form.autoRecycleEnabled === 1">
          <el-input-number v-model="form.autoRecycleDays" :min="1" :max="365" style="width: 200px" />
          <span class="form-tip">（超过该天数未跟进自动回收到公海）</span>
        </el-form-item>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-radio-group v-model="form.isEnabled">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
          <span class="form-tip">（同一时间只能有一个规则处于启用状态）</span>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  getPublicSeaRuleList,
  getPublicSeaRule,
  createPublicSeaRule,
  updatePublicSeaRule,
  deletePublicSeaRule,
  type PublicSeaRuleVO
} from '@/api/resourceSecurity'

const loading = ref(false)
const tableData = ref<PublicSeaRuleVO[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = ref('新增规则')
const formRef = ref<FormInstance>()

const searchForm = reactive({
  ruleType: undefined as number | undefined,
  isEnabled: undefined as number | undefined
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const form = reactive({
  id: undefined as number | undefined,
  ruleName: '',
  ruleType: 1,
  rotateDays: 7,
  claimLimitPerDay: 10,
  claimLimitPerWeek: 50,
  autoRecycleEnabled: 1,
  autoRecycleDays: 30,
  isEnabled: 1,
  description: ''
})

const rules: FormRules = {
  ruleName: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
  ruleType: [{ required: true, message: '请选择规则类型', trigger: 'change' }],
  claimLimitPerDay: [{ required: true, message: '请输入每日认领上限', trigger: 'blur' }],
  claimLimitPerWeek: [{ required: true, message: '请输入每周认领上限', trigger: 'blur' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getPublicSeaRuleList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ruleType: searchForm.ruleType,
      isEnabled: searchForm.isEnabled
    })
    if (res.code === 0) {
      tableData.value = res.data.records || []
      pagination.total = res.data.total || 0
    } else {
      ElMessage.error(res.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取公海规则列表失败', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

const handleReset = () => {
  searchForm.ruleType = undefined
  searchForm.isEnabled = undefined
  pagination.pageNum = 1
  fetchData()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增规则'
  form.id = undefined
  form.ruleName = ''
  form.ruleType = 1
  form.rotateDays = 7
  form.claimLimitPerDay = 10
  form.claimLimitPerWeek = 50
  form.autoRecycleEnabled = 1
  form.autoRecycleDays = 30
  form.isEnabled = 1
  form.description = ''
  dialogVisible.value = true
}

const handleEdit = async (row: PublicSeaRuleVO) => {
  isEdit.value = true
  dialogTitle.value = '编辑规则'
  try {
    const res = await getPublicSeaRule(row.id)
    if (res.code === 0 && res.data) {
      form.id = res.data.id
      form.ruleName = res.data.ruleName || ''
      form.ruleType = res.data.ruleType || 1
      form.rotateDays = res.data.rotateDays || 7
      form.claimLimitPerDay = res.data.claimLimitPerDay || 10
      form.claimLimitPerWeek = res.data.claimLimitPerWeek || 50
      form.autoRecycleEnabled = res.data.autoRecycleEnabled !== undefined ? res.data.autoRecycleEnabled : 1
      form.autoRecycleDays = res.data.autoRecycleDays || 30
      form.isEnabled = res.data.isEnabled !== undefined ? res.data.isEnabled : 1
      form.description = res.data.description || ''
      dialogVisible.value = true
    } else {
      ElMessage.error(res.message || '获取规则详情失败')
    }
  } catch (error) {
    console.error('获取规则详情失败', error)
    ElMessage.error('获取规则详情失败')
  }
}

const handleEnable = (row: PublicSeaRuleVO) => {
  ElMessageBox.confirm('启用该规则后，其他规则将自动禁用，确定要启用吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await updatePublicSeaRule({
        id: row.id,
        isEnabled: 1
      })
      if (res.code === 0) {
        ElMessage.success('启用成功')
        fetchData()
      } else {
        ElMessage.error(res.message || '启用失败')
      }
    } catch (error) {
      console.error('启用失败', error)
      ElMessage.error('启用失败')
    }
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        let res
        if (isEdit.value && form.id) {
          res = await updatePublicSeaRule({
            id: form.id,
            ruleName: form.ruleName,
            ruleType: form.ruleType,
            rotateDays: form.rotateDays,
            claimLimitPerDay: form.claimLimitPerDay,
            claimLimitPerWeek: form.claimLimitPerWeek,
            autoRecycleEnabled: form.autoRecycleEnabled,
            autoRecycleDays: form.autoRecycleDays,
            isEnabled: form.isEnabled,
            description: form.description
          })
        } else {
          res = await createPublicSeaRule({
            ruleName: form.ruleName,
            ruleType: form.ruleType,
            rotateDays: form.rotateDays,
            claimLimitPerDay: form.claimLimitPerDay,
            claimLimitPerWeek: form.claimLimitPerWeek,
            autoRecycleEnabled: form.autoRecycleEnabled,
            autoRecycleDays: form.autoRecycleDays,
            isEnabled: form.isEnabled,
            description: form.description
          })
        }
        if (res.code === 0) {
          ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
          dialogVisible.value = false
          fetchData()
        } else {
          ElMessage.error(res.message || '操作失败')
        }
      } catch (error) {
        console.error('提交失败', error)
        ElMessage.error('操作失败')
      }
    }
  })
}

const handleDelete = (row: PublicSeaRuleVO) => {
  ElMessageBox.confirm('确定要删除该规则吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deletePublicSeaRule(row.id)
      if (res.code === 0) {
        ElMessage.success('删除成功')
        fetchData()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      console.error('删除失败', error)
      ElMessage.error('删除失败')
    }
  })
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.public-sea-rule {
  min-height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.form-tip {
  margin-left: 10px;
  color: #909399;
  font-size: 12px;
}
</style>
