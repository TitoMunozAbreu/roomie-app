// Dashboard.jsx
import React, { useState } from "react";
import {
  Layout,
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
} from "@ant-design/icons";

const { Header, Content, Sider } = Layout;
const { TabPane } = Tabs;

const households = [
  {
    id: "household1",
    name: "Casa de la Playa",
    tasks: [
      { id: "task1", name: "Limpiar la nevera", state: "Pendiente" },
      { id: "task2", name: "Lavar los platos", state: "Completada" },
      { id: "task3", name: "Regar las plantas", state: "En Progreso" },
    ],
  },
  {
    id: "household2",
    name: "Apartamento Centro",
    tasks: [
      { id: "task4", name: "Aspirar la sala", state: "Pendiente" },
      { id: "task5", name: "Limpiar el baño", state: "Completada" },
    ],
  },
];

const taskStates = ["Todas", "Pendiente", "En Progreso", "Completada"];

const Dashboard = () => {
  const [selectedHousehold, setSelectedHousehold] = useState(households[0].id);
  const [selectedFilter, setSelectedFilter] = useState("Todas");

  const filteredTasks = households
    .find((household) => household.id === selectedHousehold)
    .tasks.filter(
      (task) => selectedFilter === "Todas" || task.state === selectedFilter
    );

  const renderTaskStateBadge = (state) => {
    switch (state) {
      case "Pendiente":
        return (
          <Tag icon={<ExclamationCircleOutlined />} color="orange">
            Pendiente
          </Tag>
        );
      case "En Progreso":
        return (
          <Tag icon={<ClockCircleOutlined />} color="blue">
            En Progreso
          </Tag>
        );
      case "Completada":
        return (
          <Tag icon={<CheckCircleOutlined />} color="green">
            Completada
          </Tag>
        );
      default:
        return <Tag color="default">{state}</Tag>;
    }
  };

  return (
    <>
      <h2 style={{ textAlign: "start" }}>Households</h2>

      <Row gutter={16}>
        <Col span={6}>
          <Menu
            mode="inline"
            defaultSelectedKeys={[households[0].id]}
            style={{ height: "100%", borderRight: 0 }}
            onSelect={({ key }) => setSelectedHousehold(key)}
          >
            {households.map((household) => (
              <Menu.Item key={household.id}>{household.name}</Menu.Item>
            ))}
          </Menu>
          <Button variant="outlined" icon={<EditOutlined />}></Button>
        </Col>
        <Col span={18}>
          <Card title="Resumen de Tareas">
            <Tabs
              defaultActiveKey="Todas"
              onChange={(key) => setSelectedFilter(key)}
            >
              {taskStates.map((state) => (
                <TabPane tab={state} key={state} />
              ))}
            </Tabs>
            <List
              itemLayout="horizontal"
              dataSource={filteredTasks}
              renderItem={(task) => (
                <List.Item>
                  <List.Item.Meta
                    title={task.name}
                    description={renderTaskStateBadge(task.state)}
                  />
                </List.Item>
              )}
            />
          </Card>
        </Col>
      </Row>
    </>
  );
};

export default Dashboard;
