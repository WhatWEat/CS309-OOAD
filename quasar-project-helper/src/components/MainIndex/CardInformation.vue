<template>
  <div class="row q-col-gutter-sm  q-py-sm">
    <div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
      <q-card class="q-ma-xs no-shadow" bordered style="background-color: #38b1c5">
        <q-card-section class="text-h6 text-white">
          Project List
        </q-card-section>
        <q-card-section class="bg-white q-mx-sm q-mb-sm">
          <q-scroll-area style="height: 200px">
            <div v-if="projects.length === 0" class="justify-center">
              You don't have any project yet.
            </div>
            <q-list class="rounded-borders" separator v-else>
              <q-item v-for="project in projects" :key="project.projectId" clickable v-ripple :to="`/projects/${project.projectId}`">
                <q-item-section avatar>
                  <q-avatar>
                    <q-img :src="avatarMap[project.teacherName]?.toString()"></q-img>
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

          </q-scroll-area>
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
                    :class="badgeClasses(event, 'day')"
                    :style="badgeStyles(event, 'day')"
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
          <q-scroll-area style="height: 200px" v-if="messages.length!==0">
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
          </q-scroll-area>
          <div v-else class="justify-center" style="height: 200px">
            No announcement yet.
          </div>
        </q-card-section>
      </q-card>
    </div>

  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {api} from 'boot/axios'
import {
  addToDate,
  parseDate,
  parseTimestamp,
  QCalendarMonth,
  today
} from '@quasar/quasar-ui-qcalendar'
import '@quasar/quasar-ui-qcalendar/src/QCalendarVariables.sass'
import '@quasar/quasar-ui-qcalendar/src/QCalendarTransitions.sass'
import '@quasar/quasar-ui-qcalendar/src/QCalendarMonth.sass'
import {formatDateString, getAvatarUrlById, truncate} from 'src/composables/usefulFunction';
import {computed} from 'vue-demi';
import {assProps, defaultNotice, noticeProps, projectProps} from "src/composables/comInterface";

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
const projects = ref<projectProps[]>([]), avatarMap: { [key: string]: string | null } = {};

onMounted(() => {
  api.get('/project-list/0/10').then(async res => {
    projects.value = res.data.body;
    for (const project of projects.value) {
      avatarMap[project.teacherName] = await getAvatarUrlById(project.teacherId)
    }
  }).catch(err => {
    console.log(err)
  })
})
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
  {
    assignmentId: 3,
    title: 'Meeting',
    creatorId: 1,
    description: 'Time to pitch my idea to the company',
    deadline: getCurrentDay(-10),
    projectId: 0,
    projectName: 'Project 1',
    fullMark: 100,
    type: '0',
  }
])

// announcements
const messages = ref<noticeProps[]>([defaultNotice])
onMounted(() => {
  api.get(`/notice-list/-1/0/10`).then((res) => {
    messages.value = res.data.body;
  }).catch((err) => {
    console.log('err', err)
  })
})
// calendar
onMounted(()=>{
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

function badgeClasses(event: assProps, type: string) {
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

function badgeStyles(event: any, day: string) {
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
