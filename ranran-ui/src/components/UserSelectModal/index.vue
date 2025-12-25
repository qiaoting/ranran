<template>
  <el-dialog :modelValue="modelValue" @update:modelValue="handleDialogUpdate" title="添加用户到角色" :width="750"
             @close="handleClose" append-to-body>
    <el-form :model="searchForm" :inline="true" class="search-form">
      <el-form-item label="用户名">
        <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable style="width: 200px"
                  @keyup.enter="fetchUserList"/>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="searchForm.nickname" placeholder="请输入昵称" clearable style="width: 200px"
                  @keyup.enter="fetchUserList"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchUserList">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="userList" :loading="loading" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column prop="username" label="用户名" align="center"/>
      <el-table-column prop="nickname" label="昵称" align="center"/>
      <el-table-column prop="email" label="邮箱" align="center"/>
      <el-table-column prop="phone" label="手机号" align="center"/>
      <el-table-column prop="status" label="状态" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'">
            {{ scope.row.status === '1' ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination" v-if="total > 0">
      <el-pagination :current-page="pageNum" :page-size="pageSize" :total="total" :page-sizes="[10, 20, 50, 100]"
                     layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"/>
    </div>
    <template #footer>
      <el-button @click="emit('update:modelValue', false)">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确认添加</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import {getUnallocatedPage} from '@/api/system/roleUser'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  roleId: {
    type: String,
    required: true
  },
  assignedUserIds: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['update:modelValue', 'confirm', 'close'])

const searchForm = reactive({
  username: '',
  nickname: ''
})
const userList = ref([])
const loading = ref(false)
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const selectedUserIds = ref([])
const multipleSelection = ref([])

watch(() => props.modelValue, (newVal) => {
  if (newVal) {
    pageNum.value = 1
    fetchUserList()
  } else {
    resetSearch()
    selectedUserIds.value = []
    multipleSelection.value = []
  }
})

watch(() => props.assignedUserIds, (newVal) => {
  if (props.modelValue) {
    selectedUserIds.value = [...newVal]
  }
}, {deep: true})

function handleDialogUpdate(value) {
  emit('update:modelValue', value)
}

function fetchUserList() {
  loading.value = true
  const params = {
    ...searchForm,
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    roleId: props.roleId
  }
  getUnallocatedPage(params).then(res => {
    loading.value = false
    userList.value = res.rows || []
    total.value = res.total || 0
  })
}

function resetSearch() {
  searchForm.username = ''
  searchForm.nickname = ''
  pageNum.value = 1
  fetchUserList()
}

function handleSizeChange(val) {
  pageSize.value = val
  pageNum.value = 1
  fetchUserList()
}

function handleCurrentChange(val) {
  pageNum.value = val
  fetchUserList()
}

function handleSelectionChange(val) {
  multipleSelection.value = val
  selectedUserIds.value = val.map(item => item.userId)
}

function handleConfirm() {
  if (selectedUserIds.value.length === 0) {
    ElMessage.warning('请选择用户')
    return
  }
  emit('confirm', {
    roleId: props.roleId,
    userIds: selectedUserIds.value
  })
  emit('update:modelValue', false)
}

function handleClose() {
  emit('update:modelValue', false)
  emit('close')
}
</script>

<style scoped>
.search-form {
  margin-bottom: 15px;
}

.pagination {
  margin-top: 15px;
  text-align: right;
}
</style>