<template>
  <div class="header-container">
    <div class="header-left">
      <div @click="toggleSidebar">
        <el-icon>
          <component :is="sidebarStore.isCollapse ? 'Expand' : 'Fold'"/>
        </el-icon>
      </div>
      <Breadcrumb/>
    </div>

    <div class="header-right">
      <el-dropdown @command="handleCommand">
        <div class="avatar-dropdown">
          <el-avatar :src="userStore.info?.avatar ? (baseUrl + userStore.info?.avatar) : defaultAvatar"/>
          <span>{{ userStore.info?.nickname }}</span>
          <el-icon>
            <arrow-down-bold/>
          </el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item :icon="Avatar" command="goMyProfile">
              个人中心
            </el-dropdown-item>
            <el-dropdown-item :icon="SwitchButton" command="logout">
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, watch, createVNode, ref } from "vue";
import {useRouter} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {useSidebarStore} from '@/store'
import {useUserStore} from '@/store/user'
import Breadcrumb from '@/components/Breadcrumb/index.vue'
import {Avatar, SwitchButton} from '@element-plus/icons-vue'
import defaultAvatar from '@/assets/image/avatar.png'
import { ElNotification } from "element-plus";
import { useAnnouncementStore } from "@/store/announcement";
import { announcementIconMap, announcementColorMap } from "@/utils/dict";

const store = useAnnouncementStore();
const activeAnnouncementId = ref(null);
const userStore = useUserStore()
const baseUrl = ref(import.meta.env.VITE_APP_BASE_API)

const router = useRouter()
const sidebarStore = useSidebarStore()

function toggleSidebar() {
  sidebarStore.toggleCollapse()
}

function handleCommand(command) {
  switch (command) {
    case 'logout':
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        userStore.logout().then(() => {
          ElMessage.success('退出登录成功')
          router.push('/login')
        })
      })
      break
    case 'goMyProfile':
      router.push('/myself/profile')
      break
  }
}

watch(
  () => store.list,
  (newList) => {
    if (newList.length > 0) {
      const announcement = newList[0];
      if (activeAnnouncementId.value === announcement.announcementId) {
        return;
      }
      const IconComponent = announcementIconMap[announcement.announcementType];
      activeAnnouncementId.value = announcement.announcementId;
      ElNotification({
        title: announcement.title,
        message: announcement.content,
        offset: 40, 
        duration: 0, 
        icon: () => createVNode(IconComponent, { 
          style: { 
            color: announcementColorMap[announcement.announcementType],
          } 
        }), 
        onClose: () => {
          activeAnnouncementId.value = null;
          store.closeAnnouncement(announcement.announcementId);
        }
      });
    }
  },
  { immediate: true }
);

let intervalId = null; 
onMounted(() => {
  store.fetchValidAnnouncements();
  intervalId = setInterval(() => {
    store.fetchValidAnnouncements();
  }, 60000);
});
onUnmounted(() => {
  if (intervalId) {
    clearInterval(intervalId);
  }
});

</script>

<style scoped>
.header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 50px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.header-right {
  display: flex;
  align-items: center;
}

.avatar-dropdown {
  display: flex;
  align-items: center;
  gap: 4px;
  outline: none;
}
</style>