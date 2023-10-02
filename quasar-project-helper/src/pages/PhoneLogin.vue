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
          v-model="phoneNumber"
          label="Phone Number"
          :rules="[val => val && val.length === 11 || 'Please enter a valid phone number']"
          type="tel"
          @input="clearError"
        />
        <q-input
          filled
          v-model="verificationCode"
          label="Verification Code"
          :rules="[val => val && val.length === 6 || 'Please enter a valid verification code']"
          type="number"
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
    const phoneNumber = ref('')
    const verificationCode = ref('')
    const error = ref('')

    function clearError() {
      error.value = ''
    }

    function login() {
      // 执行使用手机号码和验证码进行登录的逻辑
      if (!phoneNumber.value || phoneNumber.value.length !== 11) {
        error.value = 'Please enter a valid phone number'
        return
      }

      if (!verificationCode.value || verificationCode.value.length !== 6) {
        error.value = 'Please enter a valid verification code'
        return
      }

      // 执行登录操作，验证手机号码和验证码的正确性

      // 登录成功后的逻辑
      console.log('Login successful')
    }

    return {
      phoneNumber,
      verificationCode,
      error,
      clearError,
      login
    }
  }
}
</script>
