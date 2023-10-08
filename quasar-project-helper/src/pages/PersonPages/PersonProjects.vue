<template>
  <div class="row justify-center">
    <ProjectCard
      class="q-ma-md"
      style="width: 70%;"
      v-for="i in 5" :key="i"></ProjectCard>
  </div>
</template>

<script setup>

import ProjectCard from 'components/PersonIndex/ProjectCard.vue';

</script>

<style scoped>

</style>
