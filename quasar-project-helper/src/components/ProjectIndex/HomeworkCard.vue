<template>
  <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
    <q-card class="q-ma-xs no-shadow" bordered style="background-color: #38b9c5">
      <q-card-section class="text-h6 text-white">
        <q-avatar>
          <q-icon name="menu_book" color="white"></q-icon>
        </q-avatar>
        Assignment
      </q-card-section>
      <q-card-section class="bg-white q-mb-sm q-mx-xs">
        <q-scroll-area style="height: 200px">
          <q-item v-for="msg in messages" :key="msg.id" clickable v-ripple>
            <q-item-section>
              <q-item-label>{{ msg.name }}</q-item-label>
              <q-item-label caption lines="1" class="ellipsis">{{ truncate(msg.msg) }}</q-item-label>
            </q-item-section>
            <q-item-section side>
              {{ msg.time }}
            </q-item-section>
          </q-item>
        </q-scroll-area>
      </q-card-section>
    </q-card>
  </div>
</template>

<script setup>
import {truncate} from 'src/composables/usefulFunction';
import {ref} from 'vue';

const messages = ref([
  {
    id: 5,
    name: 'Projects 1',
    msg: ' -- You \'ll be in your neighborhood doing errands this\n' +
      '            weekend. Do you want to grab brunch?',
    avatar: 'https://avatars2.githubusercontent.com/u/34883558?s=400&v=4',
    time: '10:42 PM'
  }, {
    id: 6,
    name: 'Projects 2',
    msg: ' -- You\'ll be in your neighborhood doing errands this\n' +
      '            weekend. Do you want to grab brunch?',
    avatar: 'https://cdn.quasar.dev/img/avatar6.jpg',
    time: '11:17 AM'
  }, {
    id: 1,
    name: 'Projects 3',
    msg: ' -- You\'ll be in your neighborhood ',
    avatar: 'https://cdn.quasar.dev/img/boy-avatar.png',
    time: '5:17 AM'
  }, {
    id: 2,
    name: 'Projects 2',
    msg: ' -- You\'ll be in your neighborhood doing errands this\n' +
      '            weekend. Do you want to grab brunch?',
    avatar: 'https://cdn.quasar.dev/team/jeff_galbraith.jpg',
    time: '5:17 AM'
  }, {
    id: 3,
    name: 'Projects 3',
    msg: ' -- You\'ll be in your neighborhood doing errands this\n' +
      '            weekend. Do you want to grab brunch?',
    avatar: 'https://cdn.quasar.dev/team/razvan_stoenescu.jpeg',
    time: '5:17 AM'
  }
])
</script>

<style scoped>

</style>
