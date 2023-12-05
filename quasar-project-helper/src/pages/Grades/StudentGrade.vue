<template>
  <div class="q-pa-md">
    <q-file
      v-if="identity<=2 && identity>=0"
      color="purple-12"
      v-model="model"
      label="upload file to grade"
      @update:model-value="showDialog"
    >
      <template v-slot:prepend>
        <q-icon name="attach_file"/>
      </template>
    </q-file>
    <q-dialog v-model="isShowDialog" v-if="identity<=2 && identity>=0">
      <q-table
        :rows="data"
        :columns="columns1"
        row-key="id"
        dense
        flat
        bordered
      >
        <template v-slot:top-right>
          <q-btn label="Upload" color="green" @click="saveUploadAvatar"/>
          <q-btn label="Cancel" color="red" @click="cancelUploadAvatar"/>
        </template>
      </q-table>
    </q-dialog>
    <q-table
      title="Grades"
      :grid="$q.screen.lt.sm"
      :rows="data"
      :columns="columns"
      :filter="filter"
      row-key="name"
      v-model:pagination="pagination"
      :loading="isLoadingGrade"
      flat>
      <template v-slot:item="props">
        <div
          class="q-py-xs col-12 grid-style-transition"
          :style="props.selected ? 'transform: scale(0.95);' : ''"
        >
          <q-card :class="props.selected ? 'bg-grey-2' : ''">
            <q-card-section class="row items-center">
              <q-checkbox dense v-model="props.selected" :label="props.row.name"/>
              <q-space>
              </q-space>
            </q-card-section>
            <q-separator/>
            <q-list dense>
              <q-item v-for="col in props.cols.filter(col => col.name !== 'submittedTime')"
                      :key="col.name">
                <q-item-section>
                  <q-item-label>{{ col.label }}</q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-item-label caption>
                    <div v-html="truncate(col.value,20)">
                    </div>
                  </q-item-label>
                </q-item-section>
              </q-item>
              <q-item>
                <q-item-section>
                  <q-item-label> submittedTime</q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-item-label caption>
                    {{ formatDateString(props.row.submittedTime) }}
                  </q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </q-card>
        </div>
      </template>
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td key="assignmentId" :props="props">
            <span>{{ props.row.assignmentId }}</span>
          </q-td>
          <q-td key="title" :props="props">
            <span>{{ props.row.title }}</span>
          </q-td>
          <q-td key="submitterId" :props="props">
            <span>{{ props.row.submitterId }}</span>
          </q-td>
          <q-td key="submitterName" :props="props">
            <span>{{ props.row.submitterName }}</span>
          </q-td>
          <q-td key="grade" :props="props">
            <span>{{ props.row.grade }}</span>
            <q-popup-edit v-model="props.row.grade" title="Update the grade" buttons v-slot="scope"
                          v-if="identity<=2 && identity>=0" @save="save(props.row)">
              <q-input type="number" v-model="scope.value" dense autofocus/>
            </q-popup-edit>
          </q-td>
          <q-td key="comment" :props="props">
            <span>{{ props.row.comment }}</span>
            <q-popup-edit
              buttons
              v-model="props.row.comment"
              v-slot="scope"
              @save="save(props.row)"
              v-if="identity<=2 && identity>=0"
            >
              <q-editor
                v-model="scope.value"
                min-height="5rem"
                autofocus
                @keyup.enter.stop
              />
            </q-popup-edit>
          </q-td>
          <q-td key="submittedTime" :props="props">
            <span>{{ formatDateString(props.row.submittedTime) }}</span>
          </q-td>
        </q-tr>
      </template>
      <template v-slot:top-right>
        <q-btn flat round @click="isLoadingChart=true" v-if="identity<=2 && identity >= 0">
          <q-avatar icon="equalizer" size="42px">

          </q-avatar>
        </q-btn>
        <q-space style="width: 20px"/>
        <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
          <template v-slot:append>
            <q-icon name="search"/>
          </template>
        </q-input>
      </template>
    </q-table>
    <q-separator v-if="data.length > 0"/>
  </div>
  <q-dialog v-model="isLoadingChart">
    <ChartShow>
    </ChartShow>
  </q-dialog>
<!--    <div class="row q-col-gutter-sm q-py-sm" v-if="identity<=2 && identity>=0">-->
<!--      <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">-->
<!--        <bar-chart></bar-chart>-->
<!--      </div>-->
<!--      <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">-->
<!--        <pie-chart></pie-chart>-->
<!--      </div>-->
<!--    </div>-->
</template>

<script lang="ts" setup>
import {formatDateString, truncate, useProjectId} from "src/composables/usefulFunction";
import {nextTick, onMounted, ref, watch} from 'vue';
import {defaultGrade, gradeProps} from "src/composables/comInterface";
import {api} from "boot/axios"
import {useRouter} from 'vue-router'
import {useUserStore} from "src/composables/useUserStore";
import {watchEffect} from "vue-demi";
import {useQuasar} from "quasar";
import BarChart from "components/Chart/BarChart.vue";
import PieChart from "components/Chart/PieChart.vue";
import ChartShow from "components/Chart/ChartShow.vue";
//import LineChart from "components/Chart/LineChart.vue";

const {identity} = useUserStore()
const router = useRouter()
const project_id = ref(router.currentRoute.value.params.projectID)
const data = ref<gradeProps[]>([]);
//const data1 = ref<gradeProps[]>([]);
//const newData = ref<gradeProps[]>([]);
const filter = ref('')
const $q = useQuasar();
const assignmentID = ref(-1);
const review = ref('wang')
const columns = [
  {
    name: "assignmentId",
    required: true,
    label: "assignmentId",
    align: "left",
    field: row => row.assignmentId,
    format: val => `${val}`,
    sortable: true
  },
  {
    name: "title",
    required: true,
    label: "title",
    align: "left",
    field: row => row.title,
    format: val => `${val}`,
    sortable: true
  },
  {
    name: "submitterId",
    required: true,
    label: "submitterId",
    align: "left",
    field: row => row.submitterId,
    format: val => `${val}`,
    sortable: true
  },
  {
    name: "submitterName",
    required: true,
    label: "submitterName",
    align: "left",
    field: row => row.submitterName,
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
    name: "submittedTime",
    required: true,
    label: "submitted time",
    align: "left",
    field: row => row.submittedTime,
    format: val => `${val}`,
    sortable: true
  },
]

const columns1 = [
  {
    name: "submitterId",
    required: true,
    label: "submitterId",
    align: "left",
    field: row => row.submitterId,
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
]

const pagination = ref({
  page: 1,
  rowsPerPage: 10,
})
// 初始化
const isLoadingGrade = ref(true);
onMounted(() => {
  project_id.value = useProjectId().toString();
  watchEffect(() => {
    if (identity.value !== -1 && isLoadingGrade.value) {
      console.log('initit')
      onRefresh();
      isLoadingGrade.value = false;
    }
  })
})
watch(pagination, (newVal, oldVal) => {
  if (newVal.page !== oldVal.page || newVal.rowsPerPage !== oldVal.rowsPerPage) {
    onRefresh();
  }
})
const isLoadingChart = ref(false);

async function onRefresh() {
  isLoadingGrade.value = true;
  console.log('refresh11')
  if (identity.value == 3) {
    // TODO 增加分页
    api
      .get(
        `/stu/GradeBook/${project_id.value}/${pagination.value.page - 1}/${
          pagination.value.rowsPerPage == 0 ? 9999 : pagination.value.rowsPerPage
        }`
      )
      .then((res) => {
        data.value = res.data.body;
        console.log('dddddd', data.value)
        isLoadingGrade.value = false;
      }).catch((err) => {
      console.log('err', err)
    });
  } else if (identity.value === 1) {
    api
      .get(
        `/tea/allGradeBook/${project_id.value}/${pagination.value.page - 1}/${
          pagination.value.rowsPerPage == 0 ? 9999 : pagination.value.rowsPerPage
        }`
      )
      .then((res) => {
        let saveData = res.data.body.map(item => item.value)
          .reduce((acc, val) => acc.concat(val), []);

        data.value = saveData;
        console.log('savedata',data.value)
        isLoadingGrade.value = false;
      })
      .catch((err) => {
        console.log("err", err);
      });
  }
}

// 保存
async function save(grade: gradeProps) {
  await nextTick()
  api.post('/tea/grade_ass', {
    grade: grade.grade,
    assignmentId: grade.assignmentId,
    submitterId: grade.submitterId,
    comment: grade.comment,
    review: review.value
  }).then((res) => {
    $q.notify({
      position: 'top',
      message: 'save success',
      color: 'positive'
    })
    onRefresh();
  }).catch(err => {
    console.log(err)
  })
}

const model = ref(null), isShowDialog = ref(false);
const excel_file = ref();
// const router = useRouter();
function saveUploadAvatar() {
  isShowDialog.value = false;
  if (model.value) {
    excel_file.value = model.value;
    let formdata = new FormData();
    formdata.append('file', excel_file.value);
    formdata.append('assignmentId', assignmentID.value);
    api.post('/tea/grade_ass_with_file',formdata
    ).then((res) => {
      console.log('res',res.data)
      data.value = res.data.body;
      model.value = null;
      router.go(0);

    }).catch((err) => {
      console.log(err)
    })
  }
}


function showDialog() {
  const userInput = prompt('Please enter the assignmentId:');
  if (userInput !== null) {
    // 用户点击了确定按钮并输入了内容
    console.log('User input:', userInput);
    assignmentID.value = parseInt(userInput);
    isShowDialog.value = true;
  } else {
    console.log('User canceled input.');
  }

  console.log('isShowDialog', isShowDialog.value)
}

function cancelUploadAvatar() {
  isShowDialog.value = false;
  model.value = null;
}


</script>
