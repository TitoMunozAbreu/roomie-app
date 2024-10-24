import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  profile: {
    isEditPreference: false,
    isEditAvailability: false,
    modal: {
      isOpen: false,
      title: "",
      type: "",
    },
    isLoading: false,
  },
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
    showModal(state) {
      state.profile.modal.isOpen = !state.profile.modal.isOpen;
    },
    modalData(state, action) {
      state.profile.modal.title = action.payload.title;
      state.profile.modal.type = action.payload.type;
    },
    showLoading(state) {
      state.profile.isLoading = !state.profile.isLoading;
    },
  },
});

export const uiActions = uiSlice.actions;

export default uiSlice.reducer;
