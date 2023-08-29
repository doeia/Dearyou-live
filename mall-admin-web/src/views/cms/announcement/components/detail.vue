<template> 
  <el-card class="form-container" shadow="never">
    <el-form :model="announcement" label-width="120px" style="width: 720px"
             size="small">
      <el-form-item label="标题">
        <el-input v-model="announcement.title"></el-input>
      </el-form-item>
      <el-form-item label="阅读次数">
        <el-input-number v-model="announcement.readCount" :min="0" label="请选择"></el-input-number>
      </el-form-item>
<!--      <el-form-item label="公告类型">-->
      <!--        <el-select v-model="announcement.type">-->
      <!--          <el-option v-for="item in options"-->
      <!--                     :key="item.value"-->
      <!--                     :value="item.value"-->
      <!--                     :label="item.label"></el-option>-->
      <!--        </el-select>-->
      <!--      </el-form-item>-->
      <el-form-item label="公告内容">
        <tinymce :width="595" :height="300" v-model="announcement.content"></tinymce>
      </el-form-item>
      <el-form-item style="text-align: center">
        <el-button size="medium" @click="$router.back()">返回</el-button>
        <el-button type="primary" size="medium" @click="createOrUpdate()">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
  import {create, update, getInfo} from '@/api/announcement'
  import Tinymce from '@/components/Tinymce'

  export default {
    name: 'announcementEdit',
    data() {
      return {
        announcement: {
          id: null,
          title: null,
          content: null,
          readCount: 0,
          type: 0
        },
        options: [{
          label: '0',
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
    components: {Tinymce},
    async created() {
      if (this.isEdit) {
        const response = await getInfo(this.$route.query.id)
        this.announcement = response.data
      }
    },
    methods: {
      async createOrUpdate() {
        if (this.announcement.id) {
          await update(this.announcement)
        } else {
          await create(this.announcement)
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
