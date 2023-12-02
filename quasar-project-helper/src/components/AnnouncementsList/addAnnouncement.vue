<template>
  <q-card>
    <q-card-section class="row justify-center">
      <q-list style="width: 700px; max-width: 80vw;">
        <q-expansion-item
          header-class="text-h6"
          default-opened
          label="Notice Information"
          group="someGroup"
        >
          <q-card>
            <q-card-section class="row q-pa-sm">
              <q-item class="col-lg-3 col-md-3 col-sm-3 col-xs-12">
                <q-item-section class="text-h5">
                  Title
                </q-item-section>
              </q-item>
              <q-item class="col-lg-9 col-md-9 col-sm-9 col-xs-12">
                <q-item-section>
                  <q-input dense outlined v-model="noticeProps.title"/>
                </q-item-section>
              </q-item>
              <q-item class="col-12">
                <q-item-section class="text-h5">
                  Content
                </q-item-section>
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
                <q-item-section class="text-h6">
                  To All ?
                </q-item-section>
                <q-item-section side>
                  <q-toggle v-model="noticeProps.toAll"/>
                </q-item-section>
              </q-item>
            </q-card-section>
            <q-card-section class="row">
              <!--              <div class="col">-->
              <!--                <q-input v-model="filter" placeholder="Search TAs"/>-->
              <!--                <q-list dense style="max-height: 200px; overflow-y: auto;">-->
              <!--                  <q-item-->
              <!--                    v-for="person in filteredPeople"-->
              <!--                    :key="person.userId"-->
              <!--                    clickable-->
              <!--                    @click="moveToRight(person.userId)"-->
              <!--                  >-->
              <!--                    <q-item-section class="row">-->
              <!--                            <span>-->
              <!--                              <q-chip square dense class="col-3" color="blue-4"> {{person.userId}}</q-chip>-->
              <!--                              <span class="col">-->
              <!--                                {{ person.name }}-->
              <!--                              </span>-->
              <!--                            </span>-->
              <!--                    </q-item-section>-->
              <!--                  </q-item>-->
              <!--                </q-list>-->
              <!--              </div>-->
              <!--              <div class="col" style="height: 250px; overflow-y: auto;">-->
              <!--                <q-list dense>-->
              <!--                  <q-item-->
              <!--                    v-for="person in selectedPeople"-->
              <!--                    :key="person.userId"-->
              <!--                    clickable-->
              <!--                    @click="moveToLeft(person.userId)"-->
              <!--                  >-->
              <!--                    <q-item-section class="row">-->
              <!--                            <span>-->
              <!--                              <q-chip square dense class="col-3 text-white" color="primary"> {{person.userId}}</q-chip>-->
              <!--                              <span class="col">-->
              <!--                                {{ person.name }}-->
              <!--                              </span>-->
              <!--                            </span>-->
              <!--                    </q-item-section>-->
              <!--                  </q-item>-->
              <!--                </q-list>-->
              <!--              </div>-->
            </q-card-section>
          </q-card>
        </q-expansion-item>
      </q-list>


    </q-card-section>
    <q-card-actions align="right">
      <q-btn flat label="Cancel" color="primary" v-close-popup/>
      <q-btn flat label="Save" color="positive" @click="saveInfo" v-close-popup/>
    </q-card-actions>
  </q-card>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {defaultNotice, noticeProps, projectProps} from "src/composables/comInterface";
import {api} from "boot/axios";
import {useQuasar} from "quasar";

const $q = useQuasar();
const noticeProps = ref<noticeProps>(defaultNotice);
const props = defineProps<{
  edit: boolean;
  notice?: noticeProps;
}>();
// 可见性和内容的初始化
const projectOptions = ref<projectProps[]>([]), project = ref<projectProps>();
onMounted(() => {
  api.get(`/project-list/0/100`).then(res => {
    projectOptions.value = res.data.body;
  })
  if (props.edit) {
    noticeProps.value = props.notice!;
    api.get(`/get-notice-view/${props.notice?.noticeId}`).then(res => {
      noticeProps.value.stuView = res.data.body.stuView;
      noticeProps.value.toAll = res.data.body.toAll;
    })
  } else {
    noticeProps.value.stuView = [];
    noticeProps.value.toAll = true;
  }
})
const filterProject = ref<projectProps[]>([]);

function filterFn(val: string, update: (arg0: projectProps[]) => void) {
  if (val === '') {
    filterProject.value = projectOptions.value;
    update(filterProject.value);
    return;
  }
  console.log(val, 'val')
  const needle = val.toLowerCase();
  filterProject.value = projectOptions.value.filter(v => v.name.toLowerCase().indexOf(needle) > -1);
  console.log(filterProject.value)
  update(projectOptions.value.filter(v => v.name.toLowerCase().indexOf(needle) > -1));

}

// 选择学生可见
function saveInfo() {
  if (props.edit) {
    console.log('save')
    console.log(props)
    api.put('/tea/modify_notice', props).then(res => {
      console.log(res)
      $q.notify({
        position: 'top',
        message: 'save success',
        color: 'positive'
      })
    }).catch(err => {
      console.log(err)
    })
  } else {
    api.post('/tea/post_notice', {
      title: noticeProps.value.title,
      content: noticeProps.value.content,
      projectId: project.value?.projectId,
      toAll: true,
      stuView: [],
    }).then(res => {
      $q.notify({
        position: 'top',
        message: 'release success',
        color: 'positive'
      })
      console.log(res.data);
    })
  }
}
</script>
