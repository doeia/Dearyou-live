<template> 
  <el-card class="form-container" shadow="never">
    <el-steps :active="active" finish-status="success" align-center>
      <el-step title="编辑管理员信息"></el-step>
      <el-step title="编辑管理员角色"></el-step>
      <el-step title="编辑管理员权限"></el-step>
    </el-steps>
    <admin-base
      v-show="showStatus[0]"
      v-model="adminInfo"
      :is-edit="isEdit"
      @nextStep="nextStep">
    </admin-base>
    <admin-role
      v-show="showStatus[1]"
      v-model="adminRoleIds"
      :is-edit="isEdit"
      @nextStep="nextStep"
      @prevStep="prevStep">
    </admin-role>
    <admin-permission
      v-show="showStatus[2]"
      v-model="adminPermissionIds"
      :is-edit="isEdit"
      @prevStep="prevStep"
      @finishCommit="finishCommit">
    </admin-permission>
  </el-card>
</template>
<script>
  import AdminBase from './AdminBase'
  import AdminRole from './AdminRole'
  import AdminPermission from './AdminPermission'
  import {getDetail, getRole, getPermission, update, updateRole, updatePermission, register} from '@/api/admin'

  export default {
    name: 'adminDetail',
    components: {AdminBase, AdminRole, AdminPermission},
    props: {
      isEdit: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        active: 0,
        adminInfo: {},
        adminRoleIds: [],
        adminPermissionIds: [],
        showStatus: [true, false, false]
      }
    },
    created() {
      if (this.isEdit) {
        const self = this
        getDetail(this.$route.params.id).then(res => {
          self.adminInfo = res.data
        })
        getRole(this.$route.params.id).then(res => {
          res.data.forEach(role => {
            self.adminRoleIds.push(role.id)
          })
        })
        getPermission(this.$route.params.id).then(res => {
          res.data.forEach(permission => {
            self.adminPermissionIds.push(permission.id)
          })
        })
      }
    },
    methods: {
      hideAll() {
        for (let i = 0; i < this.showStatus.length; i++) {
          this.showStatus[i] = false
        }
      },
      prevStep() {
        if (this.active > 0 && this.active < this.showStatus.length) {
          this.active--
          this.hideAll()
          this.showStatus[this.active] = true
        }
      },
      nextStep() {
        if (this.active < this.showStatus.length - 1) {
          this.active++
          this.hideAll()
          this.showStatus[this.active] = true
        }
      },
      async finishCommit(isEdit) {
        await this.$confirm('是否要提交', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        if (isEdit) {
          const adminInfo = await update(this.adminInfo)
          await updateRole({adminId: adminInfo.id, roleIds: this.adminRoleIds})
          await updatePermission({adminId: adminInfo.id, permissionIds: this.adminPermissionIds})
          this.$message({
            type: 'success',
            message: '提交成功',
            duration: 1000
          })
          this.$router.back()
        } else {
          const adminInfo = await register(this.adminInfo)
          await updateRole({adminId: adminInfo.id, roleIds: this.adminRoleIds})
          await updatePermission({adminId: adminInfo.id, permissionIds: this.adminPermissionIds})
          this.$message({
            type: 'success',
            message: '提交成功',
            duration: 1000
          })
          this.$router.back()
        }
      }
    }
  }
</script>
<style>
  .form-container {
    width: 800px;
  }
</style>


