<template>
  <div class="public-pool-management">
    <el-card>
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="线索名称">
          <el-input v-model="searchForm.leadName" placeholder="请输入线索名称" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="searchForm.contactMobile" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item label="行业">
          <el-input v-model="searchForm.industry" placeholder="请输入行业" clearable />
        </el-form-item>
        <el-form-item label="等级">
          <el-select v-model="searchForm.level" placeholder="请选择等级" clearable style="width: 100px">
            <el-option label="高" :value="1" />
            <el-option label="中" :value="2" />
            <el-option label="低" :value="3" />
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
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="leadNo" label="线索编号" width="180" />
        <el-table-column prop="leadName" label="线索名称" min-width="150">
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
        <el-table-column prop="industry" label="行业" width="100">
          <template #default="{ row }">
            {{ row.industry || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="expectedAmount" label="预计金额" width="120">
          <template #default="{ row }">
            {{ row.expectedAmount ? `¥${row.expectedAmount}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="fromUserName" label="原负责人" width="100">
          <template #default="{ row }">
            {{ row.fromUserName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="publicTime" label="入池时间" width="180">
          <template #default="{ row }">
            {{ row.publicTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleClaim(row)">认领</el-button>
            <el-button link type="info" size="small" @click="handleView(row)">详情</el-button>
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
      v-model="claimDialogVisible"
      title="认领线索"
      width="400px"
    >
      <el-form :model="claimForm" label-width="80px">
        <el-form-item label="线索名称">
          <el-input :value="currentRow?.leadName" disabled />
        </el-form-item>
        <el-form-item label="认领备注">
          <el-input
            v-model="claimForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入认领备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="claimDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitClaim" :loading="claimSubmitLoading">确定认领</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getPublicPoolList,
  claimPublicPoolLead
} from '@/api/publicPool'

const loading = ref(false)
const claimDialogVisible = ref(false)
const claimSubmitLoading = ref(false)
const currentRow = ref<any>(null)

const searchForm = reactive({
  leadName: '',
  contactMobile: '',
  industry: '',
  level: null as number | null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref<any[]>([])

const claimForm = reactive({
  leadId: null as number | null,
  remark: ''
})

const getLevelTagType = (level: number) => {
  if (level === 1) return 'danger'
  if (level === 2) return 'warning'
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
    const res = await getPublicPoolList(params)
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
  searchForm.leadName = ''
  searchForm.contactMobile = ''
  searchForm.industry = ''
  searchForm.level = null
  handleSearch()
}

const handleClaim = (row: any) => {
  ElMessageBox.confirm('确定要领用该线索吗？领用后将归您所有。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    currentRow.value = row
    claimForm.leadId = row.leadId
    claimForm.remark = ''
    claimDialogVisible.value = true
  })
}

const handleView = (row: any) => {
  ElMessage.info('详情功能开发中')
}

const handleSubmitClaim = async () => {
  claimSubmitLoading.value = true
  try {
    await claimPublicPoolLead({
      leadId: claimForm.leadId!,
      remark: claimForm.remark
    })
    ElMessage.success('认领成功')
    claimDialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error(error)
  } finally {
    claimSubmitLoading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.public-pool-management {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}
</style>
