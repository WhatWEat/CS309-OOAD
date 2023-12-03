<template>
  <q-expansion-item
    popup
    header-class="bg-blue-1"
  >
    <template v-slot:header>
      <q-item-section>

        <q-item-label class="text-h6">{{ project.name }}</q-item-label>
        <q-item-label caption>
          <q-badge rounded floating :color="isGroup ? 'green' : 'red'"/>
          {{ project.teacherName }}
        </q-item-label>
      </q-item-section>
    </template>

    <q-separator/>
    <q-card>
      <q-tab-panels v-model="tab" animated>
        <q-tab-panel name="des">
          {{ project.description }}
        </q-tab-panel>

        <q-tab-panel name="group" v-if="isGroup">
          <div class="text-h6">
            <q-avatar>
              <q-icon color="blue" name="groups"></q-icon>
            </q-avatar>

            <span>
              {{ leader.name }}
              <q-tooltip anchor="center right" self="center left" class="bg-blue">
                <q-badge color="blue" v-for="skill in leader.programmingSkills" :key="skill">
                  {{ skill }}
                </q-badge>
              </q-tooltip>
            </span>

            <q-badge rounded color="red" :label="GroupMember.length + 1"/>
          </div>
          <q-separator/>
          <q-list dense class="row">
            <q-item class="flex-center" clickable v-ripple v-for="member in GroupMember"
                    :key="member.userId"
                    :to="`/person/${member.userId}`">
              <q-item-label>
                {{ member.name }}
                <q-tooltip class="bg-blue">
                  <q-badge color="blue" v-for="skill in member.programmingSkills" :key="skill">
                    {{ skill }}
                  </q-badge>
                </q-tooltip>
              </q-item-label>
            </q-item>
          </q-list>
        </q-tab-panel>
        <q-tab-panel name="group" v-else>
          <q-item dense clickable :to="`/projects/${project.projectId}/group-list`">
            <q-item-section class="row text-h6">
              <div>
                <span v-if="person_id==userid">
                  you are not in a group. Click to join a group
                </span>
                <span v-else>
                  She/he is not in a group. Click to invite her/him to join a group
                </span>
              </div>
            </q-item-section>
          </q-item>
        </q-tab-panel>
        <q-tab-panel name="intended">
          <div class="row q-gutter-sm">
            <q-chip
              v-for="(tag, index) in TeammateRequire"
              :key="index"
              square
              class="text-white"
              :color="getColor(index)"
              removable
              @remove="removeTeam(index)"
            >
              {{ tag }}
            </q-chip>
            <q-input
              v-if="userid==person_id"
              square
              borderless
              clearable
              dense
              autogrow
              v-model="inputTagsValue"
              placeholder="Add a tag"
              @keyup.enter="handleInputConfirm"
            />

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
        <q-tab name="group" label="Group"/>
        <q-tab name="intended" label="Intended teammates"/>
      </q-tabs>
    </q-card>
  </q-expansion-item>
</template>

<script setup lang="ts">
import {projectProps, GroupProps, personProps, defaultPerson} from "src/composables/comInterface";
import {defineProps, nextTick, onMounted, ref} from 'vue';
import {api} from "boot/axios";
import {usePersonId} from "src/composables/usefulFunction";
import {useUserStore} from "src/composables/useUserStore";

const tab = ref<string>('des')
const props = defineProps<{
  project: projectProps
}>()

// group
const isGroup = ref<boolean>(false)
const groupInfo = ref<GroupProps>()
const GroupMember = ref<personProps[]>([])
const leader = ref<personProps>(defaultPerson)
const group_id = ref<number>()
onMounted(() => {
  api.get(`/get_group_id/${props.project.projectId}`).then(res => {
    group_id.value = res.data.body
    console.log(group_id.value, 'group_id')
    isGroup.value = group_id.value !== undefined && group_id.value !== -1
    if (group_id.value !== undefined && group_id.value !== -1) {
      api.get(`/getGroupInfo/${group_id.value}`).then(res => {
        groupInfo.value = res.data.body
        setGroup()
      }).catch(err => {
        console.log(err)
      })
      return
    }

    // setGroup()
  }).catch(err => {
    console.log(err)
  })
})

//group method
function setGroup() {
  let leader_id = groupInfo.value?.leaderId;
  GroupMember.value = []
  if (!groupInfo.value?.memberIds) {
    return
  }

  for (const member of groupInfo.value.memberIds) {
    if (member == leader_id) {
      api.get(`/get_personal_info/${member}`).then(res => {
        leader.value = res.data.body
      }).catch(err => {
        console.log(err)
      })
    } else {
      api.get(`/get_personal_info/${member}`).then(res => {
        // console.log('group member', res.data.body)
        GroupMember.value.push(res.data.body)
      }).catch(err => {
        console.log(err)
      })
    }

  }

}

// intended change to intended teammate
const inputVisible = ref<boolean>(false)
const TeammateRequire = ref<string[]>(['会java', '懂springboot'])
const inputTagsValue = ref<string>('')
const person_id = ref(), {userid} = useUserStore();
onMounted(() => {
  // todo 改成不同人看的不一样
  person_id.value = usePersonId();
  api.get(`/intend_teammates/${person_id.value}/${props.project.projectId}/`).then(res => {
    TeammateRequire.value = res.data.body
    console.log('teammates', res.data.body)
  }).catch(err => {
    console.log(err)
  })
})

// intended teammates method
function getColor(index: number): string {
  const colors = ['primary', 'tertiary', 'positive', 'negative', 'info', 'warning'];
  return colors[index % colors.length];
}

// TODO Check if the intended teammate is in the group
function removeTeam(index: number) {
  api.delete(`/stu/delete_intend_teammates`, {
    data: {
      key: props.project.projectId,
      value: TeammateRequire.value[index]
    }
  }).then(res => {
    console.log(res.data)
  }).catch(err => {
    console.log(err)
  })
  TeammateRequire.value.splice(index, 1);
}

const handleClose = (tag: string) => {
  TeammateRequire.value.splice(TeammateRequire.value.indexOf(tag), 1)
}

const handleInputConfirm = () => {
  // todo 添加
  if (inputTagsValue.value) {
    TeammateRequire.value.push(inputTagsValue.value)
    console.log('tag add', inputTagsValue.value)
    let add_tags = [inputTagsValue.value];
    api.post(`/stu/add_intend_teammates`, {
      key: props.project.projectId,
      value: add_tags
    }).then(res => {
      console.log(res.data)
    }).catch(err => {
      console.log(err)
    })
  }
  inputTagsValue.value = ''
}
</script>

<style scoped>

</style>
