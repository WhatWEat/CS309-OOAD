<template>
  <q-card>

    <q-card-section>

    </q-card-section>
  </q-card>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {api} from "boot/axios";
import {gradeProps} from "src/composables/comInterface";
import {useProjectId} from "src/composables/usefulFunction";

const charTable = ref<gradeProps[]>([]);
const project_id = ref(0);
onMounted(() => {
  project_id.value = useProjectId();
})
function onFresh(){
  api
  .get(
    `/tea/allGradeBook/${project_id.value}/0/9999}`
  )
  .then((res) => {

    let saveData = res.data.body.map(item => item.value)
    .reduce((acc, val) => acc.concat(val), []);

    data.value = saveData;

    isLoadingGrade.value = false;
  })
  .catch((err) => {
    console.log("err", err);
  });
}
</script>

<style scoped>

</style>
