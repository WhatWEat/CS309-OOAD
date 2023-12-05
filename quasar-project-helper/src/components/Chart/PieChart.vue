<template>
  <div>
    <q-card class="no-shadow" flat>
      <q-card-section class="text-h6">
        Pie Chart
        <q-btn icon="fa fa-download" class="float-right" @click="downloadChart(myChart)" flat dense>
          <q-tooltip>Download PNG</q-tooltip>
        </q-btn>
      </q-card-section>
      <q-card-section class="row">
        <q-input v-model="A" class="col-2" outlined dense label="A"></q-input>
        <q-space class="col-2"/>
        <q-input v-model="B" class="col-2" outlined color="green" label="B" dense></q-input>
      </q-card-section>
      <div ref="chart" style="width: 500px; height: 400px;" class="col-12"></div>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts';
import "echarts";
import {onBeforeUnmount, onMounted, ref, watch} from "vue";
import {gradeProps} from "src/composables/comInterface";
import {downloadChart, truncate} from "src/composables/usefulFunction";

const chart = ref<HTMLElement>();
let myChart: echarts.ECharts | null = null;
const props = defineProps({
  tableData: Array<gradeProps>,
  assignment_set: Set<number>,
  all_assignment: Boolean,
  student_set: Set<number>,
  all_student: Boolean,
  assignment_map: Map<number, string>,
});
const A = ref(90), B = ref(80);
onMounted(() => {
  if (chart.value) {
    myChart = echarts.init(chart.value);
    setOption();
    myChart.setOption(option.value);
  }
})
watch(props, (newVal, oldVal) => {
  if (myChart) {
    setOption();
    myChart.setOption(option.value);
  }
})
watch([A, B], (newVal, oldVal) => {
  if (myChart) {
    setOption();
    myChart.setOption(option.value);
  }
})
onBeforeUnmount(() => {
  if (myChart) {
    myChart.dispose();
  }
})
function setOption(){
  // 第一个参数为ass, 后面分别为[A,B,C]
  let gradeMap = new Map<number, Array<number>>();
  for(let i of props.tableData!){
    if((props.all_student || props.student_set!.has(Number(i.submitterId))) &&
      (props.all_assignment || props.assignment_set!.has(Number(i.assignmentId)))){
        if (gradeMap.has(i.assignmentId)){
          let abc_list = gradeMap.get(i.assignmentId);
          abc_list![0] += i.grade >= A.value ? 1 : 0;
          abc_list![1] += i.grade < A.value && i.grade >= B.value ? 1 : 0;
          abc_list![2] += i.grade < B.value ? 1 : 0;
          gradeMap.set(i.assignmentId, abc_list!);
        } else {
          gradeMap.set(i.assignmentId,[i.grade >= A.value ? 1 : 0,
            i.grade < A.value && i.grade >= B.value ? 1 : 0, i.grade < B.value ? 1 : 0]);
        }
    }
  }
  option.value.radiusAxis.data = Array.from(gradeMap.keys()).map(key => truncate(key.toString(),7));
  // option.value.radiusAxis.data = Array.from(gradeMap.keys()).map(key => truncate(key.toString(),3));
  option.value.series[0].data = Array.from(gradeMap.values()).map(value => value[0]);
  option.value.series[1].data = Array.from(gradeMap.values()).map(value => value[1]);
  option.value.series[2].data = Array.from(gradeMap.values()).map(value => value[2]);
}
const option = ref({
  angleAxis: {},
  radiusAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu'],
    z: 10,
  },
  polar: {},
  series: [
    {
      type: 'bar',
      data: [1, 2, 3, 4],
      coordinateSystem: 'polar',
      name: 'A',
      stack: 'a',
      emphasis: {
        focus: 'series'
      }
    },
    {
      type: 'bar',
      data: [2, 4, 6, 8],
      coordinateSystem: 'polar',
      name: 'B',
      stack: 'a',
      emphasis: {
        focus: 'series'
      },
    },
    {
      type: 'bar',
      data: [1, 2, 3, 4],
      coordinateSystem: 'polar',
      name: 'C',
      stack: 'a',
      emphasis: {
        focus: 'series'
      }
    }
  ],
  legend: {
    show: true,
    data: ['A', 'B', 'C']
  }
});
</script>
