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
import {useRouter} from 'vue-router';
import {useUserStore} from 'src/composables/useUserStore';
import {watchEffect} from 'vue-demi';
import PersonBar from 'components/Layout/PersonBar.vue';

const {userid} = useUserStore()
const essentialLinks = ref<EssentialLinkProps[]>([]);
onMounted(() => {
  watchEffect(() => {
    essentialLinks.value =
      [
        {
          title: 'Announcements',
          icon: 'record_voice_over',
          link: 'https://chat.quasar.dev'
        },
        {
          title: 'Projects',
          icon: 'extension',
          link: '',
          list: true
        },
        {
          title: 'Chat',
          icon: 'chat',
          link: 'https://forum.quasar.dev'
        },
        {
          title: 'Person',
          icon: 'account_box',
          link: `/person/${userid.value}`
        },
      ]
  })

});
const router = useRouter()
const leftDrawerOpen = ref(true)
const miniState = ref(true)


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
