import apiClient from "./apiClient.js";


export async function suggest(text) {
    const response = await apiClient.get(`/media/suggest`,  {
        params: { q: text }
    });
    return response.data;
}
