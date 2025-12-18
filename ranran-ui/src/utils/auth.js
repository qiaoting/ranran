import Cookies from 'js-cookie'

const TOKEN_KEY = 'Ranran-Token'

export function getToken() {
    return Cookies.get(TOKEN_KEY)
}

export function setToken(token) {
    return Cookies.set(TOKEN_KEY, token)
}

export function deleteToken() {
    return Cookies.remove(TOKEN_KEY)
}
