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

        <q-tab-panel name="group">
          <div class="text-h6">
            <q-avatar>

              <q-icon color="blue" name="groups"></q-icon>
            </q-avatar>
            <span>
              {{ leader.name }}
              <q-tooltip anchor="center right" self="center left" class="bg-blue">
                <q-badge color="blue" v-for="skill in leader.skills" :key="skill">
                  {{ skill }}
                </q-badge>
              </q-tooltip>
            </span>

            <q-badge rounded color="red" :label="GroupMember.length + 1"/>
          </div>
          <q-separator/>
          <q-list dense class="row">
            <q-item class="flex-center" clickable v-ripple v-for="member in GroupMember"
                    :key="member.id"
                    :href="`/person/${member.id}`">
              <q-item-label>
                {{ member.name }}
                <q-tooltip class="bg-blue">
                  <q-badge color="blue" v-for="skill in member.skills" :key="skill">
                    {{ skill }}
                  </q-badge>
                </q-tooltip>
              </q-item-label>
            </q-item>
          </q-list>
        </q-tab-panel>

        <q-tab-panel name="skill">
          <div class="row">
            <el-tag
              v-for="(tag,index) in TeammateRequire"
              :key="tag"
              class="q-pa-xs"
              closable
              :disable-transitions="false"
              @close="handleClose(tag)"
              :type="tagsColor[index % tagsColor.length]"
            >
              {{ tag }}
            </el-tag>
            <el-input
              v-if="inputVisible"
              ref="InputRef"
              v-model="inputTagsValue"
              class="q-pa-xs"
              @keyup.enter="handleInputConfirm"
              @blur="handleInputConfirm"
            />
            <el-button v-else class="q-pa-xs" size="small" @click="showInput">
              + New Tag
            </el-button>

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
        <q-tab name="skill" label="Intended teammates"/>
      </q-tabs>
    </q-card>
  </q-expansion-item>
</template>

<script setup lang="ts">
import {projectProps, GroupMember} from "src/composables/comInterface";
import {defineProps, nextTick, ref} from 'vue';
import {ElInput} from "element-plus";

const tab = ref<string>('des')
const props = defineProps<{
  project: projectProps
}>()

// group
const isGroup = ref<boolean>(true)
const GroupMember = ref<GroupMember[]>([{name: 'andy', id: 1, skills: ['java','cpp']}, {name: 'andy1', id: 2, skills: ['springboot','vue']},
  {name: 'andy2', id: 3, skills: ['python']}])
const leader = ref<GroupMember>()
setGroup()

// skill change to intended teammate
const inputVisible = ref<boolean>(false)
const TeammateRequire = ref<string[]>(['会java', '懂springboot'])
const inputTagsValue = ref<string>('')
const InputRef = ref<InstanceType<typeof ElInput>>()
const tagsColor = ref<("success" | "info" | "warning" | "danger")[]>([
  'success',
  'info',
  'warning',
  'danger',
]);

//group method
function setGroup() {
  leader.value = GroupMember.value.shift()
  isGroup.value = GroupMember.value.length > 0;
}

// skill method
const handleClose = (tag: string) => {
  TeammateRequire.value.splice(TeammateRequire.value.indexOf(tag), 1)
}
const showInput = () => {
  inputVisible.value = true
  nextTick(() => {
    if (InputRef.value && InputRef.value.input) {
      InputRef.value.input.focus()
    }
  })
}
const handleInputConfirm = () => {
  if (inputTagsValue.value) {
    TeammateRequire.value.push(inputTagsValue.value)
  }
  inputVisible.value = false
  inputTagsValue.value = ''
}
</script>

<style scoped>

</style>
