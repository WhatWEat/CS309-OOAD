<template>
  <el-form ref="ruleForm" :model="formData" :rules="formRules" class="demo-ruleForm" >
    <el-form-item label="GroupId" prop="name">
      <el-input v-model="formData.groupId"></el-input>
    </el-form-item>
    <el-form-item label="Deadline" required>
      <el-col :span="11">
        <el-form-item prop="date1">
          <el-date-picker v-model="formData.date1" placeholder="选择日期" style="width: 100%;"
                          type="date"></el-date-picker>
        </el-form-item>
      </el-col>
      <pre>-</pre>
      <el-col :span="11">
        <el-form-item prop="date2">
          <el-time-picker v-model="formData.date2" placeholder="选择时间" style="width: 100%;"></el-time-picker>
        </el-form-item>
      </el-col>
    </el-form-item>
    <el-form-item label="Instructor">
      <el-input v-model="formData.instructor"></el-input>
    </el-form-item>
    <el-form-item label="Members" >
      <el-tag
        v-for="member in formData.members"
        :key="member"
        class="mx-1"
        closable
        :disable-transitions="false"
        size="large"
        @close="handleClose(member)"
      >
        {{ member }}
      </el-tag>

      <el-input
        v-if="inputVisible"
        ref="InputRef"
        v-model="inputValue"
        class="input-new-tag"
        size="small"
        @keyup.enter="handleInputConfirm"
        @blur="handleInputConfirm"
      />
      <el-button v-else class="button-new-tag ml-1"  @click="showInput" >
        + New
      </el-button>

    </el-form-item>
    <el-form-item label="MoreInfo" prop="desc">
      <el-input v-model="formData.desc" type="textarea"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
      <el-button @click="resetForm('ruleForm')">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {ref,defineComponent} from "vue";
import {descriptionProps} from "element-plus";

export default defineComponent({
  name: "GroupFrom",
  computed: {
    descriptionProps() {
      return descriptionProps
    }
  },
  data() {
    return {
      formData: {
        groupId: '',
        region: '',
        date1: '',
        date2: '',
        instructor: false,
        type: [],
        resource: '',
        desc: '',
        members: ['标签一', '标签二', '标签三'],
      },
      formRules: {
        name: [
          {required: true, message: '请输入活动名称', trigger: 'blur'},
          {min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur'}
        ],
        region: [
          {required: true, message: '请选择活动区域', trigger: 'change'}
        ],
        date1: [
          {type: 'date', required: true, message: '请选择日期', trigger: 'change'}
        ],
        date2: [
          {type: 'date', required: true, message: '请选择时间', trigger: 'change'}
        ],
        type: [
          {type: 'array', required: true, message: '请至少选择一个活动性质', trigger: 'change'}
        ],
        resource: [
          {required: true, message: '请选择活动资源', trigger: 'change'}
        ],
        desc: [
          {required: true, message: '请填写活动形式', trigger: 'blur'}
        ]
      },
      inputVisible: false,
      inputValue: ''
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!');
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    showInput() {
      this.inputVisible = true;
    },
    handleInputConfirm() {
      if (this.inputValue) {
        this.formData.members.push(this.inputValue)
        console.log('Have pushed' + this.inputValue.value + '\n')
        console.log('Now members are: ' + this.formData.members + '\n')
      }
      this.inputVisible = false
      this.inputValue = ''
    },
    handleClose(member) {
      this.formData.members.splice(this.formData.members.indexOf(member), 1);
    }
  }
});
</script>

<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>
