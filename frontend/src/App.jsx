import "./App.css";
import { ReactKeycloakProvider } from "@react-keycloak/web";
import keycloakInst from "./keycloak.js";
import { RouterProvider } from "react-router-dom";
import router from "./routes.jsx";

const initOptions = { onLoad: "login-required" };

function App() {
  return (
    <ReactKeycloakProvider authClient={keycloakInst} initOptions={initOptions}>
      <RouterProvider router={router} />
    </ReactKeycloakProvider>
  );
}

export default App;
