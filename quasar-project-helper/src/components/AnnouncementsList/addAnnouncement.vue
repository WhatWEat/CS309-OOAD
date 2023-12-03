<template>
  <q-card>
    <q-card-section class="row justify-center">
      <q-list style="width: 700px; max-width: 80vw">
        <q-expansion-item
          header-class="text-h6"
          default-opened
          label="Notice Information"
          group="someGroup"
        >
          <q-card>
            <q-card-section class="row q-pa-sm">
              <q-item class="col-lg-3 col-md-3 col-sm-3 col-xs-12">
                <q-item-section class="text-h5"> Title</q-item-section>
              </q-item>
              <q-item class="col-lg-9 col-md-9 col-sm-9 col-xs-12">
                <q-item-section>
                  <q-input dense outlined v-model="noticeProps.title"/>
                </q-item-section>
              </q-item>
              <q-item class="col-12">
                <q-item-section class="text-h5"> Content</q-item-section>
              </q-item>
              <q-item class="col-12">
                <q-item-section>
                  <q-editor v-model="noticeProps.content" min-height="5rem"/>
                </q-item-section>
              </q-item>
            </q-card-section>
          </q-card>
        </q-expansion-item>
        <q-separator></q-separator>
        <q-expansion-item
          header-class="text-h6"
          label="To Whom"
          group="someGroup"
        >
          <q-card>
            <q-card-section>
              <q-item>
                <q-item-section class="text-h6">
                  To Which Project ?
                </q-item-section>
                <q-item-section side>
                  <q-select
                    v-model="project"
                    :options="filterProject"
                    option-value="projectId"
                    option-label="name"
                    map-options
                    use-input
                    input-debounce="0"
                    style="width: 200px"
                    @filter="filterFn"
                  />
                  <template v-slot:no-option>
                    <q-item>
                      <q-item-section class="text-grey">
                        No results
                      </q-item-section>
                    </q-item>
                  </template>
                </q-item-section>
              </q-item>
              <q-item>
                <q-item-section class="text-h6"> To All ?</q-item-section>
                <q-item-section side>
                  <q-toggle v-model="noticeProps.toAll"/>
                </q-item-section>
              </q-item>
              <q-card-section class="row" v-if="!noticeProps.toAll">
                  <div class="col-6">
                    <q-input v-model="filter" placeholder="Search Students"/>
                    <q-list dense style="max-height: 200px; overflow-y: auto;">
                      <q-item
                        v-for="person in filteredPeople"
                        :key="person"
                        clickable
                        @click="moveToRight(person)"
                      >
                        <q-item-section class="row">
                                          <span>
                                            <q-chip square dense class="col-3"
                                                    color="blue-4"> {{ person }}</q-chip>
                                            <span class="col">
                                              {{ person_list_map.get(person) }}
                                            </span>
                                          </span>
                        </q-item-section>
                      </q-item>
                    </q-list>
                  </div>
                  <div class="col-6" style="height: 250px; overflow-y: auto;">
                    <q-list dense>
                      <q-item
                        v-for="person in selectedPeople"
                        :key="person"
                        clickable
                        @click="moveToLeft(person)"
                      >
                        <q-item-section class="row">
                                          <span>
                                            <q-chip square dense class="col-3 text-white"
                                                    color="primary"> {{ person }}</q-chip>
                                            <span class="col">
                                              {{ person_list_map.get(person) }}
                                            </span>
                                          </span>
                        </q-item-section>
                      </q-item>
                    </q-list>
                  </div>
              </q-card-section>
              </q-card-section>
          </q-card>
        </q-expansion-item>
      </q-list>
    </q-card-section>
    <q-card-actions align="right">
      <q-btn flat label="Cancel" color="primary" v-close-popup/>
      <q-btn
        flat
        label="Save"
        color="positive"
        @click="saveInfo"
        v-close-popup
      />
    </q-card-actions>
  </q-card>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {
  defaultNotice,
  noticeProps, personProps,
  projectProps,
} from "src/composables/comInterface";
import {api} from "boot/axios";
import {useQuasar} from "quasar";
import {useUserStore} from "src/composables/useUserStore";
import {computed, watchEffect} from "vue-demi";

const $q = useQuasar();
const noticeProps = ref<noticeProps>(defaultNotice);
const props = defineProps<{
  edit: boolean;
  notice?: noticeProps;
}>();
// 可见性和内容的初始化
const projectOptions = ref<projectProps[]>([]),
  project = ref<projectProps>(),
  isLoadingProject = ref(false);

const {userid} = useUserStore();
onMounted(() => {
  watchEffect(() => {
    if (userid.value !== -1 && !isLoadingProject.value) {
      isLoadingProject.value = true;
      api.get(`/project-list/0/100/${userid.value}`).then((res) => {
        projectOptions.value = res.data.body;
        isLoadingProject.value = false;
      });
    }
  });
  if (props.edit) {
    noticeProps.value = props.notice!;
  } else {
    noticeProps.value.stuView = [];
    noticeProps.value.toAll = true;
  }
});
// 响应式获取project中的人数
const person_id_list = ref<number[]>([]), person_name_list = ref<string[]>([]),
  person_list_map = ref<Map<number, string>>(new Map<number, string>());
watchEffect(async () => {
  if (project.value?.projectId) {
    api.get(`/tea/stu-list/${project.value.projectId}`).then((res) => {
      person_id_list.value = res.data.body.key;
      person_name_list.value = res.data.body.value;
      person_id_list.value.forEach((v, i) => {
        person_list_map.value.set(v, person_name_list.value[i]);
      });
    });
  }
})
// 选择可见的人
const filter = ref<string>(''), selectedPeople = ref<number[]>([]);
const filteredPeople = computed(() => {
  // const num = Number(filter.value);
  return person_id_list.value.filter(
    person => (person.toString().includes(filter.value) ||
      (person_list_map.value.get(person)?.includes(filter.value) ?? false)) &&
      !selectedPeople.value.some(selected => selected === person
    ));
});

const moveToRight = (userId: number) => {
  const person = person_id_list.value.find(p => p === userId);
  if (person && !selectedPeople.value.some(p => p === userId)) {
    selectedPeople.value.push(person);
  }
};

const moveToLeft = (userId: number) => {
  const index = selectedPeople.value.findIndex(p => p === userId);
  if (index !== -1) {
    selectedPeople.value.splice(index, 1);
  }
};

const filterProject = ref<projectProps[]>([]);

function filterFn(val: string, update: (arg0: projectProps[]) => void) {
  if (val === "") {
    filterProject.value = projectOptions.value;
    update(filterProject.value);
    return;
  }
  const needle = val.toLowerCase();
  filterProject.value = projectOptions.value.filter(
    (v) => v.name.toLowerCase().indexOf(needle) > -1
  );
  update(
    projectOptions.value.filter(
      (v) => v.name.toLowerCase().indexOf(needle) > -1
    )
  );
}

// 保存
const emit = defineEmits(["save"]);
function saveInfo() {
  if (props.edit) {
    console.log("save");
    console.log(props);
    api
    .put("/tea/modify_notice", props)
    .then((res) => {
      console.log(res);
      $q.notify({
        position: "top",
        message: "save success",
        color: "positive",
      });
    })
    .catch((err) => {
      console.log(err);
    });
  } else {
    api
    .post("/tea/post_notice", {
      title: noticeProps.value.title,
      content: noticeProps.value.content,
      projectId: project.value?.projectId,
      toAll: noticeProps.value.toAll,
      stuView: selectedPeople.value,
    })
    .then((res) => {
      $q.notify({
        position: "top",
        message: "release success",
        color: "positive",
      });
      console.log(res.data);
    });
  }
  emit("save");
}
</script>
