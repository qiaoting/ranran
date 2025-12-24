import axios from "axios";
import router from "@/router";
import {useUserStore} from "@/store/user";
import {getToken} from "@/utils/auth";

const request = axios.create({
    baseURL: import.meta.env.VITE_APP_BASE_API || "",
    timeout: 20000,
    headers: {"Content-Type": "application/json;charset=utf-8"},
});

// 请求前置处理
request.interceptors.request.use(
    (config) => {
        if (getToken()) {
            config.headers.Authorization = 'Bearer ' + getToken();
        }
        if (
            ["get", "delete"].includes(config.method?.toLowerCase()) &&
            config.data
        ) {
            config.params = config.data;
            delete config.data;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 后端返回统一处理
request.interceptors.response.use(
    (res) => {
        const userStore = useUserStore();
        const code = res.data.code || 200;
        const msg = res.data.msg || errorCode[code] || errorCode["default"];
        if (
            res.request.responseType === "blob" ||
            res.request.responseType === "arraybuffer"
        ) {
            return res.data;
        }
        if (code === 401) {
            userStore.logout().then(() => {
                router.push("/login");
            })
        } else if (code === 500) {
            ElMessage({message: msg, type: "error"});
            return Promise.reject(new Error(msg));
        } else if (code === 601) {
            ElMessage({message: msg, type: "warning"});
            return Promise.reject(new Error(msg));
        } else if (code !== 200) {
            ElNotification.error({title: msg});
            return Promise.reject("error");
        } else {
            return Promise.resolve(res.data);
        }
    },
    (error) => {
        let {message} = error;
        if (message == "Network Error") {
            message = "后端接口连接异常";
        } else if (message.includes("timeout")) {
            message = "系统接口请求超时";
        } else if (message.includes("Request failed with status code")) {
            message = "系统接口" + message.substr(message.length - 3) + "异常";
        }
        ElMessage({message: message, type: "error", duration: 5 * 1000});
        return Promise.reject(error);
    }
);

const errorCode = {
    401: "认证失败，无法访问系统资源",
    403: "当前操作没有权限",
    404: "访问资源不存在",
    default: "系统未知错误，请反馈给管理员",
};

export default request;
