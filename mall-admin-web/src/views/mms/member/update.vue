<template> 
  <el-card class="form-container" shadow="never">
    <el-form :model="memberInfo" label-width="120px" style="width: 600px"
             size="small">
      <el-form-item label="头像">
        <single-upload v-model="memberInfo.icon"></single-upload>
      </el-form-item>
      <el-form-item label="名称">
        <el-input v-model="memberInfo.username" disabled></el-input>
      </el-form-item>
      <el-form-item label="真实姓名">
        <el-input v-model="memberInfo.realName"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="memberInfo.nickname"></el-input>
      </el-form-item>
      <el-form-item label="所在城市">
        <el-input v-model="memberInfo.city"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="memberInfo.gender">
          <el-option v-for="item in genderOptions" :key="item.vale" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="身份证">
        <el-input v-model="memberInfo.idCard"></el-input>
      </el-form-item>
      <el-form-item label="工作">
        <el-input v-model="memberInfo.job"></el-input>
      </el-form-item>
      <el-form-item label="手机">
        <el-input v-model="memberInfo.phone"></el-input>
      </el-form-item>
      <el-form-item label="生日">
        <el-date-picker
          v-model="memberInfo.birthday"
          type="date"
          placeholder="选择日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="成长值">
        <el-input-number v-model="memberInfo.growth" :min="0" label="请选择"></el-input-number>
      </el-form-item>
      <el-form-item label="积分">
        <el-input-number v-model="memberInfo.integration" :min="0" label="请选择"></el-input-number>
      </el-form-item>
      <el-form-item label="历史积分">
        <el-input-number v-model="memberInfo.historyIntegration" :min="0" label="请选择"></el-input-number>
      </el-form-item>
      <el-form-item label="剩余抽奖次数">
        <el-input-number v-model="memberInfo.luckeyCount" :min="0" label="请选择"></el-input-number>
      </el-form-item>
      <el-form-item label="个信签名">
        <el-input type="textarea" v-model="memberInfo.personalizedSignature"></el-input>
      </el-form-item>
      <el-form-item label="账号来源">
        <el-input v-model="memberInfo.sourceType" disabled></el-input>
      </el-form-item>
      <el-form-item label="创建日期">
        <el-date-picker
          v-model="memberInfo.createTime"
          type="date"
          placeholder="选择日期"
          disabled>
        </el-date-picker>
      </el-form-item>
      <el-form-item style="text-align: center">
        <el-button size="medium" @click="$router.back()">返回</el-button>
        <el-button type="primary" size="medium" @click="update()">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
  import {getInfo, update} from '../../../api/member'
  import SingleUpload from '@/components/Upload/singleUpload'
  import {utilsMixin} from '../../../utils/mixins'

  export default {
    name: 'memberUpdate',
    mixins: [utilsMixin],
    data() {
      return {
        memberInfo: {},
        genderOptions: [
          {label: '未知', value: 0},
          {label: '男', value: 1},
          {label: '女', value: 2}
        ]
      }
    },
    components: {SingleUpload},
    async created() {
      const response = await getInfo(this.$route.query.id)
      this.memberInfo = response.data
    },
    methods: {
      async update() {
        const data = this.cloneObj(this.memberInfo)
        delete data.password
        delete data.username
        await update(data)
        this.$message({
          type: 'success',
          message: '提交成功',
          duration: 1000
        })
        this.$router.back()
      }
    }
  }
</script>
<style>
  .form-container {
    width: 800px;
  }
</style>


