<template>
  <div class="row q-col-gutter-sm  q-py-sm">
    <div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
      <q-card class="q-ma-xs no-shadow" bordered style="background-color: #38b1c5">
        <q-card-section class="text-h6 text-white">
          Project List
        </q-card-section>
        <q-card-section class="bg-white q-mx-sm q-mb-sm">
          <q-scroll-area style="height: 200px" v-if="loadingProject">
            <div v-if="projects.length === 0" class="justify-center">
              You don't have any project yet.
            </div>
            <q-infinite-scroll v-else @load="getProject" :offset="10">
              <q-list class="rounded-borders" separator>
                <q-item v-for="project in projects" :key="project.projectId" clickable v-ripple
                        :to="`/projects/${project.projectId}`">
                  <q-item-section avatar>
                    <q-avatar>
                      <q-img :src="avatarMap.get(project.teacherName)?.toString()"></q-img>
                    </q-avatar>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label lines="1">{{ project.name }}</q-item-label>
                    <q-item-label caption lines="2">
                      <span class="text-weight-bold">{{ project.teacherName }}</span>
                    </q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
              <template v-slot:loading>
                <div class="row justify-center q-my-md" style="height: 10px">
                  <q-spinner-dots color="primary" size="40px"/>
                </div>
              </template>
            </q-infinite-scroll>

          </q-scroll-area>
          <q-skeleton style="height: 200px" type="text" v-else>

          </q-skeleton>
        </q-card-section>
      </q-card>
    </div>
    <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
      <q-card class="q-ma-xs no-shadow" bordered style="background-color: #ea4b64">
        <q-card-section class="text-h6 text-white">
          Assignment Calendar
        </q-card-section>
        <q-card-section class="bg-white q-mb-sm q-mx-xs">
          <q-scroll-area style="height: 200px">
            <q-calendar-month
              ref="calendar"
              v-model="selectedDate"
              mini-mode
              animated
              focusable
              hoverable
              no-active-date
            >
              <template #day="{ scope: { timestamp } }">
                <template
                  v-for="event in eventsMap[timestamp.date]"
                  :key="event.assignmentId"
                >
                  <div
                    :class="badgeClasses(event)"
                    :style="badgeStyles"
                    class="my-event"
                  >
                    <abbr
                      :title="event.title"
                      class="tooltip"
                    >
                <span class="title q-calendar__ellipsis">{{
                    event.title
                  }}</span>
                    </abbr>
                  </div>
                </template>
              </template>
            </q-calendar-month>
          </q-scroll-area>

        </q-card-section>
      </q-card>
    </div>
    <div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
      <q-card class="q-ma-xs no-shadow" bordered style="background-color: #1e88e5">
        <q-card-section class="text-h6 text-white">
          Announcements
        </q-card-section>
        <q-card-section class="bg-white q-mb-sm q-mx-xs">
          <div v-if="loadingMessage">
            <q-scroll-area style="height: 200px" v-if="messages.length!==0">
              <q-infinite-scroll :offset="10" @load="getNotice"
                                 >

                <q-list>
                  <q-item v-for="msg in messages" :key="msg.noticeId" clickable v-ripple>
                    <q-item-section>
                      <q-item-label>{{ msg.title }}</q-item-label>
                      <q-item-label caption lines="1" class="ellipsis">{{
                          truncate(msg.content)
                        }}
                      </q-item-label>
                    </q-item-section>
                    <q-item-section side>
                      {{ formatDateString(msg.createTime) }}
                    </q-item-section>
                  </q-item>

                </q-list>
                <template v-slot:loading>
                  <div class="row justify-center q-my-md" style="height: 10px">
                    <q-spinner-dots color="primary" size="40px"/>
                  </div>
                </template>


              </q-infinite-scroll>
            </q-scroll-area>
            <div v-else class="justify-center" style="height: 200px">
              No announcement yet.
            </div>
          </div>
          <q-skeleton type="text" style="height: 200px" v-else>
          </q-skeleton>
        </q-card-section>
      </q-card>
    </div>

  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {api} from 'boot/axios'
import {
  parseDate,
  QCalendarMonth,
  today
} from '@quasar/quasar-ui-qcalendar'
import '@quasar/quasar-ui-qcalendar/src/QCalendarVariables.sass'
import '@quasar/quasar-ui-qcalendar/src/QCalendarTransitions.sass'
import '@quasar/quasar-ui-qcalendar/src/QCalendarMonth.sass'
import {formatDateString, getAvatarUrlById, truncate} from 'src/composables/usefulFunction';
import {computed, watchEffect} from 'vue-demi';
import {assProps, noticeProps, projectProps} from "src/composables/comInterface";
import {useUserStore} from "src/composables/useUserStore";

const selectedDate = ref(today())

function getCurrentDay(day: number) {
  const newDay = new Date()
  newDay.setDate(newDay.getDate() + day)
  const tm = parseDate(newDay)
  if (!tm) {
    throw new Error('Invalid date')
  }
  return tm.date
}

// project list
const projects = ref<projectProps[]>([]);
const avatarMap = ref<Map<string, string|null>>(new Map<string, string|null>());
const loadingProject = ref(false), project_page = ref(0)
const {userid} = useUserStore();
onMounted(() => {
  watchEffect(()=>{
    if (userid.value !== -1 && loadingProject.value === false){
      getProject(0,()=>void 0)
    }
  })
})
function getProject(index=0, done:()=>void){
  if (index > project_page.value || userid.value === -1) {
    done()
    return
  }
  api.get(`/project-list/${index}/5/${userid.value}`).then(async res => {
    let page_projects: projectProps[] = res.data.body;
    projects.value.push.apply(projects.value, page_projects);
    for (const project of page_projects) {
      if (avatarMap.value.has(project.teacherName)) {
        continue
      }
      avatarMap.value.set(project.teacherName,await getAvatarUrlById(project.teacherId))
    }
    loadingProject.value = true
    if (page_projects.length === 0) {
      done()
      return
    }
    project_page.value++;
    done()

  }).catch(err => {
    console.log(err)
  })
}

// announcements
const messages = ref<noticeProps[]>([])
const loadingMessage = ref(false), notice_page = ref(0)
onMounted(() => {
  getNotice(0,()=>void 0)
})

function getNotice(index=0,done:()=>void) {
  console.log(index)
  console.log('start',messages.value)
  if (index > notice_page.value) {
    done()
    return
  }
  api.get(`/notice-list/-1/${notice_page.value}/5`).then((res) => {
    let page_messages: noticeProps[] = res.data.body;
    console.log('res', page_messages)
    messages.value.push.apply(messages.value, page_messages)
    console.log('end',messages.value)
    loadingMessage.value = true;
    if (page_messages.length === 0) {
      done()
      return
    }
    notice_page.value++;
    done()
  }).catch((err) => {
    console.log('err', err)
  })
}

// calendar
const events = ref<assProps[]>([
  {
    assignmentId: 2,
    title: 'ASS1',
    creatorId: 1,
    creatorName: 'teacher 1',
    description: 'Buy a nice present',
    deadline: getCurrentDay(1),
    projectId: 0,
    projectName: 'Project 1',
    fullMark: 100,
    type: '0',
  },
])
onMounted(() => {
  api.get(`/ass-list/-1/0/10`).then((res) => {
    events.value = res.data.body;
  }).catch((err) => {
    console.log('后端没写接口所有报错')
    console.log('err', err)
  })
})
const eventsMap = computed(() => {
  interface EventMap {
    [key: string]: assProps[];
  }

  const map: EventMap = {};

  if (events.value.length > 0) {
    events.value.forEach(event => {
      event.deadline = formatDateString(event.deadline).slice(0, 10);
      (map[event.deadline] = (map[event.deadline] || [])).push(event)
    })
  }
  return map
})
computed(() => {
  const map = {}

  console.log(map)
  return map
})

function badgeClasses(event: assProps) {
  // let event_time = parseTimestamp(event.deadline);
  let colorClass = 'bg-green';
  if (event.deadline < today()) {
    colorClass = 'bg-teal';
  } else if (today() <= event.deadline && event.deadline <= getCurrentDay(5)) {
    colorClass = 'bg-orange';
  }
  return {
    [`text-white ${colorClass}`]: true,
    'rounded-border': true
  }
}

function badgeStyles() {
  const s = {}
  return s
}

</script>
<style>
.my-event {
  position: relative;
  font-size: 12px;
  width: 100%;
  margin: 1px 0 0 0;
  justify-content: center;
  text-overflow: ellipsis;
  overflow: hidden;
  cursor: pointer;
}

.title {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.text-white {
  color: white
}

.bg-blue {
  background: blue
}

.bg-green {
  background: green
}

.bg-orange {
  background: orange
}

.bg-red {
  background: red
}

.bg-teal {
  background: teal
}

.bg-grey {
  background: grey
}

.bg-purple {
  background: purple
}

.rounded-border {
  border-radius: 2px
}

abbr.tooltip {
  text-decoration: none
}
</style>
