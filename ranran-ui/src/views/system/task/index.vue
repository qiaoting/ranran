<template>
  <div>
    <el-form :model="searchForm" ref="searchFormRef" :inline="true">
      <el-form-item label="任务名称" prop="taskName">
        <el-input
          v-model="searchForm.taskName"
          placeholder="请输入任务名称"
          clearable
          class="query_input"
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item label="任务编码" prop="taskCode">
        <el-input
          v-model="searchForm.taskCode"
          placeholder="请输入任务编码"
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
          <el-option label="启用" value="1" />
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
          新增任务
        </el-button>
      </el-col>
    </el-row>
    <el-table :data="taskList" :loading="loading">
      <el-table-column prop="taskName" label="任务名称" align="center" />
      <el-table-column prop="taskCode" label="任务编码" align="center" />
      <el-table-column
        prop="cronExpression"
        label="Cron表达式"
        align="center"
      />
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="180"
        align="center"
      />
      <el-table-column
        prop="updateTime"
        label="更新时间"
        width="180"
        align="center"
      />
      <el-table-column prop="status" label="是否启动" align="center">
        <template #default="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="1"
            inactive-value="0"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template #default="scope">
          <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
            type="text"
            text-color="#ff4d4f"
            @click="handleDelete(scope.row)"
            >删除
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
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增定时任务' : '编辑定时任务'"
      :width="600"
    >
      <el-form
        ref="taskFormRef"
        :model="taskForm"
        :rules="taskRules"
        label-width="100px"
      >
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="taskForm.taskName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="任务编码" prop="taskCode">
          <el-input v-model="taskForm.taskCode" placeholder="请输入任务编码" />
        </el-form-item>
        <el-form-item label="Cron表达式" prop="cronExpression">
          <el-input
            v-model="taskForm.cronExpression"
            placeholder="请输入Cron表达式"
          />
          <el-text type="info" class="mt-2 block"
            >例如: 0 0/5 * * * ? 表示每5分钟执行一次
          </el-text>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="taskForm.status">
            <el-radio label="1">启用</el-radio>
            <el-radio label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import {
  addTask,
  deleteTask,
  getTaskById,
  getTaskPage,
  updateTask,
} from "@/api/system/task";

const searchForm = reactive({
  taskName: undefined,
  taskCode: undefined,
  status: undefined,
});

const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);
const loading = ref(false);
const taskList = ref([]);

const dialogVisible = ref(false);
const dialogType = ref("add");
const taskFormRef = ref(null);
const taskForm = reactive({
  taskId: null,
  taskName: "",
  taskCode: "",
  cronExpression: "",
  status: "1",
});

const taskRules = reactive({
  taskName: [
    { required: true, message: "请输入任务名称", trigger: "blur" },
    { max: 100, message: "任务名称长度不能超过100个字符", trigger: "blur" },
  ],
  taskCode: [
    { required: true, message: "请输入任务编码", trigger: "blur" },
    { max: 50, message: "任务编码长度不能超过50个字符", trigger: "blur" },
  ],
  cronExpression: [
    { required: true, message: "请输入Cron表达式", trigger: "blur" },
    { max: 100, message: "Cron表达式长度不能超过100个字符", trigger: "blur" },
  ],
});

onMounted(() => {
  fetchTaskList();
});

function fetchTaskList() {
  loading.value = true;
  const params = {
    ...searchForm,
    pageNum: pageNum.value,
    pageSize: pageSize.value,
  };
  getTaskPage(params).then((res) => {
    loading.value = false;
    taskList.value = res.rows || [];
    total.value = res.total || 0;
  });
}

function handleSearch() {
  pageNum.value = 1;
  fetchTaskList();
}

function handleReset() {
  searchForm.taskName = undefined;
  searchForm.taskCode = undefined;
  searchForm.status = undefined;
  pageNum.value = 1;
  fetchTaskList();
}

function handleSizeChange(val) {
  pageSize.value = val;
  pageNum.value = 1;
  fetchTaskList();
}

function handleCurrentChange(val) {
  pageNum.value = val;
  fetchTaskList();
}

function handleAdd() {
  dialogType.value = "add";
  Object.assign(taskForm, {
    taskId: null,
    taskName: "",
    taskCode: "",
    cronExpression: "",
    status: "1",
  });
  dialogVisible.value = true;
}

function handleEdit(row) {
  loading.value = true;
  getTaskById({ taskId: row.taskId }).then((res) => {
    loading.value = false;
    const data = res.data;
    dialogType.value = "edit";
    Object.assign(taskForm, {
      taskId: data.taskId,
      taskName: data.taskName,
      taskCode: data.taskCode,
      cronExpression: data.cronExpression,
      status: data.status,
    });
    dialogVisible.value = true;
  });
}

function handleSubmit() {
  taskFormRef.value.validate((valid) => {
    if (valid) {
      if (dialogType.value === "add") {
        addTask(taskForm).then((res) => {
          ElMessage.success("新增定时任务成功");
          dialogVisible.value = false;
          fetchTaskList();
        });
      } else {
        updateTask(taskForm).then((res) => {
          ElMessage.success("修改定时任务成功");
          dialogVisible.value = false;
          fetchTaskList();
        });
      }
    }
  });
}

function handleStatusChange(row) {
  updateTask({
    taskId: row.taskId,
    status: row.status,
  })
    .then((res) => {
      const statusText = row.status === "1" ? "启动" : "停止";
      ElMessage.success(`定时任务已${statusText}`);
      fetchTaskList();
    })
    .catch((error) => {
      row.status = row.status === "1" ? "0" : "1";
    });
}

function handleDelete(row) {
  ElMessageBox.confirm(
    `确定要删除定时任务【${row.taskName}】吗？`,
    "确认删除",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  ).then(() => {
    deleteTask({ taskId: row.taskId }).then((res) => {
      ElMessage.success("删除成功");
      fetchTaskList();
    });
  });
}
</script>

<style scoped></style>
