<template>
  <div id="q-app" style="min-height: 100vh;">
    <!--    这里是顶栏部分-->
    <q-toolbar class="bg-primary text-white shadow-2">
      <q-btn flat label="GroupAdminister"></q-btn>
      <q-toolbar-title></q-toolbar-title>

      <q-space></q-space>

      <!--
        notice shrink property since we are placing it
        as child of QToolbar
      -->
      <q-tabs v-model="separator" shrink>
        <q-tab label="Cell" name="cell"></q-tab>
        <q-tab label="Herizontal" name="horizontal"></q-tab>
        <q-tab label="Vertical" name="vertical"></q-tab>
        <q-tab label="None" name="none"></q-tab>
      </q-tabs>
    </q-toolbar>

    <!--这里是表格部分-->
    <div class="q-pa-md ">
      <q-table
        v-model:selected="selected"
        :columns="columns"
        :filter="search"
        :rows="rows"
        :separator="separator"
        class="my-sticky-header-column-table"
        row-key="groupId"
        selection="single"
        title="Groups Info"
      >
        <!--        右上方按钮插槽-->
        <template v-slot:top-right>
          <q-toolbar class="bg-primary text-white rounded-borders">
            <!--            这里是下拉框-->
            <q-btn-dropdown color="primary" icon="menu">
              <q-list>
                <q-item v-close-popup clickable @click="onItemClick">
                  <q-item-label style="font-weight: bolder">View the Group</q-item-label>
                </q-item>

                <q-item v-close-popup clickable @click="onJoinClick">
                  <q-item-label style="font-weight: bolder">Apply to join</q-item-label>
                </q-item>
              </q-list>
            </q-btn-dropdown>
            <!--这里是搜索框-->
            <q-input v-model="search" class="q-ml-md" dark dense input-class="text-right" standout style="width: 160px">
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
  <q-dialog v-model="Dialog" :position="position">
    <q-card>
      <q-toolbar>
        <q-avatar>
          <img src="https://cdn.quasar.dev/logo-v2/svg/logo.svg">
        </q-avatar>

        <q-toolbar-title><span class="text-weight-bold">Attention</span> here</q-toolbar-title>

        <q-btn v-close-popup dense flat icon="close" round></q-btn>
      </q-toolbar>

      <q-card-section>
        {{dialogMessage}}
      </q-card-section>
    </q-card>
  </q-dialog>


</template>

<script>
export default {
  name: "GroupTeacherPage",
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
          groupMember: "John, Mary, Peter, Paul, Liweihao",
          instructor: "Dr. Smith",
          projectName: "Project 1",
          deadLine: "2021-10-01",
          moreInfo: "https://www.google.com\n" + "测试多文字时显示效果\n"
        },
        {
          groupId: 2,
          groupSize: 4,
          groupMember: "John, Mary, Peter, Paul",
          instructor: "Dr. Smith",
          projectName: "Project 2",
          deadLine: "2021-10-01",
          moreInfo: "https://www.google.com"
        },
      ],

      separator: 'cell',

      search: '',

      selected: [],

      Dialog: false,

      position: 'top',

      isJoinValid: false,

      dialogMessage: ''
    }
  },
  methods: {
    onJoinClick() {
      if (this.selected.length == 0) {
        this.dialogMessage = "Please select a group first!"
        this.Dialog = true;
      } else if (this.isJoinValid == true) {
        this.dialogMessage = "You have already joined a group!"
        this.Dialog = true
      } else {
        this.dialogMessage = "You have successfully apply to join the group!"
        this.Dialog = true
      }
    }
  }

}
</script>


<style scoped>

</style>
