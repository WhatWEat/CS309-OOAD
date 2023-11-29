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
          <div v-if="messages.length === 0" class="justify-center">
            You don't have any announcements yet.
          </div>
          <q-infinite-scroll v-else @load="getNotice" :offset="10">
            <q-list>
              <q-item v-for="msg in messages" :key="msg.noticeId" clickable v-ripple>
                <q-item-section>
                  <q-item-label>{{ msg.title }}</q-item-label>
                  <q-item-label caption lines="1" class="ellipsis">{{
                      truncate(msg.content)
                    }}
                  </q-item-label>
                </q-item-section>
                <q-item-section side>
                  {{ formatDateString(msg.createTime) }}
                </q-item-section>
              </q-item>
            </q-list>
            <template v-slot:loading>
              <div class="row justify-center q-my-md" style="height: 10px">
                <q-spinner-dots color="primary" size="40px"/>
              </div>
            </template>
          </q-infinite-scroll>
        </q-scroll-area>
      </q-card-section>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {truncate, formatDateString, useProjectId} from 'src/composables/usefulFunction';
import {api} from "boot/axios";
import {noticeProps} from "src/composables/comInterface";

// interface noticeProps {
//   noticeId: number;
//   projectId: number;
//   title: string;
//   content: string;
//   creatorId: number;
//   creatorName?: string;
//   createTime?: string;
//   stuView: null | string;
//   toAll: boolean;
// }
const messages = ref<noticeProps[]>([]);
const loadingMessage = ref(false), project_id = ref(0), page_notice = ref(0);
onMounted(() => {
  project_id.value = useProjectId()
  getNotice(0, () => void 0)
})

function getNotice(index = 0, done: () => void) {
  if (index > page_notice.value) {
    done()
    return
  }
  api.get(`/notice-list/${project_id.value}/${page_notice.value}/8`).then((res) => {
    let page_messages: noticeProps[] = res.data.body;
    console.log('page_messages', page_messages)
    messages.value.push.apply(messages.value, page_messages)
    loadingMessage.value = true;
    if (page_messages.length === 0) {
      done()
      return
    }
    page_notice.value++;
    done()
  }).catch((err) => {
    console.log('err', err)
  })
}
</script>

<style scoped>

</style>
