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
          <el-input v-model="userForm.name" autocomplete="off" :disabled=true></el-input>
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
        <el-form-item
          label="确认密码"
          prop="confirmpassword"
          :label-width="formLabelWidth"
        >
          <el-input
            type="password"
            v-model="userForm.confirmpassword"
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
    };
    var confirm = (rule, value, callback) => {
      if(value == null && this.userForm.password == null){
        callback()
        return;
      }
      if (value === '') {
          callback(new Error('请再次输入密码'));
      } else if (value !== this.userForm.password) {
          callback(new Error('两次输入密码不一致!'));
      } else {
          callback();
      }
    };

    return {
      state : null,

      disabled: true,
      formLabelWidth: "70px",
      userForm: {
      },
      rules: {
        password: [
          { validator: validatePassword, trigger: "blur" },
        ],
        confirmpassword: [
          { validator: confirm, trigger: "blur" },
        ],
        email: [
          { validator: checkEmail, trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    getDefaultState(){
      return {
        token: getToken()
      }
    },
    getUserInfo(refresh){
      getInfo(this.state.token, refresh).then((response) => {
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

            // 刷新表格
            this.userForm = null;
            this.state = null;
            this.state = this.getDefaultState()
            this.getUserInfo(true);
            this.disabled = true;
            
            this.$message({
              message: response.message,
              type: 'success'
            });
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    }
  },
  created() {
    this.state = this.getDefaultState();
    this.getUserInfo();
  },
  beforeDestroy () {
    this.userForm = null;
    this.state = null;
  }
};
</script>