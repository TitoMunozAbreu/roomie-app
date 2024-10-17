import React, { forwardRef, useEffect, useImperativeHandle, useState } from "react";
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";
import { Button, Form, Input, InputNumber, Space } from "antd";

const UserForm = forwardRef(({ formType, data }, ref) => {
  const [form] = Form.useForm();
  const [originalData, setOriginalData] = useState(null);

  useImperativeHandle(ref, () => ({
    submit: () => {
      form.submit();
    },
    reset: () => {
      form.resetFields()
    },
    restoreOriginalData: () => {
      if(originalData){
        form.setFieldsValue(originalData);
      }
    }
  }));

  useEffect(() => {
    if (data) {
      const formattedData = {
        preferences: data.map((item) => ({
          taskName: item.taskName,
          preference: item.preference,
        })),
      };
      form.setFieldsValue(formattedData);
      setOriginalData(formattedData);
    }
  }, [data, form]);

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
                        message: "set from 1 to 5",
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

  const onFinish = (values) => {
    console.log('Sending http resquest...');
        
  };

  return <>{formType === "formPreferences" ? formPreferences() : null}</>;
});

export default UserForm;
