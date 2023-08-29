<template>
  <div class="detail-container" style="margin-left: 200px">
    <div style="margin:15px 0;text-align: left" v-if="!product.verifyStatus">
      <el-button type="primary" size="small" @click="handleVerify">审核通过</el-button>
    </div>
    <el-alert
      :title="product.verifyStatus?'审核通过':'审核未通过'"
      :type="product.verifyStatus?'success':'warning'">
    </el-alert>
    <el-card shadow="never" class="standard-margin">
      <span class="font-title-medium">商品基本信息</span>
      <div class="form-container-border">
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">编号</el-col>
          <el-col class="form-border font-small" :span="18">{{product.productSn}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">商品名称</el-col>
          <el-col class="form-border font-small" :span="18">{{product.name||'暂无'}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">次级标题</el-col>
          <el-col class="form-border font-small" :span="18">{{product.subTitle||'暂无'}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">商品分类</el-col>
          <el-col class="form-border font-small" :span="18">{{product.productCategoryName||'暂无'}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">品牌</el-col>
          <el-col class="form-border font-small" :span="18">{{product.brandName||'暂无'}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">品牌分类</el-col>
          <el-col class="form-border font-small" :span="18">
            {{product.brandCategoryName||'暂无'}}
          </el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">商品属性
          </el-col>
          <el-col class="form-border font-small" :span="18">
            {{productAttr||'暂无'}}
          </el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6" style="height:100px;line-height:80px">产品图
          </el-col>
          <el-col class="form-border font-small" :span="18" style="height:100px">
            <img style="width:80px;height:80px" :src="product.pic">
          </el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6" style="height:100px;line-height:80px">产品相册
          </el-col>
          <el-col class="form-border font-small" :span="18" style="height:100px">
            <img v-for="item in product.albumPics?product.albumPics.split(','):[]" style="width:80px;height:80px"
                 :src="item">
          </el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">市场价</el-col>
          <el-col class="form-border font-small" :span="18">¥{{product.originalPrice||'暂无'}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">售价</el-col>
          <el-col class="form-border font-small" :span="18">¥{{product.price||'暂无'}}</el-col>
        </el-row>
      </div>
    </el-card>

    <el-card shadow="never" class="standard-margin" style="margin-top: 20px;">
      <span class="font-title-medium">库存相关</span>
      <div class="form-container-border">
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">单位</el-col>
          <el-col class="form-border font-small" :span="18">{{product.unit||'暂无'}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">重量</el-col>
          <el-col class="form-border font-small" :span="18">{{product.weight}}g</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">锁定库存</el-col>
          <el-col class="form-border font-small" :span="18">{{product.lockStock}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">库存告警阀值</el-col>
          <el-col class="form-border font-small" :span="18">{{product.lowStock}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">库存</el-col>
          <el-col class="form-border font-small" :span="18">{{product.stock}}</el-col>
        </el-row>
      </div>
    </el-card>

    <el-card shadow="never" class="standard-margin" style="margin-top: 20px;">
      <span class="font-title-medium">营销相关</span>
      <div class="form-container-border">
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">商品关键词汇</el-col>
          <el-col class="form-border font-small" :span="18">{{product.keywords||'暂无'}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">运费模板编号</el-col>
          <el-col class="form-border font-small" :span="18">{{product.feightTemplateId}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">赠送成长点</el-col>
          <el-col class="form-border font-small" :span="18">{{product.giftGrowth}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">赠送积分</el-col>
          <el-col class="form-border font-small" :span="18">{{product.giftPoint}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">会员价</el-col>
          <el-col class="form-border font-small" :span="18">
            <span v-for="item in product.memberPriceList" :key="item.id" style="padding-right: 20px">
              {{item.memberLevelName}}(¥{{item.memberPrice||'暂无优惠'}})
            </span>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">是否为预告商品</el-col>
          <el-col class="form-border font-small" :span="18">{{product.previewStatus|formatYesOrNo}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">是否已上架</el-col>
          <el-col class="form-border font-small" :span="18">{{product.publishStatus|formatYesOrNo}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">是否为推荐商品</el-col>
          <el-col class="form-border font-small" :span="18">{{product.recommandStatus|formatYesOrNo}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">是否新品</el-col>
          <el-col class="form-border font-small" :span="18">{{product.newStatus|formatYesOrNo}}</el-col>
        </el-row>
      </div>
    </el-card>

    <el-card shadow="never" class="standard-margin" style="margin-top: 20px;">
      <span class="font-title-medium">商品详情</span>
      <div class="form-container-border">
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">商品介绍</el-col>
          <el-col class="form-border font-small" :span="18">{{product.description||'暂无'}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">商品详情标题</el-col>
          <el-col class="form-border font-small" :span="18">{{product.detailTitle||'暂无'}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">商品详情</el-col>
          <el-col class="form-border font-small" :span="18">{{product.detailDesc||'暂无'}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">商品备注</el-col>
          <el-col class="form-border font-small" :span="18">{{product.note||'暂无'}}</el-col>
        </el-row>
      </div>
    </el-card>

    <el-tabs type="border-card" value="pc" style="margin-top: 20px; box-shadow: none;">
      <el-tab-pane label="图片详情" name="pc">
        <div class="detail-html" v-html="product.detailHtml||'无内容'"></div>
      </el-tab-pane>
      <el-tab-pane label="购买信息" name="mobile">
        <div class="detail-html" v-html="product.detailMobileHtml||'无内容'"></div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
  import {getProduct, updateVerifyStatus} from '../../../api/product'
  import {formatDate} from '@/utils/date'
  import {fetchList as fetchProductAttrList} from '../../../api/productAttr'

  export default {
    name: 'commentDetail',
    data() {
      return {
        product: {},
        attributeList: []
      }
    },
    computed: {
      productAttr() {
        let attrStr = ``
        if (!this.product.productAttributeValueList) return ''
        this.product.productAttributeValueList.forEach(item => {
          const index = this.attributeList.findIndex(attr => {
            return attr.id === item.productAttributeId
          })
          if (index !== -1) {
            attrStr = `${attrStr}${this.attributeList[index].name}(${item.value}) `
          }
        })
        return attrStr
      }
    },
    created() {
      this.getInfo()
    },
    filters: {
      formatYesOrNo(value) {
        return value ? '是' : '否'
      },
      formatShowStatus(value) {
        if (value) {
          return '显示'
        } else {
          return '隐藏'
        }
      },
      formatProductAttribute(value) {
        if (!value) return ''
        let str = ''
        const _map = JSON.parse(value)
        _map.forEach(item => {
          str += `${item.key}: ${item.value} `
        })
        return str
      },
      formatDate(value) {
        return formatDate(new Date(value), 'yyyy-MM-dd hh:mm:ss')
      }
    },
    methods: {
      async handleVerify() {
        const params = new URLSearchParams
        params.append('ids', [this.product.id])
        params.append('verifyStatus', 1)
        params.append('detail', '通过')
        await updateVerifyStatus(params)
        this.$message({
          type: 'success',
          message: '审核通过',
          duration: 1000
        })
        this.getInfo()
      },
      getProductAttrList(type, cid) {
        if (cid) {
          let param = {pageNum: 1, pageSize: 100, type: type}
          fetchProductAttrList(cid, param).then(response => {
            console.log(response)
            this.attributeList = [...this.attributeList, ...response.data.list]
          })
        }
      },
      async getInfo() {
        const id = this.$route.query.id
        const response = await getProduct(id)
        console.log(response)
        this.product = response.data
        this.getProductAttrList(0, this.product.productAttributeCategoryId)
        this.getProductAttrList(1, this.product.productAttributeCategoryId)
      }
    }
  }
</script>
<style scoped>
  .el-row {
    display: flex;
    align-items: stretch;
  }

  .el-col {
    display: flex;
    align-items: center;
  }

  .detail-container {
    position: absolute;
    left: 0;
    right: 0;
    width: 1080px;
    padding: 35px 35px 15px 35px;
    margin: 20px auto;
  }

  .standard-margin {
    margin-top: 15px;
  }

  .form-border {
    border-right: 1px solid #DCDFE6;
    border-bottom: 1px solid #DCDFE6;
    padding: 10px;
  }

  .form-container-border {
    border-left: 1px solid #DCDFE6;
    border-top: 1px solid #DCDFE6;
    margin-top: 15px;
  }

  .form-left-bg {
    background: #F2F6FC;
  }
</style>
