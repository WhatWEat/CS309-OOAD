<template>
  <div>
    <q-card class="no-shadow" flat>
      <q-card-section class="text-h6">
        Linear Chart
        <q-btn icon="fa fa-download" class="float-right" @click="downloadChart(myChart)" flat dense>
          <q-tooltip>Download PNG</q-tooltip>
        </q-btn>
      </q-card-section>
      <div ref="chart" style="width: 500px; height: 400px;"></div>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts';
import type {EChartsOption} from 'echarts';
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
  user_map: Map<number, string>,
  assignment_map: Map<number, string>,
});

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
onBeforeUnmount(() => {
  if (myChart) {
    myChart.dispose();
  }
})
const dataDict = {
  name: '',
  type: 'line',
  stack: 'Total',
  data: []
};
function setOption(){
  // 第一维度是学号 第二位是作业号， 第三维是成绩
  let userMap = new Map<string, Map<number, number>>();
  let assignmentSet = new Set<number>(), userSet = new Set<string>();
  for (let item of props.tableData!){
    if ((props.all_student || props.student_set!.has(Number(item.submitterId))) &&
      props.all_assignment || props.assignment_set!.has(Number(item.assignmentId))){
      userMap.set(item.submitterId, new Map());
      assignmentSet.add(item.assignmentId);
      userSet.add(item.submitterId);
      userMap.get(item.submitterId)!.set(item.assignmentId, item.grade);
    }
  }
  for (let [key, value] of userMap){
    for(let assignment of assignmentSet){
      if (!value.has(assignment)){
        value.set(assignment, 0);
      }
    }
  }
  let assignmentList = Array.from(assignmentSet).map((item) => Number(item));
  assignmentList.sort((a, b) => a - b);
  option.value.legend.data = Array.from(userSet).map((item) => item.toString());
  option.value.xAxis.data = assignmentList.map((item) => truncate(item.toString()+props.assignment_map?.get(Number(item)),7));
  option.value.series = [];
  for(const user of userSet.keys()){
    let temp = JSON.parse(JSON.stringify(dataDict));
    temp.name = user.toString();
    for (const assignment of assignmentList){
      temp.data.push(userMap.get(user)!.get(assignment));
    }
    option.value.series.push(temp);
  }
}
const option = ref({
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['']
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [

  ]
});
</script>

<style scoped>
</style>
