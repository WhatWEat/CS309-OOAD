<template>
  <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
    <q-card
      class="q-ma-xs no-shadow"
      bordered
      style="background-color: #38b9c5"
    >
      <q-card-section class="text-h6 text-white">
        <q-avatar>
          <q-icon name="menu_book" color="white"></q-icon>
        </q-avatar>
        Assignment
      </q-card-section>
      <q-card-section class="bg-white q-mb-sm q-mx-xs">
        <q-scroll-area style="height: 200px">
          <q-infinite-scroll
            @load="getAss"
            :offset="10"
            v-if="loadingMessage && messages.length > 0"
          >
            <q-item
              v-for="msg in messages"
              :key="msg.assignmentId"
              clickable
              v-ripple
            >
              <q-item-section>
                <q-item-label>{{ msg.title }}</q-item-label>
                <q-item-label caption lines="1" class="ellipsis"
                  >{{ truncate(msg.description) }}
                </q-item-label>
              </q-item-section>
              <q-item-section side>
                {{ formatDateString(msg.deadline) }}
              </q-item-section>
            </q-item>
          </q-infinite-scroll>
          <div v-else>You don't have any assignments yet.</div>
        </q-scroll-area>
      </q-card-section>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import {
  truncate,
  useProjectId,
  formatDateString,
} from "src/composables/usefulFunction";
import { onMounted, ref } from "vue";
import { assProps } from "src/composables/comInterface";
import { api } from "boot/axios";

const messages = ref<assProps[]>([]);
const loadingMessage = ref(false),
  project_id = ref(0),
  count = ref(0);
onMounted(() => {
  project_id.value = useProjectId();
  getAss(0, () => void 0);
});

function getAss(index = 0, done: () => void) {
  if (index > count.value) {
    done();
    return;
  }
  api
    .get(`/ass-list/${project_id.value}/${count.value}/8`)
    .then((res) => {
      let page_ass: assProps[] = res.data.body;
      messages.value.push.apply(messages.value, page_ass);
      loadingMessage.value = true;
      if (page_ass.length === 0) {
        done();
        return;
      }
      count.value++;
      done();
    })
    .catch((err) => {
      console.log("err", err);
    });
}
</script>

<style scoped></style>
