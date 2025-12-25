<template>
  <el-breadcrumb separator="/" class="breadcrumb-container">
    <el-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index" :to="item.redirectedPath || item.path">
      {{ item.displayTitle }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup>

const route = useRoute()

const breadcrumbList = computed(() => {
  return route.matched
      .map(item => {
        const displayTitle = item.meta.title
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