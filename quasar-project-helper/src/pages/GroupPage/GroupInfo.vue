<template>
    <q-toolbar class="bg-primary text-white rounded-borders">
      <q-btn
        flat
        label="GroupInfo"
      ></q-btn>

      <q-toolbar-title></q-toolbar-title>

      <q-btn
        color="primary"
        icon="add"
        @click="$refs.rightDrawer.open()"
        round
      ></q-btn>
    </q-toolbar>

</template>

<script>
export default {
  name: "GroupInfo"
}
</script>

<style scoped>

</style>
