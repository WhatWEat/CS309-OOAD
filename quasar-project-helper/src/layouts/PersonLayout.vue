<template>
  <q-layout view="hHh lpR fFf">

    <q-header elevated class="bg-primary text-white" height-hint="98">
      <q-toolbar>
        <q-toolbar-title>
          <q-btn dense @click="handleIconClick">
            <q-icon name="home"></q-icon>
          </q-btn>
          <span v-if="flag">{{personId}}'s Person Information</span>
        </q-toolbar-title>
        <span class="text-h6">Hello, {{username}}!</span>
        <q-btn flat round dense icon="person" @click="goPersonInfo"/>
      </q-toolbar>

      <q-tabs align="left">
        <q-route-tab :to='`/person/${personId}`' label="Overview"/>
        <q-route-tab :to='`/person/${personId}/projects`' label="Projects"/>
      </q-tabs>
    </q-header>

    <q-page-container>
      <router-view/>
    </q-page-container>

  </q-layout>
</template>

<script setup>
import {useRouter} from 'vue-router';
import {useUserStore} from 'src/composables/useUserStore';
import {onMounted, ref} from 'vue';
import {watchEffect} from 'vue-demi';

const router = useRouter()
const personId = ref(router.currentRoute.value.params.personId)
const {username, userid} = useUserStore()

const flag = ref(false)
onMounted(() => {
  watchEffect(() => {
    if (userid.value !== -1 && userid.value !== Number(personId.value)) {
      flag.value = true
    }
  })
})
function handleIconClick() {
  router.push('/')
}
function goPersonInfo() {
  router.push(`/person/${userid.value}`)
}
</script>

<style scoped>

</style>
