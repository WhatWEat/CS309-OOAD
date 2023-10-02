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
            <q-form
              class="q-gutter-md"
            >
              <q-input
                filled
                v-model="studentId"
                label="StudentId"
                lazy-rules
              />

              <q-input
                type="password"
                filled
                v-model="password"
                label="Password"
                lazy-rules

              />

              <div>
                <q-btn label="Login" to="/" type="button" color="primary"/>
              </div>

              <div>
                <q-btn label="Register" to="/Register" type="button" color="primary" />
              </div>

              <q-card-section class="q-pt-none">
                <div class="q-mt-md">
                  <q-btn
                    label="Phone Login"
                    type="button"
                    color="primary"
                    class="q-mr-sm"
                    @click="goToPhoneLogin"
                  />
                  <q-btn
                    label="Email Login"
                    type="button"
                    color="primary"
                    @click="goToEmailLogin"
                  />
                </div>
              </q-card-section>
            </q-form>
          </q-card-section>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script>
import {defineComponent} from 'vue'
import {ref} from 'vue'
import { useRouter } from 'vue-router'

export default defineComponent({
  setup() {
    const router = useRouter()

    function goToPhoneLogin() {
      router.push('/PhoneLogin') // 替换为您的手机号码登录页面的路由路径
    }

    function goToEmailLogin() {
      router.push('/EmailLogin') // 替换为您的邮箱登录页面的路由路径
    }

    return {
      studentId: ref('111111'),
      password: ref('123456'),
      goToPhoneLogin,
      goToEmailLogin
    }
  },
})
</script>

<style>

.bg-image {
  background-image: linear-gradient(135deg, #7028e4 0%, #e5b2ca 100%);
}
</style>
