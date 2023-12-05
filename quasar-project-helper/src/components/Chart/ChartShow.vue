<template>
  <q-card flat bordered>
    <q-card-section horizontal class="row">
      <q-card-section class="q-pt-xs" style="width: 500px">
        <div class="text-overline">Assignment Statistics</div>
        <q-separator/>
        <span v-if="!assignmentAll">
          <q-chip removable v-for="(i,index) in assignmentConditional" :key="i" square @remove="assignmentConditional.splice(index,1)">
            <q-avatar color="green" class="col-auto">{{ i }}</q-avatar>
            做作业
          </q-chip>
        </span>
        <q-chip removable square v-else @remove="assignmentAll=false"> All Assignment</q-chip>
        <q-separator/>
        <span v-if="!userAll">
          <q-chip removable v-for="(i, index) in userConditional" :key="i" square @remove="userConditional.splice(index, 1)">
            <q-avatar color="red" class="col-auto">{{ i }}</q-avatar>
            tyl
          </q-chip>
        </span>
        <q-chip removable square v-else @remove="userAll=false"> All Student</q-chip>
      </q-card-section>
      <q-separator vertical/>
      <q-card-section>
        <q-item-section side>
          <q-btn
            avatar
            flat
            color="primary"
            @click="editAddConditional"
            size="sm"
            round
          >
            <q-avatar size="20px" icon="edit"></q-avatar>
          </q-btn>
          <q-btn
            avatar
            flat
            color="primary"
            @click="clearAllConditional"
            size="sm"
            round
          >
            <q-avatar size="20px" icon="delete_outline"></q-avatar>
          </q-btn>
        </q-item-section>
        <q-card-section side></q-card-section>
      </q-card-section>
    </q-card-section>
    <q-separator/>
    <q-card-section>
      <BarChart :table-data="charTable"
                :student_set="new Set(userConditional)"
                :assignment_set="new Set(assignmentConditional)"
                :all_assignment="assignmentAll"
                :all_student="userAll"></BarChart>
    </q-card-section>
  </q-card>
  <q-dialog v-model="isOpenEditConditional" persistent>
    <q-card>
      <q-card-section class="row justify-center">
        <q-list style="width: 700px; max-width: 80vw">
          <q-expansion-item
            header-class="text-h6"
            default-opened
            label="Assignment choose"
            group="someGroup"
          >
            <q-toggle
              v-model="assignmentAll"
              color="green"
              label="All assignment"
              keep-color
            >
            </q-toggle>
            <filter-card-list
              :id_list="assignment_list"
              v-if="!assignmentAll"
              @update:selected="assignmentConditional = $event"
            ></filter-card-list>
          </q-expansion-item>
          <q-separator></q-separator>
          <q-expansion-item
            header-class="text-h6"
            label="Person choose"
            group="someGroup"
          >
            <q-toggle
              v-model="userAll"
              color="primary"
              label="All student"
              keep-color
            />
            <filter-card-list
              :id_list="student_id_list"
              v-if="!userAll"
              @update:selected="userConditional = $event"
            >
            </filter-card-list>
          </q-expansion-item>
        </q-list>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn flat label="Cancel" color="primary" v-close-popup/>
        <q-btn
          flat
          label="Save"
          color="positive"
          @click="saveInfo"
          v-close-popup
        />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {api} from "boot/axios";
import {gradeProps, projectProps} from "src/composables/comInterface";
import {useProjectId} from "src/composables/usefulFunction";
import {computed} from "vue-demi";
import FilterCardList from "components/Chart/filterCardList.vue";
import BarChart from "components/Chart/BarChart.vue";
import LineChart from "components/Chart/LineChart.vue";

const charTable = ref<gradeProps[]>([]),
  charStudentIdList = ref<Map<number, gradeProps[]>>(new Map());
const student_id_list = ref<number[]>([]),
  student_list_map = ref<Map<number, string>>(new Map());
const assignment_list = ref<number[]>([]),
  assignment_list_map = ref<Map<number, string>>(new Map());
const project_id = ref(0);
onMounted(() => {
  project_id.value = useProjectId();
  onFresh();
});

function onFresh() {
  api
  .get(`/tea/allGradeBook/${project_id.value}/0/9999`)
  .then((res) => {
    console.log("res", res);
    res.data.body.forEach((item: { key: number; value: gradeProps[] }) => {
      charStudentIdList.value.set(item.key, item.value);
    });
    console.log("charStudentIdList", charStudentIdList.value);
    let assignment_set = new Set<number>();
    for (let [key, value] of charStudentIdList.value) {
      value.forEach((item) => {
        assignment_set.add(item.assignmentId);
      });
    }
    assignment_list.value = Array.from(assignment_set);
    console.log("assignment list", assignment_list);
    student_id_list.value = res.data.body.map(
      (item: { key: any }) => item.key
    );
    let saveData = res.data.body
    .map((item) => item.value)
    .reduce((acc, val) => acc.concat(val), []);
    charTable.value = saveData;
    console.log("charTable", charTable.value);
  })
  .catch((err) => {
    console.log("err", err);
  });
}

// 处理 编辑 和 清空 条件
const assignmentConditional = ref<number[]>([]),
  userConditional = ref<number[]>([]);
const isOpenEditConditional = ref(false);
const assignmentAll = ref(true),
  userAll = ref(true);

function editAddConditional() {
  isOpenEditConditional.value = true;
  console.log("editAddContional");
}

function clearAllConditional() {
  assignmentConditional.value = [];
  userConditional.value = [];
  console.log("clearAllConditional");
}

// const filterProject = ref<projectProps[]>([]);
//
// function filterFn(val: string, update: (arg0: projectProps[]) => void) {
//   if (val === "") {
//     filterProject.value = projectOptions.value;
//     update(filterProject.value);
//     return;
//   }
//   const needle = val.toLowerCase();
//   filterProject.value = projectOptions.value.filter(
//     (v) => v.name.toLowerCase().indexOf(needle) > -1
//   );
//   update(
//     projectOptions.value.filter(
//       (v) => v.name.toLowerCase().indexOf(needle) > -1
//     )
//   );
// }
function saveInfo() {
  console.log("save");
}
</script>

<style scoped></style>
