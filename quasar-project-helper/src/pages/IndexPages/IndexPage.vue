<template>
  <q-page class="q-ma-md content-start">
    <CardSocial icon_position="right"></CardSocial>
    <CardCharts></CardCharts>

<!--      <TodoList></TodoList>-->
      <CardWithImage/>
  </q-page>
</template>

<script setup lang="ts">
import {CardInfoProps} from 'components/MainIndex/CardComponent.vue';
import {useUserStore} from 'src/composables/useUserStore';
import {onMounted, ref} from 'vue';
import {watchEffect} from 'vue-demi';
import CardSocial from 'components/MainIndex/CardSocial.vue';
import CardCharts from 'components/MainIndex/CardInformation.vue';
import TodoList from 'components/MainIndex/List/TodoList.vue';
import CardWithImage from 'components/MainIndex/CardWithImage.vue';


const userStore = useUserStore()
const cardData = ref<CardInfoProps[]>([]);
onMounted(() => {
  watchEffect(() => {
    const { userid } = userStore
    if (userid.value !== null){
      cardData.value = [
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
          link: `/person/${userid.value}/projects`
        },
        {
          title: 'Released Homework',
          caption: 'Released Homework',
        }
      ]
    }
  })
});
</script>
