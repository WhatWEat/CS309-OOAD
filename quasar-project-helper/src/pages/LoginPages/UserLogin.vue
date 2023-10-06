<template>
  <q-layout view="lHh Lpr lFf">
    <q-page-container>
      <q-page class="flex bg-image flex-center">
        <q-card v-bind:style="$q.screen.lt.sm?{'width': '80%'}:{'width':'30%'}" >
          <q-card-section>
            <q-avatar size="103px" class="absolute-center shadow-10">
              <img src="profile.svg">
            </q-avatar>
          </q-card-section>
          <q-card-section >
            <div class="text-center q-pt-lg">
              <div class="col text-h6 ellipsis">
                Log in
              </div>
            </div>
          </q-card-section>

          <q-card-section>
            <q-form class="q-gutter-md">
              <q-tabs
                v-model="loginType"
                no-caps
                class="bg-white text-black"
              >
                <q-tab name="studentId" label="学号登录" />
                <q-tab name="email" label="邮箱登录" />
                <q-tab name="phone" label="手机号码登录" />
              </q-tabs>

              <q-input
                filled
                v-model="loginValue"
                :label="loginType === 'studentId' ? '学号' : loginType === 'email' ? '邮箱' : '手机号码'"
                :rules="getLoginValueRules()"
              />

              <q-input
                type="password"
                filled
                v-model="password"
                label="密码"
                :rules="getPasswordRules()"
              />

              <div style="display: flex; justify-content: space-between;">
                <div>
                  <q-btn
                    label="登录"
                    type="button"
                    color="primary"
                    @click="login"
                  />
                </div>
                <div>
                  <q-btn
                    label="注册"
                    type="button"
                    color="primary"
                    @click="goToRegister"
                  />
                </div>
              </div>
              <div style="display: flex; justify-content: space-between;">
                <div>
                  <q-btn
                    label="忘记密码"
                    type="button"
                    color="primary"
                    class="q-mr-sm"
                    @click="goToForgotPassword"
                  />
                </div>
              </div>

            </q-form>
          </q-card-section>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script>
import { defineComponent } from 'vue'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

export default defineComponent({
  setup() {
    const router = useRouter()

    const loginType = ref('studentId') // 默认为学号登录
    const loginValue = ref('')
    const password = ref('')

    function goToStudentIdLogin() {
      loginType.value = 'studentId'
    }

    function goToEmailLogin() {
      loginType.value = 'email'
    }

    function goToPhoneLogin() {
      loginType.value = 'phone'
    }

    function getLoginValueRules() {
      const rules = []

      if (loginType.value === 'studentId') {
        rules.push((val) => val.length === 8 || '学号必须为8位')
      } else if (loginType.value === 'email') {
        rules.push((val) => /.+@.+\..+/.test(val) || '邮箱格式不正确')
      } else if (loginType.value === 'phone') {
        rules.push((val) => val.length === 11 || '手机号码必须为11位')
      }

      return rules
    }

    function getPasswordRules() {
      return [(val) => val.length >= 6 || '密码长度不能小于6位']
    }

    function login() {
      // 登录逻辑
      router.push('/')
      console.log('登录:', loginValue.value, password.value)
    }

    function goToRegister() {
      router.push('/Register')
    }

    function goToForgotPassword() {
      router.push('/ForgotPassword')    }

    return {
      loginType,
      loginValue,
      password,
      goToStudentIdLogin,
      goToEmailLogin,
      goToPhoneLogin,
      getLoginValueRules,
      getPasswordRules,
      login,
      goToRegister,
      goToForgotPassword
    }
  }
})
</script>

<style>
.bg-image {
  background-image: url('https://p8.itc.cn/images01/20210707/653f5d1b05cc4a3caabd0e15e676f7be.png');
  background-size: cover;
  backdrop-filter: blur(8px);
}

</style>
