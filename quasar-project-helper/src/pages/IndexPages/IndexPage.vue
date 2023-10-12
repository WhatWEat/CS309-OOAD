<template>
  <q-page class="q-ma-md content-start">
    <CardSocial icon_position="right"></CardSocial>
    <CardCharts></CardCharts>
    <div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
      <CardComponent
        v-for="card in cardData"
        :key="card.title"
        v-bind="card"
        @click="() => {
        router.push(card.link as string)
      }"
        class="q-ma-xs no-shadow"
      >
      </CardComponent>
    </div>

  </q-page>
</template>

<script setup lang="ts">
import CardComponent, {CardInfoProps} from 'components/MainIndex/CardComponent.vue';
import {useUserStore} from 'src/composables/useUserStore';
import {useRouter} from 'vue-router';
import {onMounted, ref} from 'vue';
import {watchEffect} from 'vue-demi';
import CardSocial from 'components/MainIndex/CardSocial.vue';
import CardCharts from "components/MainIndex/CardCharts.vue";

const router = useRouter()

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
