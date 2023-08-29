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
            <el-input style="width: 203px" v-model="listQuery.name" placeholder="管理员名称"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button
        class="btn-add"
        @click.stop.prevent="handleAddAdmin()"
        v-if="hasPermission('ums:admin:create')"
        size="mini">
        添加
      </el-button>
    </el-card>
    <div class="table-container">
      <el-table ref="productTable"
                :data="list"
                style="width: 100%"
                v-loading="listLoading"
                border>
        <el-table-column type="selection" width="60" align="center"></el-table-column>
        <el-table-column label="编号" width="100" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>
        <el-table-column label="头像" width="120" align="center">
          <template slot-scope="scope"><img style="height: 80px" :src="scope.row.icon"></template>
        </el-table-column>
        <el-table-column label="管理员名称" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.username}}</p>
            <p>昵称：{{scope.row.nickName}}</p>
          </template>
        </el-table-column>
        <el-table-column label="邮箱" width="120" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.email}}</p>
          </template>
        </el-table-column>
        <el-table-column label="备注" width="140" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.note}}</p>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="140" align="center">
          <template slot-scope="scope">
            <p>{{formatDate(scope.row.createTime)}}</p>
          </template>
        </el-table-column>
        <el-table-column label="登录时间" width="140" align="center">
          <template slot-scope="scope">
            <p>{{formatDate(scope.row.loginTime)}}</p>
          </template>
        </el-table-column>
        <el-table-column label="角色与权限" width="140" align="center" v-if="hasPermission('ums:admin:update')">
          <template slot-scope="scope">
            <p>
              <el-button
                size="mini"
                v-if="hasPermission('ums:admin:update')"
                @click="fetchAdminRole(scope.row.id)">编辑角色
              </el-button>
            </p>
            <p>
              <el-button
                size="mini"
                v-if="hasPermission('ums:admin:update')"
                @click="fetchAdminPermission(scope.row.id)">编辑权限
              </el-button>
            </p>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right" align="center"
                         v-if="hasPermission('ums:admin:update')||hasPermission('ums:admin:delete')">
          <template slot-scope="scope">
            <p>
              <el-button
                size="mini"
                v-if="hasPermission('ums:admin:update')"
                @click="handleUpdateAdmin(scope.row)">编辑
              </el-button>
              <el-button
                size="mini"
                type="danger"
                v-if="hasPermission('ums:admin:delete')"
                @click="handleDelete(scope.row)">删除
              </el-button>
            </p>
          </template>
        </el-table-column>
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
    <el-dialog
      title="编辑角色"
      :visible.sync="adminRoleDialog.dialogVisible"
      width="40%">
      <el-checkbox-group v-model="adminRoleDialog.roleIds">
        <el-checkbox v-for="role in roleList" :key="role.id" :label="role.id">{{role.name}}</el-checkbox>
      </el-checkbox-group>
      <span slot="footer" class="dialog-footer">
        <el-button @click="adminRoleDialog.dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateAdminRole()">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="权限"
      :visible.sync="adminPermissionDialog.dialogVisible"
      width="40%">
      <el-tree
        :data="permissionList"
        show-checkbox
        node-key="id"
        ref="tree"
        check-strictly
        default-expand-all
        check-on-click-node
        :expand-on-click-node="false"
        :props="permissionProps">
      </el-tree>
      <span slot="footer" class="dialog-footer">
        <el-button @click="adminPermissionDialog.dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click.stop.prevent="updateAdminPermission()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
  import {
    fetchList,
    getRole,
    getPermission,
    update,
    updateRole,
    updatePermission,
    deleteAdmin
  } from '@/api/admin'
  import {
    fetchList as fetchRoleList
  } from '@/api/role'
  import {
    fetchTreeList as fetchPermissionList
  } from '@/api/permission'
  import {utilsMixin, permissionMixin} from '../../../utils/mixins'

  const defaultListQuery = {
    name: null,
    pageNum: 1,
    pageSize: 5
  }
  export default {
    name: 'adminList',
    mixins: [utilsMixin, permissionMixin],
    data() {
      return {
        adminRoleDialog: {
          dialogVisible: false,
          roleIds: [],
          adminId: ''
        },
        adminPermissionDialog: {
          dialogVisible: false,
          permissionIds: [],
          adminId: ''
        },
        permissionList: [],
        permissionProps: {
          children: 'children',
          label: 'name'
        },
        roleList: [],
        listQuery: Object.assign({}, defaultListQuery),
        list: null,
        total: null,
        listLoading: true
      }
    },
    created() {
      this.getList()
      this.fetchRoleList()
      this.fetchPermissionList()
    },
    methods: {
      async updateAdminRole() {
        const data = this.adminRoleDialog
        const params = new URLSearchParams
        params.append('adminId', data.adminId)
        params.append('roleIds', data.roleIds)
        await updateRole(params)
        this.$message({
          type: 'success',
          message: '提交成功',
          duration: 1000
        })
        data.dialogVisible = false
      },
      async updateAdminPermission() {
        const data = this.adminPermissionDialog
        const params = new URLSearchParams
        params.append('adminId', data.adminId)
        params.append('permissionIds', this.$refs.tree.getCheckedKeys())
        await updatePermission(params)
        this.$message({
          type: 'success',
          message: '提交成功',
          duration: 1000
        })
        data.dialogVisible = false
      },
      async fetchRoleList() {
        const res = await fetchRoleList()
        this.roleList = res.data
      },
      async fetchPermissionList() {
        const res = await fetchPermissionList()
        this.permissionList = res.data
      },
      async fetchAdminRole(adminId) {
        this.adminRoleDialog.roleIds = []
        const response = await getRole(adminId)
        response.data.forEach(role => {
          role && this.adminRoleDialog.roleIds.push(role.id)
        })
        this.adminRoleDialog.adminId = adminId
        this.adminRoleDialog.dialogVisible = true
      },
      async fetchAdminPermission(adminId) {
        this.adminPermissionDialog.permissionIds = []
        const response = await getPermission(adminId)
        const permissionList = response.data
        permissionList.forEach(permission => {
          permission && this.adminPermissionDialog.permissionIds.push(permission.id)
        })
        this.adminPermissionDialog.adminId = adminId
        this.adminPermissionDialog.dialogVisible = true
        await this.$nextTick()
        this.$refs.tree.setCheckedKeys(this.adminPermissionDialog.permissionIds)
      },
      async getList() {
        this.listLoading = true
        const response = await fetchList(this.listQuery)
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
      },
      handleSearchList() {
        this.listQuery.pageNum = 1
        this.getList()
      },
      handleAddAdmin() {
        this.$router.push({path: '/ums/admin/add'})
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
      handleResetSearch() {
        this.listQuery = Object.assign({}, defaultListQuery)
        this.getList()
      },
      handleDelete(row) {
        this.$confirm('是否要进行删除操作?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteAdmin(row.id).then(() => {
            this.$message({
              type: 'success',
              message: '提交成功',
              duration: 1000
            })
            this.getList()
          })
        })
      },
      handleUpdateAdmin(row) {
        this.$router.push({name: 'adminUpdate', params: row})
      }
    }
  }
</script>
<style></style>


