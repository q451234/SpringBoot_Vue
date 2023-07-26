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

          <el-select v-model="searchModel.filedValue" clearable placeholder="请选择">
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
        total: 0,
        fieldName : '',
        searchModel: {
          pageNo: 1,
          pageSize: 10,
          dateValue: [new Date(2023, 1, 1, 0, 0), new Date(2023, 3, 1, 0, 0)],
          filedValue: ''
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
          this.fieldName = this.getLabel(this.searchModel.filedValue) + "(" + this.sensorDataList[0]._field + ")";
          console.log(this.sensorDataList)
          console.log(this.fieldName)
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
      }
    }
  }
</script>

<style>
#search .el-input {
  width: 200px;
  margin-right: 10px;
}

</style>