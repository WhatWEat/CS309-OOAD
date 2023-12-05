<template>
  <div>
    <q-card class="no-shadow" bordered>
      <q-card-section class="text-h6">
        Chart
      </q-card-section>
      <q-card-section>
        <ECharts :option="options" class="q-mt-md" :resizable="true" autoresize style="height: 285px;" />
      </q-card-section>
    </q-card>
  </div>
</template>

<script >
import { defineComponent, ref, onMounted } from 'vue';
import ECharts from 'vue-echarts';
import axios from 'axios';
import {useRouter} from "vue-router";

export default defineComponent({
  name: 'PieChart',
  components: {
    ECharts
  },
  props:{
    gradesAll:{
      type: [],
      require: true
    }
  },
  setup() {
    const options = ref({
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        top: '10%',
        right: '5%'
      },
      series: [
        {
          name: 'Assignment',
          type: 'pie',
          radius: '50%',
          center: ['50%', '50%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: true,
            formatter: '{b}: {c} ({d}%)'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '14',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: true
          },
          data: []  // 空数组，等待数据填充
        }
      ]
    });

    const router = useRouter()
    const project_id = ref(router.currentRoute.value.params.projectID)

    const data = ref([]); // 存储接收的数据

    const fetchData = () => {
      data.value = props.gradesAll;
      console.log(data.value,'data');
      // axios
      //   .get(`/tea/allGradeBook/${project_id.value}`)
      //   .then((res) => {
      //     let hashmap = res.data.body;
      //     let saveData = [];
      //     for (const [studentID, grades] of Object.entries(hashmap)) {
      //       saveData = saveData.concat(grades);
      //     }
      //     data.value = saveData;
      //     processData(); // 调用处理数据的函数，来生成图表设置
      //   })
      //   .catch((err) => {
      //     console.log("Error fetching data:", err);
      //   });
    };

    const processData = () => {
      const assignmentCounts = {};
      data.value.forEach(grade => {
        if (assignmentCounts[grade.assignmentId]) {
          assignmentCounts[grade.assignmentId]++;
        } else {
          assignmentCounts[grade.assignmentId] = 1;
        }
      });

      // 格式化数据为图表所需格式
      const chartData = Object.keys(assignmentCounts).map(assignmentId => ({
        name: assignmentId,
        value: assignmentCounts[assignmentId]
      }));

      updateChartData(chartData);
    };

    const updateChartData = (chartData) => {
      options.value.series[0].data = chartData;
    };

    onMounted(fetchData);  // 在组件加载后获取数据

    return {
      options
    };
  }
});

</script>

<style scoped>
</style>
