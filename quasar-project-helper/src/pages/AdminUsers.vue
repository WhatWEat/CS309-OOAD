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
      <div>
        <label for="start-number">From:</label>
        <input id="start-number" type="number" v-model="startNumber" />
      </div>
      <div>
        <label for="end-number">TO:</label>
        <input id="end-number" type="number" v-model="endNumber" />
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
      </q-table>
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

      <div>
        <label for="start-number">From:</label>
        <input id="start-number" type="number" v-model="startNumber2" />
      </div>
      <div>
        <label for="end-number">TO:</label>
        <input id="end-number" type="number" v-model="endNumber2" />
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
      </q-table>
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
    </q-page-container>
  </q-layout>
</template>

<script>
import { ref } from 'vue'
import { exportFile,useQuasar } from 'quasar';
import {useRouter} from 'vue-router';
import PersonBar from 'components/Layout/PersonBar.vue';

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
      columns: [
        { name: 'id', required: true, label: 'id', align: 'left', field: 'id', sortable: true },
        { name: 'password', required: true, label: 'password', align: 'left', field: 'password', sortable: false },
      ]
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

    exportTable() {
      // Filter selected rows
      const selectedRows = this.rows.filter(row => this.selected.includes(row))

      // Naive encoding to csv format
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
