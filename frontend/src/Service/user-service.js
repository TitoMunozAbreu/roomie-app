import instance from "../Api/instance.js";
import { ENDPOINTS } from "./endpoints";

const getUser = async (userId) => {
  const userToken = localStorage.getItem("token");
  return await instance.get(
    `${ENDPOINTS.USERS.URL}${ENDPOINTS.USERS.getUsers}/${userId}`,
    { headers: { Authorization: `Bearer ${userToken}` } }
  );
};

export const userService = { getUser };
