<template>
  <div>1123</div>
  <div>{{userid}}</div>
  <div>{{username}}</div>
  <q-btn @click="userid++"></q-btn>
</template>

<script setup>
import {useUserStore} from 'src/composables/useUserStore';
const {username, userid} = useUserStore()
</script>

<style scoped>

</style>
