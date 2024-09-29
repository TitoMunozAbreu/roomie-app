import React from "react";
import { Row, Col, Button, Card } from "antd";
import { Divider } from "antd";

import "./Home.css";

import cleanHouse from "../../assets/images/clean-house.jpg";
import { FEATURES } from "./fetures";

export default function Home() {
  return (
    <div className="home-page">
      {/* Section 1: rigth image and left some text*/}
      <section className="intro-section">
        <Row gutter={16} align="middle">
          <Col xs={24} md={12}>
            <h1>Manage Your Household Tasks</h1>
            <p>
              Organize and assign tasks easily to save time and simplify your
              life.
            </p>
            <Button type="primary" size="large">
              Sign in
            </Button>
          </Col>

          <Col xs={24} md={12}>
            <img
              src={cleanHouse}
              alt="Tareas del hogar"
              className="intro-image"
            />
          </Col>
        </Row>
      </section>

      {/* Section 2: Three main features */}
      <section className="features-section">
        <Divider plain>
          <h2>Main Features</h2>
        </Divider>
        <Row gutter={16}>
          {FEATURES.map(({ key, title, description, image }) => (
            <Col xs={24} md={8} key={key}>
              <Card
                hoverable
                cover={
                  <img className="feature-image" alt={title} src={image} />
                }
              >
                <Card.Meta title={title} description={description} />
              </Card>
            </Col>
          ))}
        </Row>
      </section>
    </div>
  );
}
