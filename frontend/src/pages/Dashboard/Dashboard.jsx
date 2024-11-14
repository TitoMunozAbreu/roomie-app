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

  const [selectedFilter, setSelectedFilter] = useState("All");

  useEffect(() => {
    if (households === null || households.length === 0) {
      dispatch(getHouseholds());
    }
  }, [errorMessage, dispatch]);

  // const filteredTasks = households
  //   .find((household) => household.id === selectedHousehold)
  //   .tasks.filter(
  //     (task) => selectedFilter === "All" || task.status === selectedFilter
  //   );

  const renderTaskStateBadge = (state) => {
    switch (state) {
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
        return <Tag color="default">{state}</Tag>;
    }
  };

  const updateSelectedHousehold = (householdId) => {
    const household = households.find((h) => h.id === householdId);
    dispatch(householdActions.updateSelecteHousehold(household));
    setSelectedFilter("All");
  };

  const handleCreateTask = () => {
    dispatch(uiActions.showModal());
    dispatch(householdActions.setIsTaskEdit());
    dispatch(
      uiActions.modalData({
        title: "Create task",
        type: "formTask",
      })
    );
  };

  const handleEditTask = (task) => {
    dispatch(householdActions.setTask(task));
    dispatch(householdActions.setIsTaskEdit());
    dispatch(uiActions.showModal());
    dispatch(
      uiActions.modalData({
        title: "Edit task",
        type: "formTask",
      })
    );
  };

  return (
    <>
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
                        description={renderTaskStateBadge(task.status)}
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
