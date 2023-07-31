<template>
  
    <div>
        <el-card id="search">
            <el-row>
                <el-col :span="20">
                  <el-select v-model="searchModel.projectName" placeholder="项目" clearable @change="cleanBoxCd" >
                    <el-option
                      v-for="item in projectNameOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                      :disabled="item.disabled">
                    </el-option>
                  </el-select>
                    <el-select v-model="searchModel.boxName" clearable placeholder="采集仪">
                      <el-option                  
                        v-for="item in boxNameOptions[searchModel.projectName]"
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
                    @click="getSensorDataDrawListDistribution"
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
        projectNameOptions:[],
        boxNameOptions:{},
  
        x_series :[],
        series:[],
        unit:'',
  
        chart: null,
        fieldName : '测斜位移曲线图',
        searchModel: {
          pageNo: 1,
          pageSize: 10,
          dateValue: [new Date(2023, 1, 1, 0, 0), new Date(2023, 3, 1, 0, 0)],
          fieldValue: 'mData',
          projectName:"2标",
          boxName:"CJ131490R1",
          sensorType:"测斜仪(mm)"
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
  
        sensorDataList: [],
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
      this.getSensorDataDrawListDistribution(false);
      this.getNavigateInclinometer();
    },
    methods: {
      createSeries(){
        this.series = [];
          
        let timeList = Object.keys(this.sensorDataList.distributionMap);
        
        for(var i = 0; i < timeList.length; i++){
  
          let r = Math.round(Math.random()*255);
          let g = Math.round(Math.random()*255);
          let b = Math.round(Math.random()*255);
  
          let seriesItem = {
            name: timeList[i],
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
                color: 'rgb(' + r + ',' + g + ',' + b + ')',
                borderColor: 'rgb(' + r + ',' + g + ',' + b + ',' + 0.2 +')',
                borderWidth: 12
              }
            },
            data: []
          };
  
          let seriesData = [];
          for(var j = 0; j < this.sensorDataList.cdNameList.length; j++){
            seriesData[j] = this.sensorDataList.distributionMap[timeList[i]][this.sensorDataList.cdNameList[j]];
          }
          
          seriesItem.data = [null].concat(seriesData).concat([null]);
          this.series.push(seriesItem);
        }
  
        for(var j = 0; j < this.sensorDataList.cdNameList.length; j++){
          let n = this.sensorDataList.cdNameList[j] + "";
          this.x_series.push(n.substring(n.indexOf("(") + 1 ,n.indexOf(")")));
        }
      },
      cleanBoxCd(){
        this.searchModel.boxName = '';
      },
      getNavigateInclinometer(){
        sensorApi.getNavigateInclinometer(this.searchModel).then((response) =>{
          let navigate = response.data.navigate;

          let prolist = Object.keys(navigate);
          prolist.sort();
          for(var i = 0; i < prolist.length; i++){
            let proOpt = this.createOptions(navigate[prolist[i]])
            this.boxNameOptions[prolist[i]] = proOpt;
  
            let pop = {};
            pop.value = prolist[i];
            this.projectNameOptions.push(pop);
          }
          
          let unauthorized = response.data.unauthorized;
          for(var i = 0; i < unauthorized.length; i++){
            unauthorized[i] = {value: unauthorized[i], disabled: true};
          }
          this.projectNameOptions = this.projectNameOptions.concat(unauthorized);
        })
      },
      createOptions(optionsList){
        optionsList.sort();
        let res = [];
        for(var i = 0; i < optionsList.length; i++){
          let optionsDic = {};
          optionsDic.value = optionsList[i]
          res.push(optionsDic);
        }
  
        return res;
      },
      getSensorDataDrawListDistribution(flag = true) {
        sensorApi.getSensorDataDrawListDistribution(this.searchModel, flag).then((response) => {
            this.sensorDataList = response.data;
  
            let unit = this.searchModel.sensorType;
            this.unit = unit.substring(unit.indexOf("(") + 1 ,unit.indexOf(")"))
            
            this.chart = null;
            this.createSeries()
  
            this.unit = this.sensorDataList.fieldName + '(' + this.unit + ')';
            this.initChart();
            
            this.x_series = []
            if(flag){
              this.$message({
                message: response.message,
                type: 'success'
              });
            }
        });        
      },
    
      initChart() {
        this.chart = echarts.init(document.getElementById(this.id))
        this.chart.clear();
        this.chart = echarts.init(document.getElementById(this.id));

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
                color: 'rgb(255,255,255)'
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
              color: 'rgb(255,255,255)'
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
                color: 'rgb(255,255,255)'
              }
            },
            data: [""].concat(this.x_series).concat([""])
          }],
          yAxis: [{
            type: 'value',
            name: this.unit,
            min: this.sensorDataList.min,
            max: this.sensorDataList.max,
            axisTick: {
              show: false
            },
            axisLine: {
              lineStyle: {
                color: 'rgb(255,255,255)'
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
                color: 'rgb(255,255,255)'
              }
            }
          }],
          series : this.series 
        })
      }
    }
  }
  </script>
