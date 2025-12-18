import {defineStore} from "pinia";
import {getRoutesTree} from "@/api/system/menu";
import {constantRoutes} from "@/router";
// 菜单store
export const useMenuStore = defineStore("menu", {
    state: () => ({
        menuList: [],
    }),
    actions: {
        loadMenuList() {
            return new Promise((resolve, reject) => {
                getRoutesTree({}).then(res => {
                    let menuData = res.data || [];
                    const layoutRoute = constantRoutes.find(
                        (route) => route.name === "Layout"
                    );
                    if (layoutRoute?.children && layoutRoute.children.length > 0) {
                        layoutRoute.children.forEach((targetRoute, index) => {
                            // 仅处理meta存在且show为true的路由
                            if (targetRoute.meta?.show !== true) return;
                            // 检查菜单是否已存在（通过menuCode匹配路由name）
                            const menuExists = menuData.some(
                                (menu) => menu.menuCode === targetRoute.name
                            );
                            if (!menuExists) {
                                // 构建菜单数据（menuId按索引递增，避免重复）
                                const newMenu = {
                                    menuId: 9999 + index, // 索引递增确保唯一ID
                                    parentId: 0,
                                    menuName: targetRoute.meta.title || targetRoute.name,
                                    menuCode: targetRoute.name,
                                    menuType: 2,
                                    path: targetRoute.path,
                                    viewPath: targetRoute.component?.name || targetRoute.name,
                                    icon: targetRoute.meta.icon || "",
                                    sort: index, // 按路由定义顺序排序
                                    status: "1",
                                    children: [],
                                };
                                menuData.unshift(newMenu);
                            }
                        });
                    }
                    this.menuList = menuData;
                    resolve()
                }).catch(error => {
                    this.menuList = [];
                    reject(error)
                })
            })
        },
    },
});
