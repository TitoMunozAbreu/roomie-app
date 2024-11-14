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
        dispatch(householdActions.addNewTaskToHouseHold(response.data));
        handleNotification("Task created.", dispatch);
      })
      .catch(function (error) {
        handleErrorResponse(error, dispatch);
      });
  };
};


export const updateTask = (householdId, task) => {
    return async (dispatch) => {
      const updateTask = async () => {
        return await taskService.updateTask(householdId,task);
      };
  
      updateTask()
        .then(function (response) {
          dispatch(householdActions.updateTaskToHouseHold(response.data));
          handleNotification("Task updated.", dispatch);
        })
        .catch(function (error) {
          handleErrorResponse(error, dispatch);
        });
    };
  };

const handleErrorResponse = (error, dispatch) => {
  dispatch(
    uiActions.showNotification({
      type: "error",
      message: error.response.data,
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
