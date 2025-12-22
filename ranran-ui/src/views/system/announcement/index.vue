<template>
  <div>
    <el-form :model="searchForm" ref="searchFormRef" :inline="true">
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="searchForm.title"
          placeholder="请输入公告标题"
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
          新增公告
        </el-button>
      </el-col>
    </el-row>
    <el-table
      :data="announcementList"
      :loading="loading"
      style="margin-top: 15px"
    >
      <el-table-column prop="title" label="公告标题" align="center" />
      <el-table-column prop="content" label="公告内容" align="center">
        <template #default="scope">
          <el-tooltip placement="top">
            <template #content>
            <div class="content-tip">
              {{ scope.row.content }}
            </div>
            </template>
            <div class="content-cell">{{ scope.row.content }}</div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="公告类型" align="center">
        <template #default="scope">
          <el-tag :type="getTypeTag(scope.row.announcementType)">
            <el-icon style="margin-right: 5px;">
              <component :is="getTypeIcon(scope.row.announcementType)" />
            </el-icon>
            {{ getTypeName(scope.row.announcementType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" align="center">
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
      :title="dialogType === 'add' ? '新增公告' : '编辑公告'"
      :width="600"
    >
      <el-form
        ref="announcementFormRef"
        :model="announcementForm"
        :rules="announcementRules"
        label-width="100px"
      >
        <el-form-item label="公告标题" prop="title">
          <el-input
            v-model="announcementForm.title"
            placeholder="请输入公告标题"
          />
        </el-form-item>
        <el-form-item label="公告类型" prop="announcementType">
          <el-select
            v-model="announcementForm.announcementType"
            placeholder="请选择公告类型"
          >
            <el-option label="系统公告" value="1" />
            <el-option label="活动通知" value="2" />
            <el-option label="其他通知" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="公告内容" prop="content">
          <el-input
            v-model="announcementForm.content"
            type="textarea"
            rows="5"
            placeholder="请输入公告内容"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="announcementForm.status">
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

<script setup name="Announcement">
import { onMounted, reactive, ref, createVNode } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import {
  addAnnouncement,
  deleteAnnouncement,
  getAnnouncementById,
  getAnnouncementPage,
  updateAnnouncement,
} from "@/api/system/announcement";
import { announcementTypeMap, announcementTagMap,announcementIconMap } from "@/utils/dict";

const searchForm = reactive({
  title: undefined,
  status: undefined,
});

const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);
const loading = ref(false);
const announcementList = ref([]);

const dialogVisible = ref(false);
const dialogType = ref("add");
const announcementFormRef = ref(null);
const announcementForm = reactive({
  announcementId: null,
  title: "",
  content: "",
  announcementType: "1",
  status: "1",
});

const announcementRules = reactive({
  title: [
    { required: true, message: "请输入公告标题", trigger: "blur" },
    { max: 100, message: "公告标题长度不能超过100个字符", trigger: "blur" },
  ],
  content: [
    { required: true, message: "请输入公告内容", trigger: "blur" },
    { max: 2000, message: "公告内容长度不能超过2000个字符", trigger: "blur" },
  ],
  announcementType: [
    { required: true, message: "请选择公告类型", trigger: "change" },
  ],
});

onMounted(() => {
  fetchAnnouncementList();
});

function fetchAnnouncementList() {
  loading.value = true;
  const params = {
    ...searchForm,
    pageNum: pageNum.value,
    pageSize: pageSize.value,
  };
  getAnnouncementPage(params).then((res) => {
    loading.value = false;
    announcementList.value = res.rows || [];
    total.value = res.total || 0;
  });
}

function handleSearch() {
  pageNum.value = 1;
  fetchAnnouncementList();
}

function handleReset() {
  searchForm.title = undefined;
  searchForm.status = undefined;
  pageNum.value = 1;
  fetchAnnouncementList();
}

function handleSizeChange(val) {
  pageSize.value = val;
  pageNum.value = 1;
  fetchAnnouncementList();
}

function handleCurrentChange(val) {
  pageNum.value = val;
  fetchAnnouncementList();
}

function handleAdd() {
  dialogType.value = "add";
  Object.assign(announcementForm, {
    announcementId: null,
    title: "",
    content: "",
    announcementType: "1",
    status: "1",
  });
  dialogVisible.value = true;
}

function handleEdit(row) {
  loading.value = true;
  getAnnouncementById({ announcementId: row.announcementId }).then((res) => {
    loading.value = false;
    const data = res.data;
    dialogType.value = "edit";
    Object.assign(announcementForm, {
      announcementId: data.announcementId,
      title: data.title,
      content: data.content,
      announcementType: data.announcementType,
      status: data.status,
    });
    dialogVisible.value = true;
  });
}

function handleSubmit() {
  announcementFormRef.value.validate((valid) => {
    if (valid) {
      if (dialogType.value === "add") {
        addAnnouncement(announcementForm).then((res) => {
          ElMessage.success("新增公告成功");
          dialogVisible.value = false;
          fetchAnnouncementList();
        });
      } else {
        updateAnnouncement(announcementForm).then((res) => {
          ElMessage.success("修改公告成功");
          dialogVisible.value = false;
          fetchAnnouncementList();
        });
      }
    }
  });
}

function handleStatusChange(row) {
  updateAnnouncement({
    announcementId: row.announcementId,
    status: row.status,
  })
    .then((res) => {
      const statusText = row.status === "1" ? "启用" : "禁用";
      ElMessage.success(`已${statusText}公告`);
    })
    .catch((error) => {
      row.status = row.status === "1" ? "0" : "1";
    });
}

function handleDelete(row) {
  ElMessageBox.confirm(`确定要删除公告【${row.title}】吗？`, "确认删除", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    deleteAnnouncement({ announcementId: row.announcementId }).then((res) => {
      ElMessage.success("删除成功");
      fetchAnnouncementList();
    });
  });
}

function getTypeName(type) {
  return announcementTypeMap[type];
}
function getTypeTag(type) {
  return announcementTagMap[type];
}
function getTypeIcon(type) {
  return announcementIconMap[type];
}
</script>

<style scoped>
.content-tip {
  max-width: 500px; 
  white-space: normal; 
  word-break: break-all;
}
.content-cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 300px;
}
</style>
