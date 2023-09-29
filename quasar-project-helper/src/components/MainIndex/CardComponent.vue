<template>
  <div class="q-pa-md row ">
    <q-card class="my-card">
      <q-card-section class="bg-green-8 text-white">
        <div class="text-h6">{{title}}</div>
      </q-card-section>
      <q-separator dark inset />
      <q-card-section>
        {{caption}}
      </q-card-section>
    </q-card>
  </div>
</template>

<script setup lang="ts">
export interface CardInfoProps {
  title: string;
  caption: string;
  link: string;
  icon: string;
}
withDefaults(defineProps<CardInfoProps>(), {
});
</script>

<style lang="sass" scoped>
.my-card
  width: 80vh
</style>
