import React from "react";
import { Row, Col, Button, Card } from "antd";
import "./Home.css";
import cleanHouse from "../../assets/images/clean-house.jpg";
import historyClass from "../../assets/images/history-task.jpg";
import smartIA from "../../assets/images/smart-ia.jpg";
import organizerTask from "../../assets/images/organizer-task.jpg";


export default function Home() {
  return (
    <div className="home-page">
      {/* Sección 1: Imagen derecha y texto izquierdo */}
      <section className="intro-section">
        <Row gutter={16} align="middle">
          {/* Columna izquierda - Texto */}
          <Col xs={24} md={12}>
            <h1>Manage Your Household Tasks</h1>
            <p>
            Organize and assign tasks easily to save time and simplify your life.
            </p>
            <Button type="primary" size="large">
              Sign in
            </Button>
          </Col>
          {/* Columna derecha - Imagen */}
          <Col xs={24} md={12}>
            <img
              src={cleanHouse}
              alt="Tareas del hogar"
              className="intro-image"
            />
          </Col>
        </Row>
      </section>

      {/* Sección 2: Tres características principales */}
      <section className="features-section">
        <h2>Características Principales</h2>
        <Row gutter={16}>
          {/* Característica 1 */}
          <Col xs={24} md={8}>
            <Card
              hoverable
              cover={
                <img alt="Organización" src={organizerTask} />
              }
            >
              <Card.Meta
                title="Task Organization"
                description="Organize cleaning tasks and assign responsibilities easily."
              />
            </Card>
          </Col>
          {/* Característica 2 */}
          <Col xs={24} md={8}>
            <Card
              hoverable
              cover={
                <img
                  alt="Asignación Inteligente"
                  src={smartIA}
                />
              }
            >
              <Card.Meta
                title="Smart Assignment"
                description="Use our artificial intelligence to distribute tasks fairly."
              />
            </Card>
          </Col>
          {/* Característica 3 */}
          <Col xs={24} md={8}>
            <Card
              hoverable
              cover={
                <img
                  alt="Historial de Tareas"
                  src={historyClass}
                />
              }
            >
              <Card.Meta
                title="Task History"
                description="Check the history of completed tasks and optimize your time."
              />
            </Card>
          </Col>
        </Row>
      </section>
    </div>
  );
}
