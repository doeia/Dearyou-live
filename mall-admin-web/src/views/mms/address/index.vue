<template> 
  <div class="app-container">
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
        <el-table-column label="所属会员编号" width="200" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.memberId}}</p>
            <el-button type="link" @click="handleViewMemberInfo(scope.row.memberId)">查看会员信息</el-button>
          </template>
        </el-table-column>
        <el-table-column label="名称" width="150" align="center">
          <template slot-scope="scope">{{scope.row.name}}</template>
        </el-table-column>
        <el-table-column label="手机" width="140px" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.phoneNumber}}</p>
          </template>
        </el-table-column>
        <el-table-column label="地址" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.province}} {{scope.row.city}} {{scope.row.region}} {{scope.row.detailAddress}}</p>
          </template>
        </el-table-column>
        <!--        <el-table-column label="操作" width="160" align="center">-->
        <!--          <template slot-scope="scope">-->
        <!--            <p>-->
        <!--              <el-button-->
        <!--                size="mini"-->
        <!--                @click="handleUpdateMember(scope.$index, scope.row)">编辑-->
        <!--              </el-button>-->
        <!--              <el-button-->
        <!--                size="mini"-->
        <!--                type="danger"-->
        <!--                @click="handleDelete(scope.$index, scope.row)">删除-->
        <!--              </el-button>-->
        <!--            </p>-->
        <!--          </template>-->
        <!--        </el-table-column>-->
      </el-table>
    </div>
    <div class="pagination-container">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        layout="total, sizes,prev, pager, next,jumper"
        :page-size="listQuery.pageSize"
        :page-sizes="[5,10,15]"
        :current-page.sync="listQuery.pageNum"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>
<script>
  import {
    fetchAddressList
  } from '@/api/member'

  import {utilsMixin} from '../../../utils/mixins'

  const defaultListQuery = {
    pageNum: 1,
    pageSize: 5
  }
  export default {
    name: 'addressList',
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
      handleViewMemberInfo(id) {
        this.$router.push({path: '/mms/member/update', query: {id}})
      },
      getList() {
        this.listLoading = true
        fetchAddressList(this.listQuery).then(response => {
          this.listLoading = false
          this.list = response.data.list
          this.total = response.data.total
        })
      },
      handleSizeChange(val) {
        this.listQuery.pageNum = 1
        this.listQuery.pageSize = val
        this.getList()
      },
      handleCurrentChange(val) {
        this.listQuery.pageNum = val
        this.getList()
      }
    }
  }
</script>
<style></style>
