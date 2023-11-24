<template>
  <q-item
    v-if="!list"
    clickable
    tag="a"
    :to="link"
  >
    <q-item-section
      v-if="icon"
      avatar
    >
      <q-icon :name="icon"/>
    </q-item-section>

    <q-item-section>
      <q-item-label>{{ title }}</q-item-label>
    </q-item-section>
  </q-item>
  <q-expansion-item
    v-else
    :label="title"
    :icon="icon"
  >
    <q-list class="q-pl-lg">
      <q-item v-for="(item, index) in subItems" :key="index"
              :to="item.link" active-class="q-item-no-link-highlighting">
        <q-item-section avatar>
          <q-icon name="pages"></q-icon>
        </q-item-section>
        <q-item-section>
          <q-item-label class="text-h12 q-mt-sm q-mb-xs text-grey-8">{{ item.label }}</q-item-label>
        </q-item-section>
      </q-item>
      <q-item :to='`/person/${userid}/projects`'  active-class="q-item-no-link-highlighting">
        <q-item-section>
          <q-item-label class="text-h6 ">Click Here to See More!</q-item-label>
        </q-item-section>
      </q-item>
    </q-list>
  </q-expansion-item>
</template>

<script setup lang="ts">
import {useUserStore} from 'src/composables/useUserStore';
const {userid} = useUserStore()
export interface EssentialLinkProps {
  title: string;
  list?: boolean;
  subItems?: Array<{
    label: string;
    index: string;
    link: string;
    icon: string;
  }>;
  caption?: string;
  link?: string;
  icon?: string;
}

withDefaults(defineProps<EssentialLinkProps>(), {
  caption: '',
  link: '#',
  icon: '',
  list: false,
  subItems: () => [{
    label: 'test-CS309',
    index: '1',
    link: '/projects/1',
    icon: 'list'
  }, {
    label: 'test-CS307',
    index: '2',
    link: '/projects/2',
    icon: 'list'
  }]
});
</script>
