<template>
  <div class="row justify-center">
    <q-list class="q-ma-sm q-mt-md col-lg-8 col-md-10 col-xs-10">
      <q-expansion-item
        v-for="(mail, index) in mail_data" :key="index"
        style="border-radius: 10px"
        popup
        header-class="bg-white"
      >
        <template v-slot:header>
          <q-item-section>
            <q-item-label>{{ mail.name }}</q-item-label>
            <q-item-label caption>{{ mail.date }}</q-item-label>
          </q-item-section>
        </template>

        <q-separator/>
        <q-card>
          <q-card-section>
            {{ mail.msg }}
          </q-card-section>
        </q-card>
      </q-expansion-item>
    </q-list>
  </div>
</template>

<script setup lang="ts">

import ProjectCard from 'components/PersonIndex/ProjectCard.vue';
import {onMounted, ref} from "vue";

const mail_data= ref([
  {
    name: 'Pratik Patel',
    avatar: 'https://avatars2.githubusercontent.com/u/34883558?s=400&v=4',
    date: 'March 12, 2019',
  },
  {
    name: 'Pratik Patel',
    avatar: 'https://avatars2.githubusercontent.com/u/34883558?s=400&v=4',
    date: 'March 22, 2019',
  },
  {
    name: 'Pratik Patel',
    avatar: 'https://avatars2.githubusercontent.com/u/34883558?s=400&v=4',
    date: 'March 12, 2019',
  },
  {
    name: 'Winfield Stapforth',
    avatar: 'https://cdn.quasar.dev/img/avatar6.jpg',
    date: 'March 22, 2019',
  },
  {
    name: 'Jeff Galbraith',
    avatar: 'https://cdn.quasar.dev/team/jeff_galbraith.jpg',
    date: 'March 12, 2019',
  },
  {
    name: 'Jeff Galbraith',
    avatar: 'https://cdn.quasar.dev/team/jeff_galbraith.jpg',
    date: 'March 22, 2019',
  },
  {
    name: 'Razvan Stoenescu',
    avatar: 'https://cdn.quasar.dev/team/razvan_stoenescu.jpeg',
    date: 'March 12, 2019',
  },
  {
    name: 'Razvan Stoenescu',
    avatar: 'https://cdn.quasar.dev/team/razvan_stoenescu.jpeg',
    date: 'March 22, 2019',
  },
  {
    name: 'John Doe',
    avatar: 'https://cdn.quasar.dev/img/boy-avatar.png',
    date: 'March 12, 2019',
  },
  {
    name: 'Pratik Patel',
    avatar: 'https://cdn.quasar.dev/img/boy-avatar.png',
    date: 'March 22, 2019',
  },
])
onMounted(() => {
  const messageText = 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Quidem, eius reprehenderit eos corrupti' +
    ' commodi magni quaerat ex numquam, dolorum officiis modi facere maiores architecto suscipit iste' +
    ' eveniet doloribus ullam aliquid.';
  mail_data.value = mail_data.value.map(item => ({ ...item, msg: messageText }));
})
</script>

<style scoped>

</style>
