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
          <el-form-item label="会员状态：">
            <el-select v-model="listQuery.status" placeholder="全部" clearable>
              <el-option
                v-for="item in memberStatusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="会员昵称：">
            <el-input style="width: 203px" v-model="listQuery.nickname" placeholder="会员昵称"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button
        class="btn-add"
        v-show="hasPermission('ums:member:create')"
        @click.stop.prevent="handleAddMember()"
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
        <el-table-column label="头像" width="120" align="center">
          <template slot-scope="scope"><img style="height: 80px" :src="scope.row.icon"></template>
        </el-table-column>
        <!--        <el-table-column label="会员等级" width="140" align="center">-->
        <!--          <template slot-scope="scope">{{displayMemberLevel(scope.row.memberLevelId)}}</template>-->
        <!--        </el-table-column>-->
        <el-table-column label="昵称" width="140" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.nickname}}</p>
          </template>
        </el-table-column>
        <el-table-column label="手机" width="140" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.phone}}</p>
          </template>
        </el-table-column>
        <el-table-column label="性别" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.gender===0?'未知':(scope.row.gender===1?'男':'女')}}</p>
          </template>
        </el-table-column>
        <!--        <el-table-column label="生日" width="140" align="center">-->
        <!--          <template slot-scope="scope">-->
        <!--            <p>{{formatDate(scope.row.birthday)}}</p>-->
        <!--          </template>-->
        <!--        </el-table-column>-->
        <el-table-column label="所在城市" width="140" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.city}}</p>
          </template>
        </el-table-column>
        <!--        <el-table-column label="职业" width="140" align="center">-->
        <!--          <template slot-scope="scope">-->
        <!--            <p>{{scope.row.job}}</p>-->
        <!--          </template>-->
        <!--        </el-table-column>-->
        <el-table-column label="身份证" width="200" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.idCard}}</p>
          </template>
        </el-table-column>
        <el-table-column label="个性签名" width="300" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.personalizedSignature}}</p>
          </template>
        </el-table-column>
        <el-table-column label="用户来源" width="140" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.sourceType|formatSourceType}}</p>
          </template>
        </el-table-column>
        <el-table-column label="活动相关" width="140" align="center">
          <template slot-scope="scope">
            <p>积分：{{scope.row.integration}}</p>
            <p>成长值：{{scope.row.growth}}</p>
            <p>剩余抽奖次数：{{scope.row.luckeyCount}}</p>
            <p>历史积分数量：{{scope.row.historyIntegration}}</p>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right"
                         v-if="hasPermission('ums:member:read')||hasPermission('ums:member:update')">
          <template slot-scope="scope">
            <p>
              <el-button
                style="width: 100%;"
                size="mini"
                v-show="hasPermission('ums:member:read')"
                @click="handleViewDetail(scope.row.id)">详情
              </el-button>
            </p>
            <p>
              <el-button
                size="mini"
                v-show="hasPermission('ums:member:update')"
                @click="handleUpdateMember(scope.$index, scope.row)">编辑
              </el-button>
              <el-button
                v-if="scope.row.status"
                size="mini"
                v-show="hasPermission('ums:member:update')"
                type="danger"
                @click="handleDelete(0, scope.row)">删除
              </el-button>
              <el-button
                v-if="!scope.row.status"
                size="mini"
                v-show="hasPermission('ums:member:update')"
                type="primary"
                @click="handleDelete(1, scope.row)">恢复
              </el-button>
            </p>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="batch-operate-container" v-show="hasPermission('ums:member:update')">
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
    updateStatus
  } from '../../../api/member'
  // import {fetchList as fetchMemberLevelList} from '@/api/memberLevel'

  import {utilsMixin, permissionMixin} from '../../../utils/mixins'

  const defaultListQuery = {
    nickname: null,
    status: null,
    pageNum: 1,
    pageSize: 5
  }
  export default {
    name: 'memberList',
    mixins: [utilsMixin, permissionMixin],
    data() {
      return {
        currentListStatus: 1,
        memberLevelList: [],
        operateType: null,
        listQuery: Object.assign({}, defaultListQuery),
        list: null,
        total: null,
        listLoading: true,
        multipleSelection: [],
        memberStatusOptions: [{
          value: 1,
          label: '激活'
        }, {
          value: 0,
          label: '已删除'
        }]
      }
    },
    computed: {
      operates() {
        if (this.currentListStatus) {
          return [
            {
              label: '批量删除',
              value: 'statusOff'
            }
          ]
        } else {
          return [
            {
              label: '批量恢复',
              value: 'statusOn'
            }
          ]
        }
      }
    },
    filters: {
      formatSourceType(value) {
        switch (value) {
          case 0:
            return '电脑'
          case 1:
            return '安卓APP'
          case 2:
            return '苹果APP'
          case 3:
            return '小程序'
          default:
            return '暂无'
        }
      }
    },
    created() {
      this.getList()
      // this.fetchMemberLevelList()
    },
    methods: {
      handleViewDetail(id) {
        this.$router.push({path: '/mms/member/detail', query: {id}})
      },
      displayMemberLevel(levelId) {
        const index = this.memberLevelList.findIndex(item => {
          return item.id === levelId
        })
        if (index !== -1) {
          return this.memberLevelList[index].name
        }
        return ''
      },
      // async fetchMemberLevelList() {
      //   const response = await fetchMemberLevelList()
      //   this.memberLevelList = response.data.list
      // },
      handleAddMember() {
        this.$router.push('/mms/member/add')
      },
      getList() {
        this.listLoading = true
        fetchList(this.listQuery).then(response => {
          this.listLoading = false
          this.list = response.data.list
          this.total = response.data.total
          this.operateType = null
          this.currentListStatus = this.listQuery.status === null ? 1 : 0
        })
      },
      handleSearchList() {
        this.listQuery.pageNum = 1
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
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      handlePublishStatusChange(index, row) {
        let ids = []
        ids.push(row.id)
        this.updatePublishStatus(row.publishStatus, ids)
      },
      handleNewStatusChange(index, row) {
        let ids = []
        ids.push(row.id)
        this.updateNewStatus(row.newStatus, ids)
      },
      handleRecommendStatusChange(index, row) {
        let ids = []
        ids.push(row.id)
        this.updateRecommendStatus(row.recommandStatus, ids)
      },
      handleResetSearch() {
        this.listQuery = Object.assign({}, defaultListQuery)
        this.getList()
      },
      handleDelete(status, row) {
        this.$confirm('是否要进行操作?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids = []
          ids.push(row.id)
          this.updateDeleteStatus(status, ids)
        })
      },
      handleUpdateMember(index, row) {
        this.$router.push({path: '/mms/member/update', query: {id: row.id}})
      },
      handleShowProduct(index, row) {
        console.log('handleShowProduct', row)
      },
      handleShowVerifyDetail(index, row) {
        console.log('handleShowVerifyDetail', row)
      },
      handleShowLog(index, row) {
        console.log('handleShowLog', row)
      },
      updateDeleteStatus(deleteStatus, ids) {
        let params = new URLSearchParams()
        params.append('ids', ids)
        params.append('status', deleteStatus)
        updateStatus(params).then(response => {
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


