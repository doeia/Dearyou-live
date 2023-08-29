<template> 
  <div class="app-container">
    <!--    <el-card class="filter-container" shadow="never">-->
    <!--      <div>-->
    <!--        <i class="el-icon-search"></i>-->
    <!--        <span>筛选搜索</span>-->
    <!--        <el-button-->
    <!--          style="float: right"-->
    <!--          @click="handleSearchList()"-->
    <!--          type="primary"-->
    <!--          size="small">-->
    <!--          查询结果-->
    <!--        </el-button>-->
    <!--        <el-button-->
    <!--          style="float: right;margin-right: 15px"-->
    <!--          @click="handleResetSearch()"-->
    <!--          size="small">-->
    <!--          重置-->
    <!--        </el-button>-->
    <!--      </div>-->
    <!--      <div style="margin-top: 15px">-->
    <!--        <el-form :inline="true" size="small" label-width="140px">-->
    <!--          <el-form-item label="搜索区域">-->
    <!--            <el-input style="width: 203px" placeholder="全国/省"></el-input>-->
    <!--          </el-form-item>-->
    <!--          <el-form-item label="按时间搜索">-->
    <!--            <el-date-picker placeholder="年/月" type="month"></el-date-picker>-->
    <!--          </el-form-item>-->
    <!--        </el-form>-->
    <!--      </div>-->
    <!--    </el-card>-->
    <!--    <el-card shadow="never" :border="false" style="margin-top: 20px; border: none;" :body-style="{padding:'0 10px'}">-->
    <!--      <el-row :gutter="20">-->
    <!--        <el-col :span="6"-->
    <!--                style="display: flex; align-items: center; border-radius: 5px; border: 1px solid #eee; padding: 20px;">-->
    <!--          <svg-icon icon-class="admin" style="font-size: 35px; color: #409EFF;"></svg-icon>-->
    <!--          <span style="padding-left: 20px; color: #999;">相比上月增长<strong style="color: #F56C6C">500</strong>名会员</span>-->
    <!--        </el-col>-->
    <!--      </el-row>-->
    <!--    </el-card>-->
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据图表</span>
    </el-card>
    <el-card shadow="never" style="margin-top: 20px">
      <div class="statistics-layout">
        <div class="layout-title">会员性别统计</div>
        <el-row>
          <el-col :span="4">
            <div style="padding: 20px">
              <div>
                <div style="color: #909399;font-size: 14px">会员总数</div>
                <div style="color: #606266;font-size: 24px;padding: 10px 0"><strong class="color-danger">{{originGenderData.male+originGenderData.female+originGenderData.unknown}}</strong>
                </div>
              </div>
              <div>
                <div style="color: #909399;font-size: 14px">男性会员</div>
                <div style="color: #606266;font-size: 24px;padding: 10px 0">{{originGenderData.male}}</div>
                <div>
                  <span class="color-success" style="font-size: 14px">{{originGenderData.male/(originGenderData.male+originGenderData.female+originGenderData.unknown)*100|formatPercent}}%</span>
                  <span style="color: #C0C4CC;font-size: 14px">所占百分比</span>
                </div>
              </div>
              <div style="margin-top: 20px;">
                <div style="color: #909399;font-size: 14px">女性会员</div>
                <div style="color: #606266;font-size: 24px;padding: 10px 0">{{originGenderData.female}}</div>
                <div>
                  <span class="color-success" style="font-size: 14px">{{originGenderData.female/(originGenderData.male+originGenderData.female+originGenderData.unknown)*100|formatPercent}}%</span>
                  <span style="color: #C0C4CC;font-size: 14px">所占百分比</span>
                </div>
              </div>
              <div style="margin-top: 20px;">
                <div style="color: #909399;font-size: 14px">未知性别</div>
                <div style="color: #606266;font-size: 24px;padding: 10px 0">{{originGenderData.unknown}}</div>
                <div>
                  <span class="color-success" style="font-size: 14px">{{originGenderData.unknown/(originGenderData.male+originGenderData.female+originGenderData.unknown)*100|formatPercent}}%</span>
                  <span style="color: #C0C4CC;font-size: 14px">所占百分比</span>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="20">
            <div style="padding: 10px;border-left:1px solid #DCDFE6">
              <div>
                <ve-pie :data="genderData"></ve-pie>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
    <el-card style="margin-top: 20px" shadow="never">
      <div class="statistics-layout">
        <div class="layout-title">成员增长情况</div>
        <el-row>
          <el-col :span="24">
            <div style="padding: 10px">
              <el-date-picker
                style="float: right;z-index: 1"
                type="year"
                size="small"
                align="right"
                v-model="year"
                placeholder="按年份搜索"
                @change="handleDateChange"
                unlink-panels>
              </el-date-picker>
              <div>
                <ve-line
                  :data="timeData"
                  :legend-visible="false"
                  :settings="chartSettings"></ve-line>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>
<script>
  import {getMemberGenderStatistics, getMemberStatisticsOfTime} from '../../../api/member'
  import {cloneObj} from '../../../utils'
  import {formatDate} from '../../../utils/date'

  const rows = [
    {month: '1月', count: 0},
    {month: '2月', count: 0},
    {month: '3月', count: 0},
    {month: '4月', count: 0},
    {month: '5月', count: 0},
    {month: '6月', count: 0},
    {month: '7月', count: 0},
    {month: '8月', count: 0},
    {month: '9月', count: 0},
    {month: '10月', count: 0},
    {month: '11月', count: 0},
    {month: '12月', count: 0}
  ]

  export default {
    data() {
      return {
        originGenderData: {
          columns: ['month', 'count'],
          rows: []
        },
        genderData: null,
        chartSettings: {
          stack: {'sell': ['cost', 'profit']},
          area: true,
          scale: [true, true]
        },
        year: new Date(),
        timeData: {
          columns: ['month', 'count'],
          rows: cloneObj(rows)
        }
      }
    },
    filters: {
      formatPercent(value) {
        return value.toFixed(2)
      }
    },
    created() {
      this.getMemberGenderStatistics()
      this.getMemberStatisticsOfTime()
    },
    methods: {
      handleDateChange() {
        this.getMemberStatisticsOfTime()
      },
      async getMemberStatisticsOfTime() {
        this.timeData.rows = cloneObj(rows)
        const self = this
        const params = {
          mode: 1,
          date: formatDate(self.year, 'yyyy-MM-dd')
        }
        const response = await getMemberStatisticsOfTime(params)
        response.data.forEach(item => {
          this.timeData.rows[item.index - 1].count = item.total
        })
      },
      async getMemberGenderStatistics() {
        const response = await getMemberGenderStatistics()
        const data = response.data
        this.originGenderData = data
        const rows = []
        Object.keys(data).forEach(key => {
          switch (key) {
            case 'female':
              rows.push({gender: '女', count: data[key]})
              break
            case 'male':
              rows.push({gender: '男', count: data[key]})
              break
            default:
              rows.push({gender: '未知', count: data[key]})
              break
          }
        })
        this.genderData = {
          columns: ['gender', 'count'],
          rows
        }
      },
      handleSearchList() {
      },
      handleResetSearch() {
      }
    }
  }
</script>
<style scoped>
</style>
