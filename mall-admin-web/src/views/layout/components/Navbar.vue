<template>
  <el-menu class="navbar" mode="horizontal">
    <hamburger class="hamburger-container" :toggleClick="toggleSideBar" :isActive="sidebar.opened"></hamburger>
    <breadcrumb></breadcrumb>
    <el-dropdown class="avatar-container" trigger="click">
      <div class="avatar-wrapper">
        <div class="avatar-inner">
          <img class="user-avatar" :src="avatar">
        </div>
        <i class="el-icon-caret-bottom"></i>
      </div>
      <el-dropdown-menu class="user-dropdown" slot="dropdown">
        <router-link class="inlineBlock" to="/">
          <el-dropdown-item>
            首页
          </el-dropdown-item>
        </router-link>
        <el-dropdown-item divided>
          <span @click="handleUpdatePassword" style="display:block;">修改密码</span>
        </el-dropdown-item>
        <el-dropdown-item divided>
          <span @click="logout" style="display:block;">退出</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <el-dialog
      title="修改密码"
      :visible.sync="passwordDialog.dialogVisible"
      width="40%">
      <el-form :model="passwordDialog.data">
        <el-form-item label="旧密码">
          <el-input type="password" v-model="passwordDialog.data.oldPassword" placeholder="输入旧密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input type="password" v-model="passwordDialog.data.newPassword" placeholder="输入新密码"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
            <el-button @click="passwordDialog.dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="submitUpdatePassword()">确 定</el-button>
          </span>
    </el-dialog>
  </el-menu>
</template>

<script>
  import {mapGetters} from 'vuex'
  import Breadcrumb from '@/components/Breadcrumb'
  import Hamburger from '@/components/Hamburger'
  import {updatePassword} from '../../../api/admin'

  export default {
    data() {
      return {
        passwordDialog: {
          dialogVisible: false,
          data: {
            oldPassword: '',
            newPassword: ''
          }
        }
      }
    },
    components: {
      Breadcrumb,
      Hamburger
    },
    computed: {
      ...mapGetters([
        'sidebar',
        'avatar',
        'name'
      ])
    },
    methods: {
      handleUpdatePassword() {
        this.passwordDialog.data = {oldPassword: '', newPassword: ''}
        this.passwordDialog.dialogVisible = true
      },
      async submitUpdatePassword() {
        this.passwordDialog.data.username = this.name
        console.log(this.passwordDialog.data)
        await updatePassword(this.passwordDialog.data)
        this.$message({
          type: 'success',
          message: '提交成功',
          duration: 1000
        })
        this.passwordDialog.dialogVisible = false
      },
      toggleSideBar() {
        this.$store.dispatch('ToggleSideBar')
      },
      logout() {
        this.$store.dispatch('LogOut').then(() => {
          location.reload() // 为了重新实例化vue-router对象 避免bug
        })
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .navbar {
    height: 50px;
    line-height: 50px;
    border-radius: 0px !important;

    .hamburger-container {
      line-height: 58px;
      height: 50px;
      float: left;
      padding: 0 10px;
    }

    .screenfull {
      position: absolute;
      right: 90px;
      top: 16px;
      color: red;
    }

    .avatar-container {
      height: 50px;
      display: inline-block;
      position: absolute;
      right: 35px;

      .avatar-wrapper {
        cursor: pointer;
        margin-top: 5px;
        position: relative;

        .avatar-inner {
          position: relative;
          width: 40px;
          height: 40px;

          .user-avatar {
            position: absolute;
            left: 50%;
            transform: translateX(-50%);
            width: auto;
            height: 100%;
            border-radius: 10px;
          }
        }

        .el-icon-caret-bottom {
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
</style>

