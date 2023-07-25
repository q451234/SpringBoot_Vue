import request from '@/utils/request'

export default{
    uploadFile(tableData){
        return request({
          url: '/upload',
          method: 'post',
          data: tableData
        });
      },
}