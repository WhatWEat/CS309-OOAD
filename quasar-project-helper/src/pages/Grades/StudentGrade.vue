<template>
  <div class="q-pa-md">
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
          <q-td key="grade" :props="props">
            <span>{{ props.row.grade }}</span>
          </q-td>
          <q-td key="contribution" :props="props">
            <span>{{ props.row.contribution }}</span>
          </q-td>
          <q-td key="comment" :props="props">
            <span>{{ props.row.comment }}</span>
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
</template>

<script lang="ts" setup>
import {formatDateString} from "src/composables/usefulFunction";
import { ref, watch} from 'vue';
import {defaultGrade, gradeProps} from "src/composables/comInterface";
import {api} from "boot/axios"
import {useQuasar} from "quasar";
import { useRouter } from 'vue-router'

const  router = useRouter()
const projectID = ref(router.currentRoute.value.params.projectID)
const data = ref<gradeProps[]>([defaultGrade]);
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
    name: "grade",
    required: true,
    label: "grade",
    align: "left",
    field: row => row.grade,
    format: val => `${val}`,
    sortable: true
  },
  {
    name: "contribution",
    required: true,
    label: "contribution",
    align: "left",
    field: row => row.contribution,
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

</script>
