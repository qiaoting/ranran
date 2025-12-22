<template>
  <div>
    <el-form :model="searchForm" ref="searchFormRef" :inline="true">
      <el-form-item label="用户名" prop="username">
        <el-input
          v-model="searchForm.menuName"
          placeholder="请输入菜单名称"
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
        <el-button type="primary" @click="handleAdd" plain>
          <el-icon>
            <Plus />
          </el-icon>
          新增菜单
        </el-button>
      </el-col>
    </el-row>
    <el-table
      v-loading="loading"
      :data="menuList"
      row-key="menuId"
      default-expand-all
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column label="图标" align="center">
        <template #default="scope">
          <el-icon v-if="scope.row.icon" class="icon">
            <component :is="getIconComponent(scope.row.icon)" />
          </el-icon>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="菜单名称" align="left" width="200">
        <template #default="scope">
          <span>{{ scope.row.menuName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="菜单类型" align="center" width="120">
        <template #default="scope">
          <el-tag
            :type="
              scope.row.menuType === menuTypes.DIRECTORY.value
                ? 'primary'
                : scope.row.menuType === menuTypes.MENU.value
                ? 'success'
                : 'info'
            "
          >
            {{ getMenuTypeName(scope.row.menuType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="路由地址" prop="path" align="center" />
      <el-table-column label="组件路径" align="center">
        <template #default="scope">
          <span v-if="scope.row.menuType === menuTypes.MENU.value">{{ scope.row.viewPath }}</span> 
        </template>
      </el-table-column>
      <el-table-column label="权限字符串" prop="permissions" align="center" />
      <el-table-column label="排序" prop="sort" align="center" width="80" />
      <el-table-column label="状态" align="center" width="100">
        <template #default="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="1"
            inactive-value="0"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template #default="scope">
          <el-button type="text" @click="handleAdd(scope.row)"
            >新增子菜单</el-button
          >
          <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
            type="text"
            text-color="#ff4d4f"
            @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      v-model="dialogVisible"
      :title="
        dialogType === 'add'
          ? parentMenu
            ? '新增子菜单'
            : '新增菜单'
          : '编辑菜单'
      "
      width="600px"
    >
      <el-form
        ref="menuFormRef"
        :model="menuForm"
        :rules="menuRules"
        label-width="120px"
      >
        <el-form-item label="上级菜单" prop="parentId">
          <el-tree-select
            v-model="menuForm.parentId"
            :data="treeMenuOptions"
            :props="treeProps"
            placeholder="请选择上级菜单"
            check-strictly
            style="width: 100%"
          >
            <template #default="scope">
              <span>{{ scope.data.menuName }}</span>
            </template>
          </el-tree-select>
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="menuForm.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="menuForm.menuType" @change="handleMenuTypeChange">
            <el-radio :label="menuTypes.DIRECTORY.value">{{
              menuTypes.DIRECTORY.label
            }}</el-radio>
            <el-radio :label="menuTypes.MENU.value">{{
              menuTypes.MENU.label
            }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="路由地址"
          prop="path"
        >
          <el-input v-model="menuForm.path" placeholder="请输入路由地址" />
        </el-form-item>
        <el-form-item
          label="组件路径"
          prop="viewPath"
          v-if="menuForm.menuType === menuTypes.MENU.value"
        >
          <el-input
            v-model="menuForm.viewPath"
            placeholder="请输入组件路径（如：system/menu/index）"
          />
        </el-form-item>
        <el-form-item label="权限字符串" prop="permissions"
            v-if="menuForm.menuType === menuTypes.MENU.value"
          >
          <el-input
            v-model="menuForm.permissions"
            placeholder="请输入权限字符串（如：system:menu:index）"
          />
        </el-form-item>
        <el-form-item
          label="图标"
          prop="icon"
          v-if="menuForm.menuType !== menuTypes.BUTTON.value"
        >
          <el-input
            v-model="menuForm.icon"
            placeholder="请选择图标（如：Menu）"
            @click="iconDialogVisible = true"
            readonly
            style="cursor: pointer"
          />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input
            v-model.number="menuForm.sort"
            type="number"
            placeholder="请输入排序号"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="menuForm.status">
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
    <el-dialog v-model="iconDialogVisible" title="选择图标" width="600px">
      <div class="icon-selector">
        <div
          v-for="(icon, name) in iconList"
          :key="name"
          class="icon-item"
          @click="selectIcon(name)"
        >
          <el-icon :size="24">
            <component :is="icon" />
          </el-icon>
          <span>{{ name }}</span>
        </div>
      </div>
      <template #footer>
        <el-button @click="iconDialogVisible = false">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Menu">
import { onMounted, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import { Plus } from "@element-plus/icons-vue";
import {
  addMenu,
  deleteMenu,
  getAllMenuTree,
  getMenuById,
  updateMenu,
} from "@/api/system/menu";

const menuTypes = {
  DIRECTORY: { value: 1, label: "目录" },
  MENU: { value: 2, label: "菜单" },
  BUTTON: { value: 3, label: "按钮" },
};

function getMenuTypeName(typeValue) {
  const type = Object.values(menuTypes).find(
    (item) => item.value === typeValue
  );
  return type ? type.label : "未知";
}

function getIconComponent(iconName) {
  return ElementPlusIconsVue[iconName] || null;
}

const iconList = ElementPlusIconsVue;

const loading = ref(false);
const menuList = ref([]);
const treeMenuOptions = ref([]);
const dialogVisible = ref(false);
const dialogType = ref("add");
const parentMenu = ref(null);
const menuFormRef = ref(null);
const iconDialogVisible = ref(false);

const searchForm = reactive({
  menuName: "",
  status: "",
});

const menuForm = reactive({
  menuId: null,
  parentId: 0,
  menuName: "",
  menuCode: "",
  menuType: menuTypes.DIRECTORY.value,
  path: "",
  viewPath: "",
  permissions: "",
  icon: "",
  sort: 0,
  status: "1",
});

const menuRules = reactive({
  menuName: [
    { required: true, message: "请输入菜单名称", trigger: "blur" },
    {min: 2, max: 50, message: '菜单名称长度在 2 到 50 个字符', trigger: 'blur'}
  ],
  menuType: [{ required: true, message: "请选择菜单类型", trigger: "change" }],
  path: [{ required: true, message: "请输入路由地址", trigger: "blur" },
        {min: 2, max: 50, message: '路由地址长度在 2 到 50 个字符', trigger: 'blur'}
  ],
  viewPath: [{ required: true, message: "请输入组件路径", trigger: "blur" }],
  permissions: [
    { required: true, message: "请输入权限字符串", trigger: "blur" },
  ],
  sort: [
    { required: true, message: "请输入排序号", trigger: "blur" },
    { type: "number", message: "排序号必须为数字", trigger: "blur" },
  ],
});

const treeProps = {
  value: "menuId",
  label: "menuName",
  children: "children",
};

const selectIcon = (name) => {
  menuForm.icon = name;
  iconDialogVisible.value = false;
};

onMounted(() => {
  fetchMenuList();
});

function fetchMenuList() {
  loading.value = true;
  getAllMenuTree(searchForm).then((res) => {
    loading.value = false;
    menuList.value = res.data;
    treeMenuOptions.value = [
      {
        menuId: 0,
        menuName: "无（顶级菜单）",
        children: res.data,
      },
    ];
  });
}

function handleSearch() {
  fetchMenuList();
}

function handleReset() {
  searchForm.menuName = "";
  searchForm.status = "";
  fetchMenuList();
}

function handleAdd(parent = null) {
  dialogType.value = "add";
  parentMenu.value = parent;
  Object.assign(menuForm, {
    menuId: null,
    parentId: parent ? parent.menuId : 0,
    menuName: "",
    menuCode: "",
    menuType: menuTypes.DIRECTORY.value,
    path: "",
    viewPath: "",
    permissions: "",
    icon: "",
    sort: 0,
    status: "1",
  });
  dialogVisible.value = true;
}

function handleEdit(row) {
  loading.value = true;
  getMenuById({ menuId: row.menuId }).then((res) => {
    loading.value = false;
    dialogType.value = "edit";
    Object.assign(menuForm, res.data);
    dialogVisible.value = true;
  });
}

const handleMenuTypeChange = (value) => {
  if (value === menuTypes.DIRECTORY.value) {
    menuForm.viewPath = 'ParentView';
    menuForm.permissions = '';
  } else if (value === menuTypes.MENU.value) {
    menuForm.viewPath = '';
  }
};

function handleSubmit() {
  menuFormRef.value.validate().then((valid) => {
    if (dialogType.value === "add") {
      addMenu(menuForm).then((res) => {
        dialogVisible.value = false;
        ElMessage.success("新增菜单成功");
        fetchMenuList();
      });
    } else {
      updateMenu(menuForm).then((res) => {
        dialogVisible.value = false;
        ElMessage.success("修改菜单成功");
        fetchMenuList();
      });
    }
  });
}

function handleDelete(row) {
  if (row.children && row.children.length) {
    ElMessage.warning("请先删除子菜单");
    return;
  }
  ElMessageBox.confirm(`是否确认删除菜单【${row.menuName}】？`, "确认删除", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    deleteMenu({ menuId: row.menuId }).then((res) => {
      ElMessage.success("删除菜单成功");
      fetchMenuList();
    });
  });
}

function handleStatusChange(row) {
  updateMenu({
    menuId: row.menuId,
    status: row.status,
  })
    .then((res) => {
      const statusText = row.status === "1" ? "启用" : "禁用";
      ElMessage.success(`已${statusText}菜单`);
    })
    .catch((error) => {
      row.status = row.status === "1" ? "0" : "1";
    });
}
</script>

<style scoped>
.menu-actions {
  display: flex;
  gap: 10px;
}

.icon-selector {
  /* 限制容器最大高度并添加滚动 */
  max-height: 200px;
  overflow-y: auto;
  /* 使用网格布局实现多列 */
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 16px;
  padding: 10px;
}

.icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.icon-item:hover {
  background-color: #f5f7fa;
  transform: translateY(-2px);
}

.icon-item span {
  margin-top: 8px;
  font-size: 0.9rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
  text-align: center;
}
</style>
