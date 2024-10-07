import axios from "axios";
import { useKeycloak } from "@react-keycloak/web";

const instance = axios.create({
  baseURL: "http://localhost",
});

const { keycloak } = useKeycloak();

keycloak.authenticated
  ? (instance.defaults.headers.common[
      Authorization
    ] = `Bearer ${keycloak.token}`)
  : null;

export default instance;
