<template>
  <q-layout view="hHh lpR fFf">
    <q-header elevated>
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="toggleLeftDrawer"
        />
        <q-toolbar-title>
          <span> Welcome to Project Helper</span>&nbsp;
          <q-icon name="waving_hand"></q-icon>
        </q-toolbar-title>
        <span class="text-blue-grey-2 text-h6">{{ username }}</span>
        <q-btn flat round dense icon="person" @click="() => router.push(`/person/${userid}`)"
        />
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      :mini="miniState"
      @mouseover="miniState = false"
      @mouseout="miniState = true"

      :width="200"
      :breakpoint="500"
      bordered
      class="bg-grey-3"
    >
      <q-list>
        <EssentialLink
          v-for="link in essentialLinks"
          :key="link.title"
          v-bind="link"
        />
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import EssentialLink, {EssentialLinkProps} from 'components/EssentialLink.vue';
import {useRouter} from 'vue-router';
import {useUserStore} from 'src/composables/useUserStore';
import {watchEffect} from 'vue-demi';

const {username, userid} = useUserStore()

const essentialLinks = ref<EssentialLinkProps[]>([]);
onMounted(() => {
  watchEffect(() => {
    essentialLinks.value =
      [
        {
          title: 'Announcements',
          icon: 'record_voice_over',
          link: 'https://chat.quasar.dev'
        },
        {
          title: 'Projects',
          icon: 'extension',
          link: '',
          list: true
        },
        {
          title: 'Chat',
          icon: 'chat',
          link: 'https://forum.quasar.dev'
        },
        {
          title: 'Person',
          icon: 'account_box',
          link: `/person/${userid.value}`
        },
      ]
  })

});
const router = useRouter()
const leftDrawerOpen = ref(true)
const miniState = ref(true)


function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value
}
</script>
