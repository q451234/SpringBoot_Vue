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
        cdId: searchModel.cdId,
        dateStart: searchModel.dateValue[0],
        dateEnd: searchModel.dateValue[1],
        field: searchModel.filedValue,
        flag: flag
      }
    });
  },
  getSensorDataDrawList(searchModel){
    return request({
      url: '/sensor/draw',
      method: 'get',
      params:{
        projectName: searchModel.projectName,
        cdId: searchModel.cdId,
        dateStart: searchModel.dateValue[0],
        dateEnd: searchModel.dateValue[1],
        field: searchModel.fieldValue
      }
    });
  }
}