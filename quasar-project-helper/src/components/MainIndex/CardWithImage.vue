<template>
  <div class="col-12" style="height: 400px">
    <q-card class="my-card no-shadow" bordered>
      <q-img
        :src="imgUrl"
        basic
        height="400px"
      >
        <div class="absolute-bottom-left bg-transparent q-ml-md">
          <div class="text-h1 gt-xs">
            {{ formatTime(currentTime) }}
          </div>
          <div class="text-h2 lt-sm">
            {{ formatTime(currentTime) }}
          </div>
          <div class="text-h5">
            {{ location }}
          </div>
        </div>
      </q-img>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {aw} from "app/dist/spa/assets/index.6e7c6110";

const imgUrls = ref<string[]>(['https://cdn.quasar.dev/img/parallax2.jpg', 'https://cdn.quasar.dev/img/parallax1.jpg',
  'https://pic.imgdb.cn/item/6560bfeec458853aefc5ac10.jpg', 'https://pic.imgdb.cn/item/6560c06cc458853aefc80277.webp', 'https://pic.imgdb.cn/item/6560c06cc458853aefc8031c.jpg'])
const imgUrl = imgUrls.value[Math.floor(Math.random() * imgUrls.value.length)]
const location = ref<string | null>(null);
const currentTime = ref(new Date());
setInterval(() => {
  currentTime.value = new Date();
}, 1000);
if ('geolocation' in navigator) {
  navigator.geolocation.getCurrentPosition(
    async (position) => {
      const {latitude, longitude} = position.coords;

      const response = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${latitude}&lon=${longitude}&accept-language=en`);
      const data = await response.json();
      location.value = data.address.city;
    },
    (error) => {
      console.error("Error Code = " + error.code + " - " + error.message);
    }
  );
} else {
  console.error("Geolocation is not supported by this browser.");
}
function formatTime(date: Date) {
  const hours = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');
  const seconds = date.getSeconds().toString().padStart(2, '0');
  return `${hours}:${minutes}:${seconds}`;
}
</script>

<style scoped>

</style>
