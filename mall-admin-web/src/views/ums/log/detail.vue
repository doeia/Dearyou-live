<template> 
  <el-card class="form-container" shadow="never">
    <div slot="header" class="clearfix">
      <span>日志详情</span>
    </div>
    <main>
      <el-row gutter="20" style="margin-bottom: 20px">
        <el-col span="4">
          <strong class="label">编号</strong>
        </el-col>
        <el-col span="20">
          <span class="content">{{log.id}}</span>
        </el-col>
      </el-row>
      <el-row gutter="20" style="margin-bottom: 20px">
        <el-col span="4">
          <strong class="label">操作人</strong>
        </el-col>
        <el-col span="20">
          <span class="content">{{log.admin}}</span>
        </el-col>
      </el-row>
      <el-row gutter="20" style="margin-bottom: 20px">
        <el-col span="4">
          <strong class="label">IP地址</strong>
        </el-col>
        <el-col span="20">
          <span class="content">{{log.ip}}</span>
        </el-col>
      </el-row>
      <el-row gutter="20" style="margin-bottom: 20px">
        <el-col span="4">
          <strong class="label">请求地址</strong>
        </el-col>
        <el-col span="20">
          <span class="content">{{log.url}}</span>
        </el-col>
      </el-row>
      <el-row gutter="20" style="margin-bottom: 20px">
        <el-col span="4">
          <strong class="label">请求方法</strong>
        </el-col>
        <el-col span="20">
          <span class="content">
            <el-tag type="primary">{{log.method}}</el-tag>
          </span>
        </el-col>
      </el-row>
      <el-row gutter="20" style="margin-bottom: 20px">
        <el-col span="4">
          <strong class="label">操作类型</strong>
        </el-col>
        <el-col span="20">
          <span class="content">
            <el-tag v-if="log.type" type="primary">安全操作</el-tag>
            <el-tag v-if="!log.type" type="warning">未知操作</el-tag>
          </span>
        </el-col>
      </el-row>
      <el-row gutter="20" style="margin-bottom: 20px">
        <el-col span="4">
          <strong class="label">操作内容</strong>
        </el-col>
        <el-col span="20">
          <span class="content">{{log.action}}</span>
        </el-col>
      </el-row>
      <el-row gutter="20" style="margin-bottom: 20px">
        <el-col span="4">
          <strong class="label">操作状态</strong>
        </el-col>
        <el-col span="20">
          <span class="content">
            <el-tag v-if="log.status" type="success">成功</el-tag>
            <el-tag v-if="!log.status" type="danger">失败</el-tag></span>
        </el-col>
      </el-row>
      <el-row gutter="20" style="margin-bottom: 20px">
        <el-col span="4">
          <strong class="label">备注</strong>
        </el-col>
        <el-col span="20">
          <span class="content">{{log.comment||'无'}}</span>
        </el-col>
      </el-row>
      <el-row gutter="20" style="margin-bottom: 20px">
        <el-col span="4">
          <strong class="label">创建时间</strong>
        </el-col>
        <el-col span="20">
          <span class="content">{{formatDate(log.createTime)}}</span>
        </el-col>
      </el-row>
      <el-row gutter="20" style="margin-bottom: 20px">
        <el-col span="24" style="margin-bottom: 20px">
          <strong class="label">传入参数</strong>
        </el-col>
        <el-col span="24" style="overflow: auto">
          <span class="content">{{log.parameter||'无'}}</span>
        </el-col>
      </el-row>
      <el-row gutter="20" style="margin-bottom: 20px">
        <el-col span="24" style="margin-bottom: 20px">
          <strong class="label">返回结果</strong>
        </el-col>
        <el-col span="24" style="overflow: auto">
          <span class="content">{{log.result}}</span>
        </el-col>
      </el-row>
    </main>
  </el-card>
</template>
<script>
  import {getInfo} from '@/api/log'
  import {utilsMixin} from '../../../utils/mixins'

  export default {
    name: 'logInfo',
    mixins: [utilsMixin],
    data() {
      return {
        log: {}
      }
    },
    async created() {
      const response = await getInfo(this.$route.query.id)
      this.log = response.data
    }
  }
</script>
<style>
  .form-container {
    width: 800px;
  }
</style>
