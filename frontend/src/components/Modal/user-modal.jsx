import React, { useEffect, useRef, useState } from "react";
import { Modal, Button } from "antd";
import UserForm from "../Forms/user-form";
import { useDispatch, useSelector } from "react-redux";
import { uiActions } from "../../store/reducers/ui-slice";
import { householdActions } from "../../store/reducers/household-slice";

export default function userModal() {
  const dispatch = useDispatch();
  const isModalOpen = useSelector((state) => state.ui.profile.modal.isOpen);
  const modalTitle = useSelector((state) => state.ui.profile.modal.title);
  const modalType = useSelector((state) => state.ui.profile.modal.type);

  const handleCancel = () => {
    dispatch(uiActions.showModal());
    if (modalType === "formTask") {
      dispatch(householdActions.setIsTaskEdit(false));
      dispatch(uiActions.resetFormSubmit());
    }
  };

  const handleSendForm = () => {
    dispatch(uiActions.handleSubmitForm());
  };

  return (
    <Modal
      className="userModal"
      title={modalTitle}
      open={isModalOpen}
      onCancel={handleCancel}
      footer={[
        <Button key="back" onClick={handleCancel}>
          Cancel
        </Button>,
        <Button key="submit" onClick={handleSendForm}>
          Update
        </Button>,
      ]}
    >
      <UserForm />
    </Modal>
  );
}
