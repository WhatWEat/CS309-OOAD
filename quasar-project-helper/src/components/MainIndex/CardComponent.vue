<template>
    <q-card >
      <q-card-section class="bg-blue-9 text-white">
        <div class="text-h6">{{title}}</div>
      </q-card-section>
      <q-separator dark inset />
      <q-card-section>
        {{caption}}
      </q-card-section>
    </q-card>
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
</style>
