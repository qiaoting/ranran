<template>
  <div>
    <el-form :model="searchForm" ref="searchFormRef" :inline="true">
      <el-form-item label="用户名" prop="username">
        <el-input
          v-model="searchForm.username"
          placeholder="请输入用户名"
          clearable
          class="query_input"
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input
          v-model="searchForm.nickname"
          placeholder="请输入昵称"
          clearable
          class="query_input"
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="searchForm.status"
          placeholder="请选择状态"
          clearable
          style="width: 160px"
        >
          <el-option label="正常" value="1" />
          <el-option label="禁用" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row class="mt12 mb8">
      <el-col>
        <el-button type="primary" plain @click="handleAdd">
          <el-icon>
            <Plus />
          </el-icon>
          新增用户
        </el-button>
      </el-col>
    </el-row>
    <el-table :data="userList" :loading="loading">
      <el-table-column prop="username" label="用户名" align="center" />
      <el-table-column prop="nickname" label="昵称" align="center" />
      <el-table-column prop="email" label="邮箱" align="center" />
      <el-table-column prop="phone" label="手机号" align="center" />
      <el-table-column prop="status" label="是否启用" align="center">
        <template #default="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="1"
            inactive-value="0"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="修改时间"
        width="160"
        align="center"
      />
      <el-table-column label="操作" width="200" align="center">
        <template #default="scope">
          <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <div class="page-div" v-if="total > 0">
      <el-pagination
        :current-page="pageNum"
        :page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增用户' : '编辑用户'"
      :width="dialogWidth"
    >
      <el-form
        ref="userFormRef"
        :model="userForm"
        :rules="userRules"
        label-width="100px"
      >
        <el-form-item label="用户头像">
          <el-upload
            class="avatar-uploader"
            :action="baseUrl + '/api/file/upload'"
            :headers="{ Authorization: `Bearer ${getToken()}` }"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            accept="image/*"
          >
            <img
              v-if="userForm.avatar"
              :src="baseUrl + userForm.avatar"
              class="avatar"
            />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="userForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="userForm.username"
            :disabled="dialogType === 'edit'"
            placeholder="请输入用户名"
          />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="dialogType === 'add'">
          <el-input
            v-model="userForm.password"
            type="password"
            placeholder="请输入密码"
          />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="userForm.status">
            <el-radio label="1">正常</el-radio>
            <el-radio label="0">禁用 </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定 </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="User">
import { computed, onMounted, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import {
  addUser,
  deleteUser,
  getById,
  getUserPage,
  updateUser,
} from "@/api/system/user";
import { getToken } from "@/utils/auth";

const baseUrl = ref(import.meta.env.VITE_APP_BASE_API);

const searchForm = reactive({
  username: undefined,
  nickname: undefined,
  status: undefined,
});

const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);
const loading = ref(false);
const userList = ref([]);

const dialogVisible = ref(false);
const dialogType = ref("add");
const dialogWidth = computed(() =>
  dialogType.value === "add" ? "500px" : "600px"
);
const userFormRef = ref(null);
const userForm = reactive({
  userId: null,
  username: "",
  password: "",
  nickname: "",
  email: "",
  phone: "",
  status: 1,
});

const userRules = reactive({
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    {
      min: 2,
      max: 50,
      message: "用户名长度在 2 到 50 个字符",
      trigger: "blur",
    },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度在 6 到 20 个字符", trigger: "blur" },
  ],
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

onMounted(() => {
  fetchUserList();
});

function fetchUserList() {
  loading.value = true;
  const params = {
    ...searchForm,
    pageNum: pageNum.value,
    pageSize: pageSize.value,
  };
  getUserPage(params).then((res) => {
    loading.value = false;
    userList.value = res.rows;
    total.value = res.total;
  });
}

function handleSearch() {
  pageNum.value = 1;
  fetchUserList();
}

function handleReset() {
  searchForm.username = undefined;
  searchForm.nickname = undefined;
  searchForm.status = undefined;
  pageNum.value = 1;
  fetchUserList();
}

function handleSizeChange(val) {
  pageSize.value = val;
  pageNum.value = 1;
  fetchUserList();
}

function handleCurrentChange(val) {
  pageNum.value = val;
  fetchUserList();
}

// 新增用户
function handleAdd() {
  dialogType.value = "add";
  Object.assign(userForm, {
    userId: null,
    username: "",
    password: "",
    nickname: "",
    email: "",
    phone: "",
    status: 1,
    avatar: "",
    fileId: null,
  });
  dialogVisible.value = true;
}

function handleAvatarSuccess(response, file) {
  if (response.code === 200) {
    userForm.avatar = response.data.fileUrl;
    userForm.fileId = response.data.fileId;
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

function handleEdit(row) {
  loading.value = true;
  getById({ userId: row.userId }).then((res) => {
    loading.value = false;
    const userData = res.data;
    dialogType.value = "edit";
    Object.assign(userForm, {
      userId: userData.userId,
      username: userData.username,
      nickname: userData.nickname,
      email: userData.email,
      phone: userData.phone,
      status: userData.status,
      avatar: userData.avatar,
      fileId: userData.fileId || null,
    });
    dialogVisible.value = true;
  });
}

function handleSubmit() {
  userFormRef.value.validate((valid) => {
    if (valid) {
      if (dialogType.value === "add") {
        addUser(userForm).then((res) => {
          ElMessage.success("新增用户成功");
          dialogVisible.value = false;
          fetchUserList();
        });
      } else {
        updateUser(userForm).then((res) => {
          ElMessage.success("修改用户成功");
          dialogVisible.value = false;
          fetchUserList();
        });
      }
    }
  });
}

function handleStatusChange(row) {
  updateUser({
    userId: row.userId,
    status: row.status,
  })
    .then((res) => {
      const statusText = row.status === "1" ? "启用" : "禁用";
      ElMessage.success(`已${statusText}用户`);
    })
    .catch((error) => {
      row.status = row.status === "1" ? "0" : "1";
    });
}

function handleDelete(row) {
  ElMessageBox.confirm(`确定要删除用户【${row.username}】吗？`, "确认删除", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    deleteUser({ userId: row.userId }).then((res) => {
      ElMessage.success("删除成功");
      fetchUserList();
    });
  });
}
</script>

<style scoped>
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
  font-size: 1.8rem;
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
