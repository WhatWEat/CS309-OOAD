<template>
  <q-card>
    <q-card-section class="row items-center">
      <q-avatar :color="icon_color" :icon="icon_name" :text-color="icon_text_color"/>
      <span class="q-ml-sm">{{ text }}</span>
    </q-card-section>

    <q-card-actions align="right">
      <!--        插槽部分-->
      <slot name="button1">
        <q-btn v-close-popup color="primary" flat label="Cancel"/>
      </slot>
      <slot name="button2">
        <q-btn v-close-popup color="primary" flat label="Turn on Wifi"/>
      </slot>
    </q-card-actions>
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
  props: ['icon_name', 'icon_color', 'icon_text_color', 'text']
})
</script>

<style scoped>

</style>




