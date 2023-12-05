<template>
  <div>
    <q-card class="no-shadow" flat>
      <q-card-section class="text-h6">
        Bar Chart
        <q-btn icon="fa fa-download" class="float-right" @click="downloadChart" flat dense>
          <q-tooltip>Download PNG</q-tooltip>
        </q-btn>
      </q-card-section>
      <q-card-section>
        <div ref="chart" style="width: 600px; height: 400px;"></div>

      </q-card-section>
    </q-card>
  </div>
</template>


<script setup lang="ts">
import * as echarts from 'echarts';
import type {EChartsOption} from 'echarts';
import "echarts";
import {onBeforeUnmount, onMounted, ref, watch} from "vue";
import {gradeProps} from "src/composables/comInterface";

const chart = ref<HTMLElement>();
let myChart: echarts.ECharts | null = null;
const props = defineProps({
  tableData: Array<gradeProps>,
  assignment_set: Set<number>,
  all_assignment: Boolean,
  student_set: Set<number>,
  all_student: Boolean,
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

function downloadChart() {
  if (!myChart) return;

  const url = myChart.getDataURL({
    type: 'png',
    pixelRatio: 2,
    backgroundColor: '#fff'
  });

  const link = document.createElement('a');
  link.href = url;
  link.download = 'chart.png';
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

const tableTab = ['score', 'amount', 'assignment'];

function setOption() {
  let source = [], map = new Map();
  props.tableData!.forEach((item) => {
    let temp = [];
    temp.push(item.grade);
    temp.push(1);
    if (props.all_assignment || props.assignment_set!.has(item.assignmentId)){
      if (!map.has(item.assignmentId)) {
        map.set(item.assignmentId, temp);
      } else {
        let current = map.get(item.assignmentId);
        current[0] = (current[0] * current[1]++ + item.grade) / current[1];
        map.set(item.assignmentId, current);
      }
    }
  })
  let min = 100000, max = 0;
  for (let [key, value] of map) {
    let temp = [];
    temp.push(value[0]);
    temp.push(value[1]);
    if (value[0] < min) min = value[0];
    if (value[0] > max) max = value[0];
    temp.push(key);
    source.push(temp);
  }
  source.sort((a, b) => {
    return a[2] - b[2];
  })
  source.unshift(tableTab);
  option.value.dataset.source = source;
  option.value.visualMap.min = Math.min(min - 10, 0)
  option.value.visualMap.max = Math.max(max + 10, 100);
  console.log('soure', source);
}

const option = ref({
  dataset: {
    source: [
      ['score', 'amount', 'assignment'],

    ]
  },
  grid: {containLabel: true},
  xAxis: {
    name: 'amount',
    minInterval: 1,
  },
  yAxis: {
    name: 'assignment',
    type: 'category'},
  visualMap: {
    orient: 'horizontal',
    left: 'center',
    min: 10,
    max: 100,
    text: ['High Score', 'Low Score'],
    // Map the score column to color
    dimension: 0,
    inRange: {
      color: ['#65B581', '#FFCE34', '#FD665F']
    }
  },
  series: [
    {
      type: 'bar',
      encode: {
        // Map the "amount" column to X axis.
        x: 'amount',
        // Map the "product" column to Y axis
        y: 'assignment'
      }
    }
  ]
});

</script>


<style scoped>
</style>


