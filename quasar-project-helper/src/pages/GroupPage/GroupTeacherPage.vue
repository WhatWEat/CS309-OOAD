<template>
  <div id="q-app" style="min-height: 300vh;">
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
        :columns="columns"
        :filter="search"
        :rows="rows"
        :separator="separator"
        class="my-sticky-header-column-table"
        row-key="groupId"
        title="Groups Info"
        selection="single"
        v-model:selected="selected"
      >
<!--        右上方按钮插槽-->
        <template v-slot:top-right>
          <q-toolbar class="bg-primary text-white rounded-borders">
            <!--            这里是下拉框-->
            <q-btn-dropdown color="primary" icon="menu">
              <q-list>
                <q-item v-close-popup clickable @click="onItemClick">
                  <q-card-section class="card">
                    <q-btn
                      color="primary"
                      label="Delete selected group"
                      no-caps
                      @click="exportTable"
                      size="15px"
                    ></q-btn>
                  </q-card-section>
                </q-item>

                <q-item v-close-popup clickable @click="onItemClick">
                  <q-card-section class="card">
                    <q-btn
                      color="primary"
                      label="Modefy selected group"
                      no-caps
                      @click="exportTable"
                      size="15px"
                    ></q-btn>
                  </q-card-section>
                </q-item>

                <q-item v-close-popup clickable @click="onItemClick">
                  <q-card-section class="card">
                    <q-uploader
                      accept=".xlsx"
                      auto-upload="false"
                      headers={{headers}}
                      hide-upload-button="false"
                      label="Upload Group Info"
                      max-files="1"
                      style="max-width: 600px"
                      url="http://localhost:4444/upload"
                      @rejected="onRejected"
                    ></q-uploader>
                  </q-card-section>
                </q-item>

                <q-item v-close-popup clickable @click="onItemClick">
                  <q-card-section class="card">
                    <q-btn
                      color="primary"
                      icon-right="archive"
                      label="Export all  groups infomation to csv a file"
                      no-caps
                      @click="exportTable"
                    ></q-btn>
                  </q-card-section>
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
    <!--    这里是表格之下的其他内容-->
    <div class="q-mt-md">
      Selected: {{ JSON.stringify(selected) }}
    </div>
  </div>
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
        {name: 'ProjectName', align: 'left', label: 'Project Name', field: 'projectName', sortable: false},
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
          moreInfo: "https://www.google.com\n" + "测试多文字时显示效果\n" + "--888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888-"
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
    }
  }

}
</script>


<style scoped>

</style>
