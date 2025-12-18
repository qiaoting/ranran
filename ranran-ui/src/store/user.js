import {defineStore} from "pinia";
import {getInfo, login as loginApi, logout as logoutApi,} from "@/api/system/login";
import {deleteToken, setToken} from "@/utils/auth";

// 用户store
export const useUserStore = defineStore("user", {
    state: () => ({
        info: {},
        hasLoadedRoutes: false
    }),
    actions: {
        login(loginForm) {
            return new Promise((resolve, reject) => {
                loginApi(loginForm).then(res => {
                    setToken(res.data.token);
                    resolve()
                }).catch(error => {
                    reject(error)
                })
            })
        },
        logout() {
            return new Promise((resolve, reject) => {
                logoutApi().then(res => {
                    deleteToken();
                    this.info = {};
                    this.hasLoadedRoutes = false;
                    resolve()
                }).catch(error => {
                    reject(error)
                })
            })
        },
        fetchUserInfo() {
            return new Promise((resolve, reject) => {
                getInfo().then(res => {
                    this.info = res.data || {};
                    resolve()
                }).catch(error => {
                    reject(error)
                })
            })
        },
        setHasLoadedRoutes(status) {
            this.hasLoadedRoutes = status;
        }
    },
});
