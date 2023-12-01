<template>
  <q-card bordered class="">
    <!--   上方部分 -->
    <q-item>
      <q-item-section avatar>
        <q-avatar class="shadow-10" size="60px">
          <img src='https://avatars3.githubusercontent.com/u/34883558?s=400&u=09455019882ac53dc69b23df570629fd84d37dd1&v=4'>
        </q-avatar>
      </q-item-section>

      <q-item-section>

        <q-item-label class="text-grey-8 text-weight-bold">
          <span style="font-weight: bold;">
            Group ID :
          </span>
          <span style="font-weight: normal;">
            {{ groupId }}
          </span>
        </q-item-label>

        <q-item-label class="text-grey-8">
          <span style="font-weight: bold;">
            Group size :
          </span>
          <span style="font-weight: normal;">
            {{ groupSize }}
          </span>
        </q-item-label>
      </q-item-section>

      <q-item-section side>
        <slot name="right_btn"></slot>
      </q-item-section>
    </q-item>
    <!--    间隔线部分-->
    <q-separator size="3px"></q-separator>
    <!--    下方详细信息部分-->
    <q-card-section>
      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="groups"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">Members</q-item-label>
          <div class="row">
            <q-item-label caption lines="2">
              <q-input :disable="disableList.members" :model-value="Object.keys(members_temp).toString()" dense>
                <template v-slot:after>
                  <slot name="members_btn"></slot>
                </template>
              </q-input>
            </q-item-label>
            <q-item-label>
              <slot name="invite_detail_input"></slot>
            </q-item-label>
          </div>
        </q-item-section>
      </q-item>

      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="calendar_month"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">Creation Time</q-item-label>
          <q-item-label caption lines="2">
            <q-input v-model="creationTime_temp" :disable="disableList.creationTime" dense filled mask="date">
              <template v-slot:append>
                <q-icon class="cursor-pointer" name="event">
                  <q-popup-proxy ref="qDateProxy" cover transition-hide="scale" transition-show="scale">
                    <q-date v-model="creationTime_temp">
                      <div class="row items-center justify-end">
                        <q-btn v-close-popup color="primary" flat label="Close"/>
                      </div>
                    </q-date>
                  </q-popup-proxy>
                </q-icon>
              </template>
            </q-input>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="calendar_month"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">DeadLine</q-item-label>
          <q-item-label caption lines="2">
            <q-input v-model="deadLine_temp" :disable="disableList.deadLine" dense filled mask="date">
              <template v-slot:append>
                <q-icon class="cursor-pointer" name="event">
                  <q-popup-proxy ref="qDateProxy" cover transition-hide="scale" transition-show="scale">
                    <q-date v-model="deadLine_temp">
                      <div class="row items-center justify-end">
                        <q-btn v-close-popup color="primary" flat label="Close"/>
                      </div>
                    </q-date>
                  </q-popup-proxy>
                </q-icon>
              </template>
            </q-input>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="calendar_month"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">Presentation Time</q-item-label>
          <q-item-label caption lines="2">
            <q-input v-model="presentationTime_temp" :disable="disableList.presentationTime" dense filled mask="date">
              <template v-slot:append>
                <q-icon class="cursor-pointer" name="event">
                  <q-popup-proxy ref="qDateProxy" cover transition-hide="scale" transition-show="scale">
                    <q-date v-model="presentationTime_temp">
                      <div class="row items-center justify-end">
                        <q-btn v-close-popup color="primary" flat label="Close"/>
                      </div>
                    </q-date>
                  </q-popup-proxy>
                </q-icon>
              </template>
            </q-input>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="person"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">Instructor</q-item-label>
          <q-item-label caption lines="2">
            <q-input :disable="disableList.instructor" :model-value='Object.keys(instructor_temp).toString()'
                     dense></q-input>

          </q-item-label>
        </q-item-section>
      </q-item>


      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="person"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">Leader</q-item-label>
          <q-item-label caption lines="2">
            <q-input :disable="disableList.leader" :model-value="Object.keys(leader).toString()" dense></q-input>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="123"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">Max Size</q-item-label>
          <q-item-label caption lines="2">
            <q-input :disable="disableList.maxSize" :model-value="maxSize_temp" dense></q-input>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="more"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">More Information</q-item-label>
          <q-item-label caption lines="2">
            <q-input v-model="moreInformation_temp" :disable="disableList.moreInformation" dense></q-input>
          </q-item-label>
        </q-item-section>
      </q-item>
    </q-card-section>


    <q-separator></q-separator>
    <!--   最终的按钮部分 -->
    <!--    <slot name="button"></slot>-->
    <q-card-actions align="around">
      <q-btn-group :style="{'width':'100%'}" spread>
        <!--   点击后使该组件刷新回最初的状态-->
        <q-btn color="green" icon="" label="Reset" rounded @click="reset"/>
        <q-btn color="red" label="Submit Change" rounded/>
      </q-btn-group>
    </q-card-actions>

  </q-card>
</template>

<script>
import {defineComponent} from 'vue'
import {useUserStore} from 'src/composables/useUserStore';

export default defineComponent({
  name: "DirectoryCard",
  props: {
    'groupData': {
      type: Object,
      required: true,
      default: () => {
        return {
          groupId: '',
          maxSize: '',
          groupName: 'Dev Team',
          date1_deadline: '',
          date2_deadline: '',
          data1_presentation: '',
          data2_presentation: '',
          instructor: '',
          leader: '',
          members: [],
          technicalStack: [],
          desc: '',
        }
      }
    },
    'groupId': {
      type: String,
      required: true
    },
    "groupSize": {
      type: Number,
      required: true
    },
    'members': {
      type: Array,
      required: true
    },
    "creationTime": {
      type: String,
      required: true
    },
    "deadLine": {
      type: String,
      required: true
    },
    "presentationTime": {
      type: String,
      required: true
    },
    "leader": {
      type: Object,
      required: true
    },
    'instructor': {
      type: Object,
      required: true
    },
    "maxSize": {
      type: Number,
      required: true
    },
    "moreInformation": {
        type: String,
        required: true
      }
  },
  emits: ['update:groupId', 'update:groupSize', 'update:members', 'update:creationTime', 'update:deadLine', 'update:presentationTime', 'update:leader', 'update:maxSize', 'update:moreInformation'],
  methods: {
    reset() {
      this.groupId_temp = this.groupId.value
      this.groupSize_temp = this.groupSize
      this.members_temp = this.members
      this.creationTime_temp = this.creationTime
      this.deadLine_temp = this.deadLine
      this.presentationTime_temp = this.presentationTime
      this.instructor_temp = this.instructor
      this.leader_temp = this.leader
      this.maxSize_temp = this.maxSize
      this.moreInformation_temp = this.moreInformation
    }
  },
  data() {
    return {
      userData: useUserStore(),
      disableList: {
        members: true,
        creationTime: true,
        deadLine: true,
        presentationTime: false,
        instructor: true,
        leader: true,
        maxSize: true,
        moreInformation: false,
      },
      groupData_temp: this.groupData,


      groupId_temp: this.groupId.value,
      groupSize_temp: this.groupSize,
      members_temp: this.members,
      creationTime_temp: this.creationTime,
      deadLine_temp: this.deadLine,
      presentationTime_temp: this.presentationTime,
      leader_temp: this.leader,
      instructor_temp: this.instructor,
      maxSize_temp: this.maxSize,
      moreInformation_temp: this.moreInformation,
    }
  }
})
</script>

<style scoped>

</style>




