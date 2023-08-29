<template> 
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          @click="handleSearchList()"
          type="primary"
          size="small">
          查询结果
        </el-button>
        <el-button
          style="float: right;margin-right: 15px"
          @click="handleResetSearch()"
          size="small">
          重置
        </el-button>
      </div>
      <div style="margin-top: 15px">
        <el-form :inline="true" :model="listQuery" size="small" label-width="140px">
          <el-form-item label="输入搜索：">
            <el-input style="width: 203px" v-model="listQuery.admin" placeholder="管理员名称"></el-input>
          </el-form-item>
          <el-form-item label="是否删除：">
            <el-select v-model="listQuery.deleted" placeholder="是否删除" clearable>
              <el-option
                v-for="item in deletedOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
    </el-card>
    <div class="table-container">
      <el-table ref="productTable"
                :data="list"
                style="width: 100%"
                @selection-change="handleSelectionChange"
                v-loading="listLoading"
                border>
        <el-table-column type="selection" width="60" align="center"></el-table-column>
        <el-table-column label="编号" width="100" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>
        <el-table-column label="操作者" width="120" align="center">
          <template slot-scope="scope">{{scope.row.admin}}</template>
        </el-table-column>
        <el-table-column label="IP地址" width="200" align="center">
          <template slot-scope="scope">{{scope.row.ip}}</template>
        </el-table-column>
        <el-table-column label="请求地址" width="300" align="center">
          <template slot-scope="scope">{{scope.row.url}}</template>
        </el-table-column>
        <el-table-column label="请求方法" width="120" align="center">
          <template slot-scope="scope">{{scope.row.method}}</template>
        </el-table-column>
        <el-table-column label="操作类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type" type="primary">安全操作</el-tag>
            <el-tag v-if="!scope.row.type" type="warning">未知操作</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作内容" width="300" align="center">
          <template slot-scope="scope">{{scope.row.action}}</template>
        </el-table-column>
        <el-table-column label="操作状态" width="120" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status" type="success">成功</el-tag>
            <el-tag v-if="!scope.row.status" type="danger">失败</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="备注" width="120" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.comment}}</p>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="140" align="center">
          <template slot-scope="scope">
            <p>{{formatDate(scope.row.createTime)}}</p>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="scope">
            <p>
              <el-button
                size="mini"
                v-if="hasPermission('ums:sysLog:read')"
                @click="handleViewDetail(scope.row.id)">查看
              </el-button>
              <el-button
                v-if="scope.row.status && hasPermission('ums:sysLog:dropAll')"
                size="mini"
                type="danger"
                @click="handleDelete(1, scope.row.id)">删除
              </el-button>
              <el-button
                v-if="!scope.row.status && hasPermission('ums:sysLog:recoverAll')"
                size="mini"
                type="primary"
                @click="handleDelete(0, scope.row.id)">恢复
              </el-button>
            </p>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="batch-operate-container">
      <el-select
        size="small"
        v-model="operateType"
        placeholder="批量操作">
        <el-option
          v-for="item in operates"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button
        style="margin-left: 20px"
        class="search-button"
        @click="handleBatchOperate()"
        type="primary"
        size="small">
        确定
      </el-button>
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
    fetchList,
    dropBatch,
    recoverBatch
  } from '@/api/log'
  import {formatDate} from '@/utils/date'
  import {permissionMixin} from '../../../utils/mixins'

  const defaultListQuery = {
    admin: null,
    deleted: null,
    pageNum: 1,
    pageSize: 5
  }
  export default {
    name: 'log',
    mixins: [permissionMixin],
    data() {
      return {
        operateType: null,
        currentListType: null,
        listQuery: Object.assign({}, defaultListQuery),
        list: null,
        total: null,
        listLoading: true,
        multipleSelection: [],
        deletedOptions: [
          {label: '已删除', value: 1},
          {label: '未删除', value: 0}
        ]
      }
    },
    computed: {
      operates() {
        if (this.currentListType) {
          return [
            {
              label: '批量恢复',
              value: 'statusOn'
            }
          ]
        } else {
          return [
            {
              label: '批量删除',
              value: 'statusOff'
            }
          ]
        }
      }
    },
    created() {
      this.getList()
    },
    methods: {
      handleBatchOperate() {
        if (this.operateType == null) {
          this.$message({
            message: '请选择操作类型',
            type: 'warning',
            duration: 1000
          })
          return
        }
        if (this.multipleSelection == null || this.multipleSelection.length < 1) {
          this.$message({
            message: '请选择要操作的会员',
            type: 'warning',
            duration: 1000
          })
          return
        }
        this.$confirm('是否要进行该批量操作?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids = []
          for (let i = 0; i < this.multipleSelection.length; i++) {
            ids.push(this.multipleSelection[i].id)
          }
          switch (this.operateType) {
            case 'statusOff':
              this.updateDeleteStatus(0, ids)
              break
            case 'statusOn':
              this.updateDeleteStatus(1, ids)
              break
            default:
              break
          }
          this.getList()
        })
      },
      handleDelete(status, id) {
        this.$confirm('是否要进行删除操作?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids = []
          ids.push(id)
          this.updateDeleteStatus(status, ids)
        })
      },
      updateDeleteStatus(deleteStatus, ids) {
        let params = new URLSearchParams()
        params.append('ids', ids)
        if (!deleteStatus) {
          recoverBatch(params).then(response => {
            this.$message({
              message: '恢复成功',
              type: 'success',
              duration: 1000
            })
            this.getList()
          })
        } else {
          dropBatch(params).then(response => {
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 1000
            })
          })
          this.getList()
        }
      },
      handleViewDetail(id) {
        this.$router.push({path: '/ums/syslog/detail', query: {id}})
      },
      formatDate(data) {
        return formatDate(new Date(data), 'yyyy-MM-dd hh:mm')
      },
      getList() {
        this.listLoading = true
        fetchList(this.listQuery).then(response => {
          this.listLoading = false
          this.list = response.data.list
          this.total = response.data.total
          this.operateType = null
          this.currentListType = this.listQuery.deleted
        })
      },
      handleSearchList() {
        this.listQuery.pageNum = 1
        this.getList()
      },
      handleSizeChange(val) {
        this.listQuery.pageNum = 1
        this.listQuery.pageSize = val
        this.getList()
      },
      handleCurrentChange(val) {
        this.listQuery.pageNum = val
        this.getList()
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      handleResetSearch() {
        this.listQuery = Object.assign({}, defaultListQuery)
        this.getList()
      }
    }
  }
</script>
<style></style>


