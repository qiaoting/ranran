<template>
  <div>
    <div class="page-header">
        <span class="role_span">
          角色:【{{
            roleInfo.roleName || "未命名角色"
          }}】【{{ roleInfo.roleCode }}】
        </span>
    </div>

    <el-form :model="searchForm" :inline="true" class="search-form">
      <el-form-item label="用户名">
        <el-input
          v-model="searchForm.username"
          placeholder="请输入用户名"
          clearable
          style="width: 200px"
          @keyup.enter="fetchRoleUserList"
        />
      </el-form-item>
      <el-form-item label="昵称">
        <el-input
          v-model="searchForm.nickname"
          placeholder="请输入昵称"
          clearable
          style="width: 200px"
          @keyup.enter="fetchRoleUserList"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchRoleUserList">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row class="mt12 mb8">
      <el-col>
        <el-button type="primary" plain @click="showUserSelectModal = true">
          <el-icon>
            <Plus />
          </el-icon>
          添加用户
        </el-button>
      </el-col>
    </el-row>
    <el-table :data="roleUserList" :loading="loading">
      <el-table-column prop="username" label="用户名" align="center" />
      <el-table-column prop="nickname" label="昵称" align="center" />
      <el-table-column prop="email" label="邮箱" align="center" />
      <el-table-column prop="phone" label="手机号" align="center" />
      <el-table-column prop="status" label="账号状态" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'">
            {{ scope.row.status === "1" ? "正常" : "禁用" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="100">
        <template #default="scope">
          <el-button
            type="danger"
            @click="handleRemoveUser(scope.row)"
          >
            取消授权
          </el-button>
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

    <UserSelectModal
      v-model="showUserSelectModal"
      :role-id="roleId"
      :assigned-user-ids="existingUserIds"
      @confirm="handleUserAssignConfirm"
      @close="showUserSelectModal = false"
    />
  </div>
</template>

<script setup name="RoleUser">
import { computed, onMounted, reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import UserSelectModal from "@/components/UserSelectModal/index.vue";
import { getRoleById } from "@/api/system/role";
import {
  assignRoleUsers,
  getRoleUserPage,
  removeRoleUser,
} from "@/api/system/roleUser";

const route = useRoute();
const router = useRouter();
const roleId = ref(route.params.roleId || "");

const roleInfo = ref({});

const roleUserList = ref([]);
const loading = ref(false);
const total = ref(0);

const pageNum = ref(1);
const pageSize = ref(10);

const searchForm = reactive({
  username: "",
  nickname: "",
});

const showUserSelectModal = ref(false);

const existingUserIds = computed(() => {
  return roleUserList.value.map((item) => item.userId);
});

onMounted(() => {
  if (!roleId.value) {
    ElMessage.error("角色ID不存在");
    router.back();
    return;
  }
  fetchRoleInfo();
  fetchRoleUserList();
});

function fetchRoleInfo() {
  getRoleById({ roleId: roleId.value }).then((res) => {
    roleInfo.value = res.data || {};
  });
}

function fetchRoleUserList() {
  loading.value = true;
  const params = {
    roleId: roleId.value,
    ...searchForm,
    pageNum: pageNum.value,
    pageSize: pageSize.value,
  };
  getRoleUserPage(params).then((res) => {
    loading.value = false;
    roleUserList.value = res.rows || [];
    total.value = res.total || 0;
  });
}

function resetSearch() {
  searchForm.username = "";
  searchForm.nickname = "";
  pageNum.value = 1;
  fetchRoleUserList();
}

function handleSizeChange(val) {
  pageSize.value = val;
  pageNum.value = 1;
  fetchRoleUserList();
}

function handleCurrentChange(val) {
  pageNum.value = val;
  fetchRoleUserList();
}

function handleRemoveUser(row) {
  ElMessageBox.confirm(
    `确定取消用户【${row.username}】的角色授权吗？`,
    "确认移除",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  ).then(() => {
    removeRoleUser({
      roleId: roleId.value,
      userIds: [row.userId],
    }).then((res) => {
      ElMessage.success("取消成功");
      fetchRoleUserList();
    });
  });
}

function handleUserAssignConfirm(data) {
  assignRoleUsers(data).then((res) => {
    ElMessage.success("用户分配成功");
    showUserSelectModal.value = false;
    fetchRoleUserList();
  });
}
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.role_span {
  margin: 0;
  color: #13406d;
  font-size: 1rem;
  line-height: 1;
}

.search-form {
  margin-bottom: 15px;
}
</style>
