<template>
  <div>
  <q-toolbar class="bg-grey-4 text-black rounded-borders">
    <q-btn
      flat
      label="Assignments"
    ></q-btn>
    <q-toolbar-title></q-toolbar-title>
    <q-btn-group push v-if="this.$q.screen.gt.md">
      <q-btn  text-color="white" :color="color_personal" icon="perm_identity" label="Personal" push @click="buttonHandle('Personal')"></q-btn>
      <q-btn  text-color="white" :color="color_group" icon="groups" label="Group" push @click="buttonHandle('Group')"></q-btn>
    </q-btn-group>
    <q-btn-group push v-else>
      <q-btn dense text-color="white" :color="color_personal" icon="perm_identity" label="Personal" push @click="buttonHandle('Personal')"></q-btn>
      <q-btn dense text-color="white" :color="color_group" icon="groups" label="Group" push @click="buttonHandle('Group')"></q-btn>
    </q-btn-group>
  </q-toolbar>
  </div>
  <div v-show="isPersonal">
    <assignment-table :columns="columns_personal" :rows="rows_personal" :project-id="projectId" :group-id="groupId" table-title="Personal">
    </assignment-table>
  </div>
  <div v-show="isGroup">
    <assignment-table  :columns="columns_group" :rows="rows_group" :project-id="projectId" :group-id="groupId" table-title="Group">
    </assignment-table>
  </div>
</template>

<script>
import {defineAsyncComponent, ref} from 'vue';
import {useUserStore} from "src/composables/useUserStore";
import {api} from "boot/axios";
import {getUserData, formatDateString, merger} from "src/composables/usefulFunction";

export default {
  name: "AssignmentStudent",
  data() {
    return {
      path: '',
      color_personal: 'secondary',
      color_group: 'grey-4',

      userData:useUserStore(),

      columns_personal: [
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
      rows_personal: [
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
      columns_group: [
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
      rows_group: [
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

      groupId: -1,
      projectId: -1,

      isPersonal: ref(true),
      isGroup : ref(false),
      isGroupLeader: ref(false),
    }
  },
  methods: {
    buttonHandle(btn_name) {
      if (btn_name === 'Personal' && !this.isPersonal) {
        this.isPersonal = !this.isPersonal
        this.isGroup = !this.isGroup
        this.color_personal = 'secondary'
        this.color_group = 'grey-4'
      } else if (btn_name === 'Group' && !this.isGroup) {
        this.isPersonal = !this.isPersonal
        this.isGroup = !this.isGroup
        this.color_personal = 'grey-4'
        this.color_group = 'secondary'
      }
    },

    //***********************Get请求消息*************************
    getProjectId() {
      console.log("尝试获取ProjectId...\n")
      this.projectId = this.$route.params.projectID;
      this.projectId = parseInt(this.projectId);
      console.log("在Monted中获取到的ProjectId为：" + this.projectId + "，类型为：" + typeof (this.projectId) + "。\n");
    },
    // 获取该学生的所在小组的ID
    getGroupId() {
      api.get('/stu/get_group_id/' + this.projectId).then(
        (response) => {
          this.groupId = response.data.body.key;
          this.isGroupLeader = response.data.body.value;
          console.log("获取到的GroupId为：" + this.groupId + "，类型为：" + typeof (this.groupId) + "。\n");
          console.log("获取到的isGroupLeader为：" + this.isGroupLeader + "，类型为：" + typeof (this.isGroupLeader) + "。\n");
        }
      ).catch((error) => {
        console.log("errorHere");
        console.log(error);
      });
    },
    // 获取该学生的所有作业列表
    getAssignmentList() {
      api.get('/ass-list/'+ this.projectId +'/0/10000').then((res) => {
        this.rows_group = []
        this.rows_personal = []
        // console.clear()
        // console.log("获取所有学生的作业列表");
        // console.log("res:")
        // console.log(res);
        for (let i = 0; i < res.data.body.length; i++) {
            let tmp ={};
            if (res.data.body[i].type ==='i'){
              tmp['AssignmentName'] = res.data.body[i].assignmentId;
              tmp['deadLine'] = res.data.body[i].deadline.replace('T',' ');
              tmp['instructor'] = res.data.body[i].creatorName;
              tmp['moreInfo'] = res.data.body[i].moreInfo;
              this.rows_personal.push(tmp)
              // console.log("个人作业 序号i值为:" + i)
              // console.log("res.data.body[i].type:"+ res.data.body[i].type)
              // console.log("res.data.body[i].type ==='i':" + res.data.body[i].type ==='i')
              // console.log(tmp)
            }
            else {
              tmp['AssignmentName'] = res.data.body[i].assignmentId;
              tmp['deadLine'] = res.data.body[i].deadline.replace('T',' ');
              tmp['instructor'] = res.data.body[i].creatorName;
              tmp['moreInfo'] = res.data.body[i].moreInfo;
              this.rows_group.push(tmp)
              // console.log("小组作业 序号i值为:" + i)
              // console.log("res.data.body[i].type:"+ res.data.body[i].type)
              // console.log("res.data.body[i].type ==='i':" + res.data.body[i].type ==='i')
              // console.log(tmp)
            }
        }
        // console.log()
        // console.log(this.rows_group);
        // console.log(this.rows_personal);
      }).catch((err) => {
        console.log("err:");
        console.log(err);
      })
    }
  },
  components: {
    AssignmentTable: defineAsyncComponent(() => import('src/components/Component_Li/table/assignmentTable.vue')),
  },
  mounted() {
    this.getProjectId();
    this.getGroupId();
    this.getAssignmentList();
  }
}
</script>

<style scoped>

</style>

