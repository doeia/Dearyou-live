<template> 
  <el-card class="form-container" shadow="never">
    <el-form :model="companyAddress" :rules="rules" ref="ruleForm" label-width="120px" style="width: 720px"
             size="small">
      <el-form-item label="地址名称" prop="addressName">
        <el-input v-model="companyAddress.addressName"></el-input>
      </el-form-item>
      <el-form-item label="省份，城市，区域">
        <el-cascader
          placeholder="可搜索"
          :options="regionTree"
          filterable
          clearable
          v-model="address"
          :props="{value:'name',label:'name',children:'children'}"
        ></el-cascader>
      </el-form-item>
      <!--      <el-form-item label="城市">-->
      <!--        <el-input v-model="companyAddress.city"></el-input>-->
      <!--      </el-form-item>-->
      <!--      <el-form-item label="区域">-->
      <!--        <el-input v-model="companyAddress.region"></el-input>-->
      <!--      </el-form-item>-->
      <el-form-item label="详细地址" prop="detailAddress">
        <el-input v-model="companyAddress.detailAddress"></el-input>
      </el-form-item>
      <el-form-item label="收货人名称" prop="name">
        <el-input v-model="companyAddress.name"></el-input>
      </el-form-item>
      <el-form-item label="收货人手机" prop="phone">
        <el-input v-model="companyAddress.phone"></el-input>
      </el-form-item>
      <el-form-item label="是否默认发货地址">
        <el-select v-model="companyAddress.sendStatus">
          <el-option v-for="item in statusOptions"
                     :key="item.value"
                     :value="item.value"
                     :label="item.label"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否默认收获地址">
        <el-select v-model="companyAddress.receiveStatus">
          <el-option v-for="item in statusOptions"
                     :key="item.value"
                     :value="item.value"
                     :label="item.label"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item style="text-align: center">
        <el-button size="medium" @click="$router.back()">返回</el-button>
        <el-button type="primary" size="medium" @click="createOrUpdate()">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
  import {create, update, getInfo} from '@/api/companyAddress'
  import {fetchTreeList} from '../../../../api/region'

  export default {
    name: 'announcementEdit',
    data() {
      return {
        rules: {
          addressName: [
            {required: true, message: '请输入', trigger: 'blur'}
          ],
          detailAddress: [
            {required: true, message: '请输入', trigger: 'blur'}
          ],
          name: [
            {required: true, message: '请输入', trigger: 'blur'}
          ],
          phone: [
            {required: true, pattern: /^1[34578]\d{9}$/, message: '请输入正确手机号码', trigger: 'blur'}
          ]
        },
        companyAddress: {
          addressName: null,
          city: null,
          detailAddress: null,
          id: 0,
          name: null,
          phone: null,
          province: null,
          receiveStatus: 0,
          region: null,
          sendStatus: 0
        },
        address: [],
        regionTree: [],
        statusOptions: [{
          label: '否',
          value: 0
        }, {
          label: '是',
          value: 1
        }]
      }
    },
    props: {
      isEdit: {
        type: Boolean,
        required: true
      }
    },
    async created() {
      if (this.isEdit) {
        this.getInfo()
      }
      this.getTreeList()
    },
    methods: {
      async getInfo() {
        const response = await getInfo(this.$route.query.id)
        this.companyAddress = response.data
        this.address = [this.companyAddress.province, this.companyAddress.city, this.companyAddress.region]
        console.log(this.address)
      },
      async getTreeList() {
        const response = await fetchTreeList()
        this._deleteEmptyChildren(response.data.list)
        this.regionTree = response.data.list
      },
      _deleteEmptyChildren(list) {
        list.forEach(item => {
          if (item.children && item.children.length === 0) {
            delete item.children
          } else {
            this._deleteEmptyChildren(item.children)
          }
        })
      },
      createOrUpdate() {
        this.$refs['ruleForm'].validate((valid) => {
          if (!valid) {
            this.$message({
              type: 'error',
              message: '请正确填写表格',
              duration: 1000
            })
          } else {
            this.companyAddress.province = this.address[0]
            this.companyAddress.city = this.address[1]
            this.companyAddress.region = this.address[2]
            if (this.companyAddress.id) {
              update(this.companyAddress).then(() => {
                this.$message({
                  type: 'success',
                  message: '提交成功',
                  duration: 1000
                })
                this.$router.back()
              })
            } else {
              create(this.companyAddress).then(() => {
                this.$message({
                  type: 'success',
                  message: '提交成功',
                  duration: 1000
                })
                this.$router.back()
              })
            }
          }
        })
      }
    }
  }
</script>
<style>
  .form-container {
    width: 800px;
  }
</style>
