import React, {
  forwardRef,
  useEffect,
  useImperativeHandle,
  useState,
} from "react";
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";
import { Button, Form, Input, InputNumber, Space, Select } from "antd";
import { useDispatch, useSelector } from "react-redux";
import { updateAvailabilities, updatePreferences } from "../../store/actions/user-actions";

const UserForm = forwardRef(({ formType }, ref) => {
  const dispatch = useDispatch();
  const modalType = useSelector((state) => state.ui.profile.modal.type);
  const user = useSelector((state) => state.user.user);
  const [form] = Form.useForm();
  const [originalData, setOriginalData] = useState(null);

  useImperativeHandle(ref, () => ({
    submit: () => {
      form.submit();
    },
    reset: () => {
      form.resetFields();
    },
    restoreOriginalData: () => {
      if (originalData) {
        form.setFieldsValue(originalData);
      }
    },
  }));

  useEffect(() => {
    if (user) {
      const formattedData = {
        firstName: user.firstName,
        lastName: user.lastName,
        preferences:
          user.taskPreferences?.map((item) => ({
            taskName: item.taskName,
            preference: item.preference,
          })) || [],
        availabilities: user.availabilities?.map((item) => ({
          day: item.day,
          hours: item.hours,
        }))|| [],
      };
      form.setFieldsValue(formattedData);
      setOriginalData(formattedData);
    }
  }, [user, form]);

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
                    <Input placeholder="Task name" />
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

  const onFinish = (values) => {
    if (formType === "formPreferences") {
      dispatch(updatePreferences(user.id, values.preferences));
    } else if (formType === "formUserInfo") {
      // dispatch(updateUserInfo(values.userInfo));
    } else if (formType === "formAvailabilities") {
      dispatch(updateAvailabilities(user.id, values.availabilities));
    }
  };

  return (
    <>
      {modalType === "formUserInfo" && formUserInfo()}
      {modalType === "formPreferences" && formPreferences()}
      {modalType === "formAvailabilities" && formAvailabilities()}
    </>
  );
});

export default UserForm;
