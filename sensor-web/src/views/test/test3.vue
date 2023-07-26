<template>
  <div class="app-container">
    <upload-excel-component :on-success="handleSuccess" :before-upload="beforeUpload" />
    <div>部分数据预览</div>
    <el-table :data="tableDataShow" border highlight-current-row style="width: 100%;margin-top:20px;">
      <el-table-column v-for="item of tableHeader" :key="item" :prop="item" :label="item" />
    </el-table>
    <el-button
      @click="uploadFile(tableData)"
      type="primary"
      round
      icon="el-icon-upload"
      style="margin-top: 30px;"
      >上传
    </el-button>
  </div>
</template>

<script>
import UploadExcelComponent from '@/components/UploadExcel/index.vue'
import fileApi from "@/api/fileManage";

export default {
  name: 'UploadExcel',
  components: { UploadExcelComponent },
  data() {
    return {
      tableData: [],
      tableDataShow: [],
      tableHeader: []
    }
  },
  methods: {
    beforeUpload(file) {
      const isLt1M = file.size / 1024 / 1024 < 1

      if (isLt1M) {
        return true
      }

      this.$message({
        message: 'Please do not upload files larger than 1m in size.',
        type: 'warning'
      })
      return false
    },
    handleSuccess({ results, header }) {
      this.tableData = results
      this.tableDataShow = this.tableData.slice(0, 10)
      this.tableHeader = header
      console.log(this.tableDataShow)
      console.log(this.tableHeader)
    },
    uploadFile(tableData){
      fileApi.uploadFile(tableData).then(response => {
        // 成功提示
        this.$message({
          message: response.message,
          type: 'success'
        });

        this.tableData = []
        this.tableHeader = []
        this.tableDataShow = []
      })
    }
  }
}
</script>