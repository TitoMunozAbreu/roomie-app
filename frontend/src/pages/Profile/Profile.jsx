import "./Profile.css";
import { Button, Descriptions, Divider, List, Avatar, Row, Col } from "antd";
import { HomeOutlined, EditOutlined, PlusOutlined } from "@ant-design/icons";

const taskPreference = [
  {
    label: "Clean kitchen",
    children: "5",
  },
  {
    label: "Throwm the trash",
    children: "3",
  },
  {
    label: "Mop the floor",
    children: "1",
  },
];

const availability = [
  {
    label: "Tuesday",
    children: ["18:00", " 20:00"],
  },
  {
    label: "Friday",
    children: ["09:00", " 11:00"],
  },
  {
    label: "Sunday",
    children: ["20:00", " 22:00"],
  },
];

const taskHistory = [
  {
    title: "Do Dishes",
  },
  {
    title: "Do laundry",
  },
  {
    title: "Clean the bathroom",
  },
];
export default function Profile() {
  return (
    <>
      <div>
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
                items={taskPreference}
              />
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
                items={availability}
              />
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
                dataSource={taskHistory}
                renderItem={(item, index) => (
                  <List.Item>
                    <List.Item.Meta
                      avatar={<Avatar icon={<HomeOutlined />} />}
                      title={<a href="https://ant.design">{item.title}</a>}
                      description="Ant Design, a design language for background applications, is refined by Ant UED Team"
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
