<template>
  <div class="q-pa-md q-gutter-sm bg-page">
    <q-separator />

    <div class="bg-table-list">
      <q-banner inline-actions class="text-black bg-listcolor">
        <q-input
          dense
          input-class="text-left"
          color="teal"
          class="col text-white"
          v-model="currentInput"
          :readonly="tags.size > 0"
          :placeholder="tags.size > 0 ? '' : 'enter to search'"
          @keyup.enter="addTag"
          @row-dblclick="handleRowDbclick"
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
            <q-icon v-if="currentInput === ''" name="search" />
            <q-icon
              v-else
              name="clear"
              class="cursor-pointer"
              @click="currentInput = ''"
            />
          </template>
        </q-input>
        <template v-slot:action>
          <p class="q-px-sm" />
          <q-btn unelevated @click="removeRow()" color="red" label="Remove" />
          <p class="q-px-sm" />
          <q-btn
            unelevated
            @click="onNewClickAction()"
            color="black"
            label="ADD"
          />
        </template>
      </q-banner>
      <q-table
        :rows="data"
        :columns="columns"
        row-key="title"
        selection="multiple"
        v-model:selected="selected"
        v-model:pagination="pagination"
        :hide-selected-banner="hideSelectedBanner"
        :loading="loading"
        flat
      >
        <template v-slot:header="props">
          <q-tr :props="props">
            <q-th auto-width />
            <q-th v-for="col in props.cols" :key="col.name" :props="props">
              {{ col.label }}
            </q-th>
          </q-tr>
        </template>

        <template v-slot:body="props">
          <q-tr :props="props">
            <q-td>
              <q-checkbox v-model="props.selected" />
            </q-td>
            <q-td key="title" :props="props">
              <span>{{ props.row.title }}</span>
              <q-popup-edit
                v-model="props.row.title"
                title="Update title"
                buttons
                v-slot="scope"
              >
                <q-input type="text" v-model="scope.value" dense autofocus />
              </q-popup-edit>
            </q-td>
            <q-td key="content" :props="props">
              <span>{{ props.row.content.substring(0, 10) }}</span>
              <q-popup-edit buttons v-model="props.row.content" v-slot="scope">
                <q-editor
                  v-model="scope.value"
                  min-height="5rem"
                  autofocus
                  @keyup.enter.stop
                />
              </q-popup-edit>
            </q-td>
            <q-td key="creatorName" :props="props">
              <span>{{ props.row.creatorName }}</span>
            </q-td>
            <q-td key="createTime" :props="props">
              <span>{{ formatDateString(props.row.createTime) }}</span>
            </q-td>
            <q-td auto-width>
              <q-btn
                size="sm"
                color="accent"
                round
                dense
                @click="props.expand = !props.expand"
                :icon="props.expand ? 'remove' : 'add'"
              />
            </q-td>
          </q-tr>
          <q-tr v-show="props.expand" :props="props">
            <q-td colspan="100%">
              <div class="text-left" style="font-size: 20px">
                This is the detailed content for row above:
                {{ props.row.content }}.
              </div>
            </q-td>
          </q-tr>
        </template>
      </q-table>
      <q-separator v-if="data.length > 0" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { formatDateString, useProjectId } from "src/composables/usefulFunction";
import { onMounted, ref, watch } from "vue";
import { defaultNotice, noticeProps } from "src/composables/comInterface";
import { api } from "boot/axios";
import { useQuasar } from "quasar";
import { useRouter } from "vue-router";

const router = useRouter();
const data = ref<noticeProps[]>([defaultNotice]);
const loading = ref(true),
  selected = ref([]),
  show_detail = ref(false);
const hideSelectedBanner = ref(true);
const columns = [
  {
    name: "title",
    required: true,
    label: "title",
    align: "left",
    field: (row:noticeProps) => row.title,
    format: (val: any) => `${val}`,
    sortable: true,
  },
  {
    name: "content",
    required: true,
    label: "content",
    align: "left",
    field: (row: noticeProps) => row.content,
    format: (val: any) => `${val}`,
    sortable: true,
  },
  {
    name: "creatorName",
    required: true,
    label: "creator",
    align: "left",
    field: (row: noticeProps) => row.creatorName,
    format: (val: any) => `${val}`,
    sortable: true,
  },
  {
    name: "createTime",
    required: true,
    label: "create time",
    align: "left",
    field: (row : noticeProps) => row.createTime,
    format: (val: any) => `${val}`,
    sortable: true,
  },
];
const pagination = ref({
  page: 1,
  rowsPerPage: 10,
});

watch(pagination, (newVal, oldVal) => {
  if (
    newVal.page !== oldVal.page ||
    newVal.rowsPerPage !== oldVal.rowsPerPage
  ) {
    onRefresh();
  }
});

async function created() {
  await onRefresh();
}

function handleRowDbclick(evt, row, index) {
  show_detail.value = true;
}

async function beforeRouteUpdate(to, from, next) {
  console.info("beforeRouteUpdate");
  await onRefresh();
  next();
}

const projectID = ref(-1);
onMounted(() => {
  projectID.value = useProjectId();
  onRefresh();
});

async function onRefresh() {
  loading.value = true;
  let search = "";
  if (tags.value.size > 0) search = tags.value.keys().next().value;
  let project_id = Number.isNaN(projectID.value) ? -1 : projectID.value;
  if (search === "") {
    api
      .get(
        `/notice-list/${project_id}/${pagination.value.page - 1}/${
          pagination.value.rowsPerPage
        }`
      )
      .then((res) => {
        data.value = res.data.body;
        loading.value = false;
      })
      .catch((err) => {
        console.log("err", err);
      });
  } else {
    api
      .get(
        `/notice-list/${project_id}/${pagination.value.page - 1}/${
          pagination.value.rowsPerPage
        }/${search}`
      )
      .then((res) => {
        data.value = res.data.body;
        loading.value = false;
      })
      .catch((err) => {
        console.log("err", err);
      });
  }
}

function onRequestAction(value) {
  console.info("onRequestAction");
  console.info(value);
  tablePagination.rowsPerPage = value.rowsPerPage;
}

// function getQuery() {
//   let query = {};
//   for (let i = 0; i < this.queryColumns.length; i++) {
//     const queryColumn = this.queryColumns[i];
//     if (queryColumn.value && queryColumn.value.trim() !== "") {
//       query[queryColumn.name] = queryColumn.value;
//     }
//   }
//   console.info(query);
//   return query;
// }

function onNewClickAction() {
  const queryParams = {
    title: "",
    content: "",
    projected: "",
  };

  router.push({ path: "/announcements/new", query: queryParams });
}

function removeRow() {
  const selectedRows = [...selected.value];

  selectedRows.forEach((index) => {
    rows.splice(index, 1);
  });

  selected.value = [];
}

const tags = ref<Set<string>>(new Set());
const currentInput = ref<string>("");
const $q = useQuasar();
const emit = defineEmits(["update:tags"]);

function addTag() {
  if (currentInput.value) {
    if (tags.value.size > 3) {
      $q.notify({
        position: "top",
        message: "you can only add 4 tags",
        color: "warning",
      });
    } else if (tags.value.has(currentInput.value)) {
      $q.notify({
        position: "top",
        message: "you have the same condition",
        color: "positive",
      });
    } else {
      tags.value.add(currentInput.value);
      currentInput.value = "";
      onRefresh();
    }
  }
}

function removeTag(index: string) {
  tags.value.delete(index);
  onRefresh();
}
</script>
