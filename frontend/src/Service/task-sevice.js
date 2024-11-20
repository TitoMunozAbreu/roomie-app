import instance from "../Api/instance";
import { ENDPOINTS } from "./endpoints";

const createTask = async (task) => {
  const userToken = localStorage.getItem("token");
  return await instance.post(
    `${ENDPOINTS.TASK.URL}${ENDPOINTS.TASK.URI}`,
    task,
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};

const updateTask = async (task) => {
  const userToken = localStorage.getItem("token");
  return await instance.put(
    `${ENDPOINTS.TASK.URL}${ENDPOINTS.TASK.URI}/${task.taskId}`,
    task,
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};

const updateStatus = async (taskId, status) => {
  const userToken = localStorage.getItem("token");
  return await instance.patch(
    `${ENDPOINTS.TASK.URL}${ENDPOINTS.TASK.URI}/${taskId}/status?status=${status}`,
    {},
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};

const deleteTask = async (taskId) => {
  const userToken = localStorage.getItem("token");
  return await instance.delete(
    `${ENDPOINTS.TASK.URL}${ENDPOINTS.TASK.URI}/${taskId}`,
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};

export const taskService = {
  createTask,
  updateTask,
  updateStatus,
  deleteTask,
};
