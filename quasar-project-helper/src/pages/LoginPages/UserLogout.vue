<template>
  <q-layout view="lHh Lpr lFf">
    <q-page-container>
      <q-page class="flex bg-image flex-center row">
        <q-card class="col-lg-4 col-md-5 col-sm-6 col-xs-12">
          <q-card-section>
            <q-avatar size="103px" class="absolute-center shadow-10">
              <q-img :src="AvatarUrl"/>
            </q-avatar>
          </q-card-section>
          <q-card-section >
            <div class="text-center q-pt-lg">
              <div class="col text-h6 ellipsis">
                Log in {{userid}}
              </div>
            </div>
          </q-card-section>

          <q-card-section >
            <q-form class="q-gutter-md">

<!--              <q-input-->
<!--                filled-->
<!--                v-model="personId"-->
<!--                label="ID"-->
<!--              />-->

              <q-input
                type="password"
                filled
                v-model="password"
                label="password"
                :rules="getPasswordRules()"
                autocomplete="current-password"
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
                    label="Switch"
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
import {defineComponent, onMounted} from 'vue'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useQuasar } from 'quasar';
import {api} from 'boot/axios';
import {useUserStore} from 'src/composables/useUserStore';
import {getAvatarUrl} from "src/composables/usefulFunction";
import {useUser} from "../../stores/user-store";

export default defineComponent({
  setup() {
    const router = useRouter()
    const $q = useQuasar()
    const password = ref('')
    const {userid,username} = useUserStore()
    const personId = ref(router.currentRoute.value.params.userid)
    const AvatarUrl = ref()
    function getPasswordRules() {
      return [(val) => val.length >= 6 || 'The password length cannot be less than 6 digits']
    }
    function login() {
      api.post('/login', {
        key: userid.value,
        value: password.value
      }).then((res) => {
        console.log(res.data)
        if (res.data.statusCode === 200) {
          localStorage.setItem('Token', res.data.jwt_token);
          router.push('/');

        }
        //   不要改动以下代码
      }).catch((err) => {
        $q.notify({
          message: err.response.data.msg,
          position: 'center'
        });
        console.log(userid.value)
        console.log(personId.value)
        console.log(err)
        console.log(password)
      })
    }
    onMounted(async () => {
      AvatarUrl.value = await getAvatarUrl()
    })
    function goToLogin() {
      api.delete('/logout')
      const userStore = useUser();
      userStore.reset();
      router.push('/login')
    }

    return {
      password,
      getPasswordRules,
      login,
      userid,
      goToLogin,
      AvatarUrl
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
