<template> 
  <el-card class="form-container" shadow="never">
    <el-form :model="article" label-width="120px" style="width: 720px"
             size="small">
      <el-form-item label="图片">
        <single-upload v-model="article.pic"></single-upload>
      </el-form-item>
      <el-form-item label="标题">
        <el-input v-model="article.title"></el-input>
      </el-form-item>
      <el-form-item label="摘要">
        <el-input v-model="article.synopsis"></el-input>
      </el-form-item>
      <el-form-item label="排序">
        <el-input-number v-model="article.sort" :min="0" label="请选择"></el-input-number>
      </el-form-item>
      <el-form-item label="是否置顶">
        <el-select v-model="article.isTop">
          <el-option v-for="item in options"
                     :key="item.label"
                     :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="内容">
        <tinymce :width="595" :height="300" v-model="article.description"></tinymce>
      </el-form-item>
      <el-form-item style="text-align: center">
        <el-button size="medium" @click="$router.back()">返回</el-button>
        <el-button type="primary" size="medium" @click="createOrUpdate()">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
  import {create, update, getInfo} from '@/api/article'
  import Tinymce from '@/components/Tinymce'
  import SingleUpload from '@/components/Upload/singleUpload'

  export default {
    name: 'announcementEdit',
    data() {
      return {
        article: {
          id: null,
          title: null,
          description: null,
          pic: null,
          sort: 0,
          isTop: false,
          synopsis: null
        },
        options: [
          {
            label: '置顶',
            value: true
          }, {
            label: '不置顶',
            value: false
          }
        ]
      }
    },
    props: {
      isEdit: {
        type: Boolean,
        required: true
      }
    },
    components: {Tinymce, SingleUpload},
    async created() {
      if (this.isEdit) {
        const response = await getInfo(this.$route.query.id)
        this.article = response.data
      }
    },
    methods: {
      async createOrUpdate() {
        if (this.article.id) {
          await update(this.article)
        } else {
          await create(this.article)
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
