<template>
  <!--  主体部分-->
  <div class="q-pa-md ">
    <q-table
      v-model:selected="selected"
      :columns="columns"
      :filter="search"
      :rows="rows"
      :separator="separator"
      card-class="bg-grey-2"
      class="my-sticky-header-column-table"
      row-key="AssignmentName"
      selection="multiple"
      :title="tableTitle"
      @row-dblclick="handleRowDbclick"
      @row-contextmenu="handleRowContextmenu"
      @contextmenu.prevent
    >
      <!--        右上方按钮插槽-->
      <template v-slot:top-right>
        <q-toolbar class="bg-grey-5 text-white rounded-borders">
          <!--            这里是下拉框-->
          <q-btn-dropdown color="grey-5" icon="menu">
            <q-list>
              <q-item v-close-popup clickable @click="viewDetails">
                <q-item-section>
                  <q-item-label>View Details</q-item-label>
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
          <q-input v-model="search" class="q-ml-md" dark dense input-class="text-right" standout>
            <template v-slot:append>
              <q-icon v-if="search === ''" name="search"></q-icon>
              <q-icon v-else class="cursor-pointer" name="clear" @click="search = ''"></q-icon>
            </template>
          </q-input>
        </q-toolbar>
      </template>
      <!--        修改按钮插槽-->
    </q-table>
  </div>

  <!--  弹窗-->
  <div>
    <q-dialog v-model="cardDialog">
      <q-chat-message
        :text="['What are you doing?']"
        avatar="../../../assets/iKun.jpg"
        bg-color="primary"
        name="Qi-Kun Xue"
        stamp="now"
        style="scale: 3"
        text-color="white"
      ></q-chat-message>
    </q-dialog>
  </div>
  <!--  作业详情部分-->
  <div v-show="show_assignment_detail">
    <AssignmentsDetail :AssignmentAttachment="AssignmentAttachment" :AssignmentDetail="AssignmentDetail"></AssignmentsDetail>
  </div>
  <!--  右键弹窗部分-->
  <div>
    <q-btn-group v-show="show_button_teacher"
                 :style="{'border-radius':'10px','position':'absolute', 'top':p_x, 'left':p_y, 'opacity': '1'}">
      <q-btn color="grey-3" dense icon="edit" size="md" text-color="black" />
      <q-btn color="grey-3" dense icon="delete" size="md" text-color="black" />
    </q-btn-group>
  </div>
  <div>
    <q-btn-group v-show="show_button_student"
                 :style="{'border-radius':'10px','position':'absolute', 'top':p_x, 'left':p_y, 'opacity': '1'}">
      <q-btn color="grey-3" icon="group_add" size="md" text-color="black" />
    </q-btn-group>
  </div>
  <!--  创建作业表单部分-->
  <div>
    <el-dialog v-model="show_create_ass_table" center="true">
      <template v-slot:header>
        <div style="font-size: 20px; font-weight: bolder">Create Assignment</div>
      </template>
      <assignment-form></assignment-form>
    </el-dialog>
  </div>
  <!--  修改作业表单部分-->
  <div>
    <el-dialog v-model="show_edit_ass_table" center="true">
      <template v-slot:header>
        <div style="font-size: 20px; font-weight: bolder">Edit Assignment</div>
      </template>
      <assignment-form></assignment-form>
    </el-dialog>
  </div>


</template>

<script>
import {defineAsyncComponent, defineComponent,ref} from 'vue'
import {useUserStore} from "src/composables/useUserStore";
import {api} from "boot/axios";

export default defineComponent( {
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
    }
  },
  data() {
    return {
      search: '',
      selected: [],
      cardDialog: ref(false),    //是否显示弹窗
      p_x: ref(200),
      p_y: ref(200),
      selected_row: {
        'row': '',
        'index': '',
      },

      userData: useUserStore(),

      show_assignment_detail: ref(false),
      show_button_teacher: ref(false),
      show_button_student: ref(false),
      show_create_ass_table: ref(false),
      show_edit_ass_table: ref(true),

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
    }
  },
  methods:{
    viewDetails() {
      // 根据需要替换为您的路由链接
      //这个跳转还需要进一步的完善
      const newpath = this.$route.path + '/assignmentDetail'
      this.$router.push(newpath)
    },
    handleRowDbclick(row) {
      this.show_assignment_detail = true
    },
    handleRowContextmenu(evt, row, index){
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
  },
  components: {
    AssignmentsDetail: defineAsyncComponent(() => import('src/components/Component_Li/special/assignmentsDetail.vue')),
    AssignmentForm: defineAsyncComponent(() => import('src/components/Component_Li/form/AssignmentForm.vue')),
  }
})
</script>

<style scoped>

</style>
