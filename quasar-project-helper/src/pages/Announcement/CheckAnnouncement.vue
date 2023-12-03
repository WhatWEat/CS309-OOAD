<template>
  <div class="q-pa-md q-gutter-sm bg-page">
    <q-separator/>

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
            <q-icon v-if="currentInput === ''" name="search"/>
            <q-icon
              v-else
              name="clear"
              class="cursor-pointer"
              @click="currentInput = ''"
            />
          </template>
        </q-input>
        <template v-slot:action>
          <p class="q-px-sm"/>
          <q-btn unelevated @click="removeRow()" color="red" label="Remove"
                 v-if="identity<=2 && identity>=0"/>
          <p class="q-px-sm"/>
          <q-btn
            v-if="identity<=2 && identity>=0"
            unelevated
            @click="onNewClickAction()"
            color="green"
            label="ADD"
          />

        </template>
      </q-banner>
      <q-table
        :grid="$q.screen.lt.sm"
        :rows="data"
        :columns="columns"
        row-key="noticeId"
        selection="multiple"
        v-model:selected="selected"
        v-model:pagination="pagination"
        :hide-selected-banner="hideSelectedBanner"
        :loading="loading"
        flat
      >
        <template v-slot:header="props">
          <q-tr :props="props">
            <q-th auto-width/>
            <q-th v-for="col in props.cols" :key="col.name" :props="props">
              {{ col.label }}
            </q-th>
          </q-tr>
        </template>

        <template v-slot:item="props">
          <div
            class="q-py-xs col-12 grid-style-transition"
            :style="props.selected ? 'transform: scale(0.95);' : ''"
          >
            <q-card :class="props.selected ? 'bg-grey-2' : ''">
              <q-card-section class="row items-center">
                <q-checkbox dense v-model="props.selected" :label="props.row.name"/>
                <q-space>
                </q-space>
                <q-btn flat size="sm" icon="edit" @click="openEdit(props.row)"
                       v-if="identity <= 1 && identity >= 0 || props.row.creatorId === userid"/>
                <q-btn flat size="sm" icon="expand_more" @click="openPhoneView(props.row)"/>
              </q-card-section>
              <q-separator/>
              <q-list dense>
                <q-item v-for="col in props.cols.filter(col => col.name !== 'createTime')"
                        :key="col.name">
                  <q-item-section>
                    <q-item-label>{{ col.label }}</q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <q-item-label caption>
                      <div v-html="truncate(col.value,20)">
                      </div>
                    </q-item-label>
                  </q-item-section>
                </q-item>
                <q-item>
                  <q-item-section>
                    <q-item-label> create Time</q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <q-item-label caption>
                      {{ formatDateString(props.row.createTime) }}
                    </q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </q-card>
          </div>
        </template>
        <template v-slot:body="props">
          <q-tr :props="props">
            <q-td>
              <q-checkbox v-model="props.selected"/>
            </q-td>
            <q-td key="title" :props="props">
              <span>{{ truncate(props.row.title, 10) }}</span>
              <q-popup-edit
                v-model="props.row.title"
                title="Update title"
                @save="save(props.row)"
                v-if="identity <= 1 && identity >= 0 || props.row.creatorId === userid"
                buttons
                v-slot="scope"
              >
                <q-input type="text" v-model="scope.value" dense autofocus
                         @keyup.enter="scope.set"/>
              </q-popup-edit>
            </q-td>
            <q-td key="content" :props="props">
              <div v-html="truncate(props.row.content)"></div>
              <q-popup-edit
                buttons
                title="Update content"
                v-model="props.row.content"
                @save="save(props.row)"
                v-if="identity <= 1 && identity >= 0 || props.row.creatorId === userid"
                v-slot="scope">
                <q-input type="text" v-model="scope.value" dense autofocus
                         @keyup.enter="scope.set"/>
              </q-popup-edit>

            </q-td>
            <q-td key="creatorName" :props="props">
              <span>{{ props.row.creatorName }}</span>
            </q-td>
            <q-td key="createTime" :props="props">
              <span>{{ formatDateString(props.row.createTime) }}</span>
            </q-td>
            <q-td key="noticeId" auto-width>
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
              <q-card flat>
                <q-item>
                  <q-item-section avatar>
                    <q-btn round flat size="sm" :to="`/person/${props.row.creatorId}`">
                      <q-avatar size="md">
                        <q-img :src="avatarMap.get(props.row.creatorId)"></q-img>
                      </q-avatar>
                    </q-btn>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label>{{ props.row.title }}</q-item-label>
                    <q-item-label caption>
                      {{ props.row.creatorName }}
                    </q-item-label>
                  </q-item-section>
                  <q-item-section avatar
                                  v-if="identity <= 1 && identity >= 0 || props.row.creatorId === userid">
                    <q-btn @click="openEdit(props.row)" round flat size="md" icon="edit">
                    </q-btn>
                  </q-item-section>
                </q-item>

                <q-separator/>
                <q-card-section style="font-size: 20px">
                  <div v-html="props.row.content"></div>
                </q-card-section>
                <q-separator/>
                <q-card-section v-if="checkNoticeType(props.row, 0)">
                  <q-item v-if="statusDictionary[props.row.status] !== undefined">
                    <q-item-section>
                      <q-item-label class="text-h5">status</q-item-label>
                    </q-item-section>
                    <q-item-section side>
                      <q-item-label caption>
                        <q-chip square>
                          <q-icon :name="`${statusDictionary[props.row.status][2]}`"
                                  :color="`${statusDictionary[props.row.status][1]}`"/>
                          <span
                            :class="`text-weight-bold text-${statusDictionary[props.row.status][1]}`">{{ statusDictionary[props.row.status][0] }}</span>
                        </q-chip>
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                  <q-item v-if="checkNoticeType(props.row, 1)" class="q-gutter-lg">
                    <q-btn round flat color="green" icon="done"
                           @click="clickAcceptGroup(props.row)"/>
                    <q-btn round flat color="red" icon="close"
                           @click="clickRejectGroup(props.row)"/>
                  </q-item>
                </q-card-section>
              </q-card>
            </q-td>
          </q-tr>
        </template>
      </q-table>

      <q-separator v-if="data.length > 0"/>
    </div>
    <q-dialog v-model="isNewDialogOpen">
      <AddAnnouncement @save="onRefresh" :edit="false"/>
    </q-dialog>
    <q-dialog v-model="isEditDialogOpen">
      <AddAnnouncement @save="onRefresh" :edit="true" :notice="selectedNotice"/>
    </q-dialog>
    <q-dialog v-model="isPhoneView">
      <q-card flat style="height: 30vh; width: 80vh;">
        <q-item>
          <q-item-section avatar>
            <q-btn round flat size="sm" :to="`/person/${phoneViewNotice!.creatorId}`">
              <q-avatar size="md">
                <q-img :src="avatarMap.get(phoneViewNotice!.creatorId)"></q-img>
              </q-avatar>
            </q-btn>
          </q-item-section>
          <q-item-section>
            <q-item-label>{{ phoneViewNotice!.title }}</q-item-label>
            <q-item-label caption>
              {{ phoneViewNotice!.creatorName }}
            </q-item-label>
          </q-item-section>
        </q-item>

        <q-separator/>
        <q-card-section style="font-size: 25px">
          <div v-html="phoneViewNotice!.content"></div>
        </q-card-section>
        <q-separator/>
        <q-card-section v-if="checkNoticeType(phoneViewNotice!, 0)">
          <q-item>
            <q-item-section>
              <q-item-label class="text-h5">status</q-item-label>
            </q-item-section>
            <q-item-section side>
              <q-item-label caption>
                <q-chip square>
                  <q-icon :name="`${statusDictionary[phoneViewNotice!.status][2]}`"
                          :color="`${statusDictionary[phoneViewNotice!.status][1]}`"/>
                  <span
                    :class="`text-weight-bold text-${statusDictionary[phoneViewNotice!.status][1]}`">{{ statusDictionary[phoneViewNotice!.status][0] }}</span>
                </q-chip>
              </q-item-label>
            </q-item-section>
          </q-item>
          <q-item v-if="checkNoticeType(phoneViewNotice!, 1)" class="q-gutter-lg">
            <q-btn round flat color="green" icon="done"
                   @click="clickAcceptGroup(phoneViewNotice!)"/>
            <q-btn round flat color="red" icon="close" @click="clickRejectGroup(phoneViewNotice!)"/>
          </q-item>
        </q-card-section>

      </q-card>
    </q-dialog>
  </div>
</template>

<script lang="ts" setup>
import {
  formatDateString,
  getAvatarUrlById,
  truncate,
  useProjectId
} from "src/composables/usefulFunction";
import {onMounted, ref, watch} from "vue";
import {defaultNotice, noticeProps} from "src/composables/comInterface";
import {api} from "boot/axios";
import {useQuasar} from "quasar";
import {useUserStore} from "src/composables/useUserStore";
import AddAnnouncement from "components/AnnouncementsList/addAnnouncement.vue";

const $q = useQuasar();
const data = ref<noticeProps[]>([defaultNotice]);
const loading = ref(true),
  selected = ref<noticeProps[]>([]),
  show_detail = ref(false);
const hideSelectedBanner = ref(true);
const columns = [
  {
    name: "title",
    required: true,
    label: "title",
    align: "left",
    field: (row: noticeProps) => row.title,
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
    field: (row: noticeProps) => row.createTime,
    format: (val: any) => `${val}`,
    sortable: true,
  },
];

// 分页
function testProps(row: noticeProps) {
  console.log(row)
}

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
// 获取身份,avatar
// TODO TA无法发消息,改端点
const {userid, identity} = useUserStore(),
  avatarMap = ref<Map<number, string | null>>(new Map<number, string | null>());

// 同意进组
function clickAcceptGroup(notice: noticeProps) {
  let noticeId = notice.noticeId, type = notice.type;
  switch (type) {
    case 2:
      api.post('/stu/ack_invitation', {
        object: noticeId
      }).then(res => {
        console.log(res)
        $q.notify({
          position: 'top',
          message: 'accept success',
          color: 'positive'
        })
        notice.status = 1
      }).catch(err => {
        $q.notify({
          position: 'top',
          message: 'accept failed, please try again',
          color: 'negative'
        })
        onRefresh();
        console.log(err)
      })
      break
    case 1:
      api.post('/stu/ack_application', {
        object: noticeId
      }).then(res => {
        console.log(res)
        $q.notify({
          position: 'top',
          message: 'accept success',
          color: 'positive'
        })
        notice.status = 1
      }).catch(err => {
        $q.notify({
          position: 'top',
          message: 'accept failed, please try again',
          color: 'negative'
        })
        onRefresh();
        console.log(err)
      })
      break
  }
  // onRefresh();
}

function clickRejectGroup(notice: noticeProps) {
  let noticeId = notice.noticeId
  api.post('/stu/nak_invitation_or_application', {
    object: noticeId
  }).then(res => {
    console.log(res)
    $q.notify({
      position: 'top',
      message: 'reject success',
      color: 'danger'
    })
    notice.status = 2
  }).catch(err => {
    console.log(err)
  })
  // onRefresh();
  console.log('reject')
}

// 保存
function save(notice: noticeProps) {
  api.put('/tea/modify_notice', notice).then(() => {
    $q.notify({
      position: 'top',
      message: 'save success',
      color: 'positive'
    })
  }).catch(err => {
    console.log(err)
  })
}

// 删除操作
function removeRow() {
  const selectedRows = [...selected.value.map((row) => row.noticeId)];
  api.post('/tea/delete_notice', selectedRows).then(() => {
    $q.notify({
      position: 'top',
      message: 'delete success',
      color: 'negative'
    })
  }).catch(err => {
    console.log(err)
  })
  onRefresh();
  selected.value = [];
}

function handleRowDbclick() {
  show_detail.value = true;
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
        pagination.value.rowsPerPage == 0 ? 9999 : pagination.value.rowsPerPage
      }`
    )
    .then(async (res) => {
      data.value = res.data.body;
      console.log(data.value)
      for (const notice of data.value) {
        if (!avatarMap.value.has(notice.creatorId)) {
          avatarMap.value.set(notice.creatorId, await getAvatarUrlById(notice.creatorId));
        }
      }
      loading.value = false;
    })
    .catch((err) => {
      console.log("err", err);
    });
  } else {
    api
    .get(
      `/notice-list/${project_id}/${pagination.value.page - 1}/${
        pagination.value.rowsPerPage == 0 ? 9999 : pagination.value.rowsPerPage
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

// 打开新建框
const isNewDialogOpen = ref(false);

function onNewClickAction() {
  isNewDialogOpen.value = true;
  console.log('open dialog', isNewDialogOpen.value)
}

// 打开编辑框
const isEditDialogOpen = ref(false);
const selectedNotice = ref<noticeProps>();

function openEdit(notice: noticeProps) {
  selectedNotice.value = JSON.parse(JSON.stringify(notice));
  isEditDialogOpen.value = true;
}

// 打开手机查看
const isPhoneView = ref(false), phoneViewNotice = ref<noticeProps>();

function openPhoneView(notice: noticeProps) {
  phoneViewNotice.value = JSON.parse(JSON.stringify(notice));
  isPhoneView.value = true;
}

// 判断信息类型
const statusDictionary: { [key: number]: [string, string, string] } = {
  0: ["Undecided", "blue", "hourglass_top"],
  1: ["Accepted", "green", "done"],
  2: ["Rejected", "red", "close"],
  "-1": ["Expired", "orange", "hourglass_disabled"]
};

function checkNoticeType(notice: noticeProps, type: number) {
  switch (type) {
    case 0:
      return notice.type !== 0;
    case 1:
      return notice.type === 1 && notice.status === 0 || notice.type === 2 && notice.status === 0;
    default:
      return false;
  }
}

// 搜索tags
const tags = ref<Set<string>>(new Set());
const currentInput = ref<string>("");
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
