<template>
  <q-page class="q-ma-md content-start">
    <CardSocial></CardSocial>
    <CardComponent
      v-for="card in cardData"
      :key="card.title"
      v-bind="card"
      @click="() => {
        router.push(card.link as string)
      }"
      class="q-my-lg"
    >
    </CardComponent>
  </q-page>
</template>

<script setup lang="ts">
import CardComponent, {CardInfoProps} from 'components/MainIndex/CardComponent.vue';
import {useUserStore} from 'src/composables/useUserStore';
import {useRouter} from 'vue-router';
import {onMounted, ref} from 'vue';
import {watchEffect} from 'vue-demi';
import CardSocial from "components/MainIndex/CardSocial.vue";

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
          title: 'Your Group',
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

<style scoped>

</style>
