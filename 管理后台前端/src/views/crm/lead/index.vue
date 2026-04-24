<template>
  <div class="lead-management">
    <el-card>
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="线索名称">
          <el-input v-model="searchForm.leadName" placeholder="请输入线索名称" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="searchForm.contactMobile" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item label="来源">
          <el-select v-model="searchForm.sourceId" placeholder="请选择来源" clearable style="width: 140px">
            <el-option
              v-for="source in sourceList"
              :key="source.id"
              :label="source.sourceName"
              :value="source.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="新线索" :value="0" />
            <el-option label="跟进中" :value="1" />
            <el-option label="已转化" :value="2" />
            <el-option label="已无效" :value="3" />
            <el-option label="已回收" :value="4" />
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
          新增线索
        </el-button>
        <el-upload
          class="import-upload"
          :action="uploadAction"
          :headers="uploadHeaders"
          :show-file-list="false"
          :on-success="handleImportSuccess"
          :on-error="handleImportError"
          :before-upload="beforeUpload"
          accept=".xlsx,.xls"
        >
          <el-button type="success">
            <el-icon><Upload /></el-icon>
            Excel导入
          </el-button>
        </el-upload>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="leadNo" label="线索编号" width="200" />
        <el-table-column prop="leadName" label="线索名称" width="150">
          <template #default="{ row }">
            <div :title="row.leadName">{{ row.leadName }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="contactName" label="联系人" width="100" />
        <el-table-column prop="contactMobile" label="手机号" width="130" />
        <el-table-column prop="sourceName" label="来源" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ row.sourceName || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="levelName" label="等级" width="80">
          <template #default="{ row }">
            <el-tag
              :type="getLevelTagType(row.level)"
              size="small"
            >
              {{ row.levelName || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="statusName" label="状态" width="90">
          <template #default="{ row }">
            <el-tag
              :type="getStatusTagType(row.status)"
              size="small"
            >
              {{ row.statusName || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assignUserName" label="负责人" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="success" size="small" @click="handleStatusChange(row)">状态</el-button>
            <el-button link type="warning" size="small" @click="handleAssign(row)">分配</el-button>
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
            <el-form-item label="线索名称" prop="leadName">
              <el-input v-model="form.leadName" placeholder="请输入线索名称" clearable>
                <template #append>
                  <el-button @click="checkLeadNameDuplicate">查重</el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人">
              <el-input v-model="form.contactName" placeholder="请输入联系人姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号">
              <el-input v-model="form.contactMobile" placeholder="请输入手机号" clearable>
                <template #append>
                  <el-button @click="checkMobileDuplicate">查重</el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱">
              <el-input v-model="form.contactEmail" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="线索来源" prop="sourceId">
              <el-select v-model="form.sourceId" placeholder="请选择来源" style="width: 100%">
                <el-option
                  v-for="source in sourceList"
                  :key="source.id"
                  :label="source.sourceName"
                  :value="source.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="线索等级" prop="level">
              <el-select v-model="form.level" placeholder="请选择等级" style="width: 100%">
                <el-option label="高" :value="1" />
                <el-option label="中" :value="2" />
                <el-option label="低" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12" v-if="isEdit">
            <el-form-item label="线索状态">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="新线索" :value="0" />
                <el-option label="跟进中" :value="1" />
                <el-option label="已转化" :value="2" />
                <el-option label="已无效" :value="3" />
                <el-option label="已回收" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="行业">
              <el-input v-model="form.industry" placeholder="所属行业" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="需求描述">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="3"
                placeholder="请输入需求描述"
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
      v-model="statusDialogVisible"
      title="更新状态"
      width="400px"
    >
      <el-form :model="statusForm" label-width="80px">
        <el-form-item label="状态">
          <el-select v-model="statusForm.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="新线索" :value="0" />
            <el-option label="跟进中" :value="1" />
            <el-option label="已转化" :value="2" />
            <el-option label="已无效" :value="3" />
            <el-option label="已回收" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="statusForm.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="statusDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitStatus" :loading="statusSubmitLoading">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="assignDialogVisible"
      title="分配线索"
      width="400px"
    >
      <el-form :model="assignForm" label-width="80px">
        <el-form-item label="负责人">
          <el-select
            v-model="assignForm.assignUserId"
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
        <el-form-item label="备注">
          <el-input
            v-model="assignForm.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitAssign" :loading="assignSubmitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import {
  getLeadList,
  createLead,
  updateLead,
  deleteLead,
  updateLeadStatus,
  assignLead,
  getLeadSources
} from '@/api/lead'
import { getUserList } from '@/api/user'
import { checkDuplicate } from '@/api/leadFollow'

const loading = ref(false)
const submitLoading = ref(false)
const statusSubmitLoading = ref(false)
const assignSubmitLoading = ref(false)
const dialogVisible = ref(false)
const statusDialogVisible = ref(false)
const assignDialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref<FormInstance>()
const sourceList = ref<any[]>([])
const userList = ref<any[]>([])
const currentRow = ref<any>(null)

const searchForm = reactive({
  leadName: '',
  contactMobile: '',
  sourceId: null as number | null,
  status: null as number | null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref<any[]>([])

const form = reactive({
  id: null as number | null,
  leadName: '',
  contactName: '',
  contactMobile: '',
  contactEmail: '',
  industry: '',
  sourceId: null as number | null,
  level: 3,
  status: 0,
  description: '',
  remark: '',
  tags: ''
})

const statusForm = reactive({
  status: 0,
  remark: ''
})

const assignForm = reactive({
  assignUserId: null as number | null,
  remark: ''
})

const rules: FormRules = {
  leadName: [{ required: true, message: '请输入线索名称', trigger: 'blur' }],
  sourceId: [{ required: true, message: '请选择线索来源', trigger: 'change' }],
  level: [{ required: true, message: '请选择线索等级', trigger: 'change' }]
}

const uploadAction = '/api/crm/leads/import'

const uploadHeaders = computed(() => {
  const token = localStorage.getItem('accessToken')
  return {
    Authorization: token ? `Bearer ${token}` : ''
  }
})

const getLevelTagType = (level: number) => {
  if (level === 1) return 'danger'
  if (level === 2) return 'warning'
  return 'info'
}

const getStatusTagType = (status: number) => {
  if (status === 0) return 'primary'
  if (status === 1) return 'warning'
  if (status === 2) return 'success'
  if (status === 3) return 'danger'
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
    const res = await getLeadList(params)
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const fetchSources = async () => {
  try {
    const res = await getLeadSources()
    sourceList.value = res.data
  } catch (error) {
    console.error(error)
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
  searchForm.leadName = ''
  searchForm.contactMobile = ''
  searchForm.sourceId = null
  searchForm.status = null
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增线索'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const checkMobileDuplicate = async () => {
  if (!form.contactMobile || form.contactMobile.trim() === '') {
    ElMessage.warning('请输入手机号后再查重')
    return
  }
  try {
    const params: any = {
      contactMobile: form.contactMobile,
      checkType: 2
    }
    if (form.id) {
      params.leadId = form.id
    }
    const res = await checkDuplicate(params)
    if (res.data && res.data.isDuplicate) {
      const duplicateList = res.data.duplicateLeads || []
      const message = duplicateList.map((item: any) => {
        return `线索编号: ${item.leadNo}, 线索名称: ${item.leadName}`
      }).join('\n')
      ElMessage.warning(`手机号已存在重复数据:\n${message}`)
    } else {
      ElMessage.success('该手机号无重复数据')
    }
  } catch (error: any) {
    console.error(error)
    ElMessage.error(error.message || '查重失败')
  }
}

const checkLeadNameDuplicate = async () => {
  if (!form.leadName || form.leadName.trim() === '') {
    ElMessage.warning('请输入线索名称后再查重')
    return
  }
  try {
    const params: any = {
      leadName: form.leadName,
      checkType: 1
    }
    if (form.id) {
      params.leadId = form.id
    }
    const res = await checkDuplicate(params)
    if (res.data && res.data.isDuplicate) {
      const duplicateList = res.data.duplicateLeads || []
      const message = duplicateList.map((item: any) => {
        return `线索编号: ${item.leadNo}, 手机号: ${item.contactMobile}`
      }).join('\n')
      ElMessage.warning(`企业名称已存在重复数据:\n${message}`)
    } else {
      ElMessage.success('该企业名称无重复数据')
    }
  } catch (error: any) {
    console.error(error)
    ElMessage.error(error.message || '查重失败')
  }
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑线索'
  isEdit.value = true
  currentRow.value = row

  Object.assign(form, {
    id: row.id,
    leadName: row.leadName,
    contactName: row.contactName,
    contactMobile: row.contactMobile,
    contactEmail: row.contactEmail,
    industry: row.industry,
    sourceId: row.sourceId,
    level: row.level,
    status: row.status,
    description: row.description,
    remark: row.remark,
    tags: row.tags
  })

  dialogVisible.value = true
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该线索吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteLead(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error(error)
    }
  })
}

const handleStatusChange = (row: any) => {
  currentRow.value = row
  statusForm.status = row.status
  statusForm.remark = ''
  statusDialogVisible.value = true
}

const handleAssign = (row: any) => {
  currentRow.value = row
  assignForm.assignUserId = row.assignUserId
  assignForm.remark = ''
  assignDialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) {
          await updateLead(form.id!, form as any)
          ElMessage.success('更新成功')
        } else {
          await createLead(form as any)
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

const handleSubmitStatus = async () => {
  statusSubmitLoading.value = true
  try {
    await updateLeadStatus(currentRow.value.id, {
      status: statusForm.status,
      remark: statusForm.remark
    })
    ElMessage.success('状态更新成功')
    statusDialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error(error)
  } finally {
    statusSubmitLoading.value = false
  }
}

const handleSubmitAssign = async () => {
  if (!assignForm.assignUserId) {
    ElMessage.warning('请选择负责人')
    return
  }

  assignSubmitLoading.value = true
  try {
    await assignLead(currentRow.value.id, {
      assignUserId: assignForm.assignUserId,
      remark: assignForm.remark
    })
    ElMessage.success('分配成功')
    assignDialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error(error)
  } finally {
    assignSubmitLoading.value = false
  }
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    leadName: '',
    contactName: '',
    contactMobile: '',
    contactEmail: '',
    industry: '',
    sourceId: null,
    level: 3,
    status: 0,
    description: '',
    remark: '',
    tags: ''
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  resetForm()
}

const beforeUpload = (file: File) => {
  const isExcel = file.name.endsWith('.xlsx') || file.name.endsWith('.xls')
  if (!isExcel) {
    ElMessage.error('只能上传 Excel 文件！')
    return false
  }
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过 10MB！')
    return false
  }
  return true
}

const handleImportSuccess = (response: any) => {
  if (response.code === 200) {
    const data = response.data
    ElMessage.success(`导入完成：成功 ${data.successCount} 条，失败 ${data.failCount} 条`)
    if (data.failCount > 0) {
      console.log('导入失败详情：', data.failDetails)
    }
    fetchData()
  } else {
    ElMessage.error(response.message || '导入失败')
  }
}

const handleImportError = (error: any) => {
  console.error('导入错误：', error)
  ElMessage.error('导入失败，请检查文件格式')
}

onMounted(() => {
  fetchData()
  fetchSources()
  fetchUsers()
})
</script>

<style scoped>
.lead-management {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;
}

.import-upload {
  display: inline-block;
}
</style>
