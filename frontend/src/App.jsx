import "./App.css";
import React, { useEffect } from "react";
import { ReactKeycloakProvider } from "@react-keycloak/web";
import keycloakInst from "./keycloak.js";
import { RouterProvider } from "react-router-dom";
import router from "./routes.jsx";

const initOptions = { onLoad: "check-sso" };

function App() {
  const handleKeycloakTokens = (tokens) => {
    localStorage.setItem("token", tokens.token);
    localStorage.setItem("refreshToken", tokens.refreshToken);
  };

  useEffect(() => {
    const refreshTokenInterval = setInterval(() => {
      keycloakInst
        .updateToken(60)
        .then((refreshed) => {
          if (refreshed) {
            console.log("Token successfully refreshed");
            localStorage.setItem("token", keycloakInst.token);
          }
        })
        .catch(() => {
          console.error("Failed to refresh token, or the session has expired");
          keycloakInst.logout({ redirectUri: "http://localhost:5173/" });
        });
    }, 60000);

    return () => clearInterval(refreshTokenInterval);
  }, []);

  return (
    <ReactKeycloakProvider
      authClient={keycloakInst}
      initOptions={initOptions}
      onTokens={handleKeycloakTokens}
    >
      <RouterProvider router={router} />
    </ReactKeycloakProvider>
  );
}

export default App;
