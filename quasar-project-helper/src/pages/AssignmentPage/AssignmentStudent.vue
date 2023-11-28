<template>

  <q-toolbar class="bg-grey-4 text-black rounded-borders">
    <q-btn
      flat
      label="Assignments"
    ></q-btn>

    <q-toolbar-title></q-toolbar-title>
    <q-btn-group push>
      <q-btn :color="color_personal" icon="perm_identity" label="Personal" push @click="buttonHandle"></q-btn>
      <q-btn :color="color_group" icon="groups" label="Group" push @click="buttonHandle"></q-btn>
    </q-btn-group>
  </q-toolbar>
<!--  <router-view></router-view>-->

  个人表单/小组表单
  <div v-show="isPersonal">
    <assignment-table :columns="columns_personal" :rows="rows_personal" table-title="Personal">
    </assignment-table>
  </div>
  <div v-show="isGroup">
    <assignment-table  :columns="columns_group" :rows="rows_group" table-title="Group">
    </assignment-table>
  </div>
<!--  <div v-show="true">-->
    <assignments-detail :AssignmentAttachment="AssignmentAttachment" :AssignmentDetail="AssignmentDetail"></assignments-detail>
<!--  </div>-->
</template>

<script>
import {defineAsyncComponent, ref} from 'vue';

export default {
  name: "AssignmentStudent",
  data() {
    return {
      path: '',
      color_personal: 'grey-5',
      color_group: 'grey-4',

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

      isPersonal: ref(true),
      isGroup : ref(false),
    }
  },
  methods: {
    buttonHandle() {
      const regex = /\/personal$/;
      const regex2 = /\/group$/;
      this.path = this.$route.path
      console.log(this.path)

      if (regex.test(this.path)) {
        const newpath = this.path.replace(/\/[^/]*$/, '/group')
        console.log("newpath", newpath)
        this.$router.push(newpath)
        this.color_group = 'grey-5'
        this.color_personal = 'grey-4'
      } else if (regex2.test(this.path)) {
        const newpath = this.path.replace(/\/[^/]*$/, '/personal')
        console.log("newpath", newpath)
        this.$router.push(newpath)
        this.color_group = 'grey-4'
        this.color_personal = 'grey-5'
      } else {
        const newpath = this.path.replace(/\/[^/]*$/, '')
        console.log("newpath", newpath)
        this.$router.push(newpath)
        this.color_group = 'grey-4'
        this.color_personal = 'grey-5'
      }

      this.isPersonal = !this.isPersonal
      this.isGroup = !this.isGroup
    }
  },
  components: {
    AssignmentTable: defineAsyncComponent(() => import('src/components/Component_Li/table/assignmentTable.vue')),
    AssignmentsDetail: defineAsyncComponent(() => import('src/components/Component_Li/special/assignmentsDetail.vue')),
  }
}
</script>

<style scoped>

</style>
