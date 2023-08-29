<template>
  <div class="detail-container" style="margin-left: 200px">
    <div style="margin-top:15px;text-align: left">
      <el-button type="primary" size="small" @click="handleReplay">回复</el-button>
      <el-button size="small" @click="handleViewReplyCommentList">查看回复评论</el-button>
      <el-button type="warning" size="small" @click="toggleShowOrHide">切换显示隐藏</el-button>
      <el-button type="danger" size="small" @click="handleDelete">删除</el-button>
    </div>
    <el-card shadow="never" class="standard-margin">
      <span class="font-title-medium">评论详情</span>
      <div class="form-container-border">
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">评论编号</el-col>
          <el-col class="form-border font-small" :span="18">{{comment.id}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">用户昵称</el-col>
          <el-col class="form-border font-small" :span="18">{{comment.memberNickName}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">用户IP</el-col>
          <el-col class="form-border font-small" :span="18">{{comment.memberIp}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">评论内容</el-col>
          <el-col class="form-border font-small" :span="18">{{comment.content}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small" style="height:50px;line-height:30px">订单编号
          </el-col>
          <el-col class="form-border font-small" :span="18" style="height:50px">
            {{comment.orderId}}
            <el-button type="text" size="small" @click="handleViewOrder">查看</el-button>
          </el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6" style="height:50px;line-height:30px">商品编号
          </el-col>
          <el-col class="form-border font-small" :span="18" style="height:50px">
            {{comment.productId}}
            <el-button type="text" size="small" @click="handleViewProduct">查看</el-button>
          </el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">商品名称</el-col>
          <el-col class="form-border font-small" :span="18">{{comment.productName}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">商品属性</el-col>
          <el-col class="form-border font-small" :span="18">{{comment.productAttribute|formatProductAttribute}}</el-col>
        </el-row>
<!--        <el-row>-->
<!--          <el-col class="form-border form-left-bg font-small" :span="6" style="height:100px;line-height:80px">嗮图-->
<!--          </el-col>-->
<!--          <el-col class="form-border font-small" :span="18" style="height:100px" v-if="comment.pics">-->
<!--            <img v-for="item in comment.pics.split(',')" style="width:80px;height:80px" :src="item">-->
<!--          </el-col>-->
<!--        </el-row>-->
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">评论时间</el-col>
          <el-col class="form-border font-small" :span="18">{{comment.createTime|formatDate}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">是否显示</el-col>
          <el-col class="form-border font-small" :span="18">{{comment.showStatus|formatShowStatus}}</el-col>
        </el-row>
<!--        <el-row>-->
<!--          <el-col :span="6" class="form-border form-left-bg font-small">收藏数</el-col>-->
<!--          <el-col :span="18" class="form-border font-small">{{comment.collectCouont}}</el-col>-->
<!--        </el-row>-->
<!--        <el-row>-->
<!--          <el-col :span="6" class="form-border form-left-bg font-small">阅读量</el-col>-->
<!--          <el-col :span="18" class="form-border font-small">{{comment.readCount}}</el-col>-->
<!--        </el-row>-->
<!--        <el-row>-->
<!--          <el-col :span="6" class="form-border form-left-bg font-small">回复数</el-col>-->
<!--          <el-col :span="18" class="form-border font-small">{{comment.replayCount}}</el-col>-->
<!--        </el-row>-->
<!--        <el-row>-->
<!--          <el-col :span="6" class="form-border form-left-bg font-small">Star数</el-col>-->
<!--          <el-col :span="18" class="form-border font-small">{{comment.star}}</el-col>-->
<!--        </el-row>-->
      </div>
    </el-card>
    <el-dialog title="回复评论" :visible.sync="replyCommentDialog.dialogVisible">
      <el-form :model="replyCommentDialog.replayComment">
        <el-form-item label="回复内容">
          <el-input type="textarea" v-model="replyCommentDialog.replayComment.content" placeholder="请输入回复内容"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
            <el-button @click="replyCommentDialog.dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="doReply">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
  import {getInfo, deleteBatch, show, hide} from '@/api/comment'
  import {create} from '@/api/replyComment'
  import {formatDate} from '@/utils/date'
  import {mapGetters} from 'vuex'

  const defaultComment = {
    'collectCouont': 0,
    'content': null,
    'createTime': new Date(),
    'id': null,
    'memberIcon': null,
    'memberIp': null,
    'memberNickName': null,
    'orderId': 0,
    'pics': null,
    'productAttribute': null,
    'productId': 0,
    'productName': null,
    'readCount': 0,
    'replayCount': 0,
    'showStatus': 0,
    'star': 0
  }
  export default {
    name: 'commentDetail',
    data() {
      return {
        comment: Object.assign({}, defaultComment),
        replyCommentDialog: {
          dialogVisible: false,
          replayComment: {
            'commentId': 0,
            'content': null,
            'memberIcon': null,
            'memberNickName': null,
            'type': 1
          }
        }
      }
    },
    computed: {
      ...mapGetters([
        'avatar',
        'name'
      ])
    },
    created() {
      this.getInfo()
    },
    filters: {
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
        if (!value) return '暂无'
        return formatDate(new Date(value), 'yyyy-MM-dd hh:mm:ss')
      }
    },
    methods: {
      handleReplay() {
        this.replyCommentDialog.dialogVisible = true
      },
      async doReply() {
        const comment = this.replyCommentDialog.replayComment
        comment.commentId = this.$route.query.id
        comment.memberIcon = this.avatar
        comment.memberNickName = this.name
        await create(comment)
        this.$message({
          message: '处理成功',
          type: 'success',
          duration: 1000
        })
        this.replyCommentDialog.dialogVisible = false
      },
      handleViewReplyCommentList() {
        this.$router.push({path: '/pms/replyComment', query: {commentId: this.comment.id}})
      },
      toggleShowOrHide() {
        this.$confirm('是否要进行该操作?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          if (this.comment.showStatus) {
            hide(this.comment.id).then(() => {
              this.getInfo()
              this.$message({
                message: '处理成功',
                type: 'success',
                duration: 1000
              })
            })
          } else {
            show(this.comment.id).then(() => {
              this.getInfo()
              this.$message({
                message: '处理成功',
                type: 'success',
                duration: 1000
              })
            })
          }
        })
      },
      handleDelete() {
        let params = new URLSearchParams()
        const ids = []
        const id = this.comment.id
        ids.push(id)
        params.append('ids', ids)
        this.$confirm('是否要进行该操作?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteBatch(params).then(response => {
            this.$message({
              message: '处理成功',
              type: 'success',
              duration: 1000
            })
            this.$router.back()
          })
        })
      },
      handleViewProduct() {
        this.$router.push({path: '/pms/productDetail', query: {id: this.comment.productId}})
      },
      handleViewOrder() {
        this.$router.push({path: '/oms/orderDetail', query: {id: this.comment.orderId}})
      },
      async getInfo() {
        const id = this.$route.query.id
        const response = await getInfo(id)
        this.comment = response.data
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
