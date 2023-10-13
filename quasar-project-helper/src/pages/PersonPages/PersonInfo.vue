<template>
  <q-page class="q-pa-sm">
    <div class="row q-col-gutter-sm justify-center">
      <ProfileCard bg_color="bg-blue-1"></ProfileCard>
      <PasswordCard></PasswordCard>
    </div>
  </q-page>


</template>

<script setup>
import ProfileCard from 'components/PersonIndex/ProfileCard.vue';
import PasswordCard from 'components/PersonIndex/PasswordCard.vue';

</script>

<style scoped>

</style>
