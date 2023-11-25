<template>
  <div class="row justify-center">
    <q-card class="q-mx-sm q-mt-md col-lg-8 col-md-10 col-xs-11">
      <SearchBars @update:tags="handleTagsUpdate"
      ></SearchBars>
      <q-separator></q-separator>
      <q-skeleton type="text" v-if="!isLoading">
      </q-skeleton>
      <q-card-section v-else>
        <q-list v-if="identity===3" >
          <project-exten v-for="project in project_list" :key="project.projectId"
                         :project="project"></project-exten>
        </q-list>
        <q-list v-if="identity<=1 & 0<= identity"
        >
          <project-exten-tea v-for="project in project_list" :key="project.projectId"
                             :project="project" :identity="identity"></project-exten-tea>
        </q-list>
      </q-card-section>
    </q-card>
  </div>
</template>

<script setup lang="ts">

import {onMounted, ref} from "vue";
import {projectProps} from "src/composables/comInterface";
import ProjectExten from "components/PersonIndex/ProjectExten.vue";
import SearchBars from "components/PersonIndex/SearchBars.vue";
import ProjectExtenTea from "components/PersonIndex/ProjectExtenTea.vue";
import {watchEffect} from "vue-demi";
import {useUserStore} from "src/composables/useUserStore";
import {api} from "boot/axios";

const origin_project_list = ref<projectProps[]>();
const project_list = ref<projectProps[]>();
origin_project_list.value = [{
  projectId: 0,
  name: "Project 1",
  description: "11",
  teacherId: 0,
  teacherName: "teacher 1"
}, {
  projectId: 1,
  name: "Project 2",
  description: "teacher1 2",
  teacherId: 2,
  teacherName: "teacher 2"
},
]
const {identity} = useUserStore(), isLoading = ref(false);
onMounted(()=>{
  watchEffect(()=>{
    if(identity.value !== -1 && !isLoading.value){
      isLoading.value = true
      api.get('/project-list/0/10').then(res => {
        origin_project_list.value = res.data.body;
        project_list.value = origin_project_list.value
        isLoading.value = true
      }).catch(err => {
        console.log(err)
      })
    }
  })
})

function handleTagsUpdate(tags: Set<string>) {
  if (origin_project_list.value) {
    if (tags.size === 0) {
      project_list.value = origin_project_list.value
      return
    }
    project_list.value = []
    for (const project of origin_project_list.value) {
      if (tags.has(project.name) || tags.has(project.teacherName) || tags.has(project.description)) {
        project_list.value?.push(project)
      } else {
        for (const tag of tags) {
          if (project.name.includes(tag) || project.teacherName.includes(tag) || project.description.includes(tag)) {
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
