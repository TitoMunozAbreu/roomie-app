import { householdService } from "../../Service/household-service";
import { uiActions } from "../reducers/ui-slice";
import { householdActions } from "../reducers/household-slice";

export const getHouseholds = () => {
  return async (dispatch) => {
    const fetchHouseholds = async () => {
      return await householdService.getHouseholds();
    };

    fetchHouseholds()
      .then(function (response) {
        dispatch(householdActions.setHouseholds(response.data));
        dispatch(uiActions.updateErrorMessage(null));
        dispatch(uiActions.showLoading());
      })
      .catch(function (error) {
        dispatch(uiActions.updateErrorMessage(error.response.data));
        dispatch(uiActions.showLoading());
      });
  };
};

export const createHousehold = (householdName, userId, email) => {
  return async (dispatch) => {
    const addNewHousehold = async () => {
      return await householdService.createHousehold(
        householdName,
        userId,
        email
      );
    };

    addNewHousehold()
      .then(function (response) {
        dispatch(householdActions.addNewHousehold(response.data));
        dispatch(uiActions.updateErrorMessage(null));
        handleNotification("New household added.", dispatch);
      })
      .catch(function (error) {
        handleErrorResponse(error, dispatch);
      });
  };
};

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
        handleNotification("Household name updated.", dispatch);
      })
      .catch(function (error) {
        handleErrorResponse(error, dispatch);
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
        handleNotification("Member deleted.", dispatch);
      })
      .catch(function (error) {
        handleErrorResponse(error, dispatch);
      });
  };
};

export const updateHouseholdMembers = (householdId, memberEmails) => {
  return async (dispatch) => {
    const updateMembers = async () => {
      return await householdService.updateHouseholdMembers(
        householdId,
        memberEmails
      );
    };

    updateMembers()
      .then(function (response) {
        dispatch(householdActions.updateHouseholdMembers(response.data));
        handleNotification("New member added.", dispatch);
      })
      .catch(function (error) {
        handleErrorResponse(error, dispatch);
      });
  };
};

export const deleteHousehold = (householdId) => {
  return async (dispatch) => {
    const deleteHouseholdById = async () => {
      return await householdService.deleteHousehold(householdId);
    };

    deleteHouseholdById()
      .then(function (response) {
        dispatch(householdActions.deleteHouseholdById(householdId));
        handleNotification(response.data, dispatch);
      })
      .catch(function (error) {
        handleErrorResponse(error, dispatch);
      });
  };
};

export const memberInvitation = (householdId, isAccepted) => {
  return async (dispatch) => {
    const memberInvitation = async () => {
      return await householdService.memberInvitation(householdId, isAccepted);
    };

    memberInvitation()
      .then(function (response) {
        handleNotification(response.data, dispatch);
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
