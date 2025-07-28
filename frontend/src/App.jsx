import {
  BrowserRouter,
  Routes,
  Route,
  useLocation,
  Navigate,
  useNavigate,
} from 'react-router-dom';
import './App.css';
import Home from './pages/home';
import NavBar from './components/navBar';
import Restaurant from './pages/restaurant';
import Login from './pages/login';
import Signup from './pages/signUp';
import NotFound from './pages/notFound';
import AdminDashboard from './pages/AdminDashboard';
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer } from 'react-toastify';
import { useEffect } from 'react';
import { isTokenExpired } from './utils';
import { getRole } from '../constants';

const ProtectedRoute = ({ children, allowedRoles = [] }) => {
  const token = localStorage.getItem('token');
  const role = getRole();

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles.length > 0 && !allowedRoles.includes(role)) {
    // If role not allowed, redirect based on role
    return role === 'ADMIN' ? (
      <Navigate to="/admin" replace />
    ) : (
      <Navigate to="/" replace />
    );
  }

  return children;
};

function App() {
  return (
    <BrowserRouter>
      <Layout />
    </BrowserRouter>
  );
}

function Layout() {
  const location = useLocation();
  const navigate = useNavigate();
  const path = location.pathname;
  const role = getRole();

  const hideNav = role === 'ADMIN' || !(path === '/' || path.startsWith('/restaurant'));

  useEffect(() => {
    if (isTokenExpired()) {
      localStorage.removeItem('token');
      if (path !== '/login' && path !== '/signup') {
        navigate('/login');
      }
    }
  }, [path, navigate]);

  return (
    <>
      {!hideNav && <NavBar />}
      <ToastContainer position="top-right" autoClose={3000} />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />

        {/* 👇 USER Routes */}
        <Route
          path="/"
          element={
            <ProtectedRoute allowedRoles={['USER']}>
              <Home />
            </ProtectedRoute>
          }
        />
        <Route
          path="/restaurant/:id"
          element={
            <ProtectedRoute allowedRoles={['USER']}>
              <Restaurant />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin"
          element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <AdminDashboard />
            </ProtectedRoute>
          }
        />

        <Route path="*" element={<NotFound />} />
      </Routes>
    </>
  );
}

export default App;
