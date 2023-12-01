<template>
  <q-page class="flex flex-center">
    <div class="q-pa-md" style="width: 600px;">
      <q-input v-model="title" label="邮件标题" class="q-mb-md" />

      <div class="q-mb-md">
        <label for="recipients">收件人</label>
        <div class="q-gutter-sm q-mt-xs">
          <q-chip v-for="email in recipients" :key="email" removable @remove="removeRecipient(email)">
            {{ email }}
          </q-chip>
          <q-input
            filled
            v-model="recipientInput"
            dense
            class="inline"
            style="max-width: 300px;"
            @keyup.enter="addRecipient"
            placeholder="添加邮件地址"
          />
        </div>
      </div>

      <q-input
        v-model="content"
        label="邮件内容"
        type="textarea"
        class="q-mb-md"
      />

      <div class="row justify-around">
        <q-btn label="发送" color="primary" @click="sendEmail" />
        <q-btn label="接收" color="secondary" @click="receiveEmail" />
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const title = ref('');
const content = ref('');
const recipients = ref<string[]>([]);
const recipientInput = ref('');

const addRecipient = () => {
  if (recipientInput.value && !recipients.value.includes(recipientInput.value)) {
    recipients.value.push(recipientInput.value);
    recipientInput.value = '';
  }
};

const removeRecipient = (email: string) => {
  const index = recipients.value.indexOf(email);
  if (index > -1) {
    recipients.value.splice(index, 1);
  }
};

const sendEmail = () => {
  // 发送邮件的逻辑
};

const receiveEmail = () => {
  // 接收邮件的逻辑
};
</script>
