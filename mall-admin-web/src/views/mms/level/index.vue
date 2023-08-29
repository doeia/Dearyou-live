<template> 
  <div class="app-container">
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button
        class="btn-add"
        @click.stop.prevent="handleAddLevel()"
        size="mini">
        添加
      </el-button>
    </el-card>
    <div class="table-container">
      <el-table ref="productTable"
                :data="list"
                style="width: 100%"
                @selection-change="handleSelectionChange"
                v-loading="listLoading"
                border>
        <el-table-column type="selection" width="60" align="center"></el-table-column>
        <el-table-column label="编号" width="150" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>
        <el-table-column label="等级名称" width="140" align="center">
          <template slot-scope="scope">{{scope.row.name}}</template>
        </el-table-column>
        <el-table-column label="等级说明" width="140" align="center">
          <template slot-scope="scope">{{scope.row.note}}</template>
        </el-table-column>
        <el-table-column label="成长值点数" width="140" align="center">
          <template slot-scope="scope">
            {{scope.row.growthPoint}}
          </template>
        </el-table-column>
        <el-table-column label="免运费标准" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.freeFreightPoint}}</p>
          </template>
        </el-table-column>
        <el-table-column label="每次评论获得的成长值" width="140" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.commentGrowthPoint}}</p>
          </template>
        </el-table-column>
        <el-table-column label="是否有免邮特权" width="140" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.priviledgeFreeFreight?'是':'否'}}</p>
          </template>
        </el-table-column>
        <el-table-column label="是否有签到特权" width="140" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.priviledgeSignIn?'是':'否'}}</p>
          </template>
        </el-table-column>
        <el-table-column label="是否有评论获奖励特权" width="140" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.priviledgeComment?'是':'否'}}</p>
          </template>
        </el-table-column>
        <el-table-column label="是否有专享活动特权" width="140" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.priviledgePromotion?'是':'否'}}</p>
          </template>
        </el-table-column>
        <el-table-column label="是否有会员价格特权" width="140" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.priviledgeMemberPrice?'是':'否'}}</p>
          </template>
        </el-table-column>
        <el-table-column label="是否有生日特权" width="140" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.priviledgeBirthday?'是':'否'}}</p>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="scope">
            <p>
              <el-button
                size="mini"
                @click="handleUpdateLevel(scope.$index, scope.row)">编辑
              </el-button>
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除
              </el-button>
            </p>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="batch-operate-container">
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
  </div>
</template>
<script>
  import {
    fetchList,
    deleteBatch
  } from '@/api/memberLevel'

  import {utilsMixin} from '../../../utils/mixins'

  export default {
    name: 'memberList',
    mixins: [utilsMixin],
    data() {
      return {
        operates: [
          {
            label: '批量删除',
            value: 'delete'
          }
        ],
        operateType: null,
        listQuery: {},
        list: null,
        total: null,
        listLoading: true,
        multipleSelection: []
      }
    },
    created() {
      this.getList()
    },
    methods: {
      handleAddLevel() {
        this.$router.push('/mms/level/add')
      },
      getList() {
        this.listLoading = true
        fetchList().then(response => {
          this.listLoading = false
          this.list = response.data.list
          this.total = response.data.total
        })
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
            message: '请选择要操作的会员等级',
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
              this.updateDeleteStatus(ids)
              break
            default:
              break
          }
          this.getList()
        })
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      handleResetSearch() {
        this.listQuery = Object.assign({}, defaultListQuery)
      },
      handleDelete(index, row) {
        this.$confirm('是否要进行删除操作?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids = []
          ids.push(row.id)
          this.updateDeleteStatus(ids)
        })
      },
      handleUpdateLevel(index, row) {
        this.$router.push({path: '/mms/level/update', query: {id: row.id}})
      },
      updateDeleteStatus(ids) {
        let params = new URLSearchParams()
        params.append('ids', ids)
        deleteBatch(params).then(() => {
          this.$message({
            message: '删除成功',
            type: 'success',
            duration: 1000
          })
        })
        this.getList()
      }
    }
  }
</script>
<style></style>


