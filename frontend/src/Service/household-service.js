import instance from "../Api/instance.js";
import { ENDPOINTS } from "./endpoints.js";

const createHousehold = async (userId, householdName) => {
  const userToken = localStorage.getItem("token");
  return await instance.post(
    `${ENDPOINTS.HOUSEHOLD.URL}${ENDPOINTS.HOUSEHOLD.URI}`,
    { householdName: householdName, userId: userId },
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};

const getHouseholds = async () => {
  const userToken = localStorage.getItem("token");
  return await instance.get(
    `${ENDPOINTS.HOUSEHOLD.URL}${ENDPOINTS.HOUSEHOLD.URI}`,
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};
export const householdService = { createHousehold, getHouseholds };
