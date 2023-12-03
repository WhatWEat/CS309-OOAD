<template>
  <div class="row">
    <q-markdown v-model:src="content" class="col-12" show-copy></q-markdown>
  </div>

</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {api} from "boot/axios";
const props = defineProps<{
  getContentUrl: string
}>()
const contentUrl = ref(''),content = ref('')
onMounted(()=>{
  contentUrl.value = props.getContentUrl;
  api.get(contentUrl.value).then((res)=>{
    content.value = res.data;
  }).catch((err)=>{
    console.log('error in md', err)
  })
})
</script>

<style scoped></style>

