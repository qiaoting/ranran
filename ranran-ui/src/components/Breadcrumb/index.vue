<template>
  <el-breadcrumb separator="/" class="breadcrumb-container">
    <el-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index" :to="item.redirectedPath || item.path">
      {{ item.displayTitle }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup>
import {computed} from 'vue'
import {useRoute} from 'vue-router'

const route = useRoute()

// 规则：
// 1) 过滤 meta.show === false 的项
// 2) 文案优先级：meta.breadcrumb(字符串) > meta.title
// 3) 支持 meta.breadcrumbPath 指定跳转路径
const breadcrumbList = computed(() => {
  return route.matched
      .filter(item => item.meta?.show !== false)
      .map(item => {
        const displayTitle = typeof item.meta?.breadcrumb === 'string'
            ? item.meta.breadcrumb
            : (item.meta?.breadcrumbTitle || item.meta?.title || '')
        const redirectedPath = item.meta?.breadcrumbPath
        return {
          ...item,
          displayTitle,
          redirectedPath
        }
      })
})
</script>

<style scoped>
.breadcrumb-container {
  padding: 5px 0;
}
</style>