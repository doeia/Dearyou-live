<template> 
  <div class="detail-container" style="margin-left: 200px">
    <el-card shadow="never" class="standard-margin">
      <span class="font-title-medium">用户信息</span>
      <div class="form-container-border">
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6" style="height:100px;line-height:80px">用户头像
          </el-col>
          <el-col class="form-border font-small" :span="18" style="height:100px">
            <img style="width:80px;height:80px" :src="memberInfo.icon">
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">用户编号</el-col>
          <el-col class="form-border font-small" :span="18">{{memberInfo.id}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">用户名</el-col>
          <el-col class="form-border font-small" :span="18">{{memberInfo.username}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">真实姓名</el-col>
          <el-col class="form-border font-small" :span="18">{{memberInfo.realName}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">会员昵称</el-col>
          <el-col class="form-border font-small" :span="18">{{memberInfo.nickname}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">手机号码</el-col>
          <el-col class="form-border font-small" :span="18">{{memberInfo.phone}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">性别</el-col>
          <el-col class="form-border font-small" :span="18">
            {{memberInfo.gender|formatGender}}
          </el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">生日
          </el-col>
          <el-col class="form-border font-small" :span="18">
            {{memberInfo.birthday|formatDate}}
          </el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">所在城市</el-col>
          <el-col class="form-border font-small" :span="18">{{memberInfo.city}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">身份证</el-col>
          <el-col class="form-border font-small" :span="18">{{memberInfo.idCard|formatNull}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">用户来源</el-col>
          <el-col class="form-border font-small" :span="18">{{memberInfo.sourceType|formatSourceType|formatNull}}
          </el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">会员状态</el-col>
          <el-col class="form-border font-small" :span="18">{{memberInfo.status|formatStatus}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">个性签名</el-col>
          <el-col class="form-border font-small" :span="18">{{memberInfo.personalizedSignature}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">积分</el-col>
          <el-col class="form-border font-small" :span="18">{{memberInfo.integration}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">成长值</el-col>
          <el-col class="form-border font-small" :span="18">{{memberInfo.growth|formatNull}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">剩余抽奖次数</el-col>
          <el-col class="form-border font-small" :span="18">{{memberInfo.luckeyCount|formatNull}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">历史积分数量</el-col>
          <el-col class="form-border font-small" :span="18">{{memberInfo.historyIntegration|formatNull}}</el-col>
        </el-row>
      </div>
    </el-card>

    <el-card shadow="never" class="standard-margin" style="margin-top: 20px;">
      <span class="font-title-medium">统计数据</span>
      <div class="form-container-border">
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">累计消费金额</el-col>
          <el-col class="form-border font-small" :span="18">{{memberStatisticsInfo.consumeAmount|formatNull}}</el-col>
        </el-row>
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6">订单数量</el-col>
          <el-col class="form-border font-small" :span="18">{{memberStatisticsInfo.orderCount|formatNull}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">优惠卷数量</el-col>
          <el-col class="form-border font-small" :span="18">{{memberStatisticsInfo.couponCount|formatNull}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">评论数</el-col>
          <el-col class="form-border font-small" :span="18">{{memberStatisticsInfo.commentCount|formatNull}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">退货数量</el-col>
          <el-col class="form-border font-small" :span="18">{{memberStatisticsInfo.returnOrderCount|formatNull}}
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">登录次数</el-col>
          <el-col class="form-border font-small" :span="18">{{memberStatisticsInfo.loginCount|formatNull}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">关注数量</el-col>
          <el-col class="form-border font-small" :span="18">{{memberStatisticsInfo.attendCount|formatNull}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">粉丝数量</el-col>
          <el-col class="form-border font-small" :span="18">{{memberStatisticsInfo.fansCount|formatNull}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small">最后一次订单时间</el-col>
          <el-col class="form-border font-small" :span="18">{{memberStatisticsInfo.recentOrderTime|formatDate}}</el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>
<script>
  import {
    getInfo,
    getMemberStatisticsInfo
  } from '../../../api/member'
  import {formatDate} from '@/utils/date'

  export default {
    name: 'memberDetail',
    data() {
      return {
        memberInfo: {},
        memberStatisticsInfo: {}
      }
    },
    created() {
      this.getInfo()
      this.getMemberStatisticInfo()
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
      },
      formatNull(value) {
        if (value === undefined || value === null || value === '') {
          return '暂无'
        } else {
          return value
        }
      },
      formatDate(value) {
        if (!value) return '暂无'
        return formatDate(new Date(value), 'yyyy-MM-dd')
      },
      formatGender(value) {
        if (value === 1) {
          return '男'
        } else if (value === 2) {
          return '女'
        } else {
          return '未知'
        }
      },
      formatStatus(value) {
        if (value === 1) {
          return '激活'
        } else {
          return '已删除'
        }
      }
    },
    methods: {
      async getInfo() {
        const response = await getInfo(this.$route.query.id)
        this.memberInfo = response.data
      },
      async getMemberStatisticInfo() {
        const response = await getMemberStatisticsInfo(this.$route.query.id)
        this.memberStatisticsInfo = response.data || {}
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


