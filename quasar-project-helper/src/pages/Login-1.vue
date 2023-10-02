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
            <div class="text-center q-pt-lg">
              <div class="col text-h6 ellipsis">
                Log in
              </div>
            </div>
          </q-card-section>

          <q-card-section>
            <q-form class="q-gutter-md">
              <div style="display: flex; justify-content: space-between;">
                <div>
                  <q-btn
                    label="学号登录"
                    type="button"
                    color="primary"
                    @click="goToStudentIdLogin"
                  />
                </div>
                <div>
                  <q-btn
                    label="邮箱登录"
                    type="button"
                    color="primary"
                    @click="goToEmailLogin"
                  />
                </div>
                <div>
                  <q-btn
                    label="手机号码登录"
                    type="button"
                    color="primary"
                    @click="goToPhoneLogin"
                  />
                </div>
              </div>

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
      console.log('登录:', loginValue.value, password.value)
    }

    function goToRegister() {
      router.push('/register')
    }

    function goToForgotPassword() {
      router.push('/forgot-password')    }

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
  background-image: linear-gradient(135deg, #7028e4 0%, #e5b2ca 100%);
}
</style>
