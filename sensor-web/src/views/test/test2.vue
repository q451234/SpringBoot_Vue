<template>
  <div>
      <el-card id="search">
          <el-row>
              <el-col :span="20">
              <el-input
                  v-model="searchModel.projectName"
                  placeholder="项目名"
                  clearable
              ></el-input>
              <el-input
                  v-model="searchModel.cdId"
                  placeholder="测点ID"
                  clearable
              ></el-input>

              <el-select v-model="searchModel.fieldValue" multiple placeholder="请选择">
                  <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                  </el-option>
              </el-select>

              <el-date-picker
                  v-model="searchModel.dateValue"
                  type="datetimerange"
                  :picker-options="pickerOptions"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  >
              </el-date-picker>
              </el-col>

              <el-col :span="4" align="right">
              <el-button
                  @click="getSensorDataDrawList"
                  type="primary"
                  round
                  icon="el-icon-search"
                  >查询</el-button>        
              </el-col>
          </el-row>
      </el-card>
      <el-card>
          <div :id="id" :class="className" :style="{width:width,height:height,margin:margin}">
          </div>            
      </el-card>

  </div>
</template>

<script>
import echarts from 'echarts'
import sensorApi from "@/api/sensorManage";

export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    id: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '600px'
    },
    margin:{
      type: String,
      default: "auto"
    }
  },
  data() {
    return {
      chart: null,
      fieldName : '曲线图',
      searchModel: {
        pageNo: 1,
        pageSize: 10,
        dateValue: [new Date(2023, 1, 1, 0, 0), new Date(2023, 3, 1, 0, 0)],
        fieldValue: ['mData'],
        projectName: '1标',
        cdId: '001_19_004'
      },
      colorMap: {
        'ad':{
          color: 'rgb(255,255,0)',
          borderColor: 'rgba(255,255,0,0.2)',             
        },
        'mData':{           
          color: 'rgb(137,189,27)',
          borderColor: 'rgba(137,189,2,0.27)'
        },
        'calculatedata':{
          color: 'rgb(0,136,212)',
          borderColor: 'rgba(0,136,212,0.2)',             
        },
        'substand':{
          color: 'rgb(219,50,51)',
          borderColor: 'rgba(219,50,51,0.2)',           
        },
        'temperature':{
          color: 'rgb(255,128,0)',
          borderColor: 'rgba(255,128,0,0.2)',            
        }
      },

      sensorDataList: {unit : ' '},
      options: [{
        value: 'mData',
        label: '测量值'
      }, {
        value: 'calculatedata',
        label: '计算值'
      }, {
        value: 'substand',
        label: '变化量'
      }],
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      }
    }
  },
  mounted() {
    this.initChart();
    this.resizeChart = ()=>{
      this.chart = echarts.init(document.getElementById(this.id)).resize();
    };
    window.addEventListener('resize',this.resizeChart);
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
    window.removeEventListener('resize', this.resizeChart);
    this.resizeChart = null
  },
  created() {
    this.getSensorDataDrawList(false);
  },
  methods: {
    getSensorDataDrawList(flag = true) {
      console.log(this.searchModel.fieldValue);
      sensorApi.getSensorDataDrawList(this.searchModel, flag).then((response) => {
          this.sensorDataList = response.data;
          console.log(this.sensorDataList)
          this.initChart();
          if(flag){
            this.$message({
              message: response.message,
              type: 'success'
            });
          }
      });        
    },
    getLabel(value) {
      let obj = {};
      obj = this.options.find((item)=>{
        return item.value === value;
      });
      return obj.label;
    },
    initChart() {
      this.chart = echarts.init(document.getElementById(this.id))
      this.chart.setOption({
        backgroundColor: '#394056',
        title: {
          top: 20,
          text: this.fieldName,
          textStyle: {
            fontWeight: 'normal',
            fontSize: 16,
            color: '#F1F1F3'
          },
          left: '1%'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            lineStyle: {
              color: '#57617B'
            }
          }
        },
        legend: {
          top: 20,
          icon: 'rect',
          itemWidth: 14,
          itemHeight: 5,
          itemGap: 13,
          data: [this.fieldName],
          right: '4%',
          textStyle: {
            fontSize: 12,
            color: '#F1F1F3'
          }
        },
        grid: {
          top: 100,
          left: '2%',
          right: '2%',
          bottom: '2%',
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          boundaryGap: false,
          axisLine: {
            lineStyle: {
              color: '#57617B'
            }
          },
          data: this.sensorDataList.date
        }],
        yAxis: [{
          type: 'value',
          name: '(' + this.sensorDataList.unit + ')',
          min: this.sensorDataList.min,
          max: this.sensorDataList.max,
          axisTick: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: '#57617B'
            }
          },
          axisLabel: {
            margin: 10,
            textStyle: {
              fontSize: 14
            }
          },
          splitLine: {
            lineStyle: {
              color: '#57617B'
            }
          }
        }],
        series: [{
          name: this.sensorDataList.nameMap.dataName,
          type: 'line',
          smooth: true,
          symbol: 'circle',
          symbolSize: 5,
          showSymbol: false,
          lineStyle: {
            normal: {
              width: 1
            }
          },
          itemStyle: {
            normal: {
              color: this.colorMap["mData"].color,
              borderColor: this.colorMap["mData"].borderColor,
              borderWidth: 12

            }
          },
          data: [null].concat(this.sensorDataList.value.mData).concat([null])
        },
        {
          name: this.sensorDataList.nameMap.calculatedataName,
          type: 'line',
          smooth: true,
          symbol: 'circle',
          symbolSize: 5,
          showSymbol: false,
          lineStyle: {
            normal: {
              width: 1
            }
          },
          itemStyle: {
            normal: {
              color: this.colorMap["calculatedata"].color,
              borderColor: this.colorMap["calculatedata"].borderColor,
              borderWidth: 12

            }
          },
          data: [null].concat(this.sensorDataList.value.calculatedata).concat([null])
        },
        {
          name: this.sensorDataList.nameMap.substandName,
          type: 'line',
          smooth: true,
          symbol: 'circle',
          symbolSize: 5,
          showSymbol: false,
          lineStyle: {
            normal: {
              width: 1
            }
          },
          itemStyle: {
            normal: {
              color: this.colorMap["substand"].color,
              borderColor: this.colorMap["substand"].borderColor,
              borderWidth: 12

            }
          },
          data: [null].concat(this.sensorDataList.value.substand).concat([null])
        }
      ]
      })
    }
  }
}
</script>
