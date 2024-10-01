import "./Header.css";
import logo from "../../../assets/images/logo.jpg";

import { Layout, Menu } from "antd";
import { MENU, MENU_AUTH } from "./menu-items";
import { useKeycloak } from "@react-keycloak/web";
import { useNavigate } from "react-router-dom";

const { Header } = Layout;

export default function AppHeader() {
  const { keycloak, initialized } = useKeycloak();
  const navigate = useNavigate();

  const handleMenuClick = (selected) => {
    switch (selected.key) {
      case "login":
        keycloak.login();
        break;
      case "logout":
        keycloak.logout();
        break;
      case "dashboard":
        navigate("/dashboard");
        break;
      case "notifications":
        navigate("/notifications");
        break;
      case "profile":
        navigate("/profile");
        break;
      default:
        navigate("/");
    }
  };

  return (
    <Header
      id="app-header"
      style={{
        position: "sticky",
        top: 0,
        zIndex: 1,
        width: "100%",
        display: "flex",
        alignItems: "center",
      }}
    >
      <img src={logo} alt="Logo" className="app-logo" />
      <Menu
        theme="dark"
        mode="horizontal"
        onClick={handleMenuClick}
        defaultSelectedKeys={["1"]}
        items={keycloak.authenticated ? MENU_AUTH : MENU}
        style={{
          flex: 1,
          minWidth: 0,
        }}
      />
    </Header>
  );
}
