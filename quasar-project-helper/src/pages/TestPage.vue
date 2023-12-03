<template>
  <div class="q-pa-md row">
    <MDViewer class="col-12">
    </MDViewer>
  </div>
</template>

<script setup lang="ts">
import {defineAsyncComponent, defineComponent, onMounted, ref} from 'vue'
import PDFViewer from "components/ViewComponent/PDFViewer.vue";
import MDViewer from "components/ViewComponent/MDViewer.vue";
import {api} from "boot/axios";

const loading = ref(false)
onMounted(()=>{
  api.get('/tea/get_ass_file/34/test.md').then((res)=>{
    console.log('response',res)
  }).catch((err)=>{
    console.log(err)
  })
})
</script>
