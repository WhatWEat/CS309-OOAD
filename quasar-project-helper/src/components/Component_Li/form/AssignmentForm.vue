<template>
  <el-form :model="form" label-position="right" label-width="70px">
    <el-form-item label="Title">
      <el-input v-model="form_temp.title"/>
    </el-form-item>
    <el-form-item label="Full Mark">
      <el-input v-model="form_temp.fullMark"/>
    </el-form-item>
    <el-form-item label="Type">
      <el-input v-model="form_temp.type"/>
    </el-form-item>

    <el-form-item label="Extension">
      <el-select v-model="form_temp.requireExtension" placeholder="please select require extension">
        <el-option label="PDF" value=".pdf"/>
        <el-option label="Markdown" value=".md"/>
      </el-select>
    </el-form-item>
    <el-form-item label="Deadline">
      <el-col :span="11">
        <el-date-picker
          v-model="form_temp.deadline.data"
          placeholder="Pick a date"
          style="width: 100%"
          type="date"
        />
      </el-col>
      <el-col :span="2" class="text-center">
        <span class="text-gray-500">-</span>
      </el-col>
      <el-col :span="11">
        <el-time-picker
          v-model="form_temp.deadline.time"
          placeholder="Pick a time"
          style="width: 100%"
        />
      </el-col>
    </el-form-item>
<!--    <el-form-item label="Instant delivery">-->
<!--      <el-switch v-model="form.delivery"/>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="Activity type">-->
<!--      <el-checkbox-group v-model="form.type">-->
<!--        <el-checkbox label="Online activities" name="type"/>-->
<!--        <el-checkbox label="Promotion activities" name="type"/>-->
<!--        <el-checkbox label="Offline activities" name="type"/>-->
<!--        <el-checkbox label="Simple brand exposure" name="type"/>-->
<!--      </el-checkbox-group>-->
<!--    </el-form-item>-->
    <el-form-item label="Resources">
      <el-upload
        v-model:file-list="fileList"
        class="upload-demo"
        action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
        :on-change="handleChange"
      >
        <el-button type="primary">Click to upload</el-button>
        <template #tip>
          <div class="el-upload__tip">
            jpg/png files with a size less than 500kb
          </div>
        </template>
      </el-upload>
    </el-form-item>
    <el-form-item label="Description">
      <el-input v-model="form_temp.desc" type="textarea"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">Create</el-button>
      <el-button>Cancel</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {ref} from 'vue'


export default {
  data() {
    return {
      form_temp: {
        title: this.form.title,
        fullMark: this.form.fullMark,
        type: this.form.type,
        requireExtension: this.form.requireExtension,
        deadline: this.form.deadline,
        resource: this.form.resource,
        desc: this.form.desc,
        fileList: this.form.fileList,
      },
    }
  },
  methods: {
    onSubmit() {
      console.log('submit!')
    },
    handleChange(file, fileList) {
      console.log(file, fileList)
    },
  },
  props: {
    form: {
      required: false,
      type: Object,
      default: () => {
        return {
          title: '',
          fullMark: '',
          type: '',
          requireExtension: '',
          deadline: {
            data: '',
            time: '',
          },
          resource: '',
          desc: '',
          fileList: [],
        }
      },
    },
    isCreate: {
      type: Boolean,
      default: true,
    },
  },
}
</script>
