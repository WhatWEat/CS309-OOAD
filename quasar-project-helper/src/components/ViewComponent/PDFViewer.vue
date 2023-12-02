<template>
  <div class="app-header">
    <span v-if="showAllPages">
        {{ pageCount }} page(s)
    </span>
    <span v-else>
        <button :disabled="page <= 1" @click="page--">❮</button>
        {{ page }} / {{ pageCount }}
        <button :disabled="page >= pageCount" @click="page++">❯</button>
    </span>
    <q-btn flat size="sm" square icon="file_download" @click="downloadBase64File(pdfSource, props.fileName)">
    </q-btn>
    <label class="right">
      <input v-model="showAllPages" type="checkbox">
      Show all pages
    </label>
  </div>
  <div class="app-content">
    <q-skeleton height="100vh" v-if="!isLoading"></q-skeleton>
    <vue-pdf-embed
      ref="pdfRef"
      v-else
      @loaded="handleLoaded"
      :source="pdfSource"
      :page="page"
      @rendered="handleDocumentRender"
    />
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref, watch} from 'vue'
import VuePdfEmbed from 'vue-pdf-embed'
import {api} from "boot/axios";

const pdfRef = ref(null)
const page = ref(null)
const pageCount = ref(1)
const showAllPages = ref(true)
const isLoading = ref(false)
const pdfSource = ref('')
const props = defineProps<{
  getApiUrl: string
  fileName: string
}>();
onMounted(()=>{
  api.get(props.getApiUrl,{responseType: 'blob'}).then(res=>{
    blobToBase64(res.data).then(base64 =>{
      pdfSource.value = base64;
      isLoading.value = true;
    })
  }).catch(err=>{
    console.log(err)

  })
})
const handleDocumentRender = () => {
  pageCount.value = pdfRef.value.pageCount;
}
watch(showAllPages, (value) => {
  if (value) {
    page.value = null
  } else {
    page.value = 1
  }
})
// loading
function handleLoaded() {

  console.log('loaded')
}
const blobToBase64 = (blob: Blob): Promise<string> => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(blob);
    reader.onloadend = () => {
      resolve(reader.result as string); // Explicitly cast as string
    };
    reader.onerror = error => {
      reject(error);
    };
  });
};
function downloadBase64File(base64Data: string, fileName: string) {
  const byteCharacters = atob(base64Data.split(',')[1]);
  const byteNumbers = new Array(byteCharacters.length);
  for (let i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }
  const byteArray = new Uint8Array(byteNumbers);
  const fileBlob = new Blob([byteArray], { type: 'application/octet-stream' });

  const blobUrl = URL.createObjectURL(fileBlob);

  const link = document.createElement('a');
  link.href = blobUrl;
  link.download = fileName;
  document.body.appendChild(link);
  link.click(); // 模拟点击

  document.body.removeChild(link);
  URL.revokeObjectURL(blobUrl);
}
</script>

<style scoped>
body {
  margin: 0;
  padding: 0;
  background-color: #ccc;
}

.vue-pdf-embed > div {
  margin-bottom: 8px;
  box-shadow: 0 2px 8px 4px rgba(0, 0, 0, 0.1);
}

.app-header {
  padding: 16px;
  box-shadow: 0 2px 8px 4px rgba(0, 0, 0, 0.1);
  background-color: #555;
  color: #ddd;
}

.app-content {
  padding: 24px 16px;
}

.right {
  float: right;
}
</style>
