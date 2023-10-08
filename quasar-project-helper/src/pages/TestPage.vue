<template>
  <div>
    <q-btn @click="textAxios"></q-btn>
  </div>
</template>

<script setup lang="ts">
import {ref} from 'vue';
import {api} from 'boot/axios';

async function textAxios() {
  const response = ref(null);
  try {
    response.value = await api.get('http://localhost:3000/api/assignment');
    console.log('request')
  } catch (error) {
    console.error(error);
  }
}

</script>
