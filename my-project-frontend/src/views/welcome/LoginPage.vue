<template>
  <div class="text-center" style="margin: 0 20px">
    <div style="margin-top: 150px">
      <div style="font-size: 25px;font-weight: bold">登录</div>
      <div style="margin-top: 20px">
        <q-btn-group>
          <q-btn
              v-model="loginOption"
              value="username"
              @click="changeLoginOption('username')"
              :color="loginOption === 'username' ? 'primary' : ''"
          >
            学号登录
          </q-btn>
          <q-btn
              v-model="loginOption"
              value="phone"
              @click="changeLoginOption('phone')"
              :color="loginOption === 'phone' ? 'primary' : ''"
          >
            手机号码登录
          </q-btn>
          <q-btn
              v-model="loginOption"
              value="email"
              @click="changeLoginOption('email')"
              :color="loginOption === 'email' ? 'primary' : ''"
          >
            邮箱登录
          </q-btn>
        </q-btn-group>
      </div>
    </div>
    <div style="margin-top: 50px">
      <template v-if="loginOption === 'username'">
        <q-form ref="formRef" v-model="form">
          <q-input
              v-model="form.username"
              label="学号"
              type="text"
              placeholder="学号"
              :prefix="true"
          >
            <template #prefix>
              <q-icon name="user" />
            </template>
          </q-input>
          <q-input
              v-model="form.password"
              label="密码"
              type="password"
              placeholder="密码"
              style="margin-top: 10px"
              :prefix="true"
          >
            <template #prefix>
              <q-icon name="lock" />
            </template>
          </q-input>
          <q-card-actions align="left" style="margin-top: 5px">
            <q-checkbox v-model="form.remember" label="记住我" />
            <q-link @click="router.push('/forget')">忘记密码？</q-link>
          </q-card-actions>
        </q-form>
      </template>
      <template v-else-if="loginOption === 'phone'">
        <q-form ref="formRef" v-model="form">
          <q-input
              v-model="form.phone"
              label="手机号码"
              type="tel"
              placeholder="手机号码"
              :prefix="true"
          >
            <template #prefix>
              <q-icon name="phone" />
            </template>
          </q-input>
          <q-input
              v-model="form.verificationCode"
              label="验证码"
              type="text"
              placeholder="验证码"
              :prefix="true"
              :append="true"
              :append-icon="verificationCodeSending ? 'hourglass_empty' : 'send'"
              :append-clickable="!verificationCodeSending"
              :append-ripple="false"
              :append-cursor="verificationCodeSending ? 'not-allowed' : 'pointer'"
              @append-click="sendVerificationCode"
          ></q-input>
        </q-form>
      </template>
      <template v-else-if="loginOption === 'email'">
        <q-form ref="formRef" v-model="form">
          <q-input
              v-model="form.email"
              label="邮箱地址"
              type="email"
              placeholder="邮箱地址"
              :prefix="true"
          >
            <template #prefix>
              <q-icon name="email" />
            </template>
          </q-input>
          <q-input
              v-model="form.password"
              label="密码"
              type="password"
              placeholder="密码"
              :prefix="true"
          >
            <template #prefix>
              <q-icon name="lock" />
            </template>
          </q-input>
        </q-form>
      </template>
    </div>
    <div style="margin-top: 40px">
      <q-btn @click="userLogin" style="width: 270px" color="positive" flat>立即登录</q-btn>
    </div>
    <q-separator>
      <span style="color: grey;font-size: 13px">没有账号</span>
    </q-separator>
    <div>
      <q-btn @click="router.push('/register')" style="width: 270px" color="warning" flat>注册账号</q-btn>
    </div>
  </div>
</template>

<script setup>
import { User, Lock } from '@quasar/extras/material-icons'
import { ref, reactive } from 'vue'
import { login } from '@/net'
import { useRouter } from 'vue-router'

const formRef = ref()
const form = reactive({
  username: '',
  password: '',
  remember: false
})

const rules = {
  username: [
    { required: true, message: '请输入学号' }
  ],
  password: [
    { required: true, message: '请输入密码' }
  ]
}

const loginOption = ref('username')

function userLogin() {
  formRef.value.validate((isValid) => {
    if (isValid) {
      if (loginOption.value === 'username') {
        loginWithUsername(form.username, form.password, form.remember, () => router.push('/index'))
      } else if (loginOption.value === 'phone') {
        loginWithPhone(form.username, form.password, form.remember, () => router.push('/index'))
      } else if (loginOption.value === 'email') {
        loginWithEmail(form.username, form.password, form.remember, () => router.push('/index'))
      }
    }
  })
}

function changeLoginOption(option) {
  loginOption.value = option
}

function loginWithUsername(username, password, remember, callback) {
  const router = useRouter()
  router.push('/Main')
}

function loginWithPhone(phone, password, remember, callback) {
  const router = useRouter()
  router.push('/Main')
}

function loginWithEmail(email, password, remember, callback) {
  const router = useRouter()
  router.push('/Main')
}
</script>

<style scoped>

</style>
