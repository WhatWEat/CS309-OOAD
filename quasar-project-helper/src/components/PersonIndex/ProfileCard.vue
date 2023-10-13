<template>
  <div class="col-lg-6 col-md-8 col-xs-12 col-sm-12">
    <q-card :class="props.bg_color">
      <q-card-section>
        <div class="text-h6">Profile-{{ personIdentity }}</div>
      </q-card-section>

      <q-card-section>
        <q-form class="row">
          <div class="row justify-center col-12">
            <div class=" q-pb-md">
              <q-btn round @click="clickAvatar">
                <q-avatar size="50px">
                  <img v-if="avatarUrl" :src="avatarUrl">
                  <q-icon v-else name="person"></q-icon>
                </q-avatar>
              </q-btn>
              <q-dialog v-model="isShowDialog">
                <q-card>
                  <q-card-section>
                    <q-uploader
                      label="Upload Image"
                      :url="uploadUrl"
                      ref="uploader"
                      accept=".jpg, image/*"

                      @rejected="onRejectUploader"
                      max-files="1"
                    ></q-uploader>
                  </q-card-section>
                  <q-card-actions class="q-px-md">
                    <q-btn label="Upload" color='green' @click="uploadAvatar"/>
                    <q-btn label="Cancel" color="red" @click="isShowDialog = false"/>
                  </q-card-actions>
                </q-card>
              </q-dialog>
            </div>
          </div>
          <q-item class="col-12">
            <q-item-section class="col-sm-3 col-xs-5">
              <q-item-label>
                <q-avatar icon="badge"/>
                Person ID
              </q-item-label>
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ person_id }}</q-item-label>
            </q-item-section>
          </q-item>
          <q-item class="col-12">
            <q-item-section class="col-sm-3 col-xs-5">
              <q-item-label>
                <q-avatar icon="person_pin"/>
                Name
              </q-item-label>
            </q-item-section>
            <q-item-section>
              <q-input v-model="username"
                       outlined
                       dense
                       v-if="isEditing"
              >
              </q-input>
              <q-item-label v-else>{{ username }}</q-item-label>
            </q-item-section>
          </q-item>
          <q-item class="col-12">
            <q-item-section class="col-sm-3 col-xs-5">
              <q-item-label>
                <q-avatar icon="male" v-if="gender===1"/>
                <q-avatar icon="female" v-else-if="gender===2"/>
                <q-avatar icon="transgender" v-else-if="gender===3"/>
                <q-avatar icon="help_outline" v-else/>
                Gender
              </q-item-label>
            </q-item-section>
            <q-item-section>
              <q-option-group v-if="isEditing"
                              v-model="gender"
                              class="gt-xs"
                              :options="genderList"
                              inline
                              dense
              >

              </q-option-group>
              <q-slider
                class="lt-sm"
                v-model="gender"
                snap
                :min="0"
                :max="5"
                :inner-min="1"
                :inner-max="4"
                selection-color="transparent"
                markers
                marker-labels
                switch-marker-labels-side
                v-if="isEditing"
              >
                <template v-slot:marker-label-group="{ markerMap }">
                  <div
                    class="row items-center no-wrap"
                    :class="markerMap[gender].classes"
                    :style="markerMap[gender].style"
                  >
                    <div v-if="gender=== 1"> Male</div>
                    <div v-if="gender=== 2"> Female</div>
                    <div v-if="gender=== 3"> Non-binary</div>
                    <div v-if="gender=== 4"> Unknown</div>
                  </div>
                </template>
              </q-slider>
              <q-item-label v-else>{{ genderList[gender-1].label }}</q-item-label>
            </q-item-section>
          </q-item>
          <q-item class="col-12">
            <q-item-section class="col-sm-3 col-xs-5">
              <q-item-label>
                <q-avatar icon="email"/>
                Email
              </q-item-label>
            </q-item-section>
            <q-item-section>
              <q-input
                dense
                outlined
                v-model="email"
                type="email"
                v-if="isEditing"
                color="white"
                :suffix="selectedEmailDomain">
                <template v-slot:append>
                  <q-btn-dropdown dense flat :disable="!isEditing">
                    <q-list>
                      <q-item clickable v-close-popup v-for="(item, index) in emailDomains"
                              :key="index"
                              @click="selectEmailDomain(index)">
                        <q-item-section>
                          <q-item-label>{{ item }}</q-item-label>
                        </q-item-section>
                      </q-item>
                    </q-list>
                  </q-btn-dropdown>
                </template>
              </q-input>
              <q-item-label v-else>{{ email }}@{{ selectedEmailDomain }}</q-item-label>
            </q-item-section>
          </q-item>
          <q-item class="col-12">
            <q-item-section class="col-sm-3 col-xs-5">
              <q-item-label>
                <q-avatar icon="phone"/>
                Phone
              </q-item-label>
            </q-item-section>
            <q-item-section>
              <q-input outlined dense
                       v-model="phone"
                       type="tel"
                       color="white"
                       v-if="isEditing">
              </q-input>
              <q-item-label v-else>{{ phone }}</q-item-label>
            </q-item-section>
          </q-item>
        </q-form>
      </q-card-section>

      <q-card-section class="row justify-center" v-if="person_id===userid">
        <div>
          <q-btn
            label="Edit"
            class="text-capitalize"
            v-if="!isEditing"
            @click="isEditing = true"
            color="primary"
          />
          <q-btn
            label="Save"
            class="q-mx-lg text-capitalize"
            v-else
            color="green"
            @click="saveProfile"
          />
          <q-btn
            class="text-capitalize"
            label="Cancel"
            v-if="isEditing"
            color="red"
            @click="cancelEdit"/>
        </div>

      </q-card-section>
    </q-card>
  </div>
</template>

<script setup>
import {useUserStore} from 'src/composables/useUserStore';
import {ref, defineProps} from 'vue';
import {useQuasar} from 'quasar';
import {useCurrentPageUser} from 'stores/user-store';
import {storeToRefs} from 'pinia';
import {watchEffect} from "vue-demi";

const props = defineProps({
  bg_color: {
    required: false,
    default: 'bg-blue-1',
    type: String
  }
})
const usePerson = useCurrentPageUser()

const $q = useQuasar()
const {username, userid} = useUserStore()

const {person_id} = storeToRefs(usePerson)
const email = ref('123123123'), gender = ref(1), phone = ref('1331313')
const avatarUrl = ref('https://cdn.quasar.dev/img/boy-avatar.png'), uploadUrl = ref('')

const isEditing = ref(false), isShowDialog = ref(false)
const selectedEmailDomain = ref('gmail.com')
const emailDomains = ref(['@gmail.com', '@yahoo.com', '@outlook.com', '@qq.com', '@sustech.edu.cn',
  '@mail.sustech.edu.cn'])
const personIdentity = ref('')
const genderList = ref([{label: 'Male', value: 1}, {label: 'Female', value: 2},
  {label: 'Non-binary', value: 3}, {label: 'Unknown', value: 4}])
const firstModel = ref(2)

personIdentity.value = 'Student'

function saveProfile() {
  isEditing.value = false
}

function cancelEdit() {
  isEditing.value = false
}

function selectEmailDomain(index) {
  selectedEmailDomain.value = emailDomains.value[index]
}

function clickAvatar() {
  if (isEditing.value) {
    isShowDialog.value = true
  }
}

function uploadAvatar() {
  isShowDialog.value = false;
}

function onRejectUploader() {
  $q.notify({
    type: 'warning',
    position: 'center',
    message: 'You can only upload 1 image'
  })
}
</script>

<style scoped>

</style>
