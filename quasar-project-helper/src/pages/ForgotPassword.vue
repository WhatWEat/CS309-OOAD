<template>
  <q-layout view="lHh Lpr lFf">
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
                ForgotPassword
              </div>
            </div>
          </q-card-section>

          <q-card-section>
            <q-form class="q-gutter-md">
              <q-input
                filled
                v-model="phoneNumber"
                label="手机号码"
                lazy-rules
              />

              <q-input
                filled
                v-model="verificationCode"
                label="验证码"
              >
                <template v-slot:append>
                  <q-btn
                    flat
                    dense
                    label="获取验证码"
                    color="primary"
                    @click="getVerificationCode"
                  />
                </template>
              </q-input>

              <div style="display: flex; justify-content: space-between;">
                <div>
                  <q-btn
                    label="验证"
                    type="button"
                    color="primary"
                    @click="verifyPhoneNumber"
                  />
                </div>
                <div>
                  <q-btn
                    label="返回登录"
                    type="button"
                    color="primary"
                    @click="goToLogin"
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

    const phoneNumber = ref('')
    const verificationCode = ref('')

    function getVerificationCode() {
      // 发送验证码到手机号码的逻辑
      console.log('发送验证码到手机号码:', phoneNumber.value)
    }

    function verifyPhoneNumber() {
      // 验证手机号码和验证码的逻辑
      console.log('验证手机号码和验证码:', phoneNumber.value, verificationCode.value)
    }

    function goToLogin() {
      router.push('/Login')
    }

    return {
      phoneNumber,
      verificationCode,
      getVerificationCode,
      verifyPhoneNumber,
      goToLogin
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
