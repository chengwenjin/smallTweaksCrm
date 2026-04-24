<template>
  <div class="assign-record-management">
    <el-card>
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="线索编号">
          <el-input v-model="searchForm.leadNo" placeholder="请输入线索编号" clearable />
        </el-form-item>
        <el-form-item label="线索名称">
          <el-input v-model="searchForm.leadName" placeholder="请输入线索名称" clearable />
        </el-form-item>
        <el-form-item label="分配类型">
          <el-select v-model="searchForm.assignType" placeholder="请选择分配类型" clearable style="width: 140px">
            <el-option label="自动分配" :value="1" />
            <el-option label="手动分配" :value="2" />
            <el-option label="认领" :value="3" />
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
        <el-table-column prop="leadNo" label="线索编号" width="180" />
        <el-table-column prop="leadName" label="线索名称" min-width="150">
          <template #default="{ row }">
            <div :title="row.leadName">{{ row.leadName }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="fromUserName" label="原负责人" width="100">
          <template #default="{ row }">
            {{ row.fromUserName || '系统' }}
          </template>
        </el-table-column>
        <el-table-column prop="toUserName" label="目标负责人" width="100">
          <template #default="{ row }">
            {{ row.toUserName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="assignTypeName" label="分配类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getAssignTypeTagType(row.assignType)" size="small">
              {{ row.assignTypeName || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ruleName" label="匹配规则" width="150">
          <template #default="{ row }">
            {{ row.ruleName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="分配原因" min-width="200">
          <template #default="{ row }">
            <div :title="row.reason">{{ row.reason || '-' }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="分配时间" width="180">
          <template #default="{ row }">
            {{ row.createTime || '-' }}
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getAssignRecordList } from '@/api/leadFollow'

const loading = ref(false)

const searchForm = reactive({
  leadNo: '',
  leadName: '',
  assignType: null as number | null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref<any[]>([])

const getAssignTypeTagType = (type: number) => {
  if (type === 1) return 'primary'
  if (type === 2) return 'success'
  if (type === 3) return 'warning'
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
    const res = await getAssignRecordList(params)
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
  searchForm.leadNo = ''
  searchForm.leadName = ''
  searchForm.assignType = null
  handleSearch()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.assign-record-management {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}
</style>
