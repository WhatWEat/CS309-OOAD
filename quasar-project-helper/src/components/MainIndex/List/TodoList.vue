<template>
  <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
    <q-card class="bg-blue text-white no-shadow" bordered>
      <q-card-section class="text-h6 q-pb-none">
        <q-item>
          <q-item-section avatar>
            <q-icon color="white" name="library_add_check" style="font-size: 2em;"/>
          </q-item-section>

          <q-item-section>
            <div class="text-h6">To do list</div>
            <div class="text-subtitle2">All Your to do list</div>
          </q-item-section>
        </q-item>
      </q-card-section>
      <q-card-section class="">
        <TodoComponent></TodoComponent>
      </q-card-section>
    </q-card>
  </div>
</template>

<script setup>
import {defineAsyncComponent} from 'vue'

const TodoComponent = defineAsyncComponent(() =>  import('components/MainIndex/List/TodoComponent.vue'))

</script>
