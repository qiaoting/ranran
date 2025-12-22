<template>
  <el-row :gutter="20">
    <el-col :span="24" :xs="24">
      <div>
        <div>
          <div class="profile-header">
            <div class="avatar-container">
              <el-avatar
                :size="100"
                :src="userStore.info.avatar ? (baseUrl + userStore.info.avatar) : defaultAvatar"
              >
                <UserFilled />
              </el-avatar>
              <el-button
                type="primary"
                class="edit-avatar-btn"
                @click="handleEditAvatar"
              >
                更换头像
              </el-button>
            </div>
            <div class="user-basic">
              <h2>{{ userInfo.nickname || userInfo.username }}</h2>
              <p>{{ userInfo.admin === true ? "管理员" : "普通用户" }}</p>
            </div>
          </div>

          <div class="profile-info">
            <el-descriptions border :column="1">
              <el-descriptions-item label="用户名">{{
                userInfo.username
              }}</el-descriptions-item>
              <el-descriptions-item label="昵称">{{
                userInfo.nickname || "-"
              }}</el-descriptions-item>
              <el-descriptions-item label="邮箱">{{
                userInfo.email || "-"
              }}</el-descriptions-item>
              <el-descriptions-item label="手机号">{{
                userInfo.phone || "-"
              }}</el-descriptions-item>
              <el-descriptions-item label="角色">{{
                formatRoles(userInfo.roles) || "-"
              }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{
                formatDate(userInfo.createTime) || "-"
              }}</el-descriptions-item>
              <el-descriptions-item label="最后登录"
                >{{ formatDate(userInfo.lastLoginTime) || "-" }}
              </el-descriptions-item>
            </el-descriptions>
          </div>

          <div class="profile-actions">
            <el-button type="primary" @click="handleEditInfo"
              >编辑个人信息</el-button
            >
            <el-button @click="handleChangePassword">修改密码</el-button>
          </div>
        </div>

        <el-dialog
          v-model="infoDialogVisible"
          title="编辑个人信息"
          width="500px"
        >
          <el-form
            ref="infoFormRef"
            :model="infoForm"
            :rules="infoRules"
            label-width="100px"
          >
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="infoForm.nickname" placeholder="请输入昵称" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="infoForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="infoForm.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-form>
          <template #footer>
            <el-button @click="infoDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitInfoForm">确定</el-button>
          </template>
        </el-dialog>

        <el-dialog v-model="avatarDialogVisible" title="更换头像" width="400px">
          <el-upload
            class="avatar-uploader"
            :action="baseUrl + '/api/file/upload'"
            :headers="{ Authorization: `Bearer ${getToken()}` }"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            accept="image/*"
          >
            <img v-if="tempAvatar" :src="tempAvatar" class="avatar" />
            <img
              v-else-if="userInfo.avatar"
              :src="baseUrl + userInfo.avatar"
              class="avatar"
            />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
          <template #footer>
            <el-button @click="cancelAvatarUpload">取消</el-button>
            <el-button type="primary" @click="confirmAvatarUpload"
              >确定</el-button
            >
          </template>
        </el-dialog>

        <el-dialog v-model="pwdDialogVisible" title="修改密码" width="400px">
          <el-form
            ref="pwdFormRef"
            :model="pwdForm"
            :rules="pwdRules"
            label-width="100px"
          >
            <el-form-item label="原密码" prop="oldPassword">
              <el-input
                v-model="pwdForm.oldPassword"
                type="password"
                placeholder="请输入原密码"
              />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input
                v-model="pwdForm.newPassword"
                type="password"
                placeholder="请输入新密码"
              />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input
                v-model="pwdForm.confirmPassword"
                type="password"
                placeholder="请确认新密码"
              />
            </el-form-item>
          </el-form>
          <template #footer>
            <el-button @click="pwdDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitPwdForm">确定</el-button>
          </template>
        </el-dialog>
      </div>
    </el-col>
  </el-row>
</template>

<script setup name="Profile">
import { onMounted, reactive, ref } from "vue";
import { Plus, UserFilled } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { useUserStore } from "@/store/user";
import { getSelf, updatePassword, updateSelf } from "@/api/system/profile";
import { getToken } from "@/utils/auth";
import defaultAvatar from '@/assets/image/avatar.png'

const userStore = useUserStore();
const baseUrl = ref(import.meta.env.VITE_APP_BASE_API);
const userInfo = ref({});

const infoDialogVisible = ref(false);
const avatarDialogVisible = ref(false);
const pwdDialogVisible = ref(false);

const infoForm = reactive({
  userId: "",
  nickname: "",
  email: "",
  phone: "",
  department: "",
});

const pwdForm = reactive({
  oldPassword: "",
  newPassword: "",
  confirmPassword: "",
});

const tempAvatar = ref("");
const tempFileId = ref("");

const infoRules = reactive({
  nickname: [
    { required: true, message: "请输入昵称", trigger: "blur" },
    { max: 50, message: "昵称长度不能超过 50 个字符", trigger: "blur" },
  ],
  email: [
    {
      pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
      message: "请输入正确的邮箱格式",
      trigger: "blur",
    },
  ],
  phone: [
    {
      pattern: /^1[3-9]\d{9}$/,
      message: "请输入正确的手机号格式",
      trigger: "blur",
    },
  ],
});

const pwdRules = reactive({
  oldPassword: [{ required: true, message: "请输入原密码", trigger: "blur" }],
  newPassword: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度在 6 到 20 个字符", trigger: "blur" },
  ],
  confirmPassword: [
    { required: true, message: "请确认新密码", trigger: "blur" },
    {
      validator: (rule, value, callback) => {
        if (value !== pwdForm.newPassword) {
          callback(new Error("两次输入的密码不一致"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
});

const infoFormRef = ref(null);
const pwdFormRef = ref(null);

onMounted(() => {
  fetchUserDetail();
});

function fetchUserDetail() {
  getSelf({ userId: userStore.info.userId }).then((res) => {
    userInfo.value = res.data;
  });
}

function formatDate(dateStr) {
  if (!dateStr) return "";
  const date = new Date(dateStr);
  return date.toLocaleString();
}

function handleEditInfo() {
  infoForm.userId = userInfo.value.userId;
  infoForm.nickname = userInfo.value.nickname || "";
  infoForm.email = userInfo.value.email || "";
  infoForm.phone = userInfo.value.phone || "";
  infoForm.department = userInfo.value.department || "";
  infoDialogVisible.value = true;
}

function submitInfoForm() {
  infoFormRef.value.validate().then((valid) => {
    if (valid) {
      updateSelf(infoForm).then((res) => {
        ElMessage.success("个人信息修改成功");
        infoDialogVisible.value = false;
        fetchUserDetail();
        userStore.fetchUserInfo();
      });
    }
  });
}

function handleEditAvatar() {
  tempAvatar.value = "";
  tempFileId.value = "";
  avatarDialogVisible.value = true;
}

function handleAvatarSuccess(response) {
  if (response.code === 200) {
    tempAvatar.value = baseUrl.value + response.data.fileUrl;
    tempFileId.value = response.data.fileId;
    ElMessage.success("头像上传成功");
  } else {
    ElMessage.error("头像上传失败：" + (response.msg || "未知错误"));
  }
}

function beforeAvatarUpload(file) {
  const isJpgOrPng = file.type === "image/jpeg" || file.type === "image/png";
  const isLimit = file.size / 1024 / 1024 < 10;
  if (!isJpgOrPng) {
    ElMessage.error("只能上传JPG/PNG格式的图片");
    return false;
  }
  if (!isLimit) {
    ElMessage.error("图片大小不能超过10MB");
    return false;
  }
  return true;
}

function cancelAvatarUpload() {
  tempAvatar.value = "";
  tempFileId.value = "";
  avatarDialogVisible.value = false;
}

function confirmAvatarUpload() {
  if (!tempFileId.value) {
    ElMessage.warning("请先上传头像");
    return;
  }
  updateSelf({
    userId: userInfo.value.userId,
    avatar: tempAvatar.value.replace(baseUrl.value, ""),
    fileId: tempFileId.value,
  }).then((res) => {
    ElMessage.success("头像修改成功");
    avatarDialogVisible.value = false;
    fetchUserDetail();
    userStore.fetchUserInfo();
  });
}

function handleChangePassword() {
  pwdForm.oldPassword = "";
  pwdForm.newPassword = "";
  pwdForm.confirmPassword = "";
  pwdDialogVisible.value = true;
}

function submitPwdForm() {
  pwdFormRef.value.validate().then((valid) => {
    if (valid) {
      updatePassword({
        userId: userInfo.value.userId,
        oldPassword: pwdForm.oldPassword,
        newPassword: pwdForm.newPassword,
      }).then((res) => {
        ElMessage.success("密码修改成功，请重新登录");
        pwdDialogVisible.value = false;
        ElMessageBox.confirm("确定注销并退出系统吗？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
          userStore.logout().then(() => {
            location.href = "/";
          });
        });
      });
    }
  });
}

function formatRoles(roles) {
  if (!roles || !roles.length) return "";
  return roles.map((role) => role.roleName).join("，");
}
</script>

<style scoped>
.profile-header {
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
  margin-bottom: 20px;
}

.avatar-container {
  margin-right: 30px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.edit-avatar-btn {
  margin-top: 10px;
}

.user-basic {
  flex: 1;
}

.user-basic h2 {
  margin: 0 0 10px 0;
  font-size: 1.1rem;
}

.user-basic p {
  color: #666;
  margin: 0;
}

.profile-info {
  margin-bottom: 20px;
}

.profile-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 头像上传样式（复用用户列表的样式） */
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 1.6rem;
  color: #8c939d;
  width: 150px;
  height: 150px;
  line-height: 150px;
  text-align: center;
}

.avatar {
  width: 150px;
  height: 150px;
  display: block;
  object-fit: cover;
}
</style>
