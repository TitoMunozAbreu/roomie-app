import { createBrowserRouter } from "react-router-dom";
import AppLayout from "./components/Layout/Layout";
import PrivateRoute from "./helpers/private-route";
import Home from "./pages/Home/Home.jsx";
import Profile from "./pages/Profile/Profile.jsx";
import Dashboard from "./pages/Dashboard/Dashboard.jsx";
import Groups from "./pages/Groups/Groups.jsx";
import AcceptInvitation from "./pages/Invitation/AcceptInvitation.jsx";

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
            <Dashboard />
          </PrivateRoute>
        ),
      },
      {
        path: "/groups",
        element: (
          <PrivateRoute>
            <Groups />
          </PrivateRoute>
        ),
      },
      // {
      //   path: "/notifications",
      //   element: (
      //     <PrivateRoute>
      //       <h2>notifications</h2>
      //     </PrivateRoute>
      //   ),
      // },
      {
        path: "/profile",
        element: (
          <PrivateRoute>
            <Profile />
          </PrivateRoute>
        ),
      },
      {
        path: "/household/:householdId/acceptInvitation",
        element: <AcceptInvitation />,
      },
    ],
  },
]);

export default router;
