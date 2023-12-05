<template>
  <!--  主体部分-->
  <div class="q-pa-md ">
    <q-table
      v-model:selected="selected"
      :columns="columns_temp"
      :filter="search"
      :rows="rows_temp"
      :separator="separator"
      :title="tableTitle"
      card-class="bg-grey-2"
      class="rounded-xl"
      row-key="AssignmentName"
      selection="multiple"
      @row-dblclick="handleRowDbclick"
      @row-contextmenu="handleRowContextmenu"
      @contextmenu.prevent
    >
      <!--        右上方按钮插槽-->
      <template v-slot:top-right>
        <q-toolbar class="bg-grey-5 text-white rounded-borders">
          <!--            这里是下拉框-->
          <q-btn-dropdown v-if="this.userData.identity!==3" color="grey-5" icon="menu">
            <q-list>
              <q-item  v-close-popup clickable @click="show_create_ass_table = true">
                <q-item-section>
                  <q-item-label class="text-weight-bold">Create Assignment</q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </q-btn-dropdown>
          <!--这里是搜索框-->
          <q-input v-model="search" class="q-ml-md" dark dense input-class="text-right" standout>
            <template v-slot:append>
              <q-icon v-if="search === ''" name="search"></q-icon>
              <q-icon v-else class="cursor-pointer" name="clear" @click="search = ''"></q-icon>
            </template>
          </q-input>
        </q-toolbar>
      </template>

    </q-table>
  </div>

  <!--  删除弹窗部分-->
  <div>
    <q-dialog v-model="show_deleteDialog_student">
      <q-chat-message
        :text="['What are you doing?']"
        avatar="https://i.postimg.cc/02xBVpw2/ikun.jpg"
        bg-color="primary"
        name="Qi-Kun Xue"
        stamp="now"
        style="scale: 3"
        text-color="white"
      ></q-chat-message>
    </q-dialog>
  </div>
  <div v-if="show_button_teacher & false">
    <q-dialog v-model="show_deleteDialog_teacher">
      <confirm-dialog icon_color="red" icon_name="warning" icon_text_color="white"
                      text="Are you sure to delete?"></confirm-dialog>
    </q-dialog>
  </div>
  <!--  作业详情部分-->
  <div v-show="show_assignment_detail">
    <AssignmentsDetail :AssignmentAttachment="AssignmentAttachment"
                       :AssignmentDetail="AssignmentDetail"
                       :group-id="groupId"
                       :project-id="projectId"></AssignmentsDetail>
  </div>
  <!--  右键弹窗部分-->
  <div>
    <q-btn-group v-show="show_button_teacher"
                 :style="{'border-radius':'10px','position':'absolute', 'top':p_x, 'left':p_y, 'opacity': '1'}">
      <q-btn color="grey-3" dense icon="edit" size="md" text-color="black" @click="show_edit_ass_table = true"/>
      <q-btn color="grey-3" dense icon="delete" size="md" text-color="black" @click=" show_delete_notify"/>
    </q-btn-group>
  </div>
  <div>
    <q-btn-group v-show="show_button_student"
                 :style="{'border-radius':'10px','position':'absolute', 'top':p_x, 'left':p_y, 'opacity': '1'}">
      <q-btn color="grey-3" icon="delete" size="md" text-color="black" @click="show_deleteDialog_student = true"/>
    </q-btn-group>
  </div>
  <!--  创建作业表单部分-->
  <div>
    <el-dialog v-model="show_create_ass_table" center=true>
      <template v-slot:header>
        <div style="font-size: 20px; font-weight: bolder">Create Assignment</div>
      </template>
      <assignment-form @unfold="this.show_create_ass_table = false" :group-id="this.groupId_temp" :project-id="this.projectId_temp"></assignment-form>
    </el-dialog>
  </div>
  <!--  修改作业表单部分-->
  <div>
    <el-dialog v-model="show_edit_ass_table" center=true>
      <template v-slot:header>
        <div style="font-size: 20px; font-weight: bolder">Edit Assignment</div>
      </template>
      <assignment-form :group-id="this.groupId_temp" :project-id="this.projectId_temp"></assignment-form>
    </el-dialog>
  </div>
</template>

<script>
import {defineAsyncComponent, defineComponent, ref} from 'vue'
import {useUserStore} from "src/composables/useUserStore";
import {api} from "boot/axios";
import cloneDeep from "lodash/cloneDeep";

export default defineComponent({
  name: "AssignmentTable",
  props: {
    columns: {
      required: true,
      type: Array,
      default: () => [
        {
          name: 'AssignmentName',
          required: true,
          label: 'AssignmentId',
          align: 'left',
          field: row => row.AssignmentName,
          format: val => `${val}`,
          sortable: false,
          position: 'left'
        },
        {name: 'Deadline', align: 'left', label: 'Deadline', field: 'deadLine', sortable: true},
        {name: 'Instructor', align: 'left', label: 'Instructor', field: 'instructor', sortable: false},
        {name: 'MoreInfo', align: 'left', label: 'More Info', field: 'moreInfo', sortable: false}
      ]
    },
    rows: {
      required: true,
      type: Array,
      default: () => [
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
      ]
    },
    separator: {
      required: false,
      default: 'cell'
    },
    tableTitle: {
      required: true,
      type: String,
      default: 'Dev'
    },
    projectId: {
      required: true,
      type: Number,
      default: -1
    },
    groupId: {
      required: true,
      type: Number,
      default: -1
    },
  },
  data() {
    return {
      search: '',
      selected: [],
      p_x: ref(200),
      p_y: ref(200),
      selected_row: {
        'row': '',
        'index': '',
      },

      userData: useUserStore(),
      projectId_temp: this.projectId,
      groupId_temp:this.groupId,

      columns_temp: cloneDeep(this.columns),
      rows_temp: cloneDeep(this.rows),

      show_assignment_detail: ref(false),
      show_button_teacher: ref(false),
      show_button_student: ref(false),
      show_create_ass_table: ref(false),
      show_edit_ass_table: ref(false),
      show_deleteDialog_student: ref(false),
      show_deleteDialog_teacher: ref(false),

      AssignmentDetail: {
        assignmentId: -1,
        AssignmentName: 'Assignment 1',
        studentName: "Liwehao",
        submitTime: '2020-10-7',
        deadLine: '2020-10-10',
        instructor: 'Qi-Kun Xue1',
        grade: '100',
        state: 1,
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
        filePaths: null,
        files: null,
        type: null,
        requireExtension: '',
        filesSubmit: null,
        filePathsSubmit: null,
        comment:null,
        text:null,
        review:null,
        submitterId: null,
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
    }
  },
  methods: {
    handleRowDbclick(env, row, index) {
      let assignmentId = row.AssignmentName;
      this.getSelectedAssignment(assignmentId);
      this.show_assignment_detail = true
      this.$q.notify({
        type: 'positive',
        message: 'Switch assignment Success',
        position: 'top',
        progress: true,
        timeout: 1000,
      })
    },
    handleRowContextmenu(evt, row, index) {
      // 更新弹窗位置
      this.p_x = evt.clientY + 'px';
      this.p_y = evt.clientX + 'px';
      // 更新弹窗显示
      if (this.userData.identity !== 3) {
        this.show_button_teacher = true;
      } else {
        this.show_button_student = true;
      }
      // 更新被选中的行的内容
      this.selected_row.row = row;
      this.selected_row.index = index;
      this.$forceUpdate();
      document.addEventListener('click', () => {
        this.show_button_teacher = false;
        this.show_button_student = false;
      });
    },
    show_delete_notify() {
      let assignmentId = this.selected_row.row.AssignmentName;
      console.log(this.selected_row)
      this.$q.notify({
        message: 'Are you sure to Delete it?',
        avatar: 'https://i.postimg.cc/2SKPXCTn/ikun.jpg',
        iconSize: '40px',
        position: 'top',
        progress: true,
        timeout: 3000,
        actions: [
          { label: 'Cancel', color: 'green', handler: () => { /* ... */ } },
          { label: 'Confirm', color: 'red', handler: ()=>{this.deleteAssignment(assignmentId)}}
        ]
      })
    },

    //*************************************GET*************************************
    //获取指定作业的详细信息
    getSelectedAssignment(assignmentsId) {
      api.get('/ass/' + assignmentsId).then((res) => {
        console.log('获取作业的详细返回值在这里' + res)
        console.log(res)
        let res_body = res.data.body.key;
        let res_value = res.data.body.value;

        this.AssignmentDetail.assignmentId = assignmentsId;
        this.AssignmentDetail.AssignmentName = res_body.title;
        this.AssignmentDetail.deadLine = res_body.deadline.slice(0, 19).replace('T', ' ');
        this.AssignmentDetail.grade = (res_value === null || res_value.grade===null) ? 'Not Graded' : res_value.grade;
        this.AssignmentDetail.state = res_body.state;
        this.AssignmentDetail.moreInfo = res_body.description;
        this.AssignmentDetail.instructor = res_body.creatorName;
        this.AssignmentDetail.isReturned = () => {
          if (res_body.state === 0) {
            return 'Not Submitted'
          } else if (res_body.state === 1) {
            return 'Waiting for Grading'
          } else if (res_body.state === 2) {
            return 'Returned'
          }
        }
        this.AssignmentDetail.matGrade = res_body.fullMark;
        this.AssignmentDetail.studentName = this.userData.username;
        this.AssignmentDetail.submitTime = (res_value === null) ? 'Not Submitted' : res_value.submittedTime.slice(0, 19).replace('T', ' ');
        this.AssignmentDetail.files = res_body.files;
        this.AssignmentDetail.filePaths = res_body.filePaths;
        this.AssignmentDetail.requireExtension = res_body.requireExtension;
        this.AssignmentDetail.type = res_body.type;
        this.AssignmentDetail.filesSubmit = res_value.files;
        this.AssignmentDetail.filePathsSubmit = res_value.filepaths;
        this.AssignmentDetail.comment = res_value.comment;
        this.AssignmentDetail.text = res_value.text;
        this.AssignmentDetail.review = res_value.review;
        this.AssignmentDetail.submitterId = res_value.submitterId;

        // console.log ("AssignmentDetail: ")
        // console.log (this.AssignmentDetail)
        // console.log ("res.data: ")
        // console.log (res.data)
      }).catch((err) => {
        console.log(err);
      })
    },


    //*************************************DELETE*************************************
    //删除指定作业
    deleteAssignment(assignmentId){
      console.log(assignmentId)
      api.delete('/tea/delete_ass/' + assignmentId) .then(
        (res)=>{
          console.log(res)
          console.log(res.data)
          console.log(res.data.statusCode)
          console.log(res.data.msg)

          if (res.data.statusCode === 200){
           this.$q.notify({
              type: 'positive',
              message: 'Deleted successfully'
            })
          }
          else {
            this.$q.notify({
              type: 'negative',
              message: res.data.msg
            })
          }
        }
        )
        .catch((err)=>{
          this.$q.notify({
            type: 'negative',
            message: err
          })
          }
        )
    }
  },
  components: {
    AssignmentsDetail: defineAsyncComponent(() => import('src/components/Component_Li/special/assignmentsDetail.vue')),
    AssignmentForm: defineAsyncComponent(() => import('src/components/Component_Li/form/AssignmentForm.vue')),
    ConfirmDialog: defineAsyncComponent(() => import('src/components/Component_Li/dialog/ConfirmDialog.vue')),
  },
  watch: {
    rows: {
      handler: function (newVal, oldVal) {
        this.rows_temp = cloneDeep(newVal);
      },
      deep: true
    },
    projectId:{
      handler: function (newVal, oldVal) {
        this.projectId_temp = cloneDeep(newVal);
      },
      deep: true
    },
    groupId:{
      handler: function (newVal, oldVal) {
        this.groupId_temp = cloneDeep(newVal);
      },
      deep: true
    }
  }
})
</script>

<style scoped>

</style>
