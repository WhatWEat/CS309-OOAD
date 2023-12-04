<template>
  <q-card bordered class="rounded-xl">
    <!--   上方部分 -->
    <q-item>
      <q-item-section avatar>
        <q-btn :flat="true" round>
          <q-avatar class="shadow-10" size="80px">
            <!--            <img :src=avatar>-->
            <q-img :src="avatar_tmp"
                   loading="lazy"
                   spinner-color="primary">
              <template v-slot:loading>
                <q-spinner size="40px"/>
              </template>
            </q-img>
          </q-avatar>
        </q-btn>
      </q-item-section>

      <q-item-section>
        <q-item-label class="text-grey-8 text-weight-bold">
          <span style="font-weight: bold;">
            Group ID :
          </span>
          <span style="font-weight: normal;">
            {{ groupData_temp.groupId }}
          </span>
        </q-item-label>
        <q-item-label class="text-grey-8 text-weight-bold">
          <span style="font-weight: bold;">
            Group size :
          </span>
          <span style="font-weight: normal;">
            {{ groupData_temp.memCnt }}
          </span>
        </q-item-label>
        <q-item-label class="text-grey-8 text-weight-bold">
          <span style="font-weight: bold;">
            Max size :
          </span>
          <span style="font-weight: normal;">
            {{ groupData_temp.maxsize }}
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
              <q-input :disable="disableList_temp.members" :model-value="Object.keys(groupData_temp.members).toString()"
                       dense>
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
          <q-item-label lines="1">Deadline</q-item-label>
          <q-item-label caption lines="2">
            <q-input v-model="groupData_temp.date1_deadline" :disable="disableList_temp.creationTime" dense filled
                     mask="date">
              <template v-slot:append>
                <q-icon class="cursor-pointer" name="event">
                  <q-popup-proxy ref="qDateProxy" cover transition-hide="scale" transition-show="scale">
                    <q-date v-model="groupData_temp.date1_deadline">
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

      <!--      <q-item clickable>-->
      <!--        <q-item-section avatar>-->
      <!--          <q-item-label lines="1">-->
      <!--            <q-avatar icon="calendar_month"></q-avatar>-->
      <!--          </q-item-label>-->
      <!--        </q-item-section>-->
      <!--        <q-item-section avatar>-->
      <!--          <q-item-label lines="1">DeadLine</q-item-label>-->
      <!--          <q-item-label caption lines="2">-->
      <!--            <q-input v-model="groupData_temp.date1_deadline" :disable="disableList_temp.deadLine" dense filled mask="date">-->
      <!--              <template v-slot:append>-->
      <!--                <q-icon class="cursor-pointer" name="event">-->
      <!--                  <q-popup-proxy ref="qDateProxy" cover transition-hide="scale" transition-show="scale">-->
      <!--                    <q-date v-model="deadLine_temp">-->
      <!--                      <div class="row items-center justify-end">-->
      <!--                        <q-btn v-close-popup color="primary" flat label="Close"/>-->
      <!--                      </div>-->
      <!--                    </q-date>-->
      <!--                  </q-popup-proxy>-->
      <!--                </q-icon>-->
      <!--              </template>-->
      <!--            </q-input>-->
      <!--          </q-item-label>-->
      <!--        </q-item-section>-->
      <!--      </q-item>-->

      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="calendar_month"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">Presentation Time</q-item-label>
          <q-item-label caption lines="2">
            <q-input v-model="groupData_temp.data1_presentation" :disable="disableList_temp.presentationTime" dense
                     filled mask="date">
              <template v-slot:append>
                <q-icon class="cursor-pointer" name="event">
                  <q-popup-proxy ref="qDateProxy" cover transition-hide="scale" transition-show="scale">
                    <q-date v-model="groupData_temp.data1_presentation">
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
            <q-input :disable="disableList_temp.instructor"
                     :model-value='Object.keys(groupData_temp.instructor).toString()'
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
            <q-input :disable="disableList_temp.leader" :model-value="Object.keys(groupData_temp.leader).toString()"
                     dense></q-input>
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
            <q-input :disable="disableList_temp.maxSize" :model-value="groupData_temp.maxsize" dense></q-input>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="check_box"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">Visibility</q-item-label>
          <q-item-label caption lines="2">
            <q-btn>
              <q-checkbox v-model="this.groupData_temp.visibility[0]" color="teal" keep-color label="GroupMember"/>
              <q-checkbox v-model="this.groupData_temp.visibility[1]" color="orange" keep-color label="Leader"/>
              <q-checkbox v-model="this.groupData_temp.visibility[2]" color="red" keep-color label="CreationTime"/>
              <q-checkbox v-model="this.groupData_temp.visibility[3]" color="cyan" keep-color label="Recruitment"/>
            </q-btn>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-item clickable>
        <q-item-section avatar>
          <q-item-label lines="1">
            <q-avatar icon="check_box"></q-avatar>
          </q-item-label>
        </q-item-section>
        <q-item-section avatar>
          <q-item-label lines="1">Member Administer</q-item-label>
          <q-btn :style="{'min-width':'130px'}" flat>
            <div v-for="(value,index) in this.groupData_temp.members" :key="value">
              <q-item-label v-if="value!== this.groupData_temp.leaderId" caption lines="2">
                <q-chip color="primary" icon="people" removable text-color="white"
                        @remove="delete this.groupData_temp.members[index]">
                  {{ index }}
                </q-chip>
              </q-item-label>
            </div>
          </q-btn>
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
            <q-input v-model="groupData_temp.description" :disable="disableList_temp.moreInformation" dense></q-input>
          </q-item-label>
        </q-item-section>
      </q-item>
    </q-card-section>

    <div v-if="isGroupLeader">
      <q-separator></q-separator>
      <!--   最终的按钮部分 -->
      <!--    <slot name="button"></slot>-->
      <q-card-actions align="around">
        <q-btn-group :style="{'width':'100%'}" class="rounded-xl" spread>
          <!--   点击后使该组件刷新回最初的状态-->
          <q-btn color="green" icon="" label="Reset" rounded @click="reset"/>
          <q-btn color="red" label="Submit Change" rounded @click="submitChange()"/>
        </q-btn-group>
      </q-card-actions>
    </div>
  </q-card>

  {{ this.groupData_temp }}<br/>
  Members:{{ this.groupData_temp.members }}<br/>
  Member:{{ Object.keys(this.groupData_temp.members)[0] }}
  MemberID:{{ Object.values(this.groupData_temp.members) }}
</template>

<script>
import {defineComponent} from 'vue'
import {useUserStore} from 'src/composables/useUserStore';
import cloneDeep from 'lodash/cloneDeep';
import {api} from "boot/axios";
import {getAvatarUrlById} from "src/composables/usefulFunction";

export default defineComponent({
  name: "DirectoryCard",
  props: {
    'groupData': {
      type: Object,
      required: true,
      default: () => {
        // return {
        //   groupId: '9999',
        //   maxSize: '9999',
        //   groupName: 'Dev Team',
        //   date1_deadline: '2002-10-1',
        //   date2_deadline: '10:00',
        //   data1_presentation: '2000-10-1',
        //   data2_presentation: '10:00',
        //   instructor: '20001000',
        //   leader: '20001000',
        //   members: [12110415,1211100,124,12],
        //   technicalStack: [],
        //   desc: 'Dev Team des',
        // }
        // return {
        //   "groupId": 1,
        //   "groupName": "group1",
        //   "creatorId": 30002000,
        //   "instructorId": 30002000,
        //   "instructorName": "Andy",
        //   "leaderId": 12110000,
        //   "leaderName": "stu0",
        //   "maxsize": 10,
        //   "projectId": 1,
        //   "teamTime": "2023-11-06T23:47:18.995108",
        //   "deadline": "2024-03-10T10:00:00",
        //   "reportTime": "2024-10-10T10:00:00",
        //   "description": null,
        //   "technicalStack": null,
        //   "visibility": [
        //     true,
        //     true,
        //     true,
        //     true
        //   ],
        //   "recruitment": null,
        //   "memberIds": [
        //     12110002,
        //     12110004,
        //     12110001,
        //     12110003,
        //     12110000
        //   ],
        //   "members": [
        //     "stu2",
        //     "stu4",
        //     "stu1",
        //     "stu0",
        //     "stu0"
        //   ],
        //   "memCnt": 5
        // }
        return {
          "groupId": 77777777,
          "groupName": "Dev group1",
          "creatorId": 77777777,
          "instructorId": 77777777,
          "instructor": {'Andy': 77777777},
          "instructorName": "Andy",
          'leader': {'stu0': 77777777},
          "leaderId": 77777777,
          "leaderName": "stu0",
          "maxsize": 77777777,
          "projectId": 77777777,
          "teamTime": "2077-11-06T23:47:18.995108",
          "deadline": "2077-03-10T10:00:00",
          "reportTime": "2077-10-10T10:00:00",
          "description": null,
          "technicalStack": null,
          "visibility": [
            true,
            false,
            true,
            false
          ],
          "recruitment": null,
          "memberIds": [
            12110002,
            12110004,
            12110001,
            12110003,
            12110000
          ],
          "members": [
            "stu2",
            "stu4",
            "stu1",
            "stu0",
            "stu0"
          ],
          "memCnt": 5,
          'date1_deadline': '2077-03-10',
          'date2_deadline': '10:00:00',
          'data1_presentation': '2099-10-10',
          'data2_presentation': '10:00:00',
        }
      }
    },
    'avatar': {
      type: String,
      required: true,
      default: 'https://files.oaiusercontent.com/file-x6PnnxUzina3lkZnqhf2XSMh?se=2023-12-01T07%3A59%3A06Z&sp=r&sv=2021-08-06&sr=b&rscc=max-age%3D31536000%2C%20immutable&rscd=attachment%3B%20filename%3D18c9f5e2-a43a-44a9-a177-7fcab28aa1fa.webp&sig=4iLXmf/mjsCzhYQ1VPwbYwfZUCxA34huyA8gBsoxsoY%3D'
    },
    'disableList': {
      type: Object, default: () => {
        return {
          members: true,
          creationTime: true,
          deadLine: true,
          presentationTime: false,
          instructor: true,
          leader: true,
          maxSize: true,
          moreInformation: false,
        }
      }
    },
    'isGroupLeader': {
      type: Boolean,
      required: true,
      default: false
    },
  },
  emits: ['update:groupId', 'update:groupSize', 'update:members', 'update:creationTime', 'update:deadLine', 'update:presentationTime', 'update:leader', 'update:maxSize', 'update:moreInformation'],
  methods: {
    reset() {
      this.groupData_temp = cloneDeep(this.groupData)
    },
    submitChange() {
      // console.clear();
      // console.log("这里是提交修改部分")
      // console.log({
      //   "groupId": this.groupData_temp.groupId,
      //   "projectId": this.groupData_temp.projectId,
      //   "groupName": this.groupData_temp.groupName,
      //   "visibility": this.groupData_temp.visibility,
      //   "technicalStack": this.groupData_temp.technicalStack,
      //   "description": this.groupData_temp.description
      // })
      // console.log({
      //   "groupId": typeof this.groupData_temp.groupId,
      //   "projectId":typeof this.groupData_temp.projectId,
      //   "groupName":typeof this.groupData_temp.groupName,
      //   "visibility":typeof this.groupData_temp.visibility,
      //   "technicalStack":typeof this.groupData_temp.technicalStack,
      //   "description":typeof this.groupData_temp.description
      // })
      api.post('/stu/modify_group_info',
        {
          "groupId": this.groupData_temp.groupId,
          "projectId": this.groupData_temp.projectId,
          "groupName": this.groupData_temp.groupName,
          "visibility": this.groupData_temp.visibility,
          "technicalStack": this.groupData_temp.technicalStack,
          "description": this.groupData_temp.description
        }
      ).then((res) => {
        this.$q.notify({
          color: 'green-4',
          textColor: 'white',
          icon: 'cloud_done',
          message: res.data.msg,
          position: 'top',
          timeout: 3000,
        })
      }).catch((err) => {
        this.$q.notify({
          color: 'red-4',
          textColor: 'white',
          icon: 'cloud_done',
          message: err.response.data.msg,
          position: 'top',
          timeout: 3000,
        })
        console.log(err)
        console.log({
          "maxsize": this.groupData_temp.maxsize,
          "groupName": this.groupData_temp.groupName,
          "instructorId": this.groupData_temp.instructorId,
          "reportTime": this.groupData.reportTime,
          "memberIds":
          this.groupData_temp.memberIds
        })
      })
    },
  },
  data() {
    return {
      userData: useUserStore(),
      disableList_temp: cloneDeep(this.disableList),
      groupData_temp: cloneDeep(this.groupData),
      isGroupLeader_temp: this.isGroupLeader,
      avatar_tmp: '',
    }
  },
  watch: {
    groupData: {
      handler: async function (val, oldVal) {
        this.groupData_temp = cloneDeep(val)
        console.log('group_data变化了')
        if(this.groupData_temp.leaderId===99999999) return
        this.avatar_tmp = await getAvatarUrlById(this.groupData.leaderId)
      },
      deep: true
    },
    isGroupLeader: {
      handler: function (val, oldVal) {
        this.isGroupLeader_temp = val
      },
      deep: true
    }
  }
})
</script>

<style scoped>

</style>




