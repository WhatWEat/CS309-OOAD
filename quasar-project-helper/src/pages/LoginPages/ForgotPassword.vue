<template>
  <q-layout view="lHh Lpr lFf">
    <q-page-container>
      <q-page class="flex bg-image flex-center">
        <q-card v-bind:style="$q.screen.lt.sm?{'width': '70%'}:{'width':'50%'}">
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
          <q-card-section class="row q-ma-sm" v-if="type==='phone'">
            <q-input
              dense
              outlined
              v-model="loginPhone"
              label="Phone"
              class="col-12"
            >
            </q-input>
            <q-space class="col-1"/>
            <q-input dense outline class="col-11" label="Code" v-model="phoneCode">
              <template v-slot:append>
                <q-btn
                  dense
                  flat
                  icon="send"
                  @click="sendCode"
                  v-if="countdown_phone === 0"
                />
                <div v-else>{{ countdown_phone }}s</div>
              </template>
            </q-input>
          </q-card-section>
          <q-card-section class="q-ma-sm row" v-if="type==='email'">
            <q-input
              dense
              outlined
              v-model="loginPhone"
              label="Email"
              class="col-12"
            >
            </q-input>
            <q-space class="col-1"/>
            <q-input dense outline class="col-11" label="Code" v-model="phoneCode">
              <template v-slot:append>
                <q-btn
                  dense
                  flat
                  icon="send"
                  @click="sendCode"
                  v-if="countdown_phone === 0"
                />
                <div v-else>{{ countdown_phone }}s</div>
              </template>
            </q-input>
          </q-card-section>
          <q-separator/>
          <q-form ref="password_input">
            <q-card-section class="q-pa-sm row">

              <q-item class="col-12">
                <q-item-section>
                  <q-input autocomplete="on"
                           type="password" dense outlined round
                           v-model="password_dict.new_password"
                           :rules="[validatePasswordComplexity]"
                           label="New Password"/>
                </q-item-section>
              </q-item>
              <q-item class="col-12">
                <q-item-section>
                  <q-input autocomplete="on"
                           type="password" dense outlined round
                           v-model="password_dict.confirm_new_password"
                           :rules="[checkPasswordsMatch]"
                           label="Confirm New Password"/>
                </q-item-section>
              </q-item>

            </q-card-section>

            <q-card-actions class="row">

              <q-btn
                class="col-2"
                label="Verify"
                type="button"
                color="primary"
                @click="verifyPhoneNumber"
              />
              <q-btn
                class="col-2"
                label="Login"
                type="button"
                color="primary"
                @click="goToLogin"
              />
              <q-space class="col-4"/>
              <q-btn
                icon="mail"
                round
                flat
                v-if="type==='phone'"
                @click="type='email'"
              >
              </q-btn>
              <q-btn
                icon="phone"
                round
                flat
                v-if="type==='email'"
                @click="type='phone'"
              />
            </q-card-actions>
          </q-form>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script setup>
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import {QForm} from "quasar";
import {api} from "boot/axios";

const router = useRouter()

const phoneNumber = ref('')
const verificationCode = ref('')

const loginPhone = ref(''), phoneCode = ref(''), countdown_phone = ref(0)
const loginEmail = ref(''), emailCode = ref(''), countdown_email = ref(0)
const type = ref('phone')
const countdown = ref(0)
const password_dict = ref({
  new_password: '',
  confirm_new_password: ''
})

function sendCode() {
  // TODO 发送手机验证码
  api.post(`/get_forget_password_code`,{
    key: type.value == 'phone' ? "1" : "2",
    value: loginPhone.value,
  }).then(res => {
    console.log('合成',email)
    $q.notify({
      color: "green",
      textColor: "white",
      icon: "mail",
      message: "Code has been sent",
    });
    countdown.value = 60;
    const interval = setInterval(() => {
      countdown.value--;
      if (countdown.value === 0) {
        clearInterval(interval);
      }
    }, 1000);
  }).catch(err => {
    console.log(err)
  })
}
const password_input = ref(null)
function verifyPhoneNumber() {
  // 验证手机号码和验证码的逻辑
  if (!password_input.value) return;
  password_input.value.validate().then((success) => {
    if (success) {
      // 验证成功
      console.log('验证成功')
    }
  })
}

function goToLogin() {
  router.push('/Login')
}

// 验证密码复杂度和两次密码是否一致
function checkPasswordsMatch(val) {
  return val === password_dict.value.new_password || 'Passwords do not match';
}

function validatePasswordComplexity(val) {
  const hasUpperCase = /[A-Z]/.test(val);
  const hasLowerCase = /[a-z]/.test(val);
  const hasNumber = /\d/.test(val);
  const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(val);

  if (!hasUpperCase) {
    return 'Password must contain at least one uppercase letter';
  }
  if (!hasLowerCase) {
    return 'Password must contain at least one lowercase letter';
  }
  if (!hasNumber) {
    return 'Password must contain at least one number';
  }
  if (!hasSpecialChar) {
    return 'Password must contain at least one special character';
  }
  return true;
}
</script>

<style>
.bg-image {
  background-image: url('https://p8.itc.cn/images01/20210707/653f5d1b05cc4a3caabd0e15e676f7be.png');
  background-size: cover;
  backdrop-filter: blur(8px);
}

</style>
