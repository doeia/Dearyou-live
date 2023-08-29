<template> 
  <el-card class="form-container" shadow="never">
    <el-form :model="ship" label-width="120px" style="width: 720px"
             size="small">
      <el-form-item label="名称">
        <el-input v-model="ship.name"></el-input>
      </el-form-item>
      <el-form-item label="公司编号">
        <el-input v-model="ship.code"></el-input>
      </el-form-item>
      <el-form-item label="禁用与启用">
        <el-select v-model="ship.state">
          <el-option v-for="item in options"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="排序">
        <el-input-number v-model="ship.displayOrder" :min="0" label="请选择"></el-input-number>
      </el-form-item>
      <el-form-item style="text-align: center">
        <el-button size="medium" @click="$router.back()">返回</el-button>
        <el-button type="primary" size="medium" @click="createOrUpdate()">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
  import {create, update, getInfo} from '@/api/ship'

  export default {
    name: 'shipEdit',
    data() {
      return {
        ship: {
          id: null,
          name: null,
          displayOrder: 0,
          code: null,
          state: 1
        },
        options: [{
          label: '启用',
          value: 1
        }, {
          label: '禁用',
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
        this.ship = response.data
      }
    },
    methods: {
      async createOrUpdate() {
        if (this.ship.id) {
          await update(this.ship)
        } else {
          await create(this.ship)
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
