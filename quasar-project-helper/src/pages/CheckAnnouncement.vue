<template>
  <q-layout view="hHh lpR fFf" class="bg-grey-1">
    <q-header class="bg-white text-grey-8">
      <q-toolbar class="GNL__toolbar">

        <q-toolbar-title v-if="$q.screen.gt.xs" shrink class="row items-center no-wrap">
          <q-btn dense @click="router.push('/')">
            <q-icon name="home"></q-icon>
          </q-btn>
          Announcement
        </q-toolbar-title>

        <q-space/>

        <q-input class="GNL__toolbar-input" outlined dense v-model="search" color="bg-grey-7 shadow-1"
                 placeholder="Search for title, publisher & time">
          <template v-slot:prepend>
            <q-icon v-if="search === ''" name="search"/>
            <q-icon v-else name="clear" class="cursor-pointer" @click="search = ''"/>
          </template>
          <template v-slot:append>
            <q-btn
              flat
              dense
              round
              aria-label="Menu"
              icon="arrow_drop_down"
            >
            </q-btn>
          </template>
        </q-input>

        <q-space/>
        <person-bar></person-bar>
      </q-toolbar>
    </q-header>



    <q-page-container>
      <q-list class="q-ma-sm q-mt-md">
        <q-expansion-item
          v-for="(mail, index) in mail_data" :key="index"
          style="border-radius: 10px"
          popup
          header-class="bg-white"
        >
          <template v-slot:header>
            <q-item-section avatar>
              <q-avatar>
                <img :src="mail.avatar">
              </q-avatar>
            </q-item-section>

            <q-item-section>
              <q-item-label>{{ mail.title }}</q-item-label>
              <q-item-label caption>{{ mail.date }}</q-item-label>
            </q-item-section>
          </template>

          <q-separator/>
          <q-card>
            <q-card-section>
              {{ mail.msg }}
            </q-card-section>
          </q-card>
        </q-expansion-item>
      </q-list>
    </q-page-container>

    <q-page-sticky v-if="$q.screen.lt.sm" position="bottom-right" :offset="[10,10]">
      <q-btn round
             icon="add"
             direction="up"
             color="accent"
      >

      </q-btn>
    </q-page-sticky>
  </q-layout>
</template>

<script>

import {defineComponent} from 'vue'
import {ref} from 'vue'
import { useRouter } from 'vue-router'
import PersonBar from "components/Layout/PersonBar.vue";



const links1 = [
  {icon: 'move_to_inbox', text: 'Inbox'},
  {icon: 'star', text: 'Stared'},
  {icon: 'send', text: 'Sent'},
  {icon: 'error', text: 'Spam'}
];
const links2 = [
  {icon: 'flag', text: 'Updates', color: 'text-orange'},
  {icon: 'group', text: 'Social', color: 'text-red'},
  {icon: 'label', text: 'Promos', color: 'text-indigo-8'},
  {icon: 'forum', text: 'Forums', color: 'text-teal'}
];

export default defineComponent({
  name: 'eMail',
  components: {PersonBar},
  setup() {


    const leftDrawerOpen = ref(false)
    const miniState = ref(false)
    const router = useRouter()

    return {
      leftDrawerOpen,
      router,
      miniState,
      search: '',
      showAdvanced: ref(false),
      showDateOptions: ref(false),
      exactPhrase: '',
      hasWords: '',
      excludeWords: '',
      byWebsite: '',
      byDate: 'Any time',
      links1,
      links2,
      mail_data: [
        {
          title: 'Pratik Patel',
          avatar: 'https://avatars2.githubusercontent.com/u/34883558?s=400&v=4',
          date: 'March 12, 2019',
        },
        {
          title: 'Pratik Patel',
          avatar: 'https://avatars2.githubusercontent.com/u/34883558?s=400&v=4',
          date: 'March 22, 2019',
        },
        {
          title: 'Pratik Patel',
          avatar: 'https://avatars2.githubusercontent.com/u/34883558?s=400&v=4',
          date: 'March 12, 2019',
        },
        {
          title: 'Winfield Stapforth',
          avatar: 'https://cdn.quasar.dev/img/avatar6.jpg',
          date: 'March 22, 2019',
        },
        {
          title: 'Jeff Galbraith',
          avatar: 'https://cdn.quasar.dev/team/jeff_galbraith.jpg',
          date: 'March 12, 2019',
        },
        {
          title: 'Jeff Galbraith',
          avatar: 'https://cdn.quasar.dev/team/jeff_galbraith.jpg',
          date: 'March 22, 2019',
        },
        {
          title: 'Razvan Stoenescu',
          avatar: 'https://cdn.quasar.dev/team/razvan_stoenescu.jpeg',
          date: 'March 12, 2019',
        },
        {
          title: 'Razvan Stoenescu',
          avatar: 'https://cdn.quasar.dev/team/razvan_stoenescu.jpeg',
          date: 'March 22, 2019',
        },
        {
          title: 'John Doe',
          avatar: 'https://cdn.quasar.dev/img/boy-avatar.png',
          date: 'March 12, 2019',
        },
        {
          title: 'Pratik Patel',
          avatar: 'https://cdn.quasar.dev/img/boy-avatar.png',
          date: 'March 22, 2019',
        },
      ]
    }
  },

  mounted() {
    this.mail_data = this.mail_data.map(function (item) {
      item['msg'] = 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Quidem, eius reprehenderit eos corrupti\n' +
        '              commodi magni quaerat ex numquam, dolorum officiis modi facere maiores architecto suscipit iste\n' +
        '              eveniet doloribus ullam aliquid.';
      return item;
    })
  }

})
</script>

<style>
.GNL__toolbar {
  height: 64px;
}

.GNL__toolbar-input {
  width: 55%;
}

.GNL__drawer-item .q-item__section--avatar .q-icon {
  color: #5f6368;
}

.GNL__drawer-item .q-item__label {
  color: #3c4043;
  letter-spacing: .01785714em;
  font-size: .875rem;
  font-weight: 500;
  line-height: 1.25rem;
}

</style>
