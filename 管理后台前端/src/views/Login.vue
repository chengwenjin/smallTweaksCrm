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

// 安全获取对象属性
const getSafeValue = (obj: any, ...path: string[]): any => {
  let current = obj
  for (const key of path) {
    if (current === null || current === undefined) {
      return undefined
    }
    current = current[key]
  }
  return current
}

// 获取验证码
const getCaptcha = async () => {
  try {
    console.log('开始获取验证码...')
    const res = await request.get('/auth/captcha')
    console.log('验证码原始响应:', JSON.stringify(res))
    
    // 尝试各种可能的数据结构
    let captchaImg: string | undefined
    let captchaK: string | undefined

    // 情况1: res 直接是数据对象 {captchaKey, captchaImage}
    if (res && res.captchaImage) {
      captchaImg = res.captchaImage
      captchaK = res.captchaKey
      console.log('情况1: res 直接是数据对象')
    }
    // 情况2: res = {code, data}, data = {captchaKey, captchaImage}
    else if (res && res.data) {
      captchaImg = getSafeValue(res, 'data', 'captchaImage')
      captchaK = getSafeValue(res, 'data', 'captchaKey')
      console.log('情况2: res.data 包含数据')
    }
    // 情况3: res = {code, message, captchaKey, captchaImage}
    else if (res && 'captchaImage' in res) {
      captchaImg = res.captchaImage
      captchaK = res.captchaKey
      console.log('情况3: res 包含 captchaImage')
    }

    console.log('解析结果 - captchaImg:', captchaImg ? (captchaImg.substring(0, 50) + '...') : 'undefined')
    console.log('解析结果 - captchaKey:', captchaK)

    // 确保图片格式正确
    if (captchaImg) {
      if (!captchaImg.startsWith('data:image')) {
        captchaImg = 'data:image/png;base64,' + captchaImg
      }
      captchaImage.value = captchaImg
    } else {
      console.error('未能解析出验证码图片')
      ElMessage.error('获取验证码失败: 无法解析响应数据')
    }

    if (captchaK) {
      captchaKey.value = captchaK
      loginForm.captchaKey = captchaK
    }

    console.log('最终 captchaImage.value:', captchaImage.value ? (captchaImage.value.substring(0, 50) + '...') : '空')
  } catch (error) {
    console.error('获取验证码失败', error)
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
        console.log('登录请求参数:', loginForm)
        const res = await request.post('/auth/login', loginForm)
        console.log('登录响应:', res)

        // 尝试各种可能的数据结构
        let accessToken: string | undefined
        let refreshToken: string | undefined

        if (res && res.data) {
          accessToken = getSafeValue(res, 'data', 'accessToken')
          refreshToken = getSafeValue(res, 'data', 'refreshToken')
        }
        if (!accessToken && res) {
          accessToken = res.accessToken
          refreshToken = res.refreshToken
        }

        console.log('解析 accessToken:', accessToken ? '已获取' : '未获取')

        if (accessToken) {
          localStorage.setItem('accessToken', accessToken)
          if (refreshToken) {
            localStorage.setItem('refreshToken', refreshToken)
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
  console.log('Login.vue 已挂载，开始获取验证码...')
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
