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
import { userService } from "../../Service/user-service";

export default function Profile() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchUser() {
      const userId = localStorage.getItem("userAuth");
      const userFound = await userService.getUser(userId)
      setUser(userFound.data);
      setLoading(false);
    }
    fetchUser();
  }, []);

  if (loading) {
    return (
      <Spin
        size="large"
        style={{ display: "flex", justifyContent: "center", marginTop: "10%" }}
      />
    );
  }

  const hanldeEditPreferences = (preferences) => {
    
  }

  return (
    <>
      <div>
        <Row gutter={16}>
          <Col xs={24} md={12}>
            {/* User info */}
            <div style={{ padding: "20px" }}>
              <Descriptions
                className="userData"
                bordered
                column={{ xs: 1, sm: 1, md: 1, lg: 2, xl: 2, xxl: 2 }}
                title="User info"
                extra={
                  <Button variant="outlined" icon={<EditOutlined />}></Button>
                }
              >
                <Descriptions.Item label="Name">
                  {user.firstName}
                </Descriptions.Item>
                <Descriptions.Item label="Lastname">
                  {user.lastName}
                </Descriptions.Item>
                <Descriptions.Item label="Email">
                  {user.email}
                </Descriptions.Item>
              </Descriptions>
            </div>
          </Col>
        </Row>
        <Divider />
        <Row gutter={16}>
          <Col xs={24} md={12}>
            {/* Preferences */}
            <div style={{ padding: "20px" }}>
              <Descriptions
                className="preferences"
                bordered
                column={{ xs: 1, sm: 1, md: 1, lg: 2, xl: 2, xxl: 2 }}
                title="Preferences"
                extra={
                  <Button
                    variant="outlined"
                    icon={
                      user.taskPreferences !== null ? (
                        <EditOutlined />
                      ) : (
                        <PlusOutlined />
                      )
                    }
                    onClick={hanldeEditPreferences}
                  ></Button>
                }
              >
                {user.taskPreferences ? (
                  user.taskPreferences.map((preference, index) => (
                    <Descriptions.Item label={preference.taskname} key={index}>
                      {preference.preference}
                    </Descriptions.Item>
                  ))
                ) : (
                  <Descriptions.Item label="">
                    No preference set
                  </Descriptions.Item>
                )}
              </Descriptions>
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
                  <Button
                    variant="outlined"
                    icon={
                      user.availabilities !== null ? (
                        <EditOutlined />
                      ) : (
                        <PlusOutlined />
                      )
                    }
                  ></Button>
                }
              >
                {user.availabilities ? (
                  user.availabilities.map((availability, index) => (
                    <Descriptions.Item label={availability.day} key={index}>
                      {availability.hours[0]} - {availability.hours[1]}
                    </Descriptions.Item>
                  ))
                ) : (
                  <Descriptions.Item label="">
                    No availability set
                  </Descriptions.Item>
                )}
              </Descriptions>
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
                  user.taskHistories
                    ? user.taskHistories
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
