<template>
  <q-card class="bg-transparent no-shadow no-border" bordered v-if="loading">
      <q-card-section class="q-pa-none">
        <div class="row q-col-gutter-sm ">
          <div v-for="(item, index) in items" :key="index" class="col-md-3 col-sm-6 col-xs-12">
            <q-item :style="`background-color: ${item.color1}`" class="q-pa-none">
              <q-item-section v-if="icon_position === 'left'" side
                              :style="`background-color: ${item.color2}`"
                              class=" q-pa-lg q-mr-none text-white">
                <q-icon :name="item.icon" color="white" size="24px"></q-icon>
              </q-item-section>
              <q-item-section class=" q-pa-md q-ml-none  text-white">
                <q-item-label class="text-white text-h6 text-weight-bolder">{{
                    item.value
                  }}
                </q-item-label>
                <q-item-label>{{ item.title }}</q-item-label>
              </q-item-section>
              <q-item-section v-if="icon_position === 'right'" side class="q-mr-md text-white">
                <q-icon :name="item.icon" color="white" size="44px"></q-icon>
              </q-item-section>
            </q-item>
          </div>

        </div>
      </q-card-section>
  </q-card>
  <div v-else>
    <q-skeleton animation="pulse-x" height="60px">

    </q-skeleton>
  </div>
</template>

<script setup>
import {defineProps, watch, ref, onMounted} from 'vue';
import {useUserStore} from "src/composables/useUserStore";
import {useProjectId} from "src/composables/usefulFunction";
import {api} from "boot/axios";

const props = defineProps({
  icon_position: {
    required: false,
    default: 'left'
  }
});


const items = ref([])
const loading = ref(false)
const {userid, identity} = useUserStore()
const project_id = ref(1);
onMounted(() => {
  if (props.icon_position === 'left'){
    project_id.value = useProjectId();
  }
  watch(identity, async (newIdentity) =>{
    await handleItemList(newIdentity)
  })
  if (identity.value !== -1) {
    handleItemList(identity.value)
  }
})

async function handleItemList(newIdentity){
  if (newIdentity === -1) return
  let type = newIdentity === 3 ? 'stu' : 'tea';

  if (props.icon_position === 'left') {
    // project_id.value = useProjectId();
    const noticeResponse = await api.get(`/notice-list/${project_id.value}/0/1000`)
    // const assignmentResponse = await api.get(`/ass-list/${project_id.value}/0/1000`)
    // console.log('assi',assignmentResponse)
    const groupResponse = ref(0), groupDis = ref('Group Member');
    if(newIdentity === 3){
      // groupResponse.value = (await api.get(`/stu/group_members/${project_id.value}`)).data.body.length;
      groupResponse.value = 1;
    } else {
      groupDis.value = 'Group Number'
      groupResponse.value = (await api.get(`/get_brief_groups_from_proj/${project_id.value}`)).data.body.length;
    }

    items.value = [
      {
        title: 'Profile',
        icon: 'person',
        value: `${userid.value}`,
        color1: '#5064b5',
        color2: '#3e51b5'
      },
      {
        title: `${groupDis.value}`,
        icon: 'fa-solid fa-user-group',
        value: `${groupResponse.value}`,
        color1: '#f37169',
        color2: '#f34636'
      },
      {
        title: 'Assignment',
        icon: 'fa-solid fa-file',
        // value: `${assignmentResponse.data.body.length}`,
        value: `1`,
        color1: '#ea6a7f',
        color2: '#ea4b64'
      },
      {
        title: 'Announcements',
        icon: 'fa-solid fa-comments',
        value: `${noticeResponse.data.body.length}`,
        color1: '#f8a261',
        color2: '#f76b1c'
      }
    ]
  } else {
    const noticeResponse = await api.get(`/notice-list/-1/0/1000`)
    const assignmentResponse = await api.get(`/ass-list/-1/0/1000`)
    const projectResponse = await api.get(`/project-list/0/1000`)
    items.value = [
      {
        title: 'Profile',
        icon: 'person',
        value: `${userid.value}`,
        color1: '#5064b5',
        color2: '#3e51b5'
      },
      {
        title: 'Projects',
        icon: 'fa-solid fa-person-digging',
        value: `${projectResponse.data.body.length}`,
        color1: '#f37169',
        color2: '#f34636'
      },
      {
        title: 'Announcements',
        icon: 'fa-solid fa-envelope',
        value: `${noticeResponse.data.body.length}`,
        color1: '#ea6a7f',
        color2: '#ea4b64'
      },
      {
        title: 'Assignment',
        icon: 'fa-solid fa-file',
        value: `${assignmentResponse.data.body.length}`,
        color1: '#f8a261',
        color2: '#f76b1c'
      }
    ]
  }
  loading.value = true
}
</script>
