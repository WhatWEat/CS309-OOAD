<template>
  <q-card bordered flat>
    <q-card-section class="row" horizontal>
      <q-card-section class="q-pt-xs" style="width: 500px">
        <div class="text-overline">Assignment Statistics</div>
        <q-separator />
        <span v-if="!assignmentAll">
          <q-chip v-for="(i,index) in assignmentConditional" :key="i" removable square
                  @remove="assignmentConditional.splice(index,1)" :color="lightColors[Math.floor(Math.random() * lightColors.length)]" class="col-auto">
            {{ i }}
          </q-chip>
        </span>
        <q-chip v-else removable square @remove="assignmentAll=false" :color="lightColors[Math.floor(Math.random() * lightColors.length)]"> All Assignment</q-chip>

        <q-separator />
        <span v-if="!userAll">
          <q-chip v-for="(i, index) in userConditional" :key="i" removable square :color="lightColors[Math.floor(Math.random() * lightColors.length)]"
                  @remove="userConditional.splice(index, 1)">
<!--            <q-avatar class="col-auto" color="red">{{ i }}</q-avatar>-->
            {{ i }}
          </q-chip>
        </span>
        <q-chip v-else removable square :color="lightColors[Math.floor(Math.random() * lightColors.length)]" @remove="userAll=false"> All Student</q-chip>
      </q-card-section>
      <q-separator vertical />
      <q-card-section>
        <q-item-section side>
          <q-btn
            avatar
            color="primary"
            flat
            round
            size="sm"
            @click="editAddConditional"
          >
            <q-avatar icon="edit" size="20px"></q-avatar>
          </q-btn>
          <q-btn
            avatar
            color="primary"
            flat
            round
            size="sm"
            @click="clearAllConditional"
          >
            <q-avatar icon="delete_outline" size="20px"></q-avatar>
          </q-btn>
        </q-item-section>
        <q-card-section side></q-card-section>
      </q-card-section>
    </q-card-section>
    <q-separator />
    <q-card-section>
      <q-tab-panels v-model="tab" animated>
        <q-tab-panel name="barchart">
          <BarChart
            :table-data="charTable"
            :student_set="new Set(userConditional)"
            :assignment_set="new Set(assignmentConditional)"
            :all_assignment="assignmentAll"
            :all_student="userAll"
            :assignment_map="assignment_list_map"
          ></BarChart>
        </q-tab-panel>

        <q-tab-panel name="linearchar">
          <LineChart
            :table-data="charTable"
            :student_set="new Set(userConditional)"
            :assignment_set="new Set(assignmentConditional)"
            :all_assignment="assignmentAll"
            :all_student="userAll"
            :user_map="student_list_map"
            :assignment_map="assignment_list_map"
          ></LineChart>
        </q-tab-panel>

        <q-tab-panel name="piechart">
          <PieChart
            :table-data="charTable"
            :student_set="new Set(userConditional)"
            :assignment_set="new Set(assignmentConditional)"
            :all_assignment="assignmentAll"
            :all_student="userAll"
            :assignment_map="assignment_list_map"
          ></PieChart>
        </q-tab-panel>
      </q-tab-panels>

      <q-separator />

      <q-tabs v-model="tab" dense align="justify" narrow-indicator>
        <q-tab name="barchart" label="Bar Chart" />
        <q-tab name="linearchar" label="Linear Chart" />
        <q-tab name="piechart" label="Pie Chart" />
      </q-tabs>
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
        <!--        <q-btn flat label="Cancel" color="primary" v-close-popup/>-->
        <q-btn
          v-close-popup
          color="positive"
          flat
          label="Done"
          @click="saveInfo"
        />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { api } from "boot/axios";
import { gradeProps, projectProps } from "src/composables/comInterface";
import { useProjectId } from "src/composables/usefulFunction";
import { computed } from "vue-demi";
import FilterCardList from "components/Chart/filterCardList.vue";
import BarChart from "components/Chart/BarChart.vue";
import LineChart from "components/Chart/LineChart.vue";
import PieChart from "components/Chart/PieChart.vue";

const tab = ref("barchart");
const charTable = ref<gradeProps[]>([]),
  charStudentIdList = ref<Map<number, gradeProps[]>>(new Map());
const student_id_list = ref<number[]>([]),
  student_list_map = ref<Map<number, string>>(new Map());
const assignment_list = ref<number[]>([]),
  assignment_list_map = ref<Map<number, string>>(new Map());
const project_id = ref(0);
const lightColors = [
  'primary',
  'positive',
  'info',
  'warning',
  'pink-3',
  'red-3',
  'blue-5',
  'orange-3',
  'purple-3',
  'brown-3',
  'green-3',
  'grey-3',
  'deep-purple-3',
  'light-blue-4'
];
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
          assignment_list_map.value.set(item.assignmentId, item.title!);
          student_list_map.value.set(Number(item.submitterId), item.submitterName!);
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
