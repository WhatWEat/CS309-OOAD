<template>
  <div>1123</div>
  <div>{{userid}}</div>
  <div>{{username}}</div>
  <q-btn @click="userid++"></q-btn>
</template>

<script setup>
import {useUser} from "stores/user-store";
import {storeToRefs} from "pinia";

const userStore = useUser()
userStore.fetchUser()
// 用写好的函数获取数据，基本fetch一次就好，他会在关闭前保留在内存中
const {username, userid} = storeToRefs(userStore)
// 通过storeToRefs将store中的数据转换为ref,username和userid都是ref类型可以在template中直接使用
// userid改变时，内存中userStore存储的数据也会改变
console.log(userStore)
</script>

<style scoped>

</style>
