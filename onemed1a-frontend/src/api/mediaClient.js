import apiClient from "./apiClient/apiClient.js";


export async function getMediaById(id) {
    const response = await apiClient.get(`/media/${id}`);
    return response.data;
}
