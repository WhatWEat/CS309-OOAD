<template>
  <q-toolbar class="bg-primary text-white rounded-borders">
    <q-btn
      flat
      label="AssignMents_Teacher"
    ></q-btn>

    <q-toolbar-title></q-toolbar-title>
  </q-toolbar>

  <!--  表格一-->
  <div v-if="true" class="q-pa-md">
    <q-table
      v-model:selected="selected_AllAssignments"
      :columns="columns_AllAssignments"
      :filter="search_AllAssignments"
      :rows="rows_AllAssignments"
      :separator="separator"
      class="my-sticky-header-column-table"
      row-key="AssignmentName"
      selection="single"
      title="All Assignments"
    >
      <!--        右上方按钮插槽-->
      <template v-slot:top-right>
        <q-toolbar class="bg-primary text-white rounded-borders">
          <!--            这里是下拉框-->
          <q-btn-dropdown color="primary" icon="menu">
            <q-list>
              <q-item v-close-popup clickable @click="viewAssignment">
                <q-item-section>
                  <q-item-label>View Details</q-item-label>
                </q-item-section>
              </q-item>

              <q-item v-close-popup clickable @click="viewDetails">
                <q-item-section>
                  <q-item-label>Post New One</q-item-label>
                </q-item-section>
              </q-item>

              <q-item v-close-popup clickable @click="cardDialog = selected != '' ? true : false">
                <q-item-section>
                  <q-item-label>Delete</q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </q-btn-dropdown>
          <!--这里是搜索框-->
          <q-input v-model="search_AllAssignments" class="q-ml-md" dark dense input-class="text-right" standout>
            <template v-slot:append>
              <q-icon v-if="search_AllAssignments === ''" name="search"></q-icon>
              <q-icon v-else class="cursor-pointer" name="clear" @click="search_AllAssignments = ''"></q-icon>
            </template>
          </q-input>
        </q-toolbar>
      </template>
      <!--        修改按钮插槽-->
    </q-table>
  </div>


  <!--  表格二-->
  <div v-if="true" class="q-pa-md">
    <q-table
      v-model:selected="selected_AssignmentDetails"
      :columns="columns_AssignmentDetails"
      :filter="search_AssignmentDetails"
      :rows="rows_AssignmentDetails[AssignmentDetailsIndex].Details"
      :separator="separator"
      class="my-sticky-header-column-table"
      row-key="StudentName"
      selection="single"
      title="Assignments details"
    >
      <!--        右上方按钮插槽-->
      <template v-slot:top-right>
        <q-toolbar class="bg-primary text-white rounded-borders">
          <!--            这里是下拉框-->
          <q-btn-dropdown color="primary" icon="menu">
            <q-list>
              <q-item v-close-popup clickable @click="viewDetails">
                <q-item-section>
                  <q-item-label>View Details</q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </q-btn-dropdown>
          <!--这里是搜索框-->
          <q-input v-model="search_AssignmentDetails" class="q-ml-md" dark dense input-class="text-right" standout>
            <template v-slot:append>
              <q-icon v-if="search_AssignmentDetails === ''" name="search"></q-icon>
              <q-icon v-else class="cursor-pointer" name="clear" @click="search_AssignmentDetails = ''"></q-icon>
            </template>
          </q-input>
        </q-toolbar>
      </template>
      <!--        修改按钮插槽-->
    </q-table>
  </div>

  <!--  作业细表-->
  <div>
    <div id="q-app" style="">
      <div class="q-pa-md" style="">
        <div class="justify-between row">
          <q-card class="my-card" style="width: 800px;left: 0px;min-height: 200px">
            <q-item>
              <q-item-section avatar>
                <q-avatar>
                  <img src="../../assets/iKun.jpg">
                </q-avatar>
              </q-item-section>

              <q-item-section>
                <q-item-label style="font-weight: bolder; font-size: x-large">Assignment Title</q-item-label>
                <q-item-label caption style="font-weight: bold;font-size: large">Instructure</q-item-label>
              </q-item-section>
              <q-item-section>
                <span v-if="AssignmentDetail.isReturned" style="font-weight: 1000;color: #C10015;font-size: large">-- Returned</span>
                <span v-else style="font-weight: 1000;color: #C10015;font-size: large">-- Not Return</span>
              </q-item-section>
            </q-item>

            <q-item>
              <q-item-section>
            <pre style="font-family: 'Microsoft YaHei UI';font-weight: bold">Student Name:{{
                "      " + AssignmentDetail.studentName
              }}</pre>
                <pre style="font-family: 'Microsoft YaHei UI';font-weight: bold">Submit Time:{{
                    "        " + AssignmentDetail.submitTime
                  }}</pre>
                <pre style="font-family: 'Microsoft YaHei UI';font-weight: bold">Grade:{{
                    "                   " + AssignmentDetail.grade
                  }}</pre>
                <pre style="font-family: 'Microsoft YaHei UI';font-weight: bold">Deadline:{{
                    "              " + AssignmentDetail.deadLine
                  }}</pre>
                <pre style="font-family: 'Microsoft JhengHei';font-weight: bolder;font-size: large">More Info:</pre>
                <pre style="font-family: 'Microsoft YaHei UI';font-weight: normal">{{ AssignmentDetail.moreInfo }}</pre>

              </q-item-section>
            </q-item>
          </q-card>


          <q-card class="my-card" style="min-width: 540px;left: 0px;min-height: 120px;">
            <q-item>
              <q-item-section>
                <q-item-label style="font-weight: bolder;font-size: x-large">Assignment attachment</q-item-label>
                <q-item-label caption style="font-weight: bold">Submit time: {{
                    this.AssignmentDetail.submitTime
                  }}
                </q-item-label>
              </q-item-section>
            </q-item>

            <q-item>
              <q-item-section>
                <div v-for="attachment in AssignmentAttachment" :key="attachment" class="attachment-item">
                  <pre style="font-family: 'Microsoft YaHei UI'"><a :href="attachment.fileAddress">{{
                      attachment.fileName
                    }}</a>          <button>按钮</button>
                  </pre>
                </div>
              </q-item-section>
            </q-item>


            <q-banner></q-banner>

            <!--评分部分-->
            <q-item>
              <q-item-section>
                <q-item-label style="font-weight: bolder;font-size: x-large">Here to Grade</q-item-label>
              </q-item-section>
            </q-item>
            <div id="q-app" class="justify-between">
              <div class="q-pa-md" style="max-width:90%; display: flex; align-items: center;">
                <q-input
                  ref="inputRef"
                  v-model="grade"
                  :rules="[val => (val >= 0 && val <= 100) || 'Please enter a number between 0 and 100']"
                  filled
                  label=" input grade ">
                </q-input>
                <pre>   </pre>
                <q-btn class="" color="primary" label="submit" style="scale: 0.9" @click="submitGrade"></q-btn>
              </div>
            </div>

            <!--            评论部分-->
            <q-item>
              <q-item-section>
                <q-item-label style="font-weight: bolder;font-size: x-large">Here to leave Comment</q-item-label>
              </q-item-section>
            </q-item>

            <q-item>
              <q-item-section>
                <div id="q-app" style="min-height: 10vh;">
                  <div class="q-pa-md q-gutter-sm">
                    <q-editor
                      v-model="editor"
                      :definitions="{
                        bold: {label: 'Bold', icon: null, tip: 'My bold tooltip'},
                        save: {tip: 'Save your work',icon: 'save',label: 'Save',handler: saveWork},
                        upload: {tip: 'Upload to cloud',icon: 'cloud_upload',label: 'Upload',handler: uploadIt}
                      }"
                      :toolbar="[
                          ['bold', 'italic', 'strike', 'underline'],
                          ['upload', 'save']
                      ]"
                    ></q-editor>
                  </div>
                </div>
              </q-item-section>
            </q-item>


          </q-card>


        </div>
      </div>
    </div>
  </div>

  显示被选中的内容
  <div className="q-mt-md">
    selected_AllAssignments:{{ JSON.stringify(selected_AllAssignments) }}
  </div>

  显示被选中的作业的序号
  <div className="q-mt-md">
    AssignmentDetailsIndex:{{ AssignmentDetailsIndex }}
  </div>

  Debug
  <div className="q-mt-md">
    <!--    {{JSON.stringify(this.selected_AllAssignments[0].AssignmentName)}}-->
  </div>

</template>

<script>
export default {
  name: "AssignmentTeacher",
  data() {
    return {
      //公共数据
      separator: 'cell',
      checkDetails: false,
      // 表格一数据
      columns_AllAssignments: [
        {
          name: 'AssignmentName',
          required: true,
          label: 'AssignmentName',
          align: 'left',
          field: row => row.AssignmentName,
          format: val => `${val}`,
          sortable: false,
          position: 'left'
        },
        {name: 'Deadline', align: 'left', label: 'Deadline', field: 'deadLine', sortable: true},
        {name: 'Instructor', align: 'left', label: 'Instructor', field: 'instructor', sortable: false},
        {name: 'MoreInfo', align: 'left', label: 'More Info', field: 'moreInfo', sortable: false}
      ],
      rows_AllAssignments: [
        {
          AssignmentName: 'Assignment 1',
          deadLine: '2020-10-10',
          instructor: 'Qi-Kun Xue1',
          moreInfo: 'View Details1'
        },
        {
          AssignmentName: 'Assignment 2',
          deadLine: '2020-10-11',
          instructor: 'Qi-Kun Xue2',
          moreInfo: 'View Details2'
        },
      ],
      selected_AllAssignments: [],
      search_AllAssignments: '',
      // 表格二数据
      columns_AssignmentDetails: [
        {
          name: 'StudentName',
          required: true,
          label: 'StudentName',
          align: 'left',
          field: row => row.StudentName,
          format: val => `${val}`,
          sortable: false,
          position: 'left'
        },
        {name: 'SubmitTime', align: 'left', label: 'SubmitTime', field: 'SubmitTime', sortable: true},
        {name: 'Deadline', align: 'left', label: 'Deadline', field: 'deadLine', sortable: true},
        {name: 'GradingTeacher', align: 'left', label: 'GradingTeacher', field: 'GradingTeacher', sortable: false},
        {name: 'Grade', align: 'left', label: 'Grade', field: 'Grade', sortable: false}
      ],
      rows_AssignmentDetails: [
        {
          AssignmentName: 'Assignment 1', Details: [{
            StudentName: 'CAIXUNKUN  Biaoge1',
            SubmitTime: 'No submit',
            deadLine: '2020-10-10',
            GradingTeacher: 'Qi-Kun Xue1',
            Grade: 'No grade'
          }, {
            StudentName: 'Xueqikun',
            SubmitTime: ' 2020-10-01',
            deadLine: '2020-10-11',
            GradingTeacher: 'Qi-Kun Xue2',
            Grade: '100'
          },]
        },
        {
          AssignmentName: 'Assignment 2', Details: [{
            StudentName: 'CAIXUNKUN  Biage2',
            SubmitTime: 'No submit',
            deadLine: '2020-10-10',
            GradingTeacher: 'Qi-Kun Xue1',
            Grade: 'No grade'
          }, {
            StudentName: 'Xueqikun',
            SubmitTime: ' 2020-10-01',
            deadLine: '2020-10-11',
            GradingTeacher: 'Qi-Kun Xue2',
            Grade: '100'
          }]
        }
      ],
      selected_AssignmentDetails: [],
      search_AssignmentDetails: '',
      AssignmentDetailsIndex: 0,
      // 表格三数据
      AssignmentDetail: {
        AssignmentName: 'Assignment 1',
        studentName: "Liwehao",
        submitTime: '2020-10-7',
        deadLine: '2020-10-10',
        instructor: 'Qi-Kun Xue1',
        grade: '100',
        matGrade: '100',
        isReturned: true,
        moreInfo: 'Course Assignment 4:\n' +
          '\n' +
          'Chapter 4：Exercise 2，Exercise 8，and Exercise 22\n' +
          '\n' +
          'Deadline: Please submit your homework through the Sakai system before 10:20AM on April 11, 2023.  \n' +
          '\n' +
          'Please note that you have only one chance to submit your homework in the SAKAI system.\n' +
          '\n' +
          'No re-submission is allowed.\n' +
          '\n' +
          'No late submission is allowed.',
      },
      AssignmentAttachment: [
        {
          fileName: 'Assignment 1',
          fileAddress: 'www.baidu.com',
        },
        {
          fileName: 'Assignment 2',
          fileAddress: 'www.Google.com',
        }
      ],
      grade: null,
      editor: null
    }
  }
  ,
  methods: {
    viewAssignment() {
      if (this.selected_AllAssignments.length !== 0) {
        this.AssignmentDetailsIndex = this.rows_AssignmentDetails.findIndex(item => item.AssignmentName == this.selected_AllAssignments[0].AssignmentName)
      }
    },
    saveWork() {
      return false
    },
    uploadIt() {
      return false
    },
  }
}


</script>

<style scoped>

</style>
