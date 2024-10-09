import { useKeycloak } from "@react-keycloak/web";
import { Navigate } from "react-router-dom";
import Home from "../pages/Home/Home";

const PrivateRoute = ({ children }) => {
  const { keycloak } = useKeycloak();
  const isLoggedIn = keycloak.authenticated;

  return isLoggedIn ? children : <Navigate to="/" />;
};

export default PrivateRoute;
