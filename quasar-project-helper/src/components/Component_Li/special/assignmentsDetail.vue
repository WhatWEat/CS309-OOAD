<template>
  <div class="q-pa-md" style="">
    <div class="row justify-between">
      <div class="col-6">
        <q-card ref="card1" :style="{'min-height':minHeight}" class="my-card">
          <q-item :style="{'height':'80px'}" clickable>
            <q-item-section avatar>
              <q-btn flat round size="lg">
                <q-avatar size="xl">
                  <img src="https://i.postimg.cc/02xBVpw2/ikun.jpg">
                </q-avatar>
              </q-btn>
            </q-item-section>
            <q-item-section>
              <q-item-label style="font-weight:bolder; font-size: x-large">{{
                  this.AssignmentDetail.AssignmentName
                }}
              </q-item-label>
              <q-item-label caption style="font-weight: bold;font-size: large">{{
                  this.AssignmentDetail.instructor
                }}
              </q-item-label>
            </q-item-section>
            <q-item-section side>
              <q-btn flat size="lg">
                <span v-if="AssignmentDetail.state===2" style="font-weight: 1000;color: #3CA278;font-size:large">-- Returned</span>
                <span v-else-if="AssignmentDetail.state===1" style="font-weight: 1000;color: #E4AE1B;font-size: large">-- Waiting check</span>
                <span v-else-if="AssignmentDetail.state===0" style="font-weight: 1000;color: #C10015;font-size: large">-- Not Submitted</span>
                <span v-else style="font-weight: 1000;color: #C10015;font-size: large">-- Dev</span>
              </q-btn>
            </q-item-section>
          </q-item>
          <q-separator size="2px"/>
          <q-card-section>
            <q-item>
              <q-item-section :style="{'width': this.width }" avatar>
                <span class="text-h6 text-weight-bold">Student Name: </span>
              </q-item-section>
              <q-item-section>
                <span class="text-h6 text-weight-regular">
                {{ this.AssignmentDetail.studentName }}
              </span>
              </q-item-section>
            </q-item>
            <q-item>
              <q-item-section :style="{'width': this.width }" avatar>
                <span class="text-h6 text-weight-bold">Submit Time: </span>
              </q-item-section>
              <q-item-section>
                <span class="text-h6 text-weight-regular">
                {{ this.AssignmentDetail.submitTime }}
              </span>
              </q-item-section>
            </q-item>
            <q-item>
              <q-item-section :style="{'width': this.width }" avatar>
                <span class="text-h6 text-weight-bold">Grade: </span>
              </q-item-section>
              <q-item-section>
                <span class="text-h6 text-weight-regular">
                {{ this.AssignmentDetail.grade }}
              </span>
              </q-item-section>
            </q-item>
            <q-item>
              <q-item-section :style="{'width': this.width }" avatar>
                <span class="text-h6 text-weight-bold">Deadline: </span>
              </q-item-section>
              <q-item-section>
                <span class="text-h6 text-weight-regular">
                {{ this.AssignmentDetail.deadLine }}
              </span>
              </q-item-section>
            </q-item>
            <q-item>
              <q-item-section :style="{'width': this.width }" avatar top>
                <span class="text-h6 text-weight-bold">Description: </span>
              </q-item-section>
              <q-item-section>
                <span class="text-h6 text-weight-regular" style="text-overflow: ellipsis;  word-break: break-all;">
                  {{ this.AssignmentDetail.moreInfo }}
                </span>
              </q-item-section>
            </q-item>
          </q-card-section>
        </q-card>
      </div>

      <div class="col-5">
        <q-card ref="card2" :style="{'min-height':minHeight}" class="my-card">
          <q-item :style="{'height':'80px'}" clickable>
            <q-item-section>
              <q-item-label class="text-h5 text-weight-bold">Assignment attachment</q-item-label>
              <q-item-label class="text-subtitle1 text-weight-light">Submit time: {{
                  this.AssignmentDetail.submitTime
                }}
              </q-item-label>
            </q-item-section>
          </q-item>

          <q-separator size="2px"/>
          <div v-for="attachment in AssignmentAttachment" :key="attachment" class="attachment-item">
            <q-item>
              <q-item-section avatar>
                <q-icon class="col-3" name="attachment" size="2rem"/>
              </q-item-section>
              <q-item-section>
                <span class="text-h6 text-weight-regular ">
                  <a :href="attachment.fileAddress">{{ attachment.fileName }}</a>
                </span>
              </q-item-section>
            </q-item>
          </div>

          <!--      虚拟的分割线部分-->
          <!--      下半部分-->

          <q-separator color="white" size="35px"/>

          <!--      学生提交作业-->
          <div>
            <!--      提交作业的部分-->
            <q-item clickable>
              <q-item-section>
                <q-item-label style="font-weight: bolder;font-size: x-large">Here to Submit</q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-btn color="primary" dense flat icon="upload" size="md" @click="postSubmitAssignment">
                  <q-tooltip :offset="[10, 10]" anchor="center left" self="center right">Submit your assgnment!
                  </q-tooltip>
                </q-btn>
              </q-item-section>
            </q-item>
            <q-item>
              <q-item-section>
                <q-uploader
                  ref="uploader"
                  :auto-upload="false"
                  :hide-upload-btn="true"
                  label="Max number of files (3)"
                  max-files="3"
                  multiple
                  style="width: 100%"
                  @added="onFilesAdded"
                  @removed="onFilesRemoved"
                ></q-uploader>
              </q-item-section>
            </q-item>
            <!--      作业评论部分-->
            <q-item>
              <q-item-section>
                <q-editor
                  v-model="editorInput" :definitions="{bold: {icon:bold, tip: '彩蛋被你发现了!'}}"
                  placeholder="Type your description here...">
                </q-editor>
              </q-item-section>
            </q-item>
          </div>

          <!--     教师批改作业-->
          <div>
            <!--      提交作业的部分-->
            <q-item clickable>
              <q-item-section>
                <q-item-label style="font-weight: bolder;font-size: x-large">Here to Grade</q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-btn color="primary" dense flat icon="upload" size="md" @click="postGradeAssignment">
                  <q-tooltip :offset="[10, 10]" anchor="center left" self="center right">Submit your Grade!</q-tooltip>
                </q-btn>
              </q-item-section>
            </q-item>
            <!--      作业评论部分-->
            <q-item>
              <q-input v-model.number="grade" label="Grade" outlined type="number">
                <template v-slot:append>
                  <q-avatar>
                    <img src="https://cdn.quasar.dev/logo-v2/svg/logo.svg">
                  </q-avatar>
                </template>
              </q-input>
            </q-item>
            <q-item>
              <q-item-section>
                <q-editor
                  v-model="editorInput" :definitions="{bold: {icon:bold, tip: '彩蛋被你发现了!'}}"
                  placeholder="Type your description here...">
                </q-editor>
              </q-item-section>
            </q-item>
          </div>

          <!--     特殊作业提交-->
          <div>
            <!--      提交作业的部分-->
              <q-item clickable>
                <q-item-section>
                  <q-item-label style="font-weight: bolder;font-size: x-large">Peer Evaluation</q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-btn color="primary" dense flat icon="upload" size="md" @click="postGradeAssignment">
                    <q-tooltip :offset="[10, 10]" anchor="center left" self="center right">Submit your Grade!
                    </q-tooltip>
                  </q-btn>
                </q-item-section>
              </q-item>
              <!--      作业评论部分-->
            <div>
              <q-item>
                <q-input v-model.number="grade" label="Grade" outlined type="number">
                  <template v-slot:append>
                    <q-avatar>
                      <img src="https://cdn.quasar.dev/logo-v2/svg/logo.svg">
                    </q-avatar>
                  </template>
                </q-input>
              </q-item>
              <q-item>
                <q-item-section>
                  <q-editor  min-height="3rem" :square="true"
                             v-model="editorInput"  :definitions="{bold: {icon:bold, tip: '彩蛋被你发现了!'}}"
                    placeholder="Type your description here...">
                  </q-editor>
                </q-item-section>
              </q-item>
            </div>

          </div>

        </q-card>
      </div>
    </div>
  </div>
</template>

<script>
import {defineComponent} from 'vue';
import {useUserStore} from "src/composables/useUserStore";
import {api} from "boot/axios";

export default defineComponent({
  name: "AssignmentDetail",
  data() {
    return {
      userData: useUserStore(),

      editorInput: '',
      grade: '',

      width: '32%',
      minHeight: '690px',
      formData: new FormData(),
    }
  },
  methods: {
    onFilesAdded(files) {
      // console.log("文件添加");
      // console.log (files)
      for (let i = 0; i < files.length; i++) {
        let tmp = null
        if (this.formData.get('file') !== null) {
          tmp = this.formData.get('file')
          this.formData.delete('file')
        }
        if (tmp !== null) {
          tmp.push(files[i])
        } else {
          tmp = [files[i]]
        }
        this.formData.append('file', tmp);
        this.$refs.uploader.updateFileStatus(files[i], 'uploaded')
      }
      // console.log(this.formData.get('file'))
    },
    onFilesRemoved(files) {
      // console.log("文件删除");
      // console.log (files)
      for (let i = 0; i < files.length; i++) {
        this.formData.delete('file')
      }
      // console.log(this.formData.get('file'))
    },

    //**********************************POST***************************************
    postSubmitAssignment() {
      if (this.editorInput === '') {
        this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'Please input your description!',
          icon: 'report_problem',
          timeout: 1000,
        })
        return
      }
      this.formData.append('assignmentId', this.AssignmentDetail.assignmentId)
      this.formData.append('description', this.editorInput)
      api.post('/assignment/submit', this.formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then((res) => {
        console.log(res)
        this.$q.notify({
          color: 'positive',
          position: 'top',
          message: 'Submit successfully!',
          icon: 'check',
          timeout: 1000,
        })
        this.$emit('update')
      }).catch((err) => {
        console.log(err)
        this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'Submit failed!',
          icon: 'report_problem',
          timeout: 1000,
        })
      })
    },
    postGradeAssignment() {
      if (this.editorInput === '') {
        this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'Please input your description!',
          icon: 'report_problem',
          timeout: 1000,
        })
        return
      }
      this.formData.append('assignmentId', this.AssignmentDetail.assignmentId)
      this.formData.append('description', this.editorInput)
      api.post('/assignment/grade', this.formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then((res) => {
        console.log(res)
        this.$q.notify({
          color: 'positive',
          position: 'top',
          message: 'Grade successfully!',
          icon: 'check',
          timeout: 1000,
        })
        this.$emit('update')
      }).catch((err) => {
        console.log(err)
        this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'Grade failed!',
          icon: 'report_problem',
          timeout: 1000,
        })
      })
    },
  },
  props: {
    AssignmentDetail: {
      required: true,
      default: {
        AssignmentName: 'Assignment 1',
        studentName: "Liwehao",
        submitTime: '2020-10-7',
        deadLine: '2020-10-10',
        instructor: 'Qi-Kun Xue1',
        grade: '100',
        matGrade: '100',
        isReturned: true,
        moreInfo: 'Dev',
        state: 0,
      }
    },
    AssignmentAttachment: {
      required: true,
      default: [
        {
          fileName: 'Dev',
          fileAddress: 'www.baidu.com',
        },
        {
          fileName: 'Assignment 2',
          fileAddress: 'www.Google.com',
        }
      ]
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
  watch: {
    AssignmentDetail: {
      handler: function (val, oldVal) {
        console.log(val);
      },
      deep: true
    },
  },
})
</script>

<style scoped>

</style>
