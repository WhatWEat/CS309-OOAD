<template>
  <q-item
    v-if="!list"
    clickable
    tag="a"
    :href="link"
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
    <q-list bordered separator dense>
      <q-item v-for="(item, index) in subItems" :key="index"
              :href="item.link" clickable v-ripple>
        <q-item-section>
          <q-icon name="workspaces"></q-icon>
        </q-item-section>
        <q-item-section>
          <q-item-label class="text-blue-grey-9 text-caption justify-start">{{ item.label }}</q-item-label>
        </q-item-section>
      </q-item>
      <q-item clickable href="projects" class="row justify-end">
<!--        <q-item-section v-for="(number, index) in 2" :key="index">-->
<!--          <q-icon name="more_horiz" class="text-cyan-10"></q-icon>-->
<!--        </q-item-section>-->
        <q-item-section>
          <q-item-label class="text-blue-grey-8 text-caption justify-start">Click Here to See More!</q-item-label>
        </q-item-section>
      </q-item>
    </q-list>
  </q-expansion-item>
</template>

<script setup lang="ts">


export interface EssentialLinkProps {
  title: string;
  list?: boolean;
  subItems?: Array<{
    label: string;
    index: string;
    link: string
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
    link: '/projects/1'
  }, {
    label: 'test-CS307',
    index: '2',
    link: '/projects/2'
  }]
});
</script>
