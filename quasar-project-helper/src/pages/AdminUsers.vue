<template>
  <q-layout view="hHh lpR fFf">
    <q-header elevated>
      <q-toolbar>
        <q-toolbar-title>
          Project Helper
          <q-btn dense flat icon="home" @click="router.push('/')">
          </q-btn>
        </q-toolbar-title>
        <PersonBar></PersonBar>
      </q-toolbar>
    </q-header>


    <q-page-container>
      <q-table
        title="Create"
        :rows="rows"
        :columns="columns"
        color="primary"
        row-key="id"
        selection="multiple"
        v-model:selected="selected"
      >
        <template v-slot:top-right>
          <q-btn
            color="primary"
            icon-right="archive"
            label="Export to csv"
            no-caps
            @click="exportTable"
          />
        </template>
        <template v-slot:top-left>
          <q-btn
            color="primary"
            label="Create"
            @click="showDialog = true"
          />
        </template>
      </q-table>
<!--      <q-btn label="Export to Excel" @click="exportToExcel" />-->
      <q-dialog v-model="showDialog">
        <div>
          <label for="start-number">From:</label>
          <input id="start-number" type="number" v-model="startNumber" />
        </div>
        <div>
          <label for="end-number">To:</label>
          <input id="end-number" type="number" v-model="endNumber" />
        </div>

        <q-card-actions align="right">
          <q-btn label="Save" color="primary" @click="showDialog = false" />
        </q-card-actions>
      </q-dialog>
      <div class="q-pa-md">
        <div class="q-gutter-md row items-start">
          <span style="font-size: 20px;">Please upload CSV files to batch create users</span>
          <q-file
            style="max-width: 300px"
            v-model="filesPng"
            rounded
            outlined
            label="Filtered (csv only)"
            multiple
            :filter="checkFileType"
            @rejected="onRejected"
          />
        </div>
      </div>

      <q-table
        title="Create"
        :rows="rows"
        :columns="columns"
        color="primary"
        row-key="id"
        selection="multiple"
        v-model:selected="selected"
      >
        <template v-slot:top-right>
          <q-btn
            color="primary"
            icon-right="archive"
            label="Export to csv"
            no-caps
            @click="exportTable"
          />
        </template>
        <template v-slot:top-left>
          <q-btn
            color="primary"
            label="Create"
            @click="showDialog = true"
          />
        </template>
      </q-table>

      <q-dialog v-model="showDialog">
        <div>
          <label for="start-number">From:</label>
          <input id="start-number" type="number" v-model="startNumber2" />
        </div>
        <div>
          <label for="end-number">To:</label>
          <input id="end-number" type="number" v-model="endNumber2" />
        </div>

        <q-card-actions align="right">
          <q-btn label="Save" color="primary" @click="showDialog = false" />
        </q-card-actions>
      </q-dialog>
      <div class="q-pa-md">
        <div class="q-gutter-md row items-start">
          <span style="font-size: 20px;">Please upload CSV files to batch reset users</span>
          <q-file
            style="max-width: 300px"
            v-model="filesPng"
            rounded
            outlined
            label="Filtered (csv only)"
            multiple
            :filter="checkFileType"
            @rejected="onRejected"
          />
        </div>
      </div>

      <div>
        <q-table
          title="Predefined Emails"
          :rows="predefinedEmails"
          :columns="column"
          row-key="id"
          selection="multiple"
          v-model:selected="selectedEmails"
        >
          <template v-slot:top-right>
            <q-btn
              color="primary"
              label="Add"
              @click="showDialogs = true"
            />
          </template>
          <template v-slot:top-left>
            <q-btn
              color="primary"
              label="Delete"
              @click="deleteSelectedEmails"
              :disable="selectedEmails.length === 0"
            />
          </template>
        </q-table>

        <q-dialog v-model="showDialogs">
          <q-card>
            <q-card-section>
              <q-input
                outlined
                v-model="newEmail"
                label="Email"
              />
            </q-card-section>

            <q-card-actions align="right">
              <q-btn
                color="primary"
                label="Cancel"
                @click="cancelAddEmail"
              />
              <q-btn
                color="primary"
                label="Add"
                @click="addEmail"
              />
            </q-card-actions>
          </q-card>
        </q-dialog>
      </div>
    </q-page-container>
  </q-layout>
</template>

<script>
import { exportFile,useQuasar } from 'quasar';
import {useRouter} from 'vue-router';
import PersonBar from 'components/Layout/PersonBar.vue';
// import XLSX from 'xlsx';
// import { saveAs } from 'file-saver';

function wrapCsvValue (val, formatFn, row) {
  let formatted = formatFn !== void 0
    ? formatFn(val, row)
    : val

  formatted = formatted === void 0 || formatted === null
    ? ''
    : String(formatted)

  formatted = formatted.split('"').join('""')
  return `"${formatted}"`
}
export default {
  components: {PersonBar},
  data() {
    const router = useRouter()
    const $q = useQuasar()
    return {
      router,
      $q,
      selected: [],
      startNumber: 0,
      endNumber: 0,
      startNumber2: 0,
      endNumber2: 0,
      filesPng: null,
      rows: [],
      showDialog: false,
      columns: [
        { name: 'id', required: true, label: 'id', align: 'left', field: 'id', sortable: true },
        { name: 'password', required: true, label: 'password', align: 'left', field: 'password', sortable: false },
        { name: 'email', required: true, label: 'email', align: 'left', field: 'email', sortable: false },
        { name: 'phone number', required: true, label: 'phone', align: 'left', field: 'phone', sortable: false },
      ],
      predefinedEmails: [
        { id: 1, email: 'email1@example.com' },
        { id: 2, email: 'email2@example.com' },
        { id: 3, email: 'email3@example.com' }
      ],
      column: [
        { name: 'email', required: true, label: 'Email', align: 'left', field: 'email', sortable: true }
      ],
      selectedEmails: [],
      showDialogs: false,
      newEmail: ''
    };
  },
  watch: {
    startNumber: {
      handler() {
        this.batchLoadData();
      },
      immediate: true,
    },
    endNumber: {
      handler() {
        this.batchLoadData();
      },
      immediate: true,
    },
    startNumber2: {
      handler() {
        this.batchLoadData2();
      },
      immediate: true,
    },
    endNumber2: {
      handler() {
        this.batchLoadData2();
      },
      immediate: true,
    },
  },
  methods: {
    // handleDownloadCSV() {
    //   axios.get('/api/endpoint', {
    //     responseType: 'blob' // 设置响应类型为 blob
    //   })
    //     .then(response => {
    //       this.handleCSVResponse(response.data);
    //     })
    //     .catch(error => {
    //       error.message.data
    //     });
    // },
    //
    // handleCSVResponse(csvFile) {
    //   const reader = new FileReader();
    //   reader.onload = () => {
    //     const csvData = reader.result;
    //     this.rows = this.parseCSVData(csvData);
    //   };
    //   reader.readAsText(csvFile);
    // },
    //
    // parseCSVData(csvData) {
    //   const parsedData = Papa.parse(csvData, { header: true }).data;
    //   const transformedData = parsedData.map(row => {
    //     return {
    //       id: row.ID || '', // 从解析数据的 'ID' 列获取值，如果不存在则为空字符串
    //       password: '', // 这里使用空字符串，因为密码列在 CSV 中不存在
    //       email: row.email || '', // 从解析数据的 'email' 列获取值，如果不存在则为空字符串
    //       phone: row['phone number'] || '' // 从解析数据的 'phone number' 列获取值，如果不存在则为空字符串
    //     };
    //   });
    //   return transformedData;
    // },

    // exportToExcel() {
    //   const worksheet = XLSX.utils.json_to_sheet(this.rows);
    //   const workbook = XLSX.utils.book_new();
    //   XLSX.utils.book_append_sheet(workbook, worksheet, 'Sheet1');
    //   const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
    //   this.saveExcelFile(excelBuffer, 'tableData.xlsx');
    // },
    //
    // saveExcelFile(buffer, fileName) {
    //   const data = new Blob([buffer], { type: 'application/octet-stream' });
    //   saveAs(data, fileName);
    // },

    batchLoadData() {
      const password = '123456';

      const leadingZeros = 8;
      const rows = [];

      for (let i = this.startNumber; i <= this.endNumber; i++) {
        const id = i.toString().padStart(leadingZeros, '0');

        rows.push({ id, password });
      }

      this.rows = rows;
    },
    batchLoadData2() {
      const password = '123456';

      const leadingZeros = 8;
      const rows = [];

      for (let i = this.startNumber2; i <= this.endNumber2; i++) {
        const id = i.toString().padStart(leadingZeros, '0');

        rows.push({ id, password });
      }

      this.rows = rows;
    },
    checkFileType(files) {
      return files.filter(file => {
        const fileType = file.name.split('.').pop().toLowerCase()
        return fileType === 'csv'
      })
    },

    onRejected (rejectedEntries) {
      this.$q.notify({
        type: 'negative',
        message: `${rejectedEntries.length} file(s) did not pass validation constraints`
      })
    },

    addEmail() {
      const newId = this.predefinedEmails.length + 1;
      this.predefinedEmails.push({ id: newId, email: this.newEmail });
      this.newEmail = '';
      this.showDialogs = false;
    },

    cancelAddEmail() {
      this.newEmail = '';
      this.showDialogs = false;
    },

    deleteSelectedEmails() {
      this.predefinedEmails = this.predefinedEmails.filter(email => !this.selectedEmails.includes(email));
      this.selectedEmails = [];
    },

    exportTable() {

      const selectedRows = this.rows.filter(row => this.selected.includes(row))
      const content = [this.columns.map(col => wrapCsvValue(col.label))].concat(
        selectedRows.map(row => this.columns.map(col => wrapCsvValue(
          typeof col.field === 'function'
            ? col.field(row)
            : row[col.field === void 0 ? col.name : col.field],
          col.format,
          row
        )).join(','))
      ).join('\r\n')

      const status = exportFile(
        'table-export.csv',
        content,
        'text/csv'
      )

      if (status !== true) {
        this.$q.notify({
          message: 'Browser denied file download...',
          color: 'negative',
          icon: 'warning'
        })
      }
    }
  },
};
</script>

