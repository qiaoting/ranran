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
      <el-form-item label="信息" prop="msg">
        <el-input
          v-model="searchForm.msg"
          placeholder="请输入信息关键词"
          clearable
          class="query_input"
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="loginLogList" :loading="loading" style="margin-top: 15px">
      <el-table-column prop="username" label="用户名" align="center" width="120" />
      <el-table-column
        prop="createTime"
        label="登录时间"
        width="180"
        align="center"
      />
      <el-table-column prop="msg" label="信息" align="center">
        <template #default="scope">
          <el-tooltip :content="scope.row.msg" placement="top">
            <div>{{ scope.row.msg }}</div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" align="center">
        <template #default="scope">
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
  </div>
</template>

<script setup name="LoginLog">
import { deleteLoginLog, getLoginLogPage } from "@/api/system/loginlog";

const searchForm = reactive({
  username: undefined,
  msg: undefined,
});

const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);
const loading = ref(false);
const loginLogList = ref([]);

onMounted(() => {
  fetchLoginLogList();
});

function fetchLoginLogList() {
  loading.value = true;
  const params = {
    ...searchForm,
    pageNum: pageNum.value,
    pageSize: pageSize.value,
  };
  getLoginLogPage(params).then((res) => {
    loading.value = false;
    loginLogList.value = res.rows;
    total.value = res.total;
  });
}

function handleSearch() {
  pageNum.value = 1;
  fetchLoginLogList();
}

function resetSearch() {
  searchForm.username = undefined;
  searchForm.msg = undefined;
  pageNum.value = 1;
  fetchLoginLogList();
}

function handleSizeChange(val) {
  pageSize.value = val;
  pageNum.value = 1;
  fetchLoginLogList();
}

function handleCurrentChange(val) {
  pageNum.value = val;
  fetchLoginLogList();
}

function handleDelete(row) {
  ElMessageBox.confirm(
    `确定要删除ID为【${row.logId}】的登录日志吗？`,
    "确认删除",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  ).then(() => {
    deleteLoginLog({ logId: row.logId }).then((res) => {
      ElMessage.success("删除成功");
      fetchLoginLogList();
    });
  });
}
</script>

<style scoped></style>
