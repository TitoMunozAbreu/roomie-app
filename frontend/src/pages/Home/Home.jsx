import React from "react";
import { Row, Col, Button, Card, Typography } from "antd";
import { Divider } from "antd";
import { FEATURES } from "./fetures";
import { useKeycloak } from "@react-keycloak/web";
import cleanHouse from "../../assets/images/clean-house.jpg";
import styles from "./Home.module.css";
import { useSelector } from "react-redux";
import logo from "../../assets/images/logo.png";


const { Title } = Typography;

export default function Home() {
  const isDarkMode = useSelector((state) => state.ui.isDarkMode);

  const { keycloak } = useKeycloak();
  const handleSignIn = () => {
    keycloak.login({ redirectUri: "http://localhost:5173/dashboard" });
  };

  let btnSignIn = (
    <Button type="primary" size="large" onClick={handleSignIn}>
      Sign in
    </Button>
  );

  if (keycloak.authenticated) {
    btnSignIn = <></>;
  }

  return (
    <div className={styles.homePage}>
      {/* Section 1: rigth image and left some text*/}
      <section className={styles.introSection}>
        <Row gutter={16} align="middle">
          <Col xs={24} md={12}>
            <img
              style={{ width: 140, alignItems: "center", marginLeft: "80px" }}
              src={logo}
            ></img>
            <Title id={isDarkMode ? styles.titleDarkMode : styles.title}>
              ROOMIE
            </Title>
            <h1>Manage Your Household Tasks</h1>
            <p>
              Organize and assign tasks easily to save time and simplify your
              life.
            </p>
            {btnSignIn}
          </Col>

          <Col xs={24} md={12}>
            <img
              src={cleanHouse}
              alt="Tareas del hogar"
              className={styles.introImage}
            />
          </Col>
        </Row>
      </section>

      {/* Section 2: Three main features */}
      <section className={styles.featuresSection}>
        <Divider plain>
          <h2>Main Features</h2>
        </Divider>
        <Row gutter={16}>
          {FEATURES.map(({ key, title, description, image }) => (
            <Col xs={24} md={8} key={key}>
              <Card
                className={styles.customCard}
                hoverable
                cover={
                  <img
                    className={styles.featureImage}
                    alt={title}
                    src={image}
                    style={
                      isDarkMode
                        ? {
                            filter: "brightness(0.8) contrast(0.9)",
                            "mix-blend-mode": "difference",
                          }
                        : {}
                    }
                  />
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
