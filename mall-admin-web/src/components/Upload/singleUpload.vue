<template> 
  <div>
    <el-upload
      :action="`${dataObj.host}/file/upload`"
      :data="dataObj"
      list-type="picture"
      :multiple="false"
      :show-file-list="showFileList"
      :file-list="fileList"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :on-preview="handlePreview"
      :on-progress="handleProgress">
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过10MB</div>
    </el-upload>
    <el-progress v-if="isUploading&&!showFileList" :percentage="percent"></el-progress>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="fileList[0].url" alt="">
    </el-dialog>
  </div>
</template>
<script>
  import {policy} from '../../api/file'

  export default {
    name: 'singleUpload',
    props: {
      value: String
    },
    computed: {
      imageUrl() {
        return this.value
      },
      imageName() {
        if (this.value != null && this.value !== '') {
          return this.value.substr(this.value.lastIndexOf('/') + 1)
        } else {
          return null
        }
      },
      fileList() {
        return [{
          name: this.imageName,
          url: this.imageUrl
        }]
      },
      showFileList: {
        get: function () {
          return this.value !== null && this.value !== '' && this.value !== undefined
        },
        set: function (newValue) {
        }
      }
    },
    data() {
      return {
        dataObj: {
          policy: '',
          signature: '',
          key: '',
          ossaccessKeyId: '',
          dir: '',
          host: ''
          // callback:'',
        },
        dialogVisible: false,
        isUploading: false,
        percent: 0
      }
    },
    methods: {
      handleProgress(event, file, fileList) {
        this.isUploading = true
        this.percent = event.percent
      },
      emitInput(val) {
        this.$emit('input', val)
      },
      handleRemove(file, fileList) {
        this.emitInput('')
      },
      handlePreview(file) {
        this.dialogVisible = true
      },
      beforeUpload(file) {
        let _self = this
        return new Promise((resolve, reject) => {
          policy().then(response => {
            _self.dataObj.policy = response.data.policy
            _self.dataObj.signature = response.data.signature
            _self.dataObj.ossaccessKeyId = response.data.accessKeyId
            _self.dataObj.key = response.data.dir + '/${filename}'
            _self.dataObj.dir = response.data.dir
            _self.dataObj.host = response.data.host
            // _self.dataObj.callback = response.data.callback;
            resolve(true)
          }).catch(err => {
            console.log(err)
            reject(false)
          })
        })
      },
      handleUploadSuccess(res, file) {
        this.showFileList = true
        this.fileList.pop()
        this.fileList.push({name: file.name, url: this.dataObj.host + '/' + res.data})
        this.emitInput(this.fileList[0].url)
      }
    }
  }
</script>
<style>

</style>


