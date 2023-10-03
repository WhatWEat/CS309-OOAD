<template>
  <q-layout>
    <q-page-container>
      <q-page class="flex bg-image flex-center">
        <q-card v-bind:style="$q.screen.lt.sm?{'width': '80%'}:{'width':'30%'}">
          <q-card-section>
            <div class="text-center q-pt-lg">
              <div class="col text-h6 ellipsis">
                Register
              </div>
            </div>
          </q-card-section>
          <q-card-section>
            <q-form class="q-gutter-md">
              <q-input
                filled
                v-model="studentId"
                label="学号"
                lazy-rules
                :rules="[
                  val => /^[0-9]{8}$/.test(val) || '请输入8位有效学号'
                ]"
              />

              <q-input
                type="password"
                filled
                v-model="password"
                label="密码"
                lazy-rules
              />

              <q-input
                type="password"
                filled
                v-model="confirmPassword"
                label="确认密码"
                lazy-rules
                :rules="[
                  val => val === password || '前后两次输入密码不匹配'
                ]"
              />

              <q-input
                filled
                v-model="phoneNumber"
                label="手机号码"
              />

              <div>
                <q-btn label="注册" type="button" color="primary" @click="register" />
              </div>
              <div>
                <q-btn label="重新登录" type="button" color="primary" @click="goToLogin" />
              </div>
            </q-form>
          </q-card-section>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script>
import { defineComponent, ref } from 'vue'
import {useRouter} from "vue-router";

export default defineComponent({
  setup() {
    const router = useRouter()
    const studentId = ref('')
    const password = ref('')
    const confirmPassword = ref('')
    const phoneNumber = ref('')

    function goToLogin() {
      router.push('/Login-1')  // 替换为您登录页面的路由路径
    }
    function register() {
      // Add your registration logic here
      // You can access the entered studentId, password, and confirmPassword values using studentId.value, password.value, and confirmPassword.value
    }

    return {
      studentId,
      password,
      confirmPassword,
      phoneNumber,
      goToLogin,
      register
    }
  },
})
</script>

<style>
.bg-image {
  background-image: linear-gradient(135deg, #7028e4 0%, #e5b2ca 100%);
}
</style>
