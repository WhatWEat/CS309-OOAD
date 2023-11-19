<template>
  <div class="row justify-center">
    <SearchBars @update:tags="handleTagsUpdate"
                class="q-mx-sm q-mt-md col-lg-6 col-md-9 col-xs-10"></SearchBars>
    <q-list v-if="identity===3" class="q-ma-sm q-mt-md col-lg-8 col-md-10 col-xs-11">
      <project-exten v-for="project in project_list" :key="project.projectId"
                     :project="project"></project-exten>
    </q-list>
    <q-list v-if="identity<=1 & 0<= identity" class="q-ma-sm q-mt-md col-lg-8 col-md-10 col-xs-11">
      <project-exten-tea v-for="project in project_list" :key="project.projectId"
                         :project="project" :identity="identity"></project-exten-tea>
    </q-list>
  </div>
</template>

<script setup lang="ts">

import {onMounted, ref} from "vue";
import {projectProps} from "src/composables/comInterface";
import ProjectExten from "components/PersonIndex/ProjectExten.vue";
import {watchEffect} from "vue-demi";
import {useUserStore} from "src/composables/useUserStore";
import SearchBars from "components/PersonIndex/SearchBars.vue";
import ProjectExtenTea from "components/PersonIndex/ProjectExtenTea.vue";

const origin_project_list = ref<projectProps[]>();
const project_list = ref<projectProps[]>();
origin_project_list.value = [{
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
// const {identity} = useUserStore();
const identity = ref(1)
project_list.value = origin_project_list.value
function handleTagsUpdate(tags: Set<string>) {
  if (origin_project_list.value){
    if(tags.size === 0){
      project_list.value = origin_project_list.value
      return
    }
    project_list.value = []
    for (const project of origin_project_list.value) {
      if (tags.has(project.projectName) || tags.has(project.teacherName) || tags.has(project.projectDescription)){
        project_list.value?.push(project)
      } else {
        for(const tag of tags){
          if (project.projectName.includes(tag) || project.teacherName.includes(tag) || project.projectDescription.includes(tag)){
            project_list.value?.push(project)
            break
          }
        }
      }
    }
  }
}
</script>

<style scoped>

</style>
