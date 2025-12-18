<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <div class="error-code">404</div>
      <div class="error-message">页面不存在</div>
      <p class="error-desc">
        您访问的页面不存在或已被删除，请检查网址是否正确
      </p>
      <el-button type="primary" @click="$router.push('/')" class="back-home">
        返回首页
      </el-button>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.not-found-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
}

.not-found-content {
  text-align: center;
  padding: 50px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.error-code {
  font-size: 5rem;
  font-weight: 700;
  color: #409eff;
  margin-bottom: 20px;
}

.error-message {
  font-size: 1.5rem;
  color: #303133;
  margin-bottom: 10px;
}

.error-desc {
  font-size: 0.9rem;
  color: #606266;
  margin-bottom: 30px;
  max-width: 400px;
}

.back-home {
  padding: 10px 20px;
  font-size: 0.9rem;
}
</style>