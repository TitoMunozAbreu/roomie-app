import instance from "../Api/instance.js";
import { ENDPOINTS } from "./endpoints.js";

const createHousehold = async (householdName, userId, email) => {
  const userToken = localStorage.getItem("token");
  return await instance.post(
    `${ENDPOINTS.HOUSEHOLD.URL}${ENDPOINTS.HOUSEHOLD.URI}`,
    { householdName: householdName, userId: userId, email: email },
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

const updateHouseholdName = async (householdId, householdName) => {
  const userToken = localStorage.getItem("token");
  return await instance.put(
    `${ENDPOINTS.HOUSEHOLD.URL}${ENDPOINTS.HOUSEHOLD.URI}/${householdId}`,
    {},
    {
      headers: { Authorization: `Bearer ${userToken}` },
      params: { name: householdName },
    }
  );
};

const deleteHouseholdMember = async (householdId, memberEmail) => {
  const userToken = localStorage.getItem("token");
  return await instance.delete(
    `${ENDPOINTS.HOUSEHOLD.URL}${ENDPOINTS.HOUSEHOLD.URI}/${householdId}/members/${memberEmail}`,
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};

const updateHouseholdMembers = async (householdId, memberEmails) => {
  const userToken = localStorage.getItem("token");
  return await instance.put(
    `${ENDPOINTS.HOUSEHOLD.URL}${ENDPOINTS.HOUSEHOLD.URI}/${householdId}/members`,
    {},
    {
      headers: { Authorization: `Bearer ${userToken}` },
      params: { memberEmails: memberEmails },
    }
  );
};

const deleteHousehold = async (householdId) => {
  const userToken = localStorage.getItem("token");
  return await instance.delete(
    `${ENDPOINTS.HOUSEHOLD.URL}${ENDPOINTS.HOUSEHOLD.URI}/${householdId}`,
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};

export const householdService = {
  createHousehold,
  getHouseholds,
  updateHouseholdName,
  deleteHouseholdMember,
  updateHouseholdMembers,
  deleteHousehold,
};
