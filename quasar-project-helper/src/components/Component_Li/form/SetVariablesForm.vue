<template>
  <div style="margin: 20px" />
  <el-form
    ref = 'FormRef'
    :model="FormData"
    :label-position="labelPosition"
    :rules = "rules"
  >
    <el-form-item label="Set Group Size" prop='groupSize'>
      <el-input v-model="FormData.groupSize" placeholder="Please input your groupSize" clearable/>
    </el-form-item>
    <el-form-item label="Set Group Number" prop="groupNumber">
      <el-input v-model="FormData.groupNumber" placeholder="Please input your groupNumber" clearable/>
    </el-form-item>



    <el-form-item>
      <el-button type="primary" @click="submitForm(FormRef)"
      >Submit</el-button
      >
      <el-button @click="resetForm(FormRef)">Reset</el-button>
    </el-form-item>
  </el-form>
</template>


<script lang="ts" setup>
import { reactive, ref } from 'vue'
import type { FormProps } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

interface FormData {
    groupSize: number
    groupNumber: number
}

const FormRef = ref<FormInstance>()

const labelPosition = ref<FormProps['labelPosition']>('right')

const props = defineProps({
    projectID: {
        type: Number,
        required: true
    },
    userID:{
        type: Number,
        required: true
    },
})

const emit = defineEmits(['change', 'delete'])

const FormData = reactive({
    groupSize: '',
    groupNumber: '',
})

const rules =
{
    groupSize: [
        { required: true, message: 'Please input groupSize', trigger: 'blur' },
        { type: 'number', message: 'Please input a number', trigger: 'blur' },
        { min: 1, message: 'groupSize should be greater than 0', trigger: 'blur' }
    ],
    groupNumber: [
        { required: true, message: 'Please input groupNumber', trigger: 'blur' },
        { type: 'number', message: 'Please input a number', trigger: 'blur' },
        { min: 1, message: 'groupNumber should be greater than 0', trigger: 'blur' }
    ]
}


const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      console.log('submit!')
    } else {
      console.log('error submit!')
      return false
    }
  })
}

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
</script>


<style scoped>
</style>



<!--<script>-->
<!--import { reactive, ref } from 'vue'-->
<!--import { FormProps } from 'element-plus'-->

<!--export default defineComponent({-->
<!--  name: "SetVariablesForm",-->
<!--  computed: {-->
<!--    descriptionProps() {-->
<!--      return descriptionProps-->
<!--    }-->
<!--  },-->
<!--  data() {-->
<!--    return {-->
<!--      labelPosition: ref<FormProps['labelPosition']>('right'),-->
<!--      formLabelAlign: reactive({-->
<!--        name: '',-->
<!--        region: '',-->
<!--        type: ''-->
<!--      })-->
<!--    };-->
<!--  },-->
<!--  methods: {-->
<!--    submitForm(formName) {-->
<!--      this.$refs[formName].validate((valid) => {-->
<!--        if (valid) {-->
<!--          alert('submit!');-->
<!--        } else {-->
<!--          console.log('error submit!!');-->
<!--          return false;-->
<!--        }-->
<!--      });-->
<!--    },-->
<!--    resetForm(formName) {-->
<!--      this.$refs[formName].resetFields();-->
<!--    },-->
<!--    showInput() {-->
<!--      this.inputVisible = true;-->
<!--    },-->
<!--    handleInputConfirm() {-->
<!--      if (this.inputValue) {-->
<!--        this.formData.members.push(this.inputValue)-->
<!--        console.log('Have pushed' + this.inputValue.value + '\n')-->
<!--        console.log('Now members are: ' + this.formData.members + '\n')-->
<!--      }-->
<!--      this.inputVisible = false-->
<!--      this.inputValue = ''-->
<!--    },-->
<!--    handleClose(member) {-->
<!--      this.formData.members.splice(this.formData.members.indexOf(member), 1);-->
<!--    }-->
<!--  },-->
<!--  props: {-->
<!--  },-->
<!--});-->
<!--</script>-->
