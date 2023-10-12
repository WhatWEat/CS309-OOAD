<template>
  <div class="q-gutter-sm row items-center no-wrap">
    <span class="text-h6">Hello, {{username}}!</span>
    <q-btn round dense flat color="white" :icon="$q.fullscreen.isActive ? 'fullscreen_exit' : 'fullscreen'"
           @click="$q.fullscreen.toggle()"
           v-if="$q.screen.gt.sm">
    </q-btn>
    <q-btn round dense flat color="white" icon="notifications">
      <q-badge color="red" text-color="white" floating>
        5
      </q-badge>
      <q-menu
      >
        <q-list style="min-width: 100px">
          <Messages></Messages>
          <q-card class="text-center no-shadow no-border">
            <q-btn label="View All" style="max-width: 120px !important;" flat dense
                   class="text-indigo-8"></q-btn>
          </q-card>
        </q-list>
      </q-menu>
    </q-btn>
    <q-btn round flat @click="handleClickPerson">
      <q-avatar size="26px">
        <img src="https://cdn.quasar.dev/img/boy-avatar.png">
      </q-avatar>
    </q-btn>
  </div>
</template>

<script setup>
import Messages from 'components/Layout/CardMessages.vue';
import {useUserStore} from 'src/composables/useUserStore';
import {useRouter} from 'vue-router';
import {useQuasar} from 'quasar';
const router = useRouter()
const {username, userid} = useUserStore()
const $q = useQuasar()
function handleClickPerson() {
  router.push(`/person/${userid.value}`)
}
</script>

<style scoped>

</style>
