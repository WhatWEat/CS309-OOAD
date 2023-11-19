<template>
  <q-expansion-item
    popup
    header-class="bg-blue-1"
  >
    <template v-slot:header>
      <q-item-section>

        <q-item-label class="text-h6">{{ project.projectName }}</q-item-label>
        <q-item-label caption>
          {{ project.teacherName }}
        </q-item-label>
      </q-item-section>
    </template>

    <q-separator/>
    <q-card>
      <q-tab-panels v-model="tab" animated>
        <q-tab-panel name="des">
          {{ project.projectDescription }}
        </q-tab-panel>

        <q-tab-panel name="ta">
          <div class="row">

          </div>
        </q-tab-panel>
        <q-tab-panel name="group">
          <div class="row">

          </div>
        </q-tab-panel>
      </q-tab-panels>

      <q-separator/>

      <q-tabs
        v-model="tab"
        dense
        class="bg-blue-9 text-white"
        align="justify"
        narrow-indicator
      >
        <q-tab name="des" label="Description"/>
        <q-tab name="ta" label="TAs"/>
        <q-tab name="group" label="Group List"/>
      </q-tabs>
    </q-card>
  </q-expansion-item>
</template>

<script setup lang="ts">
import {defineProps, ref} from "vue";
import {GroupMember, projectProps} from "src/composables/comInterface";

const tab = ref<string>('des')
const props = defineProps<{
  project: projectProps,
  identity: number
}>()
</script>

<style scoped>

</style>
