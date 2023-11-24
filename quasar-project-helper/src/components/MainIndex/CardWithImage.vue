<template>
  <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12" style="height: 300px">
    <q-card class="my-card no-shadow" bordered>
      <q-img
        :src="imgUrl"
        basic
        height="350px"
      >
        <div class="absolute-bottom-left bg-transparent q-ml-md">
          <div class="text-h1">
            28 <sup>o</sup>
          </div>
          <div class="text-h5">
            Project Helper
          </div>
        </div>
      </q-img>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";

const imgUrls = ref<string[]>(['https://cdn.quasar.dev/img/parallax2.jpg', 'https://cdn.quasar.dev/img/parallax1.jpg',
  'https://pic.imgdb.cn/item/6560bfeec458853aefc5ac10.jpg', 'https://pic.imgdb.cn/item/6560c06cc458853aefc80277.webp', 'https://pic.imgdb.cn/item/6560c06cc458853aefc8031c.jpg'])
const imgUrl = imgUrls.value[Math.floor(Math.random() * imgUrls.value.length)]
</script>

<style scoped>

</style>
