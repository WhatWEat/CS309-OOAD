<template>
  <q-toolbar class="bg-primary text-white rounded-borders row">
    <q-input
      dense
      input-class="text-left"
      color="teal"
      class="col bg-primary text-white"
      standout="bg-blue-6 text-white"
      v-model="currentInput"
      placeholder="enter to search"
      @keyup.enter="addTag"
    >
      <template v-slot:prepend>
        <q-chip
          v-for="(tag, index) in tags"
          :key="index"
          removable
          dense
          square
          class="bg-blue-8 text-white"
          @remove="removeTag(tag)"
        >
          {{ tag }}
        </q-chip>
      </template>
      <template v-slot:append>
        <q-icon v-if="currentInput === ''" name="search"/>
        <q-icon v-else name="clear" class="cursor-pointer" @click="currentInput = ''"/>
      </template>
    </q-input>
  </q-toolbar>

</template>

<script setup lang="ts">
import {ref} from "vue";
import {useQuasar} from "quasar";

const tags = ref<Set<string>>(new Set());
const currentInput = ref<string>('');
const $q = useQuasar();
const emit = defineEmits(['update:tags'])

function addTag() {
  if (currentInput.value) {
    if (tags.value.size > 3) {
      $q.notify({
          position: "top",
          message: "you can only add 4 tags",
          color: "warning"
        }
      )
    } else if (tags.value.has(currentInput.value)) {
      $q.notify({
          position: "top",
          message: "you have the same condition",
          color: "positive"
        }
      )
    } else {
      tags.value.add(currentInput.value);
      currentInput.value = '';
      notifyFather();
    }
  }
}

function removeTag(index: string) {
  tags.value.delete(index);
  notifyFather();
}

function notifyFather() {
  emit('update:tags', tags.value);
}
</script>

<style scoped>

</style>
