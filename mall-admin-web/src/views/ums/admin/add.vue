<template> 
  <el-card class="form-container" shadow="never">
    <el-form :model="adminInfo" label-width="120px" style="width: 600px"
             size="small">
      <el-form-item label="头像">
        <single-upload v-model="adminInfo.icon"></single-upload>
      </el-form-item>
      <el-form-item label="邮件">
        <el-input type="email" v-model="adminInfo.email"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="adminInfo.password" type="password"></el-input>
      </el-form-item>
      <el-form-item label="名称">
        <el-input v-model="adminInfo.username"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="adminInfo.nickName"></el-input>
      </el-form-item>
      <el-form-item label="备注">
        <el-input
          :autoSize="true"
          v-model="adminInfo.note"
          type="textarea"
          placeholder="请输入内容"></el-input>
      </el-form-item>
      <el-form-item style="text-align: center">
        <el-button size="medium" @click="$router.back()">返回</el-button>
        <el-button type="primary" size="medium" @click="register()">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
  import {register} from '@/api/admin'
  import SingleUpload from '@/components/Upload/singleUpload'

  export default {
    name: 'adminAdd',
    data() {
      return {
        adminInfo: {}
      }
    },
    components: {SingleUpload},
    methods: {
      register() {
        register(this.adminInfo).then(() => {
          this.$message({
            type: 'success',
            message: '提交成功',
            duration: 1000
          })
          this.$router.back()
        })
      }
    }
  }
</script>
<style>
  .form-container {
    width: 800px;
  }
</style>


