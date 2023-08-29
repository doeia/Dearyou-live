<template> 
  <el-card class="form-container" shadow="never">
    <el-form :model="feightTemplate" label-width="120px" style="width: 720px"
             size="small">
      <el-form-item label="名称">
        <el-input v-model="feightTemplate.name"></el-input>
      </el-form-item>
      <el-form-item label="计费方式">
        <el-select v-model="feightTemplate.chargeType">
          <el-option v-for="item in options"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="目的地">
        <el-input v-model="feightTemplate.dest"></el-input>
      </el-form-item>
      <el-form-item label="首费">
        <el-input-number v-model="feightTemplate.firstFee" :min="0" label="请选择"></el-input-number>
      </el-form-item>
      <el-form-item label="首重">
        <el-input-number v-model="feightTemplate.firstWeight" :min="0" label="请选择"></el-input-number>
      </el-form-item>
      <el-form-item label="续费">
        <el-input-number v-model="feightTemplate.continueFee" :min="0" label="请选择"></el-input-number>
      </el-form-item>
      <el-form-item label="续重">
        <el-input-number v-model="feightTemplate.continueWeight" :min="0" label="请选择"></el-input-number>
      </el-form-item>
      <el-form-item style="text-align: center">
        <el-button size="medium" @click="$router.back()">返回</el-button>
        <el-button type="primary" size="medium" @click="createOrUpdate()">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
  import {create, update, getInfo} from '@/api/feightTemplate'

  export default {
    name: 'shipEdit',
    data() {
      return {
        feightTemplate: {
          chargeType: 0,
          continueFee: 0,
          continueWeight: 0,
          dest: null,
          firstFee: 0,
          firstWeight: 0,
          id: null,
          name: null
        },
        options: [{
          label: '按件数',
          value: 1
        }, {
          label: '按重量',
          value: 0
        }]
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
        this.feightTemplate = response.data
      }
    },
    methods: {
      async createOrUpdate() {
        if (this.feightTemplate.id) {
          await update(this.feightTemplate)
        } else {
          await create(this.feightTemplate)
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
