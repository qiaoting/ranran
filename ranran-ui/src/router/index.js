import {createRouter, createWebHistory} from "vue-router";
import Layout from "@/layout/index.vue";
import ParentView from "@/components/ParentView/index.vue";
import {useUserStore} from "@/store/user";
import {useMenuStore} from "@/store/menu";
import {getToken} from "@/utils/auth";
import NProgress from 'nprogress'
import {getRoutesTree} from "@/api/system/menu";
import {ElMessage} from 'element-plus'

NProgress.configure({
  color: '#4fc08d',
  showSpinner: false,
  minimum: 0.1,
  speed: 500,
  trickle: true,
  trickleSpeed: 200
});

const modules = import.meta.glob("@/views/**/*.vue", {eager: true});

function loadView(view) {
    for (const path in modules) {
        const match = path.match(/views\/(.*)\.vue$/);
        if (match && match[1] === view) {
            const module = modules[path];
            if (module.default) {
                return () => Promise.resolve(module.default);
            }
            return module; 
        }
    }
    return () => import("@/views/public/404.vue");
};

export const constantRoutes = [
    {
        path: "/login",
        name: "Login",
        component: () => import("@/views/Login.vue"),
    },
    {
        path: "",
        name: "",
        redirect: "/index"
    },
    {
        path: "/index",
        name: "IndexLayout",
        component: Layout,
        show: true,
        meta: {
            title: "首页",
            icon: "House"
        },
        children: [
            {
                path: "",
                name: "Index",
                component: () => import("@/views/Home.vue"),
            }
        ]
    },
    {
        path: "/myself",
        component: Layout,
        name: "Myself",
        children: [
            {
                path: "profile",
                name: "Profile",
                component: () => import("@/views/system/profile/index.vue"),
                meta: {
                    title: "个人中心",
                    icon: "UserFilled"
                }
            },
        ],
    },
    {
        path: "/system/role-user",
        component: Layout,
        children: [
            {
                path: "index/:roleId",
                name: "RoleUserList",
                component: () => import("@/views/system/role/roleUserList.vue"),
                meta: {
                    title: "角色分配用户",
                    activeMenu: "/system/role"
                }
            },
        ],
    },
    {
        path: "/:pathMatch(.*)",
        component: () => import("@/views/public/404.vue")
    }
];

function getRouteComponent(route) {
    if (route.component === 'Layout') {
        route.component = Layout
    } else if (route.component === 'ParentView') {
        route.component = ParentView
    } else {
        route.component = loadView(route.component)
    }
};

function fillView(routes) {
    if (!Array.isArray(routes)) return [];
    return routes
        .map((route) => {
            if (!route.path || !route.name) return null;
            getRouteComponent(route);
            if (route.children && Array.isArray(route.children)) {
                const processedChildren = fillView(route.children).filter(Boolean);
                if (processedChildren.length === 0) {
                    delete route.children;
                } else {
                    route.children = processedChildren;
                }
            } else {
                delete route.children;
            }
            if (route.redirect === undefined || route.redirect === null) {
                delete route.redirect;
            }
            return route;
        })
        .filter(Boolean);
}

const router = createRouter({
    history: createWebHistory(),
    routes: constantRoutes,
});


router.beforeEach((to, from, next) => {

    NProgress.start()
    if (getToken()) {
        if (to.path === "/login") {
            next({path: "/index"});
        } else {
            if (permitRequest(to.path)) {
                next()
            } else {
                if (useUserStore().info === null) {
                    useUserStore().fetchUserInfo().then(() => {
                        getRoutesTree({}).then(res => {
                            const dynamicRoutes = fillView(res.data || []);
                            dynamicRoutes.forEach((route) => {
                                router.addRoute(route);
                            });
                            useMenuStore().setMenuList([...constantRoutes, ...dynamicRoutes])
                            next({...to, replace: true}); 
                        }).catch(err => {
                            useUserStore().logout().then(() => {
                                ElMessage.error(err)
                                next({ path: '/index' })
                            })
                        })
                    }).catch(err => {
                        useUserStore().logout().then(() => {
                            ElMessage.error(err)
                            next({ path: '/index' })
                        })
                    })
                } else {
                    next()
                }
            }
        } 
    } else {
        if (permitRequest(to.path)) {
            next();
        } else {
            next(`/login?redirect=${to.fullPath}`);
        }
    }
});
const whiteList = ["/login"];
function permitRequest(path) {
    return whiteList.some(pattern => isMatch(pattern, path))
}
export function isMatch(pattern, path) {
  const regexPattern = pattern.replace(/\//g, '\\/').replace(/\*\*/g, '.*').replace(/\*/g, '[^\\/]*')
  const regex = new RegExp(`^${regexPattern}$`)
  return regex.test(path)
}
router.afterEach(() => {
  NProgress.done()
});
export default router;