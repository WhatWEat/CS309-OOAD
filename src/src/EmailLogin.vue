<template>
  <q-layout>
    <q-page-container>
      <q-page class="flex bg-image flex-center">
        <q-card v-bind:style="$q.screen.lt.sm?{'width': '80%'}:{'width':'30%'}">
          <q-card-section>
            <q-avatar size="103px" class="absolute-center shadow-10">
              <img src="profile.svg">
            </q-avatar>
          </q-card-section>
      <q-card-section>
        <q-input
          filled
          v-model="email"
          label="Email"
          :rules="[val => validateEmail(val) || 'Please enter a valid email']"
          type="email"
          @input="clearError"
        />
        <q-input
          filled
          v-model="password"
          label="Password"
          :rules="[val => val && val.length >= 6 || 'Password must be at least 6 characters']"
          type="password"
          @input="clearError"
        />
      </q-card-section>
      <q-card-section class="q-pt-none">
        <q-btn
          label="Login"
          type="button"
          color="primary"
          @click="login"
        />
      </q-card-section>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script>
import { ref } from 'vue'

export default {
  setup() {
    const email = ref('')
    const password = ref('')
    const error = ref('')

    function clearError() {
      error.value = ''
    }

    function validateEmail(email) {
      // 使用正则表达式验证邮箱格式
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      return emailRegex.test(email)
    }

    function login() {
      // 执行使用邮箱和密码进行登录的逻辑
      if (!email.value || !validateEmail(email.value)) {
        error.value = 'Please enter a valid email'
        return
      }

      if (!password.value || password.value.length < 6) {
        error.value = 'Password must be at least 6 characters'
        return
      }

      // 执行登录操作，验证邮箱和密码的正确性

      // 登录成功后的逻辑
      console.log('Login successful')
    }

    return {
      email,
      password,
      error,
      clearError,
      login
    }
  }
}
</script>
