<template>
  <div>
    <span></span>
  </div>
  <div class="sidebar-container">
    <div class="sidebar-logo">
      <img src="@/assets/image/logo.svg" class="sidebar-logo-img" alt="然然管理系统"/>
      <div class="sidebar-logo-text" :class="{ hidden: sidebarStore.isCollapse }">
        <h1 class="logo-main-title">然然管理系统</h1>
      </div>
    </div>
    <el-menu :default-active="activeMenu" :default-openeds="openMenus" mode="vertical"
             background-color="rgb(50, 66, 88)" text-color="#fff" active-text-color="#ffd04b"
             :collapse="sidebarStore.isCollapse" :collapse-transition="false" :router="true" :unique-opened="true"
             style="flex: 1;">
      <template v-for="menu in menuRoutes">
        <sidebar-item :item="menu" :parent-path="''" :is-collapse="sidebarStore.isCollapse"/>
      </template>
    </el-menu>
  </div>
</template>

<script setup>
import {useSidebarStore} from '@/store'
import {useMenuStore} from '@/store/menu'
import SidebarItem from './SidebarItem.vue'

const sidebarStore = useSidebarStore()
const menuStore = useMenuStore()
const route = useRoute()

const {menuList} = storeToRefs(menuStore)

const activeMenu = computed(() => route.meta?.activeMenu || route.path)

const menuRoutes = computed(() => {
  return menuList.value
})

const openMenus = computed(() => {
  const matched = route.matched?.map(r => r.path) || []
  return matched
})
</script>

<style scoped>
.sidebar-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: rgb(50, 66, 88);
}

.sidebar-logo {
  border-right: 1px solid var(--el-menu-border-color);
  background-color: rgb(50, 66, 88);
  display: flex;
  align-items: center;
  justify-content: flex-start;
  padding: 0 16px;
  height: 64px;
  cursor: pointer;
  transition: all 0.2s;
  color: #fff;

  .sidebar-logo-img {
    width: 32px;
    height: 32px;
    border-radius: 4px;
    object-fit: contain;
  }

  .sidebar-logo-text {
    margin-left: 8px;
    overflow: hidden;

    .logo-main-title {
      margin: 0;
      font-size: 1rem;
      font-weight: 600;
      line-height: 1.2;
    }

    .logo-sub-title {
      margin: 4px 0 0 0;
      font-size: 0.9rem;
      line-height: 1;
    }
  }

  .hidden {
    display: none;
  }
}
</style>