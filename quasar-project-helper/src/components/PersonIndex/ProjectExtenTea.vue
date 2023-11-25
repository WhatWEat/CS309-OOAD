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
      <q-item-section side v-if="identity<=1 && identity >= 0">
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
                          :key="person.id"
                          clickable
                          @click="moveToRight(person.id)"
                        >
                          <q-item-section class="row">
                            <span>
                              <q-chip square dense class="col-3" color="blue-4"> {{person.id}}</q-chip>
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
                          :key="person.id"
                          clickable
                          @click="moveToLeft(person.id)"
                        >
                          <q-item-section class="row">
                            <span>
                              <q-chip square dense class="col-3 text-white" color="primary"> {{person.id}}</q-chip>
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
          <q-list class="row">
            <q-item
              class="col-4"
              :href="`/person/${ta.id}`"
              v-for="ta in ta_list"
              :key="ta.id"
              clickable
            >
              <q-item-section class="row"
                              >
                <span>
                  <q-chip square dense class="col-3 text-white" color="primary"> {{ta.id}}</q-chip>
                  <span class="col">
                    {{ ta.name }}
                  </span>
                </span>
              </q-item-section>
            </q-item>
          </q-list>
        </q-tab-panel>
        <q-tab-panel name="group">
          <div class="row">
            <q-chip square v-for="group in group_list" :key="group.groupId" @click="clickGroup">
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
import {defaultGroup, GroupMember, GroupProps, projectProps} from "src/composables/comInterface";
import {computed} from "vue-demi";
import {api} from "boot/axios";

const tab = ref<string>('des');
const props = defineProps<{
  project: projectProps,
  identity: number
}>()
const project_show = ref<projectProps>(props.project);
// TAs
// all people is all the TAs, ta_list is the TAs in this project
const allPeople = ref<GroupMember[]>([]), ta_list = ref<GroupMember[]>([]);
const group_list = ref<GroupProps[]>([defaultGroup]);
onMounted(()=>{
  // TODO
  // use api to get all people
  allPeople.value = [{
    id: 0,
    name: 'person 1'
  }, {
    id: 1,
    name: 'person 2'
  }, {
    id: 2,
    name: 'person 3'
  }];
  ta_list.value = [{
    id: 0,
    name: 'person 1'
  }];
  // api.get(`/get_brief_groups_from_proj/${props.project.projectId}`).then(res => {
  //   console.log('group', res.data)
  //   group_list.value = res.data;
  // }).catch(err => {
  //   console.log(err)
  // })
})
// edit group instruct
const isAssignTA = ref<boolean>(false);
// edit part
const isEdit = ref<boolean>(false), project_edit = ref<projectProps>(props.project);
const filter = ref<string>(''), selectedPeople = ref<GroupMember[]>([]);
const filteredPeople = computed(() => {
  const num = Number(filter.value);
  return allPeople.value.filter(
    person => (person.name.toLowerCase().includes(filter.value.toLowerCase()) ||
        person.id.toString().includes(filter.value)) &&
      !selectedPeople.value.some(selected => selected.id === person.id)
  );
});

const moveToRight = (id: number) => {
  const person = allPeople.value.find(p => p.id === id);
  if (person && !selectedPeople.value.some(p => p.id === id)) {
    selectedPeople.value.push(person);
  }
};

const moveToLeft = (id: number) => {
  const index = selectedPeople.value.findIndex(p => p.id === id);
  if (index !== -1) {
    selectedPeople.value.splice(index, 1);
  }
};
// save info
function saveInfo() {
  console.log('save info')
  project_show.value = JSON.parse(JSON.stringify(project_edit.value));
  ta_list.value = JSON.parse(JSON.stringify(selectedPeople.value));

  // TODO
  // use api to submit
  console.log(project_edit.value)
}

function clickEdit() {
  isEdit.value = true
  project_show.value = JSON.parse(JSON.stringify(project_edit.value));
  selectedPeople.value = JSON.parse(JSON.stringify(ta_list.value));
  console.log('click edit')
}
function clickGroup() {
  isAssignTA.value = true
}
function saveGroup() {
  console.log('save info')
  project_show.value = JSON.parse(JSON.stringify(project_edit.value));
  ta_list.value = JSON.parse(JSON.stringify(selectedPeople.value));

  // TODO
  // use api to submit
  console.log(project_edit.value)
}
</script>

<style scoped>

</style>
