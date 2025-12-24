<template>
  <div>
    <el-form :model="form" inline>
      <el-form-item label="角色名称">
        <el-input v-model="form.roleName" placeholder="请输入角色名称" clearable
            class="query_input"  @keyup.enter="handleSearch"/>
      </el-form-item>
      <el-form-item label="角色编码">
        <el-input v-model="form.roleCode" placeholder="请输入角色编码" clearable
            class="query_input" @keyup.enter="handleSearch"/>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="form.status" placeholder="请选择状态" clearable style="width: 160px;">
          <el-option label="启用" value="1"/>
          <el-option label="禁用" value="0"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row class="mt12 mb8">
      <el-col>
        <el-button type="primary" @click="handleAdd" plain>
        <el-icon>
          <Plus/>
        </el-icon>
        新增角色
      </el-button>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="tableData">
      <el-table-column label="角色名称" prop="roleName" align="center"/>
      <el-table-column label="角色编码" prop="roleCode" align="center"/>
      <el-table-column label="角色描述" prop="description" align="center"/>
      <el-table-column label="是否启用" align="center" width="100">
        <template #default="scope">
          <el-switch v-model="scope.row.status" active-value="1" inactive-value="0"
                      @change="handleStatusChange(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="180" align="center"/>
      <el-table-column label="更新时间" prop="updateTime" width="180" align="center"/>
      <el-table-column label="操作" align="center" width="180">
        <template #default="scope">
          <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" @click="handleDelete(scope.row)">删除</el-button>
          <el-button type="text" @click="handleAssignUser(scope.row)">
            分配用户
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
          @current-change="handleCurrentChange"/>
    </div>
    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增角色' : '编辑角色'" width="600px">
      <el-form ref="roleFormRef" :model="roleForm" :rules="roleRules" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称"/>
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码"/>
        </el-form-item>
        <el-form-item label="菜单权限" prop="menuIds">
          <el-scrollbar style="max-height: 300px; padding-right: 5px;">
            <el-tree ref="menuTreeRef" :check-strictly="true" :data="menuList" show-checkbox node-key="menuId"
                     :default-expand-all="true"
                     :props="treeProps" @check="handleMenuCheck"/>
          </el-scrollbar>
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input v-model="roleForm.description" placeholder="请输入角色描述" type="textarea" :rows="3"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="roleForm.status">
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

<script setup name="Role">
import {Plus} from '@element-plus/icons-vue'
import {addRole, deleteRole, getRoleById, getRolePage, updateRole, changeStatus} from '@/api/system/role'
import {getAllMenuTree} from '@/api/system/menu'

const router = useRouter()

function handleAssignUser(row) {
  router.push('/system/role-user/index/' + row.roleId)
}

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const form = ref({
  roleName: '',
  roleCode: '',
  status: ''
})

const dialogVisible = ref(false)
const dialogType = ref('add')
const roleFormRef = ref(null)
const roleForm = reactive({
  roleId: null,
  roleName: '',
  roleCode: '',
  description: '',
  sort: 0,
  status: '1',
  menuIds: []
})

const roleRules = reactive({
  roleName: [
    {required: true, message: '请输入角色名称', trigger: 'blur'},
    {max: 50, message: '角色名称长度不能超过50个字符', trigger: 'blur'}
  ],
  roleCode: [
    {required: true, message: '请输入角色编码', trigger: 'blur'},
    {min: 2, max: 50, message: '角色编码长度在 2 到 50 个字符', trigger: 'blur'}
  ]
})

const menuList = ref([])
const checkedMenuIds = ref([])
const treeProps = {
  value: 'menuId',
  label: 'menuName',
  children: 'children'
}
const menuTreeRef = ref(null)

onMounted(() => {
  getRoleList()
  loadMenuData()
})

function getRoleList() {
  loading.value = true
  getRolePage({
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    ...form.value
  }).then(res => {
    loading.value = false
    tableData.value = res.rows || []
    total.value = res.total || 0
  })
}

function handleSearch() {
  pageNum.value = 1
  getRoleList()
}

function handleReset() {
  form.value = {
    roleName: '',
    roleCode: '',
    status: ''
  }
  pageNum.value = 1
  getRoleList()
}

function handleSizeChange(val) {
  pageSize.value = val
  pageNum.value = 1
  getRoleList()
}

function handleCurrentChange(val) {
  pageNum.value = val
  getRoleList()
}

function handleAdd() {
  dialogType.value = 'add'
  Object.assign(roleForm, {
    roleId: null,
    roleName: '',
    roleCode: '',
    description: '',
    sort: 0,
    status: '1',
    menuIds: []
  })
  checkedMenuIds.value = []
  if (menuTreeRef.value) {
    menuTreeRef.value.setCheckedKeys([])
  }
  dialogVisible.value = true
}

function handleEdit(row) {
  loading.value = true
  checkedMenuIds.value = []
  if (menuTreeRef.value) {
    menuTreeRef.value.setCheckedKeys([])
  }
  getRoleById({roleId: row.roleId}).then(res => {
    loading.value = false
    dialogType.value = 'edit'
    Object.assign(roleForm, res.data)
    checkedMenuIds.value = res.data.menuIds || []
    nextTick(() => {
      if (menuTreeRef.value) {
        menuTreeRef.value.setCheckedKeys(checkedMenuIds.value)
      }
    })
    dialogVisible.value = true
  })
}

function handleSubmit() {
  roleFormRef.value.validate().then(valid => {
    if (valid) {
      roleForm.menuIds = checkedMenuIds.value.map(id => Number(id))
      if (dialogType.value === 'add') {
        addRole(roleForm).then(res => {
          ElMessage.success('新增角色成功')
          dialogVisible.value = false
          getRoleList()
        })
      } else {
        updateRole(roleForm).then(res => {
          ElMessage.success('编辑角色成功')
          dialogVisible.value = false
          getRoleList()
        })
      }
    }
  })
}

function handleDelete(row) {
  ElMessageBox.confirm(
      `是否确认删除角色【${row.roleName}】？`,
      '确认删除',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }
  ).then(() => {
    deleteRole({roleId: row.roleId}).then(res => {
      ElMessage.success('删除成功')
      getRoleList()
    })
  })
}

function handleStatusChange(row) {
  changeStatus({
    roleId: row.roleId,
    status: row.status
  }).then(res => {
    const statusText = row.status === '1' ? '启用' : '禁用'
    ElMessage.success(`已${statusText}角色`)
  }).catch(error => {
    row.status = row.status === '1' ? '0' : '1'
  })
}

function loadMenuData() {
  return getAllMenuTree({}).then(res => {
    menuList.value = res.data || []
  })
}

function handleMenuCheck(currentKey, event) {
  let checkedKeys = menuTreeRef.value.getCheckedKeys()
  let halfCheckedKeys = menuTreeRef.value.getHalfCheckedKeys()
  checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
  checkedMenuIds.value = checkedKeys
}

</script>

<style scoped>

</style>