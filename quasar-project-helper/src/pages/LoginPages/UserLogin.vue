<template>
  <q-layout view="lHh Lpr lFf">
    <q-page-container>
      <q-page class="flex bg-image flex-center row">
        <q-card class="col-lg-4 col-md-5 col-sm-6 col-xs-12">
          <q-card-section>
            <q-avatar size="103px" class="absolute-center shadow-10">
              <img src="profile.svg" />
            </q-avatar>
          </q-card-section>
          <q-card-section>
            <div class="text-center q-pt-lg">
              <div class="col text-h6 ellipsis">Log in</div>
            </div>
          </q-card-section>

          <q-card-section>
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
              <q-tab-panels v-model="loginType" animated class="row">
                <q-tab-panel name="studentId" class="col-12 row">
                  <q-input
                    dense
                    outlined
                    v-model="loginValue"
                    label="ID"
                    class="col-12"
                    :rules="getLoginValueRules()"
                  />
                  <q-input
                    dense
                    type="password"
                    outlined
                    class="col-12"
                    v-model="password"
                    label="password"
                    :rules="getPasswordRules()"
                    autocomplete="current-password"
                  />
                </q-tab-panel>
                <q-tab-panel name="email" class="col-12 row">
                  <q-input
                    dense
                    outlined
                    v-model="loginEmail"
                    label="Email"
                    class="col-12"
                    :suffix="selectedEmailDomain"
                  >
                    <template v-slot:append>
                      <q-btn-dropdown dense flat>
                        <q-list>
                          <q-item
                            clickable
                            v-close-popup
                            v-for="(item, index) in emailDomains"
                            :key="index"
                            @click="selectEmailDomain(index)"
                          >
                            <q-item-section>
                              <q-item-label>{{ item }}</q-item-label>
                            </q-item-section>
                          </q-item>
                        </q-list>
                      </q-btn-dropdown>
                    </template>
                  </q-input>
                  <q-input dense outline v-model="email_code" class="col-12" label="Code">
                    <template v-slot:append>
                      <q-btn
                        dense
                        flat
                        icon="send"
                        @click="sendEmailCode"
                        v-if="countdown === 0"
                      />
                      <div v-else>{{ countdown }}s</div>
                    </template>
                  </q-input>
                </q-tab-panel>
                <q-tab-panel name="phone" class="col-12 row">
                  <q-input
                    dense
                    outlined
                    v-model="loginPhone"
                    label="Phone"
                    class="col-12"
                  >
                  </q-input>
                  <q-input v-model="phone_code" dense outline class="col-12" label="Code">
                    <template v-slot:append>
                      <q-btn
                        dense
                        flat
                        icon="send"
                        @click="sendPhoneCode"
                        v-if="countdown_phone === 0"
                      />
                      <div v-else>{{ countdown_phone }}s</div>
                    </template>
                  </q-input>
                </q-tab-panel>
              </q-tab-panels>
              <div style="display: flex; justify-content: space-between">
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
                    label="forgot password"
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

<script setup>
import {defineComponent} from "vue";
import {ref} from "vue";
import {useRouter} from "vue-router";
import {useQuasar} from "quasar";
import {api} from "boot/axios";
import {useUserStore} from "src/composables/useUserStore";

const router = useRouter();
const $q = useQuasar();
const loginType = ref("studentId"); // 默认为学号登录
const loginValue = ref("");
const password = ref("");
// email login
const loginEmail = ref(""), email_code = ref(''),
  countdown = ref(0);
const selectedEmailDomain = ref("gmail.com");
const emailDomains = ref([
  "@gmail.com",
  "@yahoo.com",
  "@outlook.com",
  "@qq.com",
  "@sustech.edu.cn",
  "@mail.sustech.edu.cn",
]);

function selectEmailDomain(index) {
  selectedEmailDomain.value = emailDomains.value[index];
}

function sendEmailCode() {
  let email = loginEmail.value + selectedEmailDomain.value;
  api.post(`/request_code`, email,{
    headers: {
      'Content-Type': 'text/plain'
    }}).then(res => {
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

//phone login
const countdown_phone = ref(0);
const loginPhone = ref("");
const phone_code = ref("");
// TODO 没有清理上次登录的jwt
function sendPhoneCode() {
  api.post(`/request_massage`, loginPhone.value,{
    headers: {
      'Content-Type': 'text/plain'
    }}).then(res => {
    $q.notify({
      color: "green",
      textColor: "white",
      icon: "mail",
      message: "Code has been sent",
    });
    countdown_phone.value = 60;
    const interval = setInterval(() => {
      countdown_phone.value--;
      if (countdown_phone.value === 0) {
        clearInterval(interval);
      }
    }, 1000);
  }).catch(err => {
    console.log(err)
  })
}

function getLoginValueRules() {
  const rules = [];
  if (loginType.value === "studentId") {
    rules.push((val) => val.length === 8 || "Student ID must be 8 digits");
  } else if (loginType.value === "email") {
    rules.push((val) => /.+@.+\..+/.test(val) || "Incorrect email format");
  } else if (loginType.value === "phone") {
    rules.push(
      (val) => val.length === 11 || "Phone number must be 11 digits"
    );
  }

  return rules;
}

function getPasswordRules() {
  return [
    (val) =>
      val.length >= 6 || "The password length cannot be less than 6 digits",
  ];
}

function loginStudentId() {
  api.post("/login", {
    key: loginValue.value,
    value: password.value,
  })
  .then((res) => {
    console.log(res.data);
    if (res.data.statusCode === 200) {
      localStorage.setItem("Token", res.data.jwt_token);
      router.push("/");
    }
    //   不要改动以下代码
  })
  .catch((err) => {
    $q.notify({
      message: err.response.data.msg,
      position: "center",
    });
    console.log(err);
    console.log(loginValue.value);
    console.log(password.value);
  });
}
//1053246332
function loginByEmail() {
  console.log('code',email_code.value)
  let email = loginEmail.value+selectedEmailDomain.value;
  console.log('email',email)
  api.post(`/login_with_email_code`,{
    key: email,
    value: email_code.value,
  }).then(res => {
    console.log(res.data)
    if (res.data.statusCode === 200) {
      localStorage.setItem("Token", res.data.jwt_token);
      router.push("/");
    }
  }).catch(err => {
    console.log(err)
  })
}

function loginByPhone() {
  console.log('code',phone_code.value)
  console.log('code',loginPhone.value)
  api.post("/login_with_phone_code", {
    key: loginPhone.value,
    value: phone_code.value.toString(),
  }).then((res) => {
    console.log(res.data);
    if (res.data.statusCode === 200) {
      localStorage.setItem("Token", res.data.jwt_token);
      router.push("/");
    }
  }).catch((err) => {
    $q.notify({
      message: err.response.data.msg,
      position: "center",
    });
    console.log(err);
    console.log(loginPhone.value);
    console.log(phone_code.value);
});
}

function login() {
  if (loginType.value === "studentId") {
    loginStudentId();
  } else if (loginType.value === "email") {
    loginByEmail();
  } else if (loginType.value === "phone") {
    loginByPhone();
  }
  console.log("登录:", loginValue.value, password.value);
}

function goToRegister() {
  router.push("/Register");
}

function goToForgotPassword() {
  router.push("/ForgotPassword");
}

</script>

<style>
.bg-image {
  background-image: url("https://p8.itc.cn/images01/20210707/653f5d1b05cc4a3caabd0e15e676f7be.png");
  background-size: cover;
  backdrop-filter: blur(8px);
}
</style>
