import { createBrowserRouter } from "react-router-dom";
import AppLayout from "./components/Layout/Layout";
import PrivateRoute from "./helpers/private-route";
import Home from "./pages/Home/Home.jsx";
const router = createBrowserRouter([
  {
    path: "/",
    element: <AppLayout />,
    children: [
      { path: "/", element: <Home /> },
      { path: "/home", element: <Home /> },
      {
        path: "/dashboard",
        element: (
          <PrivateRoute>
            <h2>dashboard</h2>
          </PrivateRoute>
        ),
      },
      {
        path: "/notifications",
        element: (
          <PrivateRoute>
            <h2>notifications</h2>
          </PrivateRoute>
        ),
      },
      { path: "/profile", element: <h2>profile</h2> },
    ],
  },
]);

export default router;
