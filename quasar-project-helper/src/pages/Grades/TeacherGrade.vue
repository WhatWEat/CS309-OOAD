<template>
  <div class="q-pa-md">
    <q-file
      color="purple-12"
      v-model="model"
      label="upload file to grade"
      @update:model-value="onFileChange"
    >
      <template v-slot:prepend>
        <q-icon name="attach_file" />
      </template>
    </q-file>
    <q-dialog v-model="isShowDialog">
      <q-banner>Are you sure to save this file</q-banner>
      <q-card-actions class="q-px-md">
        <q-btn label="Upload" color='green' @click="saveUploadAvatar"/>
        <q-btn label="Cancel" color="red" @click="cancelUploadAvatar"/>
      </q-card-actions>
      </q-dialog>
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
import {formatDateString, usePersonId} from "src/composables/usefulFunction";
import {onMounted, ref, watch} from 'vue';
import {defaultTGrade, tgradeProps} from "src/composables/comInterface";
import {api} from "boot/axios"
import { useRouter } from 'vue-router'
import LineChart from "components/Chart/LineChart.vue"
import BarChart from "components/Chart/BarChart.vue";
import {useUserStore} from "src/composables/useUserStore";
import {watchEffect} from "vue-demi";

const {identity} = useUserStore();
const  router = useRouter()
const data = ref<tgradeProps[]>([defaultTGrade]);
const loading = ref(true)
const filter = ref('')
const person_id = ref(1);
const model = ref(null),isShowDialog = ref(false), avatar_preview = ref('')
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


onMounted(() => {
  person_id.value = usePersonId();
  console.log('person_id',person_id.value)
  watchEffect(() => {
    if (identity.value == 3) {
      api.get(`/grade_list/${person_id.value}`).then((res) => {
        console.log(res.data)
        personInfo.value = res.data.body
        copyPersonInfo()
        username.value = personInfo.value.name
      })
      api.get(`/get_avatar/${person_id.value}`, { responseType: 'arraybuffer' }).then((res) => {
        const blob = new Blob([res.data], { type: 'image/jpeg' });
        avatar_preview.value = URL.createObjectURL(blob);
        avatar_clone.value = avatar_preview.value;
      })
    }
  })
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

function saveUploadAvatar() {
  isShowDialog.value = false;
  if(model.value){
    data.value.name = model.value.name;
    model.value = null;
  }
}

function cancelUploadAvatar() {
  isShowDialog.value = false;
  model.value = null;
}
function onFileChange(){
  const file = model.value
  if(file){
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => {
      avatar_preview.value = reader.result as string
    }
    reader.onerror = (error) => {
      console.log(error)
    }
    isShowDialog.value = true
  }
}


</script>
