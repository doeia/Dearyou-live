<template> 
  <div class="app-container">
    <!--    <el-card class="filter-container" shadow="never">-->
    <!--      <div>-->
    <!--        <i class="el-icon-search"></i>-->
    <!--        <span>筛选搜索</span>-->
    <!--        <el-button-->
    <!--          style="float: right"-->
    <!--          @click="handleSearchList()"-->
    <!--          type="primary"-->
    <!--          size="small">-->
    <!--          查询结果-->
    <!--        </el-button>-->
    <!--        <el-button-->
    <!--          style="float: right;margin-right: 15px"-->
    <!--          @click="handleResetSearch()"-->
    <!--          size="small">-->
    <!--          重置-->
    <!--        </el-button>-->
    <!--      </div>-->
    <!--      <div style="margin-top: 15px">-->
    <!--        <el-form :inline="true" :model="listQuery" size="small" label-width="140px">-->
    <!--          <el-form-item label="会员编号：">-->
    <!--            <el-input style="width: 203px" v-model="listQuery.memberId" placeholder="会员编号"></el-input>-->
    <!--          </el-form-item>-->
    <!--          <el-form-item label="关键字：">-->
    <!--            <el-input style="width: 203px" v-model="listQuery.keywords" placeholder="关键字"></el-input>-->
    <!--          </el-form-item>-->
    <!--        </el-form>-->
    <!--      </div>-->
    <!--    </el-card>-->
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
    </el-card>
    <div class="table-container">
      <el-table ref="productTable"
                :data="list"
                style="width: 100%"
                v-loading="listLoading"
                border>
        <el-table-column type="selection" width="60" align="center"></el-table-column>
        <el-table-column label="编号" width="150" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>
        <el-table-column label="会员ID" width="140" align="center">
          <template slot-scope="scope">{{scope.row.memberId}}</template>
        </el-table-column>
        <el-table-column label="登录时间" align="center">
          <template slot-scope="scope">{{formatDate(scope.row.createTime)}}</template>
        </el-table-column>
        <el-table-column label="登录地点" width="200" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.province}} {{scope.row.city}}</p>
          </template>
        </el-table-column>
        <el-table-column label="登录IP" width="200" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.ip}}</p>
          </template>
        </el-table-column>
        <el-table-column label="登录类型" width="200" align="center">
          <template slot-scope="scope">
            <p>{{getLoginType(scope.row.loginType)}}</p>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  import {
    fetchLoginLogList
  } from '@/api/member'
  import {utilsMixin} from '../../../utils/mixins'

  const defaultListQuery = {
    memberId: null,
    keywords: null,
    pageNum: 1,
    pageSize: 5
  }

  export default {
    name: 'memberSearchHistoryList',
    mixins: [utilsMixin],
    data() {
      return {
        listQuery: Object.assign({}, defaultListQuery),
        list: null,
        total: null,
        listLoading: true
      }
    },
    created() {
      this.getList()
    },
    methods: {
      getLoginType(code) {
        switch (code) {
          case 0:
            return 'P'
          case 1:
            return 'Android'
          case 2:
            return 'IOS'
          case 3:
            return '小程序'
        }
      },
      handleSearchList() {
        this.listQuery.pageNum = 1
        this.getList()
      },
      handleResetSearch() {
        this.listQuery = Object.assign({}, defaultListQuery)
      },
      getList() {
        this.listLoading = true
        fetchLoginLogList(this.listQuery).then(response => {
          this.listLoading = false
          this.list = response.data.list
          this.total = response.data.total
        })
      }
    }
  }
</script>
<style></style>
