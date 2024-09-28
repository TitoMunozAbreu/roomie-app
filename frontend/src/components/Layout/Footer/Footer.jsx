import { Layout } from "antd";

const { Footer } = Layout;

export default function AppFooter() {
  return (
    <Footer id="app-footer" style={{ textAlign: "center" }}>
      Roomie app ©{new Date().getFullYear()} Created by Tito Muñoz
    </Footer>
  );
}
