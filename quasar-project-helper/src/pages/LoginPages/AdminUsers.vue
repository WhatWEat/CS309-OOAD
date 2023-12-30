<template>
  <q-layout view="hHh lpR fFf">
    <q-header elevated>
      <q-toolbar>
        <q-toolbar-title>
          Project Helper
          <q-btn dense flat icon="home" @click="router.push('/')">
          </q-btn>
        </q-toolbar-title>
        <PersonBar></PersonBar>
      </q-toolbar>
    </q-header>

    <q-page-container>
  <div class="q-pa-md">

<!--    此处是创建-->
    <q-file
      color="purple-12"
      v-model="model1"
      label="upload file to create"
      @update:model-value="showDialog1"
    >
      <template v-slot:prepend>
        <q-icon name="attach_file" />
      </template>
    </q-file>
    <q-dialog v-model="isShowDialog1">
      <q-card>
        <q-card-section  class="confirm-text">
          Are you sure you want to upload the file?
        </q-card-section>
        <q-card-actions align="right">
          <q-btn label="Upload" color='green' @click="saveUploadAvatar1"/>
          <q-btn label="Cancel" color="red" @click="cancelUploadAvatar1"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-table
      title="Create"
      :rows="data1"
      :columns="columns"
      :filter="filter1"
      row-key="name"
      flat>
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td key="userId" :props="props">
            <span>{{ props.row.userId }}</span>
          </q-td>
          <q-td key="password" :props="props">
            <span>{{ props.row.password }}</span>
          </q-td>
        </q-tr>
      </template>
      <template v-slot:top-right>
        <q-input borderless dense debounce="300" v-model="filter1" placeholder="Search">
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>
      </template>
    </q-table>
    <q-separator v-if="data1.length > 0"/>

<!--    此处是重置-->
    <q-file
      color="purple-12"
      v-model="model2"
      label="upload file to reset"
      @update:model-value ="showDialog2"
    >
      <template v-slot:prepend>
        <q-icon name="attach_file" />
      </template>
    </q-file>
    <q-dialog v-model="isShowDialog2">
      <q-card>
        <q-card-section  class="confirm-text">
          Are you sure you want to upload the file?
        </q-card-section>
        <q-card-actions align="right">
          <q-btn label="Upload" color='green' @click="saveUploadAvatar2"/>
          <q-btn label="Cancel" color="red" @click="cancelUploadAvatar2"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-table
      title="Reset"
      :rows="data2"
      :columns="columns2"
      :filter="filter2"
      row-key="name"
      flat>
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td key="userId" :props="props">
            <span>{{ props.row.userId }}</span>
          </q-td>
          <q-td key="password" :props="props">
            <span>{{ props.row.password }}</span>
          </q-td>
        </q-tr>
      </template>
      <template v-slot:top-right>
        <q-input borderless dense debounce="300" v-model="filter2" placeholder="Search">
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>
      </template>
    </q-table>
    <q-separator v-if="data2.length > 0"/>


<!--    此处是冻结-->
    <q-file
      color="purple-12"
      v-model="model3"
      label="upload file to freeze"
      @update:model-value="showDialog3"
    >
      <template v-slot:prepend>
        <q-icon name="attach_file" />
      </template>
    </q-file>
    <q-dialog v-model="isShowDialog3">
      <q-card>
        <q-card-section  class="confirm-text">
          Are you sure you want to upload the file?
        </q-card-section>
        <q-card-actions align="right">
          <q-btn label="Upload" color='green' @click="saveUploadAvatar3"/>
          <q-btn label="Cancel" color="red" @click="cancelUploadAvatar3"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-table
      title="Freeze"
      :rows="data3"
      :columns="columns3"
      :filter="filter3"
      row-key="name"
      flat>
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td key="userId" :props="props">
            <span>{{ props.row.userId }}</span>
          </q-td>
        </q-tr>
      </template>
      <template v-slot:top-right>
        <q-input borderless dense debounce="300" v-model="filter3" placeholder="Search">
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>
      </template>
    </q-table>
    <q-separator v-if="data3.length > 0"/>

<!--    此处是解冻-->
    <q-file
      color="purple-12"
      v-model="model4"
      label="upload file to unfreeze"
      @update:model-value="showDialog4"
    >
      <template v-slot:prepend>
        <q-icon name="attach_file" />
      </template>
    </q-file>
    <q-dialog v-model="isShowDialog4">
      <q-card>
        <q-card-section  class="confirm-text">
          Are you sure you want to upload the file?
        </q-card-section>
        <q-card-actions align="right">
          <q-btn label="Upload" color='green' @click="saveUploadAvatar4"/>
          <q-btn label="Cancel" color="red" @click="cancelUploadAvatar4"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-table
      title="Unfreeze"
      :rows="data4"
      :columns="columns4"
      :filter="filter4"
      row-key="name"
      flat>
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td key="userId" :props="props">
            <span>{{ props.row.userId }}</span>
          </q-td>
        </q-tr>
      </template>
      <template v-slot:top-right>
        <q-input borderless dense debounce="300" v-model="filter4" placeholder="Search">
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>
      </template>
    </q-table>
    <q-separator v-if="data4.length > 0"/>
  </div>
    </q-page-container>
  </q-layout>
</template>

<script lang="ts" setup>
import { ref} from 'vue';
import {
  createProps,
   freezeProps, resetProps, unfreezeProps,
} from "src/composables/comInterface";
import {api} from "boot/axios"
import { useRouter } from 'vue-router'
import PersonBar from "components/Layout/PersonBar.vue";

const  router = useRouter()
const data1 = ref<createProps[]>([]);
const data2 = ref<resetProps[]>([]);
const data3 = ref<freezeProps[]>([]);
const data4 = ref<unfreezeProps[]>([]);
const filter1 = ref(''), filter2 = ref(''), filter3 = ref(''), filter4 = ref('')
const model1 = ref(null), model2 = ref(null), model3 = ref(null), model4 = ref(null)
const columns = [
  {
    name: "userId",
    required: true,
    label: "studentID",
    align: "left",
    field: row => row.userId,
    format: val => `${val}`,
    sortable: true
  },
  {
    name: "password",
    required: true,
    label: "password",
    align: "left",
    field: row => row.password,
    format: val => `${val}`
  },
]
const columns2 = [
  {
    name: "userId",
    required: true,
    label: "studentID",
    align: "left",
    field: row => row.userId,
    format: val => `${val}`,
    sortable: true
  },
  {
    name: "password",
    required: true,
    label: "password",
    align: "left",
    field: row => row.password,
    format: val => `${val}`
  },
]
const columns3 = [
  {
    name: "userId",
    required: true,
    label: "studentID",
    align: "left",
    field: row => row.userId,
    format: val => `${val}`,
    sortable: true
  },
]


const columns4 = [
  {
    name: "userId",
    required: true,
    label: "studentID",
    align: "left",
    field: row => row.userId,
    format: val => `${val}`,
    sortable: true
  },
]

const isShowDialog1 = ref(false);
const excel_file1 = ref();

function showDialog1() {
  isShowDialog1.value = true;
}

function saveUploadAvatar1() {
  isShowDialog1.value = false;
  if(model1.value){
    excel_file1.value = model1.value;
    let formdata = new FormData();
    formdata.append('file',excel_file1.value);
    api.post('/adm/create_multiple_users_with_file',formdata).then((res)=>{
      data1.value = res.data.body;
      data1.value.shift();
      console.log('data1',data1.value)
    }).catch((err)=>{
      console.log('err', err)
    })
  }
}

function cancelUploadAvatar1() {
  isShowDialog1.value = false;
  model1.value = null;
}

const isShowDialog2 = ref(false);
const excel_file2 = ref();

function showDialog2() {
  isShowDialog2.value = true;
}

function saveUploadAvatar2() {
  isShowDialog2.value = false;
  if(model2.value){
    excel_file2.value = model2.value;
    let formdata = new FormData();
    formdata.append('file',excel_file2.value);
    api.put('/adm/reset_pass_for_multiple_users',formdata).then((res)=>{
      data2.value = res.data.body;
      console.log('data2',data2.value);
    }).catch((err)=>{
      console.log('err', err);
    })
  }
}

function cancelUploadAvatar2() {
  isShowDialog2.value = false;
  model2.value = null;
}

const isShowDialog3 = ref(false);
const excel_file3 = ref();

function showDialog3() {
  isShowDialog3.value = true;
}

function saveUploadAvatar3() {
  isShowDialog3.value = false;
  if(model3.value){
    excel_file3.value = model3.value;
    let formdata = new FormData();
    formdata.append('file',excel_file3.value);
    api.put('/adm/freeze_multiple_users',formdata).then((res)=>{
      console.log('res',res.data.body)
      for(const i of res.data.body){
        data3.value.push({
          userId: i
        })
      }
      console.log('data3',data3.value);
    }).catch((err)=>{
      console.log('err', err);
    })
  }
}

function cancelUploadAvatar3() {
  isShowDialog3.value = false;
  model3.value = null;
}


const isShowDialog4 = ref(false);
const excel_file4 = ref();

function showDialog4() {
  isShowDialog4.value = true;
}

function saveUploadAvatar4() {
  isShowDialog4.value = false;
  if(model4.value){
    excel_file4.value = model4.value;
    let formdata = new FormData();
    formdata.append('file',excel_file4.value);
    api.put('/adm/unfreeze_multiple_users',formdata).then((res)=>{
      for(const i of res.data.body){
        data4.value.push({
          userId: i
        })
      }
      console.log('data4',data4.value);
    }).catch((err)=>{
      console.log('err', err);
    })
  }
}

function cancelUploadAvatar4() {
  isShowDialog4.value = false;
  model4.value = null;
}
</script>

<style>
.confirm-text {
  font-size: 20px; /* Adjust this value to your preference */
}
</style>
