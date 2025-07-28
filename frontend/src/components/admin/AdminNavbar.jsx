import React from 'react';
import { useNavigate } from 'react-router-dom';

const AdminNavbar = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <nav className="bg-white shadow-md px-6 py-3 flex justify-between items-center border-b">
      <h1 className="text-xl font-semibold text-gray-800">DineCONNECT Admin</h1>
      <button
        onClick={handleLogout}
        className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700"
      >
        Logout
      </button>
    </nav>
  );
};

export default AdminNavbar;
