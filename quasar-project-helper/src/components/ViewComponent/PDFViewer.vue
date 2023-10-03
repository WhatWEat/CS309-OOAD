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
    <label class="right">
      <input v-model="showAllPages" type="checkbox">
      Show all pages
    </label>
  </div>
  <div class="app-content">
    <vue-pdf-embed
      ref="pdfRef"
      :source="pdfSource"
      :page="page"
      @rendered="handleDocumentRender"
    />
  </div>
</template>

<script setup>
import {ref, watch} from 'vue'
import VuePdfEmbed from 'vue-pdf-embed'

const pdfRef = ref(null)
const page = ref(null)
const pageCount = ref(1)
const showAllPages = ref(true)
const pdfSource = ref('https://raw.githubusercontent.com/mozilla/pdf.js/ba2edeae/web/compressed.tracemonkey-pldi-09.pdf')


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
