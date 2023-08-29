<template> 
  <el-card class="form-container" shadow="never">
    <el-form :model="memberInfo" label-width="120px" style="width: 600px"
             size="small">
      <el-form-item label="头像">
        <single-upload v-model="memberInfo.icon"></single-upload>
      </el-form-item>
      <el-form-item label="名称">
        <el-input v-model="memberInfo.username"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input type="password" v-model="memberInfo.password"></el-input>
      </el-form-item>
      <el-form-item label="真是姓名">
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
        <el-input type="phone" v-model="memberInfo.phone"></el-input>
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
<!--      <el-form-item label="会员等级">-->
<!--        <el-select v-model="memberInfo.memberLevelId">-->
<!--          <el-option v-for="item in memberLevelOptions"-->
<!--                     :key="item.value"-->
<!--                     :label="item.label"-->
<!--                     :value="item.value"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="个信签名">
        <el-input type="textarea" v-model="memberInfo.personalizedSignature"></el-input>
      </el-form-item>
      <el-form-item label="账号来源">
        <el-input v-model="memberInfo.sourceType" disabled></el-input>
      </el-form-item>
      <el-form-item style="text-align: center">
        <el-button size="medium" @click="$router.back()">返回</el-button>
        <el-button type="primary" size="medium" @click="update()">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
  import {create} from '../../../api/member'
  import SingleUpload from '../../../components/Upload/singleUpload'

  export default {
    name: 'memberAdd',
    data() {
      return {
        memberInfo: {
          id: null,
          username: null,
          realName: null,
          nickname: null,
          memberLevelId: null,
          icon: null,
          password: null,
          city: null,
          gender: null,
          idCard: null,
          job: null,
          phone: null,
          birthday: null,
          growth: 0,
          integration: 0,
          historyIntegration: 0,
          luckeyCount: 0,
          personalizedSignature: null,
          sourceType: null,
          status: 1
        },
        genderOptions: [
          {label: '未知', value: 0},
          {label: '男', value: 1},
          {label: '女', value: 2}
        ]
      }
    },
    components: {SingleUpload},
    methods: {
      async update() {
        await create(this.memberInfo)
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


