import { useKeycloak } from "@react-keycloak/web";
import { useNavigate } from "react-router-dom";

const PrivateRoute = ({ children }) => {
  const { keycloak } = useKeycloak();
  const isLoggedIn = keycloak.authenticated;
  // const navigate = useNavigate();

  // useEffect(() => {
  //   if (!isLoggedIn) {
  //     navigate("/"); // Redirige al login si no está autenticado
  //   }
  // }, [isLoggedIn, navigate]);

  return isLoggedIn ? children : null; // Renderiza los hijos solo si está autenticado
};

export default PrivateRoute;
