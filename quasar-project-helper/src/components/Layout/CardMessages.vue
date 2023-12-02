<template>
  <div>
    <q-item style="width: 250px" v-for="msg in messageList" :key="msg.noticeId" clickable
            v-ripple>
      <q-item-section avatar>
        <q-avatar square>
          <img :src='avatarMap.get(msg.creatorId)'>
        </q-avatar>
      </q-item-section>

      <q-item-section>
        <q-item-label>{{ msg.title }}</q-item-label>
        <q-item-label caption lines="1"><div v-html="msg.content"></div></q-item-label>
      </q-item-section>

      <q-item-section side>
        {{ msg.creatorName }}
      </q-item-section>
    </q-item>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {noticeProps} from "src/composables/comInterface";
import {PropType} from "vue-demi";
import {getAvatarUrlById} from "src/composables/usefulFunction";

const props = defineProps({
  messages: {
    type: Array as PropType<noticeProps[]>,
    default: () => []
  }
})
const avatarMap = ref(new Map<number, string|null>());
const messageList = ref<noticeProps[]>(props.messages);
onMounted(async () => {
  for (const message of messageList.value) {
    if (!avatarMap.value.has(message.creatorId)) {
      avatarMap.value.set(message.creatorId, await getAvatarUrlById(message.creatorId))
    }
  }
  console.log('avatar',avatarMap.value.keys())
})
// const
// const messages = ref([
//   {
//     id: 5,
//     name: 'Pratik Patel',
//     msg: ' -- I\'ll be in your neighborhood doing errands this\n' +
//       '            weekend. Do you want to grab brunch?',
//     avatar: 'https://avatars2.githubusercontent.com/u/34883558?s=400&v=4',
//     time: '10:42 PM'
//   },
//   {
//     id: 6,
//     name: 'Winfield Stapforth',
//     msg: ' -- I\'ll be in your neighborhood doing errands this\n' +
//       '            weekend. Do you want to grab brunch?',
//     avatar: 'https://cdn.quasar.dev/img/avatar6.jpg',
//     time: '11:17 AM'
//   },
//   {
//     id: 1,
//     name: 'Boy',
//     msg: ' -- I\'ll be in your neighborhood doing errands this\n' +
//       '            weekend. Do you want to grab brunch?',
//     avatar: 'https://cdn.quasar.dev/img/boy-avatar.png',
//     time: '5:17 AM'
//   },
//   {
//     id: 2,
//     name: 'Jeff Galbraith',
//     msg: ' -- I\'ll be in your neighborhood doing errands this\n' +
//       '            weekend. Do you want to grab brunch?',
//     avatar: 'https://cdn.quasar.dev/team/jeff_galbraith.jpg',
//     time: '5:17 AM'
//   },
//   {
//     id: 3,
//     name: 'Razvan Stoenescu',
//     msg: ' -- I\'ll be in your neighborhood doing errands this\n' +
//       '            weekend. Do you want to grab brunch?',
//     avatar: 'https://cdn.quasar.dev/team/razvan_stoenescu.jpeg',
//     time: '5:17 AM'
//   }
// ],)

</script>

<style scoped>

</style>
