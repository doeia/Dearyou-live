<template> 
  <el-card class="form-container" shadow="never">
    <el-form :model="brandCategory" label-width="120px" style="width: 720px"
             size="small">
      <el-form-item label="名称">
        <el-input v-model="brandCategory.name"></el-input>
      </el-form-item>
      <el-form-item label="品牌">
        <el-select v-model="brandCategory.brandId" filterable placeholder="请选择">
          <el-option
            v-for="item in brandList"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="排序">
        <el-input-number v-model="brandCategory.sort" :min="0" label="请选择"></el-input-number>
      </el-form-item>
      <el-form-item style="text-align: center">
        <el-button size="medium" @click="$router.back()">返回</el-button>
        <el-button type="primary" size="medium" @click="createOrUpdate()">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
  import {create, update, getInfo} from '@/api/brandCategory'
  import {fetchListAll as fetchBrandList} from '@/api/brand'
  import Tinymce from '@/components/Tinymce'

  export default {
    name: 'announcementEdit',
    data() {
      return {
        brandCategory: {
          id: null,
          name: null,
          brandId: null,
          sort: 0
        },
        brandList: []
      }
    },
    props: {
      isEdit: {
        type: Boolean,
        required: true
      }
    },
    components: {Tinymce},
    created() {
      fetchBrandList().then((response) => {
        response.data.forEach(item => {
          this.brandList.push({label: item.name, value: item.id})
        })
      })
      if (this.isEdit) {
        getInfo(this.$route.query.id).then((response) => {
          this.brandCategory = response.data
        })
      }
    },
    methods: {
      async createOrUpdate() {
        if (this.brandCategory.id) {
          await update(this.brandCategory)
        } else {
          await create(this.brandCategory)
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
