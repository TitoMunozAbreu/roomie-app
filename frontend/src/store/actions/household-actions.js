import { householdService } from "../../Service/household-service";
import { uiActions } from "../reducers/ui-slice";
import { householdActions } from "../reducers/household-slice";
import { message } from "antd";

export const updateHouseholdName = (householdId, householdName) => {
  return async (dispatch) => {
    const putHouseholdName = async () => {
      return await householdService.updateHouseholdName(
        householdId,
        householdName
      );
    };

    putHouseholdName()
      .then(function (response) {
        dispatch(householdActions.updateHouseholdName(response.data));
        dispatch(
          uiActions.showNotification({
            type: "success",
            message: "Household name updated!.",
          })
        );
      })
      .catch(function (error) {
        hanldeErrorResponse(error);
      });
  };
};

export const deleteHouseholdMember = (householdId, memberEmail) => {
  return async (dispatch) => {
    const deleteMember = async () => {
      return await householdService.deleteHouseholdMember(
        householdId,
        memberEmail
      );
    };

    deleteMember()
      .then(function (response) {
        dispatch(householdActions.updateHouseholdMembers(response.data));
        dispatch(
          uiActions.showNotification({
            type: "success",
            message: "Member deleted.",
          })
        );
      })
      .catch(function (error) {
        hanldeErrorResponse(error);
      });
  };
};

const hanldeErrorResponse = (error) => {
  dispatch(
    uiActions.showNotification({
      type: "error",
      message: error.response.data,
    })
  );
};
