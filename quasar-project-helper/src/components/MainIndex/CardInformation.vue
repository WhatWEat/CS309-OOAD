<template>
  <div class="row q-col-gutter-sm  q-py-sm">
    <div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
      <q-card class="q-ma-xs no-shadow" bordered style="background-color: #38b1c5">
        <q-card-section class="text-h6 text-white">
          Project List
        </q-card-section>
        <q-card-section class="bg-white q-mx-sm q-mb-sm">
          <q-scroll-area style="height: 200px">
            <q-list class="rounded-borders" separator>
              <q-item v-for="project in projects" :key="project.id" clickable v-ripple>
                <q-item-section avatar>
                  <q-avatar>
                    <q-img :src="project.avatar"></q-img>
                  </q-avatar>
                </q-item-section>
                <q-item-section>
                  <q-item-label lines="1">{{ project.project }}</q-item-label>
                  <q-item-label caption lines="2">
                    <span class="text-weight-bold">{{ project.teacher }}</span>
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
                  :key="event.id"
                >
                  <div
                    :class="badgeClasses(event, 'day')"
                    :style="badgeStyles(event, 'day')"
                    class="my-event"
                  >
                    <abbr
                      :title="event.details"
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
          Latest Announcements
        </q-card-section>
        <q-card-section class="bg-white q-mb-sm q-mx-xs">
          <q-scroll-area style="height: 200px">
            <q-item v-for="msg in messages" :key="msg.id" clickable v-ripple>
              <q-item-section>
                <q-item-label>{{ msg.name }}</q-item-label>
                <q-item-label caption lines="1" class="ellipsis">{{ truncate(msg.msg) }}</q-item-label>
              </q-item-section>
              <q-item-section side>
                {{ msg.time }}
              </q-item-section>
            </q-item>
          </q-scroll-area>
        </q-card-section>
      </q-card>
    </div>

  </div>
</template>

<script setup>
import {ref} from 'vue'
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
import {computed} from "vue-demi";
const selectedDate = ref(today())
const CURRENT_DAY = new Date()

function getCurrentDay(day) {
  const newDay = new Date(CURRENT_DAY)
  newDay.setDate(day)
  const tm = parseDate(newDay)
  return tm.date
}

const projects = ref([
  {
    id: 1,
    teacher: 'Teacher 1',
    project: 'Project 1',
    avatar: 'https://avatars2.githubusercontent.com/u/34883558?s=400&v=4'
  },
  {
    id: 2,
    teacher: 'Teacher 2',
    project: 'Project 2',
    avatar: 'https://cdn.quasar.dev/team/razvan_stoenescu.jpeg'
  },
  {
    id: 3,
    teacher: 'Teacher 2',
    project: 'Project 3',
    avatar: 'https://cdn.quasar.dev/team/razvan_stoenescu.jpeg'
  },
  {
    id: 4,
    teacher: 'Teacher 1',
    project: 'Project 4',
    avatar: 'https://avatars2.githubusercontent.com/u/34883558?s=400&v=4'
  }
])
const events = ref([
  {
    id: 2,
    title: 'ASS1',
    details: 'Buy a nice present',
    date: getCurrentDay(4),
    bgcolor: 'green',
    icon: 'fas fa-birthday-cake'
  },
  {
    id: 3,
    title: 'Meeting',
    details: 'Time to pitch my idea to the company',
    date: getCurrentDay(10),
    duration: 120,
    bgcolor: 'red',
    icon: 'fas fa-handshake'
  },
  {
    id: 5,
    title: 'Visit mom',
    details: 'Always a nice chat with mom',
    date: getCurrentDay(20),
    duration: 90,
    bgcolor: 'grey',
    icon: 'fas fa-car'
  },
  {
    id: 6,
    title: 'Conference',
    details: 'Teaching Javascript 101',
    date: getCurrentDay(22),
    duration: 540,
    bgcolor: 'blue',
    icon: 'fas fa-chalkboard-teacher'
  },
  {
    id: 8,
    title: 'Rowing',
    details: 'Stay in shape!',
    date: getCurrentDay(27),
    bgcolor: 'purple',
    icon: 'rowing',
    days: 2
  },
  {
    id: 9,
    title: 'Fishing',
    details: 'Time for some weekend R&R',
    date: getCurrentDay(27),
    bgcolor: 'purple',
    icon: 'fas fa-fish',
    days: 2
  },
  {
    id: 10,
    title: 'Vacation',
    details: 'Trails and hikes, going camping! Don\'t forget to bring bear spray!',
    date: getCurrentDay(29),
    bgcolor: 'purple',
    icon: 'fas fa-plane',
    days: 5
  }
])
const messages = ref([
  {
    id: 5,
    name: 'Projects 1',
    msg: ' -- You \'ll be in your neighborhood doing errands this\n' +
      '            weekend. Do you want to grab brunch?',
    avatar: 'https://avatars2.githubusercontent.com/u/34883558?s=400&v=4',
    time: '10:42 PM'
  }, {
    id: 6,
    name: 'Projects 2',
    msg: ' -- You\'ll be in your neighborhood doing errands this\n' +
      '            weekend. Do you want to grab brunch?',
    avatar: 'https://cdn.quasar.dev/img/avatar6.jpg',
    time: '11:17 AM'
  }, {
    id: 1,
    name: 'Projects 3',
    msg: ' -- You\'ll be in your neighborhood ',
    avatar: 'https://cdn.quasar.dev/img/boy-avatar.png',
    time: '5:17 AM'
  }, {
    id: 2,
    name: 'Projects 2',
    msg: ' -- You\'ll be in your neighborhood doing errands this\n' +
      '            weekend. Do you want to grab brunch?',
    avatar: 'https://cdn.quasar.dev/team/jeff_galbraith.jpg',
    time: '5:17 AM'
  }, {
    id: 3,
    name: 'Projects 3',
    msg: ' -- You\'ll be in your neighborhood doing errands this\n' +
      '            weekend. Do you want to grab brunch?',
    avatar: 'https://cdn.quasar.dev/team/razvan_stoenescu.jpeg',
    time: '5:17 AM'
  }
])
const eventsMap = computed(() =>{
  const map = {}
  if (events.value.length > 0) {
    events.value.forEach(event => {
      (map[event.date] = (map[event.date] || [])).push(event)
      if (event.days !== undefined) {
        let timestamp = parseTimestamp(event.date)
        let days = event.days
        // add a new event for each day
        // skip 1st one which would have been done above
        do {
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
function badgeClasses(event, type) {
  return {
    [`text-white bg-${event.bgcolor}`]: true,
    'rounded-border': true
  }
}

function badgeStyles(day, event) {
  const s = {}
  return s
}
function truncate(str) {
  if (str.length > 15) {
    return str.substring(0, 15) + '...';  // 如果超过15个字符，添加省略号
  }
  return str;  // 如果不超过15个字符，直接返回原字符串
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
