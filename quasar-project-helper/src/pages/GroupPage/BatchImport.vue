<!--该页面用于教师的批量导入数据，本质是一个文件提交界面，带有文件检验功能-->
<template>
<!--  该处应该为欢迎界面，需要进一步进行美化处理等内容-->
  <div>
    Welcome Teacher {{teacherId}}
    <p>You need to upload projects info here</p>
  </div>

  <div id="q-app" style="min-height: 100vh;">
    <div class="q-pa-md">
      <div class="q-gutter-md row items-start">
        <q-uploader
          accept=".xlsx"
          label="Restricted to xlsx file"
          style="max-width: 600px"
          url="http://localhost:4444/upload"
          @rejected="onRejected"
          max-files="1"
          auto-upload="false"
          hide-upload-button="false"
          headers= {{headers}}
        ></q-uploader>

      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "BatchImport",
  data() {
    return {
      teacherId: this.$route.params.teacherId,
      headers:[{name:'groupInfoUpload', value: 'teacherId'}]
    }
  }
}
</script>

<style scoped>

</style>
