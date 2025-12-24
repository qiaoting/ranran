import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import {resolve} from 'path'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'
import AutoImport from 'unplugin-auto-import/vite'
import VueSetupExtend from 'unplugin-vue-setup-extend-plus/vite' 

export default defineConfig({
    base: '/',
    plugins: [
        vue(),
        VueSetupExtend(),
        AutoImport({
            imports: [
                'vue',
                'vue-router', 
                'pinia' 
            ],
            dts: './auto-imports.d.ts', 
            resolvers: [ElementPlusResolver()],
            exclude: [/node_modules/, /\.git/],
            }),
    ],
    resolve: {
        alias: {
            '@': resolve(__dirname, 'src')
        }
    },
    server: {
        port: 80,
        open: true,
        host: true,
        proxy: {
            '/backend': {
                target: 'http://localhost:8080/',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/backend/, '')
            }
        }
    }
})