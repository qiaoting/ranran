```vue
<template>
  <div>
    <el-form :model="searchForm" ref="searchFormRef" :inline="true">
      <el-form-item label="配置名称" prop="configName">
        <el-input
          v-model="searchForm.configName"
          placeholder="请输入配置名称"
          clearable
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item label="配置键" prop="configKey">
        <el-input
          v-model="searchForm.configKey"
          placeholder="请输入配置键"
          clearable
          @keyup.enter="handleSearch"
        />
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
          新增系统参数
        </el-button>
      </el-col>
    </el-row>
    <el-table
      :data="configList"
      :loading="loading"
    >
      <el-table-column prop="configName" label="配置名称" align="center" />
      <el-table-column prop="configKey" label="配置键" align="center" />
      <el-table-column prop="configValue" label="配置值" align="center"/>
      <el-table-column label="配置类型" align="center">
        <template #default="scope">
          <span v-if="scope.row.configType === '1'">系统配置</span> 
          <span v-else>普通配置</span> 
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="180"
        align="center"
      />
      <el-table-column
        prop="updateTime"
        label="修改时间"
        width="180"
        align="center"
      />
      <el-table-column label="操作" width="200" align="center">
        <template #default="scope">
          <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" @click="handleDelete(scope.row)">删除</el-button>
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
      :title="dialogType === 'add' ? '新增系统参数' : '编辑系统参数'"
      :width="600"
    >
      <el-form
        ref="configFormRef"
        :model="configForm"
        :rules="configRules"
        label-width="100px"
      >
        <el-form-item label="配置名称" prop="configName">
          <el-input
            v-model="configForm.configName"
            placeholder="请输入配置名称"
          />
        </el-form-item>
        <el-form-item label="配置键" prop="configKey">
          <el-input
            v-model="configForm.configKey"
            placeholder="请输入配置键"
          />
        </el-form-item>
        <el-form-item label="配置值" prop="configValue">
          <el-input
            v-model="configForm.configValue"
            placeholder="请输入配置值"
          />
        </el-form-item>
        <el-form-item label="配置类型" prop="configType">
          <el-select
            v-model="configForm.configType"
            placeholder="请选择配置类型"
          >
            <el-option label="系统配置" value="1" />
            <el-option label="普通配置" value="2" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="SysConfig">
import { Plus } from "@element-plus/icons-vue";
import { addConfig, deleteConfig, getConfigById, getConfigPage, updateConfig } from "@/api/system/config";
import { ElMessage, ElMessageBox } from "element-plus";
import { reactive, ref, onMounted } from "vue";

const searchForm = reactive({
  configName: undefined,
  configKey: undefined,
});

const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);
const loading = ref(false);
const configList = ref([]);

const dialogVisible = ref(false);
const dialogType = ref("add");
const configFormRef = ref(null);
const configForm = reactive({
  configId: null,
  configName: "",
  configKey: "",
  configValue: "",
  configType: "",
});

const configRules = reactive({
  configName: [
    { required: true, message: "请输入配置名称", trigger: "blur" },
    { max: 100, message: "配置名称长度不能超过100个字符", trigger: "blur" },
  ],
  configKey: [
    { required: true, message: "请输入配置键", trigger: "blur" },
    { max: 50, message: "配置键长度不能超过50个字符", trigger: "blur" },
  ],
  configValue: [
    { required: true, message: "请输入配置值", trigger: "blur" },
    { max: 2000, message: "配置值长度不能超过2000个字符", trigger: "blur" },
  ],
  configType: [
    { required: true, message: "请选择配置类型", trigger: "blur" },
  ],
});

onMounted(() => {
  fetchConfigList();
});

function fetchConfigList() {
  loading.value = true;
  const params = {
    ...searchForm,
    pageNum: pageNum.value,
    pageSize: pageSize.value,
  };
  getConfigPage(params).then((res) => {
    loading.value = false;
    configList.value = res.rows || [];
    total.value = res.total || 0;
  });
}

function handleSearch() {
  pageNum.value = 1;
  fetchConfigList();
}

function handleReset() {
  searchForm.configName = undefined;
  searchForm.configKey = undefined;
  pageNum.value = 1;
  fetchConfigList();
}

function handleSizeChange(val) {
  pageSize.value = val;
  pageNum.value = 1;
  fetchConfigList();
}

function handleCurrentChange(val) {
  pageNum.value = val;
  fetchConfigList();
}

function handleAdd() {
  dialogType.value = "add";
  Object.assign(configForm, {
    configId: null,
    configName: "",
    configKey: "",
    configValue: "",
    configType: "2",
  });
  dialogVisible.value = true;
}

function handleEdit(row) {
  loading.value = true;
  getConfigById({ configId: row.configId }).then((res) => {
    loading.value = false;
    const data = res.data;
    dialogType.value = "edit";
    Object.assign(configForm, {
      configId: data.configId,
      configName: data.configName,
      configKey: data.configKey,
      configValue: data.configValue,
      configType: data.configType,
    });
    dialogVisible.value = true;
  });
}

function handleSubmit() {
  configFormRef.value.validate((valid) => {
    if (valid) {
      if (dialogType.value === "add") {
        addConfig(configForm).then((res) => {
          ElMessage.success("新增系统参数成功");
          dialogVisible.value = false;
          fetchConfigList();
        });
      } else {
        updateConfig(configForm).then((res) => {
          ElMessage.success("修改系统参数成功");
          dialogVisible.value = false;
          fetchConfigList();
        });
      }
    }
  });
}

function handleDelete(row) {
  ElMessageBox.confirm(`确定要删除系统参数【${row.configName}】吗？`, "确认删除", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    deleteConfig({ configId: row.configId }).then((res) => {
      ElMessage.success("删除成功");
      fetchConfigList();
    });
  });
}
</script>

<style scoped>
</style>
```