import { Layout, theme } from "antd";

import styles from "../Layout/Layout.module.css";

import AppHeader from "./Header/Header.jsx";
import AppFooter from "./Footer/Footer.jsx";
import { Outlet } from "react-router-dom";

const { Content } = Layout;

export default function AppLayout({ children }) {
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  return (
    <Layout className={styles.appLayout}>
      <AppHeader />
      <Content style={{ padding: "48px 48px" }}>
        <div className={styles.contentBackground}>
          <Outlet />
        </div>
      </Content>
      <AppFooter />
    </Layout>
  );
}
