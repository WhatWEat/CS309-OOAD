<template>
  <q-layout view="hHh lpR fFf">
    <q-header elevated>
      <q-toolbar>
        <q-toolbar-title>
          Project Helper
          <q-btn dense flat icon="home" @click="router.push('/')">
          </q-btn>
        </q-toolbar-title>
        <PersonBar></PersonBar>
      </q-toolbar>
    </q-header>

    <q-page-container>
  <div class="q-pa-md">

<!--    此处是创建-->
    <q-file color="purple-12" v-model="model1" label="upload file to create">
      <template v-slot:prepend>
        <q-icon name="attach_file" />
      </template>
    </q-file>
    <q-table
      title="Create"
      :rows="data1"
      :columns="columns"
      :filter="filter1"
      row-key="name"
      v-model:pagination="pagination1"
      flat>
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td key="studentID" :props="props">
            <span>{{ props.row.studentID }}</span>
          </q-td>
          <q-td key="password" :props="props">
            <span>{{ props.row.password }}</span>
          </q-td>
          <q-td key="email" :props="props">
            <span>{{ props.row.email }}</span>
          </q-td>
          <q-td key="phone" :props="props">
            <span>{{ props.row.phone }}</span>
          </q-td>
        </q-tr>
      </template>
      <template v-slot:top-right>
        <q-input borderless dense debounce="300" v-model="filter1" placeholder="Search">
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>
      </template>
    </q-table>
    <q-separator v-if="data1.length > 0"/>

<!--    此处是重置-->
    <q-file color="purple-12" v-model="model2" label="upload file to reset">
      <template v-slot:prepend>
        <q-icon name="attach_file" />
      </template>
    </q-file>
    <q-table
      title="Reset"
      :rows="data2"
      :columns="columns"
      :filter="filter2"
      row-key="name"
      v-model:pagination="pagination2"
      flat>
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td key="studentID" :props="props">
            <span>{{ props.row.studentID }}</span>
          </q-td>
          <q-td key="password" :props="props">
            <span>{{ props.row.password }}</span>
          </q-td>
          <q-td key="email" :props="props">
            <span>{{ props.row.email }}</span>
          </q-td>
          <q-td key="phone" :props="props">
            <span>{{ props.row.phone }}</span>
          </q-td>
        </q-tr>
      </template>
      <template v-slot:top-right>
        <q-input borderless dense debounce="300" v-model="filter2" placeholder="Search">
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>
      </template>
    </q-table>
    <q-separator v-if="data2.length > 0"/>


<!--    此处是冻结-->
    <q-file color="purple-12" v-model="model3" label="upload file to freeze">
      <template v-slot:prepend>
        <q-icon name="attach_file" />
      </template>
    </q-file>
    <q-table
      title="Freeze"
      :rows="data3"
      :columns="columns"
      :filter="filter3"
      row-key="name"
      v-model:pagination="pagination3"
      flat>
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td key="studentID" :props="props">
            <span>{{ props.row.studentID }}</span>
          </q-td>
          <q-td key="password" :props="props">
            <span>{{ props.row.password }}</span>
          </q-td>
          <q-td key="email" :props="props">
            <span>{{ props.row.email }}</span>
          </q-td>
          <q-td key="phone" :props="props">
            <span>{{ props.row.phone }}</span>
          </q-td>
        </q-tr>
      </template>
      <template v-slot:top-right>
        <q-input borderless dense debounce="300" v-model="filter3" placeholder="Search">
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>
      </template>
    </q-table>
    <q-separator v-if="data3.length > 0"/>

<!--    此处是解冻-->
    <q-file color="purple-12" v-model="model4" label="upload file to unfreeze">
      <template v-slot:prepend>
        <q-icon name="attach_file" />
      </template>
    </q-file>
    <q-table
      title="Unfreeze"
      :rows="data4"
      :columns="columns"
      :filter="filter4"
      row-key="name"
      v-model:pagination="pagination4"
      flat>
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td key="studentID" :props="props">
            <span>{{ props.row.studentID }}</span>
          </q-td>
          <q-td key="password" :props="props">
            <span>{{ props.row.password }}</span>
          </q-td>
          <q-td key="email" :props="props">
            <span>{{ props.row.email }}</span>
          </q-td>
          <q-td key="phone" :props="props">
            <span>{{ props.row.phone }}</span>
          </q-td>
        </q-tr>
      </template>
      <template v-slot:top-right>
        <q-input borderless dense debounce="300" v-model="filter4" placeholder="Search">
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>
      </template>
    </q-table>
    <q-separator v-if="data4.length > 0"/>
  </div>
    </q-page-container>
  </q-layout>
</template>

<script lang="ts" setup>
import {formatDateString} from "src/composables/usefulFunction";
import { ref, watch} from 'vue';
import {
  defaultCreate,
  defaultFreeze,
  defaultReset,
  defaultUnfreeze,
  gradeProps
} from "src/composables/comInterface";
import {api} from "boot/axios"
import {useQuasar} from "quasar";
import { useRouter } from 'vue-router'
import PersonBar from "components/Layout/PersonBar.vue";

const  router = useRouter()
const projectID = ref(router.currentRoute.value.params.projectID)
const data1 = ref<createProps[]>([defaultCreate]);
const data2 = ref<resetProps[]>([defaultReset]);
const data3 = ref<freezeProps[]>([defaultFreeze]);
const data4 = ref<unfreezeProps[]>([defaultUnfreeze]);
const filter1 = ref(''), filter2 = ref(''), filter3 = ref(''), filter4 = ref('')
const model1 = ref(null), model2 = ref(null), model3 = ref(null), model4 = ref(null)
const columns = [
  {
    name: "studentID",
    required: true,
    label: "studentID",
    align: "left",
    field: row => row.studentID,
    format: val => `${val}`,
    sortable: true
  },
  {
    name: "password",
    required: true,
    label: "password",
    align: "left",
    field: row => row.password,
    format: val => `${val}`
  },
  {
    name: "email",
    required: true,
    label: "email",
    align: "left",
    field: row => row.email,
    format: val => `${val}`
  },
  {
    name: "phone",
    required: true,
    label: "phone",
    align: "left",
    field: row => row.phone,
    format: val => `${val}`
  },
]

const pagination1 = ref({
  page: 1,
  rowsPerPage: 10,
})

const pagination2 = ref({
  page: 1,
  rowsPerPage: 10,
})

const pagination3 = ref({
  page: 1,
  rowsPerPage: 10,
})

const pagination4 = ref({
  page: 1,
  rowsPerPage: 10,
})
watch(pagination1, (newVal, oldVal)=>{
  if (newVal.page !== oldVal.page || newVal.rowsPerPage !== oldVal.rowsPerPage){
    onRefresh1();
  }
})

watch(pagination2, (newVal, oldVal)=>{
  if (newVal.page !== oldVal.page || newVal.rowsPerPage !== oldVal.rowsPerPage){
    onRefresh2();
  }
})

watch(pagination3, (newVal, oldVal)=>{
  if (newVal.page !== oldVal.page || newVal.rowsPerPage !== oldVal.rowsPerPage){
    onRefresh3();
  }
})

watch(pagination4, (newVal, oldVal)=>{
  if (newVal.page !== oldVal.page || newVal.rowsPerPage !== oldVal.rowsPerPage){
    onRefresh4();
  }
})
async function onRefresh1() {
  api.post(`/adm/create_multiple_users_with_file`).then((res) => {
    data1.value = res.data.body;
  }).catch((err) => {
    console.log('err', err)
  })
}
async function onRefresh2() {
  api.get(`/batch-reset/${pagination.value.page-1}/${pagination.value.rowsPerPage}`).then((res) => {
    data2.value = res.data2.body;

  }).catch((err) => {
    console.log('err', err)
  })
}
async function onRefresh3() {
  api.get(`/batch-freeze/${pagination.value.page-1}/${pagination.value.rowsPerPage}`).then((res) => {
    data3.value = res.data3.body;

  }).catch((err) => {
    console.log('err', err)
  })
}
async function onRefresh4() {
  api.get(`/batch-unfreeze/${pagination.value.page-1}/${pagination.value.rowsPerPage}`).then((res) => {
    data4.value = res.data4.body;

  }).catch((err) => {
    console.log('err', err)
  })
}
</script>
