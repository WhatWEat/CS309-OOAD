<template>
  <q-layout view="hHh lpR fFf">
    <q-header elevated>
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="toggleLeftDrawer"
        />

        <q-toolbar-title>
          Project Helper
          <q-btn dense flat icon="home" @click="router.push('/')">
          </q-btn>
        </q-toolbar-title>
        <PersonBar></PersonBar>
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      :mini="miniState"
      @mouseover="miniState = false"
      @mouseout="miniState = true"

      :width="300"
      :breakpoint="500"
      bordered
      class="text-h6 text-weight-bolder"
    >
      <q-list>
        <EssentialLink
          v-for="link in essentialLinks"
          :key="link.title"
          v-bind="link"
        />
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import EssentialLink, {EssentialLinkProps} from 'components/Layout/EssentialLink.vue';
import {useUserStore} from 'src/composables/useUserStore';
import {useRouter} from 'vue-router';
import {watchEffect} from 'vue-demi';
import PersonBar from 'components/Layout/PersonBar.vue';

const router = useRouter()
const projectID = ref(router.currentRoute.value.params.projectID)
const {userid} = useUserStore()
const essentialLinks = ref<EssentialLinkProps[]>([])
onMounted(() => {

  watchEffect(() => {
      essentialLinks.value = [
        {
          title: 'Announcements',
          icon: 'record_voice_over',
          link: `/projects/${projectID.value}/announcements/`
        },
        {
          title: 'Group',
          icon: 'supervisor_account',
          link: `/projects/${projectID.value}/group-list`
        },
        {
          title: 'Chat',
          icon: 'chat',
          link: 'https://forum.quasar.dev'
        },
        {
          title: 'Homework',
          caption: '@quasarframework',
          icon: 'article',
          link: `/projects/${projectID.value}/assignment-list/personal`
        },
        {
          title: 'Grade',
          icon: 'school',
          link: `/projects/${projectID.value}/sgrade`
        },
        {
          title: 'TGrade',
          icon: 'school',
          link: `/projects/${projectID.value}/tgrade`
        },
        {
          title: 'Person',
          icon: 'account_box',
          link: `/person/${userid.value}`
        },
      ];

  })
})


const leftDrawerOpen = ref(true)
const miniState = ref(true)

function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value
}
</script>
