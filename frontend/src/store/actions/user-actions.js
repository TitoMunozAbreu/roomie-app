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
        dispatch(
          uiActions.showNotification({
            type: "success",
            message: "Task preferences updated!",
          })
        );
      })
      .catch(function (error) {
        dispatch(uiActions.showModal());
        dispatch(
          uiActions.showNotification({
            type: "error",
            message: error.response.data,
          })
        );
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
        dispatch(
          uiActions.showNotification({
            type: "success",
            message: "Availabilities updated!",
          })
        );
      })
      .catch(function (errorResponse) {
        dispatch(uiActions.showModal());
        dispatch(
          uiActions.showNotification({
            type: "error",
            message: errorResponse.data,
          })
        );
      });
  };
};
