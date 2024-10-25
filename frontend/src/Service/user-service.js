import instance from "../Api/instance.js";
import { ENDPOINTS } from "./endpoints";

const getUser = async (userId) => {
  const userToken = localStorage.getItem("token");
  return await instance.get(
    `${ENDPOINTS.USERS.URL}${ENDPOINTS.USERS.URI}/${userId}`,
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};

const putPreferences = async (userId, taskPreferences) => {
  const userToken = localStorage.getItem("token");
  return await instance.put(
    `${ENDPOINTS.USERS.URL}${ENDPOINTS.USERS.URI}/${userId}/preferences`,
    taskPreferences,
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};

const putAvailabilities = async (userId, availabilities) => {
  const userToken = localStorage.getItem("token");
  return await instance.put(
    `${ENDPOINTS.USERS.URL}${ENDPOINTS.USERS.URI}/${userId}/availabilities`,
    availabilities,
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};

export const userService = { getUser, putPreferences, putAvailabilities };
