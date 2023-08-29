<template> 
  <div class="app-container">
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button
        class="btn-add"
        @click.stop.prevent="handleAddPermission()"
        v-if="hasPermission('ums:permission:create')"
        size="mini">
        添加
      </el-button>
      <el-button
        style="margin-right: 10px"
        class="btn-add"
        @click.stop.prevent="handleFetchPermissionTreeList()"
        size="mini">
        显示权限树
      </el-button>
    </el-card>
    <div class="table-container">
      <el-table ref="productTable"
                :data="list"
                row-key="id"
                :tree-props="{children:'children'}"
                style="width: 100%"
                v-loading="listLoading"
                border>
        <!--        <el-table-column label="编号" width="100" align="center">-->
        <!--          <template slot-scope="scope">{{scope.row.id}}</template>-->
        <!--        </el-table-column>-->
        <el-table-column label="名称" width="300" align="left">
          <template slot-scope="scope">{{scope.row.name}}</template>
        </el-table-column>
        <el-table-column label="权限归档" width="200" align="center">
          <template slot-scope="scope">
            {{getCategory(scope.row)}}
          </template>
        </el-table-column>
        <el-table-column label="权限标识" width="300" align="center">
          <template slot-scope="scope">{{scope.row.value}}</template>
        </el-table-column>
        <!--        <el-table-column label="路由" width="200" align="center">-->
        <!--          <template slot-scope="scope">-->
        <!--            {{scope.row.uri}}-->
        <!--          </template>-->
        <!--        </el-table-column>-->
        <!--        <el-table-column label="权重" align="center">-->
        <!--          <template slot-scope="scope">-->
        <!--            {{scope.row.sort}}-->
        <!--          </template>-->
        <!--        </el-table-column>-->
        <el-table-column label="权限类型" align="center">
          <template slot-scope="scope">
            {{scope.row.type?(scope.row.type===1?'菜单':'按钮'):'目录'}}
          </template>
        </el-table-column>
        <!--        <el-table-column label="创建时间" width="300" align="center">-->
        <!--          <template slot-scope="scope">-->
        <!--            <p>{{formatDate(scope.row.createTime)}}</p>-->
        <!--          </template>-->
        <!--        </el-table-column>-->
        <el-table-column label="操作" width="160" align="center" fixed="right"
                         v-if="hasPermission('ums:permission:update')||hasPermission('ums:permission:delete')">
          <template slot-scope="scope">
            <p>
              <el-button
                size="mini"
                v-if="hasPermission('ums:permission:update')"
                @click="handleUpdatePermission(scope.row)">编辑
              </el-button>
              <el-button
                size="mini"
                type="danger"
                v-if="hasPermission('ums:permission:delete')"
                @click="handleDelete(scope.row)">删除
              </el-button>
            </p>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
      title="添加权限"
      :visible.sync="permissionDialog.dialogVisible"
      width="40%">
      <el-form :model="permissionDialog.role">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="权限名称">
              <el-input v-model="permissionDialog.permission.name" placeholder="请输入权限名称"></el-input>
            </el-form-item>
          </el-col>
          <!--          <el-col :span="8">-->
          <!--            <el-form-item label="权限图标">-->
          <!--              <el-input v-model="permissionDialog.permission.icon" placeholder="例如：user"></el-input>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->
          <!--          <el-col :span="24">-->
          <!--            <el-form-item label="权限权重">-->
          <!--              <el-input-number :controls="false"-->
          <!--                               v-model="permissionDialog.permission.sort"-->
          <!--                               :min="0"-->
          <!--                               placeholder="请输入权限权重"-->
          <!--                               style="display: block; width: 100%;"></el-input-number>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="权限类型">
              <el-select v-model="permissionDialog.permission.type"
                         placeholder="请选择"
                         @change="handleSelectorChange"
                         style="width:100%;">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="权限归档" v-if="permissionDialog.permission.type!==0">
              <el-cascader
                :options="parentList"
                v-model="cascaderSelected"
                style="width:100%;"
                expand-trigger="hover"
              ></el-cascader>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="权限标识">
              <el-input v-model="permissionDialog.permission.value" placeholder="请输入权限标识"></el-input>
            </el-form-item>
          </el-col>
          <!--          <el-col :span="12">-->
          <!--            <el-form-item label="权限路由">-->
          <!--              <el-input v-model="permissionDialog.permission.uri" placeholder="请输入权限路由"></el-input>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
            <el-button @click="permissionDialog.dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="updatePermission()">确 定</el-button>
          </span>
    </el-dialog>
    <el-dialog
      title="权限树"
      :visible.sync="permissionTreeDialog.dialogVisible"
      width="40%">
      <el-tree
        :data="permissionTreeDialog.permissionList"
        show-checkbox
        node-key="id"
        default-expand-all
        ref="tree"
        :props="defaultProps">
      </el-tree>
      <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="permissionTreeDialog.dialogVisible = false">确 定</el-button>
              </span>
    </el-dialog>
  </div>
</template>
<script>
  import {
    fetchList,
    fetchTreeList,
    update,
    create,
    deletePermission
  } from '@/api/permission'
  import {utilsMixin, permissionMixin} from '../../../utils/mixins'

  export default {
    name: 'permissionList',
    mixins: [utilsMixin, permissionMixin],
    data() {
      return {
        options: [{
          label: '目录',
          value: 0
        }, {
          label: '菜单',
          value: 1
        }, {
          label: '按钮',
          value: 2
        }],
        permissionDialog: {
          dialogVisible: false,
          permission: {}
        },
        parentList: [],
        list: null,
        listLoading: true,
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        permissionTreeDialog: {
          dialogVisible: false,
          permissionList: []
        },
        cascaderSelected: []
      }
    },
    created() {
      this.getList()
    },
    methods: {
      getCategory(data) {
        if (data.type === 0) {
          return '顶级目录'
        } else if (data.type === 1) {
          const index = this.list.findIndex(item => {
            return item.id === data.pid
          })
          return this.list[index].name
        } else {
          let grantName = ''
          let parentName = ''
          for (let i = 0; i < this.list.length; i++) {
            grantName = this.list[i].name
            const children = this.list[i].children
            for (let j = 0; j < children.length; j++) {
              if (children[j].id === data.pid) {
                parentName = children[j].name
                break
              }
            }
            if (parentName) {
              break
            }
          }
          return `${grantName}/${parentName}`
        }
      },
      async handleSelectorChange(data) {
        if (data === 0) {
          this.permissionDialog.permission.pid = 0
        } else {
          this.parentList = []
          if (data === 1) {
            this.list.forEach(item => {
              if (item.type === 0) {
                this.parentList.push({label: item.name, value: item.id})
              }
            })
            this.cascaderSelected = [this.parentList[0].value]
          } else {
            const response = await fetchTreeList()
            const tree = response.data
            tree.forEach(item => {
              const _tmp = []
              if (item.children && item.children.length) {
                item.children.forEach(child => {
                  _tmp.push({label: child.name, value: child.id})
                })
                this.parentList.push({label: item.name, value: item.id, children: _tmp})
              }
            })
            this.cascaderSelected = [this.parentList[0].value, this.parentList[0].children[0].value]
          }
        }
      },
      async handleFetchPermissionTreeList() {
        const response = await fetchTreeList()
        this.permissionTreeDialog.permissionList = response.data
        this.permissionTreeDialog.dialogVisible = true
      },
      async handleUpdatePermission(data) {
        this.permissionDialog.permission = this.cloneObj(data)
        const pid = this.permissionDialog.permission.pid
        // console.log(pid)
        const type = this.permissionDialog.permission.type
        if (type !== 0) {
          this.parentList = []
          if (type === 1) {
            this.list.forEach(item => {
              if (item.type === 0) {
                this.parentList.push({label: item.name, value: item.id})
              }
            })
            this.cascaderSelected = [pid]
            this.permissionDialog.dialogVisible = true
          } else {
            const response = await fetchTreeList()
            const tree = response.data

            tree.forEach(item => {
              const _tmp = []
              if (item.children && item.children.length) {
                item.children.forEach(child => {
                  _tmp.push({label: child.name, value: child.id})
                })
                this.parentList.push({label: item.name, value: item.id, children: _tmp})
              }
            })

            let grantParentId = 0
            let parentId = 0
            if (pid !== 0) {
              for (let i = 0; i < tree.length; i++) {
                grantParentId = tree[i].id
                const children = tree[i].children
                for (let j = 0; j < children.length; j++) {
                  if (children[j].id === pid) {
                    parentId = children[j].id
                    break
                  }
                }
                if (parentId) {
                  break
                }
              }
            }
            this.cascaderSelected = [grantParentId, parentId]
            console.log(this.cascaderSelected)
            this.permissionDialog.dialogVisible = true
          }
        } else {
          this.permissionDialog.dialogVisible = true
        }
      },
      updatePermission() {
        const permission = this.permissionDialog.permission
        if (permission.type === 0) {
          permission.pid = 0
        } else {
          permission.pid = this.cascaderSelected[this.cascaderSelected.length - 1]
        }
        if (permission.id) {
          update(permission).then(() => {
            this.permissionDialog.dialogVisible = false
            this.$message({
              type: 'success',
              message: '提交成功',
              duration: 1000
            })
            this.getList()
          })
        } else {
          create(permission).then(() => {
            this.permissionDialog.dialogVisible = false
            this.$message({
              type: 'success',
              message: '提交成功',
              duration: 1000
            })
            this.getList()
          })
        }
      },
      handleAddPermission() {
        this.permissionDialog.permission = {
          name: '',
          icon: '',
          type: 0,
          pid: 0,
          value: '',
          uri: '',
          sort: 0
        }
        this.permissionDialog.dialogVisible = true
      },
      async handleChangeRoleStatus(row) {
        await update(row)
      },
      async getList() {
        this.listLoading = true
        const response = await fetchTreeList()
        this.listLoading = false
        console.log(response)
        this.list = response.data
      },
      handleDelete(data) {
        this.$confirm('是否要进行删除操作?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const params = new URLSearchParams
          params.append('ids', [data.id])
          deletePermission(params).then(() => {
            this.$message({
              type: 'success',
              message: '提交成功',
              duration: 1000
            })
            this.getList()
          })
        })
      }
    }
  }
</script>
<style>
  table tbody tr td {
    padding: 0 !important;
  }
</style>


