<template>
  <div >
    <div class="row justify-between">
      <div class="col-sm-12 col-xs-12 col-md-7 q-pa-md">
        <q-card ref="card1" :style="{'min-height':minHeight}" class="my-card rounded-xl">
          <q-item :style="{'height':'80px'}" clickable>
            <q-item-section avatar>
              <q-btn flat round size="lg">
                <q-avatar size="xl">
                  <img src="https://i.postimg.cc/02xBVpw2/ikun.jpg">
                </q-avatar>
              </q-btn>
            </q-item-section>
            <q-item-section v-if="!isPhone">
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
                <span v-else-if="AssignmentDetail.state===3" style="font-weight: 1000;color: #C10015;font-size: large">-- Over Due</span>
                <span v-else-if="AssignmentDetail.state===4" style="font-weight: 1000;color: #C10015;font-size: large">-- Undefined</span>
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
              <q-item-section :style="{'width': this.width }" avatar>
                <span class="text-h6 text-weight-bold">Extension: </span>
              </q-item-section>
              <q-item-section>
                <span class="text-h6 text-weight-regular">
                {{ this.AssignmentDetail.requireExtension }}
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

      <div class="col-sm-12 col-xs-12 col-md-5 q-pa-md">
        <q-card ref="card2" :style="{'min-height':minHeight}" class="my-card rounded-xl">
          <q-item :style="{'min-height':'80px'}" clickable>
            <q-item-section>
              <q-btn flat>
              <q-item-label class="text-h5 text-weight-bold">Assignment attachment</q-item-label>
              <q-item-label class="text-subtitle1 text-weight-light">Submit time: {{
                  this.AssignmentDetail.submitTime
                }}
              </q-item-label>
              </q-btn>
            </q-item-section>
            <q-item-section side>
              <q-btn flat>
              <q-select
                dense
                filled
                v-model="selectedAssignment_Name"
                :options="assignmentOptions"
                style="width: 150px"
              /></q-btn>
            </q-item-section>
          </q-item>

          <q-separator size="2px"/>
          <div v-for="filePaths in AssignmentDetail.filePaths" :key="filePaths" class="attachment-item">
            <q-item>
              <q-item-section avatar>
                <q-icon class="col-3" name="attachment" size="2rem"/>
              </q-item-section>
              <q-item-section>
                <q-btn @click="handleFileClick(filePaths)"> {{ filePaths }}</q-btn>
              </q-item-section>
            </q-item>
          </div>
          <div v-if="AssignmentDetail.filePaths === null">
            <q-item :style="{height:'45px'}">
              <q-item-section avatar>
                <span class="text-h6 text-weight-light">No Attachment</span>
              </q-item-section>
            </q-item>
          </div>


          <!--          提交作业后的返回值部分-->
          <div
            v-if="this.AssignmentDetail.state!==0 && this.AssignmentDetail.state!==3 && this.AssignmentDetail.state!==4">
            <!--      虚拟的分割线部分-->
            <q-separator color="white" size="35px"/>
            <q-item :style="{'height':'80px'}" clickable>
              <q-item-section>
                <q-item-label class="text-h5 text-weight-bold">Submit Attachment</q-item-label>
              </q-item-section>
            </q-item>
            <div v-for="filePaths in AssignmentDetail.filePathsSubmit" :key="filePaths" class="attachment-item">
              <q-item>
                <q-item-section avatar>
                  <q-icon class="col-3" name="attachment" size="2rem"/>
                </q-item-section>
                <q-item-section>
                  <q-btn @click="handleFileClickSubmit(filePaths)"> {{ filePaths }}</q-btn>
                </q-item-section>
              </q-item>
            </div>
            <div class="row">
              <div class="q-pa-md col-12">
                <q-card :style="{width:'100%'}" class="rounded-xl">
                  <q-card-section>
                    <q-item>
                      <q-item-section :style="{'width':this.width}" avatar>
                        <span class="text-h6 text-weight-bold">Description: </span>
                      </q-item-section>
                      <q-item-section>
                             <span class="text-h6 text-weight-regular">
                                    {{ this.AssignmentDetail.text }}
                            </span>
                      </q-item-section>
                    </q-item>
                    <q-item>
                      <q-item-section :style="{'width':this.width}" avatar>
                        <span class="text-h6 text-weight-bold">Comment: </span>
                      </q-item-section>
                      <q-item-section>
                             <span class="text-h6 text-weight-regular">
                                    {{
                                 this.AssignmentDetail.comment === null ? 'No Comment' : this.AssignmentDetail.comment
                               }}
                            </span>
                      </q-item-section>
                    </q-item>
                  </q-card-section>
                </q-card>
              </div>
            </div>
            <div v-if="AssignmentDetail.filePathsSubmit === null">
              <q-item :style="{height:'45px'}">
                <q-item-section avatar>
                  <span class="text-h6 text-weight-light">No Attachment</span>
                </q-item-section>
              </q-item>
            </div>
          </div>

          <!--          下方的根据人来展示的部分-->
          <!--      虚拟的分割线部分-->
          <q-separator color="white" size="35px"/>
          <!--      下半部分-->
          <!--      学生提交作业-->
          <div v-if='userData.identity===3 && AssignmentDetail.type!=="e"'>
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
                  ref="uploader_stu"
                  :auto-upload="false"
                  :hide-upload-btn="true"
                  class="rounded-lg"
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
                  ref="editor_stu"
                  v-model="editorInput" :definitions="{bold: {icon:bold, tip: '彩蛋被你发现了!'}}"
                  class="rounded-lg"
                  placeholder="Type your description here...">
                </q-editor>
              </q-item-section>
            </q-item>
          </div>
          <!--     特殊作业提交-->
          <div v-else-if="userData.identity===3">
            <div v-for="(group,index) in this.toComment" :key="group">
              <!--      提交作业的部分-->
              <q-item clickable>
                <q-item-section>
                  <q-item-label style="font-weight: bolder;font-size: x-large">Peer Evaluation ID: {{
                      group
                    }}
                  </q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-btn color="primary" dense flat icon="upload" size="md" @click="postGradePeerEvaluation(group,index)">
                    <q-tooltip :offset="[10, 10]" anchor="center left" self="center right">Submit your Grade!
                    </q-tooltip>
                  </q-btn>
                </q-item-section>
              </q-item>
              <!--      作业评论部分-->
              <div>
                <q-item>
                  <q-input v-model.number="gradeEvaluator[index]" label="Grade" outlined type="number">
                    <template v-slot:append>
                      <q-avatar>
                        <img src="https://cdn.quasar.dev/logo-v2/svg/logo.svg">
                      </q-avatar>
                    </template>
                  </q-input>
                </q-item>
                <q-item>
                  <q-item-section>
                    <q-editor v-model="editorInputEvaluator[index]" :definitions="{bold: {icon:bold, tip: '彩蛋被你发现了!'}}"
                              :square="true" min-height="3rem"
                              placeholder="Type your description here...">
                    </q-editor>
                  </q-item-section>
                </q-item>
              </div>
            </div>
          </div>
          <!--     教师批改作业-->
          <div v-else>
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
        </q-card>
      </div>
    </div>
  </div>

  <div>
    <q-dialog v-model="this.showPDF" :full-heigh="true" :full-width="true" :persistent="true"
              transition-duration="1000">
      <q-card :bordered="true" :square=false>
        <PDFViewer :fileName=this.fileName
                   :getApiUrl=this.getApiUrl>
        </PDFViewer>
      </q-card>
    </q-dialog>
  </div>

  <div>
    <q-dialog v-model="this.showMD" :full-heigh="true" :full-width="true" transition-duration="1000">
      <q-card :bordered="true" :square=false>
        <MDViewer :get-content-url='this.getApiUrl'>
        </MDViewer>
      </q-card>
    </q-dialog>
  </div>




</template>

<script>
import {defineAsyncComponent, defineComponent, ref} from 'vue';
import {useUserStore} from "src/composables/useUserStore";
import {api} from "boot/axios";
import {getDownloadBlob} from "src/composables/usefulFunction";
import {useQuasar} from "quasar";
import cloneDeep from "lodash/cloneDeep";

export default defineComponent({
  name: "AssignmentDetail",
  components: {
    PDFViewer: defineAsyncComponent(() => import('src/components/ViewComponent/PDFViewer.vue')),
    MDViewer: defineAsyncComponent(() => import('src/components/ViewComponent/MDViewer.vue'))
  },
  data() {
    return {
      $q: useQuasar(),
      userData: useUserStore(),

      AssignmentDetail: cloneDeep(this.AssignmentBackUp),
      selectedAssignment_Name: '',
      assignmentOptions:[],
      allAssignment: [],

      isPhone :ref(true)  ,
      editorInput: '',
      editorInputEvaluator: [],
      grade: '',
      gradeEvaluator: [],
      fileName: '',

      width: '32%',
      minHeight: '500px',
      formData: new FormData(),

      getApiUrl: '',
      showPDF: ref(false),
      showMD: ref(false),

      toComment: [],
    }
  },
  mounted() {
    console.log('detail 加载');
    console.log('val BackUp ', this.AssignmentBackUp);
    if (this.$q.screen.lt.md) {
      this.width = '50%'
      this.isPhone = true
      this.$q.notify({
        message: 'Please use a larger screen to display the table',
        color: 'red',
        icon: 'warning',
        position: 'top',
        timeout: 2000,
      })
    }else {
      this.width = '32%'
      this.isPhone = false
    }
  },
  methods: {
    onFilesAdded(files) {
      // console.log("文件添加");
      // console.log (files)
      for (let i = 0; i < files.length; i++) {
        // let tmp = null
        // if (this.formData.get('file') !== null) {
        //   tmp = this.formData.get('file')
        //   this.formData.delete('file')
        // }
        // if (tmp !== null) {
        //   tmp.push(files[i])
        // } else {
        //   tmp = [files[i]]
        // }
        // console.log('添加文件')
        // console.log(files[i])
        this.formData.append('files', files[i]);
        this.$refs.uploader.updateFileStatus(files[i], 'uploaded')
      }
      // console.log(this.formData.get('file'))
    },
    onFilesRemoved(files) {
      // console.log("文件删除");
      // console.log (files)
      let fileTemp = this.formData.getAll('files');
      this.formData.delete('files')
      files.forEach(file => {
        if (file !== files) {
          this.formData.append('files', file);
        }
      });
      // console.log(this.formData.get('file'))
    },

    handleFileClick(filePaths) {
      this.getAssFile(filePaths);
    },
    handleFileClickSubmit(filePaths) {
      this.getAssFileSubmit(filePaths);
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
      this.formData.append('text', this.editorInput)
      api.post('/stu/submit_assignment', this.formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then((res) => {
        console.log(res)
        this.$q.notify({
          color: 'positive',
          position: 'top',
          message: res.data.msg,
          icon: 'check',
          timeout: 3000,
        })
        this.$refs.uploader_stu.reset()
        this.editorInput = ''
        this.$emit('updateAssList')
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
      this.formData = new FormData();
    },
    postGradeAssignment() {
      if (this.grade > this.AssignmentDetail.matGrade){
        this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'Grade should be less than the max grade:' + this.AssignmentDetail.matGrade,
          icon: 'report_problem',
          progress: true,
          timeout: 3000,
        })
        return
      }


      let formData = new FormData()
      if (this.editorInput === '') {
        this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'Please input your description!',
          icon: 'report_problem',
          progress: true,
          timeout: 3000,
        })
        return
      } else if (this.grade === '') {
        this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'Please input your grade!',
          icon: 'report_problem',
          progress: true,
          timeout: 3000,
        })
        return
      }
      formData.append('grade', this.grade)
      formData.append("assignmentId", this.AssignmentDetail.assignmentId)
      formData.append("submitterId", this.AssignmentDetail.submitterId)
      formData.append("comment", this.editorInput)
      formData.append("review", this.editorInput)

      let tmpNotFormData = {
        "grade":  this.grade,
        "assignmentId": this.AssignmentDetail.assignmentId,
        "submitterId":  this.AssignmentDetail.submitterId,
        "comment": this.editorInput,
        "review": this.editorInput
      }

      console.log('老师评分的内容为:')
      console.log(tmpNotFormData)

      api.post('/tea/grade_ass', tmpNotFormData, {
      }).then((res) => {
        console.log(res)
        this.$q.notify({
          color: 'positive',
          position: 'top',
          message: 'Grade successfully!',
          icon: 'check',
          timeout: 3000,
        })
        this.$emit('update')
        this.$emit('updateAssList')
      }).catch((err) => {
        // console.log(err)
        // console.log(formData.getAll('grade'))
        // console.log(formData.getAll("assignmentId"))
        // console.log(formData.getAll("submitterId"))
        // console.log(formData.getAll("comment"))
        // console.log(formData.getAll("review"))
        this.$q.notify({
          color: 'negative',
          position: 'top',
          message: err.response.data.msg,
          icon: 'report_problem',
          timeout: 3000,
        })
      })

    },
    postGradePeerEvaluation(evaluatedGroupId,index) {
      console.clear()
      let submitData =  {
        'assignmentId': this.AssignmentDetail.assignmentId,
        'content' : this.editorInputEvaluator[index],
        'grade' : this.gradeEvaluator[index],
        'commentedGroup': this.toComment[index],
      }
      console.log('submitData')
      console.log(submitData)
      let  formData = new FormData()
      formData.append('assignmentId', this.AssignmentDetail.assignmentId)
      formData.append('content', this.editorInputEvaluator[index])
      formData.append('grade', this.gradeEvaluator[index])
      formData.append('commentedGroup', this.toComment[index])


      api.post('/stu/submit_evaluation',
        formData
        , {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }
      ).then(
        (res) => {
          this.$q.notify({
            color: 'positive',
            position: 'top',
            message: res.data.msg,
            icon: 'check',
            timeout: 3000,
          })
          this.getToComment();
        }
      ).catch(
        (err) => {
          console.log(err)
          this.$q.notify({
            color: 'negative',
            position: 'top',
            message: err.response.data.msg,
            icon: 'report_problem',
            timeout: 3000,
          })
        })
    },

    //**********************************GET***************************************
    async getAssFile(filePaths) {
      let identity = '';
      if (this.userData.identity === 3) {
        identity = 'stu'
      } else if (this.userData.identity === 2) {
        identity = 'ta'
      } else if (this.userData.identity === 1) {
        identity = 'tea'
      } else {
        identity = 'adm'
      }
      console.log('identity',identity)
      console.log('userdataidentity',this.userData.identity)

      let isSuccess = false;
      await api.get(('/' + identity + '/get_ass_file/' + this.AssignmentDetail.assignmentId + '/' + filePaths), {responseType: 'blob'}).then((res) => {
        getDownloadBlob(res.data, filePaths)
        this.$q.notify({
          color: 'positive',
          position: 'top',
          message: 'Download successfully!',
          icon: 'check',
          timeout: 1000,
        })
        isSuccess = true
        console.log("到这里了吧" + isSuccess)

      }).catch((err) => {
        this.$q.notify({
          type: 'negative',
          message: 'There is something wrong'
        })
        console.log(err)
      })

      // console.clear()
      // console.log("测试pdf部分")
      // console.log(filePaths.slice(-4))
      // console.log(filePaths.slice(-4) === '.pdf')
      // console.log(isSuccess)
      // 判断filePaths的最后三位是不是pdf
      if (isSuccess && filePaths.slice(-4) === '.pdf') {
        console.log("进入")
        this.fileName = filePaths;
        this.getApiUrl = ('/' + identity + '/get_ass_file/' + this.AssignmentDetail.assignmentId + '/' + filePaths);
        this.showPDF = true;
        console.log("应该到了吧")
      } else if (isSuccess && (filePaths.slice(-3) === '.md' || filePaths.slice(-3) === '.MD')) {
        this.fileName = filePaths;
        this.getApiUrl = ('/' + identity + '/get_ass_file/' + this.AssignmentDetail.assignmentId + '/' + filePaths);
        this.showMD = true;
      }
    },
    async getAssFileSubmit(filePaths) {

      let identity = '';
      if (this.userData.identity === 3) {
        identity = 'stu'
      } else if (this.userData.identity === 2) {
        identity = 'ta'
      } else if (this.userData.identity === 1) {
        identity = 'tea'
      } else {
        identity = 'adm'
      }

      console.log(('/get_submitted_ass_file/' + this.AssignmentDetail.assignmentId +'/'+ this.AssignmentDetail.submitterId + '/' + filePaths))
      let isSuccess = false;
      await api.get(('/get_submitted_ass_file/' + this.AssignmentDetail.assignmentId +'/'+ this.AssignmentDetail.submitterId + '/' + filePaths),
        {responseType: 'blob'}).then((res) => {
        getDownloadBlob(res.data, filePaths)
        this.$q.notify({
          color: 'positive',
          position: 'top',
          message: 'Download successfully!',
          icon: 'check',
          timeout: 1000,
        })
        isSuccess = true
        console.log("到这里了吧" + isSuccess)
      }).catch((err) => {
        this.$q.notify({
          type: 'negative',
          message: err.response.statusText
        })
        console.log(err)
      })


      // console.clear()
      // console.log("测试pdf部分")
      // console.log(filePaths.slice(-4))
      // console.log(filePaths.slice(-4) === '.pdf')
      // console.log(isSuccess)
      // 判断filePaths的最后三位是不是pdf
      if (isSuccess && filePaths.slice(-4) === '.pdf') {
        console.log("进入")
        this.fileName = filePaths;
        this.getApiUrl = ('/get_submitted_ass_file/' + this.AssignmentDetail.assignmentId +'/'+ this.AssignmentDetail.submitterId + '/' + filePaths);
        this.showPDF = true;
        // console.log("应该到了吧")
      } else if (isSuccess && (filePaths.slice(-3) === '.md' || filePaths.slice(-3) === '.MD')) {
        this.fileName = filePaths;
        this.getApiUrl = ('/get_submitted_ass_file/' + this.AssignmentDetail.assignmentId +'/'+ this.AssignmentDetail.submitterId + '/' + filePaths);
        this.showMD = true;
      }
    },

    getAllSubmittedAss(){
      console.clear()
      console.log(('/tea/view_all_submitted_ass/'+this.AssignmentDetail.assignmentId +'/0/1000'))
      api.get(('/tea/view_all_submitted_ass/'+this.AssignmentDetail.assignmentId +'/0/1000')).then((res) => {
        console.log(res)
        this.assignmentOptions = []
        if (this.allAssignment === null){
          this.allAssignment= []
          this.selectedAssignment_Name = {label:'No Submit',value:0}
          // this.assignmentOptions=[{lable:'No Submit',value:0}]
        }
        else {
          this.allAssignment = res.data.body;
          this.selectedAssignment_Name = {label:this.allAssignment[0].submitterName,value:0}
          for (let i = 0; i < this.allAssignment.length; i++) {
            let key = this.allAssignment[i].submitterName;
            this.assignmentOptions.push({label: key, value: i})
          }
        }
        console.log(this.allAssignment)
        console.log(this.allAssignment.length)
        console.log(this.assignmentOptions)
      }).catch((err) => {
        // this.$q.notify({
        //   type: 'negative',
        //   message: '获取全部作业出错'
        // })
        // this.selectedAssignment_Name = {label:'No Submit',value:0}
        console.log(err)
      })
    },

    // TODO disable presentation time
    getToComment() {
      api.get('/stu/to_comment/' + this.AssignmentDetail.assignmentId).then((res) => {
        if (res.data.statusCode === 200) {
          this.toComment = res.data.body
          for (let i = 0; i < this.toComment.length; i++) {
            this.gradeEvaluator[i] = ''
            this.editorInputEvaluator[i] = ''
          }

          console.clear()
          console.log('/stu/to_comment/')
          console.log(this.toComment)
        } else {
          this.$q.notify({
            type: 'negative',
            message: 'This is a "negative" type notification in evaluation' + res.data.msg
          })
        }
      }).catch((err) => {
        this.$q.notify({
          type: 'negative',
          message: 'This is a "negative" type notification in evaluation' + err.response.data.msg
        })
      })
    }
  },
  props: {
    AssignmentBackUp:{
      required: true,
      default: {
        assignmentId: -1,
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
        filePaths: null,
        files: null,
        type: null,
        requireExtension: '',
        filesSubmit: null,
        filePathsSubmit: null,
        comment: null,
        text: null,
        review: null,
        submitterId: null,
      }
    },
    // AssignmentDetail: {
    //   required: true,
    //   default: {
    //     assignmentId: -1,
    //     AssignmentName: 'Assignment 1',
    //     studentName: "Liwehao",
    //     submitTime: '2020-10-7',
    //     deadLine: '2020-10-10',
    //     instructor: 'Qi-Kun Xue1',
    //     grade: '100',
    //     matGrade: '100',
    //     isReturned: true,
    //     moreInfo: 'Dev',
    //     state: 0,
    //     filePaths: null,
    //     files: null,
    //     type: null,
    //     requireExtension: '',
    //     filesSubmit: null,
    //     filePathsSubmit: null,
    //     comment: null,
    //     text: null,
    //     review: null,
    //     submitterId: null,
    //   }
    // },
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
    AssignmentBackUp: {
      handler: function (val, oldVal) {
        console.log('val BackUp ',val)
        this.AssignmentDetail = cloneDeep(val)
        this.allAssignment = []
        this.selectedAssignment_Name = ''
        this.assignmentOptions = []
        if (this.AssignmentBackUp.type === 'e' && (this.userData.identity === 3)) {
          this.getToComment()
        }
        this.getAllSubmittedAss()
      },
      deep: true
    },
    '$q.screen.lt.md': {
      handler: function (val, oldVal) {
        if(this.$q.screen.lt.md){
          this.width = '50%'
          this.isPhone = true
          this.$q.notify({
            color: 'negative',
            position: 'top',
            message: 'Please use PC to view the details!',
            icon: 'report_problem',
            timeout: 3000,
          })
        }else {
          this.width = '32%'
          this.isPhone = false
          // this.$q.notify({
          //   color: 'negative',
          //   position: 'top',
          //   message: 'Please use PC to view the details!',
          //   icon: 'report_problem',
          //   timeout: 5000,
          // })
        }
      },
      deep: true
    },
    selectedAssignment_Name:{
      handler: function (val, oldVal) {
        console.log('更新了选择的展示的作业,index：',val)
        console.log(this.selectedAssignment_Name)
          let indexTmp = val['value']
        console.log('index：',indexTmp)
        console.log('this.allAssignment:',this.allAssignment)
          this.AssignmentDetail.assignmentId = this.allAssignment[indexTmp].assignmentId
          this.AssignmentDetail.studentName = this.allAssignment[indexTmp].submitterName
          this.AssignmentDetail.submitTime = this.allAssignment[indexTmp].submittedTime
          this.AssignmentDetail.grade = this.allAssignment[indexTmp].grade === null ? 'Not Graded' : this.allAssignment[indexTmp].grade
          this.AssignmentDetail.isReturned = this.allAssignment[indexTmp].grade >= 0 ? 'true': 'false'
          this.AssignmentDetail.filePaths = this.allAssignment[indexTmp].filePaths
          this.AssignmentDetail.files = this.allAssignment[indexTmp].files
          this.AssignmentDetail.text = this.allAssignment[indexTmp].text
          this.AssignmentDetail.comment = this.allAssignment[indexTmp].comment
          this.AssignmentDetail.state = this.allAssignment[indexTmp].grade === null ? 1 : 2
        this.AssignmentDetail.submitterId = this.allAssignment[indexTmp].submitterId
        console.log('新的提交的作业')
        console.log(this.AssignmentDetail)
      },
      deep: true
    }
  },
  emits: ['updateAssList']
})
</script>

<style scoped>

</style>
