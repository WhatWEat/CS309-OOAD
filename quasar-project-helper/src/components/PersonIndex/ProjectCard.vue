<template>
  <q-card>
    <q-card-section>
      <q-splitter
        v-model="splitterModel"
        style="height: 40vh"
      >
        <template v-slot:before>
          <q-tabs
            v-model="tab"
            vertical
            class="text-teal"
          >
            <q-tab name="Project" icon="mail" label="Project" />
            <q-tab name="Group Member" icon="alarm" label="Group Member" />
            <q-tab name="Person" icon="movie" label="Person" />
          </q-tabs>
        </template>

        <template v-slot:after>
          <q-tab-panels
            v-model="tab"
            animated
            swipeable
            vertical
            transition-prev="jump-up"
            transition-next="jump-up"
          >
            <q-tab-panel name="Project">
              <div class="text-h4 q-mb-md">{{title}}</div>
              <p>{{description}}</p>
            </q-tab-panel>

            <q-tab-panel name="Group Member">
              <div class="text-h4 q-mb-md">Team Member</div>
              <div v-if="group.length === 0">
                  <p> You haven't joined any group</p>
              </div>
              <div>
                <q-list bordered separator>
                  <q-item v-for="(item, index) in group" :key="index"
                          :href="`/person/${item}`" clickable v-ripple>
                    <q-item-section>
                      <q-icon name="workspaces"></q-icon>
                    </q-item-section>
                    <q-item-section>
                      <q-item-label class="text-blue-grey-9 text-caption justify-start">{{ item }}</q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>
              </div>
            </q-tab-panel>

            <q-tab-panel name="Person">
              <div class="text-h4 q-mb-md">Person Technique</div>
              <div class="q-ma-md row no-wrap">
                <q-scroll-area
                  visible
                  :thumb-style="thumbStyle"
                  :bar-style="barStyle"
                  style="height: 200px;"
                  class="col"
                  ref="firstRef"
                >
                  <div v-for="n in 100" :key="n" class="q-pa-sm">
                    Lorem ipsum dolor sit amet, consectetur adipisicing
                    elit, sed do eiusmod tempor incididunt ut labore et
                    dolore magna aliqua.
                  </div>
                </q-scroll-area>

                <q-scroll-area
                  visible
                  :thumb-style="thumbStyle"
                  :bar-style="barStyle"
                  style="height: 200px;"
                  class="col"
                  ref="secondRef"
                >
                  <div v-for="n in 100" :key="n" class="q-pa-sm">
                    Lorem ipsum dolor sit amet, consectetur adipisicing
                    elit, sed do eiusmod tempor incididunt ut labore et
                    dolore magna aliqua.
                  </div>
                </q-scroll-area>
              </div>
            </q-tab-panel>
          </q-tab-panels>
        </template>

      </q-splitter>
    </q-card-section>
  </q-card>

</template>

<script setup lang="ts">
import {ref} from 'vue';

const tab = ref('Project')
const splitterModel = ref(15)

export interface ProjectCardProps {
  title: string;
  description: string;
  link: string;
  group: number[];
  person: string[];
} withDefaults(defineProps<ProjectCardProps>(),{
  title: 'Project Title',
  description: 'Project Description',
  link: '/projects/1',
  group: () => [1, 2, 3],
  person: () => ['1', '2', '3']
})


const thumbStyle = {
  right: '4px',
  borderRadius: '7px',
  backgroundColor: 'rgba(2,167,227,0.96)',
  width: '4px',
  opacity: 0.75,
};

const barStyle = {
  right: '2px',
  borderRadius: '9px',
  backgroundColor: '#0264e3',
  width: '8px',
  opacity: 0.2,
};
</script>

