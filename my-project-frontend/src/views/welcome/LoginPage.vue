<template>
  <div style="text-align: center;margin: 0 20px">
    <div style="margin-top: 150px">
      <div style="font-size: 25px;font-weight: bold">登录</div>
      <div style="margin-top: 20px">
        <el-button-group>
          <el-button :type="loginOption === 'username' ? 'primary' : ''" @click="changeLoginOption('username')">学号登录</el-button>
          <el-button :type="loginOption === 'phone' ? 'primary' : ''" @click="changeLoginOption('phone')">手机号码登录</el-button>
          <el-button :type="loginOption === 'email' ? 'primary' : ''" @click="changeLoginOption('email')">邮箱登录</el-button>
        </el-button-group>
      </div>
    </div>
    <div style="margin-top: 50px">
      <template v-if="loginOption === 'username'">
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="username">
            <el-input v-model="form.username" maxlength="10" type="text" placeholder="学号">
              <template #prefix>
                <el-icon>
                  <User/>
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" maxlength="20" style="margin-top: 10px" placeholder="密码">
              <template #prefix>
                <el-icon>
                  <Lock/>
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-row style="margin-top: 5px">
            <el-col :span="12" style="text-align: left">
              <el-form-item prop="remember">
                <el-checkbox v-model="form.remember" label="记住我"/>
              </el-form-item>
            </el-col>
            <el-col :span="12" style="text-align: right">
              <el-link @click="router.push('/forget')">忘记密码？</el-link>
            </el-col>
          </el-row>
        </el-form>
      </template>
      <template v-else-if="loginOption === 'phone'">
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="phone">
            <el-input v-model="form.phone" maxlength="11" type="tel" placeholder="手机号码">
              <template #prefix>
                <el-icon>
                  <Phone/>
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="verificationCode">
            <el-input v-model="form.verificationCode" type="text" maxlength="6" placeholder="验证码">
              <template #prefix>
                <el-icon>
                  <Message/>
                </el-icon>
              </template>
              <template #append>
                <el-button @click="sendVerificationCode()" :disabled="verificationCodeSending" type="primary" size="mini">{{ verificationCodeButtonText }}</el-button>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
      </template>
      <template v-else-if="loginOption === 'email'">
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="email">
            <el-input v-model="form.email" type="email" placeholder="邮箱地址">
              <template #prefix>
                <el-icon>
                  <Email/>
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" maxlength="20" placeholder="密码">
              <template #prefix>
                <el-icon>
                  <Lock/>
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
      </template>
    </div>
    <div style="margin-top: 40px">
      <el-button @click="userLogin()" style="width: 270px" type="success" plain>立即登录</el-button>
    </div>
    <el-divider>
      <span style="color: grey;font-size: 13px">没有账号</span>
    </el-divider>
    <div>
      <el-button style="width: 270px" @click="router.push('/register')" type="warning" plain>注册账号</el-button>
    </div>
  </div>
</template>

<script setup>
import {User, Lock} from '@element-plus/icons-vue'
import router from "@/router";
import {reactive, ref} from "vue";
import {login} from '@/net'
import {useRouter} from "vue-router";

const formRef = ref()
const form = reactive({
  username: '',
  password: '',
  remember: false
})

const rules = {
  username: [
    { required: true, message: '请输入学号' }
  ],
  password: [
    { required: true, message: '请输入密码'}
  ]
}

const loginOption = ref('username')

function userLogin() {
  formRef.value.validate((isValid) => {
    if(isValid) {
      if (loginOption.value === 'username') {
        loginWithUsername(form.username, form.password, form.remember, () => router.push("/index"))
      } else if (loginOption.value === 'phone') {
        loginWithPhone(form.username, form.password, form.remember, () => router.push("/index"))
      } else if (loginOption.value === 'email') {
        loginWithEmail(form.username, form.password, form.remember, () => router.push("/index"))
      }
    }
  });
}

function changeLoginOption(option) {
  loginOption.value = option
}

function loginWithUsername(username, password, remember, callback) {
  const router = useRouter();
  router.push('/Main');
}

function loginWithPhone(phone, password, remember, callback) {
  const router = useRouter();
  router.push('/Main');
}

function loginWithEmail(email, password, remember, callback) {
  const router = useRouter();
  router.push('/Main');
}
</script>

<style scoped>

</style>
