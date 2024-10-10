import "./Profile.css";
import React, { useEffect, useState } from "react";
import {
  Button,
  Descriptions,
  Divider,
  List,
  Avatar,
  Row,
  Col,
  Spin,
} from "antd";
import { HomeOutlined, EditOutlined, PlusOutlined } from "@ant-design/icons";
import { useKeycloak } from "@react-keycloak/web";

export default function Profile() {
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchUser() {
      const userData = JSON.parse(localStorage.getItem("userData"));
      const response = await fetch(
        `http://localhost:8085/api/v1/users/${userData.userId}`,
        { headers: { Authorization: `Bearer ${userData.token}` } }
      );
      const respondeData = await response.json();
      setUserData(respondeData);
      setLoading(false);
    }
    fetchUser();
  }, []);

  if (loading)
    return (
      <Spin
        size="large"
        style={{ display: "flex", justifyContent: "center", marginTop: "10%" }}
      />
    );

  if (error) return <div>Error: {error}</div>;

  return (
    <>
      <div>
        <Row gutter={16}>
          <Col xs={24} md={12}>
            <Descriptions
              className="userData"
              bordered
              column={{ xs: 1, sm: 1, md: 1, lg: 3, xl: 3, xxl: 3 }}
              title="User info"
              extra={
                <Button variant="outlined" icon={<EditOutlined />}></Button>
              }
            />
            <Descriptions.Item label="Name">
              {userData.firstName}
            </Descriptions.Item>
            <Descriptions.Item label="Lastname">
              {userData.lastName}
            </Descriptions.Item>
            <Descriptions.Item label="Email">
              {userData.email}
            </Descriptions.Item>
          </Col>
        </Row>
        <Divider />
        <Row gutter={16}>
          <Col xs={24} md={12}>
            <div style={{ padding: "20px" }}>
              <Descriptions
                className="preferences"
                bordered
                column={{ xs: 1, sm: 1, md: 1, lg: 2, xl: 2, xxl: 2 }}
                title="Preferences"
                extra={
                  <>
                    <Button
                      variant="outlined"
                      icon={<PlusOutlined />}
                      style={{ marginRight: "4px" }}
                    ></Button>
                    <Button variant="outlined" icon={<EditOutlined />}></Button>
                  </>
                }
              />
              {userData.taskPreferences ? (
                userData.taskPreferences.map((preference, index) => (
                  <Descriptions.Item label={preference.taskname}>
                    {preference.preference}
                  </Descriptions.Item>
                ))
              ) : (
                <Descriptions.Item label="">none</Descriptions.Item>
              )}
            </div>
          </Col>
          <Col xs={24} md={12}>
            <div style={{ padding: "20px" }}>
              <Descriptions
                className="availability"
                bordered
                column={{ xs: 1, sm: 1, md: 1, lg: 2, xl: 2, xxl: 2 }}
                title="Availability"
                extra={
                  <>
                    <Button
                      variant="outlined"
                      icon={<PlusOutlined />}
                      style={{ marginRight: "4px" }}
                    ></Button>
                    <Button variant="outlined" icon={<EditOutlined />}></Button>
                  </>
                }
              />
              {userData.availabilities ? (
                userData.availabilities.map((availability, index) => (
                  <Descriptions.Item label={availability.day}>
                    {availability.hours}
                  </Descriptions.Item>
                ))
              ) : (
                <Descriptions.Item label="">none</Descriptions.Item>
              )}
            </div>
          </Col>
        </Row>
        <Divider />
        <Row gutter={16}>
          <Col xs={24} md={12}>
            <div style={{ padding: "20px" }}>
              <div className="taskHistory">Task history</div>
              <List
                itemLayout="horizontal"
                dataSource={
                  userData.taskHistories
                    ? userData.taskHistories
                    : [{ title: "No task registered", description: "" }]
                }
                renderItem={(item, index) => (
                  <List.Item>
                    <List.Item.Meta
                      avatar={<Avatar icon={<HomeOutlined />} />}
                      title={<a href="https://ant.design">{item.title}</a>}
                      description={item.description}
                    />
                  </List.Item>
                )}
              />
            </div>
          </Col>
        </Row>
      </div>
    </>
  );
}
