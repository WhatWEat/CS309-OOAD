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
          <span class="gt-xs"> Welcome to Project Helper</span>&nbsp;
          <span class="lt-sm"> Project Helper</span>&nbsp;

          <q-icon class="gt-sm" name="waving_hand"></q-icon>
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
import {useRouter} from 'vue-router'
import {useUserStore} from 'src/composables/useUserStore';
import {watchEffect} from 'vue-demi';
import PersonBar from 'components/Layout/PersonBar.vue';
import {api} from "boot/axios";

// const router = useRouter()
const {userid} = useUserStore()
const essentialLinks = ref<EssentialLinkProps[]>([]);
onMounted(() => {
  watchEffect(() => {
    essentialLinks.value =
      [
        {
          title: 'Home',
          icon: 'home',
          link: '/'
        },
        {
          title: 'Announcements',
          icon: 'record_voice_over',
          link: '/announcements'
        },
        {
          title: 'Projects',
          icon: 'extension',
          link: '',
          list: true
        },
        {
          title: 'Person',
          icon: 'account_box',
          link: `/person/${userid.value}`
        },
        {
          title: 'Admin',
          icon: 'airplay',
          link: `/admin`
        },
        {
          title: 'Logout',
          icon: 'logout',
          link: `/logout/${userid.value}`
        },

      ]
  })

});
const leftDrawerOpen = ref(true)
const miniState = ref(true)
const isFresh = ref(true)
onMounted(() => {
  watchEffect(()=>{
    if (userid.value === -1 && isFresh.value) {
      return
    }
    api.get(`/project-list/0/1000/${userid.value}`).then(res => {
      essentialLinks.value[2].subItems = res.data.body;
      isFresh.value = false
    }).catch(err => {
      console.log(err)
    })
  })
});

function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value
}

</script>
<style>
.fa-beat {
  animation: fa-beat 5s ease infinite;
}

@keyframes fa-beat {
  0% {
    transform: scale(1);
  }
  5% {
    transform: scale(1.25);
  }
  20% {
    transform: scale(1);
  }
  30% {
    transform: scale(1);
  }
  35% {
    transform: scale(1.25);
  }
  50% {
    transform: scale(1);
  }
  55% {
    transform: scale(1.25);
  }
  70% {
    transform: scale(1);
  }
}
</style>
