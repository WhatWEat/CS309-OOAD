<template>
  <div class="q-pa-md q-gutter-sm bg-page">

    <q-separator/>

    <div class="bg-table-list">
      <q-banner inline-actions class="text-black bg-listcolor">
        <q-input
          dense
          input-class="text-left"
          color="teal"
          class="col bg-primary text-white"
          standout="bg-blue-6 text-white"
          v-model="currentInput"
          :readonly="tags.size>0"
          :placeholder="tags.size > 0 ? '' :'enter to search'"
          @keyup.enter="addTag"
        >
          <template v-slot:prepend>
            <q-chip
              v-for="(tag, index) in tags"
              :key="index"
              removable
              dense
              square
              class="bg-blue-8 text-white"
              @remove="removeTag(tag)"
            >
              {{ tag }}
            </q-chip>
          </template>
          <template v-slot:append>
            <q-icon v-if="currentInput === ''" name="search"/>
            <q-icon v-else name="clear" class="cursor-pointer" @click="currentInput = ''"/>
          </template>
        </q-input>
        <template v-slot:action>
          <p class="q-px-sm"/>
          <q-btn
            :disable="selected.length == 0"
            unelevated
            @click="onDeleteClickAction()"
            color="negative"
            label="批量删除"
          />
          <p class="q-px-sm"/>
          <q-btn
            unelevated
            @click="onNewClickAction()"
            color="primary"
            label="添加"
          />
        </template>

      </q-banner>
      <q-table
        :rows="data"
        :columns="columns"
        row-key="noticeId"
        selection="multiple"
        :selected="selected"
        v-model:pagination="pagination"
        :loading="loading"
        flat>
        <template v-slot:body="props">
          <q-tr :props="props">
            <q-td>
              <q-checkbox v-model="props.selected"/>
            </q-td>
            <q-td key="dataClickAction" :props="props">
              <q-btn
                unelevated
                @click="onDeleteClickAction(props.row.id)"
                color="negative"
                label="删除"
                flat
                dense
              ></q-btn>
              <q-btn
                unelevated
                @click="onEditClickAction(props.row.id)"
                color="primary"
                label="编辑"
                flat
                dense
              ></q-btn>
            </q-td>
            <q-td key="title" :props="props">
              <span>{{ props.row.title }}</span>
            </q-td>
            <q-td key="content" :props="props">
              <span>{{ props.row.content }}</span>
            </q-td>
            <q-td key="createName" :props="props">
              <span>{{ props.row.creatorName }}</span>
            </q-td>
            <q-td key="createTime" :props="props">
              <span>{{ formatDateString(props.row.createTime) }}</span>
            </q-td>
          </q-tr>
        </template>
      </q-table>
      <q-separator v-if="data.length > 0"/>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {formatDateString} from "src/composables/usefulFunction"
import {onMounted, ref, watch} from 'vue';
import {defaultNotice, noticeProps} from "src/composables/comInterface";
import {api} from "boot/axios"
import {useQuasar} from "quasar";
const data = ref<noticeProps[]>([defaultNotice]);
const loading = ref(true), selected = ref([]), dataSource = ref(''), dataSourceUrl = ref(''), queryColumns = ref([])
const columns = [
  {
    name: "dataClickAction",
    align: "center",
    label: "op",
    field: "dataClickAction",
    sortable: true
  },
  {
    name: "title",
    required: true,
    label: "title",
    align: "left",
    field: row => row.title,
    format: val => `${val}`,
    sortable: true
  },
  {
    name: "content",
    required: true,
    label: "content",
    align: "left",
    field: row => row.content,
    format: val => `${val}`,
    sortable: true
  },
  {
    name: "createName",
    required: true,
    label: "creator",
    align: "center",
    field: row => row.createName,
    format: val => `${val}`,
    sortable: true
  },
  {
    name: "createTime",
    required: true,
    label: "create time",
    align: "left",
    field: row => row.createTime,
    format: val => `${val}`,
    sortable: true
  },
]
const pagination = ref({
  page: 1,
  rowsPerPage: 10,
})

watch(pagination, (newVal, oldVal)=>{
  if (newVal.page !== oldVal.page || newVal.rowsPerPage !== oldVal.rowsPerPage){
    onRefresh();
  }
})
async function created() {
  await this.init()
  await this.onRefresh();
}


async function beforeRouteUpdate(to, from, next) {
  console.info('beforeRouteUpdate');
  await this.init(to.params.dataSource);
  await this.onRefresh();
  next();
}

function dateFormat(value) {
  return date.dateTimeFormat(value);
}

function seqTypeFormat(value) {
  if (value === "STRING") {
    return "字符串";
  } else if (value === "LONG") {
    return "数字";
  } else if (value === "GUID") {
    return "GUID";
  } else {
    return value;
  }
}
onMounted(()=>{
  onRefresh()
})
async function onRefresh() {
  loading.value = true
  let search = ''
  if (tags.value.size > 0) search = tags.value[0];
  api.get(`/notice-list/-1/${pagination.value.page-1}/${pagination.value.rowsPerPage}/${search} `).then((res) => {
    data.value = res.data.body;
    loading.value = false

  }).catch((err) => {
    console.log('err', err)
  })

  selected.value = [];
}

function onRequestAction(value) {
  console.info("onRequestAction");
  console.info(value);
  this.tablePagination.rowsPerPage = value.rowsPerPage;
  this.fetchFromServer();
}

function getQuery() {
  let query = {};
  for (let i = 0; i < this.queryColumns.length; i++) {
    const queryColumn = this.queryColumns[i];
    if (queryColumn.value && queryColumn.value.trim() !== "") {
      query[queryColumn.name] = queryColumn.value
    }
  }
  console.info(query);
  return query;
}

function onQueryClickAction() {
  this.onRefresh();
}

function onResetClickAction() {
  console.info("onResetClickAction");
  this.search = "";
  for (let i = 0; i < this.queryColumns.length; i++) {
    this.queryColumns[i].value = "";
  }
  this.onRefresh();
}

function onNewClickAction() {
  this.$router.push("/",);
}

function onEditClickAction(id) {
  this.$router.push("/");
}


async function onDeleteClickAction(id) {
  let ids = [];
  this.selected.forEach(function (val) {
    ids.push(val.id);
  });
  console.info(JSON.stringify(ids));

  try {
    this.$q
      .dialog({
        title: "删除",
        message: "确认删除吗？",
        ok: {
          unelevated: true
        },
        cancel: {
          color: "negative",
          unelevated: true
        },
        persistent: false
      })
      .onOk(async () => {
        // if (id) {
        //   await metadataSequenceService.delete(this.dataSource, id);
        // } else {
        //   await metadataSequenceService.batchDelete(this.dataSource, ids);
        // }

        this.$q.notify("删除成功");
        this.onRefresh();
      })
      // .onCancel(() => {})
      .onDismiss(() => {
        console.info("I am triggered on both OK and Cancel");
      });
  } catch (error) {
    this.$q.notify("删除失败");
  }
}

const tags = ref<Set<string>>(new Set());
const currentInput = ref<string>('');
const $q = useQuasar();
const emit = defineEmits(['update:tags'])

function addTag() {
  if (currentInput.value) {
    if (tags.value.size > 3) {
      $q.notify({
          position: "top",
          message: "you can only add 4 tags",
          color: "warning"
        }
      )
    } else if (tags.value.has(currentInput.value)) {
      $q.notify({
          position: "top",
          message: "you have the same condition",
          color: "positive"
        }
      )
    } else {
      tags.value.add(currentInput.value);
      currentInput.value = '';
      onRefresh();
    }
  }
}

function removeTag(index: string) {
  tags.value.delete(index);
  onRefresh();
}


async function init(dataSource) {
  console.info("init");
  this.$store.commit(
    "stores/mutation.js",
    this.$route.meta.isAllowBack
  );

  this.selected = [];
  this.search = "";
  this.dataSource = dataSource || this.$route.params.dataSource;
  this.dataSourceUrl = "/dataSource/" + this.dataSource;

}
</script>
