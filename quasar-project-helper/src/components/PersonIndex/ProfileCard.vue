<template>
  <div class="col-lg-7 col-md-8 col-xs-12 col-sm-12">
    <q-card :class="`${props.bg_color} rounded-xl`" >
      <q-card-section>
        <div class="text-h6">Profile-{{ personIdentity }}</div>
      </q-card-section>

      <q-card-section>
        <q-form class="row">
          <div class="row justify-center col-12">
            <div class=" q-pb-md">
              <q-btn  round @click="clickAvatar">
                <q-avatar size="50px">
                  <img v-if="avatar_preview" :src="avatar_preview">
                  <q-icon v-else name="person"></q-icon>
                </q-avatar>
              </q-btn>
              <q-dialog v-model="isShowDialog">
                <q-card>
                  <q-card-section>
                    <q-file
                      v-model="avatar_file"
                      label="Choose Avatar"
                      borderless
                      accept=".jpg, image/*"
                      @rejected="onRejected"
                      @update:model-value="onFileChange"
                    ></q-file>
                    <div v-if="avatar_preview" class="q-mt-md">
                      <q-img :src="avatar_preview" contain alt="Avatar Preview"
                             class="avatar-preview"/>
                    </div>
                  </q-card-section>
                  <q-card-actions class="q-px-md">
                    <q-btn label="Upload" color='green' @click="saveUploadAvatar"/>
                    <q-btn label="Cancel" color="red" @click="cancelUploadAvatar"/>
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
              <q-item-label v-else>{{ genderList[gender - 1].label }}</q-item-label>
            </q-item-section>
          </q-item>
          <q-item class="col-12" v-if="identity === 3">
            <q-item-section class="col-sm-3 col-xs-5">
              <q-item-label>
                <q-avatar icon="school"/>
                Skills
              </q-item-label>
            </q-item-section>
            <q-item-section class="col">
              <div>
                <q-chip
                  v-for="(skill, index) in skills"
                  :key="index"
                  :color="colorList[index % colorList.length]"
                  square
                  :disable="!isEditing"
                  :removable="isEditing"
                  @remove="removeSkill(index)"
                >
                  {{ skill }}
                </q-chip>
                <q-input
                  v-if="isEditing"
                  v-model="newSkill"
                  dense
                  placeholder="Add new tag"
                  @keyup.enter="addSkill"
                />
              </div>
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
                :suffix="$q.screen.lt.sm ? undefined : selectedEmailDomain"
                :label="$q.screen.gt.xs ? undefined : selectedEmailDomain">
                <template v-slot:append>
                  <q-btn-dropdown dense flat :disable="!isEditing" class="rounded-lg" >
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
              <q-item-label v-else-if="email==null || email==''"></q-item-label>
              <q-item-label v-else>{{ email }}{{ selectedEmailDomain }}</q-item-label>
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
            class="text-capitalize rounded-lg"
            v-if="!isEditing"
            @click="isEditing = true"
            color="primary"
          />
          <q-btn
            label="Save"
            class="q-mx-lg text-capitalize rounded-lg"
            v-else
            color="green"
            type="submit"
            @click="beforeSaveVerify"
          />
          <q-btn
            class="text-capitalize rounded-lg"
            label="Cancel"
            v-if="isEditing"
            color="red"
            @click="cancelEdit"/>
        </div>

      </q-card-section>
      <q-dialog v-model="isOpenVerify" persistent transition-show="scale" transition-hide="scale">
        <q-card style="width: 400px;" class="row">
          <q-item class="col-12">
            <q-item-section>
              <q-item-label class="text-h5">Verify</q-item-label>
              <q-item-label caption>
                We will send a verification code and please check it.
              </q-item-label>
            </q-item-section>
          </q-item>
          <q-separator/>
          <q-card-section class="col-12 row">
            <q-item v-if="saveVerifyType == 1 || saveVerifyType == 3">
              <q-item-section>
                <q-item-label>Your saved Phone: {{ phone }}</q-item-label>
              </q-item-section>
            </q-item>
            <q-input v-model="phone_code" dense outline class="col-12" label="Phone Code"
                     v-if="saveVerifyType == 1 || saveVerifyType == 3">
              <template v-slot:append>
                <q-btn
                  dense
                  flat
                  icon="send"
                  @click="sendCode"
                  v-if="countdown_phone === 0"
                />
                <div v-else>{{ countdown_phone }}s</div>
              </template>
            </q-input>
            <q-item v-if="saveVerifyType == 2 || saveVerifyType == 3">
              <q-item-section>
                <q-item-label>Your saved Email: {{ email }}{{ selectedEmailDomain }}</q-item-label>
              </q-item-section>
            </q-item>
            <q-input v-model="email_code" dense outline class="col-12" label="Email Code"
                     v-if="saveVerifyType == 2 || saveVerifyType == 3">
              <template v-slot:append>
                <q-btn
                  dense
                  flat
                  icon="send"
                  @click="sendCode"
                  v-if="countdown_email === 0"
                />
                <div v-else>{{ countdown_email }}s</div>
              </template>
            </q-input>
          </q-card-section>
          <q-card-actions>
            <q-btn label="Cancel" color="red" @click="isOpenVerify = false"/>
            <q-btn label="Save" color="green" @click="checkCode"/>
          </q-card-actions>
        </q-card>
      </q-dialog>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import {useUserStore} from 'src/composables/useUserStore';
import {getAvatarUrl, usePersonId} from 'src/composables/usefulFunction';
import {ref, defineProps, onMounted} from 'vue';
import {useQuasar} from 'quasar';
import {useCurrentPageUser} from 'stores/user-store';
import {storeToRefs} from 'pinia';
import {api} from 'boot/axios';
import {watchEffect} from "vue-demi";
import {defaultPerson, personProps} from "src/composables/comInterface";

const props = defineProps({
  bg_color: {
    required: false,
    default: 'bg-blue-1',
    type: String
  }
})
const usePerson = useCurrentPageUser()

const $q = useQuasar()
const {username, userid, identity} = useUserStore()

const person_id = ref(1);
const email = ref(''), gender = ref(4), phone = ref(''),
  skills = ref(['PHP', 'HTML', 'CSS', 'SQL', 'Go'])
const avatarUrl = ref(), avatar_file = ref(null), avatar_preview = ref<string | undefined>(''),
  avatar_clone = ref()
const newSkill = ref(), colorList = ref(['warning', 'teal', 'glossy', 'primary'])
const isEditing = ref(false), isShowDialog = ref(false), isFresh = ref(true)
const selectedEmailDomain = ref('@gmail.com')
const emailDomains = ref(['@gmail.com', '@yahoo.com', '@outlook.com', '@qq.com', '@sustech.edu.cn',
  '@mail.sustech.edu.cn'])
const personIdentity = ref('')
const genderList = ref([{label: 'male', value: 1}, {label: 'female', value: 2},
  {label: 'non-binary', value: 3}, {label: 'unknown', value: 4}])

//axios to initial
const personInfo = ref<personProps>(defaultPerson)
onMounted(() => {
  person_id.value = usePersonId();
  console.log('person_id', person_id.value)
  watchEffect(() => {
    personIdentity.value = (identity.value === 3) ? 'Student' : 'Teacher'
    if (identity.value !== -1 && isFresh.value) {
      // console.log("isfresh", isFresh.value)
      api.get(`/get_personal_info/${person_id.value}`).then((res) => {
        console.log(res.data)
        personInfo.value = res.data.body
        personInfo.value.gender = personInfo.value.gender.toLowerCase()
        copyPersonInfo()
        username.value = personInfo.value.name
        avatar_preview.value = personInfo.value.avatarPath
        avatar_clone.value = avatar_preview.value
        console.log('init', personInfo.value)
      })
    }
  })
})

function copyPersonInfo() {
  gender.value = Number(genderConvert(personInfo.value.gender))
  // console.log("gender",gender.value)
  // console.log("personInfo",personInfo.value)
  if (personInfo.value.phone == 'null') {
    phone.value = ''
  } else {
    phone.value = personInfo.value.phone
  }
  if (personInfo.value.programmingSkills)
    skills.value = personInfo.value.programmingSkills.slice()
  else
    skills.value = []
  if (personInfo.value.avatar) {
    avatarUrl.value = personInfo.value.avatar
  }
  avatar_preview.value = avatar_clone.value
  isFresh.value = false
  if (personInfo.value.email) {
    const emails = personInfo.value.email.split('@')
    email.value = emails[0]
    selectedEmailDomain.value = '@'+emails[1]
    console.log('email', email.value, selectedEmailDomain.value)
    console.log('from personInfo', personInfo.value.email)
  } else {
    email.value = ''
    selectedEmailDomain.value = '@sustech.edu.cn'
  }
}

// 校验手机号/邮箱有没有改，改的话需要验证
const saveVerifyType = ref(0), isOpenVerify = ref(false);

function beforeSaveVerify() {
  saveVerifyType.value = 0;
  if (phone.value !== personInfo.value.phone) {
    saveVerifyType.value = 1;
  }
  if (!(email.value == '' && personInfo.value.email == null ) && email.value + selectedEmailDomain.value !== personInfo.value.email) {
    saveVerifyType.value += 2;
  }
  if (saveVerifyType.value !== 0) {
    isOpenVerify.value = true;
    sendCode();
  } else {
    saveProfile();
  }
}

// 发送验证码
const countdown_phone = ref(0), countdown_email = ref(0);

function sendCode() {
  if (countdown_phone.value == 0 && (saveVerifyType.value == 1 || saveVerifyType.value == 3)) {
    // TODO 发送手机验证码
    api.post('/get_edit_code', {
      key: 1,
      value: phone.value
    }).then(() => {
      $q.notify({
        color: "green",
        textColor: "white",
        icon: "mail",
        message: "Code has been sent to your phone",
      });
      // 开始倒计时
      countdown_phone.value = 60;
      const interval = setInterval(() => {
        countdown_phone.value--;
        if (countdown_phone.value === 0) {
          clearInterval(interval);
        }
      }, 1000);
    }).catch(err => {
      if(err.response.data.statusCode === 405){
        $q.notify({
          color: "red",
          textColor: "white",
          icon: "mail",
          message: "Phone number has been registered",
        });
      } else {
        $q.notify({
          color: "red",
          textColor: "white",
          icon: "mail",
          message: "Please try again",
        });
      }
      console.log(err)
    })

  }
  if (countdown_email.value == 0 && (saveVerifyType.value == 2 || saveVerifyType.value == 3)) {
    // TODO 发送邮箱验证码
    api.post('/get_edit_code', {
      key: 2,
      value: email.value + selectedEmailDomain.value
    }).then(()=>{
      $q.notify({
        color: "green",
        textColor: "white",
        icon: "mail",
        message: "Code has been sent to your email",
      });
      // 开始倒计时
      countdown_email.value = 60;
      const interval = setInterval(() => {
        countdown_email.value--;
        if (countdown_email.value === 0) {
          clearInterval(interval);
        }
      }, 1000);
    }).catch(err => {
      if(err.response.data.statusCode === 405){
        $q.notify({
          color: "red",
          textColor: "white",
          icon: "mail",
          message: "Email has been registered",
        });
      } else {
        $q.notify({
          color: "red",
          textColor: "white",
          icon: "mail",
          message: "Please try again",
        });
      }
      console.log(err)
    })
  }
}

const email_code = ref(''), phone_code = ref('')

async function checkCode() {
  // TODO 检查验证码
  let flag = saveVerifyType.value;
  console.log('开始flag', flag)
  if (saveVerifyType.value == 1 || saveVerifyType.value == 3) {
    await api.post('/verify_edit_code', {
      key: 1,
      value: {
        key: phone.value,
        value: phone_code.value.toString()
      },
    }).then(res => {
      console.log('res phone', res.data)
      if (res.data.statusCode == 200) {
        flag -= 1;
        $q.notify({
          color: "green",
          textColor: "white",
          icon: "mail",
          message: "Phone code verified",
        });
      } else {
        $q.notify({
          color: "red",
          textColor: "white",
          icon: "mail",
          message: "Wrong phone code",
        });
      }
    }).catch(err => {
      $q.notify({
        color: "red",
        textColor: "white",
        icon: "mail",
        message: "Please try again",
      });
      console.log(err)
    })
  }
  if (saveVerifyType.value == 2 || saveVerifyType.value == 3) {
    await api.post('/verify_edit_code', {
      key: 2,
      value: {
        key: email.value + selectedEmailDomain.value,
        value: email_code.value.toString()
      },
    }).then(res => {
      console.log('res email', res.data)
      if (res.data.statusCode == 200) {
        flag -= 2;
        $q.notify({
          color: "green",
          textColor: "white",
          icon: "mail",
          message: "Email code verified",
        });
      } else {
        $q.notify({
          color: "red",
          textColor: "white",
          icon: "mail",
          message: "Wrong email code",
        });
      }
    }).catch(err => {
      $q.notify({
        color: "red",
        textColor: "white",
        icon: "mail",
        message: "Please try again",
      });
      console.log(err)
    })
  }
  console.log('最后flag', flag)
  saveVerifyType.value = flag;
  if (flag == 0) {
    console.log('保存了')
    isOpenVerify.value = false;
    saveProfile();
  }
}

function saveProfile() {
  isEditing.value = false
  const formData = new FormData();
  const type = personIdentity.value === 'Student' ? 'stu' : 'tea'
  const personSubmit = personInfo.value;
  personSubmit.gender = String(genderConvert(gender.value));
  personSubmit.phone = phone.value;
  personSubmit.programmingSkills = skills.value.slice();
  personSubmit.email = email.value + selectedEmailDomain.value;
  formData.append('phone', personSubmit.phone)
  formData.append('email', personSubmit.email)
  formData.append('name', personSubmit.name)
  formData.append('gender', personSubmit.gender)
  formData.append('birthday', '2023-11-12')
  console.log('sub的phone',personSubmit.phone);
  if (personSubmit.avatar)
    formData.append('avatar', personSubmit.avatar)
  for (const i of personSubmit.programmingSkills) {
    formData.append('programmingSkills', i)
  }
  console.log('submit', personInfo.value)
  api.post(`/edit_personal_info`, formData).then((res) => {
    console.log(res.data)
    $q.notify({
      type: 'positive',
      message: 'Profile saved'
    })
    getAvatarUrl()
    avatar_clone.value = avatar_preview.value
  }).catch((err) => {
    console.log(err)
    $q.notify({
      type: 'negative',
      message: 'Profile save failed'
    })
  })
  // isFresh.value = true
}

function cancelEdit() {
  isEditing.value = false
  copyPersonInfo()
}

function removeSkill(index: number) {
  skills.value.splice(index, 1)
}

function addSkill() {
  if (newSkill.value.trim()) {
    skills.value.push(newSkill.value.trim())
    newSkill.value = '';
  }
}

function genderConvert(gender: number | string) {
  switch (gender) {
    case 1:
      return "male";
    case 2:
      return "female";
    case 3:
      return "non-binary";
    case 4:
      return "unknown";
    case "male":
      return 1;
    case "female":
      return 2;
    case "non-binary":
      return 3;
    case "unknown":
      return 4;
  }
}

function selectEmailDomain(index: number) {
  selectedEmailDomain.value = emailDomains.value[index]
}

function clickAvatar() {
  if (isEditing.value) {
    isShowDialog.value = true
  }
}

function onFileChange() {
  const file = avatar_file.value
  if (file) {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => {
      avatar_preview.value = reader.result as string
    }
    reader.onerror = (error) => {
      console.log(error)
    }
  }
}

function cancelUploadAvatar() {
  isShowDialog.value = false;
  avatar_file.value = null;
  avatar_preview.value = avatar_clone.value;
}

function saveUploadAvatar() {
  isShowDialog.value = false;
  if (avatar_file.value) {
    personInfo.value.avatar = avatar_file.value;
    avatar_file.value = null;
  }
}

function onRejected(rejectedEntries: string | any[]) {
  // Notify plugin needs to be installed
  // https://quasar.dev/quasar-plugins/notify#Installation
  $q.notify({
    type: 'negative',
    message: `${rejectedEntries.length} file(s) isn't image`
  })
}
</script>

<style scoped>

</style>
