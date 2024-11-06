import { Avatar, Button, Card, Col, Divider, Input, List, Row } from "antd";
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
} from "@ant-design/icons";
import styles from "./Groups.module.css";

const Groups = () => {
  const user = useSelector((state) => state.user.user);
  const households = useSelector((state) => state.households.households);
  const dispatch = useDispatch();
  const [editHouseHoldNameId, setEditHouseNameId] = useState(null);
  const [newHouseholdName, setNewHouseholdName] = useState("");

  useEffect(() => {
    async function fetchHouseholds() {
      return await householdService.getHouseholds();
    }

    fetchHouseholds().then(function (response) {
      dispatch(householdActions.setHouseholds(response.data));
    });
  }, [dispatch]);

  const hanldeCreateHousehold = () => {};

  const handleEditHouseholdName = (userId, householdName) => {
    setEditHouseNameId(userId);
    setNewHouseholdName(householdName);
  };

  const hanldeUpdateHouseholdName = () => {
    console.log(
      "new household name and id",
      editHouseHoldNameId,
      newHouseholdName
    );

    handleCancelEditHouseholdName();
  };

  const handleCancelEditHouseholdName = () => {
    setEditHouseNameId(null);
    setNewHouseholdName("");
  };

  return (
    <>
      <div>
        <Row gutter={16}>
          <Col xs={24} md={12}>
            <Card
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
              {/* Household groups */}
              {households &&
                households.map((item) => (
                  <Card
                    className={styles.groupCard}
                    type="inner"
                    title={
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
                    key={item.id}
                    extra={
                      editHouseHoldNameId == item.id ? (
                        <div style={{ display: "flex" }}>
                          <Button
                            style={{ marginLeft: "5%", width: "40px" }}
                            variant="outlined"
                            icon={<CloseOutlined />}
                            onClick={handleCancelEditHouseholdName}
                          ></Button>
                          <Button
                            style={{ marginLeft: "5%", width: "40px" }}
                            variant="outlined"
                            icon={<CheckOutlined />}
                            onClick={hanldeUpdateHouseholdName}
                          ></Button>
                        </div>
                      ) : (
                        <div style={{display: "flex"}}>
                          <Button
                            style={{ marginLeft: "5%", width: "40px"  }}
                            variant="outlined"
                            icon={<EditOutlined />}
                            onClick={() =>
                              handleEditHouseholdName(
                                item.id,
                                item.householdName
                              )
                            }
                          ></Button>
                          <Button
                            style={{ marginLeft: "5%", width: "40px"  }}
                            variant="outlined"
                            icon={<UserAddOutlined />}
                            onClick={() =>
                              handleEditHouseholdName(
                                item.id,
                                item.householdName
                              )
                            }
                          ></Button>
                        </div>
                      )
                    }
                  >
                    {/* Members */}
                    <List
                      dataSource={item.members}
                      renderItem={(member) => (
                        <List.Item key={member.userId}>
                          <List.Item.Meta
                            avatar={
                              <Avatar
                                src={`https://randomuser.me/api/portraits/men/${member.userId.at(
                                  0
                                )}.jpg`}
                              />
                            }
                            title={
                              <strong>
                                {member.firstName} {member.lastName}
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
                                  handleDeleteMember(member.userId)
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
          </Col>
          <Col xs={24} md={12}></Col>
        </Row>
      </div>
    </>
  );
};

export default Groups;
