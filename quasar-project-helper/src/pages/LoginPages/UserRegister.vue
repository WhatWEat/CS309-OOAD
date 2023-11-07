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
                autocomplete="current-password"
              />

              <q-input
                type="password"
                filled
                v-model="confirmPassword"
                label="确认密码"
                lazy-rules
                autocomplete="current-password"
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
import { useQuasar } from 'quasar';
import {api} from "boot/axios";

export default defineComponent({
  setup() {
    const $q = useQuasar()
    const router = useRouter()
    const studentId = ref('')
    const password = ref('')
    const confirmPassword = ref('')
    const phoneNumber = ref('')

    function goToLogin() {
      router.push('/Login')  // 替换为您登录页面的路由路径
    }
    function register() {


      api.post('/signup', {

        identity: 3,
        password: password.value,
        name: studentId.value,
        gender: "m",
      }).then((res) => {
        console.log(res)
        if (res.data.statusCode === 200) {
          router.push('/');

        }
        //   不要改动以下代码

      }).catch((err) => {
          $q.notify({
            message: err.response.data.msg,
            position: 'center'
          });

        console.log(err)

      })

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
  background-image: url('https://p8.itc.cn/images01/20210707/653f5d1b05cc4a3caabd0e15e676f7be.png');
  background-size: cover;
  backdrop-filter: blur(8px);
}


</style>
