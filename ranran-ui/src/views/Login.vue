<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-title">然然管理系统</div>
      <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
        <div class="input-group">
          <el-form-item prop="username" class="input-item">
            <el-input v-model="loginForm.username" type="text" size="large" auto-complete="off" placeholder="账号"
                      prefix-icon="User"/>
          </el-form-item>
          <el-form-item prop="password" class="input-item">
            <el-input v-model="loginForm.password" type="password" size="large" auto-complete="off" placeholder="密码"
                      prefix-icon="Lock" @keyup.enter="handleLogin"/>
          </el-form-item>
          <el-form-item prop="code" class="input-item">
            <div class="code-container">
              <el-input v-model="loginForm.code" type="text" size="large" auto-complete="off" placeholder="验证码"
                        prefix-icon="Key" @keyup.enter="handleLogin"/>
              <div class="code-image" @click="getCodeImage">
                <img :src="codeImageUrl" alt="验证码" />
              </div>
            </div>
          </el-form-item>
        </div>

        <el-form-item class="btn-group">
          <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup name="Login">
import {ref, onMounted} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import {useUserStore} from '@/store/user'
import request from '@/utils/request'

const router = useRouter()
const loading = ref(false)
const loginRef = ref(null)
const loginForm = ref({
  username: '',
  password: '',
  code: '',
  codeToken: ''
})

const loginRules = {
  username: [
    {required: true, trigger: "blur", message: "请输入您的账号"},
  ],
  password: [
    {required: true, trigger: "blur", message: "请输入您的密码"},
  ],
  code: [
    {required: true, trigger: "blur", message: "请输入验证码"},
    {min: 4, max: 4, trigger: "blur", message: "验证码长度为4位"},
  ]
}

const codeImageUrl = ref('')

function handleLogin() {
  loginRef.value.validate().then(valid => {
    if (valid) {
      loading.value = true
      const userStore = useUserStore()
      userStore.login(loginForm.value).then(res => {
        loading.value = false
        ElMessage.success('登录成功，正在跳转...')
        router.push('/index')
      }).catch(error => {
        loading.value = false
        getCodeImage()
      })
    }
  })
}
function getCodeImage() {
  request({
    url: '/captcha/image',
    method: 'get'
  }).then(res => {
    codeImageUrl.value = res.data.image 
    loginForm.value.code = '' 
    loginForm.value.codeToken = res.data.codeToken  
  }).catch(error => {
  })
}
onMounted(() => {
  getCodeImage()
})

</script>

<style scoped>
.login-container {
  background-color: #f8e6ca; 
  background-image: url('../assets/image/login.png');
  background-size: cover;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  box-sizing: border-box;
  overflow: hidden;
}

.login-card {
  width: 100%;
  max-width: 350px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  padding: 20px 30px;
}

.login-title {
  text-align: center;
  font-size: 1.1rem;
  color: #686868;
  margin-bottom: 30px;
}

.login-form {
  width: 100%;
}

.input-item {
  height: 44px;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 1rem;
}

.code-container {
  display: flex;
  gap: 10px;
  width: 100%;
}

.code-container .el-input {
  flex: 7;
}

.code-image {
  flex: 3;
  height: 40px;
  cursor: pointer;
  overflow: hidden;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
}

.code-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

:deep(.el-input__wrapper):focus-within {
  box-shadow: 0 0 0 2px rgba(79, 172, 254, 0.2);
  border-color: #4facfe;
}
</style>