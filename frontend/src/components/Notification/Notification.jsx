import React, { useEffect } from "react";
import { message } from "antd";
import { useSelector, useDispatch } from "react-redux";
import { uiActions } from "../../store/reducers/ui-slice";

const Notification = () => {
  const [messageApi, contextHolder] = message.useMessage();
  const dispatch = useDispatch();
  const notificationState = useSelector((state) => state.ui.notification);

  useEffect(() => {
    const hideNotification = messageApi.open({
      style: {
        marginTop: "8vh",
      },
      type: notificationState.type,
      content: notificationState.message,
      duration: 5,
      onClose: () => dispatch(uiActions.clearNotification()),
    });
    return () => {
      dispatch(uiActions.clearNotification());
    };
  }, [notificationState, dispatch, messageApi]);

  return contextHolder;
};

export default Notification;
