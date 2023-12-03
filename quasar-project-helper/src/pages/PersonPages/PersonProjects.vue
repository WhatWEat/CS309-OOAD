<template>
  <div class="row justify-center">
    <q-card class="q-mx-sm q-mt-md col-lg-8 col-md-10 col-xs-11">
      <SearchBars @update:tags="handleTagsUpdate"
      ></SearchBars>
      <q-separator></q-separator>
      <q-skeleton type="text" v-if="!isLoading">
      </q-skeleton>
      <q-card-section v-else-if="project_list?.length ?? false">
        <q-list v-if="identity===3">
          <project-exten v-for="project in project_list" :key="project.projectId"
                         :project="project"></project-exten>
        </q-list>
        <q-list v-if="identity<=1 && 0<= identity"
        >
          <project-exten-tea v-for="project in project_list" :key="project.projectId"
                             :project="project" :ta_list_all="ta_list" :identity="identity"
                             :user_id="userid" :person_id="person_id"></project-exten-tea>
        </q-list>
      </q-card-section>
      <q-card-section v-else>
        <q-item class="text-h3">
          There is no project
        </q-item>
      </q-card-section>
    </q-card>
  </div>
</template>

<script setup lang="ts">

import {onMounted, ref} from "vue";
import {personProps, projectProps} from "src/composables/comInterface";
import ProjectExten from "components/PersonIndex/ProjectExten.vue";
import SearchBars from "components/PersonIndex/SearchBars.vue";
import ProjectExtenTea from "components/PersonIndex/ProjectExtenTea.vue";
import {watchEffect} from "vue-demi";
import {useUserStore} from "src/composables/useUserStore";
import {api} from "boot/axios";
import {usePersonId} from "src/composables/usefulFunction";

const origin_project_list = ref<projectProps[]>();
const project_list = ref<projectProps[]>();
const ta_list = ref<personProps[]>();
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
const {userid} = useUserStore();
const isLoading = ref(false);
const identity = ref(-1)
const person_id = ref(-1);
onMounted(() => {
  person_id.value = usePersonId();
  if (person_id.value !== -1) {
    api.get(`/get_personal_info/${person_id.value}`).then(res => {
      identity.value = res.data.body.identity
    }).catch(err => {
      console.log(err)
    })
  }
  watchEffect(() => {
    if (identity.value !== -1 && !isLoading.value) {
      isLoading.value = true
      api.get(`/project-list/0/10/${person_id.value}`).then(res => {
        origin_project_list.value = res.data.body;
        project_list.value = origin_project_list.value
        isLoading.value = true
      }).catch(err => {
        console.log(err)
      })
      if (identity.value !== -1 && identity.value <= 1) {
        api.get('/tea/ta_list/-1').then(res => {
          ta_list.value = res.data.body;
          if (ta_list.value === undefined) ta_list.value = []
          isLoading.value = true
        }).catch(err => {
          console.log('获取不了')
          console.log(err)
        })
      }

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
