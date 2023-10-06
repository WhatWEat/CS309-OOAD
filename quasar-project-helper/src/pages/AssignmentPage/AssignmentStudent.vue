<template>
  <q-toolbar class="bg-primary text-white rounded-borders">
    <q-btn
      flat
      label="AssignMents"
    ></q-btn>

    <q-toolbar-title ></q-toolbar-title>
    <q-btn-group push>
      <router-link v-slot:default="props" custom to="/personal">
        <q-btn color="blue" icon="perm_identity" label="Personal" v-bind="buttonProps(props)" push></q-btn>
      </router-link>
      <router-link v-slot:default="props" custom to="/group">
        <q-btn color="blue" icon="groups" label="Group" push  v-bind="buttonProps(props)" ></q-btn>
      </router-link>
    </q-btn-group>
  </q-toolbar>

  <router-view></router-view>
</template>

<script>
export default {
  name: "AssignmentStudent",
  methods:{
    buttonProps ({ href, route, isActive, isExactActive }) {
      const props = {
        noCaps: false,
        outline: false,
        to: href
      }
      if (isActive === true) {
        props.color = isExactActive === true ? 'amber-9' : 'amber-9'
      }
      else {
        props.color = 'blue'
      }
      return props
    }
  }
}
</script>

<style scoped>

</style>
