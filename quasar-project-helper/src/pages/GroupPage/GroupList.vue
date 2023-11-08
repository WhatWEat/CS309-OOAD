<template>
  <div id="q-app" style="min-height: 100vh;">

    <!--        这里是顶栏部分,先将顶栏部分屏蔽掉-->
    <!--        <q-toolbar class="bg-primary text-white shadow-2">-->
    <!--          <q-btn flat label="GroupAdminister"></q-btn>-->
    <!--          <q-toolbar-title></q-toolbar-title>-->

    <!--          <q-space></q-space>-->

    <!--          &lt;!&ndash;-->
    <!--            notice shrink property since we are placing it-->
    <!--            as child of QToolbar-->
    <!--          &ndash;&gt;-->
    <!--          <q-tabs v-model="separator" shrink>-->
    <!--            <q-tab label="Cell" name="cell"></q-tab>-->
    <!--            <q-tab label="Herizontal" name="horizontal"></q-tab>-->
    <!--            <q-tab label="Vertical" name="vertical"></q-tab>-->
    <!--            <q-tab label="None" name="none"></q-tab>-->
    <!--          </q-tabs>-->
    <!--        </q-toolbar>-->

    <!--这里是表格部分-->
    <div class="q-pa-md ">
      <q-table
        v-model:selected="selected"
        :columns="columns"
        :filter="search"
        :rows="rows"
        :separator="separator"
        card-class="bg-grey-2"
        class="my-sticky-header-column-table"
        row-key="groupId"
        selected="none"
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
            <q-btn-dropdown color="grey-5" icon="menu">
              <q-list>
                <q-item v-close-popup clickable @click="onItemClick">
                  <q-item-label style="font-weight: bolder">Delete selected group</q-item-label>
                </q-item>

                <q-item v-close-popup clickable @click="onItemClick">
                  <q-item-label style="font-weight: bolder">Modefy selected group</q-item-label>
                </q-item>

                <q-item v-close-popup clickable @click="fileLoader = true">
                  <q-item-label style="font-weight: bolder">Upload Groups Info</q-item-label>
                </q-item>

                <q-item v-close-popup clickable @click="exportTable">
                  <q-item-label style="font-weight: bolder">Export group info to csv</q-item-label>
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


  <!--  这里是弹窗部分-->
  <!--  文件上传弹窗-->
  <q-dialog v-model="fileLoader" :position="position">
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
  <!--  这里是删除修改按钮的弹窗部分-->
  <div>
    <q-btn-group v-if="show_button_1"
                 :style="{'border-radius':'10px','position':'absolute', 'top':p_x, 'left':p_y, 'opacity': '1'}">
      <q-btn color="grey-3" dense icon="edit" size="md" text-color="black" @click="handleEditClick"/>
      <q-btn color="grey-3" dense icon="delete" size="md" text-color="black" @click="handleDeleClick"/>
    </q-btn-group>
  </div>
  <!--  这里是小组详情弹窗部分-->
  <div>
    <q-dialog v-model="show_button_2" transition-duration="500">
      <directory-card :avatar=card_data.avatar :group-id="card_data.groupId" :group-size="card_data.groupSize"
                      :members="card_data.members" :deadline="card_data.deadline" :detail="card_data.detail"
                      :style="{width: '50%' ,height: '45%'}"
      >
      </directory-card>
    </q-dialog>
  </div>
</template>

<script>
import {api} from 'boot/axios';
import {defineAsyncComponent, ref} from 'vue';

export default {
  name: 'GroupTeacherPage',
  data() {
    return {
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

      show_button_1: ref(false),

      show_button_2: ref(false),

      p_x: ref('200'),

      p_y: ref('200'),

      selected_row: {
        'row': '',
        'index': '',
      },

      show: false,

      card_data:
        {
          avatar: 'https://avatars3.githubusercontent.com/u/34883558?s=400&u=09455019882ac53dc69b23df570629fd84d37dd1&v=4',
          groupId: 12345,
          groupSize: 4,
          members: 'liweihao,ceshi1,ceshi2',
          instructor: 'Dr. Smith',
          deadline: '2021/10/01',
          detail: 'https://www.google.com'
        }
    }
  },
  methods: {
    onItemClick() {
      this.$router.push('group-list/1');
    },
    handleRowDbclick(evt, row, index) {
      this.show_button_2 = true;
    },
    handleRowContextmenu(evt, row, index) {
      // 更新弹窗位置
      this.p_x = evt.clientY + 'px';
      this.p_y = evt.clientX + 'px';
      // 更新弹窗显示
      this.show_button_1 = true;

      // 更新被选中的行的内容
      this.selected_row.row = row;
      this.selected_row.index = index;
      document.addEventListener('click', () => {
        this.show_button_1 = false;
      })
    },
    handleEditClick() {
      // 更新弹窗显示, 隐藏弹窗
      // this.show_button_1 = false;
      // 跳转到编辑页面
    },
    handleDeleClick() {
      // 更新弹窗显示, 隐藏弹窗
      this.show_button_1 = false;
      // 删除被选中的行
    },
    testConnection() {
      console.log("test");
      api.post('/tea/create_group', {
          "maxsize": 5,
          "groupName": "gp1",
          "projectId": 1,
          "instructorId": 1
        }
      ).then((response) => {
        console.log("responseHere");
        console.log(response.data);
      }).catch((error) => {
        console.log("errorHere");
        console.log(error);
      })
    },
  },
  components: {
    DirectoryCard: defineAsyncComponent(() => import('src/components/Component_Li/cards/DirectoryCard.vue'))
  },
}


</script>

<style scoped>

</style>
