import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import {resolve} from 'path'
import Components from 'unplugin-vue-components/vite' 
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'
import AutoImport from 'unplugin-auto-import/vite'

export default defineConfig({
    plugins: [
        vue(),
        AutoImport({resolvers: [ElementPlusResolver()]}),
        Components({resolvers: [ElementPlusResolver({importStyle: 'sass'})]})
    ],
    resolve: {
        alias: {
            '@': resolve(__dirname, 'src')
        }
    },
    server: {
        port: 3000,
        open: true,
        proxy: {
            '/backend': {
                target: 'http://localhost:8080/',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/backend/, '')
            }
        }
    }
})