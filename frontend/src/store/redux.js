import { configureStore } from "@reduxjs/toolkit";
import uiReducer from "./reducers/ui-slice";
import userReducer from "./reducers/user-slice";

const store = configureStore({
  reducer: {
    ui: uiReducer,
    user: userReducer
  },
});

export default store;
