<template>
  <q-card class="bg-transparent no-shadow no-border" bordered>
    <q-card-section class="q-pa-none">
      <div class="row q-col-gutter-sm ">
        <div v-for="(item, index) in items" :key="index" class="col-md-3 col-sm-6 col-xs-12">
          <q-item :style="`background-color: ${item.color1}`" class="q-pa-none">
            <q-item-section v-if="icon_position === 'left'" side :style="`background-color: ${item.color2}`"
                            class=" q-pa-lg q-mr-none text-white">
              <q-icon :name="item.icon" color="white" size="24px"></q-icon>
            </q-item-section>
            <q-item-section class=" q-pa-md q-ml-none  text-white">
              <q-item-label class="text-white text-h6 text-weight-bolder">{{ item.value }}</q-item-label>
              <q-item-label>{{ item.title }}</q-item-label>
            </q-item-section>
            <q-item-section v-if="icon_position === 'right'" side class="q-mr-md text-white">
              <q-icon :name="item.icon" color="white" size="44px"></q-icon>
            </q-item-section>
          </q-item>
        </div>
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {computed} from 'vue-demi';
import {ref} from 'vue';

const props = defineProps({
  icon_position: {
    required: false,
    default: 'left'
  }
});

const items = ref(computed(() => {
  return props.icon_position === 'left'
    ? [
      {
        title: 'Profile',
        icon: 'person',
        value: '1',
        color1: '#5064b5',
        color2: '#3e51b5'
      },
      {
        title: 'Group Member',
        icon: 'fa-solid fa-user-group',
        value: '500',
        color1: '#f37169',
        color2: '#f34636'
      },
      {
        title: 'Assignment',
        icon: 'fa-solid fa-file',
        value: '50',
        color1: '#ea6a7f',
        color2: '#ea4b64'
      },
      {
        title: 'Messages',
        icon: 'fa-solid fa-comments',
        value: '100',
        color1: '#f8a261',
        color2: '#f76b1c'
      }
    ]
    : [
      {
        title: 'Profile',
        icon: 'person',
        value: '1',
        color1: '#5064b5',
        color2: '#3e51b5'
      },
      {
        title: 'Projects',
        icon: 'fa-solid fa-person-digging',
        value: '500',
        color1: '#f37169',
        color2: '#f34636'
      },
      {
        title: 'Announcements',
        icon: 'fa-solid fa-envelope',
        value: '50',
        color1: '#ea6a7f',
        color2: '#ea4b64'
      },
      {
        title: 'Messages',
        icon: 'fa-solid fa-comments',
        value: '100',
        color1: '#f8a261',
        color2: '#f76b1c'
      }
    ]
}));

</script>
