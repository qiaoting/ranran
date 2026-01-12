<template>
  <div>
    <el-table
      :data="generatorTableList"
      :loading="loading"
    >
      <el-table-column prop="tableName" label="表名" align="center" min-width="200" />
      <el-table-column prop="engine" label="存储引擎" align="center" width="180" />
      <el-table-column prop="comment" label="表注释" align="center" min-width="300" />
      <el-table-column label="操作" align="center" width="180">
        <template #default="scope">
          <el-button
            type="text"
            size="small"
            @click="openGenerateDialog(scope.row)"
          >
            生成代码
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      v-model="dialogVisible"
      title="生成代码"
      width="70%"
      :close-on-click-modal="false"
    >
      <div class="query-params">
        <el-row :gutter="20">
          <el-col :span="5">
            <el-input
              v-model="queryParam.moduleName"
              placeholder="模块名（如：system）"
              label="模块名"
              clearable
            ></el-input>
          </el-col>
          <el-col :span="5">
            <el-input
              v-model="queryParam.functionName"
              placeholder="功能名（如：用户管理）"
              label="功能名"
              clearable
            ></el-input>
          </el-col>
          <el-col :span="5">
            <el-input
              v-model="queryParam.author"
              placeholder="作者"
              label="作者"
              clearable
            ></el-input>
          </el-col>
          <el-col :span="5">
            <el-button
              type="primary"
              @click="generateTableCode"
              :loading="loading"
            >
              重新生成
            </el-button>
          </el-col>
        </el-row>
      </div>

      <el-tabs v-model="activeCodeTab" type="card">
        <el-tab-pane 
          v-for="(code, key) in codeFiles" 
          :key="key" 
          :label="key" 
          :name="key"
        >
          <div class="code-header">
            <el-button 
              type="text" 
              size="small" 
              @click="copyCode(code, key)"
            >
              <el-icon><CopyDocument /></el-icon>
              复制代码
            </el-button>
          </div>
          <div class="code-content">
            <pre v-html="highlightCode(code, getLanguage(key))"></pre>
          </div>
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <el-button @click="dialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="GeneratorTableList">
import { onMounted, ref, reactive } from "vue";
import { listTable, generateCode } from "@/api/tool/generator"; 
import { CopyDocument } from '@element-plus/icons-vue'
import { ElMessage } from "element-plus";
import hljs from 'highlight.js/lib/core';
import java from 'highlight.js/lib/languages/java';
import xml from 'highlight.js/lib/languages/xml';
import javascript from 'highlight.js/lib/languages/javascript';
import 'highlight.js/styles/atom-one-dark.css';

hljs.registerLanguage('java', java);
hljs.registerLanguage('xml', xml);
hljs.registerLanguage('javascript', javascript);

const loading = ref(false);
const generatorTableList = ref([]);
const dialogVisible = ref(false);
const activeCodeTab = ref("");
const codeFiles = reactive({});
const queryParam = reactive({
  tableName: '',
  moduleName: '',
  functionName: '',
  author: ''
});

onMounted(() => {
  fetchGeneratorTableList();
});

function fetchGeneratorTableList() {
  loading.value = true;
  listTable()
    .then((res) => {
      loading.value = false;
      generatorTableList.value = res.data || [];
    })
}

function openGenerateDialog(row) {
  queryParam.tableName = row.tableName;
  queryParam.moduleName = 'system';
  queryParam.functionName = row.comment;
  queryParam.author = 'ranran';
  generateTableCode();
}

function generateTableCode() {
  loading.value = true;
  generateCode(queryParam)
    .then((res) => {
      loading.value = false;
      Object.assign(codeFiles, res.data || {});
      const firstKey = Object.keys(codeFiles)[0];
      if (firstKey) activeCodeTab.value = firstKey;
      dialogVisible.value = true;
    })
}

function highlightCode(code, language) {
  if (!code) return '';
  return hljs.highlight(code, { language: language || 'auto' }).value;
}

function getLanguage(filename) {
  if (filename.endsWith('.java')) return 'java';
  if (filename.endsWith('.xml')) return 'xml';
  if (filename.endsWith('.js')) return 'javascript';
  return 'java';
}

function copyCode(code, filename) {
  if (!code) {
    ElMessage.warning("暂无代码可复制！");
    return;
  }
  const textArea = document.createElement('textarea');
  textArea.value = code;
  document.body.appendChild(textArea);
  textArea.select();
  document.execCommand('copy');
  document.body.removeChild(textArea);
  ElMessage.success(`已成功复制${filename}代码！`);
}
</script>

<style scoped>
.code-header {
  position: absolute;
  right: 10px;
  margin-bottom: 5px;
  text-align: right;
}

.code-content {
  height: 500px;
  overflow: auto;
  border-radius: 4px;
}

pre {
  margin: 0;
  white-space: pre;
  word-break: break-all;
}

.query-params {
  margin-bottom: 10px;
}
</style>