// 菜单store
export const useMenuStore = defineStore("menu", {
    state: () => ({
        menuList: [],
    }),
    actions: {
        setMenuList(menuData) {
            this.menuList = menuData
        }
    },
});
