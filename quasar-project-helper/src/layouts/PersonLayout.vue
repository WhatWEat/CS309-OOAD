<template>
  <q-layout view="hHh lpR fFf">

    <q-header elevated class="bg-primary text-white" height-hint="98">
      <q-toolbar>
        <q-toolbar-title>
          <q-btn dense @click="router.push('/')">
            <q-icon name="home"></q-icon>
          </q-btn>
          <span v-if="flag">{{personId}}'s Person Information</span>
        </q-toolbar-title>
        <PersonBar></PersonBar>
      </q-toolbar>

      <q-tabs align="left">
        <q-route-tab :to='`/person/${personId}`' label="Overview"/>
        <q-route-tab :to='`/person/${personId}/projects`' label="Projects"/>
      </q-tabs>
    </q-header>

    <q-page-container
      transition-show="slide-right"
      transition-hide="slide-left">
      <router-view />
    </q-page-container>

  </q-layout>
</template>

<script setup>
import {useUserStore} from 'src/composables/useUserStore';
import {onMounted, ref} from 'vue';
import {watchEffect} from 'vue-demi';
import {useRouter} from 'vue-router';
import {useCurrentPageUser} from 'stores/user-store';
import PersonBar from "components/Layout/PersonBar.vue";
const usePerson = useCurrentPageUser()

const router = useRouter()

const personId = ref(router.currentRoute.value.params.personId)
usePerson.$state.person_id = Number(personId.value)
const {username, userid} = useUserStore()

const flag = ref(false)
onMounted(() => {
  watchEffect(() => {
    if (userid.value !== -1 && userid.value !== Number(personId.value)) {
      flag.value = true
    }
  })
})
</script>

<style scoped>

</style>
