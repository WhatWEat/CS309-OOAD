<template>
  <q-page padding>
    <q-form @submit="sendNotification">
      <q-input
        v-model="title"
        label="Title"
        outlined
        dense
        required
      ></q-input>
      <q-input
        v-model="content"
        label="Content"
        outlined
        dense
        required
      ></q-input>
      <q-input
        v-model="projected"
        label="Projected"
        outlined
        dense
        required
      ></q-input>
      <q-btn
        type="submit"
        label="Send Notification"
        color="primary"
        dense
        class="q-mt-md"
      ></q-btn>
    </q-form>
  </q-page>
</template>

<script>
export default {
  data() {
    return {
      title: '',
      content: '',
      projected: ''
    };
  },
  created() {
    const { title, content, projected } = this.$route.query;
    this.title = title || '';
    this.content = content || '';
    this.projected = projected || '';
  },
  methods: {
    sendNotification() {
      const notification = {
        title: this.title,
        content: this.content,
        projected: this.projected,
        timestamp: new Date().toISOString()
      };
      // 发送通知的代码
      console.log('Notification:', notification);
      // 清空输入字段
      this.title = '';
      this.content = '';
      this.projected = '';
    }
  }
};
</script>
