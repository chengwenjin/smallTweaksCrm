import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'system/users',
        name: 'UserManagement',
        component: () => import('@/views/system/User.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'system/roles',
        name: 'RoleManagement',
        component: () => import('@/views/system/Role.vue'),
        meta: { title: '角色管理' }
      },
      {
        path: 'system/menus',
        name: 'MenuManagement',
        component: () => import('@/views/system/Menu.vue'),
        meta: { title: '菜单管理' }
      },
      {
        path: 'system/operation-logs',
        name: 'OperationLog',
        component: () => import('@/views/system/OperationLog.vue'),
        meta: { title: '操作日志' }
      },
      {
        path: 'system/login-logs',
        name: 'LoginLog',
        component: () => import('@/views/system/LoginLog.vue'),
        meta: { title: '登录日志' }
      },
      {
        path: 'crm/lead-lifecycle/lead',
        name: 'LeadManagement',
        component: () => import('@/views/crm/lead/index.vue'),
        meta: { title: '多渠道录入' }
      },
      {
        path: 'crm/lead-lifecycle/assign-rule',
        name: 'AssignRuleManagement',
        component: () => import('@/views/crm/lead/assign-rule.vue'),
        meta: { title: '分配规则配置' }
      },
      {
        path: 'crm/lead-lifecycle/public-pool',
        name: 'PublicPoolManagement',
        component: () => import('@/views/crm/lead/public-pool.vue'),
        meta: { title: '公海池' }
      },
      {
        path: 'crm/lead-lifecycle/assign-record',
        name: 'AssignRecordManagement',
        component: () => import('@/views/crm/lead/assign-record.vue'),
        meta: { title: '分配记录' }
      },
      {
        path: 'crm/customer-360/customer',
        name: 'CustomerManagement',
        component: () => import('@/views/crm/customer/index.vue'),
        meta: { title: '基础信息库' }
      },
      {
        path: 'crm/resource-security/private-sea',
        name: 'PrivateSeaConfig',
        component: () => import('@/views/crm/resource-security/private-sea.vue'),
        meta: { title: '私海配置' }
      },
      {
        path: 'crm/resource-security/public-sea-rule',
        name: 'PublicSeaRule',
        component: () => import('@/views/crm/resource-security/public-sea-rule.vue'),
        meta: { title: '公海规则' }
      },
      {
        path: 'crm/resource-security/transfer',
        name: 'ResourceTransfer',
        component: () => import('@/views/crm/resource-security/transfer.vue'),
        meta: { title: '离职一键回收' }
      },
      {
        path: 'crm/sales-funnel/stage',
        name: 'SalesStageManagement',
        component: () => import('@/views/crm/sales-funnel/stage.vue'),
        meta: { title: '阶段管理' }
      },
      {
        path: 'crm/sales-funnel/opportunity',
        name: 'BusinessOpportunityManagement',
        component: () => import('@/views/crm/sales-funnel/opportunity.vue'),
        meta: { title: '商机管理' }
      },
      {
        path: 'crm/sales-process/follow-record',
        name: 'FollowRecordManagement',
        component: () => import('@/views/crm/sales-process/follow-record.vue'),
        meta: { title: '跟进记录' }
      },
      {
        path: 'crm/sales-process/field-checkin',
        name: 'FieldCheckinManagement',
        component: () => import('@/views/crm/sales-process/field-checkin.vue'),
        meta: { title: '外勤签到' }
      },
      {
        path: 'crm/sales-process/todo-reminder',
        name: 'TodoReminderManagement',
        component: () => import('@/views/crm/sales-process/todo-reminder.vue'),
        meta: { title: '待办提醒' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('accessToken')
  
  if (to.path === '/login') {
    if (token) {
      next('/')
    } else {
      next()
    }
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router
