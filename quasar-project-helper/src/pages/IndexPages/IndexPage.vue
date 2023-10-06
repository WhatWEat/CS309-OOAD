<template>
  <q-page class="q-ma-md content-start">
    <CardComponent
      v-for="card in cardData"
      :key="card.title"
      v-bind="card"
      class="q-my-lg"
      >
    </CardComponent>
  </q-page>
</template>

<script setup lang="ts">
import CardComponent, {CardInfoProps}from 'components/MainIndex/CardComponent.vue';

import {useUser} from "stores/user-store";
const users = useUser()
users.fetchUser()
const cardData: CardInfoProps[] = [
  {
    title: 'Announcements',
    caption: 'Announcements',
    icon: 'record_voice_over',
    link: 'https://chat.quasar.dev',
    color: 'green-1'
  },
  {
    title: 'Your Projects',
    caption: 'Group',
    icon: 'supervisor_account',
    link: ''
  },
  {
    title: 'Released Homework',
    caption: 'Released Homework',
  }
]
</script>
