<template> 
  <div class="app-container">
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button
        class="btn-add"
        v-if="hasPermission('cms:regions:create')"
        @click.stop.prevent="handleAddRegion()"
        size="mini">
        添加
      </el-button>
    </el-card>
    <div class="table-container">
      <el-table :data="list"
                style="width: 100%"
                v-loading="listLoading"
                row-key="id"
                border>
        <el-table-column label="名称" prop="name"></el-table-column>
        <el-table-column label="级别" prop="layer">
          <template slot-scope="scope">
            {{scope.row.layer|formatLayer}}
          </template>
        </el-table-column>
        <el-table-column label="操作"
                         v-if="hasPermission('cms:regions:update')||hasPermission('cms:regions:drop')||hasPermission('cms:regions:dropAll')">
          <template slot-scope="scope">
            <p>
              <el-button
                size="mini"
                v-if="hasPermission('cms:regions:update')"
                @click="handleUpdateRegion(scope.row.id)">编辑
              </el-button>
              <el-button
                size="mini"
                type="danger"
                v-if="hasPermission('cms:regions:drop')||hasPermission('cms:regions:dropAll')"
                @click="handleDelete(scope.row.id)">删除
              </el-button>
            </p>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  import {
    fetchTreeList as fetchList,
    drop
  } from '@/api/region'
  import {permissionMixin} from '../../../utils/mixins'

  export default {
    name: 'regionList',
    mixins: [permissionMixin],
    data() {
      return {
        list: null,
        listLoading: true
      }
    },
    filters: {
      formatLayer(value) {
        switch (value) {
          case 1:
            return '省'
          case 2:
            return '市'
          case 3:
            return '区'
          default:
            return '未知'
        }
      }
    },
    created() {
      this.getList()
    },
    methods: {
      handleMouseOver() {
        return
      },
      handleAddRegion() {
        this.$router.push({path: '/cms/region/add'})
      },
      handleUpdateRegion(id) {
        this.$router.push({path: '/cms/region/update', query: {id}})
      },
      handleDelete(id) {
        this.$confirm('是否要进行删除操作?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.delete(id)
        })
      },
      getList() {
        this.listLoading = true
        fetchList(this.listQuery).then(response => {
          this.listLoading = false
          this.list = response.data.list
        })
      },
      delete(id) {
        drop(id).then(() => {
          this.$message({
            message: '处理成功',
            type: 'success',
            duration: 1000
          })
          this.getList()
        })
      }
    }
  }
</script>
<style>
  table tbody tr td {
    padding: 0 !important;
  }

  .el-table__header {
    table-layout: auto !important;
  }
</style>
