<template>
  <q-expansion-item
    popup
    header-class="bg-blue-1"
  >
    <template v-slot:header>
      <q-item-section>
        <q-item-label class="text-h6">{{ project_show.name }}</q-item-label>
        <q-item-label caption>
          {{ project.teacherName }}
        </q-item-label>
      </q-item-section>
      <q-item-section side v-if="identity==1 && person_id == user_id || identity==0">
        <q-btn flat @click.stop="clickEdit" label="Edit"></q-btn>
      </q-item-section>
      <q-dialog v-model="isEdit" persistent>
        <q-card>
          <q-card-section class="row justify-center">
            <q-list style="width: 700px; max-width: 80vw;">
              <q-expansion-item
                header-class="text-h6"
                default-opened
                label="Project Information"
                group="someGroup"
              >
                <q-card>
                  <q-card-section>
                    <q-input v-model="project_edit.name" label="Project Name"/>
                    <q-input v-model="project_edit.description" label="Project Description"/>
                  </q-card-section>
                </q-card>
              </q-expansion-item>
              <q-separator></q-separator>
              <q-expansion-item
                header-class="text-h6"
                label="TA Management"
                group="someGroup"
              >
                <q-card>
                  <q-card-section class="row">
                    <div class="col">
                      <q-input v-model="filter" placeholder="Search TAs"/>
                      <q-list dense style="max-height: 200px; overflow-y: auto;">
                        <q-item
                          v-for="person in filteredPeople"
                          :key="person.userId"
                          clickable
                          @click="moveToRight(person.userId)"
                        >
                          <q-item-section class="row">
                            <span>
                              <q-chip square dense class="col-3" color="blue-4"> {{person.userId}}</q-chip>
                              <span class="col">
                                {{ person.name }}
                              </span>
                            </span>
                          </q-item-section>
                        </q-item>
                      </q-list>
                    </div>
                    <div class="col" style="height: 250px; overflow-y: auto;">
                      <q-list dense>
                        <q-item
                          v-for="person in selectedPeople"
                          :key="person.userId"
                          clickable
                          @click="moveToLeft(person.userId)"
                        >
                          <q-item-section class="row">
                            <span>
                              <q-chip square dense class="col-3 text-white" color="primary"> {{person.userId}}</q-chip>
                              <span class="col">
                                {{ person.name }}
                              </span>
                            </span>
                          </q-item-section>
                        </q-item>
                      </q-list>
                    </div>
                  </q-card-section>
                </q-card>
              </q-expansion-item>
            </q-list>


          </q-card-section>
          <q-card-actions align="right">
            <q-btn flat label="Cancel" color="primary" v-close-popup/>
            <q-btn flat label="Save" color="positive" @click="saveInfo" v-close-popup/>
          </q-card-actions>
        </q-card>
      </q-dialog>
    </template>

    <q-separator/>
    <q-card>
      <q-tab-panels v-model="tab" animated>
        <q-tab-panel name="des">
          {{ project_show.description }}
        </q-tab-panel>

        <q-tab-panel name="ta">
          <q-list class="row" v-if="ta_list.length!==0">
            <q-item
              class="col-4"
              :to="`/person/${ta.userId}`"
              v-for="ta in ta_list"
              :key="ta.userId"
              clickable
            >
              <q-item-section class="row">
                <span>
                  <q-chip square dense class="col-3 text-white" color="primary"> {{ta.userId}}</q-chip>
                  <span class="col">
                    {{ ta.name }}
                  </span>
                </span>
              </q-item-section>
            </q-item>
          </q-list>
          <div v-else>
            No TAs
          </div>
        </q-tab-panel>
        <q-tab-panel name="group">
          <div class="row">
            <q-chip square v-for="group in group_list" :key="group.groupId" @click="clickGroup" color="primary" class="text-white">
              {{group.groupName}}
              <q-badge color="green" floating transparent rounded>
                {{group.members.length}}
              </q-badge>
              <q-tooltip class="bg-indigo">
                Instructor: {{group.instructorName}}<br>
                GroupLeader: {{group.leaderName}}<br>
                GroupMember: {{group.members.join(', ')}}
              </q-tooltip>
            </q-chip>
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
import {defineProps, onMounted, ref} from "vue";
import {defaultGroup, personProps, GroupProps, projectProps} from "src/composables/comInterface";
import {computed, watchEffect} from "vue-demi";
import {api} from "boot/axios";
import {useQuasar} from "quasar";
import _ from 'lodash';
import GroupFrom from "components/Component_Li/form/GroupFrom.vue";
const $q = useQuasar();
const tab = ref<string>('des');
const props = defineProps<{
  project: projectProps,
  ta_list_all: personProps[] | undefined,
  identity: number | null,
  person_id: number | null,
  user_id: number | null
}>()
const project_show = ref<projectProps>(props.project);
// TAs
// all people is all the TAs, ta_list is the TAs in this project
const allPeople = ref<personProps[]>([]), ta_list = ref<personProps[]>([]);
const group_list = ref<GroupProps[]>([]);
onMounted(()=>{
  // use api to get all people
  api.get(`/tea/ta_list/${props.project.projectId}`).then(res => {
    ta_list.value = res.data.body;
    // console.log('ta list', props.project.projectId,ta_list.value)
  }).catch(err => {
    console.log(err)
  })
  watchEffect(() => {
    if (props.ta_list_all) {
      allPeople.value = props.ta_list_all;
      console.log(123123)
    } else {
      allPeople.value = [];
    }
  })
  api.get(`/get_brief_groups_from_proj/${props.project.projectId}`).then(res => {
    group_list.value = res.data.body;
  }).catch(err => {
    console.log(err)
  })
})
// edit group instruct
const isAssignTA = ref<boolean>(false);
// edit part
const isEdit = ref<boolean>(false), project_edit = ref<projectProps>(props.project);
const filter = ref<string>(''), selectedPeople = ref<personProps[]>([]);
const filteredPeople = computed(() => {
  // const num = Number(filter.value);
  return allPeople.value.filter(
    person => (person.name.toLowerCase().includes(filter.value.toLowerCase()) ||
        person.userId.toString().includes(filter.value)) &&
      !selectedPeople.value.some(selected => selected.userId === person.userId)
  );
});

const moveToRight = (userId: number) => {
  const person = allPeople.value.find(p => p.userId === userId);
  if (person && !selectedPeople.value.some(p => p.userId === userId)) {
    selectedPeople.value.push(person);
  }
};

const moveToLeft = (userId: number) => {
  const index = selectedPeople.value.findIndex(p => p.userId === userId);
  if (index !== -1) {
    selectedPeople.value.splice(index, 1);
  }
};
// save info
function saveInfo() {
  console.log('save info')

  if (project_show.value.name === project_edit.value.name &&
    project_show.value.description === project_edit.value.description) {
    console.log('no change')
  } else {
    project_show.value = JSON.parse(JSON.stringify(project_edit.value));
    api.post(`/edit_project/${props.project.projectId}`,{
      key: project_show.value.name,
      value: project_show.value.description
    }).then(res => {
      notifySuccess()
    }).catch(err => {
      notifyFailed()
      console.log(err)
    })
    console.log('change')
  }

  if (selectedPeople.value.length === 0 && _.isEqual(ta_list.value, selectedPeople.value)) {
    console.log('no change')
  } else {
    ta_list.value = JSON.parse(JSON.stringify(selectedPeople.value));
    let submit = ta_list.value.map(ta => ta.userId)
    // console.log('submit', submit)
    api.post(`/tea/designate_ta_to_proj`,{
      key: props.project.projectId,
      value: submit
    }).then(res => {
      notifySuccess()
    }).catch(err => {
      notifyFailed()
      console.log(err)
    })
    console.log('change')
  }
  // use api to submit
}
function notifySuccess() {
  $q.notify({
    color: 'positive',
    message: 'Edit Success',
    position: 'top'
  })
}
function notifyFailed() {
  $q.notify({
    color: 'negative',
    message: 'Edit Failed',
    position: 'top'
  })
}
function clickEdit() {
  isEdit.value = true
  project_show.value = JSON.parse(JSON.stringify(project_edit.value));
  selectedPeople.value = JSON.parse(JSON.stringify(ta_list.value));
  console.log('click edit', selectedPeople.value)
}
function clickGroup() {
  isAssignTA.value = true
}
function saveGroup() {
  console.log('save info')
  project_show.value = JSON.parse(JSON.stringify(project_edit.value));
  ta_list.value = JSON.parse(JSON.stringify(selectedPeople.value));

  // TODO 暂时不做了
  // use api to submit
  console.log(project_edit.value)
}
</script>

<style scoped>

</style>
