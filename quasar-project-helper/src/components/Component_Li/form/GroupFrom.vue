<template>
  <el-form ref="ruleFormRef" :model="formData" :rules="formRules" class="demo-ruleForm" >
    <el-form-item label="GroupId" prop="groupId">
      <el-input v-model="formData.groupId"></el-input>
    </el-form-item>
    <el-form-item label="Deadline" required >
      <el-col :span="11">
        <el-form-item prop="date1_deadline">
          <el-date-picker v-model="formData.date1_deadline" placeholder="选择日期" style="width: 100%;"
                          type="date"></el-date-picker>
        </el-form-item>
      </el-col>
      <pre>-</pre>
      <el-col :span="11">
        <el-form-item prop="date2_deadline">
          <el-time-picker v-model="formData.date2_deadline" placeholder="选择时间" style="width: 100%;"></el-time-picker>
        </el-form-item>
      </el-col>
    </el-form-item>
    <el-form-item label="Presentation" required>
      <el-col :span="11">
        <el-form-item prop="data1_presentation">
          <el-date-picker v-model="formData.data1_presentation" placeholder="选择日期" style="width: 100%;"
                          type="date"></el-date-picker>
        </el-form-item>
      </el-col>
      <pre>-</pre>
      <el-col :span="11">
        <el-form-item prop="data2_presentation">
          <el-time-picker v-model="formData.data2_presentetion" placeholder="选择时间" style="width: 100%;"></el-time-picker>
        </el-form-item>
      </el-col>
    </el-form-item>
    <el-form-item label="Instructor" prop="instructor">
      <el-input v-model="formData.instructor"></el-input>
    </el-form-item>
    <el-form-item label="Leader" prop="leader">
      <el-input v-model="formData.leader"></el-input>
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
      <el-button type="primary" @click="submitForm('ruleFormRef')">立即创建</el-button>
      <el-button @click="resetForm('ruleFormRef')">重置</el-button>
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
        date1_deadline: '',
        date2_deadline: '',
        data1_presentation:'',
        data2_presentetion:'',
        instructor:'',
        leader:'',
        members: [],
        desc: '',
      },
      formRules: {
        groupId:[
          {required:true, message:"请输入小组ID",trigger:'blur'},
          // 要求输入是数字
          {type:'integer',message:"请输入数字",trigger: 'blur'}
        ],
        date1_deadline: [
          {type: 'date', required: true, message: '请选择日期', trigger: 'blur'}
        ],
        date2_deadline: [
          {type: 'date', required: true, message: '请选择时间', trigger: 'blur'}
        ],
        data1_presentation: [
          {type: 'date', required: true, message: '请选择日期', trigger: 'blur'}
        ],
        data2_presentation: [
          {type: 'date', required: true, message: '请选择时间', trigger: 'blur'}
        ],
        instructor: [
          {
            required: true,
            message: '请输入指导老师姓名',
            trigger: 'blur',
          },
        ],
        leader: [
          {
            required: true,
            message: '请输入小组长姓名',
            trigger: 'blur',
          },
        ],
        desc: [
          {required: false, message: '请填写更多信息', trigger: 'blur'}
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
  },
  props: {
  },
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
