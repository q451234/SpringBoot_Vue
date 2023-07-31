import request from '@/utils/request'

export default{
  getSensorDataList(searchModel, flag){
    return request({
      url: '/sensor/list',
      method: 'get',
      params:{
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize,
        projectName: searchModel.projectName,
        cdName: searchModel.cdName,
        boxName: searchModel.boxName,
        dateStart: searchModel.dateValue[0],
        dateEnd: searchModel.dateValue[1],
        field: searchModel.fieldValue,
        flag: flag
      }
    });
  },
  getSensorDataDrawListProcess(searchModel){
    return request({
      url: '/sensor/drawProcess',
      method: 'post',
      params:{
        projectName: searchModel.projectName,
        boxName: searchModel.boxName,
        cdName:searchModel.cdName,
        dateStart: searchModel.dateValue[0],
        dateEnd: searchModel.dateValue[1],
      },
      data: searchModel.fieldValue
    });
  },
  getSensorDataDrawListDistribution(searchModel){
    return request({
      url: '/sensor/drawDistribution',
      method: 'get',
      params:{
        projectName: searchModel.projectName,
        boxName: searchModel.boxName,
        sensorType:searchModel.sensorType,
        dateStart: searchModel.dateValue[0],
        dateEnd: searchModel.dateValue[1],
        field: searchModel.fieldValue
      },
    });
  },
  getNavigateProcess(){
    return request({
      url: '/navigate/process',
      method: 'get',
    });
  },
  getNavigateDistribution(){
    return request({
      url: '/navigate/distribution',
      method: 'get',
    });
  },
  getNavigateInclinometer(searchModel){
    return request({
      url: '/navigate/inclinometer',
      method: 'get',
      params:{
        sensorType: searchModel.sensorType
      }
    });
  }
}