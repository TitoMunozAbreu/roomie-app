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
  },
});

export const householdActions = householdSlice.actions;

export default householdSlice.reducer;