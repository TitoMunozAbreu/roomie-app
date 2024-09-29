import { Layout, theme } from "antd";

import "./Layout.css"

import AppHeader from "./Header/Header.jsx";
import AppFooter from "./Footer/Footer.jsx";
import { Outlet } from "react-router-dom";

const { Content } = Layout;

export default function AppLayout({ children }) {
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  return (
    <Layout id="app-layout">
      <AppHeader />
      <Content style={{ padding: "48px 48px" }}>
        <div id="content-background">
          <Outlet />
        </div>
      </Content>
      <AppFooter />
    </Layout>
  );
}
