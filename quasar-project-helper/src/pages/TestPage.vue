<template>
  <div class="q-pa-md">
    <PDFViewer getApiUrl="/tea/get_submitted_ass_file/1/12110003/Week8-Transport1(1).pdf"
               fileName="name.pdf">
    </PDFViewer>
  </div>
</template>

<script>
import {defineAsyncComponent, defineComponent, onMounted, ref} from 'vue'
import PDFViewer from "components/ViewComponent/PDFViewer.vue";

const loading = ref(false)
export default defineComponent({
  name: "AssignmentDetail",
  components: {
    PDFViewer: defineAsyncComponent(() => import('src/components/ViewComponent/PDFViewer.vue')),
  }
})
</script>
