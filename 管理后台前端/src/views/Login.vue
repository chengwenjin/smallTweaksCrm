<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="login-title">RBAC管理后台</h2>
      <el-form :model="loginForm" :rules="rules" ref="formRef" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item prop="captcha">
          <div class="captcha-wrapper">
            <el-input
              v-model="loginForm.captcha"
              placeholder="验证码"
              prefix-icon="Key"
              size="large"
              style="flex: 1"
              @keyup.enter="handleLogin"
            />
            <img
              v-if="captchaImage"
              :src="captchaImage"
              alt="验证码"
              class="captcha-image"
              @click="getCaptcha"
            />
            <div v-else class="captcha-loading" @click="getCaptcha">
              点击加载
            </div>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-button"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const formRef = ref<FormInstance>()
const loading = ref(false)
const captchaImage = ref('')
const captchaKey = ref('')

const loginForm = reactive({
  username: 'admin',
  password: '123456',
  captcha: '',
  captchaKey: ''
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

// 获取验证码
const getCaptcha = async () => {
  try {
    console.log('--- 开始获取验证码 ---')
    const res = await request.get('/auth/captcha')
    console.log('完整响应对象:', res)
    console.log('完整响应字符串:', JSON.stringify(res))

    // 从响应中提取数据
    // 注意：request 拦截器返回的是 response.data（即后端返回的 R 对象）
    // R 对象结构：{code: 200, message: "...", data: {captchaKey, captchaImage}}
    
    let img: string | undefined
    let key: string | undefined

    // 检查 res.data 是否存在（这是后端 R 对象的 data 字段）
    if (res && res.data) {
      console.log('res.data 存在:', res.data)
      img = res.data.captchaImage
      key = res.data.captchaKey
      console.log('从 res.data 获取 - captchaImage:', img ? (img.substring(0, 60) + '...') : 'undefined')
      console.log('从 res.data 获取 - captchaKey:', key)
    }

    // 如果 res.data 不存在，但 res 本身有 captchaImage
    if (!img && res && res.captchaImage) {
      console.log('从 res 直接获取 captchaImage')
      img = res.captchaImage
      key = res.captchaKey
    }

    // 处理图片格式
    if (img) {
      console.log('处理前的图片值:', img.substring(0, 80) + '...')
      console.log('是否已包含 data:image 前缀:', img.indexOf('data:image') !== -1)
      
      // 检查是否已经包含 data:image 前缀
      if (img.indexOf('data:image') === -1) {
        console.log('需要添加前缀')
        img = 'data:image/png;base64,' + img
      } else {
        console.log('不需要添加前缀')
        // 防止重复前缀的情况：data:image/png;base64,data:image/png;base64,...
        const prefix = 'data:image/png;base64,'
        if (img.indexOf(prefix + prefix) !== -1) {
          console.log('检测到重复前缀，正在修复...')
          // 移除重复的前缀
          img = prefix + img.substring((prefix + prefix).length)
        }
      }
      
      console.log('处理后的图片值:', img.substring(0, 80) + '...')
      captchaImage.value = img
    } else {
      console.error('未能获取到验证码图片')
      ElMessage.error('获取验证码失败')
    }

    if (key) {
      captchaKey.value = key
      loginForm.captchaKey = key
    }

    console.log('--- 验证码获取完成 ---')
    console.log('最终 captchaImage.value:', captchaImage.value ? (captchaImage.value.substring(0, 60) + '...') : '空')
  } catch (error) {
    console.error('获取验证码异常:', error)
    ElMessage.error('获取验证码失败: ' + (error as Error).message)
  }
}

// 登录
const handleLogin = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        console.log('登录参数:', loginForm)
        const res = await request.post('/auth/login', loginForm)
        console.log('登录响应:', res)

        let token: string | undefined
        let refresh: string | undefined

        if (res && res.data) {
          token = res.data.accessToken
          refresh = res.data.refreshToken
        }
        if (!token && res) {
          token = res.accessToken
          refresh = res.refreshToken
        }

        if (token) {
          localStorage.setItem('accessToken', token)
          if (refresh) {
            localStorage.setItem('refreshToken', refresh)
          }
          ElMessage.success('登录成功')
          router.push('/')
        } else {
          ElMessage.error('登录失败: 未获取到Token')
          getCaptcha()
          loginForm.captcha = ''
        }
      } catch (error) {
        console.error('登录失败', error)
        getCaptcha()
        loginForm.captcha = ''
      } finally {
        loading.value = false
      }
    }
  })
}

onMounted(() => {
  console.log('Login.vue 已挂载')
  getCaptcha()
})
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  padding: 20px;
}

.login-title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
}

.login-form {
  margin-top: 20px;
}

.captcha-wrapper {
  display: flex;
  gap: 10px;
  align-items: center;
}

.captcha-image {
  width: 120px;
  height: 40px;
  cursor: pointer;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
}

.captcha-loading {
  width: 120px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  color: #909399;
}

.captcha-loading:hover {
  color: #409eff;
  border-color: #c0c4cc;
}

.login-button {
  width: 100%;
}
</style>
