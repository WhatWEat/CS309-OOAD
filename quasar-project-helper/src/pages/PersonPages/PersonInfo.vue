<template>
  <div class="q-pa-md">
    <q-card>
      <q-card-section>
        <div class="text-h6">Profile</div>
      </q-card-section>

      <q-card-section>
        <q-form>
          <q-input
            v-model="userid"
            prefix="Student ID：&nbsp;&nbsp;&nbsp;"
            readonly
            filled
          >
            <template v-slot:prepend>
              <q-icon name="badge" />
            </template>
          </q-input>
          <q-input
            v-model="username"
            prefix="Name： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            :readonly="!isEditing"
            filled
          >
            <template v-slot:prepend>
              <q-icon name="person_pin" />
            </template>
          </q-input>
          <q-input filled
                   v-model="email"
                   type="email"
                   :readonly="!isEditing"
                   prefix="Email： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;">
            <template v-slot:prepend>
              <q-icon name="mail" />
            </template>
            <template v-slot:append>
              <q-select
                dense
                flat
                rounded
                v-model="selectedEmailDomain"
                :options="emailDomains"
                :readonly="!isEditing"
                class="q-mt-md"
              />
            </template>
          </q-input>
        </q-form>
      </q-card-section>

      <q-card-section>
        <q-btn
          label="Edit"
          v-if="!isEditing"
          @click="isEditing = true"
        />
        <q-btn
          label="Save"
          v-else
          @click="saveProfile"
        />
      </q-card-section>
    </q-card>
  </div>
</template>

<script setup>
import {useUserStore} from 'src/composables/useUserStore';
import {ref} from 'vue';
const {username, userid} = useUserStore()
const isEditing = ref(false)
const email = ref('')
const selectedEmailDomain = ref('gmail.com')
const emailDomains = ref(['gmail.com', 'yahoo.com', 'outlook.com'])
function saveProfile() {
  isEditing.value = false
}
</script>

<style scoped>

</style>
