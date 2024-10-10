import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://localhost:9090",
  realm: "roomie_app",
  clientId: "roomie_app_web",
});

export default keycloak;
