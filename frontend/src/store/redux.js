import { configureStore } from "@reduxjs/toolkit";
import uiReducer from "./reducers/ui-slice";
import userReducer from "./reducers/user-slice";
import householdReducer from "./reducers/household-slice"

const store = configureStore({
  reducer: {
    ui: uiReducer,
    user: userReducer,
    households: householdReducer
  },
});

export default store;
