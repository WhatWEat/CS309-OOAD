<template>
  <q-page class="q-pa-sm">
    <div class="row q-col-gutter-sm justify-center">
      <ProfileCard bg_color="bg-blue-1"></ProfileCard>
      <PasswordCard v-if="personId===identity"></PasswordCard>
    </div>
  </q-page>


</template>

<script setup lang="ts">
import ProfileCard from 'components/PersonIndex/ProfileCard.vue';
import PasswordCard from 'components/PersonIndex/PasswordCard.vue';
import {usePersonId} from "src/composables/usefulFunction";
import {useUserStore} from "src/composables/useUserStore";

const personId = usePersonId();
const {identity} = useUserStore();
</script>

<style scoped>

</style>
