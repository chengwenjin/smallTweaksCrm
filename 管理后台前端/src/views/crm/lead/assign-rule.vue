<template>
  <div class="assign-rule-management">
    <el-card>
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="规则名称">
          <el-input v-model="searchForm.ruleName" placeholder="请输入规则名称" clearable />
        </el-form-item>
        <el-form-item label="规则类型">
          <el-select v-model="searchForm.ruleType" placeholder="请选择规则类型" clearable style="width: 140px">
            <el-option label="地域规则" :value="1" />
            <el-option label="行业规则" :value="2" />
            <el-option label="规模规则" :value="3" />
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

      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增规则
        </el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="ruleName" label="规则名称" min-width="180">
          <template #default="{ row }">
            <div :title="row.ruleName">{{ row.ruleName }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="ruleTypeName" label="规则类型" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ row.ruleTypeName || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="province" label="地域" width="120">
          <template #default="{ row }">
            {{ row.province || '-' }}{{ row.city ? '/' + row.city : '' }}
          </template>
        </el-table-column>
        <el-table-column prop="industry" label="行业" width="120">
          <template #default="{ row }">
            {{ row.industry || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="minEmployeeCount" label="规模范围" width="150">
          <template #default="{ row }">
            <span v-if="row.minEmployeeCount || row.maxEmployeeCount">
              {{ row.minEmployeeCount || 0 }} - {{ row.maxEmployeeCount || '不限' }}人
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="assignUserName" label="分配目标" width="100">
          <template #default="{ row }">
            {{ row.assignUserName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80">
          <template #default="{ row }">
            <el-tag size="small" :type="getPriorityTagType(row.priority)">
              {{ row.priority || 100 }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template #default="{ row }">
            <el-tag size="small" :type="row.isEnabled === 1 ? 'success' : 'danger'">
              {{ row.isEnabled === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
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
      width="800px"
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
            <el-form-item label="规则名称" prop="ruleName">
              <el-input v-model="form.ruleName" placeholder="请输入规则名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规则类型" prop="ruleType">
              <el-select v-model="form.ruleType" placeholder="请选择规则类型" style="width: 100%">
                <el-option label="地域规则" :value="1" />
                <el-option label="行业规则" :value="2" />
                <el-option label="规模规则" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">地域条件</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="省份">
              <el-select v-model="form.province" placeholder="请选择省份" clearable style="width: 100%" filterable
                @change="handleProvinceChange">
                <el-option
                  v-for="item in provinceList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="城市">
              <el-select v-model="form.city" placeholder="请选择城市" clearable style="width: 100%" filterable
                @change="handleCityChange">
                <el-option
                  v-for="item in cityList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="区县">
              <el-select v-model="form.district" placeholder="请选择区县" clearable style="width: 100%" filterable>
                <el-option
                  v-for="item in districtList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">行业条件</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="行业">
              <el-select v-model="form.industry" placeholder="请选择行业" clearable style="width: 100%" filterable allow-create>
                <el-option
                  v-for="item in industryList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">规模条件</el-divider>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="最小员工数">
              <el-input-number v-model="form.minEmployeeCount" :min="0" :controls="false" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="最大员工数">
              <el-input-number v-model="form.maxEmployeeCount" :min="0" :controls="false" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="最小年营收">
              <el-input-number v-model="form.minAnnualRevenue" :min="0" :precision="2" :controls="false" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="最大年营收">
              <el-input-number v-model="form.maxAnnualRevenue" :min="0" :precision="2" :controls="false" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">分配目标</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="分配用户" prop="assignUserId">
              <el-select
                v-model="form.assignUserId"
                placeholder="请选择分配目标用户"
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
          <el-col :span="8">
            <el-form-item label="优先级">
              <el-input-number v-model="form.priority" :min="1" :max="100" :controls="false" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态">
              <div style="display: flex; align-items: center; height: 40px;">
                <el-switch
                  v-model="form.isEnabled"
                  :active-value="1"
                  :inactive-value="0"
                />
                <span style="margin-left: 8px; font-size: 14px;">
                  {{ form.isEnabled === 1 ? '启用' : '禁用' }}
                </span>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="规则描述">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="3"
                placeholder="请输入规则描述"
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
  getAssignRuleList,
  createAssignRule,
  updateAssignRule,
  deleteAssignRule
} from '@/api/assignRule'
import { getUserList } from '@/api/user'
import { provinceList, getCityList, getDistrictList, industryList } from '@/data/region'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const userList = ref<any[]>([])

const cityList = ref<any[]>([])
const districtList = ref<any[]>([])

const searchForm = reactive({
  ruleName: '',
  ruleType: null as number | null,
  isEnabled: null as number | null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref<any[]>([])

const form = reactive({
  id: null as number | null,
  ruleName: '',
  ruleType: 1,
  province: '',
  city: '',
  district: '',
  industry: '',
  minEmployeeCount: null as number | null,
  maxEmployeeCount: null as number | null,
  minAnnualRevenue: null as number | null,
  maxAnnualRevenue: null as number | null,
  assignUserId: null as number | null,
  priority: 100,
  isEnabled: 1,
  description: ''
})

const rules: FormRules = {
  ruleName: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
  ruleType: [{ required: true, message: '请选择规则类型', trigger: 'change' }],
  assignUserId: [{ required: true, message: '请选择分配目标用户', trigger: 'change' }]
}

const getPriorityTagType = (priority: number) => {
  if (priority <= 10) return 'danger'
  if (priority <= 30) return 'warning'
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
    const res = await getAssignRuleList(params)
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const fetchUsers = async () => {
  try {
    const params = { pageNum: 1, pageSize: 1000 }
    const res = await getUserList(params)
    userList.value = res.data.records
  } catch (error) {
    console.error(error)
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

const handleReset = () => {
  searchForm.ruleName = ''
  searchForm.ruleType = null
  searchForm.isEnabled = null
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增分配规则'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑分配规则'
  const newProvince = row.province || ''
  const newCity = row.city || ''
  const newDistrict = row.district || ''
  
  Object.assign(form, {
    id: row.id,
    ruleName: row.ruleName,
    ruleType: row.ruleType || 1,
    province: newProvince,
    city: newCity,
    district: newDistrict,
    industry: row.industry || '',
    minEmployeeCount: row.minEmployeeCount,
    maxEmployeeCount: row.maxEmployeeCount,
    minAnnualRevenue: row.minAnnualRevenue,
    maxAnnualRevenue: row.maxAnnualRevenue,
    assignUserId: row.assignUserId,
    priority: row.priority || 100,
    isEnabled: row.isEnabled ?? 1,
    description: row.description || ''
  })
  
  if (newProvince) {
    cityList.value = getCityList(newProvince)
    if (newCity) {
      districtList.value = getDistrictList(newProvince, newCity)
    } else {
      districtList.value = []
    }
  } else {
    cityList.value = []
    districtList.value = []
  }
  
  dialogVisible.value = true
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该规则吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteAssignRule(row.id)
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
        if (form.id) {
          await updateAssignRule(form.id, form as any)
          ElMessage.success('更新成功')
        } else {
          await createAssignRule(form as any)
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
    ruleName: '',
    ruleType: 1,
    province: '',
    city: '',
    district: '',
    industry: '',
    minEmployeeCount: null,
    maxEmployeeCount: null,
    minAnnualRevenue: null,
    maxAnnualRevenue: null,
    assignUserId: null,
    priority: 100,
    isEnabled: 1,
    description: ''
  })
  cityList.value = []
  districtList.value = []
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  resetForm()
}

const handleProvinceChange = (val: string) => {
  form.city = ''
  form.district = ''
  districtList.value = []
  if (val) {
    cityList.value = getCityList(val)
  } else {
    cityList.value = []
  }
}

const handleCityChange = (val: string) => {
  form.district = ''
  if (form.province && val) {
    districtList.value = getDistrictList(form.province, val)
  } else {
    districtList.value = []
  }
}

onMounted(() => {
  fetchData()
  fetchUsers()
})
</script>

<style scoped>
.assign-rule-management {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;
}
</style>
