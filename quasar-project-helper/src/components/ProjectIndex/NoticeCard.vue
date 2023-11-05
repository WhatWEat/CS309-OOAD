<template>
  <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
    <q-card class="q-ma-xs no-shadow" bordered style="background-color: #1e88e5">
      <q-card-section class="text-h6 text-white">
        <q-avatar>
          <q-icon name="record_voice_over" color="white"></q-icon>
        </q-avatar>
        Latest Announcements
      </q-card-section>
      <q-card-section class="bg-white q-mb-sm q-mx-xs">
        <q-scroll-area style="height: 200px">
          <q-item v-for="msg in messages" :key="msg.noticeId" clickable v-ripple>
            <q-item-section>
              <q-item-label>{{ msg.title }}</q-item-label>
              <q-item-label caption lines="1" class="ellipsis">{{ truncate(msg.content) }}</q-item-label>
            </q-item-section>
            <q-item-section side>
              {{ msg.creatorId }}
            </q-item-section>
          </q-item>
        </q-scroll-area>
      </q-card-section>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {truncate} from 'src/composables/usefulFunction';
import {noticeProps, defaultNotice} from 'src/composables/comInterface';
import {api} from "boot/axios";

const messages = ref<noticeProps[]>([defaultNotice]);
// onMounted(() => {
//   api.get('/notice').then((res) => {
//     messages.value = res.data;
//   })
// })
</script>

<style scoped>

</style>
