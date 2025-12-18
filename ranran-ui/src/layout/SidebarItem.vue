<template>
  <el-menu-item v-if="!hasChildren(item)" :index="resolvePath(item)" :key="item.menuId + '-item'">
    <el-icon v-if="item.icon">
      <component :is="IconComponent"/>
    </el-icon>
    <span class="title">
      {{ item.menuName }}
    </span>
  </el-menu-item>
  <el-sub-menu v-else :index="resolvePath(item)" :key="item.menuId + '-sub'">
    <template #title>
      <el-icon v-if="item.icon">
        <component :is="IconComponent"/>
      </el-icon>
      <span v-if="!isCollapse" class="title">
        {{ item.menuName }}
      </span>
    </template>
    <template v-for="child in visibleChildren(item)" :key="child.menuId">
      <sidebar-item :item="child" :parent-path="resolvePath(item)" :is-collapse="isCollapse"/>
    </template>
  </el-sub-menu>
</template>

<script setup>
import {computed, defineProps} from 'vue'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const props = defineProps({
  item: {type: Object, required: true},
  parentPath: {type: String, default: ''},
  isCollapse: {type: Boolean, default: false}
})

const IconComponent = computed(() => {
  if (!props.item.icon) return null
  return ElementPlusIconsVue[props.item.icon] || null
})

function resolvePath(item) {
  if (item.path.startsWith('/')) return item.path
  const parent = props.parentPath.replace(/\/$/, '')
  return parent ? `${parent}/${item.path}` : `/${item.path}`
}

function hasChildren(item) {
  return item.children && item.children.filter(child => child.status === '1').length > 0
}

function visibleChildren(item) {
  return (item.children || []).filter(child => child.status === '1')
}
</script>

<style scoped>
.title {
  margin-left: 8px;
}
</style>