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
  link?: string;
}
withDefaults(defineProps<CardInfoProps>(), {
});
</script>

<style lang="sass" scoped>
.my-card
  width: 100%
  border-radius: 10px
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.25)
  transition: box-shadow 0.25s ease-in-out
  &:hover
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.5)
</style>
