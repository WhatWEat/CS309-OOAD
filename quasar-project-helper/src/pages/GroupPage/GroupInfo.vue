<template>
  <q-toolbar class="bg-primary text-white rounded-borders">
    <q-btn
      flat
      label="GroupInfo"
    ></q-btn>

    <q-toolbar-title></q-toolbar-title>
    <q-btn-group push>
      <q-btn color="blue" icon="timeline" label="Modify" push></q-btn>
      <q-btn color="blue" icon="visibility" label="Copy" push></q-btn>
      <q-btn color="red" icon="update" label="Delete" push @click="confirm = true"></q-btn>
    </q-btn-group>
  </q-toolbar>

  <p></p>

  <div>
    <q-input v-model="detailInfo[0]" label="GroupId" outlined>
      <template v-slot:before>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-icon name="fingerprint"></q-icon>
        <q-card-section align="center" class="font-card">
          {{ info[0] }}
        </q-card-section>
      </template>
    </q-input>
    <p></p>

    <q-input v-model="detailInfo[1]" label="GroupSize" outlined>
      <template v-slot:before>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-icon name="pin"></q-icon>
        <q-card-section align="center" class="font-card">
          {{ info[1] }}
        </q-card-section>
      </template>
    </q-input>
    <p></p>


    <q-input v-model="detailInfo[2]" label="GroupMember" outlined>
      <template v-slot:before>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-icon name="groups"></q-icon>
        <q-card-section align="center" class="font-card">
          {{ info[2] }}
        </q-card-section>
      </template>
    </q-input>
    <p></p>


    <q-input v-model="detailInfo[3]" label="Instructor" outlined>
      <template v-slot:before>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-icon name="person"></q-icon>
        <q-card-section align="center" class="font-card">
          {{ info[3] }}
        </q-card-section>
      </template>
    </q-input>
    <p></p>


    <q-input v-model="detailInfo[4]" label="ProjectName" outlined>
      <template v-slot:before>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-icon name="drive_file_rename_outline"></q-icon>
        <q-card-section align="center" class="font-card">
          {{ info[4] }}
        </q-card-section>
      </template>
    </q-input>
    <p></p>


    <q-input v-model="detailInfo[5]" label="deadLine" outlined>
      <template v-slot:before>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-icon name="event"></q-icon>
        <q-card-section align="center" class="font-card">
          {{ info[5] }}
        </q-card-section>
      </template>
    </q-input>
    <p></p>


    <q-input v-model="detailInfo[6]" label="MoreInfo" outlined>
      <template v-slot:before>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-space></q-space>
        <q-icon name="mail"></q-icon>
        <q-card-section align="center" class="font-card">
          {{ info[6] }}
        </q-card-section>
      </template>
    </q-input>
  </div>

  <div>
    <q-dialog v-model="confirm" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar color="red" icon="warning"  text-color="white"></q-avatar>
          <span class="q-ml-sm">Are you sure to delete the group?</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn v-close-popup color="primary" flat label="Cancel"></q-btn>
          <q-btn v-close-popup color="red" flat label="Delete"></q-btn>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>


</template>

<script>
export default {
  name: "GroupInfo",
  data() {
    return {
      info: [
        'Group     ID',
        'Group   Size',
        'Group Member',
        ' Instructor ',
        'Project Name',
        '  deadLine  ',
        '  MoreInfo  '
      ],
      detailInfo: [
        "Group     ID",
        "Group   Size",
        "Group Member",
        " Instructor ",
        "Project Name",
        "  deadLine  ",
        "  MoreInfo  "
      ],
      confirm: false
    }
  }
}
</script>

<style scoped>

</style>
