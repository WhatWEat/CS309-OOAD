<template>
  <div class="q-pa-md">
    <q-file color="purple-12" v-model="model1" label="upload file to grade">
      <template v-slot:prepend>
        <q-icon name="attach_file" />
      </template>
    </q-file>
    <q-table
      title="Grades"
      :rows="data"
      :columns="columns"
      :filter="filter"
      row-key="name"
      v-model:pagination="pagination"
      :loading="loading"
      flat>
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td key="homework" :props="props">
            <span>{{ props.row.name }}</span>
          </q-td>
          <q-td key="studentID" :props="props">
            <span>{{ props.row.studentID }}</span>
          </q-td>
          <q-td key="grade" :props="props">
            <span>{{ props.row.grade }}</span>
            <q-popup-edit v-model="props.row.grade" title="Update the grade" buttons v-slot="scope">
              <q-input type="number" v-model="scope.value" dense autofocus />
            </q-popup-edit>
          </q-td>
          <q-td key="comment" :props="props">
            <span>{{ props.row.comment }}</span>
            <q-popup-edit
              buttons
              v-model="props.row.comment"
              v-slot="scope"
            >
              <q-editor
                v-model="scope.value"
                min-height="5rem"
                autofocus
                @keyup.enter.stop
              />
            </q-popup-edit>
          </q-td>
          <q-td key="reviewerName" :props="props">
            <span>{{ props.row.reviewerName }}</span>
          </q-td>
          <q-td key="createTime" :props="props">
            <span>{{ formatDateString(props.row.createTime) }}</span>
          </q-td>
        </q-tr>
      </template>
      <template v-slot:top-right>
        <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>
      </template>
    </q-table>
    <q-separator v-if="data.length > 0"/>
  </div>
  <div class="row q-col-gutter-sm q-py-sm">
    <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
      <bar-chart></bar-chart>
    </div>
    <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
      <line-chart></line-chart>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {formatDateString} from "src/composables/usefulFunction";
import { ref, watch} from 'vue';
import {defaultTGrade, tgradeProps} from "src/composables/comInterface";
import {api} from "boot/axios"
import {useQuasar} from "quasar";
import { useRouter } from 'vue-router'
import LineChart from "components/Chart/LineChart.vue"
import BarChart from "components/Chart/BarChart.vue";

const  router = useRouter()
const projectID = ref(router.currentRoute.value.params.projectID)
const data = ref<tgradeProps[]>([defaultTGrade]);
const loading = ref(true)
const filter = ref('')
const columns = [
  {
    name: "homework",
    required: true,
    label: "homework",
    align: "left",
    field: row => row.name,
    format: val => `${val}`,
    sortable: true
  },
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
    name: "grade",
    required: true,
    label: "grade",
    align: "left",
    field: row => row.grade,
    format: val => `${val}`,
    sortable: true
  },
  {
    name: "comment",
    required: true,
    label: "comment",
    align: "left",
    field: row => row.comment,
    format: val => `${val}`,
    sortable: true
  },
  {
    name: "reviewerName",
    required: true,
    label: "reviewer",
    align: "center",
    field: row => row.reviewerName,
    format: val => `${val}`,
    sortable: true
  },
  {
    name: "createTime",
    required: true,
    label: "create time",
    align: "left",
    field: row => row.createTime,
    format: val => `${val}`,
    sortable: true
  },
]

const pagination = ref({
  page: 1,
  rowsPerPage: 10,
})

watch(pagination, (newVal, oldVal)=>{
  if (newVal.page !== oldVal.page || newVal.rowsPerPage !== oldVal.rowsPerPage){
    onRefresh();
  }
})
async function onRefresh() {
  loading.value = true
  api.get(`/grade-list/${pagination.value.page-1}/${pagination.value.rowsPerPage}`).then((res) => {
    data.value = res.data.body;
    loading.value = false

  }).catch((err) => {
    console.log('err', err)
  })
}

function turnToChart() {
  router.push('/projects/${projectID.value}/sgrade/chart')
}
</script>
