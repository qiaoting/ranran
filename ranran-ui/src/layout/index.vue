<template>
  <el-container class="el-container">
    <el-aside :style="{
      width: sidebarStore.isCollapse ? '64px' : '210px',
      minWidth: sidebarStore.isCollapse ? '64px' : '210px',
      maxWidth: sidebarStore.isCollapse ? '64px' : '210px',
      transition: 'width 0.2s ease-in-out'
    }">
      <Sidebar/>
    </el-aside>
    <el-container>
      <el-header class="header" height="50px">
        <Header/>
      </el-header>
      <el-main>
          <router-view v-slot="{ Component }">
            <transition name="fade" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import Sidebar from './Sidebar.vue'
import Header from './Header.vue'
import {useSidebarStore} from '@/store'

const sidebarStore = useSidebarStore()

</script>

<style scoped>
.el-container {
  height: 100vh;
}
.header {
  border-bottom: 1px solid #e5e7eb;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>