import React, { useEffect, useRef, useState } from "react";
import { Modal, Button } from "antd";
import UserForm from "../Forms/user-form";

export default function userModal(props) {
  const { openModal, title, formType, data, onClose } = props;
  const [isModalOpen, setIsModalOpen] = useState(false);
  const formRef = useRef(null);

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
    if (formRef.current) {
      formRef.current.restoreOriginalData();
    }
  };

  const handleSendForm = () => {
    if (formRef.current) {
      formRef.current.submit();
    }
  };

  useEffect(() => {
    setIsModalOpen(openModal);
  }, [openModal]);


  return (
    <Modal
      className="userModal"
      title={title}
      open={isModalOpen}
      onCancel={handleCancel}
      footer={[
        <Button key="back" onClick={handleCancel}>
          Cancel
        </Button>,
        <Button key="submit" onClick={handleSendForm}>
          {data ? "Edit" : "Create"}
        </Button>,
      ]}
    >
      <UserForm ref={formRef} formType={formType} data={data} />
    </Modal>
  );
}
