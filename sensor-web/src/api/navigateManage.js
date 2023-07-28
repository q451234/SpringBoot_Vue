import request from '@/utils/request'

export default{
    getNavigate(){
        return request({
          url: '/navigate',
          method: 'get',
        });
      },
}