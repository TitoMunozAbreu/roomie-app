import instance from "../Api/instance";
import { ENDPOINTS } from "./endpoints";

const getUser = async () => {
  return await instance.get(`http://localhost:8085/${ENDPOINTS.USER}`)
};

export const userService = { getUser };
