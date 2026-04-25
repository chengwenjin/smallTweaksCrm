<template>
  <div class="private-sea-config">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>私海配置管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增配置
          </el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="配置类型">
          <el-select v-model="searchForm.configType" placeholder="请选择配置类型" clearable style="width: 140px">
            <el-option label="全局配置" :value="1" />
            <el-option label="角色配置" :value="2" />
            <el-option label="用户配置" :value="3" />
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
        <el-table-column prop="configTypeName" label="配置类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getConfigTypeTagType(row.configType)" size="small">
              {{ row.configTypeName || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="roleName" label="角色" width="120">
          <template #default="{ row }">
            {{ row.roleName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="用户" width="120">
          <template #default="{ row }">
            {{ row.userName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="maxCustomerCount" label="客户上限" width="100" />
        <el-table-column prop="maxLeadCount" label="线索上限" width="100" />
        <el-table-column prop="autoRecycleDays" label="自动回收天数" width="120" />
        <el-table-column prop="isEnabledName" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'" size="small">
              {{ row.isEnabledName || '-' }}
            </el-tag>
          </template>
        </el-table-column>
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
        label-width="120px"
      >
        <el-form-item label="配置类型" prop="configType">
          <el-select v-model="form.configType" placeholder="请选择配置类型" style="width: 100%">
            <el-option label="全局配置" :value="1" />
            <el-option label="角色配置" :value="2" />
            <el-option label="用户配置" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="客户数量上限" prop="maxCustomerCount">
          <el-input-number v-model="form.maxCustomerCount" :min="1" :max="10000" style="width: 200px" />
          <span class="form-tip">（0表示不限制）</span>
        </el-form-item>
        <el-form-item label="线索数量上限" prop="maxLeadCount">
          <el-input-number v-model="form.maxLeadCount" :min="1" :max="10000" style="width: 200px" />
          <span class="form-tip">（0表示不限制）</span>
        </el-form-item>
        <el-form-item label="自动回收天数" prop="autoRecycleDays">
          <el-input-number v-model="form.autoRecycleDays" :min="1" :max="365" style="width: 200px" />
          <span class="form-tip">（超过该天数未跟进自动回收到公海）</span>
        </el-form-item>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-radio-group v-model="form.isEnabled">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
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
  getPrivateSeaConfigList,
  getPrivateSeaConfig,
  createPrivateSeaConfig,
  updatePrivateSeaConfig,
  deletePrivateSeaConfig,
  type PrivateSeaConfigVO
} from '@/api/resourceSecurity'

const loading = ref(false)
const tableData = ref<PrivateSeaConfigVO[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = ref('新增配置')
const formRef = ref<FormInstance>()

const searchForm = reactive({
  configType: undefined as number | undefined,
  isEnabled: undefined as number | undefined
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const form = reactive({
  id: undefined as number | undefined,
  configType: 1,
  maxCustomerCount: 50,
  maxLeadCount: 100,
  autoRecycleDays: 30,
  isEnabled: 1,
  description: ''
})

const rules: FormRules = {
  configType: [{ required: true, message: '请选择配置类型', trigger: 'change' }],
  maxCustomerCount: [{ required: true, message: '请输入客户数量上限', trigger: 'blur' }],
  maxLeadCount: [{ required: true, message: '请输入线索数量上限', trigger: 'blur' }],
  autoRecycleDays: [{ required: true, message: '请输入自动回收天数', trigger: 'blur' }]
}

const getConfigTypeTagType = (type: number | undefined) => {
  if (type === 1) return 'primary'
  if (type === 2) return 'success'
  if (type === 3) return 'warning'
  return ''
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getPrivateSeaConfigList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      configType: searchForm.configType,
      isEnabled: searchForm.isEnabled
    })
    if (res.code === 0) {
      tableData.value = res.data.records || []
      pagination.total = res.data.total || 0
    } else {
      ElMessage.error(res.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取私海配置列表失败', error)
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
  searchForm.configType = undefined
  searchForm.isEnabled = undefined
  pagination.pageNum = 1
  fetchData()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增配置'
  form.id = undefined
  form.configType = 1
  form.maxCustomerCount = 50
  form.maxLeadCount = 100
  form.autoRecycleDays = 30
  form.isEnabled = 1
  form.description = ''
  dialogVisible.value = true
}

const handleEdit = async (row: PrivateSeaConfigVO) => {
  isEdit.value = true
  dialogTitle.value = '编辑配置'
  try {
    const res = await getPrivateSeaConfig(row.id)
    if (res.code === 0 && res.data) {
      form.id = res.data.id
      form.configType = res.data.configType || 1
      form.maxCustomerCount = res.data.maxCustomerCount || 50
      form.maxLeadCount = res.data.maxLeadCount || 100
      form.autoRecycleDays = res.data.autoRecycleDays || 30
      form.isEnabled = res.data.isEnabled !== undefined ? res.data.isEnabled : 1
      form.description = res.data.description || ''
      dialogVisible.value = true
    } else {
      ElMessage.error(res.message || '获取配置详情失败')
    }
  } catch (error) {
    console.error('获取配置详情失败', error)
    ElMessage.error('获取配置详情失败')
  }
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
          res = await updatePrivateSeaConfig({
            id: form.id,
            configType: form.configType,
            maxCustomerCount: form.maxCustomerCount,
            maxLeadCount: form.maxLeadCount,
            autoRecycleDays: form.autoRecycleDays,
            isEnabled: form.isEnabled,
            description: form.description
          })
        } else {
          res = await createPrivateSeaConfig({
            configType: form.configType,
            maxCustomerCount: form.maxCustomerCount,
            maxLeadCount: form.maxLeadCount,
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

const handleDelete = (row: PrivateSeaConfigVO) => {
  ElMessageBox.confirm('确定要删除该配置吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deletePrivateSeaConfig(row.id)
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
.private-sea-config {
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
