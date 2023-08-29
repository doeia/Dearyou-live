<template>
  <div class="app-container">
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets" style="margin-top: 5px"></i>
      <span style="margin-top: 5px">数据列表</span>
      <el-button
        class="btn-add"
        @click="handleAddProductCate()"
        size="mini">
        添加
      </el-button>
    </el-card>
    <div class="table-container">
      <el-table ref="productCateTable"
                style="width: 100%"
                :data="list"
                row-key="id"
                v-loading="listLoading" border>
        <el-table-column label="分类名称" width="200" align="left">
          <template slot-scope="scope">{{scope.row.name}}</template>
        </el-table-column>
        <el-table-column label="编号" width="100" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>
        <el-table-column label="级别" width="100" align="center">
          <template slot-scope="scope">{{scope.row.level | levelFilter}}</template>
        </el-table-column>
        <el-table-column label="商品数量" width="100" align="center">
          <template slot-scope="scope">{{scope.row.productCount }}</template>
        </el-table-column>
        <el-table-column label="数量单位" width="100" align="center">
          <template slot-scope="scope">{{scope.row.productUnit }}</template>
        </el-table-column>
        <el-table-column label="导航栏" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              @change="handleNavStatusChange(scope.$index, scope.row)"
              :active-value="1"
              :inactive-value="0"
              v-model="scope.row.navStatus">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="是否显示" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              @change="handleShowStatusChange(scope.$index, scope.row)"
              :active-value="1"
              :inactive-value="0"
              v-model="scope.row.showStatus">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="排序" width="100" align="center">
          <template slot-scope="scope">{{scope.row.sort }}</template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleUpdate(scope.$index, scope.row)">编辑
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
  import {fetchTreeList, deleteProductCate, updateShowStatus, updateNavStatus} from '../../../api/productCate'

  export default {
    name: 'productCateList',
    data() {
      return {
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          pageNum: 1,
          pageSize: 5
        },
        parentId: 0
      }
    },
    created() {
      this.resetParentId()
      this.getList()
    },
    methods: {
      resetParentId() {
        this.listQuery.pageNum = 1
        if (this.$route.query.parentId != null) {
          this.parentId = this.$route.query.parentId
        } else {
          this.parentId = 0
        }
      },
      handleAddProductCate() {
        this.$router.push('/pms/addProductCate')
      },
      getList() {
        this.listLoading = true
        fetchTreeList().then(response => {
          this.listLoading = false
          this.list = response.data
        })
      },
      handleNavStatusChange(index, row) {
        let data = new URLSearchParams()
        let ids = []
        ids.push(row.id)
        data.append('ids', ids)
        data.append('navStatus', row.navStatus)
        updateNavStatus(data).then(response => {
          this.$message({
            message: '修改成功',
            type: 'success',
            duration: 1000
          })
        })
      },
      handleShowStatusChange(index, row) {
        let data = new URLSearchParams()
        let ids = []
        ids.push(row.id)
        data.append('ids', ids)
        data.append('showStatus', row.showStatus)
        updateShowStatus(data).then(response => {
          this.$message({
            message: '修改成功',
            type: 'success',
            duration: 1000
          })
        })
      },
      handleUpdate(index, row) {
        this.$router.push({path: '/pms/updateProductCate', query: {id: row.id}})
      },
      handleDelete(index, row) {
        this.$confirm('是否要删除该品牌', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteProductCate(row.id).then(response => {
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 1000
            })
            this.getList()
          })
        })
      }
    },
    filters: {
      levelFilter(value) {
        if (value === 0) {
          return '一级'
        } else if (value === 1) {
          return '二级'
        } else {
          return '三级'
        }
      }
    }
  }
</script>

<style scoped>

</style>
