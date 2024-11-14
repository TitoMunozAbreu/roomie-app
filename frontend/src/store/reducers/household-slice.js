import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  households: null,
  selectedHousehold: null,
  selectedTask: null,
  isTaskEdit: false
};

const householdSlice = createSlice({
  name: "household",
  initialState: initialState,
  reducers: {
    setHouseholds(state, action) {
      state.households = action.payload;
      state.selectedHousehold = state.households[0];
    },
    updateHouseholdName(state, action) {
      if (state.households) {
        const household = state.households.find(
          (h) => h.id === action.payload.id
        );
        if (household) {
          household.householdName = action.payload.householdName;
        }
      }
    },
    updateHouseholdMembers(state, action) {
      if (state.households) {
        const household = state.households.find(
          (h) => h.id === action.payload.id
        );
        if (household) {
          household.members = action.payload.members;
        }
      }
    },
    addNewHousehold(state, action) {
      if (state.households) {
        state.households.push(action.payload);
      } else {
        state.households = [action.payload];
      }
    },
    deleteHouseholdById(state, action) {
      const index = state.households.findIndex((h) => h.id === action.payload);
      if (index !== -1) {
        state.households.splice(index, 1);
      }
      if (state.households.length === 0) {
        state.households = [];
      }
    },
    updateSelecteHousehold(state, action) {
      state.selectedHousehold = action.payload;
    },
    setTask(state, action) {
      state.selectedTask = action.payload;
    },
    setIsTaskEdit(state) {
      state.isTaskEdit = !state.isTaskEdit;
    },
    addNewTaskToHouseHold(state, action) {
      const household = state.households.find(
        (h) => h.id === action.payload.householdId
      );
      household.tasks.push(action.payload);
    },
    updateTaskToHouseHold(state, action) {
      const householdIndex = state.households.findIndex(
        (h) => h.id === action.payload.householdId
      );

      if (householdIndex !== -1) {
        const taskIndex = state.households[householdIndex].tasks.findIndex(
          (t) => t.id === id
        );
      }

      if (taskIndex !== -1) {
        state.households[householdIndex].tasks[taskIndex] = updatedTask;
      } 
    },
  },
});

export const householdActions = householdSlice.actions;

export default householdSlice.reducer;
