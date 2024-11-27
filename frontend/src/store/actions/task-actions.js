import { taskService } from "../../Service/task-sevice";
import { householdActions } from "../reducers/household-slice";
import { uiActions } from "../reducers/ui-slice";

export const createNewTask = (task) => {
  return async (dispatch) => {
    const createTask = async () => {
      return await taskService.createTask(task);
    };

    createTask()
      .then(function (response) {
        dispatch(uiActions.showModal());
        dispatch(householdActions.addNewTaskToHouseHold(response.data));
        handleNotification("Task created.", dispatch);
        dispatch(uiActions.resetFormSubmit());
      })
      .catch(function (error) {
        handleErrorResponse(error, dispatch);
        dispatch(uiActions.resetFormSubmit());
      });
  };
};

export const updateTask = (task) => {
  return async (dispatch) => {
    const updateTask = async () => {
      return await taskService.updateTask(task);
    };

    updateTask()
      .then(function (response) {
        dispatch(uiActions.showModal());
        dispatch(householdActions.updateTaskToHouseHold(response.data));
        handleNotification("Task updated.", dispatch);
        dispatch(householdActions.setIsTaskEdit(false));
        dispatch(uiActions.resetFormSubmit());
      })
      .catch(function (error) {
        handleErrorResponse(error, dispatch);
        dispatch(householdActions.setIsTaskEdit(false));
        dispatch(uiActions.resetFormSubmit());
      });
  };
};

export const deleteTask = (householdId, taskId) => {
  return async (dispatch) => {
    const deleteTaskToHousehold = async () => {
      return await taskService.deleteTask(taskId);
    };

    deleteTaskToHousehold()
      .then(function (response) {
        dispatch(
          householdActions.deleteTaskToHousehold({
            householdId: householdId,
            taskId: taskId,
          })
        );
        handleNotification("Task deleted.", dispatch);
      })
      .catch(function (error) {
        console.log(error);
        handleErrorResponse(error, dispatch);
      });
  };
};

export const updateStatus = (householdId, taskId, status) => {
  return async (dispatch) => {
    const updateTaskStatus = async () => {
      return await taskService.updateStatus(taskId, status);
    };

    updateTaskStatus()
      .then(function (response) {
        dispatch(
          householdActions.udpateTaskStatus({
            householdId: householdId,
            taskId: taskId,
            status: response.data.status,
          })
        );
        handleNotification("Status updated.", dispatch);
      })
      .catch(function (error) {
        handleErrorResponse(error, dispatch);
      });
  };
};

const handleErrorResponse = (error, dispatch) => {
  console.log(error);
  
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
