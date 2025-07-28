import { Link, useNavigate } from 'react-router-dom';

export default function NotFound() {
  const navigate = useNavigate();
  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-white text-gray-800 p-6">
      <Link to="/" className="text-xl font-semibold text-gray-800">
        Dine Connect
      </Link>
      <h1 className="text-6xl font-bold text-red-500 mb-4">404</h1>
      <p className="text-2xl font-semibold mb-2">Page Not Found</p>
      <p className="text-md text-gray-600 mb-6">
        The page you're looking for doesn’t exist or has been moved.
      </p>
      <button
        type="submit"
        className="px-4 py-2 bg-black text-white rounded hover:bg-gray-800"
        onClick={()=>{navigate("/")}}
      >
        Go to Homepage
      </button>
    </div>
  );
}
