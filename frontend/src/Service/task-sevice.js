import instance from "../Api/instance";
import { ENDPOINTS } from "./endpoints";

const createTask = async (task) => {
  const userToken = localStorage.getItem("token");
  return await instance.post(
    `${ENDPOINTS.TASK.URL}${ENDPOINTS.TASK.URI}`,
    { task },
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};

const updateTask = async (householdId, task) => {
  const userToken = localStorage.getItem("token");
  return await instance.put(
    `${ENDPOINTS.TASK.URL}${ENDPOINTS.TASK.URI}/${householdId}`,
    { task },
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};

const updateStatus = async (householdId, status) => {
  const userToken = localStorage.getItem("token");
  return await instance.patch(
    `${ENDPOINTS.TASK.URL}${ENDPOINTS.TASK.URI}/${householdId}/status?${status}`,
    {},
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};

const deleteTask = async (householdId, task) => {
  const userToken = localStorage.getItem("token");
  return await instance.delete(
    `${ENDPOINTS.TASK.URL}${ENDPOINTS.TASK.URI}/${householdId}`,
    {},
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};

export const taskService = {
  createTask,
  updateTask,
  updateStatus,
  deleteTask,
};
