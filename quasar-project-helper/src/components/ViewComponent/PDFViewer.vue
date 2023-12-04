<template>
  <!--  Edited By Li Weihao-->
  <q-header elevated >
    <q-bar>
      <q-icon name="description"/>
      <span v-if="showAllPages">
        {{ pageCount }} page(s)
    </span>
      <span v-else>
        <button :disabled="page <= 1" @click="page--">❮</button>
        {{ page }} / {{ pageCount }}
        <button :disabled="page >= pageCount" @click="page++">❯</button>
    </span>
      <q-btn dense flat icon="file_download" size="sm" square @click="downloadBase64File(pdfSource, props.fileName)">
        <q-tooltip class="bg-white text-primary">DownLoad</q-tooltip>
      </q-btn>


      <q-space></q-space>
      <div>PDF Viewer</div>

      <q-space/>
      <div>Show All</div>
      <q-checkbox v-model="showAllPages" :color="'grey-7'" dense>
        <q-tooltip class="bg-white text-primary"> Show all pages
        </q-tooltip>
      </q-checkbox>
      <q-btn v-close-popup dense flat icon="close">
        <q-tooltip class="bg-white text-primary">Close</q-tooltip>
      </q-btn>
    </q-bar>
  </q-header>
  <!--  顶部部分-->
  <!--  <div class="app-header">-->
  <!--    <span v-if="showAllPages">-->
  <!--        {{ pageCount }} page(s)-->
  <!--    </span>-->
  <!--    <span v-else>-->
  <!--        <button :disabled="page <= 1" @click="page&#45;&#45;">❮</button>-->
  <!--        {{ page }} / {{ pageCount }}-->
  <!--        <button :disabled="page >= pageCount" @click="page++">❯</button>-->
  <!--    </span>-->
  <!--    <q-btn flat icon="file_download" size="sm" square @click="downloadBase64File(pdfSource, props.fileName)">-->
  <!--    </q-btn>-->
  <!--    <label class="right">-->
  <!--      <input v-model="showAllPages" type="checkbox">-->
  <!--      Show all pages-->
  <!--    </label>-->
  <!--  </div>-->
  <!-- 下面挂载的内容-->
  <div class="app-content">
    <q-skeleton v-if="!isLoading" height="100vh"></q-skeleton>
    <vue-pdf-embed
      v-else
      ref="pdfRef"
      :page="page"
      :source="pdfSource"
      @loaded="handleLoaded"
      @rendered="handleDocumentRender"
    />
  </div>
</template>

<script lang="ts" setup>
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
onMounted(() => {
  // console.log("mounte PDF界面")
  api.get(props.getApiUrl, {responseType: 'blob'}).then(res => {
    // console.log("成功api")
    blobToBase64(res.data).then(base64 => {
      pdfSource.value = base64;
      isLoading.value = true;
    })
  }).catch(err => {
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

  // console.log('loaded')
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
  const fileBlob = new Blob([byteArray], {type: 'application/octet-stream'});

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
