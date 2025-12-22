<template>
  <div class="app-container">
    <div class="header">
      <h1 class="title">纯手工打造 · 然然管理系统</h1>
      <p class="tagline">匠心编码 · 轻量简洁</p>
    </div>

    <div class="tech-container">
      <div class="tech-card backend">
        <h3 class="card-title">后端技术栈</h3>
        <p class="tech-list">Java / Spring Boot4 </p>
        <p class="tech-list">Spring Security / Spring Task</p>
        <p class="tech-list">MyBatis-Plus</p>
        <p class="tech-list">Redis / MySQL</p>
      </div>

      <div class="tech-card frontend">
        <h3 class="card-title">前端技术栈</h3>
        <p class="tech-list">Vue3 / Vite</p>
        <p class="tech-list"> Router / Axios / Pinia</p>
        <p class="tech-list">Element-Plus</p>
      </div>
    </div>
    <div class="payment-container">
      <h3 class="payment-title">支持与赞助</h3>
      <p class="payment-desc">如果觉得本系统对您有帮助，欢迎请作者喝杯咖啡表示鼓励</p>
      
      <div class="qr-code-container">
        <div class="qr-code-item">
          <div class="qr-code">
            <img src="@/assets/image/alipay-ranran.png" alt="喝杯咖啡" class="qr-img">
          </div>
          <div class="qr-label">支付宝扫码</div>
        </div>
        
        <div class="qr-code-item">
          <div class="qr-code">
            <img src="@/assets/image/wechat-ranran.png" alt="喝杯咖啡" class="qr-img">
          </div>
          <div class="qr-label">微信扫码</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup name="Home"></script>

<style scoped>
.app-container {
  padding: 20px;
  max-width: 1100px;
  margin: 0 auto;
  font-family: "Inter", -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
}

.header {
  text-align: center;
  margin-bottom: 40px;
}

.title {
  font-size: 2.2rem;
  color: #2d3748;
  margin: 0 0 10px;
  font-weight: 600;
}

.tagline {
  color: #4299e1;
  font-size: 1.1rem;
  margin: 0;
}

.tech-container {
  display: flex;
  gap: 30px;
  flex-wrap: wrap;
  margin-bottom: 40px;
}

.tech-card {
  flex: 1;
  min-width: 350px;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.backend {
  border-top: 3px solid #48bb78;
}

.frontend {
  border-top: 3px solid #4299e1;
}

.card-title {
  color: #2d3748;
  margin: 0 0 15px;
  font-size: 1.3rem;
}

.tech-list {
  color: #4a5568;
  margin: 8px 0;
  line-height: 1.6;
}

.philosophy {
  text-align: center;
  color: #718096;
  font-size: 1rem;
  line-height: 1.8;
  max-width: 800px;
  margin: 0 auto;
}

.payment-container {
  margin: 60px 0;
  text-align: center;
}

.payment-title {
  font-size: 1.2rem;
  color: #2d3748;
  margin: 0 0 15px;
  font-weight: 500;
}

.payment-desc {
  color: #718096;
  margin: 0 0 30px;
  font-size: 0.9rem;
}

.qr-code-container {
  display: flex;
  justify-content: center;
  gap: 60px;
  flex-wrap: wrap;
  margin-top: 20px;
}

.qr-code-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.qr-code {
  width: 200px;
  height: 200px;
  padding: 10px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  margin-bottom: 15px;
}

.qr-img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.qr-label {
  color: #718096;
  font-size: 0.8rem;
  font-weight: 500;
}

</style>