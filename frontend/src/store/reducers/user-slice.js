import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  user: {
    id: null,
    firstName: null,
    lastName: null,
    email: null,
    taskPreferences: null,
    taskHistories: null,
    availabilities: null,
  },
};

const userSlice = createSlice({
  name: "user",
  initialState: initialState,
  reducers: {
    updatedUser(state, action) {
      state.user.id = action.payload.id;
      state.user.firstName = action.payload.firstName;
      state.user.lastName = action.payload.lastName;
      state.user.email = action.payload.email;
      state.user.taskPreferences = action.payload.taskPreferences;
      state.user.availabilities = action.payload.availabilities;
      state.user.taskHistories = action.payload.taskHistories;
    },
    clearUser(state) {
      state.user = {
        id: null,
        firstName: null,
        lastName: null,
        email: null,
        taskPreferences: null,
        taskHistories: null,
        availabilities: null,
      };
    },
    updatePreferences(state, action) {
      state.user.taskPreferences = action.payload.taskPreferences;
    },
    updateAvailabilities(state, action) {
      state.user.availabilities = action.payload.availabilities;
    },
    updateHistory(state, action) {
      state.user.taskHistories = action.payload.taskHistories;
    },
  },
});

export const userActions = userSlice.actions;

export default userSlice.reducer;
