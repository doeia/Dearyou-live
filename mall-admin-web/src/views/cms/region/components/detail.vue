<template> 
  <el-card class="form-container" shadow="never">
    <el-form :model="region" label-width="120px" style="width: 720px"
             size="small">
      <el-form-item label="名称">
        <el-input v-model="region.name"></el-input>
      </el-form-item>
      <el-form-item label="全拼">
        <el-input v-model="region.spell"></el-input>
      </el-form-item>
      <el-form-item label="简拼">
        <el-input v-model="region.shortSpell"></el-input>
      </el-form-item>
      <el-form-item label="归属地">
        <el-cascader
          placeholder="可搜索"
          :options="regionTree"
          filterable
          change-on-select
          clearable
          v-model="tmpParentId"
          :props="{value:'id',label:'name',children:'children'}"
        ></el-cascader>
        <p style="color: #f33;">归属地若为空，则该区域的级别为"省"</p>
      </el-form-item>
      <el-form-item label="排序">
        <el-input-number v-model="region.displayOrder" :min="0" label="请选择"></el-input-number>
      </el-form-item>
      <!--      <el-form-item label="版本">-->
      <!--        <el-input v-model="region.ver"></el-input>-->
      <!--      </el-form-item>-->
      <el-form-item style="text-align: center">
        <el-button size="medium" @click="$router.back()">返回</el-button>
        <el-button type="primary" size="medium" @click="createOrUpdate()">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
  import {create, update, getInfo, fetchTreeList} from '@/api/region'

  export default {
    name: 'regionEdit',
    data() {
      return {
        tmpParentId: [],
        region: {
          id: null,
          displayOrder: 0,
          name: null,
          parentId: 0,
          shortSpell: null,
          spell: null
        },
        regionTree: []
      }
    },
    props: {
      isEdit: {
        type: Boolean,
        required: true
      }
    },
    created() {
      if (this.isEdit) {
        this.getInfo()
      }
      this.getTreeList()
    },
    methods: {
      async getTreeList() {
        const response = await fetchTreeList()
        response.data.list.forEach(item => {
          item.children.forEach(inner => {
            delete inner.children
          })
        })
        this.regionTree = response.data.list
      },
      async getInfo() {
        const response = await getInfo(this.$route.query.id)
        this.region = response.data
      },
      async createOrUpdate() {
        this.region.parentId = this.tmpParentId[this.tmpParentId.length - 1]
        if (this.region.id) {
          await update(this.region)
        } else {
          await create(this.region)
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
