<template>
  <div class="col-lg-8 col-md-8 col-xs-12 col-sm-12">
    <q-card class="text-primary no-shadow bg-blue-4" bordered>
      <q-card-section class="text-h6 q-pa-sm">
        <div class="text-h6">Change Password</div>
      </q-card-section>
      <q-card-section class="q-pa-sm row">
        <q-item class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
          <q-item-section>
            Current Password
          </q-item-section>
        </q-item>
        <q-item class="col-lg-8 col-md-8 col-sm-12 col-xs-12">
          <q-item-section>
            <q-input type="password" dense outlined color="white" round
                     v-model="password_dict.current_password"
                     label="Current Password"/>
          </q-item-section>
        </q-item>
        <q-item class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
          <q-item-section>
            New Password
          </q-item-section>
        </q-item>
        <q-item class="col-lg-8 col-md-8 col-sm-12 col-xs-12">
          <q-item-section>
            <q-input type="password" dense outlined color="white" round v-model="password_dict.new_password"
                     label="New Password"/>
          </q-item-section>
        </q-item>
        <q-item class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
          <q-item-section>
            Confirm New Password
          </q-item-section>
        </q-item>
        <q-item class="col-lg-8 col-md-8 col-sm-12 col-xs-12">
          <q-item-section>
            <q-input type="password" dense outlined round color="white"
                     v-model="password_dict.confirm_new_password"
                     label="Confirm New Password"/>
          </q-item-section>
        </q-item>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn class="text-capitalize bg-info text-white">Change Password</q-btn>
      </q-card-actions>
    </q-card>
  </div>
</template>

<script setup>
import {ref} from 'vue';

const password_dict = ref({
  current_password: '',
  new_password: '',
  confirm_new_password: ''
})
</script>

<style scoped>

</style>
