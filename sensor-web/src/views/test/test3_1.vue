<template>
  <div>
    <el-upload
      class="upload-demo"
      action=""
      :on-preview="handlePreview"
      :on-remove="handleRemove"
      :before-remove="beforeRemove"
      :auto-upload="false"
      multiple
      :limit="3"
      :on-exceed="handleExceed"
      :file-list="fileList">
      <el-button slot="trigger" size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
      <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传</el-button>
    </el-upload>
  </div>
</template>


<script>
import fileApi from "@/api/fileManage";
  export default {
    data() {
      return {
        fileList: []
      };
    },
    methods: {
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },
      uploadFile(){
        console.log(this.fileList)
        // fileApi.uploadFile(this.fileList).then(response => {
        //   // 成功提示
        //   this.$message({
        //     message: response.message,
        //     type: 'success'
        //   });
        // })
      },
      submitUpload()
      {
        console.log("submit")
      },
    }
  }
</script>

<style>

</style>