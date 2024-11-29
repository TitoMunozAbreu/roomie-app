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
  Modal,
  Button,
  Tag,
  Row,
  Col,
  Select,
  Spin,
} from "antd";
import {
  CheckCircleOutlined,
  ClockCircleOutlined,
  ExclamationCircleOutlined,
  EditOutlined,
  PlusOutlined,
  CloseCircleOutlined,
  StopOutlined,
  DeleteOutlined,
  ExclamationCircleFilled,
} from "@ant-design/icons";
import { getHouseholds } from "../../store/actions/household-actions";
import { householdActions } from "../../store/reducers/household-slice";
import UserModal from "../../components/Modal/user-modal";
import { uiActions } from "../../store/reducers/ui-slice";
import Notification from "../../components/Notification/Notification";
import { deleteTask, updateStatus } from "../../store/actions/task-actions";

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
  const [modal, contextHolder] = Modal.useModal();

  const isLoading = useSelector((state) => state.ui.profile.isLoading);
  const households = useSelector((state) => state.households.households);
  const selectedHousehold = useSelector(
    (state) => state.households.selectedHousehold
  );
  const errorMessage = useSelector((state) => state.ui.errorMessage);
  const notification = useSelector((state) => state.ui.notification);

  const [selectedFilter, setSelectedFilter] = useState("All");
  const [isEditingStatus, setIsEditingStatus] = useState(false);
  const [editTaskStatus, setEditTaskStatus] = useState(null);
  const [assignedMemberFilter, setAssignedMemberFilter] = useState(null);

  useEffect(() => {
    if (households === null || households.length === 0) {
      dispatch(uiActions.showLoading());
      dispatch(getHouseholds());
    }
  }, [errorMessage, selectedHousehold, dispatch]);

  const getMembers = () => {
    const members = new Set();
    households?.forEach((household) => {
      household?.tasks?.forEach((task) => {
        if (task.assignedTo) {
          members.add(task.assignedTo);
        }
      });
    });
    return Array.from(members);
  };

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
    dispatch(updateStatus(selectedHousehold.id, taskId, newStatus));
    setIsEditingStatus(false);
    setEditTaskStatus(null);
  };

  const onClickStatus = (taskId) => {
    setEditTaskStatus(taskId);
    setIsEditingStatus(true);
  };

  const onClickDeleteTask = (taskId) => {
    dispatch(deleteTask(selectedHousehold.id, taskId));
  };

  const showLoading = (
    <Spin
      size="large"
      style={{ display: "flex", justifyContent: "center", marginTop: "10%" }}
    />
  );

  const filteredTasks =
    households
      ?.find((household) => household?.id === selectedHousehold?.id)
      ?.tasks?.filter((task) => {
        return (
          (selectedFilter === "All" || task.status === selectedFilter) &&
          (assignedMemberFilter
            ? task.assignedTo === assignedMemberFilter
            : true)
        );
      }) || [];

  const showDeleteConfirm = (taskId) => {
    modal.confirm({
      title: "Delete Task?",
      icon: <ExclamationCircleFilled />,
      content: "This can't be undone",
      okText: "Delete",
      okType: "danger",
      cancelText: "Cancel",
      onOk() {
        onClickDeleteTask(taskId);
      },
    });
  };
  return (
    <>
      <h2 style={{ textAlign: "start" }}>Households</h2>
      {isLoading && showLoading}
      {notification?.type && <Notification />}
      {errorMessage && <span>{errorMessage}</span>}
      {households?.length > 0 && (
        <>
          {contextHolder}
          <div>
            <Row gutter={16}>
              <Col span={6}>
                <Menu
                  mode="inline"
                  defaultSelectedKeys={[households[0]?.id]}
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

                  {/* Filtro de miembros */}
                  <Select
                    style={{ width: "200px", marginBottom: "16px" }}
                    placeholder="Filter by member"
                    onChange={(value) => setAssignedMemberFilter(value)}
                    value={assignedMemberFilter}
                  >
                    <Select.Option value={null}>All Members</Select.Option>
                    {getMembers().map((member) => (
                      <Select.Option key={member} value={member}>
                        {member}
                      </Select.Option>
                    ))}
                  </Select>

                  <List
                    itemLayout="horizontal"
                    dataSource={filteredTasks}
                    renderItem={(task) => (
                      <List.Item
                        extra={
                          <>
                            <Button
                              variant="outlined"
                              icon={<DeleteOutlined />}
                              danger
                              style={{ marginRight: "1%" }}
                              onClick={() => showDeleteConfirm(task.id)}
                            ></Button>
                            <Button
                              variant="outlined"
                              icon={<EditOutlined />}
                              onClick={() => handleEditTask(task)}
                            ></Button>
                          </>
                        }
                      >
                        <List.Item.Meta
                          title={task.title}
                          description={
                            <>
                              <Row gutter={16}>
                                <Col span={8} style={{ paddingBottom: 10 }}>
                                  <span>{task.description}</span>
                                </Col>
                                <Col span={3}>
                                  <span>assigned to</span>
                                </Col>
                                <Col>
                                  <span>
                                    <b>{task.assignedTo}</b>
                                  </span>
                                </Col>
                                <Col span={9}>
                                  <span style={{ marginLeft: "50%" }}>
                                    <b>{task.estimatedDuration} min</b>
                                  </span>
                                </Col>
                              </Row>
                              <Row gutter={16}>
                                <Col span={8}>
                                  <div
                                    onClick={() => onClickStatus(task.id)}
                                    style={{ cursor: "pointer" }}
                                  >
                                    {renderTaskStateBadge(
                                      task.status,
                                      editTaskStatus === task.id, // Comparar ID para editar solo esa tarea
                                      (newStatus) =>
                                        handleEditStatus(task.id, newStatus) // Pasar la función para actualizar
                                    )}
                                  </div>
                                </Col>
                                <Col span={3}>
                                  <span>Due</span>
                                </Col>
                                <Col>
                                  <span>
                                    <b>{task.dueDate}</b>
                                  </span>
                                </Col>
                              </Row>
                            </>
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
        </>
      )}
    </>
  );
};

export default Dashboard;
