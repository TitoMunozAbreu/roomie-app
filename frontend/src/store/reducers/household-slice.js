import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  households: null,
  selectedHousehold: null,
  selectedTask: null,
  isTaskEdit: false,
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
        state.selectedHousehold = state.households[0];
      }
    },
    deleteHouseholdById(state, action) {
      const index = state.households.findIndex((h) => h.id === action.payload);
      if (index !== -1) {
        state.households.splice(index, 1);
      }
      if (state.households.length === 0) {
        state.households = [];
        state.selectedHousehold = null;
      }
    },
    updateSelecteHousehold(state, action) {
      state.selectedHousehold = action.payload;
    },
    setTask(state, action) {
      state.selectedTask = action.payload;
    },
    setIsTaskEdit(state, action) {
      state.isTaskEdit = action.payload;
    },
    addNewTaskToHouseHold(state, action) {
      const household = state.households.find(
        (h) => h.id === action.payload.householdId
      );
      
      if (household === null || household.tasks?.length === 0) {
        household.tasks = [];
      }
      household.tasks.push(action.payload);
    },
    updateTaskToHouseHold(state, action) {
      const householdIndex = state.households.findIndex(
        (h) => h.id === action.payload.householdId
      );

      if (householdIndex !== -1) {
        const taskIndex = state.households[householdIndex].tasks.findIndex(
          (t) => t.id === action.payload.id
        );

        if (taskIndex !== -1) {
          state.households[householdIndex].tasks[taskIndex] = action.payload;
        } else {
          state.households[householdIndex].tasks = [];
          state.households[householdIndex].tasks.push(action.payload);
        }
      }
    },
    udpateTaskStatus(state, action) {
      console.log(action.payload);

      const householdIndex = state.households.findIndex(
        (h) => h.id === action.payload.householdId
      );

      if (householdIndex !== -1) {
        const taskIndex = state.households[householdIndex].tasks.findIndex(
          (t) => t.id === action.payload.taskId
        );

        if (taskIndex !== -1) {
          state.households[householdIndex].tasks[taskIndex].status =
            action.payload.status;
        }
      }
    },
    deleteTaskToHousehold(state, action) {
      const householdIndex = state.households.findIndex(
        (h) => h.id === action.payload.householdId
      );

      if (householdIndex !== -1) {
        const taskIndex = state.households[householdIndex].tasks.findIndex(
          (t) => t.id === action.payload.taskId
        );

        if (taskIndex !== -1) {
          state.households[householdIndex].tasks.splice(taskIndex, 1);

          if (state.households[householdIndex].tasks.length === 0) {
            state.households[householdIndex].tasks = [];
          }
        }
      }
    },
  },
});

export const householdActions = householdSlice.actions;

export default householdSlice.reducer;
