<template>
  <q-layout view="lHh Lpr lFf">
    <q-page-container>
      <q-page class="flex bg-image flex-center">
        <q-card  v-bind:style="{
        width: $q.screen.lt.sm && $q.screen.width < 400 ? '100%' : ($q.screen.gt.sm ? '30%' : '80%')
      }">
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

          <q-card-section >
            <q-form class="q-gutter-md">
              <q-tabs
                v-model="loginType"
                no-caps
                dense
                class="bg-white text-black"
              >
                <q-tab name="studentId" label="ID" />
                <q-tab name="email" label="Email" />
                <q-tab name="phone" label="Phone" />
              </q-tabs>

              <q-input
                filled
                v-model="loginValue"
                :label="loginType === 'studentId' ? 'ID' : loginType === 'email' ? 'Email' : 'Phone'"
                :rules="getLoginValueRules()"
              />

              <q-input
                type="password"
                filled
                v-model="password"
                label="password"
                :rules="getPasswordRules()"
              />

              <div style="display: flex; justify-content: space-between;">
                <div>
                  <q-btn
                    label="login"
                    type="button"
                    color="primary"
                    @click="login"
                  />
                </div>
                <div>
                  <q-btn
                    label="register"
                    type="button"
                    color="primary"
                    @click="goToRegister"
                  />
                </div>
              </div>
              <div style="display: flex; justify-content: space-between;">
                <div>
                  <q-btn
                    label="forgot"
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
import { useQuasar } from 'quasar';
import {api} from 'boot/axios';

export default defineComponent({
  setup() {
    const router = useRouter()
    const $q = useQuasar()
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
        rules.push((val) => val.length === 8 || 'Student ID must be 8 digits')
      } else if (loginType.value === 'email') {
        rules.push((val) => /.+@.+\..+/.test(val) || 'Incorrect email format')
      } else if (loginType.value === 'phone') {
        rules.push((val) => val.length === 11 || 'Phone number must be 11 digits')
      }

      return rules
    }

    function getPasswordRules() {
      return [(val) => val.length >= 6 || 'The password length cannot be less than 6 digits']
    }
    function login() {

      api.post('/login', {
        key: loginValue,
        value: password
      }).then((res) => {
        if (res.data.statusCode === 200) {
          router.push('/');
        }
        console.log(res)
      //   不要改动以下代码
      }).catch((err) => {
        $q.notify({
          message: err.response.data.msg,
          position: 'center'
        });
        console.log(err)
      })
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
