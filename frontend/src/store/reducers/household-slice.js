import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  households: null,
};

const householdSlice = createSlice({
  name: "household",
  initialState: initialState,
  reducers: {
    setHouseholds(state, action) {
      state.households = action.payload;
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
  },
});

export const householdActions = householdSlice.actions;

export default householdSlice.reducer;
