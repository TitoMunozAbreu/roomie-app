import { createSlice } from "@reduxjs/toolkit";
import { notification } from "antd";

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
  notification: null,
  errorMessage: null,
  submitForm: false,
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
    showNotification(state, action) {
      state.notification = {
        type: action.payload.type,
        message: action.payload.message,
      };
    },
    clearNotification(state) {
      state.notification = {
        type: "",
        message: "",
      };
    },
    updateErrorMessage(state, action) {
      state.errorMessage = action.payload;
    },
    handleSubmitForm(state) {
      state.submitForm = true;
    },
    resetFormSubmit(state) {
      state.submitForm = false;
    },
  },
});

export const uiActions = uiSlice.actions;

export default uiSlice.reducer;
