<template>
  <div>
    <div id="q-app" style="">
      <div class="q-pa-md" style="">
        <div class="justify-between row">
          <q-card class="my-card" style="width: 800px;left: 0px;min-height: 200px">
            <q-item>
              <q-item-section avatar>
                <q-avatar>
                  <img src="../../assets/iKun.jpg">
                </q-avatar>
              </q-item-section>

              <q-item-section>
                <q-item-label style="font-weight: bolder; font-size: x-large">Assignment Title</q-item-label>
                <q-item-label caption style="font-weight: bold;font-size: large">Instructure</q-item-label>
              </q-item-section>
              <q-item-section>
                <span v-if="AssignmentDetail.isReturned" style="font-weight: 1000;color: #C10015;font-size: large">-- Returned</span>
                <span v-else style="font-weight: 1000;color: #C10015;font-size: large">-- Not Return</span>
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
            <q-banner></q-banner>
            <q-item>
              <q-item-section>
                <q-item-label style="font-weight: bolder;font-size: x-large">Here to Submit</q-item-label>
              </q-item-section>
            </q-item>

            <q-item>
              <q-item-section>
                <q-uploader
                  style="width: 100%"
                  url="http://localhost:4444/upload"
                  label="Max number of files (3)"
                  multiple
                  max-files="3"
                  @rejected="onRejected"
                ></q-uploader>
              </q-item-section>
            </q-item>
          </q-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AssignmentDetail",
  data(){
    return{
      //该部分是插槽所需要的数据
      AssignmentDetail: {
        AssignmentName: 'Assignment 1',
        studentName: "Liwehao",
        submitTime: '2020-10-7',
        deadLine: '2020-10-10',
        instructor: 'Qi-Kun Xue1',
        grade: '100',
        matGrade: '100',
        isReturned: true,
        moreInfo: 'Course Assignment 4:\n' +
          '\n' +
          'Chapter 4：Exercise 2，Exercise 8，and Exercise 22\n' +
          '\n' +
          'Deadline: Please submit your homework through the Sakai system before 10:20AM on April 11, 2023.  \n' +
          '\n' +
          'Please note that you have only one chance to submit your homework in the SAKAI system.\n' +
          '\n' +
          'No re-submission is allowed.\n' +
          '\n' +
          'No late submission is allowed.',
      },

      AssignmentAttachment: [
        {
          fileName: 'Assignment 1',
          fileAddress: 'www.baidu.com',
        },
        {
          fileName: 'Assignment 2',
          fileAddress: 'www.Google.com',
        }
      ]
    }
  }
}
</script>

<style scoped>

</style>
