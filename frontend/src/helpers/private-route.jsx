import { useKeycloak } from "@react-keycloak/web";
import { Navigate } from "react-router-dom";
import { Spin } from "antd";

const PrivateRoute = ({ children }) => {
  const { keycloak, initialized } = useKeycloak();

  if (!initialized) {
    return (
      <Spin
        size="large"
        style={{ display: "flex", justifyContent: "center", marginTop: "10%" }}
      />
    );
  }
  const isLoggedIn = keycloak.authenticated;

  return isLoggedIn ? children : <Navigate to="/" />;
};

export default PrivateRoute;
