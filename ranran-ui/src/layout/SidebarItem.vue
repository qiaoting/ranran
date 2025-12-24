<template>
  <el-menu-item v-if="item.show === true && !hasChildren(item)" :index="resolvePath(item)">
    <el-icon v-if="item.meta?.icon">
      <component :is="IconComponent"/>
    </el-icon>
    <span class="title">
      {{ item.meta?.title }}
    </span>
  </el-menu-item>
  <el-sub-menu v-if="hasChildren(item)" :index="resolvePath(item)">
    <template v-if="item.show === true" #title>
      <el-icon v-if="item.meta?.icon">
        <component :is="IconComponent"/>
      </el-icon>
      <span v-if="!isCollapse" class="title">
        {{ item.meta?.title }}
      </span>
    </template>
    <template v-for="child in item.children">
      <sidebar-item :item="child" :parent-path="resolvePath(item)" :is-collapse="isCollapse"/>
    </template>
  </el-sub-menu>
</template>

<script setup>
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const props = defineProps({
  item: {type: Object, required: true},
  parentPath: {type: String, default: ''},
  isCollapse: {type: Boolean, default: false}
})

const IconComponent = computed(() => {
  if (!props.item.meta.icon) return null
  return ElementPlusIconsVue[props.item.meta.icon] || null
})

function resolvePath(item) {
  if (item.path.startsWith('/')) return item.path
  const parent = props.parentPath.replace(/\/$/, '')
  const path =  parent ? `${parent}/${item.path}` : `/${item.path}`
  return path
}

function hasChildren(item) {
  return item.children && item.children.filter(child => child.show === true).length > 0
}

</script>

<style scoped>
.title {
  margin-left: 8px;
}
</style>