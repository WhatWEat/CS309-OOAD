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

const essentialLinks: EssentialLinkProps[] = [
  {
    title: 'Announcements',
    icon: 'record_voice_over',
    link: 'https://chat.quasar.dev'
  },

  {
    title: 'Group',
    icon: 'supervisor_account',
    link: '/teacher/:teacherId/GroupInfo'
  },
  {
    title: 'Chat',
    icon: 'chat',
    link: 'https://forum.quasar.dev'
  },
  {
    title: 'Homework',
    caption: '@quasarframework',
    icon: 'article',
    link: 'https://twitter.quasar.dev'
  },
  {
    title: 'Grade',
    icon: 'school',
    link: 'https://quasar.dev'
  },
  {
    title: 'Person',
    icon: 'account_box',
    link: 'https://facebook.quasar.dev'
  },
];

const leftDrawerOpen = ref(true)
const miniState = ref(true)
function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value
}
</script>
