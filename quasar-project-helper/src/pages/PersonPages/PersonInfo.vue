<template>
  <q-page class="q-pa-md">
    <div class="row q-col-gutter-md justify-center">

      <ProfileCard bg_color="bg-blue-1"></ProfileCard>
      <PasswordCard v-if="personId===userid"></PasswordCard>
    </div>
  </q-page>


</template>

<script setup lang="ts">
import ProfileCard from 'components/PersonIndex/ProfileCard.vue';
import PasswordCard from 'components/PersonIndex/PasswordCard.vue';
import {usePersonId} from "src/composables/usefulFunction";
import {useUserStore} from "src/composables/useUserStore";

const personId = usePersonId();
const {userid} = useUserStore();
</script>

<style scoped>

</style>
