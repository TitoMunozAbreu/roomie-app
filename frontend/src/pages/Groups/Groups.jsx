import {
  Avatar,
  Button,
  Card,
  Col,
  Divider,
  Dropdown,
  Input,
  List,
  Menu,
  Modal,
  Popconfirm,
  Row,
  Space,
  Tooltip,
} from "antd";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { householdService } from "../../Service/household-service";
import { householdActions } from "../../store/reducers/household-slice";
import {
  CheckOutlined,
  CloseOutlined,
  DeleteOutlined,
  EditOutlined,
  ExclamationCircleFilled,
  HomeOutlined,
  MinusOutlined,
  PlusOutlined,
  SettingOutlined,
  UserAddOutlined,
  UserDeleteOutlined,
  WarningOutlined,
} from "@ant-design/icons";
import styles from "./Groups.module.css";
import { uiActions } from "../../store/reducers/ui-slice";
import {
  createHousehold,
  deleteHouseholdMember,
  updateHouseholdMembers,
  updateHouseholdName,
} from "../../store/actions/household-actions";
import Notification from "../../components/Notification/Notification";

const Groups = () => {
  const dispatch = useDispatch();
  const [modal, contextHolder] = Modal.useModal();

  const user = useSelector((state) => state.user.user);
  const households = useSelector((state) => state.households.households);
  const notification = useSelector((state) => state.ui.notification);
  const errorMessage = useSelector((state) => state.ui.errorMessage);

  const [editHouseholdId, setEditHouseholdId] = useState(null);
  const [newHouseholdName, setNewHouseholdName] = useState(null);
  const [newMemberEmail, setNewMemberEmail] = useState(null);
  const [selectedHouseholdId, setSelectedHouseholdId] = useState(null);
  const [selectedBtnCreateHousehold, setSelectedBtnCreateHousehold] =
    useState(false);

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

  const hanldeCreateHousehold = () => {
    dispatch(createHousehold(newHouseholdName, user.id, user.email));
    hanldeCancelCreateHousehold();
  };

  const hanldeCreateHouseholdClick = () => {
    setSelectedBtnCreateHousehold(true);
  };

  const hanldeCancelCreateHousehold = () => {
    setSelectedBtnCreateHousehold(false);
    setNewHouseholdName(null);
  };

  const handleEditHouseholdName = (userId, householdName) => {
    setEditHouseholdId(userId);
    setNewHouseholdName(householdName);
  };

  const hanldeUpdateHouseholdName = () => {
    dispatch(updateHouseholdName(editHouseholdId, newHouseholdName));
    handleCancelEditHouseholdName();
  };

  const handleCancelEditHouseholdName = () => {
    setEditHouseholdId(null);
    setNewHouseholdName(null);
  };

  const handleAddNewMemberClick = (householdId) => {
    setSelectedHouseholdId(householdId);
  };

  const handleAddNewMember = (householdId, members) => {
    if (!newMemberEmail) return;
    const memberEmails = members.map((member) => member.email);
    memberEmails.push(newMemberEmail);
    const memberEmailsString = memberEmails.join(",");
    dispatch(updateHouseholdMembers(householdId, memberEmailsString));
    handleCancelNewMember();
  };

  const handleCancelNewMember = () => {
    setSelectedHouseholdId(null);
    setNewMemberEmail(null);
  };

  const handleDeleteMember = (householdId, memberEmail) => {
    dispatch(deleteHouseholdMember(householdId, memberEmail));
  };

  const showDeleteConfirm = (householdId) => {
    modal.confirm({
      title: "Delete Household?",
      icon: <ExclamationCircleFilled />,
      content: "This can't be undone",
      okText: "Delete",
      okType: "danger",
      cancelText: "Cancel",
      onOk() {
        console.log("OK");
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  return (
    <>
      {notification?.type && <Notification />}
      {contextHolder}
      <div
        style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Card
          style={{ width: "100%" }}
          title="Household Groups"
          className={styles.groupSettings}
          extra={
            <Button
              variant="outlined"
              icon={<PlusOutlined />}
              onClick={hanldeCreateHouseholdClick}
            ></Button>
          }
        >
          {selectedBtnCreateHousehold && (
            <div style={{ margin: "10px 0" }} id="newHouseholdInpunt">
              <Row gutter={16}>
                <Col xs={24} md={12}>
                  <Input
                    placeholder="Enter new household name"
                    value={newHouseholdName}
                    onChange={(e) => setNewHouseholdName(e.target.value)}
                    onPressEnter={hanldeCreateHousehold}
                    variant="filled"
                  />
                </Col>
                <Col xs={24} md={12}>
                  {/* Btn cancel creating household */}
                  <Button
                    style={{ width: "40px" }}
                    variant="outlined"
                    icon={<CloseOutlined />}
                    onClick={hanldeCancelCreateHousehold}
                  ></Button>
                  {/* Btn create new household */}
                  <Button
                    style={{ marginLeft: "2%", width: "40px" }}
                    type="primary"
                    icon={<CheckOutlined />}
                    onClick={hanldeCreateHousehold}
                  ></Button>
                </Col>
              </Row>
            </div>
          )}
          {errorMessage && <span>{errorMessage}</span>}

          {/* Household groups */}
          {households &&
            households.map((item) => (
              <Card
                className={styles.groupCard}
                type="inner"
                title={
                  // Display houseName or input
                  editHouseholdId == item.id ? (
                    <Input
                      value={newHouseholdName}
                      onChange={(e) => setNewHouseholdName(e.target.value)}
                      onPressEnter={hanldeUpdateHouseholdName}
                      placeholder="Filled"
                      variant="filled"
                      style={{ width: "50%" }}
                    />
                  ) : (
                    item.householdName
                  )
                }
                key={item.mail}
                extra={
                  editHouseholdId == item.id ? (
                    <div style={{ display: "flex" }}>
                      {/* Btn cancel edit household name */}
                      <Button
                        style={{ marginLeft: "5%", width: "40px" }}
                        variant="outlined"
                        icon={<CloseOutlined />}
                        danger
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
                      {/* Add new member */}
                      <Button
                        style={{ marginRight: "5%" }}
                        variant="outlined"
                        icon={<UserAddOutlined />}
                        onClick={() => handleAddNewMemberClick(item.id)}
                      ></Button>
                      <Dropdown
                        overlay={
                          <Menu>
                            {/* Btn Edit household name */}
                            <Menu.Item
                              key="edit"
                              onClick={() =>
                                handleEditHouseholdName(
                                  item.id,
                                  item.householdName
                                )
                              }
                            >
                              <Button
                                variant="outlined"
                                icon={<EditOutlined />}
                                type="primary"
                                ghost
                              >
                                Household name
                              </Button>
                            </Menu.Item>
                            {/* Delete household */}
                            <Menu.Item
                              key="delete"
                              onClick={() => showDeleteConfirm(item.id)}
                            >
                              <Button
                                variant="outlined"
                                icon={<DeleteOutlined />}
                                danger
                              >
                                Delete household
                              </Button>
                            </Menu.Item>
                          </Menu>
                        }
                        trigger={["hover"]}
                      >
                        <Button icon={<SettingOutlined />} />
                      </Dropdown>
                    </div>
                  )
                }
              >
                {/* Add new member  */}
                {selectedHouseholdId === item.id && (
                  <div style={{ margin: "10px 0" }} id="newMemberInput">
                    <Row gutter={16}>
                      <Col xs={12} md={12}>
                        <Input
                          placeholder="Enter new member's email"
                          value={newMemberEmail}
                          onChange={(e) => setNewMemberEmail(e.target.value)}
                          onPressEnter={() =>
                            handleAddNewMember(item.id, item.members)
                          }
                          variant="filled"
                        />
                      </Col>
                      <Col xs={12} md={12}>
                        {/* Btn cancel adding new member */}
                        <Button
                          style={{ width: "40px" }}
                          variant="outlined"
                          danger
                          icon={<CloseOutlined />}
                          onClick={handleCancelNewMember}
                        ></Button>
                        {/* Btn adding new member */}
                        <Button
                          style={{ marginLeft: "2%", width: "40px" }}
                          variant="outlined"
                          icon={<CheckOutlined />}
                          onClick={() =>
                            handleAddNewMember(item.id, item.members)
                          }
                        ></Button>
                      </Col>
                    </Row>
                    <Row gutter={16}>
                      <Col xs={24} md={12}>
                        <small>
                          These member will be notified you've added to you
                          group.
                        </small>
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
                          {/* Delete member */}
                          <Button
                            variant="outlined"
                            icon={<UserDeleteOutlined />}
                            danger
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
      </div>
    </>
  );
};

export default Groups;
