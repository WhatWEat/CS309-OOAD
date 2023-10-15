<template>
  <q-toolbar class="bg-grey-4 text-black rounded-borders">
    <q-btn
      flat
      label="AssignMents"
    ></q-btn>

    <q-toolbar-title></q-toolbar-title>
    <q-btn-group push>
      <q-btn :color="color_personal" icon="perm_identity" label="Personal" push @click="buttonHandle"></q-btn>
      <q-btn :color="color_group" icon="groups" label="Group" push @click="buttonHandle"></q-btn>
    </q-btn-group>
  </q-toolbar>

  <router-view></router-view>
</template>

<script>
export default {
  name: "AssignmentStudent",
  data() {
    return {
      path: '',
      color_personal: 'grey-5' ,
      color_group: 'grey-4'
    }
  },
  methods: {
    buttonHandle() {
      const regex  = /\/personal$/;
      const regex2 = /\/group$/;
      this.path = this.$route.path
      console.log(this.path)

      if (regex.test(this.path)) {
        const newpath = this.path.replace(/\/[^/]*$/, '/group')
        console.log("newpath", newpath)
        this.$router.push(newpath)
        this.color_group = 'grey-5'
        this.color_personal = 'grey-4'
      }
      else if (regex2.test(this.path)) {
        const newpath = this.path.replace(/\/[^/]*$/, '/personal')
        console.log("newpath", newpath)
        this.$router.push(newpath)
        this.color_group = 'grey-4'
        this.color_personal = 'grey-5'
      }
      else {
        const newpath = this.path.replace(/\/[^/]*$/, '')
        console.log("newpath", newpath)
        this.$router.push(newpath)
        this.color_group = 'grey-4'
        this.color_personal = 'grey-5'
      }
    }
  }
}
</script>

<style scoped>

</style>
