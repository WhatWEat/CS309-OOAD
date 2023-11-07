<template>
  <q-card bordered class="no-shadow">
    <!--   上方部分 -->
    <q-item>
      <q-item-section avatar>
        <q-avatar size="60px" class="shadow-10">
          <img :src="avatar">
        </q-avatar>
      </q-item-section>

      <q-item-section>
        <q-item-label class="text-grey-8 text-weight-bold">{{groupId}}</q-item-label>
        <q-item-label caption v-for:()>{{}}</q-item-label>
        <q-item-label class="text-grey-8">{{ email }}</q-item-label>
      </q-item-section>

      <q-item-section side>
        <q-item-label>
          <q-btn size="sm" flat round icon="fab fa-facebook" class="bg-indigo-7 text-white"/>
        </q-item-label>
        <q-item-label>
          <q-btn size="sm" flat round icon="fab fa-twitter" class="bg-info text-white"/>
        </q-item-label>

      </q-item-section>
    </q-item>
<!--    间隔线部分-->
    <q-separator></q-separator>
<!--    下方详细信息部分-->
    <q-card-section>
      <div class="q-pa-sm text-grey-8 text-weight-bold text-subtitle1">

      </div>
    </q-card-section>
  </q-card>
</template>

<script>
import {defineComponent} from 'vue'
import {descriptionProps} from "element-plus";

export default defineComponent({
  name: "DirectoryCard",
  computed: {
    descriptionProps() {
      return descriptionProps
    }
  },
  props: ['avatar', 'name', 'description', 'email']
})
</script>

<style scoped>

</style>
