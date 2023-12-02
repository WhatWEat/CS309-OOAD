<template>
  <div class="q-pa-md">
<!--    <PDFViewer getApiUrl="/tea/get_submitted_ass_file/1/12110003/Week8-Transport1(1).pdf" fileName="name.pdf" v-if="loading">-->
<!--    </PDFViewer>-->
    <q-btn @click="getToken"></q-btn>
  <q-img src="https://cs309-projecthelper-oss.oss-cn-shenzhen.aliyuncs.com/avatar/30002000/avator1.jpg"></q-img>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import PDFViewer from "components/ViewComponent/PDFViewer.vue";
import axios from "boot/axios";
const loading = ref(false)
onMounted(()=>{
  console.log('log')
  loading.value = true
})
function getToken(){
  axios.post('https://sm.ms/api/v2/').then
}

</script>
