import { userService } from "../../Service/user-service";
import { userActions } from "../reducers/user-slice";
import { uiActions } from "../reducers/ui-slice";
import { message } from "antd";

export const updatePreferences = (id, preferences) => {
  return async (dispatch) => {
    const putPreferences = async () => {
      return await userService.putPreferences(id, preferences);
    };
    putPreferences()
      .then(function (response) {
        dispatch(uiActions.showModal());
        dispatch(
          userActions.updatePreferences({ taskPreferences: response.data })
        );
        handleNotification("Task preferences updated!", dispatch);
      })
      .catch(function (error) {
        dispatch(uiActions.showModal());
        handleErrorResponse(error, dispatch);
      });
  };
};

export const updateAvailabilities = (id, availabilities) => {
  return async (dispatch) => {
    const putAvailabilities = async () => {
      return await userService.putAvailabilities(id, availabilities);
    };
    putAvailabilities()
      .then(function (response) {
        dispatch(uiActions.showModal());
        dispatch(
          userActions.updateAvailabilities({ availabilities: response.data })
        );
        handleNotification("Availabilities updated!", dispatch);
        dispatch(uiActions.resetFormSubmit());
      })
      .catch(function (error) {
        dispatch(uiActions.showModal());
        handleErrorResponse(error, dispatch);
        dispatch(uiActions.resetFormSubmit());
      });
  };
};

const handleErrorResponse = (error, dispatch) => {
  const errorMessage =
    error.response && error.response.data
      ? error.response.data
      : "An unexpected error occurred.";

  dispatch(
    uiActions.showNotification({
      type: "error",
      message: errorMessage,
    })
  );
};

const handleNotification = (message, dispatch) => {
  dispatch(
    uiActions.showNotification({
      type: "success",
      message: message,
    })
  );
};
