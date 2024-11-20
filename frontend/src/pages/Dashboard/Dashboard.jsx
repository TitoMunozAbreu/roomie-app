// Dashboard.jsx
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  Menu,
  Card,
  List,
  Tabs,
  Badge,
  Divider,
  Button,
  Tag,
  Row,
  Col,
  Select,
} from "antd";
import {
  CheckCircleOutlined,
  ClockCircleOutlined,
  ExclamationCircleOutlined,
  EditOutlined,
  PlusOutlined,
  CloseCircleOutlined,
  StopOutlined,
} from "@ant-design/icons";
import { getHouseholds } from "../../store/actions/household-actions";
import { householdActions } from "../../store/reducers/household-slice";
import UserModal from "../../components/Modal/user-modal";
import { uiActions } from "../../store/reducers/ui-slice";
import Notification from "../../components/Notification/Notification";
import { updateStatus } from "../../store/actions/task-actions";

const { TabPane } = Tabs;

const taskStates = [
  "All",
  "Pending",
  "Progress",
  "Overdue",
  "Completed",
  "Cancelled",
];

const Dashboard = () => {
  const dispatch = useDispatch();
  const households = useSelector((state) => state.households.households);
  const selectedHousehold = useSelector(
    (state) => state.households.selectedHousehold
  );
  const errorMessage = useSelector((state) => state.ui.errorMessage);
  const notification = useSelector((state) => state.ui.notification);

  const [selectedFilter, setSelectedFilter] = useState("All");
  const [isEditingStatus, setIsEditingStatus] = useState(false);
  const [editTaskStatus, setEditTaskStatus] = useState(null);

  useEffect(() => {
    if (households === null || households.length === 0) {
      dispatch(getHouseholds());
    }
  }, [errorMessage, dispatch]);

  const renderTaskStateBadge = (status, isEditing, onEditStatus) => {
    const statusOptions = [
      {
        label: (
          <Tag icon={<ExclamationCircleOutlined />} color="orange">
            Pending
          </Tag>
        ),
        value: "Pending",
      },
      {
        label: (
          <Tag icon={<ClockCircleOutlined />} color="blue">
            In Progress
          </Tag>
        ),
        value: "Progress",
      },
      {
        label: (
          <Tag icon={<CloseCircleOutlined />} color="red">
            Overdue
          </Tag>
        ),
        value: "Overdue",
      },
      {
        label: (
          <Tag icon={<CheckCircleOutlined />} color="green">
            Completed
          </Tag>
        ),
        value: "Completed",
      },
      {
        label: (
          <Tag icon={<StopOutlined />} color="gray">
            Cancelled
          </Tag>
        ),
        value: "Cancelled",
      },
    ];

    if (isEditing) {
      return (
        <Select
          defaultValue={status}
          options={statusOptions}
          tagRender={statusOptions}
          onChange={onEditStatus}
          style={{ height: 25 }}
          optionLabelProp="label"
          onBlur={() => {
            setIsEditingStatus(false);
            setEditTaskStatus(null);
          }}
        />
      );
    }

    switch (status) {
      case "Pending":
        return (
          <Tag icon={<ExclamationCircleOutlined />} color="orange">
            Pending
          </Tag>
        );
      case "Progress":
        return (
          <Tag icon={<ClockCircleOutlined />} color="blue">
            In Progress
          </Tag>
        );
      case "Overdue":
        return (
          <Tag icon={<CloseCircleOutlined />} color="red">
            Overdue
          </Tag>
        );
      case "Completed":
        return (
          <Tag icon={<CheckCircleOutlined />} color="green">
            Completed
          </Tag>
        );
      case "Cancelled":
        return (
          <Tag icon={<StopOutlined />} color="gray">
            Cancelled
          </Tag>
        );
      case "All":
        return <Tag color="default">All</Tag>;
      default:
        return <Tag color="default">{status}</Tag>;
    }
  };

  const updateSelectedHousehold = (householdId) => {
    const household = households.find((h) => h.id === householdId);
    dispatch(householdActions.updateSelecteHousehold(household));
    setSelectedFilter("All");
  };

  const handleCreateTask = () => {
    dispatch(uiActions.showModal());
    dispatch(householdActions.setIsTaskEdit(false));
    dispatch(
      uiActions.modalData({
        title: "Create task",
        type: "formTask",
      })
    );
  };

  const handleEditTask = (task) => {
    dispatch(householdActions.setTask(task));
    dispatch(householdActions.setIsTaskEdit(true));
    dispatch(uiActions.showModal());
    dispatch(
      uiActions.modalData({
        title: "Edit task",
        type: "formTask",
      })
    );
  };

  const handleEditStatus = (taskId, newStatus) => {
    dispatch(updateStatus(selectedHousehold.id, taskId, newStatus))
    setIsEditingStatus(false);
    setEditTaskStatus(null);
  };

  const hanldeTaskStatus = (taskId) => {
    setEditTaskStatus(taskId);
    setIsEditingStatus(true);
  };

  return (
    <>
      {notification?.type && <Notification />}
      <h2 style={{ textAlign: "start" }}>Households</h2>
      {households && (
        <div>
          <Row gutter={16}>
            <Col span={6}>
              <Menu
                mode="inline"
                defaultSelectedKeys={[households[0].id]}
                style={{ height: "100%", borderRight: 0 }}
                onSelect={({ key }) => updateSelectedHousehold(key)}
              >
                {households.map((household) => (
                  <Menu.Item key={household.id}>
                    {household.householdName}
                  </Menu.Item>
                ))}
              </Menu>
            </Col>
            <Col span={18}>
              <Card
                title="Task Summary"
                extra={
                  <Button
                    variant="outlined"
                    icon={<PlusOutlined />}
                    onClick={handleCreateTask}
                  ></Button>
                }
              >
                <Tabs
                  defaultActiveKey="All"
                  onChange={(key) => setSelectedFilter(key)}
                >
                  {taskStates.map((state) => (
                    <TabPane tab={state} key={state} />
                  ))}
                </Tabs>
                <List
                  itemLayout="horizontal"
                  dataSource={
                    households
                      .find(
                        (household) => household.id === selectedHousehold.id
                      )
                      ?.tasks?.filter(
                        (task) =>
                          selectedFilter === "All" ||
                          task.status === selectedFilter
                      ) || []
                  }
                  renderItem={(task) => (
                    <List.Item
                      extra={
                        <Button
                          variant="outlined"
                          icon={<EditOutlined />}
                          onClick={() => handleEditTask(task)}
                        ></Button>
                      }
                    >
                      <List.Item.Meta
                        title={task.title}
                        description={
                          <div
                            onClick={() => hanldeTaskStatus(task.id)}
                            style={{ cursor: "pointer" }}
                          >
                            {renderTaskStateBadge(
                              task.status,
                              editTaskStatus === task.id, // Comparar ID para editar solo esa tarea
                              (newStatus) =>
                                handleEditStatus(task.id, newStatus) // Pasar la función para actualizar
                            )}
                          </div>
                        }
                      />
                    </List.Item>
                  )}
                />
              </Card>
            </Col>
          </Row>
          <UserModal />
        </div>
      )}
    </>
  );
};

export default Dashboard;
