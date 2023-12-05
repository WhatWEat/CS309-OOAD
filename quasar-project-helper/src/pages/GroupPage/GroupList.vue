<template>
  <div id="q-app" style="min-height: 0vh;">

    <!--这里是表格部分-->
    <div class="q-pa-md">
      <q-table
        v-model:selected="selected"
        :columns="columns"
        :filter="search"
        :grid="$q.screen.lt.sm"
        :rows="rows"
        :selected="selected"
        :separator="separator"
        card-class="bg-grey-2"
        class="my-sticky-header-column-table rounded-xl"
        row-key="groupId"
        selection="multiple"
        title="Groups List"
        @row-dblclick="handleRowDbclick"
        @row-contextmenu="handleRowContextmenu"
        @contextmenu.prevent
      >
        <!--        标题字体插槽-->
        <template v-slot:top-left>
          <b style=" font-size: 22px;">Group List</b>
        </template>
        <!--        右上方按钮插槽-->
        <template v-slot:top-right>
          <q-toolbar class="bg-grey-5 text-white rounded-borders ">
            <!--            这里是下拉框-->
            <q-btn-dropdown v-if="this.userData.identity<3" color="grey-5" icon="menu">
              <q-list class="bg-grey-4 text-black rounded-borders">
                <q-item v-close-popup clickable @click="show_set_form = true">
                  <q-item-label style="font-weight: bolder">Create Groups</q-item-label>
                </q-item>

                <q-item v-close-popup clickable @click="show_insert_form = true">
                  <q-item-label style="font-weight: bolder">Create A Group</q-item-label>
                </q-item>

                <q-item v-close-popup clickable @click="fileLoader = true">
                  <q-item-label style="font-weight: bolder">Upload Group Info</q-item-label>
                </q-item>

                <q-item v-close-popup clickable @click="exportTable">
                  <q-item-label style="font-weight: bolder">Export Group Info</q-item-label>
                </q-item>

                <q-item v-if="selected.length!=0" v-close-popup clickable @click="deleteSelected">
                  <q-item-label style="font-weight: bolder">Delete Seleted group</q-item-label>
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
        <!--        表格内容插槽-->
        <template v-slot:item="props">
          <div
            :style="props.selected ? 'transform: scale(0.95);' : ''"
            class="q-pa-xs col-xs-12 col-sm-6 col-md-4 col-lg-3 grid-style-transition"
          >
            <q-card :class="props.selected ? 'bg-grey-2' : ''">
              <q-card-section>
                <q-checkbox v-model="props.selected" :label="props.row.name" dense/>
                <q-btn v-if="userData.identity <= 2 && userData.identity >= 0" align="right" flat round size="sm"
                       @click="handleEditClick(props.row)">
                  <q-avatar icon="edit" size="20px">
                  </q-avatar>
                </q-btn>
                <q-btn v-if="userData.identity <= 2 && userData.identity >= 0" align="right" flat round size="sm"
                       @click="handleDeleClick(props.row)">
                  <q-avatar icon="delete" size="20px">
                  </q-avatar>
                </q-btn>
                <q-btn v-if="userData.identity === 3" align="right" flat round size="sm"
                       @click="handleAddClick(props.row)">
                  <q-avatar icon="group_add" size="20px">
                  </q-avatar>
                </q-btn>
              </q-card-section>
              <q-separator/>
              <q-list dense>
                <q-item v-for="col in props.cols.filter(col => col.name !== 'desc')" :key="col.name">
                  <q-item-section>
                    <q-item-label>{{ col.label }}</q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <q-item-label caption>{{ col.value }}</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </q-card>
          </div>
        </template>
      </q-table>
    </div>

    <!--  这里是本小组信息部分-->
    <div v-if="this.groupId!== -1" class="q-pa-md row wrap justify-center items-start">
      <div class="col-12 justify-between">
        <DirectoryCard_Input :disable-list="disableList"
                             :group-data=formData_user_self
                             :isGroupLeader="isGroupLeader"
                             :style="{width: '100%' , 'border-radius': '20px'}"
                             avatar="https://i.postimg.cc/P5HTzptm/img.png">
          <template v-slot:members_btn>

          </template>
          <template v-slot:invite_detail_input>
            <!--          <q-input v-show="show_invite_member" v-model="invite_member_id" counter dense label="Student ID" maxlength="8"-->
            <!--                   outlined>-->
            <!--            <template v-slot:append>-->
            <!--              <q-icon v-show="invite_member_id !== ''" name="close" @click="invite_member_id = ''"/>-->
            <!--            </template>-->
            <!--            <template v-slot:hint>-->
            <!--              Length hint-->
            <!--            </template>-->
            <!--            <template v-slot:after>-->
            <!--              <q-btn dense flat icon="send" round @click="handleSendInvite"/>-->
            <!--            </template>-->
            <!--          </q-input>-->
          </template>
          <template v-slot:right_btn>
            <q-item-label>
              <q-btn class="bg-indigo-7" icon="group_add" round size="sm" text-color="white"
                     @click="show_invite_member=!show_invite_member"/>
            </q-item-label>
            <q-item-label v-if="!isGroupLeader">
              <q-btn class="bg-indigo-7 text-white" icon="exit_to_app" round size="sm"
                     @click="warning_date.text='Are you sure you want to leave the group?',show_leave_warning = true"/>
            </q-item-label>
            <q-item-label v-else>
              <q-btn class="bg-indigo-7 text-white" icon="manage_accounts" round size="sm"/>
            </q-item-label>
          </template>
        </DirectoryCard_Input>
      </div>
      <div class="q-pa-lg q-gutter-md">
        <q-dialog v-model="show_invite_member" v-close-popup position="top">
          <q-card>
            <q-card-section>
              <div class="align-middle">
                <q-input v-model="invite_member_id" counter dense label="Invitee ID" maxlength="8"
                         outlined @keyup.enter.stop="handleSendInvite">
                  <template v-slot:append>
                    <q-btn v-show="invite_member_id !== ''" dense flat rounded>
                      <q-icon name="close" @click="invite_member_id = ''"/>
                    </q-btn>
                  </template>
                  <template v-slot:hint>
                    Length hint
                  </template>
                  <template v-slot:after>
                    <q-btn dense flat icon="send" round @click="handleSendInvite"/>
                  </template>
                </q-input>
              </div>
            </q-card-section>
          </q-card>
        </q-dialog>
      </div>
    </div>
  </div>

  <!--  这里是本组信息部分-->

  <!--  这里是弹窗部分-->

  <!--  文件上传弹窗-->
  <q-dialog v-model="fileLoader" :position="'standard'">
    <q-card-section class="card">
      <q-uploader
        accept=".xlsx"
        auto-upload="false"
        headers={{headers}}
        hide-upload-button="false"
        label="Upload Group Info"
        max-files="1"
        style="height: 200px"
        url="http://localhost:4444/upload"
        @rejected="onRejected"
      ></q-uploader>
    </q-card-section>
  </q-dialog>
  <!--  这里是右键删除修改按钮的弹窗部分-->
  <div>
    <q-btn-group v-show="show_button_teacher"
                 :style="{'border-radius':'10px','position':'absolute', 'top':p_x, 'left':p_y, 'opacity': '1'}">
      <q-btn color="grey-3" dense icon="edit" size="md" text-color="black" @click="handleEditClick(undefined)"/>
      <q-btn color="grey-3" dense icon="delete" size="md" text-color="black" @click="handleDeleClick(undefined)"/>
    </q-btn-group>
  </div>
  <div>
    <q-btn-group v-show="show_button_student"
                 :style="{'border-radius':'10px','position':'absolute', 'top':p_x, 'left':p_y, 'opacity': '1'}">
      <q-btn color="grey-3" icon="group_add" size="md" text-color="black" @click="handleAddClick(undefined)"/>
    </q-btn-group>
  </div>
  <!--  这里是小组详情弹窗部分-->
  <div>
    <q-dialog v-model="show_detail" transition-duration="500">
      <directory-card :avatar=card_data.avatar :creation-time="card_data.creationTime" :deadline="card_data.deadline"
                      :detail="card_data.moreInfo"
                      :group-id="card_data.groupId" :group-size="card_data.groupSize"
                      :instructor="card_data.instructor"
                      :leader="card_data.leader" :max-size="card_data.groupMaxSize" :members="card_data.members"
                      :presentation-time="card_data.presentationTime"
                      :groupName="card_data.groupName"
                      :style="{width: '100%' , 'border-radius': '20px'}">
        >
      </directory-card>
    </q-dialog>
  </div>
  <!--  这里是确认删除弹窗部分-->
  <div>
    <q-dialog v-model="show_warning" name="confirmWarning">
      <confirm-dialog :icon_color="warning_date.icon_color" :icon_name="warning_date.icon_name"
                      :icon_text_color="warning_date.icon_text_color" :style="{borderRadius: '20px'}"
                      :text="warning_date.text"
                      :title="warning_date.title"
      >
        <template v-slot:button1>
          <!--           这个是取消按钮，颜色好好选一下-->
          <q-btn v-close-popup :style="{borderRadius: '10px'}" color="green-4" label="Cancel"/>
        </template>
        <template v-slot:button2>
          <!--            这个是确认按钮-->
          <q-btn v-close-popup :style="{borderRadius: '10px'}" color="red-5" label="OK"
                 @click="deleteGroup(), console.log('删除至少点击了')"/>
        </template>
      </confirm-dialog>
    </q-dialog>
  </div>
  <!--  这里是Edit表单弹窗部分-->
  <div>
      <el-dialog  v-model="show_edit_form" :center=true  :width="formWidth" :style="{'min-width':'400px', 'border-radius': '25px'}">
        <template v-slot:header>
          <div style="font-size: 20px; font-weight: bolder">Edit Group Info</div>
        </template>
        <group-form :form-data="formData" :project-id="projectId" type="Edit" @errorDialog="handleError"
                    @successDialog="handleSuccess" @unfold="show_edit_form=false" ></group-form>
      </el-dialog>
  </div>
  <!--  这里是创建表单弹窗改部分-->
  <div class="row">
    <el-dialog v-model="show_insert_form" :center=true :width="formWidth" :style="{'min-width':'400px', 'border-radius': '25px'}">
      <template v-slot:header>
        <div style="font-size: 20px; font-weight: bolder">Create Group</div>
      </template>
      <group-form :project-id="projectId" type="Create" @errorDialog="handleError"
                  @successDialog="handleSuccess" @unfold="show_insert_form = false"></group-form>
    </el-dialog>
  </div>
  <!--  这里是设置批量创建小组弹窗部分-->
  <div>
    <el-dialog v-model="show_set_form" :center="true" :style="{width: '60%' , 'border-radius': '15px'}">
      <template v-slot:header>
        <div style="font-size: 20px; font-weight: bolder">Create multiple groups</div>
      </template>
      <create-groups-form :project-id="parseInt(projectId, 10)" @successDialog="handleSuccess"
                          @unfold="show_set_form=false"
                          @error-dialog="handleError"></create-groups-form>
    </el-dialog>
  </div>
  <!--  这里是confirmDialog的报错提示部分,可以是报错，可以是提示,只有一个确认按钮-->
  <div>
    <q-dialog v-model="show_confirm_dialog">
      <confirm-dialog :icon_color="dialogMessage.icon_color" :icon_name=dialogMessage.icon_name
                      :icon_text_color=dialogMessage.icon_text_color :text=dialogMessage.text>
        <template v-slot:button1>
          <q-space></q-space>
        </template>
        <template v-slot:button2>
          <q-btn v-close-popup :style="{borderRadius: '10px'}" color="red-5" label="OK"
                 @click="show_confirm_dialog = false"/>
        </template>
      </confirm-dialog>
    </q-dialog>
  </div>
  <!--  这里是确认退出小组的弹窗-->
  <div>
    <q-dialog v-model="show_leave_warning" name="confirmWarning">
      <confirm-dialog :icon_color="warning_date.icon_color" :icon_name="warning_date.icon_name"
                      :icon_text_color="warning_date.icon_text_color" :style="{borderRadius: '20px'}"
                      :text="warning_date.text"
                      :title="warning_date.title"
      >
        <template v-slot:button1>
          <!--           这个是取消按钮，颜色好好选一下-->
          <q-btn v-close-popup :style="{borderRadius: '10px'}" color="green-4" label="Cancel"/>
        </template>
        <template v-slot:button2>
          <!--            这个是确认按钮-->
          <q-btn v-close-popup :style="{borderRadius: '10px'}" color="red-5" label="OK"
                 @click="deleteLeaveGroup(),console.log('好吧至少点击了')"/>
        </template>
      </confirm-dialog>
    </q-dialog>
  </div>
</template>

<script>
import {defineAsyncComponent, ref} from "vue";
import {useUserStore} from "src/composables/useUserStore";
import {api} from "boot/axios";
import {
  formatDateString,
  formatDateStringPro,
  getAvatarUrlById,
  getUserData,
  merger
} from "src/composables/usefulFunction";
import {useQuasar} from "quasar";
//import {api} from 'boot/axios';
//import {defineAsyncComponent, ref} from 'vue';
//import {useUserStore} from 'src/composables/useUserStore';
//import {formatDateString, merger} from "src/composables/usefulFunction";
// TODO 权限管理，学生可以浏览，老师可以编辑 handleAddClick
// TODO disable presentation time
// TODO 检测edit表单bug
export default {
  name: 'GroupTeacherPage',
  userStore: useUserStore(),
  data() {
    return {
      formWidth:'50%',

      projectId: '',

      groupId: -1,
      $q: useQuasar(),
      disableList: {
        members: true,
        creationTime: true,
        deadLine: true,
        presentationTime: true,
        instructor: true,
        leader: true,
        maxSize: true,
        moreInformation: true,

        visibility: true,
        memberAdminister: true,
      },

      isGroupLeader: ref(false),

      userData: useUserStore(),

      formData: {
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
      },

      // formData_user_self: {
      //   groupId: '',
      //   maxSize: '',
      //   groupName: '',
      //   date1_deadline: '',
      //   date2_deadline: '',
      //   data1_presentation: '',
      //   data2_presentation: '',
      //   instructor: '',
      //   leader: '',
      //   members: [],
      //   technicalStack: [],
      //   desc: '',
      //   visibility:[], // 用来控制表单的可见性
      // },

      formData_user_self: {
        "groupId": 99999999,
        "groupName": "Dev group1",
        "creatorId": 99999999,
        "instructorId": 99999999,
        "instructor": {'Andy': 99999999},
        "instructorName": "Andy",
        'leader': {'stu0': 99999999},
        "leaderId": 99999999,
        "leaderName": "stu0",
        "maxsize": 99999999,
        "projectId": 99999999,
        "teamTime": "2099-11-06T23:47:18.995108",
        "deadline": "2099-03-10T10:00:00",
        "reportTime": "2099-10-10T10:00:00",
        "description": null,
        "technicalStack": null,
        "visibility": [
          true,
          false,
          true,
          false
        ],
        "recruitment": null,
        "memberIds": [
          12110002,
          12110004,
          12110001,
          12110003,
          12110000
        ],
        "members": [
          "stu2",
          "stu4",
          "stu1",
          "stu0",
          "stu0"
        ],
        "memCnt": 5,
        'date1_deadline': '2099-03-10',
        'date2_deadline': '10:00:00',
        'data1_presentation': '2099-10-10',
        'data2_presentation': '10:00:00',
      },

      columns: [
        {
          name: 'GroupId',
          required: true,
          label: 'GroupId',
          align: 'left',
          field: row => row.groupId,
          format: val => `${val}`,
          sortable: false,
          position: 'left'
        },
        {name: 'GroupSize', align: 'left', label: 'Group Size', field: 'groupSize', sortable: true},
        {name: 'GroupMember', align: 'left', label: 'Group Member', field: 'groupMember', sortable: false},
        {name: 'Instructor', align: 'left', label: 'Instructor', field: 'instructor', sortable: true},
        {name: 'deadLine', align: 'left', label: 'Deadline', field: 'deadLine', sortable: false},
        {name: 'MoreInfo', align: 'left', label: 'More Info', field: 'moreInfo', sortable: false}
      ],

      rows: [
        // {
        //   groupId: 1,
        //   groupSize: 4,
        //   groupMember: 'John, Mary, Peter, Paul, Liweihao',
        //   instructor: 'Dr. Smith',
        //   projectName: 'Project 1',
        //   deadLine: '2021-10-01',
        //   moreInfo: 'https://www.google.com\n' + '测试多文字时显示效果\n'
        // },
        // {
        //   groupId: 2,
        //   groupSize: 4,
        //   groupMember: 'John, Mary, Peter, Paul',
        //   instructor: 'Dr. Smith',
        //   projectName: 'Project 2',
        //   deadLine: '2021-10-01',
        //   moreInfo: 'https://www.google.com'
        // },
      ],

      separator: 'cell',

      search: '',

      selected: [],

      fileLoader: false,

      show_button_teacher: ref(false),

      show_button_student: ref(false),

      show_set_form: ref(false),

      show_leave_warning: ref(false),

      show_warning: ref(false),

      show_edit_form: ref(false),

      show_insert_form: ref(false),

      show_detail: ref(false),

      show_invite_member: ref(false),

      show_confirm_dialog: ref(false),

      p_x: ref('200'),

      p_y: ref('200'),

      selected_row: {
        'row': '',
        'index': '',
      },

      card_data:
        {
          avatar: 'https://avatars3.githubusercontent.com/u/34883558?s=400&u=09455019882ac53dc69b23df570629fd84d37dd1&v=4',
          groupId: 12345,
          groupSize: 4,
          groupMaxSize: 4,
          members: {
            'liweihao': 12110415,
            '小明': 12110355,
            '小王': 13454322,
          },
          creationTime: '2021/10/1',
          deadline: '2021/10/01',
          presentationTime: '2021/11/11',
          instructor: {
            'Mr.Simth': 12001111
          },
          leader: {
            '小明': 12110355
          },
          moreInfo: 'https://www.google.com'
        },

      warning_date: {
        icon_name: 'warning',
        icon_color: 'red-5',
        icon_text_color: 'white',
        text: 'The deadline is approaching'
      },

      invite_member_id: '',

      dialogMessage: {
        'icon_name': 'error',
        'icon_color': 'red',
        'icon_text_color': 'black',
        'text': "Dev error"
      },
    }
  },
  methods: {
    updateStatus() {
      if(this.$q.screen.lt.sm){
        this.formWidth = '95%'
        console.log('小屏幕')
      }
      else{
        this.formWidth = '50%'
        console.log('大屏幕')
      }
    },

    // 导出GroupList表格
    exportTable() {
      this.$refs.table.exportCsv({
        filename: 'table.csv',
        columns: this.columns,
        data: this.rows
      })
    },
    // 删除选中的表格行
    deleteSelected() {
      console.log(this.selected_row.row)
    },
    handleRowDbclick(evt, row, index) {
      this.getGroupDetail(row.groupId)
    },
    handleRowContextmenu(evt, row, index) {
      // 更新弹窗位置
      this.p_x = evt.clientY + 'px';
      this.p_y = evt.clientX + 'px';
      // 更新弹窗显示
      if (this.userData.identity > 2) {
        this.show_button_student = true;
      } else {
        this.show_button_teacher = true;
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
    handleEditClick(row) {
      if (row !== undefined) {
        this.selected_row.row = row;
      }
      let groupId = this.selected_row.row.groupId;
      api.get('/getGroupInfo/' + groupId).then(
        async (response) => {
          let tmp = {
            avatar: 'https://avatars3.githubusercontent.com/u/34883558?s=400&u=09455019882ac53dc69b23df570629fd84d37dd1&v=4',
            groupId: response.data.body.groupId,
            groupSize: response.data.body.members.length,
            groupMaxSize: response.data.body.maxsize,
            members: response.data.body.memberIds,
            creationTime: formatDateStringPro(response.data.body.teamTime),
            deadline: formatDateStringPro(response.data.body.deadline),
            presentationTime: formatDateStringPro(response.data.body.reportTime),
            instructor: response.data.body.instructorId,
            leader: response.data.body.leaderId,
            moreInfo: response.data.body.description,
            groupName: response.data.body.groupName,
          };
          this.formData.groupId = groupId;
          this.formData.maxSize = tmp.groupMaxSize;
          this.formData.groupName = tmp.groupName;
          // deadline是2023-10-01T16:00:00这种格式，需要转换
          this.formData.date1_deadline = tmp.deadline.slice(0, 10)
          this.formData.date2_deadline = tmp.deadline.slice(11, 19)


          // presentation是2023-10-01T16:00:00这种格式，需要转换
          this.formData.data1_presentation = tmp.presentationTime.slice(0, 10)
          this.formData.data2_presentation = tmp.presentationTime.slice(11, 19)
          this.formData.instructor = tmp.instructor;
          this.formData.leader = tmp.leader;
          this.formData.members = tmp.members;
          this.formData.technicalStack = tmp.technicalStack;
          this.formData.desc = tmp.moreInfo;

          await this.$nextTick();
          this.show_edit_form = true;
        }
      ).catch((error) => {
        console.log("errorHere");
        console.log(error);
      });

      this.show_edit_form = true;
    },
    handleDeleClick(row) {
      if (row !== undefined) {
        this.selected_row.row = row;
      }
      console.log(row, 'rows');
      this.warning_date.text = 'Are you sure you want to delete this group?';
      this.show_warning = true;
    },
    // 用来学生申请加入小组
    handleAddClick(row) {
      // 更新弹窗显示, 隐藏弹窗
      if (row !== undefined) {
        this.selected_row.row = row;
      }
      this.show_button_student = false;
      this.postJoinGroup();
    },
    // 测试连接指令
    testConnection() {
      console.log("test");
      // api.post('/tea/create_group', {
      //     "maxsize": 5,
      //     "groupName": "gp1",
      //     "projectId": 1,
      //     "instructorId": 1
      //   }
      // ).then((response) => {
      //   console.log("responseHere");
      //   console.log(response.data);
      // }).catch((error) => {
      //   console.log("errorHere");
      //   console.log(error);
      // });

      api.get('/getGroupInfo/1').then(
        (response) => {
          console.log("responseHere 获取小组简略信息部分:\n");
          console.log(response.data);
        }
      ).catch((error) => {
        console.log("errorHere");
        console.log(error);
      });
    },
    // 向服务器发送邀请新同学指令
    handleSendInvite() {
      // 更新弹窗显示, 隐藏弹窗
      this.show_invite_member = false;
      // 向服务器发送邀请新同学指令
      this.postInvite();
    },
    handleError(Message) {
      this.dialogMessage = Message;
      this.show_confirm_dialog = true;
    },
    handleSuccess(Message) {
      this.dialogMessage = Message;
      this.show_confirm_dialog = true;
    },


    //**********************************获取界面必要信息的代码部分**********************************//
    // 获取该页面的ProjectId
    getProjectId() {
      console.log("尝试获取ProjectId...\n")
      this.projectId = this.$route.params.projectID;
      console.log()
      console.log("在Monted中获取到的ProjectId为：" + this.projectId + "，类型为：" + typeof (this.projectId) + "。\n");
    },
    // 获取界面的GroupList的相关信息.即为概括性group信息部分
    getGroupList() {
      api.get('/get_brief_groups_from_proj/' + this.projectId).then(
        (response) => {
          this.rows = [];
          for (let i = 0; i < response.data.body.length; i++) {
            // 将data解析为自己需要的格式
            let temp = {
              groupId: response.data.body[i].groupId,
              groupSize:response.data.body[i].members===null ? 'Invisiable': response.data.body[i].members.length,
              groupMember: response.data.body[i].members ===null? ['Invisiable']: response.data.body[i].members.toString(),
              instructor: response.data.body[i].instructorName,
              projectName: 'Dev Name',
              deadLine: formatDateString(response.data.body[i].deadline),
              moreInfo: response.data.body[i].description
            }
            // 将解析好的数据添加到rows中
            this.rows.push(temp);
          }
          // console.log("responseHere 获取所有小组简略信息部分:\n");
          // console.log(response.data);
          // console.log("rows:\n");
          // console.log(this.rows);
        }
      ).catch((error) => {
        console.log("errorHere");
        console.log(error);
      });
    },
    // 获取指定小组的详细信息
    getGroupDetail(groupId) {
      api.get('/getGroupInfo/' + groupId).then(
        async (response) => {
          let avatarUrl = await getAvatarUrlById(response.data.body.leaderId)
          let tmp = {
            avatar: avatarUrl,
            groupId: response.data.body.groupId,
            groupSize: response.data.body.members === null ? ['Invisiable'] :response.data.body.members.length,
            groupMaxSize: response.data.body.maxsize,
            members:  response.data.body.members === null ? ['Invisiable'] : merger(response.data.body.members, response.data.body.memberIds),
            creationTime: formatDateString(response.data.body.teamTime),
            deadline: formatDateString(response.data.body.deadline),
            presentationTime: formatDateString(response.data.body.reportTime),
            instructor: merger(response.data.body.instructorName, response.data.body.instructorId),
            leader: response.data.body.leader ===null ? ['Invisiable'] :merger(response.data.body.leaderName, response.data.body.leaderId),
            moreInfo: response.data.body.description,
            groupName: response.data.body.groupName,
          }
          this.card_data = tmp
          this.show_detail = true;
        }
      ).catch((error) => {
        console.log("errorHere");
        console.log(error);
      });
    },
    // 获取该学生的所在小组的ID
    getGroupId() {
      api.get('/stu/get_group_id/' + this.projectId).then(
        (response) => {
          this.groupId = response.data.body.key;
          this.isGroupLeader = response.data.body.value;
          console.log("获取到的GroupId为：" + this.groupId + "，类型为：" + typeof (this.groupId) + "。\n");
          console.log("获取到的isGroupLeader为：" + this.isGroupLeader + "，类型为：" + typeof (this.isGroupLeader) + "。\n");
          console.log("responseHere:\n");
          console.log(response.data);
          if (this.isGroupLeader) {
            this.disableList.moreInformation = false;
            this.disableList.presentationTime = false;
            this.disableList.visibility = false;
            this.disableList.memberAdminister = false;
          }
        }
      ).catch((error) => {
        console.log("errorHere");
        console.log(error);
      });
    },
    // 获取该学生的所在小组的详细信息
    getGroupUserSelfDetail() {
      console.log("尝试获取GroupUserSelfDetail...\n")
      if (this.groupId === -1) return;
      api.get('/getGroupInfo/' + this.groupId).then(
        (response) => {
          let tmp = response.data.body

          tmp.members = merger(tmp.members, tmp.memberIds)
          // delete tmp.memberIds
          tmp.instructor = merger(tmp.instructorName, tmp.instructorId)
          // delete tmp.instructorName
          // delete tmp.instructorId
          tmp.leader = merger(tmp.leaderName, tmp.leaderId)
          // delete tmp.leaderName
          // delete tmp.leaderId
          tmp.creationTime = formatDateStringPro(tmp.teamTime)
          tmp.deadline = formatDateStringPro(tmp.deadline)
          tmp.presentationTime = formatDateStringPro(tmp.reportTime)
          tmp.visibility = tmp.visibility
          // tmp.visibility = [true, true, true, false]

          // tmp['maxSize'] = tmp.maxsize
          // delete tmp.maxsize

          // tmp['desc'] = tmp.description
          // delete tmp.description

          tmp['date1_deadline'] = tmp.deadline.slice(0, 10)
          tmp['date2_deadline'] = tmp.deadline.slice(11, 19)
          tmp['data1_presentation'] = tmp.presentationTime.slice(0, 10)
          tmp['data2_presentation'] = tmp.presentationTime.slice(11, 19)
          tmp['date1_creationTime'] = tmp.creationTime.slice(0, 10)
          tmp['date2_creationTime'] = tmp.creationTime.slice(11, 19)

          // delete tmp.memberIds
          // delete tmp.instructorId
          // delete tmp.instructorName

          // tmp = {
          //     groupId: '9999',
          //     maxSize: '9999',
          //     groupName: 'Dev Team',
          //     date1_deadline: '2002-10-1',
          //     date2_deadline: '10:00',
          //     data1_presentation: '2000-10-1',
          //     data2_presentation: '10:00',
          //     instructor: '20001000',
          //     leader: '20001000',
          //     members: [12110415,1211100,124,12],
          //     technicalStack: [],
          //     desc: 'Dev Team des',
          //   }

          this.formData_user_self = tmp
          console.log(tmp);

        }
      ).catch((error) => {
        console.log(error);
      });
      console.log("获取到的GroupUserSelfDetail为：" + this.formData_user_self + "，类型为：" + typeof (this.formData_user_self) + "。\n");
    },

    //**********************************Post信息部分**********************************//
    // 向服务器发送申请加入小组指令
    postJoinGroup() {
      api.post('/stu/apply_to_join_group', {
        "key": this.selected_row.row.groupId,
        "value": {
          "title": "加入请求",
          "content": "你好,我想加入你的小组,可以吗？",
        }
      }).then(
        async (response) => {
          this.dialogMessage = {
            'icon_name': 'done',
            'icon_color': 'green',
            'icon_text_color': 'white',
            'text': response.data.msg,
          }
          // 上面执行完毕后,弹出对话框
          await this.$nextTick();
          this.show_confirm_dialog = true;
        }
      ).catch(async (error) => {
        this.dialogMessage = {
          'icon_name': 'error',
          'icon_color': 'red',
          'icon_text_color': 'white',
          'text': error.response.data.msg,
        }
        // 上面执行完毕后,弹出对话框
        await this.$nextTick();
        this.show_confirm_dialog = true;
      });
    },
    postInvite() {
      api.post('/stu/recruit_mem', {
        "key": this.groupId,
        "value": {
          "title": "邀请进组",
          "content": "你好，我想邀请你加入我的小组，可以吗？",
          "stuView": [ //招募对象的id
            this.invite_member_id
          ]
        }
      }).then(
        async (response) => {
          // this.dialogMessage = {
          //   'icon_name': 'done',
          //   'icon_color': 'green',
          //   'icon_text_color': 'white',
          //   'text': response.data.msg,
          // }
          // 上面执行完毕后,弹出对话框
          // await this.$nextTick();
          // this.show_confirm_dialog = true;
          this.$q.notify({
            color: 'green-4',
            textColor: 'white',
            icon: 'done',
            message: response.data.msg,
            position: 'top',
            timeout: 3000,
            progress: true,
          })
          console.clear();
          console.log('response 这里');
          console.log(response);
        }
      ).catch(async (error) => {
        // this.dialogMessage = {
        //   'icon_name': 'error',
        //   'icon_color': 'red',
        //   'icon_text_color': 'white',
        //   'text': error.response.data.msg,
        // }
        // // 上面执行完毕后,弹出对话框
        // await this.$nextTick();
        // this.show_confirm_dialog = true;
        this.$q.notify({
          color: 'red-5',
          textColor: 'white',
          icon: 'error',
          message: error.response.data.msg,
          position: 'top',
          timeout: 3000,
          progress: true,
        })
      });
      this.invite_member_id = ''
    },

    //**********************************Delete信息部分**********************************//
    deleteGroup() {
      let groupId = this.selected_row.row.groupId;
      api.delete('/tea/delete_group/' + groupId).then(
        (response) => {
          console.log(response);
          this.dialogMessage = {
            'icon_name': 'done',
            'icon_color': 'green',
            'icon_text_color': 'white',
            'text': response.data.msg,
          }
          this.show_confirm_dialog = true;
          this.getGroupList();
        }
      ).catch((error) => {
        this.dialogMessage = {
          'icon_name': 'error',
          'icon_color': 'red',
          'icon_text_color': 'white',
          'text': error.response.data.msg,
        }
        this.show_confirm_dialog = true;
        console.log("errorHere");
        console.log(error);
        console.log('group_id');
        console.log(this.selected_row.row.groupId);
      });
    },
    deleteLeaveGroup() {
      let groupId = this.selected_row.row.groupId;
      api.delete('/stu/leave_group/' + this.projectId).then(
        (response) => {
          console.log(response);
          // this.dialogMessage = {
          //   'icon_name': 'done',
          //   'icon_color': 'green',
          //   'icon_text_color': 'white',
          //   'text': response.data.msg,
          // }
          // this.show_confirm_dialog = true;
          this.$q.notify({
            color: 'green-4',
            textColor: 'white',
            icon: 'done',
            message: response.data.msg,
            position: 'top',
            timeout: 3000,
            progress: true,
          })
        }
      ).catch((error) => {
        // this.dialogMessage = {
        //   'icon_name': 'error',
        //   'icon_color': 'red',
        //   'icon_text_color': 'white',
        //   'text': error.response.data.msg,
        // }
        // this.show_confirm_dialog = true;
        this.$q.notify({
          color: 'red-5',
          textColor: 'white',
          icon: 'error',
          message: error.response.data.msg,
          position: 'top',
          timeout: 3000,
          progress: true,
        })
      });
    },
  },
  components: {
    DirectoryCard: defineAsyncComponent(() => import('src/components/Component_Li/cards/DirectoryCard.vue')),
    ConfirmDialog: defineAsyncComponent(() => import('components/Component_Li/dialog/ConfirmDialog.vue')),
    GroupForm: defineAsyncComponent(() => import('src/components/Component_Li/form/GroupFrom.vue')),
    DirectoryCard_Input: defineAsyncComponent(() => import('src/components/Component_Li/cards/DirectoryCard_Input.vue')),
    CreateGroupsForm: defineAsyncComponent(() => import('components/Component_Li/form/CreateGroupsForm.vue')),
  },
  mounted() {
    this.getProjectId();
    this.getGroupList();
    this.getGroupId();
    this.getGroupUserSelfDetail();
  },
  filters: {
    // 将时间戳转化为日期
    formatDate(time) {
      if (time) {
        let date = new Date(time);
        return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
      } else {
        return '1970-01-01';
      }
    }
  },
  created() {
    console.log("created");
    getUserData();

  },
  watch: {
    groupId: function (newVal, oldVal) {
      console.log("groupId changed");
      this.getGroupUserSelfDetail();
    },
    '$q.screen.width': {
      immediate: true,
      handler(newVal, oldVal) {
          this.updateStatus();
      }
    }
  },
  computed: {
    styleStatic() {
      return {
        color: 'red',
        width: '200px',
      }
    },
    roundedStyle() {
      const radius = Math.min(this.width, this.height) / 10;
      return {
        borderRadius: `${radius}px`,
        height: '200px',
      };
    },
  }
}
</script>

<style scoped>
.rounded {
  border-radius: 10px
}
</style>
