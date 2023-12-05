<template>
  <div class="col-lg-7 col-md-8 col-xs-12 col-sm-12">
    <q-form ref="subform" class="rounded-xl">
      <q-card :class="`${props.bg_color} rounded-xl`">
        <q-card-section class="text-h6 q-pa-sm">
          <div class="text-h6">Change Password</div>
        </q-card-section>
        <q-card-section class="q-pa-sm row">
          <q-item class="col-lg-3 col-md-3 col-sm-3 col-xs-12">
            <q-item-section>
              Current Password
            </q-item-section>
          </q-item>
          <q-item class="col-lg-9 col-md-9 col-sm-9 col-xs-12">
            <q-item-section>
              <q-input autocomplete="on"
                       type="password" dense outlined color="white" round
                       v-model="password_dict.current_password"
                       label="Current Password"/>
            </q-item-section>
          </q-item>
          <q-item class="col-lg-3 col-md-3 col-sm-3 col-xs-12">
            <q-item-section>
              New Password
            </q-item-section>
          </q-item>
          <q-item class="col-lg-9 col-md-9 col-sm-9 col-xs-12">
            <q-item-section>
              <q-input autocomplete="on"
                       type="password" dense outlined color="white" round
                       v-model="password_dict.new_password"
                       :rules="[validatePasswordComplexity]"
                       label="New Password"/>
            </q-item-section>
          </q-item>
          <q-item class="col-lg-3 col-md-3 col-sm-3 col-xs-12">
            <q-item-section>
              Confirm New Password
            </q-item-section>
          </q-item>
          <q-item class="col-lg-9 col-md-9 col-sm-9 col-xs-12">
            <q-item-section>
              <q-input autocomplete="on"
                       type="password" dense outlined round color="white"
                       v-model="password_dict.confirm_new_password"
                       :rules="[checkPasswordsMatch]"
                       label="Confirm New Password"/>
            </q-item-section>
          </q-item>
        </q-card-section>
        <q-card-actions align="center">
          <q-btn class="text-capitalize bg-primary text-white rounded-lg" @click="changePassword">Change
            Password
          </q-btn>
        </q-card-actions>
      </q-card>

    </q-form>
  </div>
</template>

<script setup lang="ts">
import {defineProps, ref} from 'vue';
import {api} from "boot/axios";
import {QForm, useQuasar} from "quasar";

const $q = useQuasar(), subform = ref<InstanceType<typeof QForm> | null>(null);
const props = defineProps({
  bg_color: {
    required: false,
    default: 'bg-blue-1',
    type: String
  }
})
const password_dict = ref({
  current_password: '',
  new_password: '',
  confirm_new_password: ''
})

function checkPasswordsMatch(val: string) {
  return val === password_dict.value.new_password || 'Passwords do not match';
}

function validatePasswordComplexity(val: string) {
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

function changePassword() {
  if(!subform.value) return
  subform.value.validate().then((success) => {
    if (success) {
      api.post('/change_password', {
        "key": password_dict.value.current_password,
        "value": {
          "key": password_dict.value.new_password,
          "value": password_dict.value.confirm_new_password
        }
      }).then((res) => {
        if (res.data.statusCode !== 200) {
          throw res.data
        }
        $q.notify({
          type: 'positive',
          message: `change password successfully`
        })
        console.log(res)
      }).catch((err) => {
        $q.notify({
          type: 'negative',
          message: `fail to change password, please check your current password`
        })
        console.log(err)
      })
    } else {
      $q.notify({
        type: 'warning',
        position: 'center',
        message: `fail to change password, please finish the form`
      })
    }
  })

}
</script>

<style scoped>

</style>
