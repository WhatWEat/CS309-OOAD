<template>
  <!--  主体部分-->
  <div class="q-pa-md ">
    <q-table
      v-model:selected="selected"
      :columns="columns"
      :filter="search"
      :rows="rows"
      :separator="separator"
      class="my-sticky-header-column-table"
      row-key="AssignmentName"
      selection="single"
      title="Group"
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


  <!--  作业info插槽-->
  <router-view></router-view>
  <!--  弹窗-->
  <div>
    <q-dialog v-model="cardDialog">
      <q-chat-message
        :text="['What are you doing?']"
        avatar="../../assets/iKun.jpg"
        bg-color="primary"
        name="Qi-Kun Xue"
        stamp="now"
        style="scale: 3"
        text-color="white"
      ></q-chat-message>
    </q-dialog>
  </div>
</template>

<script>
export default {
  name: "AssignmentStudent_Personal",
  data() {
    return {
      columns: [
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
      ],

      rows: [
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


      separator: 'cell',

      search: '',

      selected: [],

      cardDialog: false,
    }
  },

  methods:{
      viewDetails() {
        // 根据需要替换为您的路由链接
        let temp;
        temp = 1
        this.$router.push('/mengbi1/' + temp)
      }
  }
}
</script>

<style scoped>

</style>
