import {createRouter, createWebHistory} from "vue-router";
import Layout from "@/layout/index.vue";
import ParentView from "@/components/ParentView/index.vue";
import {useUserStore} from "@/store/user";
import {useMenuStore} from "@/store/menu";
import {getToken} from "@/utils/auth";
import NProgress from 'nprogress'

NProgress.configure({
  color: '#4fc08d',
  showSpinner: false,
  minimum: 0.1,
  speed: 500,
  trickle: true,
  trickleSpeed: 200
});

const modules = import.meta.glob("@/views/**/*.vue", {eager: false});

function loadView(view) {
    if (view.startsWith("/")) {
        const path = `@/views${view}.vue`;
        return modules[path] || (() => import("@/views/public/404.vue"));
    }
    for (const path in modules) {
        const match = path.match(/views\/(.*)\.vue$/);
        if (match && match[1] === view) {
            return modules[path];
        }
    }
    return () => import("@/views/public/404.vue");
};

export const constantRoutes = [
    {
        path: "/login",
        name: "Login",
        component: () => import("@/views/Login.vue"),
        meta: {show: false},
    },
    {
        path: "/",
        component: Layout,
        name: "Layout",
        redirect: "/index",
        children: [
            {
                path: "index",
                name: "Index",
                component: () => import("@/views/Home.vue"),
                meta: {
                    show: true,
                    title: "首页",
                    icon: "House",
                    affix: true,
                },
            },
        ],
    },
    {
        path: "/:pathMatch(.*)*",
        component: () => import("@/views/public/404.vue"),
        meta: {show: false}
    },
    {
        path: "/myself",
        component: Layout,
        meta: {show: true},
        name: "Myself",
        children: [
            {
                path: "profile",
                name: "Profile",
                component: () => import("@/views/system/profile/index.vue"),
                meta: {show: false, title: "个人中心", icon: "UserFilled"},
            },
        ],
    },
    {
        path: "/system/roleUserList",
        component: Layout,
        meta: {show: false},
        children: [
            {
                path: ":roleId",
                name: "RoleUserList",
                component: () => import("@/views/system/role/roleUserList.vue"),
                meta: {
                    title: "角色分配用户",
                    activeMenu: "/system/role",
                },
            },
        ],
    },
];

function getRouteComponent(viewPath) {
    const layoutMap = {Layout, ParentView};
    return layoutMap[viewPath] || loadView(viewPath);
};

function convertMenuToRoutes(menus, parentPath = "") {
    if (!Array.isArray(menus)) return [];

    return menus
        .map((menu) => {
            if (!menu.path || !menu.menuCode) return null;
            const fullPath = parentPath
                ? `${parentPath}/${menu.path}`.replace(/\/+/g, "/")
                : menu.path.startsWith("/")
                    ? menu.path
                    : `/${menu.path}`;

            const route = {
                path: fullPath,
                name: menu.menuCode,
                meta: {
                    title: menu.menuName || "未知菜单",
                    icon: menu.icon,
                    show: true,
                    menuType: menu.menuType,
                },
                component: getRouteComponent(menu.viewPath),
            };
            if (menu.children?.length) {
                route.children = convertMenuToRoutes(menu.children, fullPath).filter(
                    Boolean
                );
            }
            return route;
        })
        .filter(Boolean);
};

const router = createRouter({
    history: createWebHistory(),
    routes: constantRoutes,
});

router.beforeEach(async (to, from, next) => {
    NProgress.start()
    const whiteList = ["/login"];
    const userStore = useUserStore();
    const menuStore = useMenuStore();

    if (getToken()) {
        if (to.path === "/login") {
            next({path: "/"});
        } else if (!userStore.hasLoadedRoutes) {
            await userStore.fetchUserInfo();
            await menuStore.loadMenuList();
            const dynamicRoutes = convertMenuToRoutes(menuStore.menuList);
            dynamicRoutes.forEach((route) => {
                if (!router.hasRoute(route.name)) {
                    router.addRoute("Layout", route);
                }
            });
            userStore.setHasLoadedRoutes(true);
            next({...to, replace: true});
        } else {
            next();
        }
    } else if (whiteList.includes(to.path)) {
        next();
    } else {
        next(`/login?redirect=${to.fullPath}`);
    }
});
router.afterEach(() => {
  NProgress.done()
});
export default router;