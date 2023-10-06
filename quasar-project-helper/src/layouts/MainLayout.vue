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
          Project Helper
        </q-toolbar-title>
        <q-btn flat round dense icon="person" @click="goPersonInfo"/>
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
        <router-view />
    </q-page-container>
  </q-layout>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import EssentialLink, { EssentialLinkProps } from 'components/EssentialLink.vue';
import {useRouter} from 'vue-router';

const essentialLinks: EssentialLinkProps[] = [
  {
    title: 'Announcements',
    icon: 'record_voice_over',
    link: 'https://chat.quasar.dev'
  },
  {
    title: 'Projects',
    icon: 'extension',
    link: '/projects/1',
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
    link: 'https://facebook.quasar.dev'
  },
];
const router = useRouter()
const leftDrawerOpen = ref(true)
const miniState = ref(true)
function goPersonInfo() {
  router.push('/person/0')
  console.log('goPersonInfo')
}
function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value
}
</script>
