import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom';

const Navbar = ({isAdmin}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const navigate = useNavigate();
  useEffect(() => {
    const loggedIn = localStorage.getItem('token');
    if (loggedIn) {
      setIsLoggedIn(true);
    }
    else {
      setIsLoggedIn(false);
    }
  });
  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  const handleLoginRedirect = () => {
    navigate('/login');
  };

  return (
    <nav className="bg-white shadow-md w-full">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16 items-center">
          <Link to="/" className="text-xl font-semibold text-gray-800">
            Dine Connect
          </Link>
          <div className="flex items-center justify-center space-x-4">
            {!isAdmin && <input
              type="search"
              placeholder="Search..."
              className="hidden sm:block w-full max-w-md px-4 py-2 border border-gray-200 rounded-full focus:outline-none focus:ring-1 focus:ring-gray-400"
            />}
            <div className="hidden md:flex space-x-2">
              {!isAdmin && <><a href="#home" className="text-gray-600 hover:text-black hover:bg-black/10 px-2 py-1 rounded">User</a>
              <a href="#about" className="text-gray-600 hover:text-black hover:bg-black/10 px-2 py-1 rounded">Owner</a>
             </> }{isLoggedIn ? (
                <button
                  onClick={handleLogout}
                  className="text-gray-600 hover:text-black hover:bg-black/10 px-2 py-1 rounded"
                >
                  Logout
                </button>
              ) : (
                <button
                  onClick={handleLoginRedirect}
                  className="text-gray-600 hover:text-black hover:bg-black/10 px-2 py-1 rounded"
                >
                  Login
                </button>
              )}
            </div>
          </div>
          <div className="md:hidden">
            <button onClick={() => setIsOpen(!isOpen)} className="text-gray-700 focus:outline-none">
              ☰
            </button>
          </div>
        </div>
      </div>

      {isOpen && (
        <div className="md:hidden bg-white px-4 pt-2 pb-4 space-y-2">
          <a href="#home" className="block text-gray-600 hover:text-black">Home</a>
          <a href="#about" className="block text-gray-600 hover:text-black">About</a>
          {isLoggedIn ? (
            <button onClick={handleLogout} className="block text-left w-full text-gray-600 hover:text-black">Logout</button>
          ) : (
            <button onClick={handleLoginRedirect} className="block text-left w-full text-gray-600 hover:text-black">Login</button>
          )}
        </div>
      )}
    </nav>
  );
};

export default Navbar;
