<template>
  <div>
    <div id="q-app" style="">
      <div class="q-pa-md" style="">
        <div class="justify-between row">
          <q-card class="my-card" style="width: 700px;left: 0px;min-height: 200px">
            <q-item>
              <q-item-section avatar>
                <q-avatar>
                  <img src="../../../assets/iKun.jpg">
                </q-avatar>
              </q-item-section>

              <q-item-section>
                <q-item-label style="font-weight: bolder; font-size: x-large">Assignment Title</q-item-label>
                <q-item-label caption style="font-weight: bold;font-size: large">Instructure</q-item-label>
              </q-item-section>
              <q-item-section>
                <span v-if="AssignmentDetail.state===2" style="font-weight: 1000;color: #3CA278;font-size: large">-- Returned</span>
                <span v-else-if="AssignmentDetail.state===1" style="font-weight: 1000;color: #E4AE1B;font-size: large">-- Waiting check</span>
                <span v-else-if="AssignmentDetail.state===0" style="font-weight: 1000;color: #C10015;font-size: large">-- Not Submitted</span>
                <span v-else style="font-weight: 1000;color: #C10015;font-size: large">-- Dev</span>
              </q-item-section>
            </q-item>

            <q-item>
              <q-item-section>
            <pre style="font-family: 'Microsoft YaHei UI';font-weight: bold">Student Name:{{
                "      " + AssignmentDetail.studentName
              }}</pre>
                <pre style="font-family: 'Microsoft YaHei UI';font-weight: bold">Submit Time:{{
                    "        " + AssignmentDetail.submitTime
                  }}</pre>
                <pre style="font-family: 'Microsoft YaHei UI';font-weight: bold">Grade:{{
                    "                   " + AssignmentDetail.grade
                  }}</pre>
                <pre style="font-family: 'Microsoft YaHei UI';font-weight: bold">Deadline:{{
                    "              " + AssignmentDetail.deadLine
                  }}</pre>
                <pre style="font-family: 'Microsoft JhengHei';font-weight: bolder;font-size: large">More Info:</pre>
                <pre style="font-family: 'Microsoft YaHei UI';font-weight: normal">{{ AssignmentDetail.moreInfo }}</pre>

              </q-item-section>
            </q-item>
          </q-card>

          <q-card class="my-card" style="min-width: 540px;left: 0px;min-height: 120px;">
            <q-item>
              <q-item-section>
                <q-item-label style="font-weight: bolder;font-size: x-large">Assignment attachment</q-item-label>
                <q-item-label caption style="font-weight: bold">Submit time: {{
                    this.AssignmentDetail.submitTime
                  }}
                </q-item-label>
              </q-item-section>
            </q-item>

            <q-item>
              <q-item-section>
                <div v-for="attachment in AssignmentAttachment" :key="attachment" class="attachment-item">
                  <pre style="font-family: 'Microsoft YaHei UI'"><a :href="attachment.fileAddress">{{
                      attachment.fileName
                    }}</a>          <button>按钮</button>
                  </pre>
                </div>
              </q-item-section>
            </q-item>

            <q-banner></q-banner>
            <!--      下半部分-->
            <q-item>
              <q-item-section>
                <q-item-label style="font-weight: bolder;font-size: x-large">Here to Submit</q-item-label>
              </q-item-section>
            </q-item>
            <q-item>
              <q-item-section>
                <q-uploader
                  label="Max number of files (3)"
                  max-files="3"
                  multiple
                  style="width: 100%"
                  url="http://localhost:4444/upload"
                  @rejected="onRejected"
                ></q-uploader>
              </q-item-section>
            </q-item>
            <q-item>
              <q-item-section>
                <q-editor
                  v-model="editorInput" placeholder="Type your description here..."
                  :definitions="{bold: {icon:bold, tip: '彩蛋被你发现了!'}}">
                </q-editor>
              </q-item-section>
            </q-item>
          </q-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {defineComponent} from 'vue';
import {useUserStore} from "src/composables/useUserStore";


export default defineComponent({
  name: "AssignmentDetail",
  data() {
    return {
      userData: useUserStore(),
      editorInput:'',
    }
  },
  methods: {},
  props: {
    AssignmentDetail: {
      required: true,
      default: {
        AssignmentName: 'Assignment 1',
        studentName: "Liwehao",
        submitTime: '2020-10-7',
        deadLine: '2020-10-10',
        instructor: 'Qi-Kun Xue1',
        grade: '100',
        matGrade: '100',
        isReturned: true,
        moreInfo: 'Dev',
        state: 0,
      }
    },
    AssignmentAttachment: {
      required: true,
      default: [
        {
          fileName: 'Dev',
          fileAddress: 'www.baidu.com',
        },
        {
          fileName: 'Assignment 2',
          fileAddress: 'www.Google.com',
        }
      ]
    }
  }
})
</script>

<style scoped>

</style>
