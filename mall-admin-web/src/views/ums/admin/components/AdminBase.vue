<template>
  <div style="margin-top: 50px">
    <el-form :model="value" :rules="rules" ref="adminBaseInfoForm" label-width="120px" style="width: 600px"
             size="small">
      <!--      <el-form-item label="商品名称：" prop="name" v-if="isEdit">-->
      <!--        <el-input v-model="value.id" disabled></el-input>-->
      <!--      </el-form-item>-->
      <el-form-item label="头像" prop="subTitle">
        <single-upload v-model="value.icon"></single-upload>
      </el-form-item>
      <el-form-item label="邮件" prop="subTitle">
        <el-input type="email" v-model="value.email"></el-input>
      </el-form-item>
      <el-form-item label="名称" prop="subTitle">
        <el-input v-model="value.username"></el-input>
      </el-form-item>
      <el-form-item label="昵称" prop="subTitle">
        <el-input v-model="value.nickName"></el-input>
      </el-form-item>
      <el-form-item label="商品介绍：">
        <el-input
          :autoSize="true"
          v-model="value.note"
          type="textarea"
          placeholder="请输入内容"></el-input>
      </el-form-item>
      <el-form-item label="管理员状态：">
        <el-select
          v-model="value.status">
          <el-option v-for="item in adminStatusOptions" :key="item.value" :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item style="text-align: center">
        <el-button type="primary" size="medium" @click="handleNext('productInfoForm')">下一步，编辑管理员角色</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import SingleUpload from '../../../components/Upload/singleUpload'

  export default {
    name: 'AdminBaseInfo',
    props: {
      value: Object,
      isEdit: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        hasEditCreated: false,
        adminStatusOptions: [{value: 0, label: '禁用'}, {value: 1, label: '激活'}],
        //选中商品分类的值
        selectProductCateValue: [],
        productCateOptions: [],
        brandOptions: [],
        rules: {
          name: [
            {required: true, message: '请输入商品名称', trigger: 'blur'},
            {min: 2, max: 140, message: '长度在 2 到 140 个字符', trigger: 'blur'}
          ],
          subTitle: [{required: true, message: '请输入商品副标题', trigger: 'blur'}],
          productCategoryId: [{required: true, message: '请选择商品分类', trigger: 'blur'}],
          brandId: [{required: true, message: '请选择商品品牌', trigger: 'blur'}],
          description: [{required: true, message: '请输入商品介绍', trigger: 'blur'}],
          requiredProp: [{required: true, message: '该项为必填项', trigger: 'blur'}]
        }
      }
    },
    components: {SingleUpload},
    created() {
      console.log(this.value)
    },
    computed: {
      //商品的编号
      productId() {
        return this.value.id
      }
    },
    methods: {
      //处理编辑逻辑
      handleEditCreated() {
        if (this.value.productCategoryId != null) {
          this.selectProductCateValue.push(this.value.cateParentId)
          this.selectProductCateValue.push(this.value.productCategoryId)
        }
        this.hasEditCreated = true
      },
      getProductCateList() {
        fetchListWithChildren().then(response => {
          let list = response.data
          this.productCateOptions = []
          for (let i = 0; i < list.length; i++) {
            let children = []
            if (list[i].children != null && list[i].children.length > 0) {
              for (let j = 0; j < list[i].children.length; j++) {
                children.push({label: list[i].children[j].name, value: list[i].children[j].id})
              }
            }
            this.productCateOptions.push({label: list[i].name, value: list[i].id, children: children})
          }
        })
      },
      getBrandList() {
        fetchBrandList({pageNum: 1, pageSize: 100}).then(response => {
          this.brandOptions = []
          let brandList = response.data.list
          for (let i = 0; i < brandList.length; i++) {
            this.brandOptions.push({label: brandList[i].name, value: brandList[i].id})
          }
        })
      },
      getCateNameById(id) {
        let name = null
        for (let i = 0; i < this.productCateOptions.length; i++) {
          for (let j = 0; j < this.productCateOptions[i].children.length; j++) {
            if (this.productCateOptions[i].children[j].value === id) {
              name = this.productCateOptions[i].children[j].label
              return name
            }
          }
        }
        return name
      },
      handleNext(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$emit('nextStep')
          } else {
            this.$message({
              message: '验证失败',
              type: 'error',
              duration: 1000
            })
            return false
          }
        })
      },
      handleBrandChange(val) {
        let brandName = ''
        for (let i = 0; i < this.brandOptions.length; i++) {
          if (this.brandOptions[i].value === val) {
            brandName = this.brandOptions[i].label
            break
          }
        }
        this.value.brandName = brandName
      }
    }
  }
</script>

<style scoped>
</style>
