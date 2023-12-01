<template>
  <el-form ref="ruleFormRef" :model="formData_temp" :rules="formRules" class="demo-ruleForm">
    <!--    <el-form-item label="GroupId" prop="groupId">-->
    <!--      <el-input v-model="formData_temp.groupId"></el-input>-->
    <!--    </el-form-item>-->
    <el-form-item label="GroupMaxSize" prop="maxSize">
      <el-input v-model.number="formData_temp.maxSize"></el-input>
    </el-form-item>
    <el-form-item label="GroupName" prop="groupName">
      <el-input v-model="formData_temp.groupName"></el-input>
    </el-form-item>
    <el-form-item label="Deadline" required>
      <el-col :span="11">
        <el-form-item prop="date1_deadline">
          <el-date-picker v-model="formData_temp.date1_deadline" format="YYYY-MM-DD" placeholder="选择日期"
                          style="width: 100%;" type="date"
                          value-format="YYYY-MM-DD"></el-date-picker>
        </el-form-item>
      </el-col>
      <pre>-</pre>
      <el-col :span="11">
        <el-form-item prop="date2_deadline">
          <el-time-picker v-model="formData_temp.date2_deadline" format="HH:mm:ss" placeholder="选择时间"
                          style="width: 100%;"
                          value-format="HH:mm:ss"></el-time-picker>
        </el-form-item>
      </el-col>
    </el-form-item>
    <el-form-item label="Presentation" required>
      <el-col :span="11">
        <el-form-item prop="data1_presentation">
          <el-date-picker v-model="formData_temp.data1_presentation" format="YYYY-MM-DD" placeholder="选择日期"
                          style="width: 100%;" type="date"
                          value-format="YYYY-MM-DD"></el-date-picker>
        </el-form-item>
      </el-col>
      <pre>-</pre>
      <el-col :span="11">
        <el-form-item prop="data2_presentation">
          <el-time-picker v-model="formData_temp.data2_presentation" format="HH:mm:ss" placeholder="选择时间"
                          style="width: 100%;"
                          value-format="HH:mm:ss"></el-time-picker>
        </el-form-item>
      </el-col>
    </el-form-item>
    <el-form-item label="Instructor" prop="instructor">
      <el-input v-model.number="formData_temp.instructor"></el-input>
    </el-form-item>
    <el-form-item label="Leader" prop="leader">
      <el-input v-model.number="formData_temp.leader"></el-input>
    </el-form-item>
    <el-form-item label="Members" prop="members">
      <el-tag
        v-for="member in formData_temp.members"
        :key="member"
        :disable-transitions="false"
        class="mx-1"
        closable
        size="large"
        @close="handleClose_members(member)"
      >
        {{ member }}
      </el-tag>
      <el-input
        v-if="inputVisible_members"
        ref="InputRef"
        v-model.number="inputValue_members"
        class="input-new-tag"
        size="small"
        @blur="handleInputConfirm_members"
        @keyup.enter="handleInputConfirm_members"
      />
      <el-button v-else class="button-new-tag ml-1" @click="showInput_members">
        + New
      </el-button>
    </el-form-item>
    <el-form-item label="Technical Stack" prop="technicalStack">
      <el-tag
        v-for="technical in formData_temp.technicalStack"
        :key="technical"
        :disable-transitions="false"
        class="mx-1"
        closable
        size="large"
        @close="handleClose_technical(technical)"
      >
        {{ technical }}
      </el-tag>
      <el-input
        v-if="inputVisible_technical"
        ref="InputRef"
        v-model="inputValue_technical"
        :disabled="isEdit"
        class="input-new-tag"
        size="small"
        @blur="handleInputConfirm_technical"
        @keyup.enter="handleInputConfirm_technical"
      />
      <el-button v-else class="button-new-tag ml-1" @click="showInput_technical">
        + New
      </el-button>
    </el-form-item>
    <el-form-item label="MoreInfo" prop="desc">
      <el-input v-model="formData_temp.desc" :disabled="isEdit" type="textarea"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button v-show="isCreate" type="primary" @click="submitCreateForm('ruleFormRef')">立即创建</el-button>
      <el-button v-show="isEdit" type="primary" @click="submitEditForm('ruleFormRef')">立即修改</el-button>
      <el-button @click="resetForm('ruleFormRef')">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {defineComponent, ref} from "vue";
import {descriptionProps} from "element-plus";
import {api} from "boot/axios";

export default defineComponent({
  name: "GroupFrom",
  computed: {
    descriptionProps() {
      return descriptionProps
    }
  },
  data() {
    return {
      formData_temp: {
        groupId: this.formData.groupId,
        maxSize: this.formData.maxSize,
        groupName: this.formData.groupName,
        date1_deadline: this.formData.date1_deadline,
        date2_deadline: this.formData.date2_deadline,
        data1_presentation: this.formData.data1_presentation,
        data2_presentation: this.formData.data2_presentation,
        instructor: this.formData.instructor,
        leader: this.formData.leader,
        members: this.formData.members,
        technicalStack: this.formData.technicalStack,
        desc: this.formData.desc
      },
      formRules: {
        groupId: [
          {required: true, message: "请输入小组ID", trigger: 'blur'},
          // 要求输入是数字
          {pattern: /^[0-9]*$/, message: "请输入数字", trigger: 'blur'}
        ],
        maxSize: [
          {required: true, message: "请输入小组最大人数", trigger: 'blur'},
          // 要求输入1-30之间的数字,不要用pattern，因为pattern是正则表达式，不是数字
          {
            validator: (rule, value, callback) => {
              if (value < 1 || value > 30) {
                callback(new Error('请输入1-30之间的数字'));
              } else {
                callback();
              }
            },
            trigger: 'blur'
          }
        ],
        groupName: [
          {required: false, message: "请输入小组名称", trigger: 'blur'},
          //长度不能超过20 使用validator
          {
            validator: (rule, value, callback) => {
              if (value != null && value.length > 20) {
                callback(new Error('长度不能超过20'));
              } else {
                callback();
              }
            },
            trigger: 'blur'
          }
        ],
        date1_deadline: [
          {type: 'string', required: true, message: '请选择日期', trigger: 'blur'}
        ],
        date2_deadline: [
          {type: 'string', required: true, message: '请选择时间', trigger: 'blur'}
        ],
        data1_presentation: [
          {type: 'string', required: true, message: '请选择日期', trigger: 'blur'}
        ],
        data2_presentation: [
          {type: 'string', required: true, message: '请选择时间', trigger: 'blur'}
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
        members: [
          {
            required: true,
            message: '请输入小组成员学号',
            trigger: 'blur',
          },
        ],
        technicalStack: {
          required: false,
          message: '请输入技术栈',
          trigger: 'blur',
        },
        desc: [
          {required: false, message: '请填写更多信息', trigger: 'blur'}
        ]
      },
      inputVisible_members: false,
      inputValue_members: '',
      inputVisible_technical: false,
      inputValue_technical: '',

      isEdit: ref(this.type === 'Edit'),
      isCreate: ref(this.type !== 'Edit'),

      errorMessage: {
        'icon_name': 'error',
        'icon_color': 'red',
        'icon_text_color': 'black',
        'text': ""
      },
      successMessage: {
        'icon_name': 'done',
        'icon_color': 'blue',
        'icon_text_color': 'white',
        'text': ""
      }
    };
  },
  methods: {
    submitCreateForm(formName) {
      this.$refs[formName].validate((valid) => {
        console.log("提交创建小组的表单");
        console.log({
          "projectId":  parseInt(this.projectId),
          "maxsize": this.formData_temp.maxSize,
          "groupName": this.formData_temp.groupName,
          "instructorId": this.formData_temp.instructor,
          "deadline": this.formData_temp.data1_presentation + "T" + this.formData_temp.data2_presentation,
          "reportTime": this.formData_temp.date1_deadline + "T" + this.formData_temp.date2_deadline,
          "description": this.formData_temp.desc,
          "leaderId": this.formData_temp.leader,
          "memberIds": this.formData_temp.members,
          "technicalStack": this.formData_temp.technicalStack,
        });
        if (valid) {
          this.$emit('unfold');
          api.post('/tea/create_group',
            {
              "projectId": this.projectId,
              "maxsize": this.formData_temp.maxSize,
              "groupName": this.formData_temp.groupName,
              "instructorId": this.formData_temp.instructor,
              "deadline": this.formData_temp.data1_presentation + "T" + this.formData_temp.data2_presentation,
              "reportTime": this.formData_temp.date1_deadline + "T" + this.formData_temp.date2_deadline,
              "description": this.formData_temp.desc,
              "leaderId": this.formData_temp.leader,
              "memberIds": this.formData_temp.members,
              "technicalStack": this.formData_temp.technicalStack,
            }
          ).then((res) => {
            this.successMessage.text = res.data.msg;
            this.$emit("successDialog", this.successMessage);
          }).catch((err) => {
            this.errorMessage.text = err.response.data.msg;
            this.$emit("errorDialog", this.errorMessage);
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    submitEditForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$emit('unfold');
          api.post('/tea/modify_group_info',
            {
              "maxsize": this.formData_temp.maxSize,
              "groupName": this.formData_temp.groupName,
              "instructorId": Object.values(this.formData_temp.instructor)[0],
              "leaderId": Object.values(this.formData_temp.leader)[0],
              "groupId": this.formData_temp.groupId,
              "reportTime": this.formData_temp.data1_presentation + "T" + this.formData_temp.data2_presentation,
              "deadline": this.formData_temp.date1_deadline + "T" + this.formData_temp.date2_deadline,
              "memberIds": Object.values(this.formData_temp.members),
            }
          ).then((res) => {
            console.log("提交成功了");
            console.log(res);
            this.successMessage.text = res.data.msg;
            this.$emit("successDialog", this.successMessage);
          }).catch((err) => {
            console.log("提交失败了");
            console.log(err)
            this.errorMessage.text = err.response.data.msg;
            this.$emit("errorDialog", this.errorMessage);
            console.log({
              "maxsize": this.formData_temp.maxSize,
              "groupName": this.formData_temp.groupName,
              "instructorId": Object.values(this.formData_temp.instructor),
              "leaderId": Object.values(this.formData_temp.leader),
              "groupId": this.formData_temp.groupId,
              "reportTime": this.formData_temp.data1_presentation + "T" + this.formData_temp.data2_presentation,
              "deadline": this.formData_temp.date1_deadline + "T" + this.formData_temp.date2_deadline,
              "memberIds": Object.values(this.formData_temp.members),
            })
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },

    showInput_members() {
      this.inputVisible_members = true;
    },
    showInput_technical() {
      this.inputVisible_technical = true;
    },
    handleInputConfirm_members() {
      console.log('handleInputConfirm_members')
      console.log(this.inputValue_members)
      if (this.inputValue_members) {
        this.formData_temp.members[this.inputValue_members] = this.inputValue_members
        console.log('Have pushed' + this.inputValue_members.value + '\n')
        console.log('Now members are: ' + this.formData_temp.members + '\n')
      }
      else {
        console.log('inputValue_members is null')
      }
      this.inputVisible_members = false
      this.inputValue_members = ''
    },
    handleInputConfirm_technical() {
      if (this.inputValue_technical) {
        this.formData_temp.technicalStack.push(this.inputValue_technical)
        console.log('Have pushed' + this.inputValue_technical.value + '\n')
        console.log('Now technicalStack are: ' + this.formData_temp.technicalStack + '\n')
      }
      this.inputVisible_technical = false
      this.inputValue_technical = ''
    },
    handleClose_members(member) {
      this.formData_temp.members.splice(this.formData_temp.members.indexOf(member), 1);
    },
    handleClose_technical(technical) {
      this.formData_temp.technicalStack.splice(this.formData_temp.technicalStack.indexOf(technical), 1);
    },
  },
  props: {
    type: {
      type: String,
      required: true,
      default: 'Edit'
    },
    formData: {
      required: false,
      default: () => {
        return {
          groupId: '',
          maxSize: '',
          groupName: '',
          date1_deadline: '',
          date2_deadline: '',
          data1_presentation: '',
          data2_presentation: '',
          instructor: '',
          leader: '',
          members: [],
          technicalStack: [],
          desc: '',
        }
      }
    },
    projectId: {
      required: true,
      default: () => {
        return ''
      }
    }
  },
  emits: ['successDialog', 'errorDialog', 'unfold'],
  watch: {
    formData: {
      handler: function (val, oldVal) {
        this.formData_temp = val;
      },
      deep: true
    },
    type: {
      handler: function (val, oldVal) {
        this.isEdit = (val === 'Edit');
        this.isCreate = (val !== 'Edit');
      },
      deep: true
    }
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
