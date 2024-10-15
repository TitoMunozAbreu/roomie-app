import Keycloak from "keycloak-js";

const keycloakInst = new Keycloak({
  url: "http://localhost:9090",
  realm: "roomie_app",
  clientId: "roomie_app_web",
});

export default keycloakInst;
