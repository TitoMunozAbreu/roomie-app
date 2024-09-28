import "./App.css";
import { ReactKeycloakProvider } from "@react-keycloak/web";
import keycloak from "./keycloak.js";
import AppLayout from "./components/Layout/Layout.jsx";
import Home from "./pages/Home/Home.jsx";

function App() {
  return (
    <ReactKeycloakProvider authClient={keycloak}>
      <AppLayout>
        <Home />
      </AppLayout>
    </ReactKeycloakProvider>
  );
}

export default App;
