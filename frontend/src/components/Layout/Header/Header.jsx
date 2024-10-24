import "./Header.css";
import logo from "../../../assets/images/logo.jpg";
import { useDispatch, useSelector } from "react-redux";
import { userActions } from "../../../store/reducers/user-slice";
import { Layout, Menu } from "antd";
import { useKeycloak } from "@react-keycloak/web";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  UserOutlined,
  LogoutOutlined,
  ProfileOutlined,
} from "@ant-design/icons";

const { Header } = Layout;

export default function AppHeader() {
  const { keycloak, initialized } = useKeycloak();
  const user = useSelector((state) => state.user.user);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    if (keycloak.authenticated && initialized) {
      keycloak.loadUserProfile().then((profile) => {
        dispatch(
          userActions.updatedUser({
            id: profile.id,
            firstName: profile.firstName,
            lastName: profile.lastName,
            email: profile.email,
            taskPreferences: null,
            availabilities: null,
            taskHistories: null
          })
        );
      });
    }
  }, [keycloak, initialized, dispatch]);

  const MENU = [
    {
      key: "home",
      label: "Home",
    },
    {
      key: "login",
      label: "Login",
    },
  ];

  const MENU_AUTH = [
    {
      key: "dashboard",
      label: "Dashboard",
    },
    {
      key: "notifications",
      label: "Notifications",
    },
    {
      key: "user",
      label: (
        <>
          {user.firstName} {user.lastName}
        </>
      ),
      icon: <UserOutlined />,
      children: [
        { key: "profile", label: "profile", icon: <ProfileOutlined /> },
        { key: "logout", label: "Logout", icon: <LogoutOutlined /> },
      ],
    },
  ];

  const handleMenuClick = (selected) => {
    switch (selected.key) {
      case "login":
        keycloak.login({ redirectUri: "http://localhost:5173/dashboard" });
        break;
      case "logout":
        keycloak.logout({ redirectUri: "http://localhost:5173/" });
        //dispatch(userActions.clearUser());
        localStorage.clear();
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
