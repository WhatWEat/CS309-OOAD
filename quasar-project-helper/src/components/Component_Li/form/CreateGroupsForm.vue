<template>
  <div style="margin: 0px"/>
  <el-form
    ref='FormRef'
    :label-position="labelPosition"
    :model="FormData"
    :rules="rules"
  >
    <el-form-item label="Set Group Size" prop='groupSize'>
      <el-input v-model.number="FormData.groupSize" clearable placeholder="Please input your groupSize"/>
    </el-form-item>
    <el-form-item label="Set Group Number" prop="groupNumber">
      <el-input v-model.number="FormData.groupNumber" clearable placeholder="Please input your groupNumber"/>
    </el-form-item>
    <el-form-item label="Set Default Instructor" prop="groupInstructor">
      <el-input v-model.number="FormData.groupInstructor" clearable placeholder="Please input default instructor"/>
    </el-form-item>
    <el-form-item label="Set Default Deadline">
      <el-col :span="11">
        <el-form-item prop="groupDeadline_date">
          <el-date-picker
            v-model="FormData.groupDeadline_date"
            placeholder="Pick a date"
            style="width: 100%"
            type="date"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
      </el-col>
      <el-col :span="2" class="text-center">
        <span class="text-gray-500">-</span>
      </el-col>
      <el-col :span="11">

      <el-form-item prop="groupDeadline_time">
          <el-time-picker
            v-model="FormData.groupDeadline_time"
            placeholder="Pick a time"
            style="width: 100%"
            format="HH:mm:ss"
            value-format="HH:mm:ss"
          />
      </el-form-item>
      </el-col>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="submitForm(FormRef), emit('unfold')"
      >Submit
      </el-button
      >
      <el-button @click="resetForm(FormRef)">Reset</el-button>
    </el-form-item>
  </el-form>
</template>


<script lang="ts" setup>
import {reactive, ref} from 'vue'
import type {FormInstance, FormProps} from 'element-plus'
import {api} from "boot/axios";

interface FormData {
  groupSize: number|string
  groupNumber: number|string
  groupInstructor: number|string
  groupDeadline_date: string
  groupDeadline_time: string
}

const FormRef = ref<FormInstance>()

const labelPosition = ref<FormProps['labelPosition']>('right')

const props = defineProps({
  projectId: {
    type: Number,
    required: true
  },
})

const emit = defineEmits(['change', 'delete', 'errorDialog', "unfold","successDialog"])

const FormData = reactive<FormData>({
  groupSize: '',
  groupNumber: '' ,
  groupInstructor: '',
  groupDeadline_date: '',
  groupDeadline_time: '',
})

const rules = {
    groupSize: [
      {required: true, message: 'Please input groupSize', trigger: 'blur'},
      {type: 'number', message: 'Please input a number', trigger: 'blur'},
      //检验范围是否再1-30之间
      {validator: (rule:any, value:any, callback:any) => {
          if (value < 1 || value > 30) {
            callback(new Error('Please input a number between 1 and 30'))
          } else {
            callback()
          }
        }, trigger: 'blur'
      }
    ],
    groupNumber: [
      {required: true, message: 'Please input groupNumber', trigger: 'blur'},
      {type: 'number', message: 'Please input a number', trigger: 'blur'},
      //检验范围是否为数字
      {validator: (rule:any, value:any, callback:any) => {
          if (value < 1) {
            callback(new Error('Please input a number bigger than 1'))
          } else {
            callback()
          }
        }, trigger: 'blur'
      }
    ],
    groupInstructor: [
      {required: true, message: 'Please input groupInstructor', trigger: 'blur'},
      {type: 'number', message: 'Please input a number', trigger: 'blur'},
      //检验范围是否为8为数字
      {validator: (rule:any, value:any, callback:any) => {
          if (value < 10000000 || value > 99999999) {
            callback(new Error('Please input a real ID'))
          } else {
            callback()
          }
        }, trigger: 'blur'
      }
    ],
    groupDeadline_date: [
      {required: true, message: 'Please input groupDeadline', trigger: 'blur'},
      {type: 'string', message: 'Please input a date', trigger: 'blur'},
    ],
    groupDeadline_time: [
      {required: true, message: 'Please input groupDeadline', trigger: 'blur'},
      {type: 'string', message: 'Please input a time', trigger: 'blur'},
    ],
  }

const errorMessage = {
  'icon_name': 'error',
  'icon_color': 'red',
  'icon_text_color': 'black',
  'text':""
}
const successMessage = {
  'icon_name': 'done',
  'icon_color': 'blue',
  'icon_text_color': 'white',
  'text':""
}



const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      console.log("ProjectId: " + props.projectId)
    } else {
      return false
    }
  })
}

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}

const postCreateMultiGroup = () => {
  console.log( FormData.groupDeadline_date + "T" + FormData.groupDeadline_time)
  api.post('/tea/create_multiple_groups', {
      "obj": {
        "maxsize": FormData.groupSize,
        "groupName": "BlankGroup",
        "projectId": props.projectId,
        "instructorId": FormData.groupInstructor,
        "deadline": FormData.groupDeadline_date + "T" + FormData.groupDeadline_time,
      },
      "count": FormData.groupNumber,
  }
  ).then((response) => {
    successMessage.text = response.data.msg;
    emit('successDialog', successMessage)
  }).catch((error) => {
    errorMessage.text = error.response.data.msg;
    emit('errorDialog', errorMessage)
  });
}
</script>


<style scoped>
</style>

