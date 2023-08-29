<template> 
  <div class="app-container">
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button
        class="btn-add"
        @click.stop.prevent="handleAddRole()"
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
        <el-table-column label="名称" width="120" align="center">
          <template slot-scope="scope">{{scope.row.name}}</template>
        </el-table-column>
        <el-table-column label="描述" align="center">
          <template slot-scope="scope">{{scope.row.description}}</template>
        </el-table-column>
        <el-table-column label="权限" align="center" v-if="hasPermission('ums:role:update')">
          <template slot-scope="scope">
            <el-button @click="updatePermission(scope.row.id)">编辑权限</el-button>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="140" align="center">
          <template slot-scope="scope">
            <p>{{formatDate(scope.row.createTime)}}</p>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right" align="center"
                         v-if="hasPermission('ums:role:update')||hasPermission('ums:role:delete')">
          <template slot-scope="scope">
            <p>
              <el-button
                size="mini"
                v-if="hasPermission('ums:role:update')"
                @click="handleUpdateRole(scope.$index, scope.row)">编辑
              </el-button>
              <el-button
                size="mini"
                type="danger"
                v-if="hasPermission('ums:role:delete')"
                @click="handleDelete(scope.$index, scope.row)">删除
              </el-button>
            </p>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
      title="添加角色"
      :visible.sync="roleUpdateDialog.dialogVisible"
      width="40%">
      <el-form :model="roleUpdateDialog.role">
        <el-form-item label="角色名称">
          <el-input v-model="roleUpdateDialog.role.name" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item label="角色描述">
          <el-input v-model="roleUpdateDialog.role.description" placeholder="请输入角色描述"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
            <el-button @click="roleUpdateDialog.dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="updateRole()">确 定</el-button>
          </span>
    </el-dialog>
    <el-dialog
      title="权限"
      :visible.sync="rolePermission.dialogVisible"
      width="40%"
      @close="onDialogClose">
      <el-tree
        :data="permissionList"
        show-checkbox
        node-key="id"
        default-expand-all
        check-strictly
        check-on-click-node
        :expand-on-click-node="false"
        ref="tree"
        :props="permissionProps">
      </el-tree>
      <span slot="footer" class="dialog-footer">
            <el-button @click="rolePermission.dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="updateRolePermission(rolePermission)">确 定</el-button>
          </span>
    </el-dialog>
  </div>
</template>
<script>
  import {
    fetchList,
    update,
    deleteRole,
    create,
    getPermission,
    updatePermission
  } from '@/api/role'
  import {fetchTreeList as fetchPermissionList} from '@/api/permission'
  import {utilsMixin, permissionMixin} from '../../../utils/mixins'

  export default {
    name: 'roleList',
    mixins: [utilsMixin, permissionMixin],
    data() {
      return {
        roleUpdateDialog: {
          dialogVisible: false,
          role: {}
        },
        list: null,
        listLoading: true,
        permissionList: [],
        rolePermission: {
          dialogVisible: false,
          permissionIds: []
        },
        permissionProps: {
          children: 'children',
          label: 'name'
        }
      }
    },
    created() {
      this.getList()
      fetchPermissionList().then(res => {
        console.log(res.data)
        this.permissionList = res.data
      })
    },
    methods: {
      onDialogClose() {
        this.rolePermission.permissionIds = []
      },
      async updateRolePermission(data) {
        const params = new URLSearchParams
        params.append('roleId', data.roleId)
        params.append('permissionIds', this.$refs.tree.getCheckedKeys())

        await updatePermission(params)
        this.rolePermission.dialogVisible = false
        this.$message({
          type: 'success',
          message: '提交成功',
          duration: 1000
        })
      },
      async updatePermission(id) {
        this.rolePermission.permissionIds = []
        this.rolePermission.roleId = id
        const response = await getPermission(id)
        response.data.forEach(permission => {
          permission && this.rolePermission.permissionIds.push(permission.id)
        })
        this.rolePermission.dialogVisible = true
        await this.$nextTick()
        this.$refs.tree.setCheckedKeys(this.rolePermission.permissionIds)
      },
      updateRole() {
        const role = this.roleUpdateDialog.role
        console.log(role)
        if (role.id) {
          update(role).then(() => {
            this.$message({
              type: 'success',
              message: '提交成功',
              duration: 1000
            })
            this.roleUpdateDialog.dialogVisible = false
          })
        } else {
          create(role).then(() => {
            this.$message({
              type: 'success',
              message: '提交成功',
              duration: 1000
            })
            this.getList()
            this.roleUpdateDialog.dialogVisible = false
          })
        }
      },
      async handleChangeRoleStatus(row) {
        await update(row)
      },
      getList() {
        this.listLoading = true
        fetchList().then(response => {
          this.listLoading = false
          this.list = response.data
        })
      },
      handleAddRole() {
        this.roleUpdateDialog.dialogVisible = true
        this.roleUpdateDialog.role = {
          name: '',
          description: ''
        }
      },
      handleDelete(index, row) {
        this.$confirm('是否要进行删除操作?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const params = new URLSearchParams
          params.append('ids', [row.id])
          deleteRole(params).then(() => {
            this.$message({
              type: 'success',
              message: '提交成功',
              duration: 1000
            })
            this.getList()
          })
        })
      },
      handleUpdateRole(index, row) {
        this.roleUpdateDialog.dialogVisible = true
        this.roleUpdateDialog.role = row
      }
    }
  }
</script>
<style></style>


