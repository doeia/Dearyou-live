<template> 
  <el-card class="form-container" shadow="never">
    <el-form :model="levelInfo" label-width="120px" style="width: 600px"
             size="small">
      <el-form-item label="名称">
        <el-input v-model="levelInfo.name"></el-input>
      </el-form-item>
      <el-form-item label="说明">
        <el-input v-model="levelInfo.note"></el-input>
      </el-form-item>
      <el-form-item label="成长点数">
        <el-input-number v-model="levelInfo.growthPoint" :min="0" label="请选择"></el-input-number>
      </el-form-item>
      <el-form-item label="免运费标准">
        <el-input-number v-model="levelInfo.freeFreightPoint" :min="0" label="请选择"></el-input-number>
      </el-form-item>
      <el-form-item label="每次评论获得的成长值">
        <el-input-number v-model="levelInfo.commentGrowthPoint" :min="0" label="请选择"></el-input-number>
      </el-form-item>
      <el-form-item label="是否有免邮特权">
        <el-select v-model="levelInfo.priviledgeFreeFreight">
          <el-option v-for="item in options"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否有签到特权">
        <el-select v-model="levelInfo.priviledgeSignIn">
          <el-option v-for="item in options"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否有评论获奖励特权">
        <el-select v-model="levelInfo.priviledgeComment">
          <el-option v-for="item in options"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否有专享活动特权">
        <el-select v-model="levelInfo.priviledgePromotion">
          <el-option v-for="item in options"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否有会员价格特权">
        <el-select v-model="levelInfo.priviledgeMemberPrice">
          <el-option v-for="item in options"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否有生日特权">
        <el-select v-model="levelInfo.priviledgeBirthday">
          <el-option v-for="item in options"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item style="text-align: center">
        <el-button size="medium" @click="$router.back()">返回</el-button>
        <el-button type="primary" size="medium" @click="createOrUpdate()">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
  import {create, update, getInfo} from '@/api/memberLevel'

  export default {
    name: 'memberAdd',
    data() {
      return {
        levelInfo: {
          id: null,
          name: null,
          note: null,
          growthPoint: 0,
          freeFreightPoint: 0,
          commentGrowthPoint: 0,
          priviledgeFreeFreight: 0,
          priviledgeSignIn: 0,
          priviledgeComment: 0,
          priviledgePromotion: 0,
          priviledgeMemberPrice: 0,
          priviledgeBirthday: 0,
          defaultStatus: 1
        },
        options: [
          {label: '无特权', value: 0},
          {label: '有特权', value: 1}
        ]
      }
    },
    props: {
      isEdit: {
        type: Boolean,
        required: true
      }
    },
    async created() {
      if (this.isEdit) {
        const response = await getInfo(this.$route.query.id)
        this.levelInfo = response.data
      }
    },
    methods: {
      async createOrUpdate() {
        if (this.levelInfo.id) {
          await update(this.levelInfo)
        } else {
          await create(this.levelInfo)
        }
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


