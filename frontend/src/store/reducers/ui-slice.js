import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  profile: {
    isEditPreference: false,
    isEditAvailability: false
  }
};

const uiSlice = createSlice({
  name: "ui",
  initialState: initialState,
  reducers: {
    isBtnEditPreference(state, action) {
      state.profile.isEditPreference = action.payload;
    },
    isBtnEditAvailability(state, action) {
      state.profile.isEditAvailability = action.payload;
    },
  },
});

export const uiActions = uiSlice.actions;

export default uiSlice.reducer;
