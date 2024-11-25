import React, { useState } from "react";
import { Button, Card, Typography, Spin } from "antd";
import { useKeycloak } from "@react-keycloak/web";
import { useNavigate, useParams } from "react-router-dom";
import { householdService } from "../../Service/household-service";

const { Title, Text } = Typography;

const AcceptInvitation = () => {
  const { keycloak } = useKeycloak();
  const authenticated = keycloak.authenticated;
  const { householdId } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const handleMemberInvitation = (isAccepted) => {
    setLoading(true);
    householdService.memberInvitation(householdId, isAccepted);
    navigate("/dashboard"); // Redirige al dashboard
    setLoading(false);
  };

  const handleLogin = () => {
    keycloak.login({ redirectUri: window.location.href });
  };

  const handleRegister = () => {
    keycloak.register({ redirectUri: window.location.href });
  };

  const handleNotAuthReject = () => {
    setLoading(true);
    navigate("/home");
    setLoading(false);
  };

  return (
    <>
      {loading && <Spin tip="Redirecting to login..." />}
      <div style={{ display: "flex", justifyContent: "center", marginTop: 50 }}>
        <Card style={{ width: 400, textAlign: "center" }}>
          <Title level={3}>You've been invited!</Title>
          <Text>
            You have been invited to join a household. Would you like to accept
            or reject this invitation?
          </Text>

          {authenticated ? (
            <div style={{ marginTop: 20 }}>
              <Button
                type="primary"
                onClick={() => handleMemberInvitation(true)}
                style={{ marginRight: 10 }}
              >
                Accept
              </Button>
              <Button danger onClick={() => handleMemberInvitation(false)}>
                Reject
              </Button>
            </div>
          ) : (
            <div style={{ marginTop: 20 }}>
              <div style={{ margin: 40 }}>
                <Button
                  disabled
                  type="primary"
                  style={{ marginRight: 10 }}
                >
                  Accept
                </Button>
                <Button danger onClick={handleNotAuthReject}>
                  Reject
                </Button>
              </div>
              <Text
                type="warning"
                style={{ display: "block", marginBottom: 10 }}
              >
                Please log in or sign up to accept the invitation.
              </Text>
              <Button
                type="primary"
                onClick={handleLogin}
                style={{ marginRight: 10 }}
              >
                Login
              </Button>
              <Button type="default" onClick={handleRegister}>
                Register
              </Button>
            </div>
          )}
        </Card>
      </div>
    </>
  );
};

export default AcceptInvitation;
