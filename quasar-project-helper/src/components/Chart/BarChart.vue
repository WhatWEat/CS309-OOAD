<template>
  <div>
    <q-card class="no-shadow" bordered>
      <q-card-section class="text-h6">
        Bar Chart
        <q-btn icon="fa fa-download" class="float-right" @click="SaveImage" flat dense>
          <q-tooltip>Download PNG</q-tooltip>
        </q-btn>
      </q-card-section>
      <q-card-section>
        <ECharts ref="barchart" :option="options"
                 class="q-mt-md"
                 :resizable="true"
                 autoresize style="height: 300px;"
        />
      </q-card-section>
    </q-card>
  </div>
</template>


<script>
import {defineComponent, onMounted, ref} from 'vue';
import ECharts from 'vue-echarts';
import "echarts";
import {api} from "boot/axios"
import {useRouter} from "vue-router";

export default defineComponent({
  name: "BarChart",
  components: {
    ECharts,
  },
  setup() {
    const data = ref([]);
    const options = ref({});
    const router = useRouter()
    const project_id = ref(router.currentRoute.value.params.projectID)

    onMounted(() => {
      api
        .get(`/tea/allGradeBook/${project_id.value}`)
        .then((res) => {
          let hashmap = res.data.body;
          let saveData = [];
          for (const [studentID, grades] of Object.entries(hashmap)) {
            saveData = saveData.concat(grades);
          }
          data.value = saveData;
          processData(); // 调用处理数据的函数，来生成图表设置
        })
        .catch((err) => {
          console.log("err", err);
        });
    });

    function processData() {
      // 这里根据具体数据结构来做调整
      const scoreSegments = ['0-50', '51-70', '71-80', '81-90', '91-100'];
      let assignmentScores = {}; // 存储每项作业对应分数段的学生人数

      // 假设 data.value 是一个包含所有分数的数组
      data.value.forEach(grades => {
        const {assignmentId,grade} = grades; // 假设每个 grade 都有assignment和score字段
        if (!assignmentScores[assignmentId]) {
          assignmentScores[assignmentId] = scoreSegments.map(() => 0);
        }
        const segmentIndex = scoreSegments.findIndex(segment => {
          const [min, max] = segment.split('-').map(Number);
          return grade >= min && grade <= max;
        });
        if (segmentIndex !== -1) {
          assignmentScores[assignmentId][segmentIndex]++;
        }
      });

      // 准备 datasetSource
      const datasetSource = [
        ['assignment', ...scoreSegments],
        ...Object.keys(assignmentScores).map(assignmentId => [
          assignmentId,
          ...assignmentScores[assignmentId]
        ])
      ];

      // 更新 options
      options.value = {
        legend: {bottom: 10},
        tooltip: {},
        dataset: {
          source: datasetSource,
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '20%',
          top: '5%',
          containLabel: true
        },
        xAxis: {type: 'category'},
        yAxis: {},
        series: scoreSegments.map(() => ({type: 'bar'})),
      };
    }

    // 确保返回响应式引用
    return {
      options,
    };
  },
  methods: {
    // 方法维持不变
    SaveImage() {
      const linkSource = this.$refs.barchart.getDataURL();
      const downloadLink = document.createElement('a');
      document.body.appendChild(downloadLink);
      downloadLink.href = linkSource;
      downloadLink.target = '_self';
      downloadLink.download = 'BarChart.png';
      downloadLink.click();
    },
  },
});
</script>


<style scoped>
</style>


