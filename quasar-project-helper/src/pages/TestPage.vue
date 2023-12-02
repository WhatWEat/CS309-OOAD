<template>
  <div class="q-pa-md">
    <PDFViewer getApiUrl="/tea/get_submitted_ass_file/1/12110003/Week8-Transport1(1).pdf" fileName="name.pdf" v-if="loading">
    </PDFViewer>

  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import PDFViewer from "components/ViewComponent/PDFViewer.vue";
const loading = ref(false)
onMounted(()=>{
  console.log('log')
  loading.value = true
})


</script>
