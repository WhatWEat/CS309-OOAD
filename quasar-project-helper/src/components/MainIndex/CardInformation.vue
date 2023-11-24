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
              <q-item v-for="project in projects" :key="project.projectId" clickable v-ripple>
                <q-item-section avatar>
                  <q-avatar>
                    <q-img :src="project.avatar"></q-img>
                  </q-avatar>
                </q-item-section>
                <q-item-section>
                  <q-item-label lines="1">{{ project.projectName }}</q-item-label>
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
                {{ msg.createTime }}
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
  QCalendarMonth,
  addToDate,
  parseDate,
  parseTimestamp,
  today
} from '@quasar/quasar-ui-qcalendar'
import '@quasar/quasar-ui-qcalendar/src/QCalendarVariables.sass'
import '@quasar/quasar-ui-qcalendar/src/QCalendarTransitions.sass'
import '@quasar/quasar-ui-qcalendar/src/QCalendarMonth.sass'
import {truncate} from 'src/composables/usefulFunction';
import {computed} from 'vue-demi';
import {noticeProps, defaultNotice, assProps, projectProps} from "src/composables/comInterface";

const selectedDate = ref(today())
const CURRENT_DAY = new Date()

function getCurrentDay(day: number) {
  const newDay = new Date(CURRENT_DAY)
  newDay.setDate(day)
  const tm = parseDate(newDay)
  if (!tm) {
    throw new Error('Invalid date')
  }
  return tm.date
}

// project list
const projects = ref<projectProps[]>([])
onMounted(() => {
  api.get('/project-list/0/9999').then(res => {
    projects.value = res.data.body;
    // console.log(res)
  }).catch(err => {
    console.log(err)
  })
})
const colors = [
  'blue',
  'green',
  'orange',
  'red',
  'teal',
  'grey',
  'purple'
]
const events = ref<assProps[]>([
  {
    assignmentId: 2,
    title: 'ASS1',
    creatorId: 1,
    creatorName: 'teacher 1',
    description: 'Buy a nice present',
    deadline: '2021-10-10 00:00:00',
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
    deadline: getCurrentDay(4),
    projectId: 0,
    projectName: 'Project 1',
    fullMark: 100,
    type: '0',
  },
  // {
  //   id: 5,
  //   title: 'Visit mom',
  //   details: 'Always a nice chat with mom',
  //   date: getCurrentDay(20),
  //   duration: 90,
  //   bgcolor: 'grey',
  //   icon: 'fas fa-car'
  // },
  // {
  //   id: 6,
  //   title: 'Conference',
  //   details: 'Teaching Javascript 101',
  //   date: getCurrentDay(22),
  //   duration: 540,
  //   bgcolor: 'blue',
  //   icon: 'fas fa-chalkboard-teacher'
  // },
  // {
  //   id: 8,
  //   title: 'Rowing',
  //   details: 'Stay in shape!',
  //   date: getCurrentDay(27),
  //   bgcolor: 'purple',
  //   icon: 'rowing',
  //   days: 2
  // },
  // {
  //   id: 9,
  //   title: 'Fishing',
  //   details: 'Time for some weekend R&R',
  //   date: getCurrentDay(27),
  //   bgcolor: 'purple',
  //   icon: 'fas fa-fish',
  //   days: 2
  // },
  // {
  //   id: 10,
  //   title: 'Vacation',
  //   details: 'Trails and hikes, going camping! Don\'t forget to bring bear spray!',
  //   date: getCurrentDay(29),
  //   bgcolor: 'purple',
  //   icon: 'fas fa-plane',
  //   days: 5
  // }
])
// announcements
const messages = ref<noticeProps[]>([defaultNotice])
onMounted(() => {
  api.get(`/notice-list/-1/0/100`).then((res) => {
    messages.value = res.data.body;
    console.log(res)
  }).catch((err) => {
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
      if (event.deadline !== undefined) {
        let timestamp = parseTimestamp(event.deadline)
        let days = 0
        // add a new event for each day
        // skip 1st one which would have been done above
        do {
          if (timestamp === null) {
            throw new Error('Invalid date')
          }
          timestamp = addToDate(timestamp, {day: 1})
          if (!map[timestamp.date]) {
            map[timestamp.date] = []
          }
          map[timestamp.date].push(event)
          // already accounted for 1st day
        } while (--days > 1)
      }
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
  let colorClass= 'bg-green';
  if (event.deadline < today()){
    colorClass= 'bg-blue';
  } else if(today() <= event.deadline && event.deadline <= getCurrentDay(5)) {
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
