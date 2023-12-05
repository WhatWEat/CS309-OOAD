<template>
  <q-file
    color="purple-12"
    v-model="model"
    label="upload file to add person"
  >
    <template v-slot:prepend>
      <q-icon name="attach_file"/>
    </template>
  </q-file>
  <q-table
    title="Create"
    :rows="computedRows"
    :columns="columns"
    row-key="compositeKey"
    :loading="loading"
    v-model:pagination="pagination"
    flat>
    <template v-slot:top>
      <span>Manage Project</span>
      <q-space/>
      <q-btn
        color="primary"
        icon="upload"
        round
        flat
        @click="clickUpload"
      />
      <q-btn round flat color="red" icon="close" @click="model=null"/>
    </template>
    <template v-slot:body="props">
      <q-tr :props="props">
        <q-td key="userId" :props="props">
          <span>{{ props.row.userId }}</span>
        </q-td>
        <q-td key="projectId" :props="props">
          <span>{{ props.row.projectId }}</span>
        </q-td>
        <q-td key="projectName" :props="props">
          <span>{{ props.row.projectName }}</span>
        </q-td>
      </q-tr>
    </template>
  </q-table>
</template>

<script setup lang="ts">
import {onMounted, ref, watch} from "vue";
import {computed} from "vue-demi";
import {api} from "boot/axios";
import {useQuasar} from "quasar";

interface dataProps {
  projectId: number;
  projectName: string;
  userId: number;
}

const rows = ref<dataProps[]>([{
  projectId: 1,
  projectName: 'test',
  userId: 1
}]);
const computedRows = computed(() => {
  return rows.value.map(row => ({
    ...row,
    compositeKey: `${row.projectId}_${row.userId}` // 创建组合键
  }));
});

const columns = ref([
  {
    name: "projectId",
    required: true,
    label: "Project Id",
    align: "left",
    field: row => row.projectId,
    format: val => `${val}`
  },
  {
    name: "userId",
    required: true,
    label: "student ID",
    align: "left",
    field: row => row.userId,
    format: val => `${val}`,
    sortable: true
  },
  {
    name: "projectName",
    required: true,
    label: "Project Name",
    align: "left",
    field: row => row.password,
    format: val => `${val}`
  },
]);
const loading = ref(true), model = ref<File | null>(null);
onMounted(()=>{
  onFresh();
})
function onFresh() {
  loading.value = true;
  api.get(`/tea/get_stu_of_all_proj/${0}/${
    pagination.value.rowsPerPage == 0 ? 9999 : pagination.value.rowsPerPage * pagination.value.page
  }`).then(res => {
    console.log(res.data)
    rows.value = [];
    for(let i of res.data.body){
      for (let j of i.stuIds){
        rows.value.push({
          projectId: i.projectId,
          projectName: i.description,
          userId: j
        })
      }
    }
    console.log(rows.value)
    loading.value = false;
  }).catch(err => {
    console.log(err)
  })
}

const pagination = ref({
  page: 1,
  rowsPerPage: 10,
});
watch(pagination, (newVal, oldVal) => {
  console.log('new pa', newVal)
  if (
    newVal.page !== oldVal.page ||
    newVal.rowsPerPage !== oldVal.rowsPerPage
  ) {
    onFresh();
  }
});
const $q = useQuasar();
function clickUpload() {
  console.log('click upload')
  let formData = new FormData();
  if (model.value === null) return;
  formData.append('file', model.value as File);
  api.post('/tea/add_stu_to_project', formData).then(res => {
    console.log(res.data)
    $q.notify({
      message: res.data.msg,
      position: 'center'
    })
    onFresh();
  }).catch(err => {
    $q.notify({
      message: 'wrong file format',
      position: 'center'
    })
    console.log(err)
  })
}
</script>

<style scoped>

</style>
