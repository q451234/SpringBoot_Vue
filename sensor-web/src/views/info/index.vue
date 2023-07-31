<template>
  <div>
    <el-card  style="width:50%; margin: 0 auto;" >
      <el-button type="primary" icon="el-icon-edit" style="margin-left: 90%; margin-bottom: 20px;" @click="disabled = !disabled"></el-button>
      <el-form :model="userForm" ref="userFormRef" :rules="rules">
        <el-form-item
          label="用户名"
          prop="username"
          :label-width="formLabelWidth"
        >
          <el-input v-model="userForm.name" autocomplete="off" :disabled="disabled"></el-input>
        </el-form-item>
        <el-form-item
          label="登录密码"
          prop="password"
          :label-width="formLabelWidth"
        >
          <el-input
            type="password"
            v-model="userForm.password"
            autocomplete="off"
            :disabled="disabled"
          ></el-input>
        </el-form-item>
        <el-form-item label="联系电话" :label-width="formLabelWidth">
          <el-input v-model="userForm.phone" autocomplete="off" :disabled="disabled"></el-input>
        </el-form-item>
        <el-form-item
          label="电子邮件"
          prop="email"
          :label-width="formLabelWidth"
        >
          <el-input v-model="userForm.email" autocomplete="off" :disabled="disabled"></el-input>
        </el-form-item>
      </el-form>
      <div class="dialog-footer" style="margin-left: 45%;">
        <el-button type="primary" @click="saveUser">修改</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import userApi from "@/api/userManage";

const getDefaultState = () => {
  return {
    token: getToken(),
  }
}

const state = getDefaultState()

export default {
  data() {
    var checkEmail = (rule, value, callback) => {
      var reg =
        /^[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*@[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*\.[a-z]{2,}$/;
      if (!reg.test(value)) {
        return callback(new Error("邮箱格式错误"));
      }
      callback();
      
    };
    var validatePassword = (rule, value, callback) => {
      if(value == null){
        callback()
        return;
      }
      if (value !== '') {
        if (value.length < 6) {
          callback(new Error('请输入至少6位的密码'))
          return false
        } else if (
          !/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@!*#$%&_=])[A-Za-z\d@!*#$%&_=]{8,18}$/.test(value)
        ) {
          callback(new Error('密码必须包含字母、数字和特殊字符(@!*#$%&_=)'))
          return false
        } else {
          callback()
        }
      }
    }

    return {
      disabled: true,
      formLabelWidth: "70px",
      userForm: {
      },
      rules: {
        password: [
          { validator: validatePassword, trigger: "blur" },
        ],
        email: [
          { validator: checkEmail, trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    getUserInfo(){
      getInfo(state.token).then((response) => {
        this.userForm = response.data;
      });
    },
    saveUser() {
      // 触发表单验证
      this.$refs.userFormRef.validate((valid) => {
        if (valid) {
          // 提交请求给后台
          userApi.updateUserPersonal(this.userForm).then(response => {
            // 成功提示
            this.$message({
              message: response.message,
              type: 'success'
            });
            // 刷新表格
            this.getUserInfo();
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    }
  },
  created() {
    this.getUserInfo();
  },
};
</script>