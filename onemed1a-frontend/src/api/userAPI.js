import apiClient from "./apiClient.js";


    export async function checkAccountDetails(userDetails) {
        const response = await apiClient.post("/api/v1/accountcheck", userDetails);
        return response.data;
    }

    export async function createAccount(userDetails) {
const response = await  apiClient.post("/api/v1/users", userDetails);
return response.data
    }