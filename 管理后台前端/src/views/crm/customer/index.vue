<template>
  <div class="customer-management">
    <el-card>
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="客户名称">
          <el-input v-model="searchForm.customerName" placeholder="请输入客户名称" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="客户等级">
          <el-select v-model="searchForm.levelCode" placeholder="请选择等级" clearable style="width: 120px">
            <el-option
              v-for="level in levelList"
              :key="level.levelCode"
              :label="level.levelName"
              :value="level.levelCode"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="客户状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="潜在" :value="0" />
            <el-option label="合作中" :value="1" />
            <el-option label="已流失" :value="2" />
            <el-option label="休眠" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属行业">
          <el-input v-model="searchForm.industry" placeholder="请输入行业" clearable style="width: 120px" />
        </el-form-item>
        <el-form-item label="所在城市">
          <el-input v-model="searchForm.city" placeholder="请输入城市" clearable style="width: 120px" />
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
          新增客户
        </el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="customerNo" label="客户编号" width="180" />
        <el-table-column prop="customerName" label="客户名称" width="150">
          <template #default="{ row }">
            <div class="name-cell" :title="row.customerName">
              <span class="name-text">{{ row.customerName }}</span>
              <span v-if="row.shortName" class="short-name">({{ row.shortName }})</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="levelName" label="客户等级" width="90">
          <template #default="{ row }">
            <el-tag
              :type="getLevelTagType(row.levelCode)"
              size="small"
            >
              {{ row.levelName || row.levelCode || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="statusName" label="客户状态" width="90">
          <template #default="{ row }">
            <el-tag
              :type="getStatusTagType(row.status)"
              size="small"
            >
              {{ row.statusName || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="industry" label="所属行业" width="120" />
        <el-table-column prop="ownerUserName" label="负责人" width="90" />
        <el-table-column prop="phone" label="联系电话" width="120" />
        <el-table-column prop="fullAddress" label="所在地区" width="150">
          <template #default="{ row }">
            <span :title="row.fullAddress">{{ row.fullAddress || row.province || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">全景视图</el-button>
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
      width="900px"
      @close="handleDialogClose"
    >
      <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="100px"
      >
        <el-divider>基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="form.customerName" placeholder="请输入客户名称" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户简称">
              <el-input v-model="form.shortName" placeholder="请输入客户简称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="客户类型">
              <el-select v-model="form.customerType" placeholder="请选择类型" style="width: 100%">
                <el-option label="企业客户" value="1" />
                <el-option label="个人客户" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户等级">
              <el-select v-model="form.levelCode" placeholder="请选择等级" style="width: 100%">
                <el-option
                  v-for="level in levelList"
                  :key="level.levelCode"
                  :label="level.levelName"
                  :value="level.levelCode"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户状态">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="潜在" :value="0" />
                <el-option label="合作中" :value="1" />
                <el-option label="已流失" :value="2" />
                <el-option label="休眠" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider>工商信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="统一信用代码">
              <el-input v-model="form.creditCode" placeholder="请输入统一社会信用代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="法定代表人">
              <el-input v-model="form.legalPerson" placeholder="请输入法定代表人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="注册资本">
              <el-input-number v-model="form.registeredCapital" :precision="2" :min="0" placeholder="万元" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="成立日期">
              <el-date-picker v-model="form.establishDate" type="date" placeholder="请选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="经营状态">
              <el-select v-model="form.businessStatus" placeholder="请选择经营状态" clearable style="width: 100%">
                <el-option label="存续" value="存续" />
                <el-option label="在业" value="在业" />
                <el-option label="注销" value="注销" />
                <el-option label="吊销" value="吊销" />
                <el-option label="迁入" value="迁入" />
                <el-option label="迁出" value="迁出" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="经营范围">
              <el-input v-model="form.businessScope" type="textarea" :rows="2" placeholder="请输入经营范围" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider>联系方式</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="所属行业">
              <el-input v-model="form.industry" placeholder="请输入所属行业" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系电话">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="企业邮箱">
              <el-input v-model="form.email" placeholder="请输入企业邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="企业官网">
              <el-input v-model="form.website" placeholder="请输入企业官网" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="企业传真">
              <el-input v-model="form.fax" placeholder="请输入企业传真" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户来源">
              <el-input v-model="form.source" placeholder="请输入客户来源" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="省份">
              <el-select v-model="form.province" placeholder="请选择省份" filterable clearable style="width: 100%">
                <el-option
                  v-for="item in provinceList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="城市">
              <el-select v-model="form.city" placeholder="请选择城市" filterable clearable style="width: 100%">
                <el-option
                  v-for="item in cityList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="区县">
              <el-select v-model="form.district" placeholder="请选择区县" filterable clearable style="width: 100%">
                <el-option
                  v-for="item in districtList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="详细地址">
              <el-input v-model="form.address" placeholder="详细地址" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider>规模信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="员工人数">
              <el-input-number v-model="form.employeeCount" :min="0" placeholder="人" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="年营收">
              <el-input-number v-model="form.annualRevenue" :precision="2" :min="0" placeholder="万元" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="企业规模">
              <el-select v-model="form.companyScale" placeholder="请选择规模" clearable style="width: 100%">
                <el-option label="小型企业" value="小型企业" />
                <el-option label="中型企业" value="中型企业" />
                <el-option label="大型企业" value="大型企业" />
                <el-option label="超大型企业" value="超大型企业" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider>分级与标签</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人">
              <el-select
                v-model="form.ownerUserId"
                placeholder="请选择负责人"
                filterable
                clearable
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
            <el-form-item label="上级客户">
              <el-select
                v-model="form.parentCustomerId"
                placeholder="请选择上级客户"
                filterable
                clearable
                style="width: 100%"
              >
                <el-option
                  v-for="customer in customerOptions"
                  :key="customer.id"
                  :label="customer.customerName"
                  :value="customer.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="客户标签">
              <el-select
                v-model="selectedTags"
                multiple
                filterable
                allow-create
                default-first-option
                placeholder="请选择或输入标签"
                style="width: 100%"
              >
                <el-option
                  v-for="tag in tagList"
                  :key="tag.id"
                  :label="tag.tagName"
                  :value="tag.tagName"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider>其他信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="客户描述">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="3"
                placeholder="请输入客户描述"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input
                v-model="form.remark"
                type="textarea"
                :rows="2"
                placeholder="请输入备注信息"
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
      v-model="detailDialogVisible"
      title="客户全景视图"
      width="1000px"
      :close-on-click-modal="false"
    >
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-descriptions :column="3" border>
            <el-descriptions-item label="客户编号">{{ currentCustomer?.customerNo }}</el-descriptions-item>
            <el-descriptions-item label="客户名称">
              <span class="customer-name-large">{{ currentCustomer?.customerName }}</span>
              <span v-if="currentCustomer?.shortName" class="ml-2 text-gray-500">({{ currentCustomer?.shortName }})</span>
            </el-descriptions-item>
            <el-descriptions-item label="客户类型">{{ currentCustomer?.customerTypeName }}</el-descriptions-item>
            <el-descriptions-item label="客户等级">
              <el-tag :type="getLevelTagType(currentCustomer?.levelCode)" size="small">
                {{ currentCustomer?.levelName || currentCustomer?.levelCode }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="客户状态">
              <el-tag :type="getStatusTagType(currentCustomer?.status)" size="small">
                {{ currentCustomer?.statusName }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="负责人">{{ currentCustomer?.ownerUserName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="所属行业">{{ currentCustomer?.industry || '-' }}</el-descriptions-item>
            <el-descriptions-item label="客户来源">{{ currentCustomer?.source || '-' }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ currentCustomer?.phone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="企业邮箱">{{ currentCustomer?.email || '-' }}</el-descriptions-item>
            <el-descriptions-item label="企业官网">{{ currentCustomer?.website || '-' }}</el-descriptions-item>
            <el-descriptions-item label="详细地址">
              <span :title="currentCustomer?.fullAddress">{{ currentCustomer?.fullAddress || '-' }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="统一信用代码">{{ currentCustomer?.creditCode || '-' }}</el-descriptions-item>
            <el-descriptions-item label="法定代表人">{{ currentCustomer?.legalPerson || '-' }}</el-descriptions-item>
            <el-descriptions-item label="注册资本">
              {{ currentCustomer?.registeredCapital ? currentCustomer.registeredCapital + ' 万元' : '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="成立日期">{{ currentCustomer?.establishDate || '-' }}</el-descriptions-item>
            <el-descriptions-item label="经营状态">{{ currentCustomer?.businessStatus || '-' }}</el-descriptions-item>
            <el-descriptions-item label="员工人数">
              {{ currentCustomer?.employeeCount ? currentCustomer.employeeCount + ' 人' : '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="年营收">
              {{ currentCustomer?.annualRevenue ? currentCustomer.annualRevenue + ' 万元' : '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="企业规模">{{ currentCustomer?.companyScale || '-' }}</el-descriptions-item>
            <el-descriptions-item label="客户标签" :span="3">
              <el-tag
                v-for="tag in currentCustomer?.tagList"
                :key="tag"
                size="small"
                class="mr-1 mb-1"
              >
                {{ tag }}
              </el-tag>
              <span v-if="!currentCustomer?.tagList?.length" class="text-gray-400">暂无标签</span>
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <el-tab-pane label="关键联系人" name="contacts">
          <div class="contact-toolbar">
            <el-button type="primary" size="small" @click="handleAddContact">
              <el-icon><Plus /></el-icon>
              新增联系人
            </el-button>
          </div>
          <el-table :data="contactList" border stripe v-loading="contactLoading">
            <el-table-column prop="contactName" label="姓名" width="100" />
            <el-table-column prop="contactPosition" label="职位" width="120" />
            <el-table-column prop="contactDepartment" label="部门" width="120" />
            <el-table-column prop="mobile" label="手机号" width="130" />
            <el-table-column prop="phone" label="办公电话" width="120" />
            <el-table-column prop="email" label="邮箱" width="180" />
            <el-table-column prop="wechat" label="微信" width="120" />
            <el-table-column prop="isKeyContact" label="关键联系人" width="90">
              <template #default="{ row }">
                <el-tag v-if="row.isKeyContact === 1" type="danger" size="small">是</el-tag>
                <span v-else>否</span>
              </template>
            </el-table-column>
            <el-table-column prop="isPrimary" label="主联系人" width="90">
              <template #default="{ row }">
                <el-tag v-if="row.isPrimary === 1" type="primary" size="small">是</el-tag>
                <span v-else>否</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button link type="primary" size="small" @click="handleEditContact(row)">编辑</el-button>
                <el-button link type="danger" size="small" @click="handleDeleteContact(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="跟进记录" name="follows">
          <div class="follow-toolbar">
            <el-button type="primary" size="small" @click="handleAddFollow">
              <el-icon><Plus /></el-icon>
              新增跟进
            </el-button>
          </div>
          <el-timeline v-if="followList.length > 0">
            <el-timeline-item
              v-for="follow in followList"
              :key="follow.id"
              :timestamp="follow.createTime"
              placement="top"
            >
              <el-card>
                <template #header>
                  <div class="follow-header">
                    <span>
                      <el-tag size="small">{{ follow.followTypeName }}</el-tag>
                      <span class="ml-2">{{ follow.followUserName }}</span>
                    </span>
                    <span v-if="follow.nextFollowTime" class="text-gray-500">
                      <el-icon><Timer /></el-icon>
                      下次跟进: {{ follow.nextFollowTime }}
                      <span v-if="follow.nextFollowRemark"> ({{ follow.nextFollowRemark }})</span>
                    </span>
                  </div>
                </template>
                <div class="follow-content">{{ follow.followContent }}</div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
          <el-empty v-else description="暂无跟进记录" />
        </el-tab-pane>

        <el-tab-pane label="历史报价" name="quotes">
          <el-table :data="quoteList" border stripe v-loading="quoteLoading">
            <el-table-column prop="quoteNo" label="报价单号" width="150" />
            <el-table-column prop="quoteName" label="报价名称" width="200" />
            <el-table-column prop="quoteAmount" label="报价金额" width="120">
              <template #default="{ row }">
                <span class="text-primary font-bold">¥{{ row.quoteAmount?.toLocaleString() }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="quoteDate" label="报价日期" width="120" />
            <el-table-column prop="validDate" label="有效截止" width="120" />
            <el-table-column prop="statusName" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getQuoteStatusType(row.status)" size="small">
                  {{ row.statusName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="description" label="说明" min-width="200">
              <template #default="{ row }">
                <span :title="row.description">{{ row.description || '-' }}</span>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="quoteList.length === 0" description="暂无报价记录" />
        </el-tab-pane>

        <el-tab-pane label="合同订单" name="contracts">
          <el-table :data="contractList" border stripe v-loading="contractLoading">
            <el-table-column prop="contractNo" label="合同编号" width="150" />
            <el-table-column prop="contractName" label="合同名称" width="200" />
            <el-table-column prop="contractType" label="合同类型" width="100" />
            <el-table-column prop="contractAmount" label="合同金额" width="120">
              <template #default="{ row }">
                <span class="text-primary font-bold">¥{{ row.contractAmount?.toLocaleString() }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="signedDate" label="签订日期" width="120" />
            <el-table-column prop="startDate" label="开始日期" width="120" />
            <el-table-column prop="endDate" label="结束日期" width="120" />
            <el-table-column prop="statusName" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getContractStatusType(row.status)" size="small">
                  {{ row.statusName }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="contractList.length === 0" description="暂无合同订单" />
        </el-tab-pane>

        <el-tab-pane label="回款流水" name="payments">
          <el-table :data="paymentList" border stripe v-loading="paymentLoading">
            <el-table-column prop="paymentNo" label="回款单号" width="150" />
            <el-table-column prop="paymentAmount" label="回款金额" width="120">
              <template #default="{ row }">
                <span class="text-success font-bold">¥{{ row.paymentAmount?.toLocaleString() }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="paymentDate" label="回款日期" width="120" />
            <el-table-column prop="paymentMethodName" label="回款方式" width="100" />
            <el-table-column prop="paymentStatusName" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.paymentStatus === 1 ? 'success' : 'warning'" size="small">
                  {{ row.paymentStatusName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="description" label="备注" min-width="200">
              <template #default="{ row }">
                <span :title="row.description">{{ row.description || '-' }}</span>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="paymentList.length === 0" description="暂无回款记录" />
        </el-tab-pane>

        <el-tab-pane label="售后工单" name="tickets">
          <el-table :data="ticketList" border stripe v-loading="ticketLoading">
            <el-table-column prop="ticketNo" label="工单编号" width="150" />
            <el-table-column prop="ticketTitle" label="工单标题" width="200" />
            <el-table-column prop="ticketTypeName" label="工单类型" width="100" />
            <el-table-column prop="priorityName" label="优先级" width="80">
              <template #default="{ row }">
                <el-tag :type="getPriorityType(row.priority)" size="small">
                  {{ row.priorityName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="statusName" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getTicketStatusType(row.status)" size="small">
                  {{ row.statusName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="assigneeUserName" label="处理人" width="100" />
            <el-table-column prop="createTime" label="创建时间" width="160" />
            <el-table-column prop="resolvedTime" label="解决时间" width="160" />
          </el-table>
          <el-empty v-if="ticketList.length === 0" description="暂无售后工单" />
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="contactDialogVisible"
      :title="contactDialogTitle"
      width="600px"
    >
      <el-form :model="contactForm" :rules="contactRules" ref="contactFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="contactName">
              <el-input v-model="contactForm.contactName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位">
              <el-input v-model="contactForm.contactPosition" placeholder="请输入职位" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门">
              <el-input v-model="contactForm.contactDepartment" placeholder="请输入部门" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号">
              <el-input v-model="contactForm.mobile" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="办公电话">
              <el-input v-model="contactForm.phone" placeholder="请输入办公电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱">
              <el-input v-model="contactForm.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="微信号">
              <el-input v-model="contactForm.wechat" placeholder="请输入微信号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="QQ号">
              <el-input v-model="contactForm.qq" placeholder="请输入QQ号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别">
              <el-radio-group v-model="contactForm.gender">
                <el-radio :value="0">未知</el-radio>
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生日">
              <el-date-picker v-model="contactForm.birthday" type="date" placeholder="请选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="关键联系人">
              <el-switch v-model="contactForm.isKeyContact" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主联系人">
              <el-switch v-model="contactForm.isPrimary" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述">
              <el-input v-model="contactForm.description" type="textarea" :rows="2" placeholder="请输入描述" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="contactDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitContact" :loading="contactSubmitLoading">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="followDialogVisible"
      title="新增跟进记录"
      width="500px"
    >
      <el-form :model="followForm" :rules="followRules" ref="followFormRef" label-width="80px">
        <el-form-item label="跟进方式">
          <el-select v-model="followForm.followType" style="width: 100%">
            <el-option label="电话" :value="1" />
            <el-option label="微信" :value="2" />
            <el-option label="邮件" :value="3" />
            <el-option label="拜访" :value="4" />
            <el-option label="其他" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="跟进内容" prop="followContent">
          <el-input v-model="followForm.followContent" type="textarea" :rows="4" placeholder="请输入跟进内容" />
        </el-form-item>
        <el-form-item label="下次跟进">
          <el-date-picker
            v-model="followForm.nextFollowTime"
            type="datetime"
            placeholder="选择下次跟进时间"
            style="width: 100%"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="跟进备注">
          <el-input v-model="followForm.nextFollowRemark" placeholder="下次跟进备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="followDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitFollow" :loading="followSubmitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import {
  getCustomerList,
  getCustomerDetail,
  createCustomer,
  updateCustomer,
  deleteCustomer,
  getCustomerLevels,
  getCustomerTags,
  getCustomerContacts,
  createCustomerContact,
  updateCustomerContact,
  deleteCustomerContact,
  getCustomerFollows,
  createCustomerFollow,
  type CustomerVO,
  type CustomerContactVO
} from '@/api/customer'
import { getUserList } from '@/api/user'
import { regionData, getCityList, getDistrictList } from '@/data/region'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref<FormInstance>()
const levelList = ref<any[]>([])
const tagList = ref<any[]>([])
const userList = ref<any[]>([])
const customerOptions = ref<any[]>([])
const currentRow = ref<any>(null)

const searchForm = reactive({
  customerName: '',
  levelCode: '',
  status: null as number | null,
  industry: '',
  city: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref<any[]>([])

const form = reactive({
  id: null as number | null,
  customerName: '',
  shortName: '',
  customerType: '1',
  creditCode: '',
  legalPerson: '',
  registeredCapital: null as number | null,
  establishDate: '',
  businessStatus: '',
  businessScope: '',
  industry: '',
  province: '',
  city: '',
  district: '',
  address: '',
  website: '',
  email: '',
  phone: '',
  fax: '',
  employeeCount: null as number | null,
  annualRevenue: null as number | null,
  companyScale: '',
  levelCode: 'D',
  ownerUserId: null as number | null,
  tags: '',
  source: '',
  status: 0,
  description: '',
  remark: '',
  parentCustomerId: null as number | null
})

const selectedTags = ref<string[]>([])

const provinceList = computed(() => regionData)
const cityList = ref<any[]>([])
const districtList = ref<any[]>([])

const activeTab = ref('basic')
const currentCustomer = ref<CustomerVO | null>(null)
const contactList = ref<CustomerContactVO[]>([])
const followList = ref<any[]>([])
const quoteList = ref<any[]>([])
const contractList = ref<any[]>([])
const paymentList = ref<any[]>([])
const ticketList = ref<any[]>([])

const contactLoading = ref(false)
const quoteLoading = ref(false)
const contractLoading = ref(false)
const paymentLoading = ref(false)
const ticketLoading = ref(false)

const contactDialogVisible = ref(false)
const contactDialogTitle = ref('')
const contactFormRef = ref<FormInstance>()
const contactSubmitLoading = ref(false)
const contactForm = reactive({
  id: null as number | null,
  customerId: null as number | null,
  contactName: '',
  contactPosition: '',
  contactDepartment: '',
  mobile: '',
  phone: '',
  email: '',
  wechat: '',
  qq: '',
  isKeyContact: 0,
  isPrimary: 0,
  gender: 0,
  birthday: '',
  description: ''
})

const followDialogVisible = ref(false)
const followFormRef = ref<FormInstance>()
const followSubmitLoading = ref(false)
const followForm = reactive({
  customerId: null as number | null,
  followType: 5,
  followContent: '',
  nextFollowTime: '',
  nextFollowRemark: ''
})

const rules: FormRules = {
  customerName: [{ required: true, message: '请输入客户名称', trigger: 'blur' }]
}

const contactRules: FormRules = {
  contactName: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
}

const followRules: FormRules = {
  followContent: [{ required: true, message: '请输入跟进内容', trigger: 'blur' }]
}

const getLevelTagType = (levelCode?: string) => {
  if (levelCode === 'A') return 'danger'
  if (levelCode === 'B') return 'warning'
  if (levelCode === 'C') return 'primary'
  return 'info'
}

const getStatusTagType = (status?: number) => {
  if (status === 1) return 'success'
  if (status === 2) return 'danger'
  if (status === 3) return 'info'
  return 'warning'
}

const getQuoteStatusType = (status?: number) => {
  if (status === 1) return 'success'
  if (status === 2) return 'danger'
  if (status === 3) return 'info'
  return 'warning'
}

const getContractStatusType = (status?: number) => {
  if (status === 1) return 'primary'
  if (status === 2) return 'success'
  if (status === 3) return 'danger'
  return 'info'
}

const getPriorityType = (priority?: number) => {
  if (priority === 1) return 'danger'
  if (priority === 2) return 'warning'
  return 'info'
}

const getTicketStatusType = (status?: number) => {
  if (status === 1) return 'primary'
  if (status === 2) return 'success'
  if (status === 3) return 'info'
  return 'warning'
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const res = await getCustomerList(params)
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const fetchLevels = async () => {
  try {
    const res = await getCustomerLevels()
    levelList.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const fetchTags = async () => {
  try {
    const res = await getCustomerTags()
    tagList.value = res.data
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

const fetchCustomerOptions = async () => {
  try {
    const params = { pageNum: 1, pageSize: 1000 }
    const res = await getCustomerList(params)
    customerOptions.value = res.data.records
  } catch (error) {
    console.error(error)
  }
}

const handleProvinceChange = (province: string) => {
  form.city = ''
  form.district = ''
  cityList.value = getCityList(province)
  districtList.value = []
}

const handleCityChange = (city: string) => {
  form.district = ''
  districtList.value = getDistrictList(form.province, city)
}

watch(() => form.province, handleProvinceChange)
watch(() => form.city, handleCityChange)

const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

const handleReset = () => {
  searchForm.customerName = ''
  searchForm.levelCode = ''
  searchForm.status = null
  searchForm.industry = ''
  searchForm.city = ''
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增客户'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑客户'
  isEdit.value = true
  currentRow.value = row

  Object.assign(form, {
    id: row.id,
    customerName: row.customerName,
    shortName: row.shortName,
    customerType: row.customerType || '1',
    creditCode: row.creditCode || '',
    legalPerson: row.legalPerson || '',
    registeredCapital: row.registeredCapital,
    establishDate: row.establishDate || '',
    businessStatus: row.businessStatus || '',
    businessScope: row.businessScope || '',
    industry: row.industry || '',
    province: row.province || '',
    city: row.city || '',
    district: row.district || '',
    address: row.address || '',
    website: row.website || '',
    email: row.email || '',
    phone: row.phone || '',
    fax: row.fax || '',
    employeeCount: row.employeeCount,
    annualRevenue: row.annualRevenue,
    companyScale: row.companyScale || '',
    levelCode: row.levelCode || 'D',
    ownerUserId: row.ownerUserId,
    tags: row.tags || '',
    source: row.source || '',
    status: row.status ?? 0,
    description: row.description || '',
    remark: row.remark || '',
    parentCustomerId: row.parentCustomerId
  })

  selectedTags.value = row.tags ? row.tags.split(',') : []

  if (row.province) {
    cityList.value = getCityList(row.province)
  }
  if (row.city) {
    districtList.value = getDistrictList(row.province, row.city)
  }

  dialogVisible.value = true
}

const handleView = async (row: any) => {
  currentRow.value = row
  detailDialogVisible.value = true
  activeTab.value = 'basic'

  try {
    const res = await getCustomerDetail(row.id)
    currentCustomer.value = res.data
    contactList.value = res.data.contacts || []
    followList.value = res.data.follows || []
    quoteList.value = res.data.quotes || []
    contractList.value = res.data.contracts || []
    paymentList.value = res.data.payments || []
    ticketList.value = res.data.tickets || []
  } catch (error) {
    console.error(error)
  }
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该客户吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCustomer(row.id)
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
        form.tags = selectedTags.value.join(',')
        if (isEdit.value) {
          await updateCustomer(form.id!, form as any)
          ElMessage.success('更新成功')
        } else {
          await createCustomer(form as any)
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
    customerName: '',
    shortName: '',
    customerType: '1',
    creditCode: '',
    legalPerson: '',
    registeredCapital: null,
    establishDate: '',
    businessStatus: '',
    businessScope: '',
    industry: '',
    province: '',
    city: '',
    district: '',
    address: '',
    website: '',
    email: '',
    phone: '',
    fax: '',
    employeeCount: null,
    annualRevenue: null,
    companyScale: '',
    levelCode: 'D',
    ownerUserId: null,
    tags: '',
    source: '',
    status: 0,
    description: '',
    remark: '',
    parentCustomerId: null
  })
  selectedTags.value = []
  cityList.value = []
  districtList.value = []
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  resetForm()
}

const handleAddContact = () => {
  contactDialogTitle.value = '新增联系人'
  Object.assign(contactForm, {
    id: null,
    customerId: currentCustomer.value?.id,
    contactName: '',
    contactPosition: '',
    contactDepartment: '',
    mobile: '',
    phone: '',
    email: '',
    wechat: '',
    qq: '',
    isKeyContact: 0,
    isPrimary: 0,
    gender: 0,
    birthday: '',
    description: ''
  })
  contactDialogVisible.value = true
}

const handleEditContact = (row: CustomerContactVO) => {
  contactDialogTitle.value = '编辑联系人'
  Object.assign(contactForm, {
    id: row.id,
    customerId: row.customerId,
    contactName: row.contactName,
    contactPosition: row.contactPosition || '',
    contactDepartment: row.contactDepartment || '',
    mobile: row.mobile || '',
    phone: row.phone || '',
    email: row.email || '',
    wechat: row.wechat || '',
    qq: row.qq || '',
    isKeyContact: row.isKeyContact ?? 0,
    isPrimary: row.isPrimary ?? 0,
    gender: row.gender ?? 0,
    birthday: row.birthday || '',
    description: row.description || ''
  })
  contactDialogVisible.value = true
}

const handleDeleteContact = (row: CustomerContactVO) => {
  ElMessageBox.confirm('确定要删除该联系人吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCustomerContact(row.customerId!, row.id!)
      ElMessage.success('删除成功')
      const res = await getCustomerDetail(currentCustomer.value!.id!)
      currentCustomer.value = res.data
      contactList.value = res.data.contacts || []
    } catch (error) {
      console.error(error)
    }
  })
}

const handleSubmitContact = async () => {
  if (!contactFormRef.value) return

  await contactFormRef.value.validate(async (valid) => {
    if (valid) {
      contactSubmitLoading.value = true
      try {
        if (contactForm.id) {
          await updateCustomerContact(contactForm.customerId!, contactForm.id, contactForm as any)
          ElMessage.success('更新成功')
        } else {
          await createCustomerContact(contactForm.customerId!, contactForm as any)
          ElMessage.success('创建成功')
        }
        contactDialogVisible.value = false
        const res = await getCustomerDetail(currentCustomer.value!.id!)
        currentCustomer.value = res.data
        contactList.value = res.data.contacts || []
      } catch (error) {
        console.error(error)
      } finally {
        contactSubmitLoading.value = false
      }
    }
  })
}

const handleAddFollow = () => {
  Object.assign(followForm, {
    customerId: currentCustomer.value?.id,
    followType: 5,
    followContent: '',
    nextFollowTime: '',
    nextFollowRemark: ''
  })
  followDialogVisible.value = true
}

const handleSubmitFollow = async () => {
  if (!followFormRef.value) return

  await followFormRef.value.validate(async (valid) => {
    if (valid) {
      followSubmitLoading.value = true
      try {
        await createCustomerFollow(followForm.customerId!, followForm as any)
        ElMessage.success('创建成功')
        followDialogVisible.value = false
        const res = await getCustomerDetail(currentCustomer.value!.id!)
        currentCustomer.value = res.data
        followList.value = res.data.follows || []
      } catch (error) {
        console.error(error)
      } finally {
        followSubmitLoading.value = false
      }
    }
  })
}

onMounted(() => {
  fetchData()
  fetchLevels()
  fetchTags()
  fetchUsers()
  fetchCustomerOptions()
})
</script>

<style scoped>
.customer-management {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;
}

.name-cell {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.name-text {
  font-weight: 500;
}

.short-name {
  color: #909399;
  font-size: 12px;
}

.contact-toolbar,
.follow-toolbar {
  margin-bottom: 15px;
}

.follow-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.follow-content {
  white-space: pre-wrap;
}

.customer-name-large {
  font-size: 16px;
  font-weight: 600;
}

.ml-2 {
  margin-left: 8px;
}

.mr-1 {
  margin-right: 4px;
}

.mb-1 {
  margin-bottom: 4px;
}

.text-gray-400 {
  color: #909399;
}

.text-gray-500 {
  color: #606266;
}

.text-primary {
  color: #409eff;
}

.text-success {
  color: #67c23a;
}

.font-bold {
  font-weight: bold;
}
</style>
