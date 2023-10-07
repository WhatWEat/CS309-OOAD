<template>
  <div class="q-pa-md row justify-center">
    <q-card style="width: 50%">
      <q-card-section>
        <div class="text-h6">Profile-{{ personIdentity }}</div>
      </q-card-section>

      <q-card-section>
        <q-form>

          <div class="row justify-center q-pb-md">
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
          <q-input
            v-model="person_id"
            prefix="ID：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            readonly
            filled
          >
            <template v-slot:prepend>
              <q-icon name="badge"/>
            </template>
          </q-input>
          <q-input
            v-model="username"
            prefix="Name： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            :readonly="!isEditing"
            filled
          >
            <template v-slot:prepend>
              <q-icon name="person_pin"/>
            </template>
          </q-input>
          <q-select filled prefix="Gender： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                    transition-show="scale"
                    transition-hide="scale"
                    v-model="gender"
                    :options="genderList"
                    :readonly="!isEditing">
            <template v-slot:prepend>
              <q-icon name="male" v-if="gender==='Male'"/>
              <q-icon name="female" v-else-if="gender==='Female'"/>
              <q-icon name="transgender" v-else-if="gender==='Non-binary'"/>
              <q-icon name="help_outline" v-else/>
            </template>
          </q-select>
          <q-input filled
                   v-model="email"
                   type="email"
                   :readonly="!isEditing"
                   :suffix="selectedEmailDomain"
                   prefix="Email： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;">
            <template v-slot:prepend>
              <q-icon name="mail"/>
            </template>
            <template v-slot:append>
              <q-btn-dropdown dense flat :disable="!isEditing">
                <q-list>
                  <q-item clickable v-close-popup v-for="(item, index) in emailDomains" :key="index"
                          @click="selectEmailDomain(index)">
                    <q-item-section>
                      <q-item-label>{{ item }}</q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>
              </q-btn-dropdown>
            </template>
          </q-input>
          <q-input filled
                   v-model="phone"
                   type="tel"
                   :readonly="!isEditing"
                   prefix="Phone： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;">
            <template v-slot:prepend>
              <q-icon name="phone"/>
            </template>
          </q-input>

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
</template>

<script setup>
import {useUserStore} from 'src/composables/useUserStore';
import {ref} from 'vue';
import {useQuasar} from 'quasar';
import {useCurrentPageUser} from 'stores/user-store';
import {storeToRefs} from "pinia";
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
const genderList = ref(['Male', 'Female', 'Non-binary', 'Unknown'])

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
