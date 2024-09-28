import "./Header.css";
import logo from "../../../assets/images/logo.jpg";

import { Layout, Menu } from "antd";
import { MENU, MENU_AUTH } from "./menu-items";
import { useKeycloak } from "@react-keycloak/web";

const { Header } = Layout;

export default function AppHeader() {  
  const {keycloak } = useKeycloak();

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
