<template>
  <el-form :model="form" label-position="right" label-width="70px" >
    <el-form-item label="Title">
      <el-input v-model="form_temp.title" />
    </el-form-item>
    <el-form-item label="Full Mark">
      <el-input v-model="form_temp.fullMark"/>
    </el-form-item>
    <el-form-item label="Type">
      <el-select v-model="form_temp.type" placeholder="please select require type">
        <el-option label="Personal" value="i"/>
        <el-option label="Group" value="g"/>
        <el-option label="Peer Evaluation" value="e"/>
      </el-select>
    </el-form-item>

    <el-form-item label="Extension">
      <el-select v-model="form_temp.requireExtension" placeholder="please select require extension">
        <el-option label="PDF" value=".pdf"/>
        <el-option label="Markdown" value=".md"/>
        <el-option label="TXT" value=".txt"/>
        <el-option label="ZIP" value=".zip"/>
        <el-option label="PPTX" value=".pptx"/>
        <el-option label="RAR" value=".rar"/>
        <el-option label="DOCX" value=".docx"/>
      </el-select>
    </el-form-item>
    <el-form-item label="Deadline">
      <el-col :span="11">
        <el-date-picker
          v-model="form_temp.deadline.data"
          placeholder="Pick a date"
          style="width: 100%"
          type="date"
          value-format="YYYY-MM-DD"
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
          value-format="HH:mm:ss"
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
        v-model:file-list="this.form_temp.fileList"
        :auto-upload="false"
        class="upload-demo"
      >
        <el-button type="primary">Click to upload</el-button>
        <template #tip>
          <div class="el-upload__tip">
            Select file you want to upload
          </div>
        </template>
      </el-upload>
    </el-form-item>

    <el-form-item label="Description">
      <el-input v-model="form_temp.desc" type="textarea"/>
    </el-form-item>

    <el-form-item v-if="isCreate">
      <el-button type="primary" @click="postCreateAssignment">Create</el-button>
      <el-button @click="this.$emit('unfold')">Cancel</el-button>
    </el-form-item>
    <el-form-item v-else>
      <el-button type="primary" @click="postEditAssignment">Edit</el-button>
      <el-button>Cancel</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {api} from "boot/axios";
import cloneDeep from "lodash/cloneDeep";
import {ref} from "vue";

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
      formDate: new FormData(),
      projectId_temp: this.projectId,
      groupId_temp: this.groupId
    }
  },
  methods: {
    onSubmit() {
      console.log('submit!')
    },
    handleChange(file, fileList) {
      console.log(file, fileList)
    },

    //**************************POST**************************
    postCreateAssignment() {
      let formDate = new FormData();

      formDate.append('title', this.form_temp.title);
      formDate.append('description', this.form_temp.desc);
      formDate.append('projectId', this.projectId_temp);
      formDate.append('fullMark', this.form_temp.fullMark);
      formDate.append('type', this.form_temp.type.substring(0, 1));
      formDate.append('deadline', this.form_temp.deadline.data + 'T' + this.form_temp.deadline.time);
      formDate.append('requireExtension', this.form_temp.requireExtension);

      // for (let i = 0; i < this.form_temp.fileList.length; i++) {
      //   this.formDate.append('file', this.form_temp.fileList[i].raw);
      // }
      for (let i = 0; i < this.form_temp.fileList.length; i++) {
        formDate.append('files', this.form_temp.fileList[i].raw)
        // console.log("看看进来没有,文件如下:");
        // console.log(this.fileList[i].raw.type);
        // console.log(this.fileList[i].raw);
        // console.log(this.fileList[i].raw);
        // // const upToUploadFile = ref<File>(this.fileList[i].raw);
        // // console.log("files,s", upToUploadFile.value)
        // // console.log(upToUploadFile.value)
      }
      // console.clear()


      api.post('/tea/post_assignment', formDate,{
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then((res) => {
        if (res.data.statusCode === 200) {
          this.$message({
            message: 'Create Assignment Success',
            type: 'success'
          });
          this.$emit('unfold');
          this.$emit('updateAssList');
        } else {
          console.log("ERR 在创建作业时");
          console.log(res);
          this.$message({
            message: res.data.msg,
            type: 'error'
          });
        }
      }).catch((err) => {
        console.log("ERR 在创建作业时");
        console.log(err);
        this.$message({
          message: err.message,
          type: 'error'
        });
      })
    }
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
    projectId: {
      required: true,
      type: Number,
      default: -1,
    },
    groupId: {
      required: true,
      type: Number,
      default: -1,
    },
  },
  emits: ['unfold','updateAssList'],
  watch: {
    projectId: {
      handler: function (newVal, oldVal) {
        this.projectId_temp = cloneDeep(newVal);
      },
      deep: true
    },
    groupId: {
      handler: function (newVal, oldVal) {
        this.groupId_temp = cloneDeep(newVal);
      },
      deep: true
    },

  }
}
</script>
