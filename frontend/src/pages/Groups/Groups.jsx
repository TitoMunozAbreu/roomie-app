import {
  Avatar,
  Button,
  Card,
  Col,
  Divider,
  Input,
  List,
  Row,
  Tooltip,
} from "antd";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { householdService } from "../../Service/household-service";
import { householdActions } from "../../store/reducers/household-slice";
import {
  CheckOutlined,
  CloseOutlined,
  EditOutlined,
  MinusOutlined,
  PlusOutlined,
  UserAddOutlined,
  WarningOutlined,
} from "@ant-design/icons";
import styles from "./Groups.module.css";
import { uiActions } from "../../store/reducers/ui-slice";
import {
  deleteHouseholdMember,
  updateHouseholdName,
} from "../../store/actions/household-actions";
import Notification from "../../components/Notification/Notification";

const Groups = () => {
  const dispatch = useDispatch();

  const user = useSelector((state) => state.user.user);
  const households = useSelector((state) => state.households.households);
  const notification = useSelector((state) => state.ui.notification);
  const errorMessage = useSelector((state) => state.ui.errorMessage);

  const [editHouseHoldNameId, setEditHouseNameId] = useState(null);
  const [newHouseholdName, setNewHouseholdName] = useState("");
  const [newMemberEmail, setNewMemberEmail] = useState(null);
  const [selectedHouseholdId, setSelectedHouseholdId] = useState(null);

  useEffect(() => {
    async function fetchHouseholds() {
      return await householdService.getHouseholds();
    }

    fetchHouseholds()
      .then(function (response) {
        dispatch(householdActions.setHouseholds(response.data));
        dispatch(uiActions.updateErrorMessage(null));
      })
      .catch(function (error) {
        dispatch(uiActions.updateErrorMessage(error.response.data));
      });
  }, [dispatch]);

  const hanldeCreateHousehold = () => {};

  const handleEditHouseholdName = (userId, householdName) => {
    setEditHouseNameId(userId);
    setNewHouseholdName(householdName);
  };

  const hanldeUpdateHouseholdName = () => {
    dispatch(updateHouseholdName(editHouseHoldNameId, newHouseholdName));
    handleCancelEditHouseholdName();
  };

  const handleCancelEditHouseholdName = () => {
    setEditHouseNameId(null);
    setNewHouseholdName("");
  };

  const handleAddNewMemberClick = (householdId) => {
    setSelectedHouseholdId(householdId);
  };

  const handleAddNewMember = () => {
    if (!newMemberEmail) return;
  };

  const handleCancelNewMember = () => {
    setSelectedHouseholdId(null);
  };

  const handleDeleteMember = (householdId, memberEmail) => {
    dispatch(deleteHouseholdMember(householdId, memberEmail));
  };

  return (
    <>
      {notification?.type && <Notification />}
      <div
        style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        {/* <Row gutter={16}>
          <Col xs={24} md={12}> */}
        <Card
          style={{ width: "60%" }}
          title="Household Groups"
          className={styles.groupSettings}
          extra={
            <Button
              variant="outlined"
              icon={<PlusOutlined />}
              onClick={hanldeCreateHousehold}
            ></Button>
          }
        >
          {errorMessage && <span>{errorMessage}</span>}
          {/* Household groups */}
          {households &&
            households.map((item) => (
              <Card
                className={styles.groupCard}
                type="inner"
                title={
                  // Display houseName or input
                  editHouseHoldNameId == item.id ? (
                    <Input
                      value={newHouseholdName}
                      onChange={(e) => setNewHouseholdName(e.target.value)}
                      onPressEnter={hanldeUpdateHouseholdName}
                      placeholder="Filled"
                      variant="filled"
                    />
                  ) : (
                    item.householdName
                  )
                }
                key={item.mail}
                extra={
                  editHouseHoldNameId == item.id ? (
                    <div style={{ display: "flex" }}>
                      {/* Btn cancel edit household name */}
                      <Button
                        style={{ marginLeft: "5%", width: "40px" }}
                        variant="outlined"
                        icon={<CloseOutlined />}
                        onClick={handleCancelEditHouseholdName}
                      ></Button>
                      {/* Btn update household name */}
                      <Button
                        style={{ marginLeft: "5%", width: "40px" }}
                        variant="outlined"
                        icon={<CheckOutlined />}
                        onClick={hanldeUpdateHouseholdName}
                      ></Button>
                    </div>
                  ) : (
                    <div style={{ display: "flex" }}>
                      {/* Btn Edit household name */}
                      <Button
                        style={{ marginLeft: "5%", width: "40px" }}
                        variant="outlined"
                        icon={<EditOutlined />}
                        onClick={() =>
                          handleEditHouseholdName(item.id, item.householdName)
                        }
                      ></Button>
                      {/* Add new member */}
                      <Button
                        style={{ marginLeft: "5%", width: "40px" }}
                        variant="outlined"
                        icon={<UserAddOutlined />}
                        onClick={() => handleAddNewMemberClick(item.id)}
                      ></Button>
                    </div>
                  )
                }
              >
                {/* Add new member  */}
                {selectedHouseholdId === item.id && (
                  <div style={{ margin: "10px 0" }}>
                    <Row gutter={16}>
                      <Col xs={24} md={12}>
                        <Input
                          placeholder="Enter new member's email"
                          value={newMemberEmail}
                          onChange={(e) => setNewMemberEmail(e.target.value)}
                          onPressEnter={handleAddNewMember}
                          variant="filled"
                        />
                      </Col>
                      <Col xs={24} md={12}>
                        {/* Btn cancel adding new member */}
                        <Button
                          style={{ width: "40px" }}
                          variant="outlined"
                          icon={<CloseOutlined />}
                          onClick={handleCancelNewMember}
                        ></Button>
                        <Button
                          style={{ marginLeft: "2%", width: "40px" }}
                          type="primary"
                          icon={<CheckOutlined />}
                          onClick={handleAddNewMember}
                        ></Button>
                      </Col>
                    </Row>
                  </div>
                )}

                {/* Members */}
                <List
                  dataSource={item.members}
                  renderItem={(member) => (
                    <List.Item key={member.email}>
                      <List.Item.Meta
                        //Member profile image
                        avatar={
                          <Avatar
                            src={
                              "https://cdn-icons-png.flaticon.com/512/149/149071.png"
                            }
                          />
                        }
                        //Member data
                        title={
                          <strong>
                            {member.firstName} {member.lastName}{" "}
                            {member.invitationAccepted === false && (
                              <Tooltip
                                placement="right"
                                title="Invitation has not been accepted."
                              >
                                <WarningOutlined />
                              </Tooltip>
                            )}
                          </strong>
                        }
                        description={member.email}
                      />
                      {user.id !== member.userId && (
                        <div>
                          <Button
                            variant="outlined"
                            icon={<MinusOutlined />}
                            onClick={() =>
                              handleDeleteMember(item.id, member.email)
                            }
                          ></Button>
                        </div>
                      )}
                    </List.Item>
                  )}
                />
              </Card>
            ))}
        </Card>
        {/* </Col> */}
        {/* <Col xs={24} md={12}></Col> */}
        {/* </Row> */}
      </div>
    </>
  );
};

export default Groups;
