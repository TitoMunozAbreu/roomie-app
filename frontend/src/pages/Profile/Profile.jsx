import "./Profile.css";
import React, { useEffect } from "react";
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
import UserModal from "../../components/Modal/user-modal.jsx";
import { useSelector, useDispatch } from "react-redux";
import { uiActions } from "../../store/reducers/ui-slice";
import { userActions } from "../../store/reducers/user-slice";
import Notification from "../../components/Notification/Notification.jsx";

export default function Profile() {
  const dispatch = useDispatch();
  const user = useSelector((state) => state.user.user);
  const isLoading = useSelector((state) => state.ui.profile.isLoading);
  const notification = useSelector((state) => state.ui.notification);

  useEffect(() => {
    async function fetchUser() {
      // dispatch(uiActions.showLoading());
      return await userService.getUser(user.id);
    }
    fetchUser().then(function (response) {
      dispatch(userActions.updatedUser(response.data));
      // dispatch(uiActions.showLoading());
    });
  }, [dispatch]);

  const hanldeEditPreferences = () => {
    dispatch(uiActions.showModal());
    dispatch(
      uiActions.modalData({
        title:
          user.taskPreferences != null
            ? "Edit Preferences"
            : "Create Preferences",
        type: "formPreferences",
      })
    );
  };

  const hanldeEditAvailabilities = () => {
    dispatch(uiActions.showModal());
    dispatch(
      uiActions.modalData({
        title:
          user.availabilities != null
            ? "Edit Availabilities"
            : "Create Availabilities",
        type: "formAvailabilities",
      })
    );
  };

  const hanldeEdituserInfo = () => {
    dispatch(uiActions.showModal());
    dispatch(
      uiActions.modalData({
        title: "Edit User",
        type: "formUserInfo",
      })
    );
  };

  const userInfo = (
    <div style={{ padding: "20px" }}>
      <Descriptions
        className="userData"
        bordered
        column={{ xs: 1, sm: 1, md: 1, lg: 2, xl: 2, xxl: 2 }}
        title="User info"
        extra={
          <Button
            variant="outlined"
            icon={<EditOutlined />}
            onClick={hanldeEdituserInfo}
          ></Button>
        }
      >
        <Descriptions.Item label="Name">{user.firstName}</Descriptions.Item>
        <Descriptions.Item label="Lastname">{user.lastName}</Descriptions.Item>
        <Descriptions.Item label="Email">{user.email}</Descriptions.Item>
      </Descriptions>
    </div>
  );

  const preferences = (
    <div style={{ padding: "20px" }}>
      <Descriptions
        className="preferences"
        bordered
        column={{ xs: 1, sm: 1, md: 1, lg: 2, xl: 2, xxl: 2 }}
        title="Task Preferences"
        extra={
          <Button
            variant="outlined"
            icon={user.taskPreferences ? <EditOutlined /> : <PlusOutlined />}
            onClick={hanldeEditPreferences}
          ></Button>
        }
      >
        {user.taskPreferences ? (
          user.taskPreferences.map((preference, index) => (
            <Descriptions.Item label={preference.taskName} key={index}>
              {preference.preference}
            </Descriptions.Item>
          ))
        ) : (
          <Descriptions.Item label="">No preference set</Descriptions.Item>
        )}
      </Descriptions>
    </div>
  );

  const availabilities = (
    <div style={{ padding: "20px" }}>
      <Descriptions
        className="availability"
        bordered
        column={{ xs: 1, sm: 1, md: 1, lg: 2, xl: 2, xxl: 2 }}
        title="Availability"
        extra={
          <Button
            variant="outlined"
            icon={user.availabilities ? <EditOutlined /> : <PlusOutlined />}
            onClick={hanldeEditAvailabilities}
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
          <Descriptions.Item label="">No availability set</Descriptions.Item>
        )}
      </Descriptions>
    </div>
  );

  const taskHistory = (
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
  );

  const showLoading = (
    <Spin
      size="large"
      style={{ display: "flex", justifyContent: "center", marginTop: "10%" }}
    />
  );
  return (
    <>
      {isLoading && showLoading}
      {notification?.type  && <Notification />}
      <>
        <div>
          <Row gutter={16}>
            <Col xs={24} md={12}>
              {userInfo}
            </Col>
          </Row>
          <Divider />
          <Row gutter={16}>
            <Col xs={24} md={12}>
              {preferences}
            </Col>
            <Col xs={24} md={12}>
              {availabilities}
            </Col>
          </Row>
          <Divider />
          <Row gutter={16}>
            <Col xs={24} md={12}>
              {taskHistory}
            </Col>
          </Row>
          <UserModal />
        </div>
      </>
    </>
  );
}
