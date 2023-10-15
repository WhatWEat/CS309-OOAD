<template>
  <q-page class="q-pa-sm bg-white row">
    <q-scroll-area style="height: 200px" class="col-4">
      <q-calendar-month
        ref="calendar"
        v-model="selectedDate"
        mini-mode
        animated
        bordered
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
  </q-page>
</template>

<script>
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
import {defineComponent} from 'vue'
// The function below is used to set up our demo data
const CURRENT_DAY = new Date()

function getCurrentDay(day) {
  const newDay = new Date(CURRENT_DAY)
  newDay.setDate(day)
  const tm = parseDate(newDay)
  return tm.date
}

export default defineComponent({
  components: {
    QCalendarMonth
  },
  data() {
    return {
      selectedDate: today(),
      events: [
        {
          id: 2,
          title: 'Birthday',
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
          id: 4,
          title: 'Lunch',
          details: 'Company is paying!',
          date: getCurrentDay(10),
          duration: 90,
          bgcolor: 'teal',
          icon: 'fas fa-hamburger'
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
          id: 7,
          title: 'Girlfriend',
          details: 'Meet GF for dinner at Swanky Restaurant',
          date: getCurrentDay(22),
          duration: 180,
          bgcolor: 'teal',
          icon: 'fas fa-utensils'
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
      ]
    }
  },
  computed: {
    eventsMap() {
      const map = {}
      if (this.events.length > 0) {
        this.events.forEach(event => {
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
      console.log(map)
      return map
    }
  },
  methods: {
    badgeClasses(event, type) {
      return {
        [`text-white bg-${event.bgcolor}`]: true,
        'rounded-border': true
      }
    },
    badgeStyles(day, event) {
      const s = {}
      // s.left = day.weekday === 0 ? 0 : (day.weekday * this.parsedCellWidth) + '%'
      // s.top = 0
      // s.bottom = 0
      // s.width = (event.days * this.parsedCellWidth) + '%'
      return s
    },
  }
})
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
