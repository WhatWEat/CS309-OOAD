<template>
  <q-page class="column items-center">
    <CardComponent
      v-for="card in cardData"
      :key="card.title"
      v-bind="card"
      >
    </CardComponent>
  </q-page>
</template>

<script setup lang="ts">
import CardComponent, {CardInfoProps}from 'components/MainIndex/CardComponent.vue';
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
