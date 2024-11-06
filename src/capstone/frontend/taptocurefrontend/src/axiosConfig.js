import axios from 'axios';
import { store } from './store';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8091',
});

axiosInstance.interceptors.request.use(
    (config) => {
        const state = store.getState();
        const token = state.auth.token;
        console.log("Token in Axios interceptor:", token);
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default axiosInstance;
