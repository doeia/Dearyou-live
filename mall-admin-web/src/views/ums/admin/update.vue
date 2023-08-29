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
        <el-button type="primary" size="medium" @click="update()">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
  import {getDetail, update} from '@/api/admin'
  import SingleUpload from '@/components/Upload/singleUpload'

  export default {
    name: 'adminDetail',
    data() {
      return {
        adminInfo: {}
      }
    },
    components: {SingleUpload},
    created() {
      const self = this
      getDetail(this.$route.params.id).then(res => {
        self.adminInfo = res.data
      })
    },
    methods: {
      update() {
        const adminInfo = this.adminInfo
        const params = {
          id: adminInfo.id,
          icon: adminInfo.icon,
          username: adminInfo.username,
          nickName: adminInfo.nickName,
          note: adminInfo.note,
          email: adminInfo.email,
          status: adminInfo.status
        }
        update(params).then(() => {
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


