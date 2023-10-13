<template>
  <q-page class="q-pa-sm">
    <div class="row q-col-gutter-sm justify-center">
      <div class="col-lg-8 col-md-8 col-xs-12 col-sm-12">
        <q-card class="bg-blue-4 no-shadow">
          <q-card-section>
            <div class="text-h6 text-accent">Profile-{{ personIdentity }}</div>
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
                <q-item-section class="col-3">
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
                <q-item-section class="col-3">
                  <q-item-label>
                    <q-avatar icon="person_pin"/>
                    Name
                  </q-item-label>
                </q-item-section>
                <q-item-section>
                  <q-input v-model="username"
                           borderless
                           v-if="isEditing"
                  >
                  </q-input>
                  <q-item-label v-else>{{ username }}</q-item-label>
                </q-item-section>
              </q-item>
              <q-item class="col-12">
                <q-item-section class="col-3">
                  <q-item-label>
                    <q-avatar icon="male" v-if="gender==='Male'"/>
                    <q-avatar icon="female" v-else-if="gender==='Female'"/>
                    <q-avatar icon="transgender" v-else-if="gender==='Non-binary'"/>
                    <q-avatar icon="help_outline" v-else/>
                    Gender
                  </q-item-label>
                </q-item-section>
                <q-item-section>
                  <q-option-group v-if="isEditing"
                                  v-model="gender"
                                  :options="genderList"
                                  inline
                  >

                  </q-option-group>
                  <q-item-label v-else>{{ gender }}</q-item-label>
                </q-item-section>
              </q-item>
              <q-item class="col-12">
                <q-item-section class="col-3">
                  <q-item-label>
                    <q-avatar icon="email"/>
                    Email
                  </q-item-label>
                </q-item-section>
                <q-item-section>
                  <q-input
                           v-model="email"
                           type="email"
                           v-if="isEditing"
                           borderless
                           label="Your New Email"
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
                  <q-item-label v-else>{{ email }}</q-item-label>
                </q-item-section>
              </q-item>
              <q-item class="col-12">
                <q-item-section class="col-3">
                  <q-item-label>
                    <q-avatar icon="phone"/>
                    Phone
                  </q-item-label>
                </q-item-section>
                <q-item-section>
                  <q-input borderless
                           v-model="phone"
                           type="tel"
                           color="white"
                           label="New Phone Number"
                           v-if="isEditing">
                  </q-input>
                  <q-item-label v-else>{{ phone }}</q-item-label>
                </q-item-section>
              </q-item>
            </q-form>
          </q-card-section>

          <q-card-section v-if="person_id===userid">
            <div>
              <q-btn
                label="Edit"
                v-if="!isEditing"
                @click="isEditing = true"
                color="primary"
              />
              <q-btn
                label="Save"
                v-else
                color="green"
                @click="saveProfile"
              />
              <q-btn
                class="q-mx-md"
                label="Cancel"
                v-if="isEditing"
                color="red"
                @click="cancelEdit"/>
            </div>

          </q-card-section>
        </q-card>
      </div>
      <PasswordCard></PasswordCard>
    </div>

  </q-page>


</template>

<script setup>
import {useUserStore} from 'src/composables/useUserStore';
import {ref} from 'vue';
import {useQuasar} from 'quasar';
import {useCurrentPageUser} from 'stores/user-store';
import {storeToRefs} from "pinia";
import PasswordCard from "components/PersonIndex/PasswordCard.vue";

const usePerson = useCurrentPageUser()

const $q = useQuasar()
const {username, userid} = useUserStore()

const {person_id} = storeToRefs(usePerson)
const email = ref(''), gender = ref(''), phone = ref('')
const avatarUrl = ref('https://cdn.quasar.dev/img/avatar2.jpg'), uploadUrl = ref('')
const previewAvatarUrl = ref('https://cdn.quasar.dev/img/avatar2.jpg')

const isEditing = ref(false), isShowDialog = ref(false)
const selectedEmailDomain = ref('gmail.com')
const emailDomains = ref(['@gmail.com', '@yahoo.com', '@outlook.com', '@qq.com', '@sustech.edu.cn',
  '@mail.sustech.edu.cn'])
const personIdentity = ref('')
const genderList = ref([{label: 'Male', value: 'Male'}, {label: 'Female', value: 'Female'},
  {label: 'Non-binary', value: 'Non-binary'}, {label: 'Unknown', value: 'Unknown'}])

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

function onRejectUploader(rejectedEntries) {
  $q.notify({
    type: 'warning',
    position: 'center',
    message: `You can only upload 1 image`
  })
}
</script>

<style scoped>

</style>
