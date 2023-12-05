<template>
  <div class="q-gutter-sm row items-center no-wrap">
    <span class="text-h6 gt-sm">Hello, {{ username }}!</span>
    <q-btn round dense flat color="white"
           :icon="$q.fullscreen.isActive ? 'fullscreen_exit' : 'fullscreen'"
           @click="$q.fullscreen.toggle()"
           v-if="$q.screen.gt.sm">
    </q-btn>
    <q-btn round dense flat color="white" icon="notifications">
      <q-badge color="red" text-color="white" floating>
        {{ messageNumber }}
      </q-badge>
      <q-menu
      >
        <q-list style="min-width: 100px">
          <Messages :messages="messages"></Messages>
          <q-card class="text-center no-shadow no-border">
            <q-btn label="View All" style="max-width: 120px !important;" flat dense
                   to="/announcements"
                   class="text-indigo-8"></q-btn>
          </q-card>
        </q-list>
      </q-menu>
    </q-btn>
    <q-btn round flat @click="handleClickPerson">
      <q-avatar size="26px">
        <img :src="avatarSrc">
      </q-avatar>
    </q-btn>
  </div>
</template>

<script setup lang="ts">
import Messages from 'components/Layout/CardMessages.vue';
import {useUserStore} from 'src/composables/useUserStore';
import {useRouter} from 'vue-router';
import {useQuasar} from 'quasar';
import {onMounted, ref} from 'vue';
import {getAvatarUrl} from 'src/composables/usefulFunction'
import {api} from "boot/axios";
import {defaultNotice, noticeProps} from "src/composables/comInterface";

const router = useRouter()
const {username, userid} = useUserStore()
const $q = useQuasar()

function handleClickPerson() {
  router.push(`/person/${userid.value}`)
}

const avatarSrc = ref<string | null>('https://cdn.quasar.dev/img/boy-avatar.png'), messageNumber = ref(0);
const messages = ref<noticeProps[]>([])
onMounted(async () => {
  avatarSrc.value = (await getAvatarUrl());
  api.get('/notice-list/-1/0/999').then(res => {
    messageNumber.value = res.data.body.length
    messages.value = res.data.body;
    messages.value = messages.value.slice(0, 5)
    // console.log(res.data.body)
  }).catch(err => {
    console.log(err)
  })
})
</script>

<style scoped>

</style>
