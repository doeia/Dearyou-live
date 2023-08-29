<template> 
  <div class="app-container">
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button
        class="btn-add"
        @click.stop.prevent="handleAddAnnouncement()"
        v-if="hasPermission('cms:announcement:create')"
        size="mini">
        添加
      </el-button>
    </el-card>
    <div class="table-container">
      <el-table ref="productTable"
                :data="list"
                style="width: 100%"
                v-loading="listLoading"
                @selection-change="handleSelectionChange"
                border>
        <el-table-column type="selection" width="60" align="center"></el-table-column>
        <el-table-column label="编号" width="150" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>
        <el-table-column label="标题" width="200" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.title}}</p>
          </template>
        </el-table-column>
        <el-table-column label="阅读次数" width="140px" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.readCount}}次</p>
          </template>
        </el-table-column>
        <!--        <el-table-column label="公告类型" align="center">-->
        <!--          <template slot-scope="scope">-->
        <!--            <p>{{scope.row.type}}</p>-->
        <!--          </template>-->
        <!--        </el-table-column>-->
        <el-table-column label="创建时间" align="center">
          <template slot-scope="scope">
            <p>{{formatDate(scope.row.createTime)}}</p>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center"
                         v-if="hasPermission('cms:announcement:update')||hasPermission('cms:announcement:deleteAll')||hasPermission('cms:announcement:delete')">
          <template slot-scope="scope">
            <p>
              <el-button
                size="mini"
                v-if="hasPermission('cms:announcement:update')"
                @click="handleUpdateAnnouncement(scope.row.id)">编辑
              </el-button>
              <el-button
                size="mini"
                type="danger"
                v-if="hasPermission('cms:announcement:deleteAll')||hasPermission('cms:announcement:delete')"
                @click="handleDelete(scope.row.id)">删除
              </el-button>
            </p>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="batch-operate-container" v-if="hasPermission('cms:announcement:deleteAll')">
      <el-select
        size="small"
        v-model="operateType" placeholder="批量操作">
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
    deleteBatch
  } from '@/api/announcement'
  import {utilsMixin, permissionMixin} from '../../../utils/mixins'

  const defaultListQuery = {
    pageNum: 1,
    pageSize: 5
  }
  export default {
    name: 'announcementList',
    mixins: [utilsMixin, permissionMixin],
    data() {
      return {
        listQuery: Object.assign({}, defaultListQuery),
        multipleSelection: [],
        list: null,
        total: null,
        listLoading: true,
        operateType: null,
        operates: [{
          label: '批量删除',
          value: 'delete'
        }]
      }
    },
    created() {
      this.getList()
    },
    methods: {
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      handleAddAnnouncement() {
        this.$router.push({path: '/cms/announcement/add'})
      },
      handleUpdateAnnouncement(id) {
        this.$router.push({path: '/cms/announcement/update', query: {id}})
      },
      handleDelete(id) {
        this.$confirm('是否要进行删除操作?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteBatch([id])
        })
      },
      getList() {
        this.listLoading = true
        fetchList(this.listQuery).then(response => {
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
      },
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
            message: '请选择要操作的公告',
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
            case 'delete':
              this.deleteBatch(ids)
              break
            default:
              break
          }
        })
      },
      deleteBatch(ids) {
        let params = new URLSearchParams()
        params.append('ids', ids)
        deleteBatch(params).then(response => {
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
<style></style>
