import "./App.css";
import { ReactKeycloakProvider } from "@react-keycloak/web";
import keycloak from "./keycloak.js";
import { RouterProvider } from "react-router-dom";
import router from "./routes.jsx";

function App() {
  return (
    <ReactKeycloakProvider authClient={keycloak}>
      <RouterProvider router={router} />
    </ReactKeycloakProvider>
  );
}

export default App;
