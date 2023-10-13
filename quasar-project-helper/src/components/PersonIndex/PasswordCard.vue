<template>
  <div class="col-lg-7 col-md-8 col-xs-12 col-sm-12">
    <q-form>
      <q-card :class="props.bg_color">
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
              <q-input autocomplete="off"
                       type="password" dense outlined color="white" round
                       v-model="password_dict.new_password"
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
              <q-input autocomplete="off"
                       type="password" dense outlined round color="white"
                       v-model="password_dict.confirm_new_password"
                       label="Confirm New Password"/>
            </q-item-section>
          </q-item>
        </q-card-section>
        <q-card-actions align="center">
          <q-btn class="text-capitalize bg-primary text-white">Change Password</q-btn>
        </q-card-actions>
      </q-card>

    </q-form>
  </div>
</template>

<script setup>
import {defineProps, ref} from 'vue';

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
</script>

<style scoped>

</style>
