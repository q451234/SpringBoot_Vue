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
          <el-select v-model="searchModel.boxName" placeholder="采集仪" clearable @change="selectBox">
            <el-option
              v-for="item in boxNameOptions[searchModel.projectName]"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
          <el-select v-model="searchModel.cdName" clearable placeholder="采点">
            <el-option                  
              v-for="item in cdSelect"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>

          <el-select v-model="searchModel.fieldValue" clearable placeholder="请选择">
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
            @click="getSensorDataListTotal"
            type="primary"
            round
            icon="el-icon-search"
            >查询</el-button>        
        </el-col>
      </el-row>
    </el-card>

        <!-- 结果列表 -->
        <el-card>
      <el-table :data="sensorDataList" stripe style="width: 100%" >
        <el-table-column label="ID" type="index">
        </el-table-column>
        <!-- <el-table-column prop="id" label="用户ID" width="180">
        </el-table-column> -->
        <el-table-column prop="_measurement" label="项目名称"  width="50" align="center">
        </el-table-column>
        <el-table-column prop="boxId" label="采集仪ID"  width="50" align="center"> 
        </el-table-column>
        <el-table-column prop="boxName" label="采集仪名称" width="100" align="center"> 
        </el-table-column>
        <el-table-column prop="cdId" label="测点ID" width="100" align="center"> 
        </el-table-column>
        <el-table-column prop="cdName" label="测点名称" width="130" align="center"> 
        </el-table-column>
        <el-table-column prop="_time" label="采集时间" width="160" align="center"> 
        </el-table-column>
        <el-table-column prop="chuanganqileixing" label="传感器类型" align="center"> 
        </el-table-column>
        <el-table-column prop="holeId" label="位置信息" align="center"> 
        </el-table-column>
        <el-table-column prop="lMax" label="正常数据上限" align="center"> 
        </el-table-column>
        <el-table-column prop="lMin" label="正常数据下限" align="center"> 
        </el-table-column>
        <el-table-column prop="mMode" label="采集类型" align="center"> 
        </el-table-column>
        <el-table-column prop="points" label="保留小数" align="center"> 
        </el-table-column>
        <el-table-column prop="_value" :label=fieldName width="130" align="center"> 
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 分页组件 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="searchModel.pageNo"
      :page-sizes="[5, 10, 20, 50]"
      :page-size="searchModel.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
  </div>
</template>

<script>
import sensorApi from "@/api/sensorManage";

  export default {
    data() {
      return {
        projectNameOptions:[],
        boxNameOptions:{},
        cdNameOptions:{},
        cdSelect:[],

        total: 0,
        fieldName : '',
        searchModel: {
          pageNo: 1,
          pageSize: 10,
          dateValue: [new Date(2023, 1, 1, 0, 0), new Date(2023, 3, 1, 0, 0)],
          fieldValue: '',
          projectName:"",
          boxName:"",
          cdName:""
        },
        sensorDataList: [],
        options: [{
          value: 'ad',
          label: 'AD值'
        }, {
          value: 'mData',
          label: '测量值'
        }, {
          value: 'calculatedata',
          label: '计算值'
        }, {
          value: 'substand',
          label: '变化量'
        }, {
          value: 'temperature',
          label: '温度'
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
    methods: {
      getSensorDataList() {
        sensorApi.getSensorDataList(this.searchModel, false).then((response) => {
          this.sensorDataList = response.data.rows;
          this.fieldName = this.getLabel(this.searchModel.fieldValue) + "(" + this.sensorDataList[0]._field + ")";

        });        
      },
      getSensorDataListTotal() {
        sensorApi.getSensorDataList(this.searchModel, true).then((response) => {
          this.total = response.data.total;
          this.getSensorDataList();
          this.$message({
              message: response.message,
              type: 'success'
            });
        });        
      },
      handleSizeChange(pageSize) {
        this.searchModel.pageSize = pageSize;
        this.getSensorDataList();
      },
      handleCurrentChange(pageNo) {
        this.searchModel.pageNo = pageNo;
        this.getSensorDataList();
      },
      getLabel(value) {
        let obj = {};
        obj = this.options.find((item)=>{
          return item.value === value;
        });
        return obj.label;
      },
      selectBox(clean = true){
        this.cdSelect = this.cdNameOptions[this.searchModel.projectName][this.searchModel.boxName]
        if(clean){
          this.searchModel.cdName = '';        
        }
      },
      cleanBoxCd(){
        this.searchModel.boxName = '';
        this.searchModel.cdName = '';
      },
      getNavigateProcess(){
        sensorApi.getNavigateProcess().then((response) =>{
          let navigate = response.data.navigate;

          let prolist = Object.keys(navigate);
          prolist.sort();
          for(var i = 0; i < prolist.length; i++){
            let proOpt = this.createOptions(Object.keys(navigate[prolist[i]]))
            this.boxNameOptions[prolist[i]] = proOpt;

            let pop = {};
            pop.value = prolist[i];
            this.projectNameOptions.push(pop);
          } 
  
          let cdNameMap = navigate;
          for(let key in cdNameMap){
            for(let box in cdNameMap[key]){
              cdNameMap[key][box].sort()
              for(var i = 0; i < cdNameMap[key][box].length; i++){
                cdNameMap[key][box][i] = {value: cdNameMap[key][box][i]}
              }
            }
          } 

          let unauthorized = response.data.unauthorized;
          for(var i = 0; i < unauthorized.length; i++){
            unauthorized[i] = {value: unauthorized[i], disabled: true};
          }
          this.projectNameOptions = this.projectNameOptions.concat(unauthorized);
          
          this.cdNameOptions = cdNameMap;
          this.selectBox(false);
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
    },
    created() {
      this.getNavigateProcess();
    },
  }
</script>

<style>
#search .el-input {
  width: 200px;
  margin-right: 10px;
}

</style>