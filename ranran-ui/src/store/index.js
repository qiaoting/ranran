import {createPinia, defineStore} from "pinia";

// 侧边栏Store
export const useSidebarStore = defineStore("sidebar", {
    state: () => ({
        isCollapse: false,
    }),
    actions: {
        toggleCollapse() {
            this.isCollapse = !this.isCollapse;
        },
    },
});

const store = createPinia();
export default store;
