<template>
  <div class="row justify-center">
    <q-list class="q-ma-sm q-mt-md col-lg-8 col-md-10 col-xs-11">
      <project-exten v-for="project in project_list" :key="project.projectId"
                     :project="project"></project-exten>
    </q-list>
  </div>
</template>

<script setup lang="ts">

import {onMounted, ref} from "vue";
import {projectProps} from "src/composables/comInterface";
import ProjectExten from "components/PersonIndex/ProjectExten.vue";

const project_list = ref<projectProps[]>()
project_list.value = [{
  projectId: 0,
  projectName: "Project 1",
  projectDescription: "11",
  teacherId: 0,
  teacherName: "teacher 1"
}, {
  projectId: 1,
  projectName: "Project 2",
  projectDescription: "teacher1 2",
  teacherId: 2,
  teacherName: "teacher 2"
},

]
</script>

<style scoped>

</style>
