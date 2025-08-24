import apiClient from "./apiClient/apiClient.js";

export async function getRecommendation(mediaType, mediaName) {
  const response = await apiClient.post(
    "/openai/recommendation",
    {
      "mediaType": mediaType,
      "mediaName": mediaName,
    }
  );
  return response.data;
}
