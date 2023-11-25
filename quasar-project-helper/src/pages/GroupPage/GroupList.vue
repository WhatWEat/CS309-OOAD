<template>
  <div id="q-app" style="min-height: 0vh;">

    <!--这里是表格部分-->
    <div class="q-pa-md ">
      <q-table
        v-model:selected="selected"
        :columns="columns"
        :filter="search"
        :rows="rows"
        :selected="selected"
        :separator="separator"
        card-class="bg-grey-2"
        class="my-sticky-header-column-table"
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
          <q-toolbar class="bg-grey-4 text-white rounded-borders ">
            <!--            这里是下拉框-->
            <q-btn-dropdown v-if="this.userData.identity<3" color="grey-4" icon="menu">
              <q-list class="bg-grey-4 text-black rounded-borders">
                <q-item v-close-popup clickable @click="show_set_form = true">
                  <q-item-label style="font-weight: bolder">Set global variables</q-item-label>
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
      </q-table>
    </div>


    <!--    这里是表格之下的其他内容-->
    <div class="q-mt-md">
      Dev :
      Selected {{ JSON.stringify(selected) }}
    </div>
  </div>

  <!--  这里是本组信息部分-->


  <div>{{ this.card_data.moreInfo }}</div>


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
    <q-btn-group v-show="show_button_teacher "
                 :style="{'border-radius':'10px','position':'absolute', 'top':p_x, 'left':p_y, 'opacity': '1'}">
      <q-btn color="grey-3" dense icon="edit" size="md" text-color="black" @click="handleEditClick"/>
      <q-btn color="grey-3" dense icon="delete" size="md" text-color="black" @click="handleDeleClick"/>
    </q-btn-group>
  </div>
  <div>
    <q-btn-group v-show="show_button_student"
                 :style="{'border-radius':'10px','position':'absolute', 'top':p_x, 'left':p_y, 'opacity': '1'}">
      <q-btn color="grey-3" icon="group_add" size="md" text-color="black" @click="handleAddClick"/>
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
                      :style="{width: '50%' , 'border-radius': '20px'}">
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
          <q-btn v-close-popup :style="{borderRadius: '10px'}" color="green-4" label="Cancel"
                 @click="confirm_1 = false"/>
        </template>
        <template v-slot:button2>
          <!--            这个是确认按钮-->
          <q-btn v-close-popup :style="{borderRadius: '10px'}" color="red-5" label="OK" @click="confirm_1 = false"/>
        </template>
      </confirm-dialog>
    </q-dialog>
  </div>
  <!--  这里是Edit表单弹窗部分-->
  <div>
    <el-dialog v-model="show_edit_form" center="true">
      <template v-slot:header>
        <div style="font-size: 20px; font-weight: bolder">Edit Group Info</div>
      </template>
      <group-form></group-form>
    </el-dialog>
  </div>
  <!--  这里是创建表单弹窗改部分-->
  <div>
    <el-dialog v-model="show_insert_form" center="true">
      <template v-slot:header>
        <div style="font-size: 20px; font-weight: bolder">Create Group</div>
      </template>
      <group-form></group-form>
    </el-dialog>
  </div>
  <!--  这里是设置小组变量表单弹窗部分-->
  <div>
    <el-dialog v-model="show_set_form" :style="{width: '60%' , 'border-radius': '15px'}" center="true">
      <template v-slot:header>
        <div style="font-size: 20px; font-weight: bolder">Set group variables</div>
      </template>
      <set-variable-form></set-variable-form>
    </el-dialog>
  </div>

  <!--  这里是本小组信息部分-->
  <div class="row wrap justify-center items-start">
    <div class="col-11 justify-between">
      <DirectoryCard_Input v-model:presentation-time="card_data.presentationTime"
                           :creation-time="card_data.creationTime" :dead-line="card_data.deadline"
                           :deadline="card_data.deadline" :detail="card_data.moreInfo"
                           :group-id="card_data.groupId"
                           :group-size="card_data.groupSize" :instructor="card_data.instructor"
                           :leader="card_data.leader"
                           :max-size="card_data.groupMaxSize" :members="card_data.members"
                           :more-information="card_data.moreInfo"
                           :style="{width: '100%' , 'border-radius': '20px'}">
        <template v-slot:members_btn>

        </template>
        <template v-slot:invite_detail_input>
          <q-input v-show="show_invite_member" v-model="invite_member_id" counter dense label="Student ID" maxlength="8"
                   outlined>
            <template v-slot:append>
              <q-icon v-if="invite_member_id !== ''" name="close" @click="invite_member_id = ''"/>
            </template>
            <template v-slot:hint>
              Length hint
            </template>
            <template v-slot:after>
              <q-btn dense flat icon="send" round @click="handleSendInvite"/>
            </template>
          </q-input>
        </template>
        <template v-slot:right_btn>
          <q-item-label >
            <q-btn  class="bg-indigo-7" icon="group_add" round size="sm" text-color="white"  @click="show_invite_member=true"/>
          </q-item-label>
          <q-item-label v-if="isGroupLeader">
            <q-btn class="bg-indigo-7 text-white" flat icon="exit_to_app" round size="sm"/>
          </q-item-label>
          <q-item-label v-else>
            <q-btn class="bg-indigo-7 text-white" flat icon="manage_accounts" round size="sm"/>
          </q-item-label>
        </template>
      </DirectoryCard_Input>
    </div>
  </div>


</template>

<script>
import {api} from 'boot/axios';
import {defineAsyncComponent, ref} from 'vue';
import {useUserStore} from 'src/composables/useUserStore';


export default {
  name: 'GroupTeacherPage',
  userStore: useUserStore(),
  data() {
    return {
      projectId: '',

      isGroupLeader: ref(),

      userData: useUserStore(),

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
        {
          groupId: 1,
          groupSize: 4,
          groupMember: 'John, Mary, Peter, Paul, Liweihao',
          instructor: 'Dr. Smith',
          projectName: 'Project 1',
          deadLine: '2021-10-01',
          moreInfo: 'https://www.google.com\n' + '测试多文字时显示效果\n'
        },
        {
          groupId: 2,
          groupSize: 4,
          groupMember: 'John, Mary, Peter, Paul',
          instructor: 'Dr. Smith',
          projectName: 'Project 2',
          deadLine: '2021-10-01',
          moreInfo: 'https://www.google.com'
        },
      ],

      separator: 'cell',

      search: '',

      selected: [],

      fileLoader: false,

      show_button_teacher: ref(false),

      show_button_student: ref(false),

      show_set_form: ref(false),

      show_warning: ref(false),

      show_edit_form: ref(false),

      show_insert_form: ref(false),

      show_detail: ref(false),

      show_invite_member: ref(false),

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
    }
  },
  methods: {
    // 获取该用户是否为组长
    getIfGroupLeader() {
      console.log("尝试获取用户是否为组长...\n")
      this.isGroupLeader = true;
      console.log("在Monted中获取到的isGroupLeader为：" + this.isGroupLeader + "，类型为：" + typeof (this.isGroupLeader) + "。\n");
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
      //更新小组详情卡片的内容
      this.getGroupDetail(row.groupId)
      // 显示小组详情卡片
      this.show_detail = true;
    },
    handleRowContextmenu(evt, row, index) {
      // 更新弹窗位置
      this.p_x = evt.clientY + 'px';
      this.p_y = evt.clientX + 'px';
      // 更新弹窗显示
      if (this.userData.identity > 1) {
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
    handleEditClick() {
      this.show_edit_form = true;
    },
    handleDeleClick() {
      this.show_warning = true;
    },
    // 用来学生申请加入小组
    handleAddClick() {
      // 更新弹窗显示, 隐藏弹窗
      this.show_button_student = false;
      // 跳转到编辑页面
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
          console.log("responseHere:\n");
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
    },


    //**********************************获取界面必要信息的代码部分**********************************//
    // 获取该页面的ProjectId
    getProjectId() {
      console.log("尝试获取ProjectId...\n")
      this.projectId = this.$route.params.projectID;
      console.log("在Monted中获取到的ProjectId为：" + this.projectId + "，类型为：" + typeof (this.projectId) + "。\n");
    },
    // 获取界面的GroupList的相关信息.即为概括性group信息部分
    getGroupList(){
      api.get('/get_brief_groups_from_proj/' + this.projectId).then(
        (response) => {
          this.rows = [];
          for (let i = 0; i < response.data.body.length; i++) {
            // 将data解析为自己需要的格式
            let temp = {
              groupId: response.data.body[i].groupId,
              groupSize: response.data.body[i].members.length,
              groupMember: response.data.body[i].members.toString(),
              instructor: response.data.body[i].instructorName,
              projectName: 'Dev Name',
              deadLine: "2021/10/01 Dev",
              moreInfo: response.data.body[i].description
            }
            // 将解析好的数据添加到rows中
            this.rows.push(temp);
          }
        }
      ).catch((error) => {
        console.log("errorHere");
        console.log(error);
      });
    },
    // 获取指定小组的详细信息
    getGroupDetail( groupId ){
      api.get('/getGroupInfo/' + groupId).then(
        (response) => {
            let tmp = {
              avatar: 'https://avatars3.githubusercontent.com/u/34883558?s=400&u=09455019882ac53dc69b23df570629fd84d37dd1&v=4',
              groupId: response.data.body.groupId,
              groupSize: response.data.body.members.length,
              groupMaxSize: response.data.body.maxsize,
              members: {
                'liweihao': 12110415,
                '小明': 12110355,
                '小王': 13454322,
                'Dev': 12345678,
              },
              creationTime: response.data.body.teamTime,
              deadline: '2021/10/01 Dev',
              presentationTime: response.data.body.reportTime,
              instructor: {
                'Mr.Simth Dev': 12001111
              },
              leader: {
                '小明 Dev': 12110355
              },
              moreInfo: response.data.body.description
            }
            this.card_data = tmp;
        }
      ).catch((error) => {
        console.log("errorHere");
        console.log(error);
      });
    }

  },
  components: {
    DirectoryCard: defineAsyncComponent(() => import('src/components/Component_Li/cards/DirectoryCard.vue')),
    ConfirmDialog: defineAsyncComponent(() => import('components/Component_Li/dialog/ConfirmDialog.vue')),
    GroupForm: defineAsyncComponent(() => import('src/components/Component_Li/form/GroupFrom.vue')),
    DirectoryCard_Input: defineAsyncComponent(() => import('src/components/Component_Li/cards/DirectoryCard_Input.vue')),
    SetVariableForm: defineAsyncComponent(() => import('src/components/Component_Li/form/SetVariablesForm.vue')),
  },
  mounted() {
    this.getProjectId();
    this.getGroupList();
    // this.getIfGroupLeader();
    // this.testConnection();
  },
}


</script>

<style scoped>
.rounded {
  border-radius: 10px
}
</style>
