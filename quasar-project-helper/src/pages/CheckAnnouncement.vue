<template>
  <q-layout view="lHh lpr lFf" container style="height: 3000px" class="shadow-2 rounded-borders">

  <q-header elevated class="bg-primary text-white" height-hint="98">
    <q-toolbar>
      <q-toolbar-title>
        <q-btn dense @click="router.push('/')">
          <q-icon name="home"></q-icon>
        </q-btn>
        <span >Announcement</span>
      </q-toolbar-title>
      <PersonBar></PersonBar>
    </q-toolbar>
  </q-header>
  <q-page-container>
    <q-page class="q-pa-md">
        <div class="notification-layout" v-for="n in 15" :key="n">
          <div class="notification-table">
            <q-table
              :rows="notifications"
              :columns="columns"
              row-key="id"
            >
              <template v-slot:body="props">
                <q-tr :props="props">
                  <q-td >{{ props.row.title }}</q-td>
                  <q-td >{{ props.row.content }}</q-td>
                  <q-td >{{ props.row.publisher }}</q-td>
                  <q-td >{{ props.row.publishTime }}</q-td>
                </q-tr>
              </template>
              <template v-slot:bottom>
              </template>

            </q-table>
          </div>
        </div>
    </q-page>
  </q-page-container>

  </q-layout>
</template>

<script setup>

import {useRouter} from 'vue-router';
import PersonBar from "components/Layout/PersonBar.vue";

const router = useRouter()

// 定义通知数据和列定义
const notifications = [
  { id: 1, title: '通知标题 ', content: '通知内容 ', publisher: 'A', publishTime: '2023'},
];

const columns = [
  { name: 'title', required: true, label: '标题', align: 'left', field: 'title', sortable: true },
  { name: 'content', required: true, label: '内容', align: 'left', field: 'content'},
  { name: 'publisher', required: true, label: '发布人', align: 'left', field: 'publisher' },
  { name: 'publishTime', required: true, label: '发布时间', align: 'left', field: 'publisher', sortable: true }
];

</script>
