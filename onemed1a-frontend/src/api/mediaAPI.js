import apiClient from "./apiClient.js";


export async function getUserMediaByUserId(userId) {
    const response = await apiClient.get(`/api/v1/usermedia/user/${userId}`, {
        params: { page: 0, size: 10, sort: 'updatedAt,desc'} // leave out status/type
    });
    return response.data;
}

export async function getStatus(userId, mediaId) {
    const response = await apiClient.get(`/api/v1/usermedia/${userId}/${mediaId}`);
    return response.data;
}

