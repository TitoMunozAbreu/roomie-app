import React, {
  forwardRef,
  useEffect,
  useImperativeHandle,
  useState,
} from "react";
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";
import {
  Button,
  Form,
  Input,
  InputNumber,
  Space,
  Select,
  DatePicker,
  Row,
  Col,
} from "antd";
import { useDispatch, useSelector } from "react-redux";
import {
  updateAvailabilities,
  updatePreferences,
} from "../../store/actions/user-actions";
import moment from "moment";
import { createNewTask, updateTask } from "../../store/actions/task-actions";
import { uiActions } from "../../store/reducers/ui-slice";
import { householdActions } from "../../store/reducers/household-slice";

const UserForm = () => {
  const dispatch = useDispatch();

  const modalType = useSelector((state) => state.ui.profile.modal.type);
  const user = useSelector((state) => state.user.user);
  const selectedTask = useSelector((state) => state.households.selectedTask);
  const isTaskEdit = useSelector((state) => state.households.isTaskEdit);
  const selectedHousehold = useSelector(
    (state) => state.households.selectedHousehold
  );
  const isFormSubmitTriggered = useSelector((state) => state.ui.submitForm);

  const [form] = Form.useForm();
  const [originalData, setOriginalData] = useState(null);

  useEffect(() => {
    if (!isTaskEdit) {
      form.resetFields();
      dispatch(householdActions.setTask(null));
    }
  }, [isTaskEdit, form, dispatch]);

  useEffect(() => {
    if (isFormSubmitTriggered) {
      console.log("submitting form");

      form.submit();
    }
  }, [isFormSubmitTriggered, form, dispatch]);

  useEffect(() => {
    let formattedData = {};

    if (modalType !== "formTask" && user) {
      console.log("if", user);
      formattedData = formatUserData(user);
    } else if (selectedHousehold || selectedTask) {
      console.log("else if", selectedHousehold);

      formattedData = formatTaskData(selectedTask);
    }
    console.log(formattedData);

    form.setFieldsValue(formattedData);
    setOriginalData(formattedData);
  }, [modalType, user, selectedHousehold, selectedTask, form]);

  const formatUserData = (user) => ({
    firstName: user?.firstName || "",
    lastName: user?.lastName || "",
    preferences:
      user.taskPreferences?.map((item) => ({
        taskName: item.taskName,
        preference: item.preference,
      })) || [],
    availabilities:
      user.availabilities?.map((item) => ({
        day: item.day,
        hours: item.hours,
      })) || [],
  });

  const formatTaskData = (task) => ({
    taskId: task?.id || null,
    householdId: task?.householdId || selectedHousehold?.id,
    createdBy: task?.createdBy || user?.email,
    title: task?.title || "",
    description: task?.description || "",
    category: task?.category || null,
    estimatedDuration: task?.estimatedDuration || null,
    assignedTo: task?.assignedTo || null,
    dueDate: task?.dueDate ? null : null,
    status: task?.status || null,
  });

  const formUserInfo = () => {
    return (
      <Form
        form={form}
        name="formUserInfo"
        onFinish={onFinish}
        style={{
          maxWidth: 600,
        }}
        autoComplete="off"
      >
        <Form.Item
          label="First Name"
          name="firstName"
          rules={[
            {
              required: true,
              message: "Please input your first name!",
            },
          ]}
        >
          <Input placeholder="Enter first name" />
        </Form.Item>

        <Form.Item
          label="Last Name"
          name="lastName"
          rules={[
            {
              required: true,
              message: "Please input your last name!",
            },
          ]}
        >
          <Input placeholder="Enter last name" />
        </Form.Item>
      </Form>
    );
  };

  const formPreferences = () => {
    return (
      <Form
        form={form}
        name="formPreferences"
        onFinish={onFinish}
        style={{
          maxWidth: 600,
        }}
        autoComplete="off"
      >
        <Form.List name="preferences">
          {(fields, { add, remove }) => (
            <>
              {fields.map(({ key, name, ...restField }) => (
                <Space
                  key={key}
                  style={{
                    display: "flex",
                    marginBottom: 8,
                  }}
                  align="baseline"
                >
                  <Form.Item
                    {...restField}
                    name={[name, "taskName"]}
                    rules={[
                      {
                        required: true,
                        message: "task name required",
                      },
                    ]}
                  >
                    <Select placeholder="Select category">
                      <Select.Option value="Cleaning">Cleaning</Select.Option>
                      <Select.Option value="Cooking">Cooking</Select.Option>
                      <Select.Option value="Laundry">Laundry</Select.Option>
                      <Select.Option value="Organizing">
                        Organizing
                      </Select.Option>
                      <Select.Option value="Gardening">Gardening</Select.Option>
                    </Select>
                  </Form.Item>
                  <Form.Item
                    {...restField}
                    name={[name, "preference"]}
                    rules={[
                      {
                        required: true,
                        message: "set from 1 (low) to 5 (high)",
                      },
                    ]}
                  >
                    <InputNumber placeholder="Preference" />
                  </Form.Item>
                  <MinusCircleOutlined onClick={() => remove(name)} />
                </Space>
              ))}
              <Form.Item>
                <Button
                  type="dashed"
                  onClick={() => add()}
                  block
                  icon={<PlusOutlined />}
                >
                  Add Preferences
                </Button>
              </Form.Item>
            </>
          )}
        </Form.List>
      </Form>
    );
  };

  const formAvailabilities = () => {
    return (
      <Form
        form={form}
        name="formAvailabilities"
        onFinish={onFinish}
        style={{
          maxWidth: 600,
        }}
        autoComplete="off"
      >
        <Form.List name="availabilities">
          {(fields, { add, remove }) => (
            <>
              {fields.map(({ key, name, ...restField }) => (
                <Space
                  key={key}
                  style={{
                    display: "flex",
                    marginBottom: 8,
                  }}
                  align="baseline"
                >
                  <Form.Item
                    {...restField}
                    name={[name, "day"]}
                    rules={[
                      {
                        required: true,
                        message: "Please select a day",
                      },
                    ]}
                  >
                    <Select placeholder="Select a day">
                      <Select.Option value="monday">Monday</Select.Option>
                      <Select.Option value="tuesday">Tuesday</Select.Option>
                      <Select.Option value="wednesday">Wednesday</Select.Option>
                      <Select.Option value="thursday">Thursday</Select.Option>
                      <Select.Option value="friday">Friday</Select.Option>
                      <Select.Option value="saturday">Saturday</Select.Option>
                      <Select.Option value="sunday">Sunday</Select.Option>
                    </Select>
                  </Form.Item>
                  <Form.Item
                    {...restField}
                    name={[name, "hours", 0]}
                    rules={[
                      {
                        required: true,
                        message: "Start time required",
                      },
                      {
                        type: "string",
                        message: "Must type numbers"
                      }
                    ]}
                  >
                    <Input placeholder="Start Time" type="time" />
                  </Form.Item>
                  <Form.Item
                    {...restField}
                    name={[name, "hours", 1]}
                    rules={[
                      {
                        required: true,
                        message: "End time required",
                      },
                    ]}
                  >
                    <Input placeholder="End Time" type="time" />
                  </Form.Item>
                  <MinusCircleOutlined onClick={() => remove(name)} />
                </Space>
              ))}
              <Form.Item>
                <Button
                  type="dashed"
                  onClick={() => add()}
                  block
                  icon={<PlusOutlined />}
                >
                  Add Availability
                </Button>
              </Form.Item>
            </>
          )}
        </Form.List>
      </Form>
    );
  };

  const formTask = () => {
    return (
      <Form form={form} name="formTask" layout="vertical" onFinish={onFinish}>
        <div>
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="taskId" name="taskId" hidden></Form.Item>
              <Form.Item
                label="householdId"
                name="householdId"
                hidden
              ></Form.Item>
              <Form.Item label="createdBy" name="createdBy" hidden></Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col span={12}>
              {/* Title */}
              <Form.Item
                label="Title"
                name="title"
                rules={[{ required: true, message: "Please enter a title!" }]}
              >
                <Input placeholder="Enter task title" />
              </Form.Item>
            </Col>
            <Col span={12}>
              {/* Category */}
              <Form.Item
                label="Category"
                name="category"
                rules={[
                  { required: true, message: "Please select a category!" },
                ]}
              >
                <Select placeholder="Select category">
                  <Select.Option value="Cleaning">Cleaning</Select.Option>
                  <Select.Option value="Cooking">Cooking</Select.Option>
                  <Select.Option value="Laundry">Laundry</Select.Option>
                  <Select.Option value="Organizing">Organizing</Select.Option>
                  <Select.Option value="Gardening">Gardening</Select.Option>
                </Select>
              </Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col span={24}>
              {/* Description */}
              <Form.Item
                label="Description"
                name="description"
                rules={[
                  {
                    pattern: /^[^\n]{0,45}$/, // Permite hasta 40 caracteres, sin saltos de línea.
                    message: "Description must not exceed 40 characters.",
                  },
                ]}
              >
                <Input.TextArea placeholder="Enter task description" />
              </Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col span={12}>
              {/* Estimated Duration */}
              <Form.Item
                label="Estimated Duration (minutes)"
                name="estimatedDuration"
                rules={[
                  {
                    required: true,
                    message: "Please enter an estimated duration!",
                  },
                  {
                    type: "number",
                    min: 1,
                    message: "Must be at least 1 minute!",
                  },
                ]}
              >
                <InputNumber
                  style={{ width: "100%" }}
                  placeholder="Estimated duration in minutes"
                />
              </Form.Item>
            </Col>
            <Col span={12}>
              {/* Status */}
              <Form.Item
                label="Status"
                name="status"
                rules={[{ required: true, message: "Please select a status!" }]}
              >
                <Select placeholder="Select status">
                  <Select.Option value="Pending">Pending</Select.Option>
                  <Select.Option value="Progress">In Progress</Select.Option>
                  <Select.Option value="Overdue">Overdue</Select.Option>
                  <Select.Option value="Completed">Completed</Select.Option>
                  <Select.Option value="Cancelled">Cancelled</Select.Option>
                </Select>
              </Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col span={12}>
              {/* Due Date */}
              <Form.Item
                label="Due Date"
                name="dueDate"
                rules={[
                  { required: true, message: "Please select a due date!" },
                ]}
              >
                <DatePicker
                  style={{ width: "100%" }}
                  showTime
                  disabledDate={(current) =>
                    current && current < moment().endOf("day")
                  }
                />
              </Form.Item>
            </Col>
            <Col span={12}>
              {/* Assigned To */}
              <Form.Item
                label="Assigned To"
                name="assignedTo"
                rules={[{ required: true, message: "Please select a member!" }]}
              >
                <Select placeholder="Select a member">
                  {selectedHousehold?.members.map((member) => (
                    <Select.Option key={member.email} value={member.email}>
                      {member.firstName} {member.lastName}
                    </Select.Option>
                  ))}
                </Select>
              </Form.Item>
            </Col>
          </Row>
        </div>
      </Form>
    );
  };

  const onFinish = (values) => {
    if (modalType === "formPreferences") {
      dispatch(updatePreferences(user.id, values.preferences));
    } else if (modalType === "formUserInfo") {
      // dispatch(updateUserInfo(values.userInfo));
    } else if (modalType === "formAvailabilities") {
      dispatch(updateAvailabilities(user.id, values.availabilities));
    } else if (modalType === "formTask") {
      if (isTaskEdit) {
        dispatch(updateTask(values));
      } else {
        dispatch(createNewTask(values));
      }
    }
  };

  return (
    <>
      {modalType === "formUserInfo" && formUserInfo()}
      {modalType === "formPreferences" && formPreferences()}
      {modalType === "formAvailabilities" && formAvailabilities()}
      {modalType === "formTask" && formTask()}
    </>
  );
};

export default UserForm;
