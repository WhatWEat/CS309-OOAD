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
        <q-infinite-scroll style="height: 200px">
          <q-item v-for="msg in messages" :key="msg.assignmentId" clickable v-ripple>
            <q-item-section>
              <q-item-label>{{ msg.title }}</q-item-label>
              <q-item-label caption lines="1" class="ellipsis">{{ truncate(msg.description) }}</q-item-label>
            </q-item-section>
            <q-item-section side>
              {{ formatDateString(msg.deadline) }}
            </q-item-section>
          </q-item>
        </q-infinite-scroll>
      </q-card-section>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import {truncate, useProjectId, formatDateString} from 'src/composables/usefulFunction';
import {onMounted, ref} from 'vue';
import {assProps} from "src/composables/comInterface";
import {api} from "boot/axios";
const messages = ref<assProps[]>([]);
onMounted(()=>{
  let projectid = useProjectId();
  api.get(`/ass-list/${projectid}/0/10`).then(res => {
    messages.value = res.data.body;
    console.log(messages)
  }).catch(err => {
    console.log(err)
  })
})
// TODO 完成无限滚动
// const messages = ref([
  // {
  //   id: 5,
  //   name: 'Projects 1',
  //   msg: ' -- You \'ll be in your neighborhood doing errands this\n' +
  //     '            weekend. Do you want to grab brunch?',
  //   avatar: 'https://avatars2.githubusercontent.com/u/34883558?s=400&v=4',
  //   time: '10:42 PM'
  // }, {
  //   id: 6,
  //   name: 'Projects 2',
  //   msg: ' -- You\'ll be in your neighborhood doing errands this\n' +
  //     '            weekend. Do you want to grab brunch?',
  //   avatar: 'https://cdn.quasar.dev/img/avatar6.jpg',
  //   time: '11:17 AM'
  // }, {
  //   id: 1,
  //   name: 'Projects 3',
  //   msg: ' -- You\'ll be in your neighborhood ',
  //   avatar: 'https://cdn.quasar.dev/img/boy-avatar.png',
  //   time: '5:17 AM'
  // }, {
  //   id: 2,
  //   name: 'Projects 2',
  //   msg: ' -- You\'ll be in your neighborhood doing errands this\n' +
  //     '            weekend. Do you want to grab brunch?',
  //   avatar: 'https://cdn.quasar.dev/team/jeff_galbraith.jpg',
  //   time: '5:17 AM'
  // }, {
  //   id: 3,
  //   name: 'Projects 3',
  //   msg: ' -- You\'ll be in your neighborhood doing errands this\n' +
  //     '            weekend. Do you want to grab brunch?',
  //   avatar: 'https://cdn.quasar.dev/team/razvan_stoenescu.jpeg',
  //   time: '5:17 AM'
  // }
// ])
</script>

<style scoped>

</style>
