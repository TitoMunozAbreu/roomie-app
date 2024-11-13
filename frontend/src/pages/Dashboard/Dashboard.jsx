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
  const selectedHousehold = useSelector((state) => state.households.selectedHousehold);
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
    dispatch(householdActions.updateSelecteHousehold(householdId));
    setSelectedFilter("All");
  }
  return (
    <>
      <h2 style={{ textAlign: "start" }}>Households</h2>

      {households && (
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
            <Card title="Task Summary">
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
                    .find((household) => household.id === selectedHousehold)
                    ?.tasks?.filter((task) =>
                      selectedFilter === "All" || task.status === selectedFilter
                    ) || []
                }
                renderItem={(task) => (
                  <List.Item>
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
      )}
    </>
  );
};

export default Dashboard;
