<template>
  <div class="q-pa-md row">
    <MDViewer :getContentUrl="md" class="col-12" v-if="loading">
    </MDViewer>
  </div>
</template>

<script setup lang="ts">
import {defineAsyncComponent, defineComponent, onMounted, ref} from 'vue'
import PDFViewer from "components/ViewComponent/PDFViewer.vue";
import MDViewer from "components/ViewComponent/MDViewer.vue";
import {api} from "boot/axios";

const loading = ref(false), md = ref('')
onMounted(()=>{
  md.value="/tea/get_ass_file/41/test.md"
  loading.value = true;
})
</script>
